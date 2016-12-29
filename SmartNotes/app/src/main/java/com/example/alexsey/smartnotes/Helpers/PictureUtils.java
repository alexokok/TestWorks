package com.example.alexsey.smartnotes.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.alexsey.smartnotes.Controllers.ViewPhotoActivity;

import java.io.File;

/**
 * генерирует интент для вызова камеры
 */
public class PictureUtils {

    public static final String PHOTO_PATH = "com.example.alexsey.smartnotes.file";

    /** формирует интент для вызова камеры
     * @param file - файл, в который будет писаться наша фотография
     */
    public static Intent getCameraIntent(File file){
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = Uri.fromFile(file);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return captureImage;
    }

    /** возвращает интент для вызова активности просмотра изображения */
    public static Intent getViewImageIntent(Context context, String photoPath){
        Intent intent = new Intent(context, ViewPhotoActivity.class);
        intent.putExtra(PHOTO_PATH, photoPath);

        return intent;
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if(srcHeight > destHeight || srcWidth > destWidth){
            if(srcWidth > srcHeight)
                inSampleSize = Math.round(srcHeight/destHeight);
            else
                inSampleSize = Math.round(srcWidth/destWidth);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);

    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();

        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
