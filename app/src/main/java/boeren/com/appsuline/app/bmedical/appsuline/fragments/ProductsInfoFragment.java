package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.ProductsAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.ProductInfo;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.ProductCarb;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsInfoFragment extends BaseFragment implements BaseDialogFragment.OnDialogFragmentDismissListener{

    public static final String ARGUMENT="Argument";
    ProductsAdapter adapter;
    ListView lv_products;
    ProductInfo selectedProduct;
    User activeUser;
    String calendarData;

    public ProductsInfoFragment() {
        // Required empty public constructor
    }
    public static ProductsInfoFragment newInstance(Serializable argument){
        ProductsInfoFragment fragment = new ProductsInfoFragment();
        Bundle arg=new Bundle();
        arg.putSerializable(ARGUMENT,argument);
        fragment.setArguments(arg);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflateMainView(inflater,R.layout.fragment_carbohydrate, container, false);
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey(ARGUMENT)){
            calendarData=(String) getArguments().getSerializable(ARGUMENT);
        }
        adapter = new ProductsAdapter(getActivity(),getProducts());
        activeUser=BaseController.getInstance().getActiveUser();
        ((ListView) getMainView().findViewById(R.id.lv_carbotable)).setAdapter(adapter);
        lv_products  = (ListView) getMainView().findViewById(R.id.lv_carbotable);
        lv_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (calendarData != null) {
                    selectedProduct = adapter.getItem(position);
                    showMealDialog(LogBookEntry.Type.MEAL);
                }
            }

        });
        addTextWatcher(getMainView().findViewById(R.id.inputSearch));

        return  getMainView();
    }

    private ArrayList<ProductInfo> getProducts(){
        return BaseController.getInstance().getDbManager(getActivity()).getProductInfoTable().getAllProducts();
    }
    private void showMealDialog(LogBookEntry.Type type){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        LogBookEntry entry= new LogBookEntry();
        entry.setUserId(activeUser.getUserId());
        entry.setEntryDate(calendarData);
        entry.setEntryName(selectedProduct.getName());
        ProductCarb.ProductInfo = selectedProduct.getCarbohydrates();
        ProductCarb.ProductName = selectedProduct.getName();
        ProductCarb.ProductQty = selectedProduct.getQuantity();
        ProductCarb.ProductPortion ="1";
        entry.setEntryAmount(Double.parseDouble(selectedProduct.getCarbohydrates()));

        BaseDialogFragment bloodDialogFragment = BaseDialogFragment.newInstance(getDialogOfEntry(), entry);
        bloodDialogFragment.setDismissListener(this);
       /* LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.fragment_meal, null);
        tv_carbolink = (TextView)popupView.findViewById(R.id.tv_carboLink);*/

        bloodDialogFragment.show(fm, bloodDialogFragment.getStringTag());

    }
    private int getDialogOfEntry(){
        return BaseDialogFragment.DIALOG_MEAL;
    }
    @Override
    protected void onFragmentTextChanged(EditText editText, final CharSequence s, int start, int before, int count) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.getFilter().filter(s);
            }
        });
    }
    @Override
    public void onDismiss(DialogFragment dialogFragment) {

    }

    @Override
    public void onDismissWithDataSaved(DialogFragment dialogFragment) {

    }
}
