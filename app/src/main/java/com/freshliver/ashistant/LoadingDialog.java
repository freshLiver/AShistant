package com.freshliver.ashistant;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoadingDialog<Result> {

    public static class Dialog extends DialogFragment {

        private ProgressBar progressBar;

        @Override
        public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                 @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                 @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_loading, container, false);
        }


        @Override
        public void show(@NonNull @NotNull FragmentManager manager,
                         @Nullable @org.jetbrains.annotations.Nullable String tag) {
            super.show(manager, tag);
            this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Style_ProgressBar_Translucent);
        }
    }

    public interface Callback<Result> {
        void onCompleted(Result result);
    }

    private final Dialog dialog;
    private final Executor executor;
    private final Handler uiThreadHandler;


    public LoadingDialog() {
        this.dialog = new Dialog();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
    }


    public void startTask(FragmentManager manager, Callable<Result> task, @Nullable Callback<Result> callback) {

        this.executor.execute(() -> {
            try {
                // do background task here
                Result result = task.call();

                // handling callback task if exists
                if (callback != null)
                    callback.onCompleted(result);

                // after all task done, dispose dialog
                this.uiThreadHandler.post(this.dialog::dismiss);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        // show dialog soon after task executed
        this.dialog.show(manager, "Task");
    }

}
