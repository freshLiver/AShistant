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

import org.jetbrains.annotations.NotNull;

public class EditorFragment extends Fragment {

    protected ResetCropImageViewListener resetCropImageViewListener;
    protected SetFragmentListener switchFragmentListener;


    public static EditorFragment newInstance() {
        return new EditorFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editor, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view,
                              @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* init listeners */
        this.resetCropImageViewListener = (AssistantActivity) this.requireActivity();
        this.switchFragmentListener = (AssistantActivity) this.requireActivity();

        /* init crop image view */
        this.resetCropImageViewListener.resetCropImageView(false);
    }
}