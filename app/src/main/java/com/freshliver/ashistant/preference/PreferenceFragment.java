package com.freshliver.ashistant.preference;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import com.freshliver.ashistant.LoadingDialog;
import com.freshliver.ashistant.R;
import com.freshliver.ashistant.assistant.AssistantService;
import com.freshliver.ashistant.models.db.DBManager;
import com.freshliver.ashistant.models.db.setting.AppSetting;
import com.freshliver.ashistant.models.db.setting.AppSettingDB;

public final class PreferenceFragment extends androidx.preference.PreferenceFragmentCompat {


    protected ActivityResultLauncher<Intent> setAssistantLauncher;

    // main settings
    private Preference setAssistant;

    // imgur settings
    private EditTextPreference editClientId, editAccessToken;

    // singleton app setting db and its getter
    private static AppSettingDB appSettingDB;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.preference_layout, rootKey);

        // register must set before STARTED state
        this.setAssistantLauncher = this.registerForActivityResult(
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
        this.setAssistant.setOnPreferenceClickListener(preference -> {
            this.setDefaultAssistant();
            return true;
        });

        // update setting on preference value changed
        this.editClientId.setOnPreferenceClickListener(this::fetchSetting);
        this.editClientId.setOnPreferenceChangeListener(this::updateSetting);

        this.editAccessToken.setOnPreferenceClickListener(this::fetchSetting);
        this.editAccessToken.setOnPreferenceChangeListener(this::updateSetting);
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


    private boolean fetchSetting(Preference preference) {
        new LoadingDialog.Builder<String>()
                .setDialogLoadingText("Loading Setting...")
                .setAsyncTask(() -> {
                    // init db if not init yet
                    if (PreferenceFragment.appSettingDB == null)
                        PreferenceFragment.appSettingDB = DBManager.getDatabase(this.getContext(), AppSettingDB.class);

                    // get current setting value and set as default value
                    preference.setDefaultValue(PreferenceFragment.appSettingDB.getAppSettingByKey(preference.getKey()));
                    return null;
                })
                .build()
                .start(this.getParentFragmentManager());
        return true;
    }


    private boolean updateSetting(Preference preference, Object newValue) {
        new LoadingDialog.Builder<String>()
                .setDialogLoadingText("Saving Change...")
                .setAsyncTask(() -> {
                    try {
                        PreferenceFragment.appSettingDB.setAppSetting(new AppSetting(preference.getKey(), newValue.toString()));
                        return "Setting Updated!";
                    }
                    catch (Exception e) {
                        return e.toString();
                    }
                })
                .setUICallback(text -> Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show())
                .build()
                .start(this.getParentFragmentManager());
        return true;
    }
}
