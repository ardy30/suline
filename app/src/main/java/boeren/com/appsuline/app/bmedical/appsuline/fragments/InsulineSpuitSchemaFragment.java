package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.InsulinSpuitShemaModel;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

/**
 * Created by Jamil on 31-3-2015.
 */
public class InsulineSpuitSchemaFragment extends android.support.v4.app.Fragment {

    private View rootview;
    User activeUser;
    public InsulineSpuitSchemaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_insuline_spuit_schema, container, false);
        rootview.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String insulineSpuitJson=populateValuesInModel().toJson();
                if(MainActivity.userCount>0){
                    User activeUser = BaseController.getInstance().getActiveUser();
                    if(activeUser==null)
                        activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
                    activeUser.setInsulinSpuitInfo(insulineSpuitJson);
                    BaseController.getInstance().getDbManager(getActivity()).getUserTable().update(activeUser);
                    getActivity().onBackPressed();
                }

              /*  InsulinePompSchemaFragment fragment=new InsulinePompSchemaFragment();

                getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).show(fragment)
                        .replace(R.id.container, fragment).addToBackStack(null)
                        .commit();*/
            }
        });
        User activeUser = BaseController.getInstance().getActiveUser();
        if(activeUser==null)
            activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        if(activeUser!=null) {
            String spuitjson = activeUser.getInsulinSpuitInfo();
            InsulinSpuitShemaModel model = null;
            if (null == spuitjson) {
                model = new InsulinSpuitShemaModel();
            } else {
                model = InsulinSpuitShemaModel.fromJson(spuitjson);
            }
            populateViewFromModel(model);
        }
        return rootview;
    }

    public InsulinSpuitShemaModel populateValuesInModel(){
        InsulinSpuitShemaModel model=new InsulinSpuitShemaModel();
        model.etShortInsuline=((EditText)rootview.findViewById(R.id.etShortInsuline)).getText().toString();
        model.etLongInsuline=((EditText)rootview.findViewById(R.id.etLongInsuline)).getText().toString();

        model.date1=((EditText)rootview.findViewById(R.id.date1)).getText().toString();
        model.date2=((EditText)rootview.findViewById(R.id.date2)).getText().toString();
        model.date3=((EditText)rootview.findViewById(R.id.date3)).getText().toString();
        model.date4=((EditText)rootview.findViewById(R.id.date4)).getText().toString();
        model.date5=((EditText)rootview.findViewById(R.id.date5)).getText().toString();
        model.t1eh02=((EditText)rootview.findViewById(R.id.t1eh02)).getText().toString();
        model.t1eh03=((EditText)rootview.findViewById(R.id.t1eh03)).getText().toString();
        model.t1eh04=((EditText)rootview.findViewById(R.id.t1eh04)).getText().toString();
        model.t1eh05=((EditText)rootview.findViewById(R.id.t1eh05)).getText().toString();
        model.t1eh11=((EditText)rootview.findViewById(R.id.t1eh11)).getText().toString();
        model.t1eh12=((EditText)rootview.findViewById(R.id.t1eh12)).getText().toString();
        model.t1eh13=((EditText)rootview.findViewById(R.id.t1eh13)).getText().toString();
        model.t1eh14=((EditText)rootview.findViewById(R.id.t1eh14)).getText().toString();
        model.t1eh15=((EditText)rootview.findViewById(R.id.t1eh15)).getText().toString();
        model.t1eh21=((EditText)rootview.findViewById(R.id.t1eh21)).getText().toString();
        model.t1eh22=((EditText)rootview.findViewById(R.id.t1eh22)).getText().toString();
        model.t1eh23=((EditText)rootview.findViewById(R.id.t1eh23)).getText().toString();
        model.t1eh24=((EditText)rootview.findViewById(R.id.t1eh24)).getText().toString();
        model.t1eh25=((EditText)rootview.findViewById(R.id.t1eh25)).getText().toString();
        model.t1eh31=((EditText)rootview.findViewById(R.id.t1eh31)).getText().toString();
        model.t1eh32=((EditText)rootview.findViewById(R.id.t1eh32)).getText().toString();
        model.t1eh33=((EditText)rootview.findViewById(R.id.t1eh33)).getText().toString();
        model.t1eh34=((EditText)rootview.findViewById(R.id.t1eh34)).getText().toString();
        model.t1eh35=((EditText)rootview.findViewById(R.id.t1eh35)).getText().toString();
        model.t1eh41=((EditText)rootview.findViewById(R.id.t1eh41)).getText().toString();
        model.t1eh42=((EditText)rootview.findViewById(R.id.t1eh42)).getText().toString();
        model.t1eh43=((EditText)rootview.findViewById(R.id.t1eh43)).getText().toString();
        model.t1eh44=((EditText)rootview.findViewById(R.id.t1eh44)).getText().toString();
        model.t1eh45=((EditText)rootview.findViewById(R.id.t1eh45)).getText().toString();
        model.t2eh11=((EditText)rootview.findViewById(R.id.t2eh11)).getText().toString();
        model.t2eh12=((EditText)rootview.findViewById(R.id.t2eh12)).getText().toString();
        model.t2eh13=((EditText)rootview.findViewById(R.id.t2eh13)).getText().toString();
        model.t3eh11=((EditText)rootview.findViewById(R.id.t3eh11)).getText().toString();
        model.t3eh12=((EditText)rootview.findViewById(R.id.t3eh12)).getText().toString();
        model.t3eh13=((EditText)rootview.findViewById(R.id.t3eh13)).getText().toString();
        model.t3eh14=((EditText)rootview.findViewById(R.id.t3eh14)).getText().toString();
        return model;
    }

    public void populateViewFromModel(InsulinSpuitShemaModel model){

        ((EditText)rootview.findViewById(R.id.etShortInsuline)).setText(model.etShortInsuline);
        ((EditText)rootview.findViewById(R.id.etLongInsuline)).setText(model.etLongInsuline);


        ((EditText)rootview.findViewById(R.id.date1)).setText(model.date1);
        ((EditText)rootview.findViewById(R.id.date2)).setText(model.date2);
        ((EditText)rootview.findViewById(R.id.date3)).setText(model.date3);
        ((EditText)rootview.findViewById(R.id.date4)).setText(model.date4);
        ((EditText)rootview.findViewById(R.id.date5)).setText(model.date5);
        ((EditText)rootview.findViewById(R.id.t1eh02)).setText(model.t1eh02);
        ((EditText)rootview.findViewById(R.id.t1eh03)).setText(model.t1eh03);
        ((EditText)rootview.findViewById(R.id.t1eh04)).setText(model.t1eh04);
        ((EditText)rootview.findViewById(R.id.t1eh05)).setText(model.t1eh05);
        ((EditText)rootview.findViewById(R.id.t1eh11)).setText(model.t1eh11);
        ((EditText)rootview.findViewById(R.id.t1eh12)).setText(model.t1eh12);
        ((EditText)rootview.findViewById(R.id.t1eh13)).setText(model.t1eh13);
        ((EditText)rootview.findViewById(R.id.t1eh14)).setText(model.t1eh14);
        ((EditText)rootview.findViewById(R.id.t1eh15)).setText(model.t1eh15);
        ((EditText)rootview.findViewById(R.id.t1eh21)).setText(model.t1eh21);
        ((EditText)rootview.findViewById(R.id.t1eh22)).setText(model.t1eh22);
        ((EditText)rootview.findViewById(R.id.t1eh23)).setText(model.t1eh23);
        ((EditText)rootview.findViewById(R.id.t1eh24)).setText(model.t1eh24);
        ((EditText)rootview.findViewById(R.id.t1eh25)).setText(model.t1eh25);
        ((EditText)rootview.findViewById(R.id.t1eh31)).setText(model.t1eh31);
        ((EditText)rootview.findViewById(R.id.t1eh32)).setText(model.t1eh32);
        ((EditText)rootview.findViewById(R.id.t1eh33)).setText(model.t1eh33);
        ((EditText)rootview.findViewById(R.id.t1eh34)).setText(model.t1eh34);
        ((EditText)rootview.findViewById(R.id.t1eh35)).setText(model.t1eh35);
        ((EditText)rootview.findViewById(R.id.t1eh41)).setText(model.t1eh41);
        ((EditText)rootview.findViewById(R.id.t1eh42)).setText(model.t1eh42);
        ((EditText)rootview.findViewById(R.id.t1eh43)).setText(model.t1eh43);
        ((EditText)rootview.findViewById(R.id.t1eh44)).setText(model.t1eh44);
        ((EditText)rootview.findViewById(R.id.t1eh45)).setText(model.t1eh45);
        ((EditText)rootview.findViewById(R.id.t2eh11)).setText(model.t2eh11);
        ((EditText)rootview.findViewById(R.id.t2eh12)).setText(model.t2eh12);
        ((EditText)rootview.findViewById(R.id.t2eh13)).setText(model.t2eh13);
        ((EditText)rootview.findViewById(R.id.t3eh11)).setText(model.t3eh11);
        ((EditText)rootview.findViewById(R.id.t3eh12)).setText(model.t3eh12);
        ((EditText)rootview.findViewById(R.id.t3eh13)).setText(model.t3eh13);
        ((EditText)rootview.findViewById(R.id.t3eh14)).setText(model.t3eh14);

    }

}
