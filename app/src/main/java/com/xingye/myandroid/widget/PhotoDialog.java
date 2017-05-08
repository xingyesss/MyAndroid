package com.xingye.myandroid.widget;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.xingye.myandroid.R;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;
import rx.functions.Action1;

/**
 * Created by sunxiquan on 2017/3/22.
 */

public class PhotoDialog extends Dialog implements View.OnClickListener {
    /**拍照标识*/
    public static final int TAKE_PICTURE = 0x0000010;

    private TextView tvCamera;
    private TextView tvPhoto;
    private TextView tvCancel;
    private RxPermissions rxPermissions;
    private Activity context;
    private CameraListener cameraListener;
    private int selectNumber = 1;
    private ArrayList<String> list = null;
    private String[] Cameras = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    public PhotoDialog(Activity context, int selectNumber, ArrayList<String> list) {
        super(context, R.style.dialog_bg);
        this.context = context;
        this.selectNumber = selectNumber;
        this.list = list;
    }

    public PhotoDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_photo_select,null);
        setContentView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvCamera = (TextView) findViewById(R.id.tv_camera);
        tvPhoto = (TextView) findViewById(R.id.tv_photo);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(context).request(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if(isShowing()){
                                    dismiss();
                                }
                                if(aBoolean){
                                   /* boolean isAll = false;
                                    for(String permission : Cameras){
                                        int i= ContextCompat.checkSelfPermission(context,permission);
                                        if(i != PackageManager.PERMISSION_GRANTED){
                                            isAll = false;
                                            break;
                                        }else{
                                            isAll = true;
                                        }
                                    }
                                    if(isAll){
                                        openCamera();
                                    }else{
                                        settingPermissionDialog(context.getResources().getString(R.string.permission_prompt));
                                    }*/
                                    openCamera();
                                }else{
                                    settingPermissionDialog(context.getResources().getString(R.string.permission_prompt));
                                }
                            }
                        });
            }
        });
        tvPhoto.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x6000));
        WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.x = 0;
        wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        onWindowAttributesChanged(wl);
        rxPermissions = new RxPermissions(context);
        rxPermissions.setLogging(true);
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
                //PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult();
                new RxPermissions(context).request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(isShowing()){
                            dismiss();
                        }
                        if(aBoolean){
                            openCamera();
                        }else{
                            settingPermissionDialog(getContext().getResources().getString(R.string.permission_prompt));
                        }
                    }
                });
                break;
            case R.id.tv_photo:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if(isShowing()){
                                    dismiss();
                                }
                                if(aBoolean){
                                    if(list!=null) {
                                        PhotoPicker.builder().setPhotoCount(selectNumber)
                                                .setShowCamera(true)
                                                .setShowGif(false)
                                                .setPreviewEnabled(true)
                                                .setSelected(list)
                                                .start(context, PhotoPicker.REQUEST_CODE);
                                    }else{
                                        PhotoPicker.builder().setPhotoCount(selectNumber)
                                                .setShowCamera(true)
                                                .setShowGif(false)
                                                .setPreviewEnabled(true)
                                                .start(context, PhotoPicker.REQUEST_CODE);
                                    }
                                }else{
                                    settingPermissionDialog(getContext().getResources().getString(R.string.permission_prompt1));
                                }
                            }
                        });
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    /**设置权限弹窗*/
    private void settingPermissionDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getContext().getResources().getString(R.string.prompt));
        builder.setMessage(msg);
        builder.setPositiveButton(getContext().getResources().getString(R.string.text_ok), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                if(Build.VERSION.SDK_INT>=9) {
                    Uri packageURI = Uri.parse("package:" + context.getPackageName());
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    context.startActivity(intent);
                }else if(Build.VERSION.SDK_INT<=8){
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
                    context.startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getContext().getString(R.string.text_cancel),null);
        builder.show();
    }
    /**打开相机*/
    private void openCamera() {
        String imgName = System.currentTimeMillis()+".jpg";
        File dir = new File(Environment.getExternalStorageDirectory().getPath(),"DCIM");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File imgFile = new File(dir,imgName);
        if(imgFile.exists()){
            imgFile.delete();
        }

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA,imgFile.getAbsolutePath());
        Uri imgUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues); //Uri.fromFile(imgFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        context.startActivityForResult(openCameraIntent, TAKE_PICTURE);
        if(cameraListener!=null) {
            cameraListener.camera(imgFile.getPath());
        }
    }

    public interface CameraListener{
        void camera(String path);
    }
}
