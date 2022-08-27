package com.example.hc;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.example.hc.utils.imageName;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //权限请求码
    static int camera_code =100;

    private ActivityResultLauncher<Intent> register;
    private Uri destinationUri;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
//        requestPermission();
        setContentView(R.layout.activity_main);
        findViewById(R.id.album).setOnClickListener(this);
        findViewById(R.id.camera).setOnClickListener(this);
        pic = findViewById(R.id.pic);


        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result != null){
                Intent intent = result.getData();
                if(intent != null && result.getResultCode() == Activity.RESULT_OK){
                    Uri uri = intent.getData();
                    Log.d("huang",uri.toString());
                    start_ShowActivity(uri);
                }
            }

        });

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.album){
            Log.d("huang","执行Onclick");
            //请求存储权限
//        requestPermission();
            //打开相册
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
//        startActivityForResult(intent, OPEN_ALBUM_CODE);//过时
            register.launch(intent);
        }else{
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT; // 获取版本号
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用相机
//            String fileName = System.currentTimeMillis() + ".jpg"; // 使用系统时间来对照片进行命名，保证惟一性
//            File destinationFile = new File(this.getCacheDir(), get_imageName()+".jpeg"); // 创建文件的保存路径
//            if (currentapiVersion < 24) { // Android 7.0 如下版本的设置方式
//                Uri fileUri = Uri.fromFile(destinationFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // 配置了以后，在 onActivityResult 中返回的 data 为 null
//            } else { // 兼容 Android 7.0 使用共享文件的形式
//                ContentValues contentValues = new ContentValues(1);
//                contentValues.put(MediaStore.Images.Media.DATA, destinationFile.getAbsolutePath());
//                Uri fileUri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            }
//            destinationUri = Uri.fromFile(destinationFile);
//            startActivityForResult(intent, camera_code); // 加载相机 Activity
            int currentapiVersion = android.os.Build.VERSION.SDK_INT; // 获取版本号
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用相机
            File tempFile = new File(this.getCacheDir(), imageName.get_imageName()+".jpeg"); // 建立文件的保存路径
            Log.d("huang",tempFile.toString());
            Log.d("huang",String.valueOf(currentapiVersion));
            if (currentapiVersion < 24) { // Android 7.0 以下版本的设置方式
                destinationUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationUri); // 配置了之后，在 onActivityResult 中返回的 data 为 null
            } else { // 兼容 Android 7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                destinationUri = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationUri);
            }
            startActivityForResult(intent, camera_code); // 加载相机 Activity

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == camera_code){
            start_ShowActivity(destinationUri);
//            pic.setImageURI(destinationUri);
        }

    }


    public  void start_ShowActivity(Uri uri){
//        Pic.setImageURI(uri);
        Log.d("huang","展示图片");
        Intent intent = new Intent(this,ShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("uri",uri.toString());

        Log.d("huang",uri.toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }




    //图片剪裁
//    private void pictureCropping(Uri uri) {
//        // 调用系统中自带的图片剪裁
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 2);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 300);
//        // 返回裁剪后的数据
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PICTURE_CROPPING_CODE);
//    }

    //重写onActivityResult方法。
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == OPEN_ALBUM_CODE && resultCode == RESULT_OK) {
//            final Uri imageUri = Objects.requireNonNull(data).getData();
//            //显示图片
////            Glide.with(this).load(imageUri).into(Pic);
//            Uri sourceUri = imageUri;
//            SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
//            Date date = new Date();
//            String imageName = simpleDateFormat.format(date);
//            Uri destinationUri = Uri.fromFile(new File(this.getCacheDir(), imageName+".jpeg"));
//            UCrop.of(sourceUri, destinationUri).start(this,200);
////            pictureCropping(imageUri);
//
//
//        } else if (requestCode == PICTURE_CROPPING_CODE && resultCode == RESULT_OK) {
//            //图片剪裁返回
//            Bundle bundle = data.getExtras();
//            if (bundle != null) {
//                //在这里获得了剪裁后的Bitmap对象，可以用于上传
//                Bitmap image = bundle.getParcelable("data");
//                //设置到ImageView上
//                Pic.setImageBitmap(image);
//            }
//        }
//    }


    //权限申请
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // 将结果转发给 EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//    @AfterPermissionGranted(REQUEST_STORAGE_CODE)
//    private void requestPermission(){
//        String[] param = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if(EasyPermissions.hasPermissions(this,param)){
//            //已有权限
//            Log.d("huang","已有权限");
////            showMsg("已获得权限");
//        }else {
//            //无权限 则进行权限请求
//            Log.d("huang","无权限");
//            EasyPermissions.requestPermissions(this,"请求权限",REQUEST_STORAGE_CODE,param);
//        }
//    }
//    private void showMsg(String msg){
//        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
//    }

}