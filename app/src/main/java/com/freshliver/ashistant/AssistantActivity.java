package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImageView;

public class AssistantActivity extends AppCompatActivity {

    public static final String ScreenshotDataKey = "Screenshot";

    protected ViewGroup layout;
    protected CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        /* find items from layout */
        this.layout = this.findViewById(R.id.layoutAssistant);
        this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);

        /* extract screenshot from bundle and convert to image */
        byte[] screenshotData = this.getIntent().getByteArrayExtra(AssistantActivity.ScreenshotDataKey);
        Bitmap screenshot = BitmapFactory.decodeByteArray(screenshotData, 0, screenshotData.length);

        /* set imageview image and adjust iv width */
        this.cropImageView.setImageBitmap(screenshot);

        /* set onclick functions */
        this.layout.setOnClickListener((view) -> this.finish());
    }

}