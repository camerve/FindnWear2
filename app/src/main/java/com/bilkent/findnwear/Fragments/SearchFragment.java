package com.bilkent.findnwear.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bilkent.findnwear.KeywordActivity;
import com.bilkent.findnwear.R;
import com.bilkent.findnwear.SearchActivity;
import com.bilkent.findnwear.Utilities.CameraPreview;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private static final int REQUEST_PERMISSIONS = 1;
    private View rootView;
    private ImageView imageView;
    private Button cameraButton;
    private Button galleryButton;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Button keywordButton;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        EasyImage.configuration(getActivity())
                .setImagesFolderName("FindNWear Images")
                .saveInAppExternalFilesDir();

        imageView = (ImageView) rootView.findViewById(R.id.image_view);

        cameraButton = (Button) rootView.findViewById(R.id.camera_button);
        galleryButton = (Button) rootView.findViewById(R.id.gallery_button);
        keywordButton = (Button) rootView.findViewById(R.id.keyword_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EasyImage.openCamera(SearchFragment.this);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(SearchFragment.this);
            }
        });

        keywordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KeywordActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        requestCameraGallery();
    }

    @Override
    public void onResume(){
        super.onResume();
        mCamera = getCameraInstance();

        if(mCamera!=null) {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getActivity(), mCamera);
            FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
            preview.removeAllViews();
            preview.addView(mPreview);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source) {
                //Handlne the image
                onPhotoReturned(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    private void onPhotoReturned(File photoFile) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("File", photoFile);
        startActivity(intent);
    }

    private boolean requestCameraGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPermissionTaken();
            }
            if (grantResults.length >= 2 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                galleryPermissionTaken();
            }
        }
    }

    private void galleryPermissionTaken() {

    }

    private void cameraPermissionTaken() {
        mCamera = getCameraInstance();

        if(mCamera!=null) {
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getActivity(), mCamera);
            FrameLayout preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
            preview.removeAllViews();
            preview.addView(mPreview);
        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
