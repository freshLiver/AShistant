package com.freshliver.ashistant.assistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.service.voice.VoiceInteractionSession;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.freshliver.ashistant.AssistantActivity;


public class AssistantSession extends VoiceInteractionSession {

    public AssistantSession(Context context) {
        super(context);
    }


    @Override
    public void onHandleScreenshot(@Nullable Bitmap screenshot) {
        super.onHandleScreenshot(screenshot);

        this.hide();

        /* start a dialog while screenshot not null */
        if (screenshot != null) {
            // save screenshot to static field of assistant class
            // instead of save as file to speed up
            AssistantActivity.SCREENSHOT_TEMP = screenshot;

            // create intent and start assistant activity
            Intent intent = new Intent(this.getContext(), AssistantActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.getContext().startActivity(intent);
        }
        else {
            Toast.makeText(this.getContext(), "screenshot null", Toast.LENGTH_SHORT).show();
        }
    }
}