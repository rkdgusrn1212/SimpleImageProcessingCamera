package com.khgkjg12.simpleimageprocessor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private Camera mCamera;
    private CameraPreview mPreview;
    private final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(requestRuntimePermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION)){
            init();
        }
    }

    /**
     * initiate this activity.
     */
    private void init(){
        if(checkCameraHardware(this)){
            mCamera = getCameraInstance();
            if(mCamera==null){
                Toast.makeText(this, R.string.camera_is_not_available_error, Toast.LENGTH_SHORT).show();
            }else {
                mPreview = new CameraPreview(this, mCamera);
                RelativeLayout preview = findViewById(R.id.camera_preview_relative_layout);
                preview.addView(mPreview,0);
                mCamera.setDisplayOrientation(90);
                Button button  = findViewById(R.id.image_processor_menu_toggle_button);
            }
        }else{
            Toast.makeText(this, R.string.no_camera_error, Toast.LENGTH_LONG).show();
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
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();// attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public boolean requestRuntimePermission(final String permission,final int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                final Activity thisAcvity = this;
                AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(R.string.camera_permission_specific).setTitle(R.string.camera_permission_title).setNeutralButton(R.string.close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(thisAcvity,
                                new String[]{permission},
                                requestCode);
                    }
                }).setCancelable(true).create();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ActivityCompat.requestPermissions(thisAcvity,
                                new String[]{permission},
                                requestCode);
                    }
                });
                alertDialog.show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        requestCode);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, R.string.camera_permission_denied_error,Toast.LENGTH_SHORT).show();
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
