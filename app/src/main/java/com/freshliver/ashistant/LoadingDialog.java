package com.freshliver.ashistant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LoadingDialog<ResultType> {

    protected static final String DEFAULT_LOADING_TEXT = "Loading...";


    public static class Dialog extends DialogFragment {

        protected String loadingText = DEFAULT_LOADING_TEXT;


        @Override
        public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                 @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                 @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_loading, container, false);

            // set textview
            ((TextView) view.findViewById(R.id.Dialog_loadingText)).setText(this.loadingText);

            return view;
        }


        @Override
        public void onResume() {
            super.onResume();

            // let window size match parent to show all components.
            Window window = this.getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    private final long msLaunchDelay;
    private final long msFinishDelay;
    private final String dialogLoadingText;
    private final Callable<ResultType> asyncTask;
    private final Callback<ResultType> asyncCallback;
    private final Callback<ResultType> uiCallback;
    private ResultType result;


    private LoadingDialog(Builder<ResultType> builder) {
        this.dialog = new Dialog();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiThreadHandler = new Handler(Looper.getMainLooper());

        this.msLaunchDelay = builder.msLaunchDelay;
        this.msFinishDelay = builder.msFinishDelay;
        this.dialogLoadingText = builder.loadingText;
        this.asyncTask = builder.asyncTask;
        this.asyncCallback = builder.asyncCallback;
        this.uiCallback = builder.uiCallback;
    }


    public void start(FragmentManager manager) {
        if (this.asyncTask != null) {

            // do async tasks
            this.executor.execute(() -> {
                try {
                    // launch delay
                    if (this.msLaunchDelay > 0)
                        Thread.sleep(this.msLaunchDelay);

                    // do background task here
                    this.result = this.asyncTask.call();

                    // do async callback task if exists
                    if (this.asyncCallback != null)
                        this.asyncCallback.onCompleted(this.result);

                    // do ui callback task if exists
                    if (this.uiCallback != null)
                        this.uiThreadHandler.post(() -> this.uiCallback.onCompleted(this.result));

                    // finish delay
                    if (this.msFinishDelay > 0)
                        Thread.sleep(this.msFinishDelay);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // after all task done or any error, dismiss dialog
                finally {
                    this.uiThreadHandler.post(this.dialog::dismiss);
                }
            });

            // set dialog loading text
            this.dialog.loadingText = this.dialogLoadingText;

            // show dialog soon after all tasks executed
            this.dialog.show(manager, "Task");
        }
    }


    public static class Builder<ResultType> {

        private long msLaunchDelay = 0;
        private long msFinishDelay = 0;
        private String loadingText = DEFAULT_LOADING_TEXT;
        private Callable<ResultType> asyncTask = null;
        private Callback<ResultType> asyncCallback = null;
        private Callback<ResultType> uiCallback = null;


        public LoadingDialog<ResultType> build() {
            return new LoadingDialog<>(this);
        }


        public Builder<ResultType> setDelay(long msLaunchDelay, long msFinishDelay) {
            this.msLaunchDelay = msLaunchDelay;
            this.msFinishDelay = msFinishDelay;
            return this;
        }


        public Builder<ResultType> setDialogLoadingText(String text) {
            this.loadingText = text;
            return this;
        }


        public Builder<ResultType> setAsyncTask(Callable<ResultType> asyncTask) {
            this.asyncTask = Objects.requireNonNull(asyncTask);
            return this;
        }


        public Builder<ResultType> setAsyncCallback(Callback<ResultType> asyncCallback) {
            this.asyncCallback = Objects.requireNonNull(asyncCallback);
            return this;
        }


        public Builder<ResultType> setUICallback(Callback<ResultType> uiCallback) {
            this.uiCallback = Objects.requireNonNull(uiCallback);
            return this;
        }

    }
}