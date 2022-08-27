package com.example.hc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.hc.utils.GPUImageUtil;
import com.example.hc.utils.ImageFilter;
import com.example.hc.utils.Uri2Bitmap;
import com.example.hc.utils.current_url;

import java.io.IOException;
import java.io.InputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;


public class FilterActivity extends AppCompatActivity implements View.OnClickListener{
    public Uri uri;
    public ImageView pic;
    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_filter);
        uri = current_url.uri;
        pic = findViewById(R.id.pic);
        pic.setImageURI(uri);

        findViewById(R.id.origin_btn).setOnClickListener(this);
        findViewById(R.id.sepia_btn).setOnClickListener(this);
        findViewById(R.id.gray_btn).setOnClickListener(this);
        findViewById(R.id.sketch_btn).setOnClickListener(this);
        findViewById(R.id.emboss_btn).setOnClickListener(this);
        findViewById(R.id.softlight_btn).setOnClickListener(this);
        findViewById(R.id.haze_btn).setOnClickListener(this);
        findViewById(R.id.cga_btn).setOnClickListener(this);
        findViewById(R.id.toon_btn).setOnClickListener(this);
        findViewById(R.id.bulge_btn).setOnClickListener(this);
        findViewById(R.id.colorin_btn).setOnClickListener(this);
        findViewById(R.id.glass_btn).setOnClickListener(this);
        findViewById(R.id.swirl_btn).setOnClickListener(this);

    }

    protected  void onPause(){
        super.onPause();
        current_url.uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
        Log.d("huang","onpause");
    }




    @Override
    public void onClick(View view) {
        GPUImage gpuImage = null;
        bitmap = Uri2Bitmap.ImageSizeCompress(uri,this);
        switch (view.getId()){
            case R.id.origin_btn://原图
                pic.setImageBitmap(bitmap);
                break;
            case R.id.sepia_btn://怀旧
                bitmap = ImageFilter.OldRemeberImage(bitmap);
                pic.setImageBitmap(bitmap);

                break;
            case R.id.gray_btn://黑白
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Gray);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.sketch_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Sketch);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.emboss_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Emboss);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.softlight_btn://光照
                bitmap = ImageFilter.SunshineImage(bitmap);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.haze_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Haze);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.cga_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.CGA);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.toon_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Toon);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.bulge_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Bulge);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.colorin_btn:
                bitmap = ImageFilter.FilmFilterImage(bitmap);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.glass_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Glass);
                pic.setImageBitmap(bitmap);
                break;
            case R.id.swirl_btn:
                bitmap = GPUImageUtil.getGpuImage(bitmap, this,gpuImage,GPUImageUtil.FilterEnum.Swirl);
                pic.setImageBitmap(bitmap);
                break;
        }
    }


//    private Bitmap ImageSizeCompress(Uri uri){
//        InputStream Stream = null;
//        InputStream inputStream = null;
//        try {
//            //根据uri获取图片的流
//            inputStream = getContentResolver().openInputStream(uri);
//            BitmapFactory.Options options = new BitmapFactory.Options();
////            //options的in系列的设置了，injustdecodebouond只解析图片的大小，而不加载到内存中去
////            options.inJustDecodeBounds = true;
////            //1.如果通过options.outHeight获取图片的宽高，就必须通过decodestream解析同options赋值
////            //否则options.outheight获取不到宽高
////            BitmapFactory.decodeStream(inputStream,null,options);
////            //2.通过 btm.getHeight()获取图片的宽高就不需要1的解析，我这里采取第一张方式
//////            Bitmap btm = BitmapFactory.decodeStream(inputStream);
////            //以屏幕的宽高进行压缩
////            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
////            int heightPixels = displayMetrics.heightPixels;
////            int widthPixels = displayMetrics.widthPixels;
////            //获取图片的宽高
////            int outHeight = options.outHeight;
////            int outWidth = options.outWidth;
////            //heightPixels就是要压缩后的图片高度，宽度也一样
////            int a = (int) Math.ceil((outHeight/(float)heightPixels));
////            int b = (int) Math.ceil(outWidth/(float)widthPixels);
////            //比例计算,一般是图片比较大的情况下进行压缩
////            int max = Math.max(a, b);
////            if(max > 1){
////                options.inSampleSize = max;
////            }
////            //解析到内存中去
////            options.inJustDecodeBounds = false;
////            根据uri重新获取流，inputstream在解析中发生改变了
//            Stream = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(Stream, null, options);
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if(inputStream != null) {
//                    inputStream.close();
//                }
//                if(Stream != null){
//                    Stream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return  null;
//    }

}