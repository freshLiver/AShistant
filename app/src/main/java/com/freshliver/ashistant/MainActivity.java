package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.freshliver.ashistant.main.MainViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

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
        this.loadingBtn.setOnClickListener((view) -> new LoadingDialog.Builder<String>()
                .setAsyncTask(() -> this.delay(1000))
                .setUICallback(result -> Toast.makeText(this, result, Toast.LENGTH_SHORT).show())
                .build().start(this.getSupportFragmentManager())
        );
    }


    public String delay(int ms) {
        try {
            Thread.sleep(ms);
            return String.format(Locale.getDefault(), "%d ms!!", ms);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return "Interrupted";
        }
    }
}