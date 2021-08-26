package com.freshliver.ashistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.freshliver.ashistant.preference.PreferenceFragment;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preference);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.preferenceContainer, new PreferenceFragment())
                .commit();
    }
}