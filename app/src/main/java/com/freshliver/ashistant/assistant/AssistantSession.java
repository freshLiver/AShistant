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
import com.freshliver.ashistant.utils.DatetimeUtils;
import com.freshliver.ashistant.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;


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

            /* convert bitmap to byte array and save a temp file */
            String screenshotPath = null;

            try {
                // save screenshot tmp file to temp file dir
                File tmpDir = new File(this.getContext().getFilesDir(), "temps");

                String filename = String.format(
                        "screenshot-%s.png",
                        DatetimeUtils.getFormattedDatetime(Calendar.getInstance().getTime(), "mm-ss")
                );

                File tmpScreenshot = FileUtils.saveBitmapToFile(
                        screenshot, tmpDir, filename,
                        FileUtils.DEFAULT_COMPRESS_FORMAT, FileUtils.DEFAULT_COMPRESS_QUALITY
                );

                // get temp file uri
                Uri screenshotUri = FileProvider.getUriForFile(this.getContext(), FileUtils.FILE_PROVIDER_AUTHORITY, tmpScreenshot);
                screenshotPath = FileUtils.getInternalFile(this.getContext(),screenshotUri).toString();

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            /* create new intent add put image data into it */
            Intent intent = new Intent(this.getContext(), AssistantActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(AssistantActivity.SCREENSHOT_URI_STRING, screenshotPath);

            /* launch activity */
            this.getContext().startActivity(intent);

        } else {
            Toast.makeText(this.getContext(), "screenshot null", Toast.LENGTH_SHORT).show();
        }
    }
}