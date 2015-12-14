package com.bilkent.findnwear;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bilkent.findnwear.Fragments.ProfileFragment;
import com.bilkent.findnwear.Fragments.RecommendationFragment;
import com.bilkent.findnwear.Fragments.SearchFragment;
import com.bilkent.findnwear.Utilities.Storage;

import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // UI references.
    private ViewPager pager;
    private MainPagerAdapter pagerAdapter;
    private FrameLayout layoutTabHome;
    private FrameLayout layoutTabSearch;
    private FrameLayout layoutTabProfile;
    private RecommendationFragment recommendationFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutTabHome = (FrameLayout) findViewById(R.id.layoutTabHome);
        layoutTabSearch = (FrameLayout) findViewById(R.id.layoutTabSearch);
        layoutTabProfile = (FrameLayout) findViewById(R.id.layoutTabProfile);

        layoutTabHome.setOnClickListener(this);
        layoutTabSearch.setOnClickListener(this);
        layoutTabProfile.setOnClickListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateTabColor(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showFragmentAtPosition(0);

        Log.d("MAin", Storage.getUser().email);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.layoutTabHome:
                showFragmentAtPosition(0);
                break;
            case R.id.layoutTabSearch:
                showFragmentAtPosition(1);
                break;
            case R.id.layoutTabProfile:
                showFragmentAtPosition(2);
                break;
        }
    }

    public void showFragmentAtPosition(int pos) {

        pager.setCurrentItem(pos);
        updateTabColor(pos);
    }

    private void updateTabColor(int pos) {
        getSupportActionBar().setTitle(pagerAdapter.getPageTitle(pos));
        switch(pos) {
            case 0:
                layoutTabHome.setBackgroundResource(R.drawable.tab_element_bg_active);
                layoutTabSearch.setBackgroundResource(R.drawable.tab_element_bg_passive);
                layoutTabProfile.setBackgroundResource(R.drawable.tab_element_bg_passive);
                break;
            case 1:
                layoutTabHome.setBackgroundResource(R.drawable.tab_element_bg_passive);
                layoutTabSearch.setBackgroundResource(R.drawable.tab_element_bg_active);
                layoutTabProfile.setBackgroundResource(R.drawable.tab_element_bg_passive);
                break;
            case 2:
                layoutTabHome.setBackgroundResource(R.drawable.tab_element_bg_passive);
                layoutTabSearch.setBackgroundResource(R.drawable.tab_element_bg_passive);
                layoutTabProfile.setBackgroundResource(R.drawable.tab_element_bg_active);
                break;
        }
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(recommendationFragment ==null) {
                        recommendationFragment = new RecommendationFragment();
                    }
                    return recommendationFragment;
                case 1:
                    if(searchFragment==null) {
                        searchFragment = new SearchFragment();
                    }
                    return searchFragment;
                case 2:
                    if(profileFragment==null) {
                        profileFragment = new ProfileFragment();
                    }
                    return profileFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.home).toUpperCase(l);
                case 1:
                    return getString(R.string.search).toUpperCase(l);
                case 2:
                    return getString(R.string.profile).toUpperCase(l);
            }
            return null;
        }
    }
}

