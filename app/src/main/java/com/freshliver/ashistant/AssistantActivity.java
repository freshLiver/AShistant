package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import com.freshliver.ashistant.assistant.AssistantFragments;
import com.freshliver.ashistant.assistant.CropImageViewInterface;
import com.freshliver.ashistant.assistant.AssistantFragmentSetter;
import com.theartofdev.edmodo.cropper.CropImageView;

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
}