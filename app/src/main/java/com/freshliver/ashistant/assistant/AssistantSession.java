package com.freshliver.ashistant.assistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.service.voice.VoiceInteractionSession;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.freshliver.ashistant.AssistantActivity;

import java.io.ByteArrayOutputStream;


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

            /* convert bitmap to byte array */
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, out);

            /* create new intent add put image data into it */
            Intent intent = new Intent(this.getContext(), AssistantActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(AssistantActivity.ScreenshotDataKey, out.toByteArray());

            /* launch activity */
            this.getContext().startActivity(intent);

        } else {
            Toast.makeText(this.getContext(), "screenshot null", Toast.LENGTH_SHORT).show();
        }
    }
}