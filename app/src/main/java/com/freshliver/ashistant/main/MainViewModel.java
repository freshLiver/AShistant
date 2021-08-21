package com.freshliver.ashistant.main;

import android.app.Application;
import android.os.Looper;
import android.util.AndroidRuntimeException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

public final class MainViewModel extends AndroidViewModel {
    
    private MutableLiveData<Integer> count;
    
    
    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
    }
    
    
    public MutableLiveData<Integer> getCount() {
        if (this.count == null)
            this.count = new MutableLiveData<>(0);
        return this.count;
    }
    
    
    public void incCount(boolean positive) {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new AndroidRuntimeException("This Method Should be Called on Main(UI) Thread.");
        this.getCount().setValue(this.getCount().getValue() + (positive ? 1 : -1));
    }
}
