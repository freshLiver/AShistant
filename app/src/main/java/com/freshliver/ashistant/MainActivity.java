package com.freshliver.ashistant;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.freshliver.ashistant.assistant.AssistantService;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {

    protected MaterialButton setAssistant;

    protected ActivityResultLauncher<Intent> setAssistantLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* get ui instances */
        this.setAssistant = this.findViewById(R.id.setAssistantBtn);


        /* instantiate view model */

        /* set observer for view instances */
        this.setAssistant.setOnClickListener(view -> this.setDefaultAssistant());

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