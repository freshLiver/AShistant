package com.freshliver.ashistant.preference;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.Preference;

import com.freshliver.ashistant.LoadingDialog;
import com.freshliver.ashistant.assistant.AssistantService;
import com.freshliver.ashistant.models.db.DBManager;
import com.freshliver.ashistant.models.db.setting.AppSetting;
import com.freshliver.ashistant.models.db.setting.AppSettingDB;

import org.jetbrains.annotations.NotNull;

public class PreferenceViewModel extends AndroidViewModel {

    private static AppSettingDB appSettingDB;


    public PreferenceViewModel(@NonNull @NotNull Application application) {
        super(application);

        // init db
        if (PreferenceViewModel.appSettingDB == null)
            PreferenceViewModel.appSettingDB = DBManager.getDatabase(application, AppSettingDB.class);
    }


    public boolean openAssistantSetting(final FragmentActivity fragmentActivity,
                                        final ActivityResultLauncher<Intent> assistantSettingLauncher) {

        // get assistant setting
        final String assistantSetting = Settings.Secure.getString(fragmentActivity.getContentResolver(), "assistant");

        if (assistantSetting == null)
            Toast.makeText(fragmentActivity, "Settings.Secure.getString cannot find assistant", Toast.LENGTH_SHORT).show();

        else {
            // get class name of current digital assistant app and my assistant app
            final String myAssistant = AssistantService.class.getName();
            final String nowAssistant = ComponentName.unflattenFromString(assistantSetting).getClassName();

            // if default assistant is not this app, launch assistant setting page
            if (!myAssistant.equals(nowAssistant))
                assistantSettingLauncher.launch(new Intent().setAction(Settings.ACTION_VOICE_INPUT_SETTINGS));
            else
                Toast.makeText(fragmentActivity, "Already set as default digital assistant app", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    public boolean readSetting(final Preference preference, final Fragment fragment) {
        new LoadingDialog.Builder<String>()
                .setDialogLoadingText("Loading Setting...")
                .setAsyncTask(() -> {
                    // get current setting value and set as default value
                    preference.setDefaultValue(PreferenceViewModel.appSettingDB.getAppSettingByKey(preference.getKey()));
                    return null;
                })
                .build()
                .start(fragment.getParentFragmentManager());
        return true;
    }


    public boolean updateSetting(final Preference preference, final Object newValue, final Fragment fragment) {
        new LoadingDialog.Builder<String>()
                .setDialogLoadingText("Saving Change...")
                .setAsyncTask(() -> {
                    try {
                        PreferenceViewModel.appSettingDB.setAppSetting(new AppSetting(preference.getKey(), newValue.toString()));
                        return "Setting Updated!";
                    }
                    catch (Exception e) {
                        return e.toString();
                    }
                })
                .setUICallback(text -> Toast.makeText(fragment.getContext(), text, Toast.LENGTH_SHORT).show())
                .build()
                .start(fragment.getParentFragmentManager());
        return true;
    }
}
