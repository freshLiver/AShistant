package com.freshliver.ashistant;

import androidx.annotation.Nullable;
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
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AssistantActivity extends AppCompatActivity implements CropImageViewInterface, AssistantFragmentSetter {

    public static final String ScreenshotDataKey = "Screenshot";
    protected Bitmap fullScreenshot;

    protected CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        /* find items from layout */
        this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);
    }


    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        /* reset intent to new intent */
        this.resetCropImageView(newIntent);
    }


    @Override
    protected void onStart() {
        /* init crop image view */
        super.onStart();
        this.resetCropImageView(this.getIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.setFragment(AssistantFragments.Home);
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
    public void resetCropImageView(@Nullable Intent newIntent) {

        /* if new intent passed (onNewIntent called), get new  */
        if (newIntent != null) {
            /* extract screenshot from bundle and convert to image */
            byte[] screenshotData = newIntent.getByteArrayExtra(AssistantActivity.ScreenshotDataKey);
            this.fullScreenshot = BitmapFactory.decodeByteArray(screenshotData, 0, screenshotData.length);
        }

        this.cropImageView.setImageBitmap(this.fullScreenshot);
        this.cropImageView.setAutoZoomEnabled(true);
        this.cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        this.cropImageView.setGuidelines(CropImageView.Guidelines.ON);

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
        /* try to save crop area to dst path */
        this.saveBitmap(
                this.cropImageView.getCroppedImage(),
                new File(   /* specify target filename using current datetime */
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        String.format(
                                "screenshot-%s.png",
                                new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(Calendar.getInstance().getTime())
                        )
                )
        );
    }


    @Override
    public void shareCroppedArea() {

        /* get private temp files dir and check if it exists */
        final File tempFilesDir = new File(this.getFilesDir(), "temps");
        if (!tempFilesDir.exists() && !tempFilesDir.mkdir())
            Toast.makeText(this, "files/temps/ create failed.", Toast.LENGTH_SHORT).show();

        /* save cropped image to temp dir and get uri */
        Uri croppedURI = FileProvider.getUriForFile(
                this,
                "com.freshliver.ashistant.provider",
                this.saveBitmap(
                        this.cropImageView.getCroppedImage(),
                        new File(tempFilesDir, "ashistant_cropped.png")
                )
        );

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, croppedURI);
        startActivity(Intent.createChooser(shareIntent, "Share Screenshot"));
    }


    public File saveBitmap(Bitmap bitmap, File dstFile) {
        /* try to save crop area to dst path */
        try {
            /* specify crop tmp file and create if file not exists */
            if (!dstFile.exists() && !dstFile.createNewFile())
                throw new RuntimeException(String.format("Create File(%s) Failed.", dstFile.getAbsoluteFile()));

            /* output cropped area to target file */
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dstFile, false));

            /* finish assistant after screenshot saved */
            String successMsg = String.format("Saved to %s.", dstFile.toString());
            Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();
            return dstFile;

        }
        catch (IOException | RuntimeException e) {
            /* log and show error msg */
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}