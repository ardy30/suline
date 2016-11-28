package boeren.com.appsuline.app.bmedical.appsuline;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taidoc.pclinklibrary.android.bluetooth.util.BluetoothUtil;
import com.taidoc.pclinklibrary.connection.AndroidBluetoothConnection;
import com.taidoc.pclinklibrary.connection.PL2303Connection;
import com.taidoc.pclinklibrary.connection.util.ConnectionManager;
import com.taidoc.pclinklibrary.constant.PCLinkLibraryConstant;
import com.taidoc.pclinklibrary.constant.PCLinkLibraryEnum;
import com.taidoc.pclinklibrary.exceptions.CommunicationTimeoutException;
import com.taidoc.pclinklibrary.exceptions.ExceedRetryTimesException;
import com.taidoc.pclinklibrary.exceptions.NotConnectSerialPortException;
import com.taidoc.pclinklibrary.exceptions.NotSupportMeterException;
import com.taidoc.pclinklibrary.interfaces.getStorageDataRecordInterface;
import com.taidoc.pclinklibrary.meter.AbstractMeter;
import com.taidoc.pclinklibrary.meter.TD4283;
import com.taidoc.pclinklibrary.meter.record.AbstractRecord;
import com.taidoc.pclinklibrary.meter.record.BloodGlucoseRecord;
import com.taidoc.pclinklibrary.meter.record.BloodPressureRecord;
import com.taidoc.pclinklibrary.meter.record.SpO2Record;
import com.taidoc.pclinklibrary.meter.record.TemperatureRecord;
import com.taidoc.pclinklibrary.meter.record.WeightScaleRecord;
import com.taidoc.pclinklibrary.meter.util.MeterManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import boeren.com.appsuline.app.bmedical.appsuline.constant.PCLinkLibraryDemoConstant;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.fragments.BluetoothFragment;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Constants;
import boeren.com.appsuline.app.bmedical.appsuline.utils.GuiUtils;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Utilities;

/**
 * Created by admin on 3/8/2015.
 */
public class PCLinkLibraryCommuTestActivity extends ListActivity  {
    // Message types sent from the meterCommuHandler Handler
    public static final int MESSAGE_STATE_CONNECTING = 1;
    public static final int MESSAGE_STATE_CONNECT_FAIL = 2;
    public static final int MESSAGE_STATE_CONNECT_DONE = 3;
    public static final int MESSAGE_STATE_CONNECT_NONE = 4;
    public static final int MESSAGE_STATE_CONNECT_METER_SUCCESS = 5;
    public static final int MESSAGE_STATE_CHECK_METER_INFORMATION = 6;
    public static final int MESSAGE_STATE_CHECK_METER_BT_DISTENCE = 7;
    public static final int MESSAGE_STATE_CHECK_METER_BT_DISTENCE_FAIL = 8;
    public static final int MESSAGE_STATE_NOT_SUPPORT_METER = 9;
    public static final int MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT = 10;
    public static final int MESSAGE_STATE_SCANED_DEVICE = 11;
    private Errorcallbacks mCallbacks;
    private int bgValue=0;
    CheckBox cb_selall =null;
    private double mgValue =0.0;
    String measurementDate;
    ListView  lvBG= null;
    User activeUser = BaseController.getInstance().getDbManager(this).getUserTable().getActiveUser();

    public interface Errorcallbacks {
        public void onConnectionError();
    }
    // Tag and Debug flag
    private static final boolean DEBUG = true;
    private static final String TAG = "PCLinkLibraryCommuTestActivity";

    // Views
    private ProgressDialog mProcessDialog = null;

    private volatile Thread mBACmdThread;

    private AndroidBluetoothConnection.LeConnectedListener mLeConnectedListener = new AndroidBluetoothConnection.LeConnectedListener() {

        @Override
        public void onConnectionStateChange_Disconnect(BluetoothGatt gatt,
                                                       int status, int newState) {
            mConnection.LeDisconnect();
            showAlertDialog(R.string.not_support_meter, true);
        }

        @SuppressLint("NewApi")
        @Override
        public void onDescriptorWrite_Complete(BluetoothGatt gatt,
                                               BluetoothGattDescriptor descriptor, int status) {
            mConnection.LeConnected(gatt.getDevice());
        }

        @Override
        public void onCharacteristicChanged_Notify(BluetoothGatt gatt,
                                                   BluetoothGattCharacteristic characteristic) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();

                    try {
                        mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                    }
                    catch (Exception e) {
                        if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                            meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT);
                        }
                        else {
                            meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                        }
                    }

                    PCLinkLibraryCommuTestActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dimissProcessDialog();
                            if (mTaiDocMeter == null) {
                                //throw new NotSupportMeterException();
                                meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                            }
                        }
                    });

                    Looper.loop();
                }
            }).start();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // TODO Auto-generated method stub

        }
    };

    // Handlers
    // The Handler that gets information back from the android bluetooth connection
    private final Handler mBTConnectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case PCLinkLibraryConstant.MESSAGE_STATE_CHANGE:
                        if (DEBUG) {
                            Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                        } /* end of if */
                        switch (msg.arg1) {
                            case AndroidBluetoothConnection.STATE_CONNECTED_BY_LISTEN_MODE:
                                mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                                dimissProcessDialog();
                                if (mTaiDocMeter == null) {
                                    throw new NotSupportMeterException();
                                }/* end of if */
                                break;
                            case AndroidBluetoothConnection.STATE_CONNECTING:

                                break;
                            case AndroidBluetoothConnection.STATE_SCANED_DEVICE:
                                meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_SCANED_DEVICE);
                                break;
                            case AndroidBluetoothConnection.STATE_LISTEN:

                                break;
                            case AndroidBluetoothConnection.STATE_NONE:

                                break;
                        } /* end of switch */
                        break;
                    case PCLinkLibraryConstant.MESSAGE_TOAST:

                        break;
                    default:
                        break;
                } /* end of switch */
            } catch (NotSupportMeterException e) {
                Log.e(TAG, "not support meter", e);
                showAlertDialog(R.string.not_support_meter, true);
            } /* end of try-catch */
        }
    };

    /**
     * BT Transfer type, Type I, Type II or PL2303
     */
    private String mBtTransferType;

    /**
     * PL2303 BaudRate
     */
    private String mBaudRate;

    /**
     * Command array in list view title
     */
    private String[] mCommandTitles = {
             "Laatste meting importeren","Alle metingen importeren",
            "Synchroniseer meter datum", "Gegevens verwijderen", "Meter afsluiten"};

    /**
     * Android BT connection
     */
    private AndroidBluetoothConnection mConnection;

    /**
     * PL2303 connection
     */
    private PL2303Connection mPL2303Connection;
    private BluetoothFragment bluetoothFragment;

    private boolean mBLEMode;
    private boolean mKNVMode;

    private getStorageDataRecordInterface getKNVRecordHandler = new getStorageDataRecordInterface() {

        @Override
        public void onFinish(WeightScaleRecord record) {
            if (record != null) {

            }
        }
    };

    private final Handler meterCommuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CONNECTING:
                    mProcessDialog = ProgressDialog.show(PCLinkLibraryCommuTestActivity.this, null,
                            getString(R.string.connection_meter_and_get_result), true);
                    mProcessDialog.setCancelable(true);
                    break;
                case MESSAGE_STATE_SCANED_DEVICE:

                    final BluetoothDevice device = BluetoothUtil.getPairedDevice(mConnection.getConnectedDeviceAddress());
                    // Attempt to connect to the device
                    mConnection.LeConnect(getApplicationContext(), device);

                    long startConnectTime = System.currentTimeMillis();
                    while (mConnection.getState() != AndroidBluetoothConnection.STATE_CONNECTED) {
                        try {
                            Thread.sleep(500);
                        }
                        catch (Exception e) {
                            showAlertDialog(R.string.check_meter_is_on, true);
                        }
                        long conntectTime = System.currentTimeMillis();
                        if ((conntectTime - startConnectTime) > (AndroidBluetoothConnection.BT_CONNECT_TIMEOUT)) {
                            mConnection.LeDisconnect();
                           // throw new CommunicationTimeoutException();
                            showAlertDialog(R.string.not_support_meter, true);
                            return;
                        }
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            mConnection.LeConnected(device);
                            try {
                                mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                            }
                            catch (ExceedRetryTimesException e) {
                                if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT);
                                }
                                else {
                                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                                }
                            }

                            PCLinkLibraryCommuTestActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dimissProcessDialog();
                                    if (mTaiDocMeter == null) {
                                        //throw new NotSupportMeterException();
                                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                                    }
                                }
                            });

                            Looper.loop();
                        }
                    }).start();
                    break;
                case MESSAGE_STATE_CONNECT_DONE:
                    dimissProcessDialog();
                    break;
                case MESSAGE_STATE_CONNECT_FAIL:
                    dimissProcessDialog();
                    showAlertDialog(R.string.connect_meter_fail, true);
                    //onBackPressed();

                    break;
                case MESSAGE_STATE_CONNECT_NONE:
                    //showAlertDialog(PCLinkLibraryCommuTestActivity.this, "00000");
                    dimissProcessDialog();
                    if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(PCLinkLibraryDemoConstant.FromPL2303, true);
                        GuiUtils.goToSpecifiedActivity(PCLinkLibraryCommuTestActivity.this,
                                PCLinkLibraryDemoActivity.class, bundle);
                    }
                    else {
                        GuiUtils.goToPCLinkLibraryHomeActivity(PCLinkLibraryCommuTestActivity.this);
                    }
                    break;
                case MESSAGE_STATE_CONNECT_METER_SUCCESS:
                    showAlertDialog(R.string.connect_meter_success, false);
                    break;
                case MESSAGE_STATE_CHECK_METER_BT_DISTENCE:
                    ProgressDialog baCmdDialog = new ProgressDialog(
                            PCLinkLibraryCommuTestActivity.this);
                    baCmdDialog.setCancelable(false);
                    baCmdDialog.setMessage("send ba command");
                    baCmdDialog.setButton(DialogInterface.BUTTON_POSITIVE, "cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Use either finish() or return() to either close the activity
                                    // or just
                                    // the dialog
                                    dialog.dismiss();
                                    mBACmdThread = null;
                                    return;
                                }
                            });
                    baCmdDialog.show();
                    break;
                case MESSAGE_STATE_CHECK_METER_BT_DISTENCE_FAIL:
                    mBACmdThread = null;
                    showAlertDialog(R.string.check_bt_fail, true);
                    break;
                case MESSAGE_STATE_NOT_SUPPORT_METER:
                    showAlertDialog(R.string.not_support_meter, true);
                    break;
                case MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT:
                    showAlertDialog(R.string.not_connect_serial_port, true);
                    break;
            } /* end of switch */
        }
    };

    private String mMacAddress;
    private AbstractMeter mTaiDocMeter = null;
    private boolean mTurnOffMeterClick = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAdapter();
        getSharedPreferencesSettings();
        bluetoothFragment = MainActivity.getBluetoothFragment();
        if (!(bluetoothFragment instanceof Errorcallbacks)) {
            throw new IllegalStateException(
                    "Fragment must implement the callbacks.");
        }

        mCallbacks = (Errorcallbacks) bluetoothFragment;
    }

    private void updatePairedList() {
        Map<String, String> addrs = new HashMap<String, String>();
        String addrKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_ADDR_ + String.valueOf(0);
        addrs.put(addrKey, mMacAddress);
        mConnection.updatePairedList(addrs, 1);
    }
    private Double round2(Double val) {
        return new BigDecimal(val.toString()).setScale(1  , RoundingMode.HALF_EVEN).doubleValue();
    }
    /**
     * Connect Meter
     */
    private void connectMeter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECTING);
                    if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                        // Attempt to connect to the device

                        mPL2303Connection.connect();

                        long startConnectTime = System.currentTimeMillis();
                        while (mPL2303Connection.getState() != PL2303Connection.STATE_CONNECTED) {
                            long conntectTime = System.currentTimeMillis();
                            if ((conntectTime - startConnectTime) > (PL2303Connection.PL2303_CONNECT_TIMEOUT)) {
                                throw new CommunicationTimeoutException();
                            }
                        }

                        mTaiDocMeter = MeterManager.detectConnectedMeter(mPL2303Connection);
                        if (mTaiDocMeter == null) {
                            throw new NotSupportMeterException();
                        }
                    }
                    else {

                        if (PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_ONE.equals(mBtTransferType)) {

                            BluetoothDevice device = BluetoothUtil.getPairedDevice(mMacAddress);
                            // Attempt to connect to the device
                            mConnection.connect(device);
                            // time out
                            long startConnectTime = System.currentTimeMillis();
                            while (mConnection.getState() != AndroidBluetoothConnection.STATE_CONNECTED) {
                                try {
                                    Thread.sleep(500);
                                }
                                catch (InterruptedException e) {
                                }
                                long conntectTime = System.currentTimeMillis();
                                if ((conntectTime - startConnectTime) > (AndroidBluetoothConnection.BT_CONNECT_TIMEOUT)) {
                                    throw new CommunicationTimeoutException();
                                }
                            }

                            mTaiDocMeter = MeterManager.detectConnectedMeter(mConnection);
                            if (mTaiDocMeter == null) {
                                throw new NotSupportMeterException();
                            }
                        } else {
                            if (mBLEMode) {
                                updatePairedList();
                                mConnection.setLeConnectedListener(mLeConnectedListener);

                                if (mConnection.getState() == AndroidBluetoothConnection.STATE_NONE) {
                                    // Start the Android Bluetooth connection services to listen mode
                                    mConnection.LeListen();

                                    if (DEBUG) {
                                        Log.i(TAG, "into listen mode");
                                    }
                                }
                            }
                            else {
                                // Only if the state is STATE_NONE, do we know that we haven't started
                                // already
                                if (mConnection.getState() == AndroidBluetoothConnection.STATE_NONE) {
                                    // Start the Android Bluetooth connection services to listen mode
                                    mConnection.listen();

                                    if (DEBUG) {
                                        Log.i(TAG, "into listen mode");
                                    }
                                }
                            }
                        }
                    }
                } catch (CommunicationTimeoutException e) {
                    Log.e(TAG, e.getMessage(), e);
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_FAIL);
                } catch (NotSupportMeterException e) {
                    Log.e(TAG, "not support meter", e);
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                } catch (NotConnectSerialPortException e) {
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT);
                } catch (ExceedRetryTimesException e) {
                    if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_CONNECT_SERIAL_PORT);
                    }
                    else {
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_NOT_SUPPORT_METER);
                    }
                } finally {
                    if (PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_ONE.equals(mBtTransferType) ||
                            PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_DONE);
                    }
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * // process dialog
     */
    private void dimissProcessDialog() {
        if (mProcessDialog != null) {
            mProcessDialog.dismiss();
            mProcessDialog = null;
        } /* end of if */
    }
    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    /**
     * Disconnnect Meter
     */
    private void disconnectMeter(final boolean isNeedPowerOff) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mTaiDocMeter!=null){
                Looper.prepare();

                try {
                    if (isNeedPowerOff) {
                        mTaiDocMeter.turnOffMeterOrBluetooth(0);
                    } /* end of if */
                    if (mBLEMode) {
                        mConnection.setLeConnectedListener(null);
                        mConnection.LeDisconnect();
                    }
                    else {
                        mConnection.disconnect();
                        mConnection.LeDisconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                } finally {
                }/* end of try-catch-finally */
                Looper.loop();
            }}
        }).start();

    }

    /**
     *
     */
    private void getSharedPreferencesSettings() {
        SharedPreferences settings = getSharedPreferences(
                PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);
        mMacAddress = settings.getString(PCLinkLibraryDemoConstant.PAIR_METER_MAC_ADDRESS, "");
        mBtTransferType = settings.getString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE, "");
        mBaudRate = settings.getString(PCLinkLibraryDemoConstant.BAUDRATE, "19200");
        mBLEMode = settings.getBoolean(PCLinkLibraryDemoConstant.BLE_MODE, false);
        mKNVMode = settings.getBoolean(PCLinkLibraryDemoConstant.KNV_MODE, false);
    }

    /**
     * setting ListView title
     */
    private void setAdapter() {
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,R.id.textTitle,
                mCommandTitles));
    }

    /**
     *  Android Bluetooth Connection
     */
    private void setupAndroidBluetoothConnection() {
        if (mConnection == null) {
            Log.d(TAG, "setupAndroidBluetoothConnection()");

            try {
                mConnection = ConnectionManager.createAndroidBluetoothConnection(mBTConnectionHandler);
            }
            catch (Exception ee) {
            }

        } /* end of if */
    }

    /**
     *  PL2303 Connection
     */
    private void setupPL2303Connection() {
        if (mPL2303Connection == null) {
            Log.d(TAG, "setupPL2303Connection()");
            // Initialize the PL2303 connection to perform PL2303 connections
            try {
                mPL2303Connection = ConnectionManager.createPL2303Connection(PCLinkLibraryCommuTestActivity.this, mBTConnectionHandler);
                mPL2303Connection.setBaudRate(mBaudRate);
            }
            catch (Exception e) {

            }

        } /* end of if */
    }

    public static void showAlertDialog(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, null).show();
    }

    /**
     * Show no title alert dialog
     *
     * @param messageConntentRStringId
     *            the R string Id of message content
     */
    private void showAlertDialog(int messageConntentRStringId, final boolean hasOnClinkListener) {
        if (!isFinishing()) {
            new AlertDialog.Builder(PCLinkLibraryCommuTestActivity.this)
                    .setMessage(messageConntentRStringId)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (hasOnClinkListener) {
                                // Back to the home menu
                                if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(PCLinkLibraryDemoConstant.FromPL2303, false);
                                    GuiUtils.goToSpecifiedActivity(PCLinkLibraryCommuTestActivity.this,
                                            PCLinkLibraryDemoActivity.class, bundle);

                                } else {
                                    dialogInterface.dismiss();
                                }
                            } /* end of if */
                        }

                        ;
                    }).show();
            if (mTaiDocMeter == null) {
                onBackPressed();
                mCallbacks.onConnectionError();
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final int clickItem = position;
        if (clickItem != 8) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECTING);

                        AlertDialog.Builder builder = null;
                        AlertDialog alertDialog = null;
                        LayoutInflater inflater = (LayoutInflater) PCLinkLibraryCommuTestActivity.this
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = null;
                        // Date Format
                        SimpleDateFormat formatterDate = new SimpleDateFormat("dd-MM-yyyy HH:mm",Locale.getDefault());

                        mTurnOffMeterClick = false;
                        switch (clickItem) {
                            /*case 0:
                                // Get Project Code
                                String projectCode = mTaiDocMeter.getDeviceModel().getProjectCode();
                                layout = inflater.inflate(R.layout.get_project_code,
                                        (ViewGroup) findViewById(R.id.projectCodeLayout));
                                TextView viewProjectCode = (TextView) layout
                                        .findViewById(R.id.projectCode);
                                viewProjectCode.setText(projectCode);
                                break;
                            case 1:
                                // Get Meter Serial Number
                                String serialNumber = mTaiDocMeter.getSerialNumberRecord()
                                        .getSerialNumber();
                                layout = inflater.inflate(R.layout.get_serial_number,
                                        (ViewGroup) findViewById(R.id.serialNumberLayout));
                                TextView viewSerialNumber = (TextView) layout
                                        .findViewById(R.id.serialNumber);
                                viewSerialNumber.setText(serialNumber);
                                break;*/
                            case 0:
                                // Get Latest Measurement Record
                                AbstractRecord record = mTaiDocMeter.getStorageDataRecord(0,
                                        PCLinkLibraryEnum.User.CurrentUser);
                                layout = inflater
                                        .inflate(
                                                R.layout.get_latest_measurement_record,
                                                (ViewGroup) findViewById(R.id.getLastMeasurementRecordLayout));
                                // Views
                                LinearLayout bgLayout = (LinearLayout) layout
                                        .findViewById(R.id.bgLayout);


                                LinearLayout bpLayout = (LinearLayout) layout
                                        .findViewById(R.id.bpLayout);
                                LinearLayout thermometerLayout = (LinearLayout) layout
                                        .findViewById(R.id.thermometerLayout);

                                TextView viewMeasurementDate = (TextView) layout
                                        .findViewById(R.id.measurementDate);
                                TextView viewMeasureType = (TextView) layout
                                        .findViewById(R.id.measurementType);
                                TextView viewBGValue = (TextView) layout.findViewById(R.id.bgValue);
                                TextView viewSysValue = (TextView) layout
                                        .findViewById(R.id.sysValue);
                                TextView viewDiaValue = (TextView) layout
                                        .findViewById(R.id.diaValue);
                                TextView viewPulseValue = (TextView) layout
                                        .findViewById(R.id.pulseValue);
                                TextView viewThermometerValue = (TextView) layout
                                        .findViewById(R.id.thermometerValue);
                                TextView viewIHBValue = (TextView) layout
                                        .findViewById(R.id.ihbValue);

                                // spO2
                                LinearLayout spO2Layout = (LinearLayout) layout
                                        .findViewById(R.id.spO2Layout);
                                TextView viewSpO2Value = (TextView) layout
                                        .findViewById(R.id.spO2Value);
                                TextView viewSpO2PulseValue = (TextView) layout
                                        .findViewById(R.id.spO2PulseValue);

                                // Weight
                                LinearLayout weightLayout = (LinearLayout) layout
                                        .findViewById(R.id.weightScaleLayout);
                                TextView viewAgeValue = (TextView) layout
                                        .findViewById(R.id.ageValue);
                                TextView viewGenderValue = (TextView) layout
                                        .findViewById(R.id.genderValue);
                                TextView viewHeightValue = (TextView) layout
                                        .findViewById(R.id.heightValue);
                                TextView viewWeightValue = (TextView) layout
                                        .findViewById(R.id.weightValue);
                                TextView viewBMIValue = (TextView) layout
                                        .findViewById(R.id.bmiValue);
                                TextView viewBMRValue = (TextView) layout
                                        .findViewById(R.id.bmrValue);
                                TextView viewBFValue = (TextView) layout.findViewById(R.id.bfValue);

                                // Convert value and set views
                                if (record instanceof BloodGlucoseRecord) {
                                    // convert value

                                    measurementDate = formatterDate.format(((BloodGlucoseRecord) record).getMeasureTime());
                                    bgValue = ((BloodGlucoseRecord) record).getGlucoseValue();
                                    mgValue = round2(bgValue / 18.0);
                                    // set views
                                    bgLayout.setVisibility(View.VISIBLE);
                                    bpLayout.setVisibility(View.GONE);
                                    thermometerLayout.setVisibility(View.GONE);
                                    weightLayout.setVisibility(View.GONE);
                                    viewMeasurementDate.setText(measurementDate);
                                    viewMeasureType.setText("BG");
                                    viewBGValue.setText(String.valueOf(mgValue));
                                } else if (record instanceof BloodPressureRecord) {
                                    // Convert value
                                    int sysValue = ((BloodPressureRecord) record)
                                            .getSystolicValue();
                                    int diaValue = ((BloodPressureRecord) record)
                                            .getDiastolicValue();
                                    int pulseValue = ((BloodPressureRecord) record).getPulseValue();
                                    int ihbValue = ((BloodPressureRecord) record).getIHB().getValue();

                                    String measurementDate = formatterDate
                                            .format(((BloodPressureRecord) record).getMeasureTime());
                                    // Set views
                                    bgLayout.setVisibility(View.GONE);
                                    bpLayout.setVisibility(View.VISIBLE);
                                    thermometerLayout.setVisibility(View.GONE);
                                    weightLayout.setVisibility(View.GONE);
                                    spO2Layout.setVisibility(View.GONE);
                                    viewMeasurementDate.setText(measurementDate);
                                    viewMeasureType.setText("BP");
                                    viewSysValue.setText(String.valueOf(sysValue));
                                    viewDiaValue.setText(String.valueOf(diaValue));
                                    viewPulseValue.setText(String.valueOf(pulseValue));
                                    viewIHBValue.setText(getResources().getStringArray(R.array.IHBValue)[ihbValue]);
                                } else if (record instanceof TemperatureRecord) {
                                    // Convert value
                                    double thermometerValue = ((TemperatureRecord) record)
                                            .getObjectTemperatureValue();
                                    String measurementDate = formatterDate
                                            .format(((TemperatureRecord) record).getMeasureTime());
                                    // Set views
                                    bgLayout.setVisibility(View.GONE);
                                    bpLayout.setVisibility(View.GONE);
                                    thermometerLayout.setVisibility(View.VISIBLE);
                                    spO2Layout.setVisibility(View.GONE);
                                    weightLayout.setVisibility(View.GONE);
                                    viewMeasurementDate.setText(measurementDate);
                                    viewMeasureType.setText("Thermometer");
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    viewThermometerValue.setText(df.format(thermometerValue));
                                } else if (record instanceof SpO2Record) {
                                    int spO2Value = ((SpO2Record) record).getSpO2();
                                    int pulseValue = ((SpO2Record) record).getPulse();
                                    String measurementDate = formatterDate
                                            .format(((SpO2Record) record).getMeasureTime());
                                    spO2Layout.setVisibility(View.VISIBLE);
                                    bgLayout.setVisibility(View.GONE);
                                    bpLayout.setVisibility(View.GONE);
                                    thermometerLayout.setVisibility(View.GONE);
                                    weightLayout.setVisibility(View.GONE);
                                    viewMeasurementDate.setText(measurementDate);
                                    viewMeasureType.setText("spO2");
                                    viewSpO2Value.setText(String.valueOf(spO2Value));
                                    viewSpO2PulseValue.setText(String.valueOf(pulseValue));
                                } else if (record instanceof WeightScaleRecord) {
                                    // Convert value
                                    PCLinkLibraryEnum.GenderType genderType = ((WeightScaleRecord) record)
                                            .getGender();
                                    int age = ((WeightScaleRecord) record).getAge();
                                    int height = ((WeightScaleRecord) record).getHeight();
                                    double weight = ((WeightScaleRecord) record).getWeight();
                                    double bmi = ((WeightScaleRecord) record).getBmi();
                                    int bmr = ((WeightScaleRecord) record).getBmr();
                                    double bf = ((WeightScaleRecord) record).getBf();
                                    String measurementDate = formatterDate
                                            .format(((WeightScaleRecord) record).getMeasureTime());
                                    // Set views
                                    bgLayout.setVisibility(View.GONE);
                                    bpLayout.setVisibility(View.GONE);
                                    thermometerLayout.setVisibility(View.GONE);
                                    spO2Layout.setVisibility(View.GONE);
                                    weightLayout.setVisibility(View.VISIBLE);
                                    viewMeasurementDate.setText(measurementDate);
                                    viewMeasureType.setText("Weight Scale");
                                    // DecimalFormat df = new DecimalFormat("#.##");
                                    viewAgeValue.setText("" + age);
                                    viewGenderValue.setText(genderType.toString());
                                    viewHeightValue.setText("" + height);
                                    viewWeightValue.setText("" + weight);
                                    viewBMIValue.setText("" + bmi);
                                    viewBMRValue.setText("" + bmr);
                                    viewBFValue.setText("" + bf);
                                }/* end of if */
                                break;
                            case 1:

                                //




                                // Get Meter Reading List
                                lvBG = new ListView(getApplication());
                                lvBG.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                final ArrayList<String> list = new ArrayList<String>();
                               List<AbstractRecord> records = mTaiDocMeter.getAllStorageDataRecord(PCLinkLibraryEnum.User.CurrentUser);
                                for (AbstractRecord item: records) {

                                    if (item instanceof BloodGlucoseRecord) {
                                        // convert value

                                        measurementDate = formatterDate.format(((BloodGlucoseRecord) item).getMeasureTime());
                                        bgValue = ((BloodGlucoseRecord) item).getGlucoseValue();
                                        mgValue = round2(bgValue / 18.0);

                                        list.add(measurementDate + "  " + Double.toString(mgValue)+"  "+getApplicationContext().getString(R.string.etBloodLevelAmount_hint));

                                       /* System.out.println(records.size());
                                        System.out.println("BG Value "+bgValue);
                                        System.out.println("MG Value " + mgValue);*/



                                    }

                                }
                                // Set views
                                layout = inflater.inflate(R.layout.get_latest_measurement_record, (ViewGroup) findViewById(R.id.getLastMeasurementRecordLayout));
                                LinearLayout allBgLayout = (LinearLayout) layout.findViewById(R.id.allBgLayout);
                                allBgLayout.setVisibility(View.VISIBLE);
                                lvBG  = (ListView) layout.findViewById(R.id.lvBgValue);
                                cb_selall = (CheckBox) layout.findViewById(R.id.chkAll);
                                cb_selall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if(isChecked){
                                            try {
                                                int itemCount = lvBG.getCount();
                                                for(int i=0 ; i < itemCount ; i++){
                                                    lvBG.setItemChecked(i, cb_selall.isChecked());
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("Selection Error ", ex.toString());
                                            }
                                        }
                                        else{

                                            try {
                                                int itemCount = lvBG.getCount();
                                                for(int i=0 ; i < itemCount ; i++){
                                                    lvBG.setItemChecked(i, false);
                                                }
                                            }
                                            catch (Exception ex){
                                                Log.e("Selection Error ", ex.toString());
                                            }
                                        }

                                    }
                                });

                                final ArrayAdapter adapter = new ArrayAdapter(getApplication(),R.layout.list_allmeter_item, list);
                                lvBG.setAdapter(adapter);
                                break;
                            case 2:
                                // Set Meter System Clock
                                // Meter System Clock
                                String beforeMeterTime = formatterDate.format(mTaiDocMeter
                                        .getSystemClock().getSystemClockTime());
                                // Meter System Clock
                                Date nowTime = new Date();
                                mTaiDocMeter.setSystemClock(nowTime);
                                String afterMeterTime = formatterDate.format(mTaiDocMeter
                                        .getSystemClock().getSystemClockTime());
                                layout = inflater.inflate(R.layout.set_meter_system_clock,
                                        (ViewGroup) findViewById(R.id.setMeterSystemClockLayout));
                                TextView viewBerforeMeterTimeClock = (TextView) layout
                                        .findViewById(R.id.beforeMeterSystemClock);
                                viewBerforeMeterTimeClock.setText(beforeMeterTime);
                                TextView viewAfterMeterTimeClock = (TextView) layout
                                        .findViewById(R.id.afterMeterSystemClock);
                                viewAfterMeterTimeClock.setText(afterMeterTime);
                                break;
                            case 3:
                                // Clear records
                                mTaiDocMeter.clearMeasureRecords(PCLinkLibraryEnum.User.CurrentUser);
                                layout = inflater.inflate(R.layout.clear_records,
                                        (ViewGroup) findViewById(R.id.clearRecordsLayout));
                                break;
                            case 4:
                                // Turn(Power) Off Meter
                                layout = inflater.inflate(R.layout.turn_off_meter,
                                        (ViewGroup) findViewById(R.id.turnOffMeterLayout));
                                mTurnOffMeterClick = true;
                                if(mTaiDocMeter!=null)
                                 mTaiDocMeter.turnOffMeterOrBluetooth(0);

                                if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                                    mPL2303Connection.disconnect();
                                }
                                else {
                                    if (mBLEMode) {
                                        mConnection.LeDisconnect();
                                    }
                                    else {
                                        mConnection.disconnect();
                                        mConnection.LeDisconnect();
                                    }
                                }

                                finish();
                                break;
                        } /* end of switch */

                        // alert dialog

                        builder = new AlertDialog.Builder(PCLinkLibraryCommuTestActivity.this);
                        builder.setView(layout);
                        if(clickItem==0) {
                            builder.setNegativeButton("Opslaan in Dagboek", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {




                                        SimpleDateFormat xdf = new SimpleDateFormat(Constants.x_DATE_FORMAT,Locale.getDefault());
                                        LogBookEntry entry = new LogBookEntry();
                                        Date dateObj = xdf.parse(measurementDate);
                                       // xdf.format(dateObj);
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(dateObj);

                                        int xhour = c.get(Calendar.HOUR_OF_DAY);
                                        int xminute = c.get(Calendar.MINUTE);
                                        if (activeUser != null)
                                            entry.setUserId(activeUser.getUserId());
                                        entry.setEntryType(LogBookEntry.Type.BLOOD);
                                        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DB_DATE_FORMAT,Locale.getDefault());
                                        String currentDateandTime = sdf.format(dateObj);
                                        entry.setEntryDate(currentDateandTime);
                                        entry.setEntryName(getResources().getString(R.string.bloedwaarde));
                                        entry.setEntryTime(Utilities.getInstance().getFormattedTime(xhour,xminute));
                                        entry.setEntryAmount(mgValue);
                                        BaseController.getInstance().getDbManager(getApplication().getApplicationContext()).getLogBookTable().insert(entry);

                                    }
                                    catch (Exception ex){
                                        Log.e("Bluetooth Entry Error ", ex.toString());
                                    }


                                }
                            });

                        }
                        if(clickItem==1) {
                            builder.setNegativeButton("Opslaan in Dagboek", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {

                                        String selected = "";
                                        int cntChoice = lvBG.getCount();
                                        SparseBooleanArray sparseBooleanArray = lvBG.getCheckedItemPositions();
                                        for(int i = 0; i < cntChoice; i++){
                                            if(sparseBooleanArray.get(i)) {
                                                selected = lvBG.getItemAtPosition(i).toString();
                                                String bg = TextUtils.substring(selected,17, 22).trim();
                                                String dt = TextUtils.substring(selected, 0, 16).trim();

                                                SimpleDateFormat xdf = new SimpleDateFormat(Constants.x_DATE_FORMAT,Locale.getDefault());
                                                LogBookEntry entry = new LogBookEntry();
                                                Date dateObj = xdf.parse(dt);
                                                // xdf.format(dateObj);
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(dateObj);

                                                int xhour = c.get(Calendar.HOUR_OF_DAY);
                                                int xminute = c.get(Calendar.MINUTE);
                                                if (activeUser != null)
                                                    entry.setUserId(activeUser.getUserId());
                                                entry.setEntryType(LogBookEntry.Type.BLOOD);
                                                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DB_DATE_FORMAT,Locale.getDefault());
                                                String currentDateandTime = sdf.format(dateObj);
                                                entry.setEntryDate(currentDateandTime);
                                                entry.setEntryName(getResources().getString(R.string.bloedwaarde));
                                                entry.setEntryTime(Utilities.getInstance().getFormattedTime(xhour,xminute));
                                                entry.setEntryAmount(Double.parseDouble(bg));
                                                BaseController.getInstance().getDbManager(getApplication().getApplicationContext()).getLogBookTable().insert(entry);

                                            }

                                        }



                                    }
                                    catch (Exception ex){
                                        Log.e("Bluetooth Entry Error ", ex.toString());
                                    }


                                }
                            });

                        }
                        builder.setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mTurnOffMeterClick) {
                                    if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean(PCLinkLibraryDemoConstant.FromPL2303, true);

                                        /*GuiUtils.goToSpecifiedActivity(PCLinkLibraryCommuTestActivity.this,
                                                PCLinkLibraryDemoActivity.class, bundle);*/
                                    }
                                    else {
                                        GuiUtils.goToPCLinkLibraryHomeActivity(PCLinkLibraryCommuTestActivity.this);
                                    }
                                } /* end of if */

                            }
                        });
                        builder.setCancelable(false);
                        alertDialog = builder.create();
                        if (!mTurnOffMeterClick)
                             alertDialog.show();

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_FAIL);
                    } finally {
                        meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CONNECT_DONE);
                    } /* end of try-catch-finally */
                    Looper.loop();
                }
            }).start();
        } else {
            mBACmdThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Looper.prepare();
                    meterCommuHandler.sendEmptyMessage(MESSAGE_STATE_CHECK_METER_BT_DISTENCE);

                    Thread thisThread = Thread.currentThread();
                    while (thisThread == mBACmdThread) {
                        try {
                            ((TD4283) mTaiDocMeter).sendBACommand();
                            Thread.sleep(500);
                        } catch (Exception e) {
                            if (e.getMessage().contains("Broken pipe")
                                    || e.getMessage().contains("Operation Canceled")) {
                                if (mBLEMode) {
                                    mConnection.LeDisconnect();
                                }
                                else {
                                    mConnection.disconnect();
                                    mConnection.LeDisconnect();
                                }

                                meterCommuHandler
                                        .sendEmptyMessage(MESSAGE_STATE_CHECK_METER_BT_DISTENCE_FAIL);
                            } /* end of if */
                        } /* end of try-catch */
                    } /* end of while */
                    Looper.loop();
                }
            });

            if (mBACmdThread != null) {
                mBACmdThread.start();
            } /* end of if */
        } /* end of if */

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE.equals(mBtTransferType)) {
            setupPL2303Connection();
            connectMeter();
        }
        else {
            if ("".equals(mMacAddress)) {

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    setupAndroidBluetoothConnection();
                    connectMeter();
                }
                else {
                    showAlertDialog(R.string.pair_meter_first, true);
                }
            } else if ("".equals(mBtTransferType)) {
                showAlertDialog(R.string.meter_trans_type_fail, true);
            } else if (mTaiDocMeter == null) {
                setupAndroidBluetoothConnection();
                connectMeter();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disconnectMeter(true);
        dimissProcessDialog();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
}
