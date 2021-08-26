package com.freshliver.ashistant;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.freshliver.ashistant.assistant.AssistantService;
import com.freshliver.ashistant.main.MainViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected MainViewModel viewModel;

    protected TextView showCountView;
    protected MaterialButton incCount, decCount, loadingBtn, setAssistant;
    protected ActivityResultLauncher<Intent> setAssistantLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* get ui instances */
        this.showCountView = this.findViewById(R.id.showCountView);
        this.incCount = this.findViewById(R.id.incCount);
        this.decCount = this.findViewById(R.id.decCount);
        this.loadingBtn = this.findViewById(R.id.loadingBtn);
        this.setAssistant = this.findViewById(R.id.setAssistantBtn);


        /* instantiate view model */
        this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /* set observer for view instances */
        this.viewModel.getCount().observe(this, iValue -> this.showCountView.setText(String.valueOf(iValue)));

        this.incCount.setOnClickListener(view -> this.viewModel.incCount(true));
        this.decCount.setOnClickListener(view -> this.viewModel.incCount(false));
        this.setAssistant.setOnClickListener(view -> this.setDefaultAssistant());
        this.loadingBtn.setOnClickListener((view) -> new LoadingDialog.Builder<String>()
                .setAsyncTask(() -> {
                    final int ms = 1000;
                    try {
                        Thread.sleep(ms);
                        return String.format(Locale.getDefault(), "%d ms!!", ms);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        return "Interrupted";
                    }
                })
                .setUICallback(result -> Toast.makeText(this, result, Toast.LENGTH_SHORT).show())
                .build().start(this.getSupportFragmentManager())
        );

        // register must set before STARTED state
        this.setAssistantLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> { }
        );
    }


    private void setDefaultAssistant() {

        // get assistant setting
        final String assistantSetting = Settings.Secure.getString(this.getContentResolver(), "assistant");

        if (assistantSetting == null)
            Toast.makeText(this, "Settings.Secure.getString cannot find assistant", Toast.LENGTH_SHORT).show();

        else {
            // get class name of current digital assistant app and my assistant app
            final String myAssistant = AssistantService.class.getName();
            final String nowAssistant = ComponentName.unflattenFromString(assistantSetting).getClassName();

            // if default assistant is not this app, launch assistant setting page
            if (!myAssistant.equals(nowAssistant))
                this.setAssistantLauncher.launch(new Intent().setAction(Settings.ACTION_VOICE_INPUT_SETTINGS));
            else
                Toast.makeText(this, "Already set as default digital assistant app", Toast.LENGTH_SHORT).show();
        }
    }
}