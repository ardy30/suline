package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.InsulinPompShemaModel;
import boeren.com.appsuline.app.bmedical.appsuline.models.InsulinSpuitShemaModel;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsulinePompSchemaFragment extends android.support.v4.app.Fragment {


    private View rootview;
    public InsulinePompSchemaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview =  inflater.inflate(R.layout.fragment_insuline_pomp_schema, container, false);
        rootview.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String insulineSpuitJson=populatePompModelFromView().toJson();
                if(MainActivity.userCount>0) {

                    User activeUser = BaseController.getInstance().getActiveUser();
                    if(activeUser==null)
                        activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
                    activeUser.setInsulinPompInfo(insulineSpuitJson);
                    BaseController.getInstance().getDbManager(getActivity()).getUserTable().update(activeUser);
                    getActivity().onBackPressed();
                }

            }
        });

        User activeUser = BaseController.getInstance().getActiveUser();
        if(activeUser==null)
            activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        if(activeUser!=null) {
            String spuitjson = activeUser.getInsulinPompInfo();
            InsulinPompShemaModel model = null;
            if (null == spuitjson) {
                model = new InsulinPompShemaModel();
            } else {
                model = InsulinPompShemaModel.fromJson(spuitjson);
            }
            populateViewFromPompModel(model);
        }
        return  rootview;
    }


    public InsulinPompShemaModel populatePompModelFromView(){
        InsulinPompShemaModel model=new InsulinPompShemaModel();
        model.t1r21=((EditText)rootview.findViewById(R.id.t1r21)).getText().toString();
        model.t1r22=((EditText)rootview.findViewById(R.id.t1r22)).getText().toString();
        model.t1r23=((EditText)rootview.findViewById(R.id.t1r23)).getText().toString();
        model.t1r31=((EditText)rootview.findViewById(R.id.t1r31)).getText().toString();
        model.t1r32=((EditText)rootview.findViewById(R.id.t1r32)).getText().toString();
        model.t1r33=((EditText)rootview.findViewById(R.id.t1r33)).getText().toString();
        model.t1r41=((EditText)rootview.findViewById(R.id.t1r41)).getText().toString();
        model.t1r42=((EditText)rootview.findViewById(R.id.t1r42)).getText().toString();
        model.t1r43=((EditText)rootview.findViewById(R.id.t1r43)).getText().toString();
        model.t1r51=((EditText)rootview.findViewById(R.id.t1r51)).getText().toString();
        model.t1r52=((EditText)rootview.findViewById(R.id.t1r52)).getText().toString();
        model.t1r53=((EditText)rootview.findViewById(R.id.t1r53)).getText().toString();
        model.t1r61=((EditText)rootview.findViewById(R.id.t1r61)).getText().toString();
        model.t1r62=((EditText)rootview.findViewById(R.id.t1r62)).getText().toString();
        model.t1r63=((EditText)rootview.findViewById(R.id.t1r63)).getText().toString();
        model.t1r71=((EditText)rootview.findViewById(R.id.t1r71)).getText().toString();
        model.t1r72=((EditText)rootview.findViewById(R.id.t1r72)).getText().toString();
        model.t1r73=((EditText)rootview.findViewById(R.id.t1r73)).getText().toString();

        model.t2eh11=((EditText)rootview.findViewById(R.id.t2eh11)).getText().toString();
        model.t2eh12=((EditText)rootview.findViewById(R.id.t2eh12)).getText().toString();
        model.t2eh13=((EditText)rootview.findViewById(R.id.t2eh13)).getText().toString();
        model.t2eh14=((EditText)rootview.findViewById(R.id.t2eh14)).getText().toString();
        model.t2eh15=((EditText)rootview.findViewById(R.id.t2eh15)).getText().toString();
        model.t2eh15a=((EditText)rootview.findViewById(R.id.t2eh15a)).getText().toString();
        model.t2eh16=((EditText)rootview.findViewById(R.id.t2eh16)).getText().toString();
        model.t3eh11=((EditText)rootview.findViewById(R.id.t3eh11)).getText().toString();
        model.t3eh12=((EditText)rootview.findViewById(R.id.t3eh12)).getText().toString();
        model.t3eh13=((EditText)rootview.findViewById(R.id.t3eh13)).getText().toString();
        model.t3eh14=((EditText)rootview.findViewById(R.id.t3eh14)).getText().toString();
        model.t3eh15=((EditText)rootview.findViewById(R.id.t3eh15)).getText().toString();
        model.t4eh11=((EditText)rootview.findViewById(R.id.t4eh11)).getText().toString();
        model.t4eh12=((EditText)rootview.findViewById(R.id.t4eh12)).getText().toString();
        model.t4eh13=((EditText)rootview.findViewById(R.id.t4eh13)).getText().toString();
        model.t4eh14=((EditText)rootview.findViewById(R.id.t4eh14)).getText().toString();
        model.t4eh21=((EditText)rootview.findViewById(R.id.t4eh21)).getText().toString();
        model.t4eh22=((EditText)rootview.findViewById(R.id.t4eh22)).getText().toString();
        model.t4eh23=((EditText)rootview.findViewById(R.id.t4eh23)).getText().toString();
        model.t4eh24=((EditText)rootview.findViewById(R.id.t4eh24)).getText().toString();
        model.t4eh31=((EditText)rootview.findViewById(R.id.t4eh31)).getText().toString();
        model.t4eh32=((EditText)rootview.findViewById(R.id.t4eh32)).getText().toString();
        model.t4eh33=((EditText)rootview.findViewById(R.id.t4eh33)).getText().toString();
        model.t4eh34=((EditText)rootview.findViewById(R.id.t4eh34)).getText().toString();
        model.t4eh41=((EditText)rootview.findViewById(R.id.t4eh41)).getText().toString();
        model.t4eh42=((EditText)rootview.findViewById(R.id.t4eh42)).getText().toString();
        model.t4eh43=((EditText)rootview.findViewById(R.id.t4eh43)).getText().toString();
        model.t4eh44=((EditText)rootview.findViewById(R.id.t4eh44)).getText().toString();
        model.t5eh11=((EditText)rootview.findViewById(R.id.t5eh11)).getText().toString();
        model.t5eh12=((EditText)rootview.findViewById(R.id.t5eh12)).getText().toString();
        model.t5eh13=((EditText)rootview.findViewById(R.id.t5eh13)).getText().toString();
        model.t5eh14=((EditText)rootview.findViewById(R.id.t5eh14)).getText().toString();
      /*  model.t5eh15=((EditText)rootview.findViewById(R.id.t5eh15)).getText().toString();
        model.t5eh16=((EditText)rootview.findViewById(R.id.t5eh16)).getText().toString();*/
       // model.t5eh17=((EditText)rootview.findViewById(R.id.t5eh17)).getText().toString();
        model.t6eh11=((EditText)rootview.findViewById(R.id.t6eh11)).getText().toString();
        model.t6eh12=((EditText)rootview.findViewById(R.id.t6eh12)).getText().toString();
        model.t6eh13=((EditText)rootview.findViewById(R.id.t6eh13)).getText().toString();
        model.t6eh14=((EditText)rootview.findViewById(R.id.t6eh14)).getText().toString();
      /*  model.t6eh15=((EditText)rootview.findViewById(R.id.t6eh15)).getText().toString();
        model.t6eh16=((EditText)rootview.findViewById(R.id.t6eh16)).getText().toString();
        model.t6eh17=((EditText)rootview.findViewById(R.id.t6eh17)).getText().toString();*/

        return model;
    }

    public void populateViewFromPompModel(InsulinPompShemaModel model){

        ((EditText)rootview.findViewById(R.id.t1r21)).setText(model.t1r21);
        ((EditText)rootview.findViewById(R.id.t1r22)).setText(model.t1r22);
        ((EditText)rootview.findViewById(R.id.t1r23)).setText(model.t1r23);
        ((EditText)rootview.findViewById(R.id.t1r31)).setText(model.t1r31);
        ((EditText)rootview.findViewById(R.id.t1r32)).setText(model.t1r32);
        ((EditText)rootview.findViewById(R.id.t1r33)).setText(model.t1r33);
        ((EditText)rootview.findViewById(R.id.t1r41)).setText(model.t1r41);
        ((EditText)rootview.findViewById(R.id.t1r42)).setText(model.t1r42);
        ((EditText)rootview.findViewById(R.id.t1r43)).setText(model.t1r43);
        ((EditText)rootview.findViewById(R.id.t1r51)).setText(model.t1r51);
        ((EditText)rootview.findViewById(R.id.t1r52)).setText(model.t1r52);
        ((EditText)rootview.findViewById(R.id.t1r53)).setText(model.t1r53);
        ((EditText)rootview.findViewById(R.id.t1r61)).setText(model.t1r61);
        ((EditText)rootview.findViewById(R.id.t1r62)).setText(model.t1r62);
        ((EditText)rootview.findViewById(R.id.t1r63)).setText(model.t1r63);
        ((EditText)rootview.findViewById(R.id.t1r71)).setText(model.t1r71);
        ((EditText)rootview.findViewById(R.id.t1r72)).setText(model.t1r72);
        ((EditText)rootview.findViewById(R.id.t1r73)).setText(model.t1r73);

        ((EditText)rootview.findViewById(R.id.t2eh11)).setText(model.t2eh11);
        ((EditText)rootview.findViewById(R.id.t2eh12)).setText(model.t2eh12);
        ((EditText)rootview.findViewById(R.id.t2eh13)).setText(model.t2eh13);
        ((EditText)rootview.findViewById(R.id.t2eh14)).setText(model.t2eh14);
        ((EditText)rootview.findViewById(R.id.t2eh15)).setText(model.t2eh15);
        ((EditText)rootview.findViewById(R.id.t2eh15a)).setText(model.t2eh15a);
        ((EditText)rootview.findViewById(R.id.t2eh16)).setText(model.t2eh16);
        ((EditText)rootview.findViewById(R.id.t3eh11)).setText(model.t3eh11);
        ((EditText)rootview.findViewById(R.id.t3eh12)).setText(model.t3eh12);
        ((EditText)rootview.findViewById(R.id.t3eh13)).setText(model.t3eh13);
        ((EditText)rootview.findViewById(R.id.t3eh14)).setText(model.t3eh14);
        ((EditText)rootview.findViewById(R.id.t3eh15)).setText(model.t3eh15);
        ((EditText)rootview.findViewById(R.id.t4eh11)).setText(model.t4eh11);
        ((EditText)rootview.findViewById(R.id.t4eh12)).setText(model.t4eh12);
        ((EditText)rootview.findViewById(R.id.t4eh13)).setText(model.t4eh13);
        ((EditText)rootview.findViewById(R.id.t4eh14)).setText(model.t4eh14);
        ((EditText)rootview.findViewById(R.id.t4eh21)).setText(model.t4eh21);
        ((EditText)rootview.findViewById(R.id.t4eh22)).setText(model.t4eh22);
        ((EditText)rootview.findViewById(R.id.t4eh23)).setText(model.t4eh23);
        ((EditText)rootview.findViewById(R.id.t4eh24)).setText(model.t4eh24);
        ((EditText)rootview.findViewById(R.id.t4eh31)).setText(model.t4eh31);
        ((EditText)rootview.findViewById(R.id.t4eh32)).setText(model.t4eh32);
        ((EditText)rootview.findViewById(R.id.t4eh33)).setText(model.t4eh33);
        ((EditText)rootview.findViewById(R.id.t4eh34)).setText(model.t4eh34);
        ((EditText)rootview.findViewById(R.id.t4eh41)).setText(model.t4eh41);
        ((EditText)rootview.findViewById(R.id.t4eh42)).setText(model.t4eh42);
        ((EditText)rootview.findViewById(R.id.t4eh43)).setText(model.t4eh43);
        ((EditText)rootview.findViewById(R.id.t4eh44)).setText(model.t4eh44);
        ((EditText)rootview.findViewById(R.id.t5eh11)).setText(model.t5eh11);
        ((EditText)rootview.findViewById(R.id.t5eh12)).setText(model.t5eh12);
        ((EditText)rootview.findViewById(R.id.t5eh13)).setText(model.t5eh13);
        ((EditText)rootview.findViewById(R.id.t5eh14)).setText(model.t5eh14);
       /* ((EditText)rootview.findViewById(R.id.t5eh15)).setText(model.t5eh15);
        ((EditText)rootview.findViewById(R.id.t5eh16)).setText(model.t5eh16);*/
        //((EditText)rootview.findViewById(R.id.t5eh17)).setText(model.t5eh17);
        ((EditText)rootview.findViewById(R.id.t6eh11)).setText(model.t6eh11);
        ((EditText)rootview.findViewById(R.id.t6eh12)).setText(model.t6eh12);
        ((EditText)rootview.findViewById(R.id.t6eh13)).setText(model.t6eh13);
        ((EditText)rootview.findViewById(R.id.t6eh14)).setText(model.t6eh14);
     /*   ((EditText)rootview.findViewById(R.id.t6eh15)).setText(model.t6eh15);
        ((EditText)rootview.findViewById(R.id.t6eh16)).setText(model.t6eh16);
        ((EditText)rootview.findViewById(R.id.t6eh17)).setText(model.t6eh17);*/

    }

}
