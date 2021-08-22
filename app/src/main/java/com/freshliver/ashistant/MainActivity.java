package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.freshliver.ashistant.main.MainViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    protected MainViewModel viewModel;

    protected TextView showCountView;
    protected MaterialButton incCount, decCount, loadingBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* get ui instances */
        this.showCountView = this.findViewById(R.id.showCountView);
        this.incCount = this.findViewById(R.id.incCount);
        this.decCount = this.findViewById(R.id.decCount);
        this.loadingBtn = this.findViewById(R.id.loadingBtn);


        /* instantiate view model */
        this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /* set observer for view instances */
        this.viewModel.getCount().observe(this, iValue -> this.showCountView.setText(String.valueOf(iValue)));
        this.incCount.setOnClickListener(view -> this.viewModel.incCount(true));
        this.decCount.setOnClickListener(view -> this.viewModel.incCount(false));
        this.loadingBtn.setOnClickListener((view) -> new LoadingDialog<String>().startTask(
                this.getSupportFragmentManager(),
                () -> this.delay(1000),
                this::done
        ));
    }


    public String delay(int ms) throws InterruptedException {
        Thread.sleep(ms);
        return String.format(Locale.getDefault(), "%d ms!!", ms);
    }


    public void done(String result) {
        runOnUiThread(() -> Toast.makeText(this, result, Toast.LENGTH_SHORT).show());
    }
}