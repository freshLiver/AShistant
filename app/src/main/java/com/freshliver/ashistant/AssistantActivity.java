package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
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
    protected Bitmap screenshot;

    protected ViewGroup btnContainer;
    protected CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);


        if (savedInstanceState == null) {

            /* extract screenshot from bundle and convert to image */
            byte[] screenshotData = this.getIntent().getByteArrayExtra(AssistantActivity.ScreenshotDataKey);
            this.screenshot = BitmapFactory.decodeByteArray(screenshotData, 0, screenshotData.length);

            /* find items from layout */
            this.btnContainer = this.findViewById(R.id.llBtnContainer_Assistant);
            this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);

            /* init crop image view */
            this.resetCropImageView();

            /* set home fragment */
            setFragment(AssistantFragments.Home);
            setFragment(AssistantFragments.Editor);
            setFragment(AssistantFragments.Home);

        }
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
    public void resetCropImageView() {

        this.cropImageView.setImageBitmap(this.screenshot);
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
    public void saveCroppedArea() {

        /* specify target filename using current datetime */
        @SuppressLint("SimpleDateFormat")
        File dstFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                String.format(
                        "screenshot-%s.png",
                        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())
                )
        );

        /* try to save crop area to dst path */
        try {
            /* specify crop tmp file and create if file not exists */
            if (!dstFile.exists() && !dstFile.createNewFile())
                throw new RuntimeException(String.format("Create File(%s) Failed.", dstFile.getAbsoluteFile()));

            /* output cropped area to target file */
            this.cropImageView.getCroppedImage()
                    .compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dstFile, false));

            /* finish assistant after screenshot saved */
            String successMsg = String.format("Screenshot saved to %s.", dstFile.toString());
            Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();

        } catch (IOException | RuntimeException e) {
            /* log and show error msg */
            e.printStackTrace();
            Toast.makeText(this, "Failed to save screenshot.", Toast.LENGTH_SHORT).show();
        }
    }
}