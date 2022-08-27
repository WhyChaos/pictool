package com.example.hc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.example.hc.utils.add.PaintView;
import com.example.hc.utils.current_url;

public class GraffitiActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnUndo;
    private Button btnDo;
    private Button btnRed;
    private Button btnBlue;
    private Button btnGreen;
    private Button btnScral; //放大。
    private Button btnEraser; //橡皮擦。
    private PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graffiti);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        initView();
        initListener();


    }





    private void initListener() {
        btnUndo.setOnClickListener(this);
        btnDo.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnScral.setOnClickListener(this);
        btnEraser.setOnClickListener(this);

        findViewById(R.id.btn_ok).setOnClickListener(this);
    }

    private void initView() {
        paintView = (findViewById(R.id.paint_view));
        btnUndo = findViewById(R.id.btn_undo);
        btnDo = findViewById(R.id.btn_do);
        btnRed = findViewById(R.id.btn_red);
        btnBlue = findViewById(R.id.btn_blue);
        btnGreen = findViewById(R.id.btn_green);
        btnScral = findViewById(R.id.btn_scral);
        btnEraser =  findViewById(R.id.btn_eraser);
        paintView.setImageURI(current_url.uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_undo:
                paintView.undo();
                return;
            case R.id.btn_do:
                paintView.reundo();
                return;
            case R.id.btn_red:
                paintView.resetPaintColor(Color.RED);
                return;
            case R.id.btn_green:
                paintView.resetPaintColor(Color.GREEN);
                return;
            case R.id.btn_blue:
                paintView.resetPaintColor(Color.BLUE);
                return;
            case R.id.btn_scral:
                paintView.resetPaintWidth();
                break;
            case R.id.btn_eraser:
                paintView.eraser();
                break;
            case R.id.btn_ok:
                Bitmap bitmap = loadBitmapFromView(paintView);
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
}