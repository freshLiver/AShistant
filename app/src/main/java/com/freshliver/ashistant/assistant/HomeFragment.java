package com.freshliver.ashistant.assistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freshliver.ashistant.AssistantActivity;
import com.freshliver.ashistant.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment {

    protected FloatingActionButton resetScreenshot, saveCroppedArea, uploadCroppedArea;
    protected FloatingActionButton shareCroppedArea, editScreenshot, exitScreenshot;

    protected CropImageViewInterface cropImageViewInterface;
    protected AssistantFragmentSetter switchFragmentListener;


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
        this.cropImageViewInterface = (AssistantActivity) this.requireActivity();
        this.switchFragmentListener = (AssistantActivity) this.requireActivity();

        /* find fabs from layout */
        this.exitScreenshot = layout.findViewById(R.id.fabExitScreenshot);
        this.resetScreenshot = layout.findViewById(R.id.fabResetScreenshot);
        this.editScreenshot = layout.findViewById(R.id.fabEditScreenshot);
        this.saveCroppedArea = layout.findViewById(R.id.fabSaveCroppedArea);
        this.uploadCroppedArea = layout.findViewById(R.id.fabUploadCroppedArea);
        this.shareCroppedArea = layout.findViewById(R.id.fabShareCroppedArea);

        /* set fab onclick functions */
        this.exitScreenshot.setOnClickListener((view) -> this.requireActivity().finish());
        this.editScreenshot.setOnClickListener((view) -> this.switchFragmentListener.setFragment(AssistantFragments.Editor));
        this.resetScreenshot.setOnClickListener((view) -> this.cropImageViewInterface.resetCropImageView());
        this.saveCroppedArea.setOnClickListener((view) -> this.cropImageViewInterface.saveCroppedArea());

        /* TODO */
        this.uploadCroppedArea.setOnClickListener((view) -> {
        });
        this.shareCroppedArea.setOnClickListener((view) -> {
        });
    }


    protected static void saveScreenshot(View view, CropImageView civ) {


    }
}