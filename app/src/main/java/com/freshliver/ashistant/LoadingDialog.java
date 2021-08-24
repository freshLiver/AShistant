package com.freshliver.ashistant;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public static class Dialog extends DialogFragment {

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

    private final Callable<ResultType> asyncTask;
    private final Callback<ResultType> asyncCallback;
    private final Callback<ResultType> uiCallback;
    private ResultType result;


    private LoadingDialog(Builder<ResultType> builder) {
        this.dialog = new Dialog();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiThreadHandler = new Handler(Looper.getMainLooper());

        this.asyncTask = builder.asyncTask;
        this.asyncCallback = builder.asyncCallback;
        this.uiCallback = builder.uiCallback;
    }


    public void start(FragmentManager manager) {
        if (this.asyncTask != null) {

            // do async tasks
            this.executor.execute(() -> {
                try {
                    // do background task here
                    this.result = this.asyncTask.call();

                    // do async callback task if exists
                    if (this.asyncCallback != null)
                        this.asyncCallback.onCompleted(this.result);

                    // do ui callback task if exists
                    if (this.uiCallback != null)
                        this.uiThreadHandler.post(() -> this.uiCallback.onCompleted(this.result));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // after all task done or any error, dismiss dialog
                finally {
                    this.uiThreadHandler.post(this.dialog::dismiss);
                }
            });

            // show dialog soon after task executed
            this.dialog.show(manager, "Task");
        }
    }


    public static class Builder<ResultType> {

        private Callable<ResultType> asyncTask = null;
        private Callback<ResultType> asyncCallback = null;
        private Callback<ResultType> uiCallback = null;


        public LoadingDialog<ResultType> build() {
            return new LoadingDialog<>(this);
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