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

    private final Callable<ResultType> task;
    private final Callback<ResultType> callback;


    private LoadingDialog(Builder<ResultType> builder) {
        this.dialog = new Dialog();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiThreadHandler = new Handler(Looper.getMainLooper());

        this.task = builder.task;
        this.callback = builder.callback;
    }


    public void start(FragmentManager manager) {
        if (this.task != null) {
            this.executor.execute(() -> {
                try {
                    // do background task here
                    ResultType result = this.task.call();

                    // handling callback task if exists
                    if (this.callback != null)
                        this.callback.onCompleted(result);

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


    public static class Builder<ResultType> {

        private Callable<ResultType> task = null;
        private Callback<ResultType> callback = null;


        public LoadingDialog<ResultType> build() {
            return new LoadingDialog<>(this);
        }


        public Builder<ResultType> setTask(Callable<ResultType> task) {
            this.task = Objects.requireNonNull(task);
            return this;
        }


        public Builder<ResultType> setCallback(Callback<ResultType> callback) {
            this.callback = Objects.requireNonNull(callback);
            return this;
        }

    }
}