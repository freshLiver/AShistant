package com.freshliver.ashistant.assistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.service.voice.VoiceInteractionSession;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.freshliver.ashistant.AssistantActivity;
import com.freshliver.ashistant.utils.BitmapUtils;
import com.freshliver.ashistant.utils.DatetimeUtils;
import com.freshliver.ashistant.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;


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
            try {
                // save screenshot tmp file to temp file dir
                String screenshotPath = BitmapUtils.saveAsPNG(
                        screenshot,
                        FileUtils.getInternalTempFile(
                                this.getContext(),
                                String.format("screenshot-%s.png", DatetimeUtils.formatCurrentTime("mm-ss"))
                        )
                ).getAbsolutePath();

                // create share intent add put screenshot data
                Intent intent = new Intent(this.getContext(), AssistantActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(AssistantActivity.SCREENSHOT_URI_STRING, screenshotPath);

                // launch share intent
                this.getContext().startActivity(intent);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this.getContext(), "screenshot null", Toast.LENGTH_SHORT).show();
        }
    }
}