package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.freshliver.ashistant.assistant.AssistantFragments;
import com.freshliver.ashistant.assistant.CropImageViewInterface;
import com.freshliver.ashistant.assistant.AssistantFragmentSetter;
import com.freshliver.ashistant.utils.DatetimeUtils;
import com.freshliver.ashistant.utils.FileUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AssistantActivity extends AppCompatActivity implements CropImageViewInterface, AssistantFragmentSetter {

    public static final String SCREENSHOT_URI_STRING = "ScreenshotUri";

    protected Bitmap fullScreenshot;
    protected CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        // find items from layout
        this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);

        // set default fragment
        this.setFragment(AssistantFragments.Home);
    }


    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        this.setIntent(newIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.resetCropImageView(this.getIntent());
    }


    @Override
    public void setFragment(AssistantFragments type) {
        this.getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fabFragContainer, type.getFragment())
                .commit();
    }


    /**
     * CropImageView Interface Impl
     **/

    @Override
    public void resetCropImageView(Intent newIntent) {

        /* if new intent passed (onNewIntent called), get new  */
        if (newIntent != null) {
            /* get screenshot path from bundle and load it */
            String screenshotPath = newIntent.getStringExtra(AssistantActivity.SCREENSHOT_URI_STRING);
            this.fullScreenshot = FileUtils.loadBitmapFromFile(new File(screenshotPath));
        }

        this.cropImageView.setImageBitmap(this.fullScreenshot);
        this.cropImageView.setAutoZoomEnabled(true);
        this.cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        this.cropImageView.setGuidelines(CropImageView.Guidelines.OFF);

        /* set min size to image size to make no crop padding */
        this.cropImageView.resetCropRect();
    }


    @Override
    public void cropImage() {
        Bitmap cropped = this.cropImageView.getCroppedImage();
        this.cropImageView.setImageBitmap(cropped);
        this.cropImageView.resetCropRect();
    }


    @Override
    public void flipVertically() {
        this.cropImageView.flipImageVertically();
    }


    @Override
    public void flipHorizontally() {
        this.cropImageView.flipImageHorizontally();
    }


    @Override
    public void rotateLeft90() {
        this.cropImageView.rotateImage(-90);
    }


    @Override
    public void rotateRight90() {
        this.cropImageView.rotateImage(90);
    }


    @Override
    @SuppressLint("SimpleDateFormat")
    public void saveCroppedArea() {

        try {
            // try to save crop area to dst path
            File result = FileUtils.saveBitmapAsPNG(
                    this.cropImageView.getCroppedImage(),
                    FileUtils.getExternalDownloadFile(
                            String.format(
                                    "screenshot-%s.png",
                                    DatetimeUtils.formatCurrentTime("yyyyMMdd-HH-mm-ss")
                            )
                    )
            );

            // toast message hint saved path
            Toast.makeText(this, "Saved to " + result.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void shareCroppedArea() {
        try {
            // save cropped image to temp dir and get uri
            File croppedFile = FileUtils.saveBitmapAsPNG(
                    this.cropImageView.getCroppedImage(),
                    FileUtils.getInternalTempFile(this, "ashistant_cropped.png")
            );

            // start a share intent
            Intent shareIntent = new Intent()
                    .setAction(Intent.ACTION_SEND)
                    .setType("image/png")
                    .putExtra(Intent.EXTRA_STREAM, FileUtils.getInternalFileUri(this, croppedFile));
            this.startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}