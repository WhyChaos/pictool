package com.example.hc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.hc.utils.Uri2Bitmap;
import com.example.hc.utils.add.DrawMosaicView;
import com.example.hc.utils.add.MosaicUtil;
import com.example.hc.utils.current_url;


public class MosaicActivity extends AppCompatActivity implements  View.OnClickListener{

    private DrawMosaicView mosaic;
    private Button okBtn;

    private int mWidth, mHeight;

    Bitmap srcBitmap = null;
    Uri uri;

    int size = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mosaic);

        uri = current_url.uri;
        initView();

//        mosaic.setMosaicBackgroundResource(uri.toString());
        mosaic.setMosaicBackgroundResource(uri,this);
        srcBitmap  = Uri2Bitmap.ImageSizeCompress(uri,this);
//        srcBitmap = BitmapFactory.decodeFile(uri.toString());

        Log.d("huang","1");
        mWidth = srcBitmap.getWidth();
        mHeight = srcBitmap.getHeight();
        Log.d("huang","2");
        Bitmap bit = MosaicUtil.getMosaic(srcBitmap);
        Log.d("huang","3");

        mosaic.setMosaicResource(bit);
        mosaic.setMosaicBrushWidth(10);
        Log.d("huang","4");
    }

    private void initView()
    {
        mosaic =  findViewById(R.id.mosaic);


        okBtn =  findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);

        findViewById(R.id.action_base).setOnClickListener(this);
        findViewById(R.id.action_ground_glass).setOnClickListener(this);
        findViewById(R.id.action_flower).setOnClickListener(this);
        findViewById(R.id.action_size_up).setOnClickListener(this);
        findViewById(R.id.action_size_down).setOnClickListener(this);
        findViewById(R.id.action_eraser).setOnClickListener(this);
        findViewById(R.id.action_rainbow).setOnClickListener(this);
        findViewById(R.id.action_beauty).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_ok:
                Bitmap bit = mosaic.getMosaicBitmap();
                current_url.uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bit, null,null));
                finish();
//
//                FileUtils.writeImage(bit, mPath, 100);
//
//                Intent okData = new Intent();
//                okData.putExtra("camera_path", mPath);
//                setResult(RESULT_OK, okData);
//                recycle();
//                MosaicActivity.this.finish();
                break;
            case R.id.action_base:
                Bitmap bitmapMosaic = MosaicUtil.getMosaic(srcBitmap);
                mosaic.setMosaicResource(bitmapMosaic);
                break;
            case R.id.action_ground_glass:
                Bitmap bitmapBlur = MosaicUtil.getBlur(srcBitmap);
                mosaic.setMosaicResource(bitmapBlur);
                break;
            case R.id.action_flower:
                Bitmap bit_flower = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.c);
                bit_flower = ResizeBitmap(bit_flower, mWidth, mHeight);
                mosaic.setMosaicResource(bit_flower);
                break;
            case R.id.action_rainbow:
                Bitmap bit_rainbow = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.d);
                bit_rainbow = ResizeBitmap(bit_rainbow, mWidth, mHeight);
                mosaic.setMosaicResource(bit_rainbow);
                break;
            case R.id.action_beauty:
                Bitmap bit_beauty = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.e);
                bit_beauty = ResizeBitmap(bit_beauty, mWidth, mHeight);
                mosaic.setMosaicResource(bit_beauty);
                break;
            case R.id.action_size_up:
                if (size < 50){
                    size += 5;
                }
                mosaic.setMosaicBrushWidth(size);
                break;
            case R.id.action_size_down:
                if (size > 5){
                    size -= 5;
                }
                mosaic.setMosaicBrushWidth(size);
                break;
            case R.id.action_eraser:
                mosaic.setMosaicType(MosaicUtil.MosaicType.ERASER);
                break;
            default:

                break;
        }
    }
    private void recycle()
    {
        if (srcBitmap != null)
        {
            srcBitmap.recycle();
            srcBitmap = null;
        }
    }
    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }
}