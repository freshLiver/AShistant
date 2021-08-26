package com.freshliver.ashistant.preference;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.Preference;

import com.freshliver.ashistant.R;
import com.freshliver.ashistant.assistant.AssistantService;

public final class PreferenceFragment extends androidx.preference.PreferenceFragmentCompat {


    protected ActivityResultLauncher<Intent> setAssistantLauncher;

    // main settings
    private Preference setAssistant;

    // imgur settings


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.preference_layout, rootKey);

        // register must set before STARTED state
        this.setAssistantLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> { }
        );

        this.getComponents();
        this.setComponentEvents();
    }


    private void getComponents() {
        // get preference items
        this.setAssistant = this.findPreference(getString(R.string.Preference_MainSettings_SetAssistant));
    }


    private void setComponentEvents() {
        this.setAssistant.setOnPreferenceClickListener(preference -> {
            this.setDefaultAssistant();
            return true;
        });
    }


    private void setDefaultAssistant() {

        // get assistant setting
        final String assistantSetting = Settings.Secure.getString(this.requireActivity().getContentResolver(), "assistant");

        if (assistantSetting == null)
            Toast.makeText(this.getActivity(), "Settings.Secure.getString cannot find assistant", Toast.LENGTH_SHORT).show();

        else {
            // get class name of current digital assistant app and my assistant app
            final String myAssistant = AssistantService.class.getName();
            final String nowAssistant = ComponentName.unflattenFromString(assistantSetting).getClassName();

            // if default assistant is not this app, launch assistant setting page
            if (!myAssistant.equals(nowAssistant))
                this.setAssistantLauncher.launch(new Intent().setAction(Settings.ACTION_VOICE_INPUT_SETTINGS));
            else
                Toast.makeText(this.getActivity(), "Already set as default digital assistant app", Toast.LENGTH_SHORT).show();
        }
    }
}
