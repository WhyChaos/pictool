package com.example.hc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hc.utils.add.AddText;
import com.example.hc.utils.current_url;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {

    Uri uri;
    //图片组件
    private ImageView imageView;
    //位于图片中的文本组件
    private TextView tvInImage;
    //图片和文本的父组件
    private View containerView;
    //父组件的尺寸
    private float imageWidth, imageHeight, imagePositionX, imagePositionY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        imageView = (ImageView) findViewById(R.id.writeText_img);
        EditText editText = (EditText) findViewById(R.id.writeText_et);
        tvInImage = (TextView) findViewById(R.id.writeText_image_tv);
        containerView = findViewById(R.id.writeText_img_rl);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                imagePositionX = imageView.getX();
                imagePositionY = imageView.getY();
                imageWidth = imageView.getWidth();
                imageHeight = imageView.getHeight();
                //设置文本大小
                tvInImage.setMaxWidth((int) imageWidth);
            }
        });
        uri = current_url.uri;
        imageView.setImageURI(uri);
        findViewById(R.id.btn).setOnClickListener(this);
//        imageView.setImageBitmap(getScaledBitmap(R.mipmap.test_img));
        //输入框
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    tvInImage.setVisibility(View.INVISIBLE);
                } else {
                    tvInImage.setVisibility(View.VISIBLE);
                    tvInImage.setText(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final GestureDetector gestureDetector = new GestureDetector(this, new SimpleGestureListenerImpl());
        //移动
        tvInImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                Bitmap bitmap = loadBitmapFromView(containerView);
                current_url.uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
                finish();
                break;


        }
    }
    //以图片形式获取View显示的内容（类似于截图）
    public static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private int count = 0;
    //tvInImage的x方向和y方向移动量
    private float mDx, mDy;

    //移动
    private class SimpleGestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //向右移动时，distanceX为负；向左移动时，distanceX为正
            //向下移动时，distanceY为负；向上移动时，distanceY为正
            count++;
            mDx -= distanceX;
            mDy -= distanceY;
            //边界检查
            mDx = calPosition(imagePositionX - tvInImage.getX(), imagePositionX + imageWidth - (tvInImage.getX() + tvInImage.getWidth()), mDx);
            mDy = calPosition(imagePositionY - tvInImage.getY(), imagePositionY + imageHeight - (tvInImage.getY() + tvInImage.getHeight()), mDy);
            //控制刷新频率
            if (count % 5 == 0) {
                tvInImage.setX(tvInImage.getX() + mDx);
                tvInImage.setY(tvInImage.getY() + mDy);
            }
            return true;
        }
    }
    //计算正确的显示位置（不能超出边界）
    private float calPosition(float min, float max, float current) {
        if (current < min) {
            return min;
        }
        if (current > max) {
            return max;
        }
        return current;
    }
    //获取压缩后的bitmap
    private Bitmap getScaledBitmap(int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, opt);
        opt.inSampleSize = AddText.calculateInSampleSize(opt, 600, 800);
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resId, opt);
    }
}