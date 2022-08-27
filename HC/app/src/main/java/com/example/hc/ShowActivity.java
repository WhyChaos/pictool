package com.example.hc;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.hc.utils.current_url;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import com.example.hc.utils.imageName;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri uri;
    private ImageView pic;
    static private int crop_code=100;
    private ActivityResultLauncher<Intent> register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_show);
        pic = findViewById(R.id.pic);
        findViewById(R.id.btn_crop).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_modify).setOnClickListener(this);
        findViewById(R.id.btn_text).setOnClickListener(this);
        findViewById(R.id.btn_mosaic).setOnClickListener(this);
        findViewById(R.id.btn_stitch).setOnClickListener(this);

        //获得上一个activity传来的bundle
        Bundle bundle = getIntent().getExtras();
        uri = Uri.parse(bundle.getString("uri"));
        Log.d("huang","Show");
        Log.d("huang", uri.toString());
        pic.setImageURI(uri);
        current_url.uri = uri;
    }

    protected  void onRestart(){
        super.onRestart();
        Log.d("huang","show Restart");
        uri = current_url.uri;
        pic.setImageURI(uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_crop:
                start_Ucrop();
                break;
            case R.id.btn_filter:
                start_filter();
                break;
            case R.id.btn_add:
                start_add();
                break;
            case R.id.btn_modify:
                start_modify();
                break;
            case R.id.btn_text:
                start_text();
                break;
            case R.id.btn_mosaic:
                start_mosaic();
                break;
            case R.id.btn_stitch:
                start_stitch();
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("huang","回传");
        if(requestCode == crop_code){
            if (resultCode == RESULT_OK && requestCode == crop_code) {
                Log.d("huang","裁剪显示");
                final Uri resultUri = UCrop.getOutput(data);
                pic.setImageURI(resultUri);
                uri=resultUri;
                current_url.uri = uri;
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        }

    }


    public void start_Ucrop(){
        //源图片uri
        Uri sourceUri = uri;

        //保存后图片uri
        Uri destinationUri = Uri.fromFile(new File(this.getCacheDir(), imageName.get_imageName()+".jpeg"));
        Log.d("huang",destinationUri.toString());
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("裁剪");
        UCrop.of(sourceUri, destinationUri).withOptions(options).start(this,crop_code);
    }

    public void start_filter(){
        current_url.uri = uri;
        Intent intent = new Intent(this,FilterActivity.class);

        startActivity(intent);
    }
    public void start_add(){
        current_url.uri = uri;
        Intent intent = new Intent(this, GraffitiActivity.class);

        startActivity(intent);
    }
    public void start_modify(){
        current_url.uri = uri;
        Intent intent = new Intent(this,ModifyActivity.class);

        startActivity(intent);
    }
    public void start_text(){
        current_url.uri = uri;
        Intent intent = new Intent(this,TextActivity.class);

        startActivity(intent);
    }
    public void start_mosaic(){
        current_url.uri = uri;
        Intent intent = new Intent(this, MosaicActivity.class);

        startActivity(intent);
    }
    public void start_stitch(){
        current_url.uri = uri;
        Intent intent = new Intent(this, StitchActivity.class);

        startActivity(intent);
    }

}