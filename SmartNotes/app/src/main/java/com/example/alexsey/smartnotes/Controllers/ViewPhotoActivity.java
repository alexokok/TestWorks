package com.example.alexsey.smartnotes.Controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.alexsey.smartnotes.Helpers.PictureUtils;
import com.example.alexsey.smartnotes.R;

import java.io.File;
import java.util.regex.Pattern;

public class ViewPhotoActivity extends AppCompatActivity {

    /** файл с изображением */
    private String mPhotoFile;

    private Button mCloseButton;

    private ImageView mPhotoView;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        Bundle b = getIntent().getExtras();

        mPhotoFile = b.getString(PictureUtils.PHOTO_PATH);

        String[] photos = mPhotoFile.split(Pattern.quote("/"));

        mPhotoView = (ImageView) findViewById(R.id.photo);

        this.setTitle(photos[photos.length - 1]);

        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile, this);
        mPhotoView.setImageBitmap(bitmap);
    }
}
