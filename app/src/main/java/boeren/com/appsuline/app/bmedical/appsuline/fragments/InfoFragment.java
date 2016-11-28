package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.InfoListAdapter;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.views.AnimatedExpandableListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   // private ListView listView;
    private InfoListAdapter mAdapter;
    Button btnOpen, btnCancel;
    Dialog dialog;
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    android.support.v4.app.Fragment fragment = null;
    private boolean isDualPan = false;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(getActivity().findViewById(R.id.container2) != null)isDualPan = true;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_info, container, false);

        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView = (AnimatedExpandableListView) rootview.findViewById(R.id.infolist);
        getActivity().setTitle(R.string.info);

        List<GroupItem> items = new ArrayList<GroupItem>();
        // Header Algemene Informatie
        GroupItem groupAlgemen = new GroupItem();
        groupAlgemen.title = getString(R.string.Algemene);

        ChildItem carbohydrates = new ChildItem();
        carbohydrates.title = getString(R.string.title_carbohydrates);
        groupAlgemen.items.add(carbohydrates);

        ChildItem diabetes = new ChildItem();
        diabetes.title = getString(R.string.Diabetes);
        groupAlgemen.items.add(diabetes);


        ChildItem injectielocaties = new ChildItem();
        injectielocaties.title = getString(R.string.Injectielocaties);
        groupAlgemen.items.add(injectielocaties);


        ChildItem injectietechniek = new ChildItem();
        injectietechniek.title = getString(R.string.Injectietechniek);
        groupAlgemen.items.add(injectietechniek);

        ChildItem naaldlengte = new ChildItem();
        naaldlengte.title = getString(R.string.Naaldlengte);
        groupAlgemen.items.add(naaldlengte);

        ChildItem insulinepomp = new ChildItem();
        insulinepomp.title = getString(R.string.Insulinepomp);
        groupAlgemen.items.add(insulinepomp);

        ChildItem prikken = new ChildItem();
        prikken.title = getString(R.string.Prikken);
        groupAlgemen.items.add(prikken);

        ChildItem wisselen = new ChildItem();
        wisselen.title = getString(R.string.Wisselen);
        groupAlgemen.items.add(wisselen);


        ChildItem hypo = new ChildItem();
        hypo.title = getString(R.string.Hypo);
        groupAlgemen.items.add(hypo);


        ChildItem hyper = new ChildItem();
        hyper.title = getString(R.string.Hyper);
        groupAlgemen.items.add(hyper);


        items.add(groupAlgemen);

        // Header Instellingen Informatie
        GroupItem instellingen = new GroupItem();
        instellingen.title = getString(R.string.Instellingen);

        ChildItem gebruikers = new ChildItem();
        gebruikers.title = getString(R.string.users);
        instellingen.items.add(gebruikers);

        ChildItem persoonlijkegegevens = new ChildItem();
        persoonlijkegegevens.title = getString(R.string.Persoonlijkegegevens);
        instellingen.items.add(persoonlijkegegevens);

        ChildItem streefwaardes = new ChildItem();
        streefwaardes.title = getString(R.string.Streefwaardes);
        instellingen.items.add(streefwaardes);

        ChildItem insulineschema = new ChildItem();
        insulineschema.title = getString(R.string.Insulineschema);
        instellingen.items.add(insulineschema);

        ChildItem v4pairing = new ChildItem();
        v4pairing.title = getString(R.string.v4meter);
        instellingen.items.add(v4pairing);

        items.add(instellingen);

        // Header Help Informatie
        GroupItem help = new GroupItem();
        help.title = getString(R.string.Help);

        ChildItem handleiding = new ChildItem();
        handleiding.title = getString(R.string.Handleiding);
        help.items.add(handleiding);

        ChildItem meldingsstijlaanpassen = new ChildItem();
        meldingsstijlaanpassen.title = getString(R.string.Meldingsstijlaanpassen);
        help.items.add(meldingsstijlaanpassen);

        ChildItem bluetoothmeterkoppelen = new ChildItem();
        bluetoothmeterkoppelen.title = getString(R.string.Bluetoothmeterkoppelen);
        help.items.add(bluetoothmeterkoppelen);

        items.add(help);

        // Header Boeren Medical Informatie
        GroupItem boerenmedical = new GroupItem();
        boerenmedical.title = getString(R.string.BorenMedical);

        ChildItem overBoeren = new ChildItem();
        overBoeren.title = getString(R.string.OverBoerenMedical);
        boerenmedical.items.add(overBoeren);

        ChildItem website = new ChildItem();
        website.title = getString(R.string.Website);
        boerenmedical.items.add(website);

        ChildItem webshop = new ChildItem();
        webshop.title = getString(R.string.Webshop);
        boerenmedical.items.add(webshop);

        ChildItem feedback = new ChildItem();
        feedback.title = getString(R.string.Feedback);
        boerenmedical.items.add(feedback);

        ChildItem disclaimer = new ChildItem();
        disclaimer.title = getString(R.string.Disclaimer);
        boerenmedical.items.add(disclaimer);

        items.add(boerenmedical);

        adapter = new ExampleAdapter(getActivity());
        adapter.setData(items);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean  onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String tag = "fragment";
               User user = null;
                // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
              // System.out.println(groupPosition +"and "+ childPosition);
                switch (groupPosition)
                {
                    case 0:
                        switch (childPosition)
                        {
                            case 0:
                                fragment = new ProductsInfoFragment();
                                break;
                            case 1:
                                fragment = new DiabetesFragment();
                                break;
                            case 2:
                                fragment = new InjectielocatiesFragment();
                                break;
                            case 3:
                                fragment = new InjectietechniekFragment();
                                break;
                            case 4:
                                fragment = new NaaldlengteFragment();
                                break;
                            case 5:
                                fragment = new InsulinePompFragment();
                                break;
                            case 6:
                                fragment = new PrikkenFragment();
                                break;
                            case 7:
                                fragment = new WisselenFragment();
                                break;
                            case 8:
                                fragment = new HypoFragment();
                                break;
                            case 9:
                                fragment = new HyperFragment();
                                break;

                        }
                       break;
                    case 1:
                        switch (childPosition) {
                            case 0:
                                fragment= new AccountFragment();
                                break;
                            case 1:

                                if(MainActivity.userCount>0){
                                    user = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
                                }
                                if(user!=null)
                                    fragment=PersonalInfoFragment.newInstance(user);
                                else
                                    fragment= new PersonalInfoFragment();
                                break;
                            case 2:
                                if(MainActivity.userCount>0){
                                    user = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
                                }
                                if(user!=null)
                                    fragment=TargetsFragment.newInstance(user);
                                else
                                    fragment= new TargetsFragment();
                                break;

                            case 3:
                                if(MainActivity.userCount>0){
                                    user = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
                                }
                                if(user!=null) {
                                   if(!user.isNeedle())
                                      fragment = new InsulinePompSchemaFragment();
                                    else
                                       fragment = new InsulineSpuitSchemaFragment();
                                }
                                break;
                            case 4:
                                 fragment = Meterv4fragment.newInstance();
                                break;
                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                fragment = new HandleidingFragment();
                                break;
                            case 1:
                                fragment = new MeldingsstijlFragment();
                                break;
                            case 2:
                                fragment = new BluetoothmeterFragment();
                                break;
                        }
                        break;
                    case 3:
                        switch (childPosition) {
                            case 0:
                                fragment = new OverBoerenMedicalFragment();
                                break;
                            case 1:
                                showWebsiteDialog();
                                if( getFragmentManager().getBackStackEntryCount()>0)
                                    getFragmentManager().popBackStack();
                                fragment =null;

                                    break;
                            case 2:
                                showWebshopDialog();
                                if( getFragmentManager().getBackStackEntryCount()>0)
                                    getFragmentManager().popBackStack();
                                fragment =null;

                                break;
                            case 3:
                                fragment = new FeedbackFragment();
                                break;
                            case 4:
                                fragment = new DisclaimerFragment();
                                break;
                        }
                      break;

                }
               if (fragment != null) {
                   int containerId = R.id.container;
                   if(isDualPan){
                       containerId = R.id.container2;
                       fragmentPopUp();
                   }
                   getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                           .replace(containerId, fragment).addToBackStack(null)
                           .commit();
                   adapter.notifyDataSetInvalidated();
               }
               return false;

           }

        });
        return rootview;
    }


    private void fragmentPopUp(){
        if( getFragmentManager().getBackStackEntryCount()>0)
            getFragmentManager().popBackStack();
    }


    protected void showWebsiteDialog() {

        dialog = new Dialog(getActivity(),
                android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_dialog);
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
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.websiteurl)));
                startActivity(intent);
            }

        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        dialog.show();
    }
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


    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
               // holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
           // holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


}


