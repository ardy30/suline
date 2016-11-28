package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.taidoc.pclinklibrary.android.bluetooth.util.BluetoothUtil;
import com.taidoc.pclinklibrary.connection.AndroidBluetoothConnection;
import com.taidoc.pclinklibrary.connection.util.ConnectionManager;
import com.taidoc.pclinklibrary.constant.PCLinkLibraryEnum;
import com.taidoc.pclinklibrary.exceptions.CommunicationTimeoutException;
import com.taidoc.pclinklibrary.exceptions.NotSupportMeterException;
import com.taidoc.pclinklibrary.meter.AbstractMeter;
import com.taidoc.pclinklibrary.meter.record.AbstractRecord;
import com.taidoc.pclinklibrary.meter.record.BloodGlucoseRecord;
import com.taidoc.pclinklibrary.meter.util.MeterManager;
import com.taidoc.pclinklibrary.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import boeren.com.appsuline.app.bmedical.appsuline.PCLinkLibraryCommuTestActivity;
import boeren.com.appsuline.app.bmedical.appsuline.PCLinkLibraryDemoActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.constant.PCLinkLibraryDemoConstant;
import boeren.com.appsuline.app.bmedical.appsuline.utils.MeterPreferenceDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BluetoothFragment extends android.support.v4.app.Fragment implements PCLinkLibraryCommuTestActivity.Errorcallbacks {
    Button btnOpen, btnCancel,btnShop,btnHelp;
    Dialog dialog;
    BluetoothAdapter mBluetoothAdapter= null;
    ScrollView sv_bluetooth;
    LinearLayout sv_meterinfo;
    TextView tv_bluetooth;
    ListView lv_meterinfo,lv_meterreading;
    private AbstractMeter BMMiniDevice =null;
    //AbstractRecord latestmeterreading;
    String serialNumber,modelNumber,meterTime;
    int glucoseValue=0;
    // Date Format
    SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
    SimpleDateFormat MeterDateformatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
    BluetoothDevice BMDiamondDevice;
    private String mMacAddress;
    ArrayList<String> meterReadingList;
    Button btn_blueconnect;
    AnimationAdapter animAdapter;
    ArrayAdapter<String> meterInfoAdapter;
    private boolean isDualPan = false;

    // Blue tooth layout
    private LinearLayout mPL2303Layout;
    private LinearLayout mBTLayout;
    private Spinner mBaudRate;
    private Button mPL2303Connect;
    private Spinner mSelectedMeter;
    private Button mConnect;
    private Button mBLEPair;
    private RadioGroup mMeterTransferType;
    private MeterPreferenceDialog meterDialog;
    private BroadcastReceiver detachReceiver;
    private boolean mBLEMode;
    private boolean mKNVMode;
    private int mPairedMeterCount;
    private Map<String, String> mPairedMeterNames;
    private Map<String, String> mPairedMeterAddrs;


    AndroidBluetoothConnection connection = null;

    public BluetoothFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(BTReceiver, filter1);
        getActivity().registerReceiver(BTReceiver, filter2);
        getActivity().registerReceiver(BTReceiver, filter3);
        if(getActivity().findViewById(R.id.container2) != null)isDualPan = true;
        setRetainInstance(true);
    }
    @Override
    public void onPause() {

        super.onPause();
        // Unregister since the activity is not visible
        try  {
                getActivity().unregisterReceiver(BTReceiver);
        }
        catch (IllegalArgumentException e) {
            // Check wether we are in debug mode
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }


    }
    @Override
    public void onResume() {
        super.onResume();
        registerBluetoothReceiver();
        boolean fromPl2303 = false;
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null && bundle.containsKey(PCLinkLibraryDemoConstant.FromPL2303)) {
            fromPl2303 = bundle.getBoolean(PCLinkLibraryDemoConstant.FromPL2303);
        }


        if(getActivity().getIntent().getAction() != null && getActivity().getIntent().getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED) ||
                fromPl2303 == true) {
            initDetachedUsbListener();
            initBaudRate();
            initUI(true);
        }
        else {
            initUI(false);
            this.queryBluetoothDevicesAndSetToUi();
        }

    }
    private void initDetachedUsbListener() {
        if (detachReceiver == null) {
            detachReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    initUI(false);
                    queryBluetoothDevicesAndSetToUi();
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            getActivity().registerReceiver(detachReceiver, filter);
        }
    }

    private void initBaudRate() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.BaudRate_Var, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBaudRate.setAdapter(adapter);
        mBaudRate.setSelection(1);
    }

    private void initUI(boolean isPL2303) {
       // mPL2303Layout.setVisibility(isPL2303 ? View.VISIBLE : View.GONE);
      // mBTLayout.setVisibility(!isPL2303 ? View.VISIBLE : View.GONE);



        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            mBLEPair.setVisibility(View.GONE);
        }
    }
    private void registerBluetoothReceiver() {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(BTReceiver, filter1);
        getActivity().registerReceiver(BTReceiver, filter2);
        getActivity().registerReceiver(BTReceiver, filter3);
    }

    @Override
    public void onStart()
    {
        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        disconnectMeter(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        tv_bluetooth =  (TextView) rootview.findViewById(R.id.tv_bluetooth_notice);
        sv_bluetooth = (ScrollView) rootview.findViewById(R.id.sv_bluetooth);
        sv_meterinfo = (LinearLayout) rootview.findViewById(R.id.sv_meterinfo);
        lv_meterreading = (ListView) rootview.findViewById(R.id.lv_meterreading);
        lv_meterinfo = (ListView) rootview.findViewById(R.id.lv_metersystemsinfo);
        btn_blueconnect = (Button) rootview.findViewById(R.id.btnmeterconnect);
        mPL2303Layout = (LinearLayout) rootview.findViewById(R.id.ll_top);
        mBTLayout = (LinearLayout) rootview.findViewById(R.id.ll_body);
        mBaudRate = (Spinner) rootview.findViewById(R.id.spBaudRate);
        mPL2303Connect = (Button) rootview.findViewById(R.id.btnPL2303ConnectMeter);

        mSelectedMeter = (Spinner) rootview.findViewById(R.id.pairDeviceList);
        mConnect = (Button) rootview.findViewById(R.id.btnConnectMeter);
        mBLEPair = (Button) rootview.findViewById(R.id.btnPair);
        mMeterTransferType = (RadioGroup) rootview.findViewById(R.id.meterBtTransferTypeRadioGroup);
        setListener();
        clearSharePreferences();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(R.string.bluetooth);

        try {
            mBluetoothAdapter = BluetoothUtil.getBluetoothAdapter();

            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
                System.out.println("Device does not support Bluetooth");
            }
             else {

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().trim().equals("DIAMOND MOBILE")) {
                            // Get the Mac Address
                            mMacAddress = device.getAddress();
//                            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                                // Register BTReceiver to receive.

                                sv_bluetooth.setVisibility(View.INVISIBLE);
                                mBTLayout.setVisibility(View.VISIBLE);
                                tv_bluetooth.setVisibility(View.INVISIBLE);
                               /* setupAndroidBluetoothConnection();
                                new ShowResult().execute();*/
                           // }

                        }
                    }
                }
            }
        }
        catch (Exception e)  {

        }

        btn_blueconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , PCLinkLibraryDemoActivity.class);
                startActivity(intent);
            }

        });
        btnShop = (Button) rootview.findViewById(R.id.btnbluetoothkopen);
        btnHelp = (Button) rootview.findViewById(R.id.btnbluetoothhelp);
        btnShop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showWebshopDialog();
            }

        });
        btnHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Open the Blue tooth instructions fragment
                android.support.v4.app.Fragment fragment =null;
                fragment = new BluetoothmeterFragment();
                if (fragment != null) {
                    int containerId = R.id.container;
                    if(isDualPan){
                        containerId = R.id.container2;
                        fragmentPopUp();
                    }
                    getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                            .replace(containerId, fragment).addToBackStack(null)
                            .commit();
                }
            }

        });



        return rootview;
    }
    // Listeners
    private AdapterView.OnItemSelectedListener mSelectedMeterOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position,
                                   long id) {

            TextView view = (TextView)selectedItemView;
            if (view != null) {
                int indexOfBLE = isBLEMeter(view.getText().toString());
                if (indexOfBLE != -1) {
                    mMeterTransferType.check(R.id.meterBtType2RadioButton);
                    for (View v : mMeterTransferType.getTouchables()) {
                        v.setEnabled(false);
                    }

                    mBLEMode = true;
                    mKNVMode = view.getText().toString().toLowerCase().contains("knv v125");
                }
                else {
                    for (int i=0; i<mMeterTransferType.getChildCount(); i++) {
                        View v = mMeterTransferType.getChildAt(i);
                        v.setEnabled(true);
                    }

                    mBLEMode = false;
                    mKNVMode = false;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
        }
    };
    private Button.OnClickListener mConnectOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (mSelectedMeter.getSelectedItem() != null) {
                String deviceMac = mSelectedMeter.getSelectedItem().toString().split("/")[1];
                if (!"".equals(deviceMac)) {
                    // Set pair device mac address
                    SharedPreferences settings = getActivity().getSharedPreferences(
                            PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);

                    settings.edit()
                            .putString(PCLinkLibraryDemoConstant.PAIR_METER_MAC_ADDRESS,
                                    deviceMac).commit();


                    if (mMeterTransferType.getCheckedRadioButtonId() == R.id.meterBtType1RadioButton) {
                        // Type One
                        //bleMode
                        settings.edit()
                                .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                                        PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_ONE)
                                .putBoolean(PCLinkLibraryDemoConstant.BLE_MODE, false)
                                .putBoolean(PCLinkLibraryDemoConstant.KNV_MODE, false)
                                .commit();
                    } else {
                        // Type Two
                        settings.edit()
                                .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                                        PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE_TWO)
                                .putBoolean(PCLinkLibraryDemoConstant.BLE_MODE, mBLEMode)
                                .putBoolean(PCLinkLibraryDemoConstant.KNV_MODE, mKNVMode)

                                .commit();
                    } /* end of if */

                    // Go to the test command activity
                    if (mKNVMode) {
                       /* GuiUtils.goToSpecifiedActivity(PCLinkLibraryDemoActivity.this,
                                PCLinkLibraryCommuTestActivityForKNV.class);*/
                    }
                    else {
                      /*  GuiUtils.goToSpecifiedActivity(getActivity(),
                                PCLinkLibraryCommuTestActivity.class);*/
                        Intent intent = new Intent(getActivity() , PCLinkLibraryCommuTestActivity.class);
                        startActivity(intent);
                    }
                } /* end of if */
            } else {
                new AlertDialog.Builder(getActivity(),R.layout.layout_dialog)
                        .setMessage(R.string.bluetooth_need_to_pair)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Go to the setting page
                                startActivityForResult(new Intent(
                                        android.provider.Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                            };
                        }).show();
            } /* end of if */
        }
    };

    private Button.OnClickListener mBLEPairOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            ShowMeterDialog();
        }
    };
    private Button.OnClickListener mPL2303ConnectOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            SharedPreferences settings = getActivity().getSharedPreferences(
                    PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);

            settings.edit()
                    .putString(PCLinkLibraryDemoConstant.BT_TRANSFER_TYPE,
                            PCLinkLibraryDemoConstant.PL2303_TRANSFER_TYPE)
                    .commit();

            settings.edit()
                    .putString(PCLinkLibraryDemoConstant.BAUDRATE,
                            mBaudRate.getSelectedItem().toString())
                    .commit();

           /* GuiUtils.goToSpecifiedActivity(getActivity(),
                    PCLinkLibraryCommuTestActivity.class);*/
            Intent intent = new Intent(getActivity() , PCLinkLibraryCommuTestActivity.class);
            startActivity(intent);

        }
    };
    private void ShowMeterDialog() {
        meterDialog = MeterPreferenceDialog.newInstance();
        meterDialog.show(getActivity().getSupportFragmentManager(), "FRAGMENT_SETTING_METER");
    }
    private void setListener() {
        mConnect.setOnClickListener(mConnectOnClickListener);
        mBLEPair.setOnClickListener(mBLEPairOnClickListener);
        mSelectedMeter.setOnItemSelectedListener(mSelectedMeterOnItemSelectedListener);
        mPL2303Connect.setOnClickListener(mPL2303ConnectOnClickListener);
    }
    private void clearSharePreferences() {
        // set pair device mac address
        SharedPreferences settings = getActivity().getSharedPreferences(
                PCLinkLibraryDemoConstant.SHARED_PREFERENCES_NAME, 0);
        settings.edit().remove(PCLinkLibraryDemoConstant.PAIR_METER_MAC_ADDRESS).commit();
    }
    public void queryBluetoothDevicesAndSetToUi() {

        new Thread(new Runnable() {
            private Handler handler = new Handler();
            private ProgressDialog processDialog;

            @Override
            public void run() {
                Looper.prepare();
                try {
                    final List<String> remoteDeviceNameList = new ArrayList<String>();
                    final ArrayAdapter<String> selectedMeterSpinnerAdapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_spinner_item,
                            remoteDeviceNameList);
                    selectedMeterSpinnerAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            processDialog = ProgressDialog.show(getActivity(),
                                    null, getString(R.string.start_search_meter), true);
                            if(selectedMeterSpinnerAdapter.getCount()>0){
                                   sv_bluetooth.setVisibility(View.INVISIBLE);
                            mBTLayout.setVisibility(View.VISIBLE);
                            tv_bluetooth.setVisibility(View.INVISIBLE);
                            }
                            mSelectedMeter.setAdapter(selectedMeterSpinnerAdapter);

                        }
                    });

                    List<BluetoothDevice> remoteDeviceList = BluetoothUtil.getPairedDevices();

                    for (int i = 0; i < remoteDeviceList.size(); i++) {
                        String remoteDeviceName = remoteDeviceList.get(i).getName() + "/"
                                + remoteDeviceList.get(i).getAddress();
                        remoteDeviceNameList.add(remoteDeviceName);
                    } /* end of for */

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int adapterCount = initBLEMeters();

                            if (adapterCount > 0) {
                                selectedMeterSpinnerAdapter.notifyDataSetChanged();
                                mSelectedMeter.setSelection(0);

                                mSelectedMeterOnItemSelectedListener.onItemSelected(mSelectedMeter,
                                        mSelectedMeter.getChildAt(0), 0, 0);


                            } else if(adapterCount < 0){
                                showNeeToPairMessageDialog();

                            } /* end of if */
                        }
                    }, 1000);
                } catch (Exception e) {
                    LogUtil.error(PCLinkLibraryDemoActivity.class, e.getMessage(), e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showErrorMessageDialog();
                        }
                    });
                } finally {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (processDialog != null) {
                                processDialog.dismiss();
                            } /* end of if */
                        }
                    });
                }
                Looper.loop();
            }


            private void showErrorMessageDialog() {
                new AlertDialog.Builder(getActivity(),android.R.style.Theme_Translucent)
                        .setMessage(R.string.bluetooth_occur_error)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Back to the home menu

                            };
                        }).show();
            }


            private void showNeeToPairMessageDialog() {
                new AlertDialog.Builder(getActivity(),android.R.style.Theme_Translucent)
                        .setMessage(R.string.bluetooth_need_to_pair)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Go to the setting page
                                startActivityForResult(new Intent(
                                        android.provider.Settings.ACTION_BLUETOOTH_SETTINGS), 0);
                            };
                        }).show();
            }
        }).start();
    }
    private int initBLEMeters() {
        try {
            int adapterCount = 0;
            if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

                mPairedMeterNames = new HashMap<String, String>();
                mPairedMeterAddrs = new HashMap<String, String>();
                mPairedMeterCount = MeterPreferenceDialog.getPairedMeters(getActivity(), mPairedMeterNames, mPairedMeterAddrs);

                ArrayAdapter<String> selectedMeterSpinnerAdapter = (ArrayAdapter<String>) mSelectedMeter.getAdapter();
                adapterCount = selectedMeterSpinnerAdapter.getCount();
                String nameKey;
                String addrKey;
                String nameValue;
                String addrValue;
                for (int i = 0; i < mPairedMeterCount; i++) {
                    nameKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_NAME_ + String.valueOf(i);
                    addrKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_ADDR_ + String.valueOf(i);
                    nameValue = mPairedMeterNames.get(nameKey);
                    addrValue = mPairedMeterAddrs.get(addrKey);
                    String remoteDeviceName = nameValue + "/" + addrValue;

                    if (selectedMeterSpinnerAdapter != null) {
                        sv_bluetooth.setVisibility(View.INVISIBLE);
                        mBTLayout.setVisibility(View.VISIBLE);
                        tv_bluetooth.setVisibility(View.INVISIBLE);
                        boolean find_flag = false;
                        for (int j = 0; j < selectedMeterSpinnerAdapter.getCount(); j++) {
                            String remoteDeviceName2 = selectedMeterSpinnerAdapter.getItem(j);
                            if (remoteDeviceName.equals(remoteDeviceName2)) {
                                find_flag = true;
                                break;
                            }
                        }
                        if (!find_flag) {
                            adapterCount++;
                            selectedMeterSpinnerAdapter.add(remoteDeviceName);
                        }

                    }
                    else  {
                        sv_bluetooth.setVisibility(View.VISIBLE);
                        mBTLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }

            return adapterCount;
        }
        catch (Exception e){
            return 0;
        }

    }
    private int isBLEMeter(final String str) {
        String nameKey;
        String addrKey;
        String nameValue;
        String addrValue;
        for (int i=0; i<mPairedMeterCount; i++) {
            nameKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_NAME_ + String.valueOf(i);
            addrKey = PCLinkLibraryDemoConstant.BLE_PAIRED_METER_ADDR_ + String.valueOf(i);
            nameValue = mPairedMeterNames.get(nameKey);
            addrValue = mPairedMeterAddrs.get(addrKey);
            String remoteDeviceName = nameValue + "/" + addrValue;

            if (str.equals(remoteDeviceName)) {
                return i;
            }
        }

        return -1;
    }
    private void fragmentPopUp(){
        if( getFragmentManager().getBackStackEntryCount()>0)
            getFragmentManager().popBackStack();
    }
    //The BroadcastReceiver that listens for bluetooth broadcasts
    private BroadcastReceiver BTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))  {
                System.out.println("Connected to a device");
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                //Do something if connected
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {

                    // CONNECT  to
                    //System.out.println("Bond state changed");
                    if(mBluetoothAdapter==null)
                    mBluetoothAdapter = BluetoothUtil.getBluetoothAdapter();
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    // If there are paired devices
                    if (pairedDevices.size() > 0) {
                        // Loop through paired devices
                        for (BluetoothDevice dev : pairedDevices) {
                            // check if the Diamond Mobile is connected
                            if(dev.getName().trim().equals("DIAMOND MOBILE"))  {
                                // Get the Mac Address
                                mMacAddress = dev.getAddress();
                                System.out.println(dev.getName() + "\n" + dev.getAddress());
                                sv_bluetooth.setVisibility(View.INVISIBLE);
                                mBTLayout.setVisibility(View.VISIBLE);
                                tv_bluetooth.setVisibility(View.INVISIBLE);
                                //tv_bluetooth.setText("Bloedglucosemeter gevonden");

                                // have to check if this is needed because sometimes the device can be turned off.
                                // so if it is turned on and paired it should try to connect automatically.

                                   /* setupAndroidBluetoothConnection();
                                    new ShowResult().execute();*/



                            }

                        }
                    }
                    else  {
                        sv_bluetooth.setVisibility(View.VISIBLE);
                        mBTLayout.setVisibility(View.INVISIBLE);
                        tv_bluetooth.setVisibility(View.INVISIBLE);
                    }
                }
                else  {
                    sv_bluetooth.setVisibility(View.INVISIBLE);
                    tv_bluetooth.setVisibility(View.INVISIBLE);
                }

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                if (connection != null){
                if (connection.getState() == AndroidBluetoothConnection.STATE_NONE) {
                    //Do something if disconnected
                    System.out.println("Disconnected from the device");
                    sv_bluetooth.setVisibility(View.VISIBLE);
                    sv_bluetooth.setVisibility(View.INVISIBLE);
                    tv_bluetooth.setVisibility(View.VISIBLE);
                    mBTLayout.setVisibility(View.INVISIBLE);

                }
            }
            }

        }
    };
    protected void showWebshopDialog() {

        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_webshop_dialog);
        btnCancel = (Button) dialog.findViewById(R.id.btncancel);
        btnOpen = (Button) dialog.findViewById(R.id.btnopen);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }

        });
        btnOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.webshopurl)));
                startActivity(intent);
            }

        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        dialog.show();
    }

    private void setupAndroidBluetoothConnection() {
        if (connection == null) {
            try {
                 connection = ConnectionManager.createAndroidBluetoothConnection(mBTConnectionHandler);
            }
            catch (Exception ee) {
            }
        }
    }
    // The Handler that gets information back from the android bluetooth connection
    private final Handler mBTConnectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (sv_meterinfo.getVisibility() != View.VISIBLE)
                sv_meterinfo.setVisibility(View.VISIBLE);


        }
    };
    private void disconnectMeter(final boolean isNeedPowerOff) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isNeedPowerOff) {
                        if(BMMiniDevice!=null)
                        BMMiniDevice.turnOffMeterOrBluetooth(0);
                    }
                        if(connection!=null) {
                            connection.disconnect();
                            connection.LeDisconnect();
                        }

                } catch (Exception e) {
                    Log.e("Error", e.getMessage(), e);
                } finally {
                }
            }
        }).start();
    }
   public ArrayList<String> connectBluetoothMeter() {
       meterReadingList = new ArrayList<String>();
       try {
           if (connection.getState() != AndroidBluetoothConnection.STATE_CONNECTED) {
               if (mMacAddress != null) {
                   BMDiamondDevice = BluetoothUtil.getPairedDevice(mMacAddress);
                   connection.connect(BMDiamondDevice);
                   long startConnectTime = System.currentTimeMillis();
                   while (connection.getState() != AndroidBluetoothConnection.STATE_CONNECTED) {
                       try {
                           Thread.sleep(500);
                       } catch (InterruptedException e) {
                           System.out.println("Check that meter is on 1");
                           long conntectTime = System.currentTimeMillis();
                           if ((conntectTime - startConnectTime) > AndroidBluetoothConnection.BT_CONNECT_TIMEOUT)
                               throw new CommunicationTimeoutException();
                       }
                   }
                   BMMiniDevice = MeterManager.detectConnectedMeter(connection);
                   if (BMMiniDevice == null) {
                       System.out.println("Check that meter is on 2");
                       throw new NotSupportMeterException();

                   }
                   // Get Last Meter Reading
                  // latestmeterreading = BMMiniDevice.getStorageDataRecord(0, PCLinkLibraryEnum.User.CurrentUser);
                  // System.out.println(latestmeterreading);

                   // Get Serial Number
                   serialNumber = BMMiniDevice.getSerialNumberRecord().getSerialNumber();
                   System.out.println(serialNumber);
                   // Get Meter Model Number
                   modelNumber = BMMiniDevice.getDeviceModel().getProjectCode();
                   System.out.println(modelNumber);
                   //Get Meter System Time
                   meterTime = MeterDateformatter.format(BMMiniDevice.getSystemClock().getSystemClockTime());
                   System.out.println(meterTime);

                    // Add to list
                   /*meterReadingList.add(0,"Systeemdatum: "+meterTime);
                   meterReadingList.add(1,"Model: "+modelNumber);
                   meterReadingList.add(2,"Serialnummer: "+serialNumber);*/
                   // Get all reading records
                   List<AbstractRecord> abstractRecordList  =  BMMiniDevice.getAllStorageDataRecord(PCLinkLibraryEnum.User.CurrentUser);
                   for (AbstractRecord item :abstractRecordList)
                   {
                       if (item instanceof BloodGlucoseRecord) {
                           // convert value
                           String measurementDate = formatterDate.format(((BloodGlucoseRecord) item).getMeasureTime());
                           System.out.println("Measurement date: "+measurementDate);
                           glucoseValue = ((BloodGlucoseRecord) item).getGlucoseValue();
                           System.out.println("Sugar level: "+glucoseValue);
                           meterReadingList.add(measurementDate +" "+glucoseValue);

                       }

                   }

                   // Convert value and set views
                  /* if (latestmeterreading instanceof BloodGlucoseRecord) {
                       // convert value
                       String measurementDate = formatterDate.format(((BloodGlucoseRecord) latestmeterreading).getMeasureTime());
                       System.out.println(measurementDate);
                       glucoseValue = ((BloodGlucoseRecord) latestmeterreading).getGlucoseValue();
                       System.out.println("Sugar level "+glucoseValue);


                   } else {
                       System.out.println("Check that the Meter is paired 3");
                   }*/
               }
           }

       } catch (Exception closeException) {
           System.out.println("Check that meter is on 4");
       }
return  meterReadingList;
   }

    @Override
    public void onConnectionError() {
        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_error_dialog);

        btnCancel = (Button) dialog.findViewById(R.id.btncancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }

        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        dialog.show();
    }

    public class ShowResult extends AsyncTask<Void,Boolean,ArrayList<String>>
{
    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        return connectBluetoothMeter();
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        if(strings==null) {
            strings=new ArrayList<String>();
        }
        sv_meterinfo.setVisibility(View.VISIBLE);
        meterInfoAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.title_color));
                return view;
            }
        };
        animAdapter = new AlphaInAnimationAdapter(meterInfoAdapter);
        animAdapter.setAbsListView(lv_meterinfo);
        lv_meterinfo.setAdapter(animAdapter);
    }
    }

}
