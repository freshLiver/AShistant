package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.freshliver.ashistant.main.MainViewModel;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    protected MainViewModel viewModel;

    protected TextView showCountView;
    protected MaterialButton incCount, decCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* get ui instances */
        this.showCountView = this.findViewById(R.id.showCountView);
        this.incCount = this.findViewById(R.id.incCount);
        this.decCount = this.findViewById(R.id.decCount);


        /* instantiate view model */
        this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /* set observer for view instances */
        this.viewModel.getCount().observe(this, iValue -> this.showCountView.setText(String.valueOf(iValue)));
        this.incCount.setOnClickListener(view -> this.viewModel.incCount(true));
        this.decCount.setOnClickListener(view -> this.viewModel.incCount(false));
    }
}