package com.freshliver.ashistant.preference;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import com.freshliver.ashistant.R;

public final class PreferenceFragment extends androidx.preference.PreferenceFragmentCompat {


    private ActivityResultLauncher<Intent> assistantSettingLauncher;

    // settings
    private Preference setAssistant;
    private EditTextPreference editClientId, editAccessToken;

    // view model
    private PreferenceViewModel viewModel;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.preference_layout, rootKey);

        // init view model
        this.viewModel = new ViewModelProvider(this).get(PreferenceViewModel.class);

        // register must set before STARTED state
        this.assistantSettingLauncher = this.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> { }
        );

        this.initComponents();
        this.setComponentEvents();
    }


    private void initComponents() {
        // get preference items and set default value (if needed)
        this.setAssistant = this.findPreference(getString(R.string.Preference_MainSettings_SetAssistant));

        this.editClientId = this.findPreference(getString(R.string.Preference_ImgurSettings_EditClientId));
        this.editAccessToken = this.findPreference(getString(R.string.Preference_ImgurSettings_EditAccessToken));
    }


    private void setComponentEvents() {

        // this is app.
        this.setAssistant.setOnPreferenceClickListener(preference ->
                viewModel.openAssistantSetting(this.requireActivity(), this.assistantSettingLauncher)
        );

        // update setting on preference value changed
        this.editClientId.setOnPreferenceClickListener(preference -> this.viewModel.readSetting(preference, this));
        this.editClientId.setOnPreferenceChangeListener((preference, newValue) ->
                this.viewModel.updateSetting(preference, newValue, this)
        );

        this.editAccessToken.setOnPreferenceClickListener(preference -> this.viewModel.readSetting(preference, this));
        this.editAccessToken.setOnPreferenceChangeListener((preference, newValue) ->
                this.viewModel.updateSetting(preference, newValue, this)
        );
    }
}
