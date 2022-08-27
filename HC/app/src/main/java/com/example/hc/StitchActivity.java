package com.example.hc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hc.utils.MergeBitmap;
import com.example.hc.utils.Uri2Bitmap;
import com.example.hc.utils.current_url;
import com.example.hc.utils.imageName;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

public class StitchActivity extends AppCompatActivity implements View.OnClickListener{
    //权限请求码
    static int camera_code =100;

    PhotoView pic;
    Uri uri;
    Bitmap bitmap;

    Uri uri2;
    Bitmap bitmap2;

    boolean LR = true;
    Button btn_LR ;

    private ActivityResultLauncher<Intent> register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stitch);

        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        findViewById(R.id.album).setOnClickListener(this);
        findViewById(R.id.camera).setOnClickListener(this);
        findViewById(R.id.OK).setOnClickListener(this);
        pic = findViewById(R.id.pic);
        btn_LR = findViewById(R.id.btn_LR);
        btn_LR.setOnClickListener(this);

        uri = current_url.uri;
        bitmap = Uri2Bitmap.ImageSizeCompress(uri,this);

        pic.setImageURI(uri);

        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result != null){
                Intent intent = result.getData();
                if(intent != null && result.getResultCode() == Activity.RESULT_OK){
                    uri2 = intent.getData();
                    Log.d("huang",uri2.toString());
                    start_merge();
                }
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_LR:
                LR = !LR;
                if(LR)  btn_LR.setText("左右");
                else    btn_LR.setText("上下");
                break;
            case R.id.album:
                Log.d("huang","执行Onclick");
                //请求存储权限
//        requestPermission();
                //打开相册
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
//        startActivityForResult(intent, OPEN_ALBUM_CODE);//过时
                register.launch(intent);
                break;
            case R.id.camera:
                start_camera();
                break;
            case R.id.OK:
                current_url.uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                finish();
                break;
        }
    }

    public void start_camera(){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT; // 获取版本号
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用相机
        File tempFile = new File(this.getCacheDir(), imageName.get_imageName()+".jpeg"); // 建立文件的保存路径
        Log.d("huang",tempFile.toString());
        Log.d("huang",String.valueOf(currentapiVersion));
        if (currentapiVersion < 24) { // Android 7.0 以下版本的设置方式
            uri2 = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2); // 配置了之后，在 onActivityResult 中返回的 data 为 null
        } else { // 兼容 Android 7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
            uri2 = getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
        }
        startActivityForResult(intent, camera_code); // 加载相机 Activity
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == camera_code){
            start_merge();
//            pic.setImageURI(destinationUri);
        }
    }

    public void start_merge(){
        bitmap2 = Uri2Bitmap.ImageSizeCompress(uri2,this);
        bitmap = MergeBitmap.mergeBitmap(bitmap,bitmap2,LR);
        pic.setImageBitmap(bitmap);
    }
}