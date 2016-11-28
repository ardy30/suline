package boeren.com.appsuline.app.bmedical.appsuline.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.adapters.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandleidingFragment extends android.support.v4.app.Fragment {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    int[] flag;
    LinearLayout llDots;
    Button btnNext;
    public HandleidingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_handleiding, container, false);
        llDots=(LinearLayout) rootview.findViewById(R.id.llDots);
        viewPager = (ViewPager) rootview.findViewById(R.id.pager);
        btnNext = (Button) rootview.findViewById(R.id.btnNext);
        Button btnEnd= (Button) viewPager.findViewById(R.id.btnEndTutorial);

        if(MainActivity.isFirstStart)
            btnNext.setVisibility(View.VISIBLE);

        // declare tutorial images
        flag = new int[] { R.drawable.kalendar, R.drawable.dag,
                R.drawable.herinneringen, R.drawable.bluetooth,
                R.drawable.informatie,R.drawable.users,0 };



        adapter = new ViewPagerAdapter(getActivity(), flag);
        // click listener for last button
        adapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPopUp();
                android.support.v4.app.Fragment fragment = new DiaryFragment();
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(fragment)
                            .replace(R.id.container, fragment)
                            .commit();

                }

            }
        });

        viewPager.setAdapter(adapter);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                jumpToPage(view);
            }

        });

        for (int i = 0; i < adapter.getCount(); i++)
        {
            ImageButton imgDot = new ImageButton(getActivity());
            imgDot.setTag(i);
            imgDot.setImageResource(R.drawable.dot_selector);
            imgDot.setBackgroundResource(0);
            imgDot.setPadding(5, 5, 5, 5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            imgDot.setLayoutParams(params);
            if(i == 0)
                imgDot.setSelected(true);

            llDots.addView(imgDot);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int pos)
            {
                Log.e("", "Page Selected is ===> " + pos);
                for (int i = 0; i < adapter.getCount(); i++)
                {
                    if(i != pos)
                    {
                        ((ImageView)llDots.findViewWithTag(i)).setSelected(false);
                    }
                }
                ((ImageView)llDots.findViewWithTag(pos)).setSelected(true);
                if(viewPager.getCurrentItem()==6)
                    btnNext.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });
        return rootview;
    }
    private void fragmentPopUp(){
        if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    public void jumpToPage(View view) {

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        if(viewPager.getCurrentItem()==6)
            btnNext.setVisibility(View.INVISIBLE);

    }

}
