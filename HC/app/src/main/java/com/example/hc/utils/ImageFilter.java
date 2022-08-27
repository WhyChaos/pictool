package com.example.hc.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImageFilter {
    public static Bitmap OldRemeberImage(Bitmap bmp)
    {
        /*
         * 怀旧处理算法即设置新的RGB
         * R=0.393r+0.769g+0.189b
         * G=0.349r+0.686g+0.168b
         * B=0.272r+0.534g+0.131b
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ?
                        255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    //图片光照效果
    public static Bitmap SunshineImage(Bitmap bmp)
    {
        /*
         * 算法原理：(前一个像素点RGB-当前像素点RGB+127)作为当前像素点RGB值
         * 在ABC中计算B点浮雕效果(RGB值在0~255)
         * B.r = C.r - B.r + 127
         * B.g = C.g - B.g + 127
         * B.b = C.b - B.b + 127
         * 光照中心取长宽较小值为半径,也能够自己定义从左上角射过来
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        //环绕圆形光照
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);
        float strength = 150F;  //光照强度100-150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1; i < height-1; i++)
        {
            for (int k = 1; k < width-1; k++)
            {
                //获取前一个像素颜色
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                //计算当前点到光照中心的距离,平面坐标系中两点之间的距离
                int distance = (int) (Math.pow((centerY-i), 2) + Math.pow((centerX-k), 2));
                if(distance < radius*radius)
                {
                    //依照距离大小计算增强的光照值
                    int result = (int)(strength*( 1.0-Math.sqrt(distance) / radius ));
                    newR = pixR + result;
                    newG = newG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[width * i + k] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    //底片效果
    public static Bitmap FilmFilterImage(Bitmap bmp){
        int width = bmp.getWidth();
        int height= bmp.getHeight();
        int color;
        int r,g,b,a;
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        int[] oldPx = new int[width*height];
        int[] newPx = new int[width*height];

        bmp.getPixels(oldPx,0,width,0,0,width,height);
        for(int i =0; i < width*height;i++){
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = 255-r;
            g = 255-g;
            b = 255  - b;
            if(r > 255){
                r = 255;
            }else if(r <0){
                r = 0;
            }
            if(g > 255){
                g = 255;
            }else if(g <0){
                g = 0;
            }
            if(b > 255){
                b = 255;
            }else if(b <0){
                b = 0;
            }
            newPx[i]  = Color.argb(a,r,g,b);
        }
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }

}
