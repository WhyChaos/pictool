package com.example.hc.utils;

import android.content.Context;
import android.graphics.Bitmap;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;


public class GPUImageUtil {

    private static GPUImageFilter filter;

    public enum FilterEnum{
        Sepia, Gray, ColorInvert, Sketch, Emboss, SoftLight, HardLight, Haze, CGA, Toon, Bulge, ColorIn,
        Glass, Swirl
    }

    public static GPUImageFilter createFilterForType(FilterEnum GPUFlag){
        switch (GPUFlag){
//            case Sepia:
//                filter = new GPUImageSepiaFilter();//褐色(怀旧)
//                break;
            case Gray:
                filter = new GPUImageGrayscaleFilter();//灰
                break;
            case ColorInvert:
                filter = new GPUImageColorInvertFilter();//反相
                break;
            case Sketch:
                filter = new GPUImageSketchFilter();//素描
                break;
            case Emboss:
                filter = new GPUImageEmbossFilter();//浮雕
                break;
//            case SoftLight:
//                filter = new GPUImageSoftLightBlendFilter();//光照
//                break;
//            case HardLight:
//                filter = new GPUImageHardLightBlendFilter();//强光
//                break;
            case Haze:
                filter = new GPUImageHazeFilter();//朦胧
                break;
            case CGA:
                filter = new GPUImageCGAColorspaceFilter();//CGA
                break;
            case Toon:
                filter = new GPUImageToonFilter();//卡通
                break;
            case Bulge:
                filter = new GPUImageBulgeDistortionFilter();//鱼眼
                break;
//            case ColorIn:
//                filter = new GPUImageColorInvertFilter();//反色
//                break;
            case Glass:
                filter = new GPUImageGlassSphereFilter();//水晶球
                break;
            case Swirl:
                filter = new GPUImageSwirlFilter();//旋涡
                break;
        }
        return filter;
    }

    public static Bitmap getGpuImage(Bitmap bitmap, Context context, GPUImage gpuImage, FilterEnum FilterFlag){
        //使用GPUImage处理图像
        gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(createFilterForType(FilterFlag));
        bitmap = gpuImage.getBitmapWithFilterApplied();
        return bitmap;
    }
}

