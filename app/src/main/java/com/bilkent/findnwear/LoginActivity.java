package com.bilkent.findnwear;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.bilkent.findnwear.Fragments.LoginFragment;
import com.bilkent.findnwear.Fragments.RegisterFragment;
import com.bilkent.findnwear.Utilities.Storage;

import java.io.IOException;
import java.util.Locale;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    // UI references.

    private SurfaceView mSurfaceView;
    private MediaPlayer mp;
    private SurfaceHolder holder;
    private ViewPager pager;
    private LoginFragment loginFragment;
    private LoginPagerAdapter pagerAdapter;
    private RegisterFragment registerFragment;
    private FrameLayout layoutTabLogin;
    private FrameLayout layoutTabRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Storage.getUser()!=null){
            gotoMainActivity();
            finish();
            return;
        }

        mSurfaceView = (SurfaceView)findViewById(R.id.surface);

        mp = new MediaPlayer();

        holder = mSurfaceView.getHolder();
        holder.setFixedSize(800, 480);
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // Set up the login form

        layoutTabLogin = (FrameLayout) findViewById(R.id.layoutTabLogin);
        layoutTabRegister = (FrameLayout) findViewById(R.id.layoutTabRegister);

        layoutTabLogin.setOnClickListener(this);
        layoutTabRegister.setOnClickListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch(position) {
                    case 0:
                        layoutTabLogin.setBackgroundResource(R.drawable.tab_element_bg_active);
                        layoutTabRegister.setBackgroundResource(R.drawable.tab_element_bg_passive);
                        break;
                    case 1:
                        layoutTabLogin.setBackgroundResource(R.drawable.tab_element_bg_passive);
                        layoutTabRegister.setBackgroundResource(R.drawable.tab_element_bg_active);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        showFragmentAtPosition(0);

    }

    private void gotoMainActivity() {
        Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Uri video = Uri.parse("android.resource://com.bilkent.findnwear/"
                + R.raw.newyork);

        try {
            mp.setDisplay(holder);
            mp.setDataSource(this, video);
            mp.prepare();
            mp.setLooping(true);
            Log.d("video", "" + video.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Get the dimensions of the video
        int videoWidth = mp.getVideoHeight();
        int videoHeight = mp.getVideoWidth();
        Log.d("video", "" + videoWidth + " " + videoHeight);

        //Start video
        mp.start();
        Log.d("video", "" + mp.isPlaying());
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.layoutTabLogin:
                showFragmentAtPosition(0);
                break;
            case R.id.layoutTabRegister:
                showFragmentAtPosition(1);
                break;
        }
    }

    public void showFragmentAtPosition(int pos) {

        pager.setCurrentItem(pos);

        switch(pos) {
            case 0:
                layoutTabLogin.setBackgroundResource(R.drawable.tab_element_bg_active);
                layoutTabRegister.setBackgroundResource(R.drawable.tab_element_bg_passive);
                break;
            case 1:
                layoutTabLogin.setBackgroundResource(R.drawable.tab_element_bg_passive);
                layoutTabRegister.setBackgroundResource(R.drawable.tab_element_bg_active);
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    private class LoginPagerAdapter extends FragmentStatePagerAdapter {
        public LoginPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if(loginFragment==null) {
                        loginFragment = new LoginFragment();
                    }
                    return loginFragment;
                case 1:
                    if(registerFragment==null) {
                        registerFragment = new RegisterFragment();
                    }
                    return registerFragment;
                default:
                    return loginFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.home).toUpperCase(l);
                case 1:
                    return getString(R.string.profile).toUpperCase(l);
            }
            return null;
        }
    }
}

