package com.freshliver.ashistant.assistant;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.freshliver.ashistant.AssistantActivity;
import com.freshliver.ashistant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    protected CropImageView cropImageView;
    protected FloatingActionButton saveCroppedArea, uploadCroppedArea;
    protected FloatingActionButton shareCroppedArea, editScreenshot, discardScreenshot;

    protected ResetCropImageViewListener resetCropImageViewListener;
    protected SetFragmentListener switchFragmentListener;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View layout,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(layout, savedInstanceState);

        /* init listeners */
        this.resetCropImageViewListener = (AssistantActivity) this.requireActivity();
        this.switchFragmentListener = (AssistantActivity) this.requireActivity();

        /* get crop image from activity */
        this.cropImageView = this.requireActivity().findViewById(R.id.cropImageView_Assistant);

        /* find fabs from layout */
        this.discardScreenshot = layout.findViewById(R.id.fabDiscardScreenshot);
        this.editScreenshot = layout.findViewById(R.id.fabEditScreenshot);
        this.saveCroppedArea = layout.findViewById(R.id.fabSaveCroppedArea);
        this.uploadCroppedArea = layout.findViewById(R.id.fabUploadCroppedArea);
        this.shareCroppedArea = layout.findViewById(R.id.fabShareCroppedArea);

        /* set fab onclick functions */
        this.discardScreenshot.setOnClickListener((view) -> this.requireActivity().finish());
        this.editScreenshot.setOnClickListener((view) -> this.switchFragmentListener.setFragment(AssistantFragments.Editor));
        this.saveCroppedArea.setOnClickListener((view) -> HomeFragment.saveScreenshot(view, this.cropImageView));
        this.uploadCroppedArea.setOnClickListener((view) -> {
        });
        this.shareCroppedArea.setOnClickListener((view) -> {
        });

        /* init crop image view */
        this.resetCropImageViewListener.resetCropImageView(false);
    }


    protected static void saveScreenshot(View view, CropImageView civ) {
        /* specify target filename using current datetime */
        @SuppressLint("SimpleDateFormat")
        File dstFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                String.format(
                        "screenshot-%s.png",
                        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())
                )
        );

        /* try to save crop area to dst file */
        try {
            /* specify crop tmp file and create if file not exists */
            if (!dstFile.exists() && !dstFile.createNewFile())
                throw new RuntimeException(String.format("Create File(%s) Failed.", dstFile.getAbsoluteFile()));

            /* output cropped area to target file */
            civ.getCroppedImage()
                    .compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(dstFile, false));

            /* finish assistant after screenshot saved */
            String successMsg = String.format("Screenshot saved to %s.", dstFile.toString());
            Toast.makeText(view.getContext(), successMsg, Toast.LENGTH_SHORT).show();

        } catch (IOException | RuntimeException e) {
            /* log and show error msg */
            e.printStackTrace();
            Toast.makeText(view.getContext(), "Failed to save screenshot.", Toast.LENGTH_SHORT).show();
        }

    }
}