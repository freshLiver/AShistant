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

import org.jetbrains.annotations.NotNull;

public class EditorFragment extends Fragment {

    protected CropImageViewInterface cropImageViewInterface;
    protected AssistantFragmentSetter switchFragmentListener;

    protected FloatingActionButton back, crop;
    protected FloatingActionButton flipHorizontally, flipVertically, rotateRight, rotateLeft;


    public static EditorFragment newInstance() {
        return new EditorFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View layout,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(layout, savedInstanceState);

        /* init listeners */
        this.cropImageViewInterface = (AssistantActivity) this.requireActivity();
        this.switchFragmentListener = (AssistantActivity) this.requireActivity();

        /* find fabs from layouy */
        this.back = layout.findViewById(R.id.fabEditorBack);
        this.crop = layout.findViewById(R.id.fabCropScreenshot);
        this.flipHorizontally = layout.findViewById(R.id.fabFlipScreenshotHorizontally);
        this.flipVertically = layout.findViewById(R.id.fabFlipScreenshotVertically);
        this.rotateLeft = layout.findViewById(R.id.fabRotateScreenshotLeft);
        this.rotateRight = layout.findViewById(R.id.fabRotateScreenshotRight);

        /* set fab events */
        this.back.setOnClickListener((view) -> this.switchFragmentListener.setFragment(AssistantFragments.Home));
        this.crop.setOnClickListener((view) -> this.cropImageViewInterface.cropImage());
        this.flipHorizontally.setOnClickListener((view) -> this.cropImageViewInterface.flipHorizontally());
        this.flipVertically.setOnClickListener((view) -> this.cropImageViewInterface.flipVertically());
        this.rotateLeft.setOnClickListener((view) -> this.cropImageViewInterface.rotateLeft90());
        this.rotateRight.setOnClickListener((view) -> this.cropImageViewInterface.rotateRight90());

    }
}