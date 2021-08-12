package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AssistantActivity extends AppCompatActivity {

    public static final String ScreenshotDataKey = "Screenshot";

    protected ViewGroup btnContainer;
    protected CropImageView cropImageView;
    protected FloatingActionButton saveSelectedRange, uploadSelectedRange;
    protected FloatingActionButton shareSelectedRange, editScreenshot, discardScreenshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        initComponents();

        /* extract screenshot from bundle and convert to image */
        byte[] screenshotData = this.getIntent().getByteArrayExtra(AssistantActivity.ScreenshotDataKey);
        Bitmap screenshot = BitmapFactory.decodeByteArray(screenshotData, 0, screenshotData.length);

        /* show screenshot and default */
        this.cropImageView.setImageBitmap(screenshot);
//        this.cropImageView.
    }


    public void initComponents() {
        /* find items from layout */
        this.btnContainer = this.findViewById(R.id.llBtnContainer_Assistant);
        this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);

        this.discardScreenshot = this.btnContainer.findViewById(R.id.fabDiscardScreenshot);
        this.editScreenshot = this.btnContainer.findViewById(R.id.fabEditScreenshot);
        this.saveSelectedRange = this.btnContainer.findViewById(R.id.fabSaveSelectedRange);
        this.uploadSelectedRange = this.btnContainer.findViewById(R.id.fabUploadSelectedRange);
        this.shareSelectedRange = this.btnContainer.findViewById(R.id.fabShareSelectedRange);

        /* set onclick functions */
        this.btnContainer.setOnClickListener((view) -> this.finish());
        this.editScreenshot.setOnClickListener((view) -> {

        });
        this.saveSelectedRange.setOnClickListener((view) -> {
        });
        this.uploadSelectedRange.setOnClickListener((view) -> {
        });
        this.shareSelectedRange.setOnClickListener((view) -> {

        });
    }
}