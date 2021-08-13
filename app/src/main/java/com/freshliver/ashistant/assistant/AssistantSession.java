package com.freshliver.ashistant.assistant;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.freshliver.ashistant.AssistantActivity;

import java.io.ByteArrayOutputStream;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AssistantSession extends VoiceInteractionSession {

    public AssistantSession(Context context) {
        super(context);
    }


    @Override
    public void onCreate() {
        super.onCreate();
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
            Intent intent = new Intent(this.getContext(), AssistantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AssistantActivity.ScreenshotDataKey, out.toByteArray());

            /* launch activity */
            this.getContext().startActivity(intent);

        } else {
            Toast.makeText(this.getContext(), "screenshot null", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
    }

}