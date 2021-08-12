package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AssistantActivity extends AppCompatActivity {

    public static final String ScreenshotDataKey = "Screenshot";

    protected ViewGroup btnContainer;
    protected CropImageView cropImageView;
    protected FloatingActionButton saveCroppedArea, uploadCroppedArea;
    protected FloatingActionButton shareCroppedArea, editScreenshot, discardScreenshot;


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
        this.saveCroppedArea = this.btnContainer.findViewById(R.id.fabSaveCroppedArea);
        this.uploadCroppedArea = this.btnContainer.findViewById(R.id.fabUploadCroppedArea);
        this.shareCroppedArea = this.btnContainer.findViewById(R.id.fabShareCroppedArea);

        /* set onclick functions */
        this.btnContainer.setOnClickListener((view) -> this.finish());
        this.editScreenshot.setOnClickListener((view) -> {
        });
        this.saveCroppedArea.setOnClickListener((view) -> {

            /* specify target filename using current datetime */
            @SuppressLint("SimpleDateFormat")
            File dstFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    String.format(
                            "screenshot-%s.png",
                            new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())
                    )
            );

            /* save crop area to dst file */
            String msg = "Save File To " + dstFile.toString();
            try {
                saveBitmapTo(this.cropImageView.getCroppedImage(), dstFile);

            } catch (IOException | RuntimeException e) {

                e.printStackTrace();
                msg += " Failed";
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


        });
        this.uploadCroppedArea.setOnClickListener((view) -> {
        });
        this.shareCroppedArea.setOnClickListener((view) -> {

        });
    }


    protected void saveBitmapTo(Bitmap bitmap, File dst) throws IOException {

        /* specify crop tmp file and create if file not exists */
        if (!dst.exists() && !dst.createNewFile())
            throw new RuntimeException(String.format("Create File(%s) Failed.", dst.getAbsoluteFile()));

        /* output cropped area to target file */
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dst, false));
    }
}