package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import com.freshliver.ashistant.assistant.AssistantFragments;
import com.freshliver.ashistant.assistant.ResetCropImageViewListener;
import com.freshliver.ashistant.assistant.SetFragmentListener;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AssistantActivity extends AppCompatActivity implements ResetCropImageViewListener, SetFragmentListener {

    public static final String ScreenshotDataKey = "Screenshot";

    protected ViewGroup btnContainer;
    protected CropImageView cropImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);


        if (savedInstanceState == null) {

            /* find items from layout */
            this.btnContainer = this.findViewById(R.id.llBtnContainer_Assistant);
            this.cropImageView = this.findViewById(R.id.cropImageView_Assistant);

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


    @Override
    public void resetCropImageView(boolean editable) {
        /* extract screenshot from bundle and convert to image */
        byte[] screenshotData = this.getIntent().getByteArrayExtra(AssistantActivity.ScreenshotDataKey);
        Bitmap screenshot = BitmapFactory.decodeByteArray(screenshotData, 0, screenshotData.length);

        /* init and show screenshot  */
        this.cropImageView.setImageBitmap(screenshot);

        /* set min crop size if not editable */
        if (editable)
            this.cropImageView.setMinCropResultSize(1, 1);
        else
            this.cropImageView.setMinCropResultSize(screenshot.getWidth(), screenshot.getHeight());
    }
}