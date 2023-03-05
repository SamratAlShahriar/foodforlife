package tws.foodforlife.app.view.fragments.home;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import tws.foodforlife.app.R;
import tws.foodforlife.app.adapters.PagerAdapterForExplore;


public class ExploreFragment extends Fragment implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    private Activity activity;
    private Context context;
    private Button btnShare;
    private Button btnRequest;
    private BottomNavigationView bottomNavigationView;

    private ViewPager2 viewPager;
    private PagerAdapterForExplore adapterForExplore;

    private NavController navContForBtmNavMenu, navContForPostType;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            if (bottomNavigationView.getSelectedItemId() != R.id.exploreFragment) {
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnShare = view.findViewById(R.id.fe_btn_share);
        btnShare.setOnClickListener(this::onClick);

        btnRequest = view.findViewById(R.id.fe_btn_request);
        btnRequest.setOnClickListener(this::onClick);

        bottomNavigationView = view.findViewById(R.id.fe_bnv_btm_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
        navContForBtmNavMenu = Navigation.findNavController(view);
        NavigationUI.setupWithNavController(bottomNavigationView, navContForBtmNavMenu);


        viewPager = view.findViewById(R.id.fe_vp_posts);
        adapterForExplore = new PagerAdapterForExplore(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapterForExplore);
        viewPager.registerOnPageChangeCallback(vpCallback);
    }

    ViewPager2.OnPageChangeCallback vpCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (position == 0) {
                activeShareBtn();
            } else {
                activeRequestBtn();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.fe_btn_share:
                    activeShareBtn();
                    viewPager.setCurrentItem(0);
                    break;

                case R.id.fe_btn_request:
                    activeRequestBtn();
                    viewPager.setCurrentItem(1);
                    break;
            }
        }
    }

    private void activeRequestBtn() {
        btnShare.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        btnShare.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPostBtnBgInActive)));
        btnShare.setTypeface(null, Typeface.NORMAL);
        btnRequest.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnRequest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPostBtnBgActive)));
        btnRequest.setTypeface(null, Typeface.BOLD);
    }

    private void activeShareBtn() {
        btnShare.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnShare.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPostBtnBgActive)));
        btnShare.setTypeface(null, Typeface.BOLD);
        btnRequest.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        btnRequest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPostBtnBgInActive)));
        btnRequest.setTypeface(null, Typeface.NORMAL);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*if (item != null) {
            switch (item.getItemId()) {
                case R.id.newPostFragment:
                    navContForBtmNavMenu.navigate(R.id.action_exploreFragment_to_newPostFragment);
                    break;

                case R.id.exploreFragment:
                    break;

                case R.id.moreFragment:
                    navContForBtmNavMenu.navigate(R.id.action_exploreFragment_to_moreFragment);
                    break;
            }
        }*/
        return true;
    }
}