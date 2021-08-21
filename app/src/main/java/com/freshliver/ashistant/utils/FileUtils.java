package com.freshliver.ashistant.utils;

import android.content.ContentProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public final class FileUtils {

    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;

    public static final String FILE_PROVIDER_AUTHORITY = "com.freshliver.ashistant.provider";


    private FileUtils() {
        // private constructor for static class
    }


    public static File getInternalFile(Context context, Uri internalFileUri) {
        return new File(context.getFilesDir(), internalFileUri.getPath());
    }


    public static File saveBitmapToFile(Bitmap bitmap, File dir, String filename, Bitmap.CompressFormat format, int quality)
            throws FileNotFoundException {

        /* create path if not exists */
        assert dir.exists() || dir.mkdirs();

        /* try output (replace) bitmap to target dst */
        File targetFile = new File(dir, filename);
        bitmap.compress(format, quality, new FileOutputStream(targetFile, false));

        /* return dst file or null(if exception catch) */
        return targetFile;
    }


    public static Bitmap loadBitmapFromFile(File srcFile) {
        return BitmapFactory.decodeFile(srcFile.getPath());
    }

}
