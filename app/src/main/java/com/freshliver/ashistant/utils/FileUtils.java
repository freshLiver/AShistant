package com.freshliver.ashistant.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public final class FileUtils {

    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;

    public static final String FILE_PROVIDER_AUTHORITY = "com.freshliver.ashistant.provider";

    public static final String INTERNAL_TEMP_DIRNAME = "temps";
    public static final File EXTERNAL_DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


    private FileUtils() {
        // private constructor for static class
    }


    //
    // get external file
    //


    public static File getExternalDownloadFile(String filename) {
        return new File(EXTERNAL_DOWNLOAD_DIR, filename);
    }

    //
    // get internal file or uri
    //


    public static Uri getInternalFileUri(Context context, File internalFile) {
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, internalFile);
    }


    public static File getInternalFile(Context context, Uri internalFileUri) {
        return new File(context.getFilesDir(), internalFileUri.getPath());
    }


    public static File getInternalTempFile(Context context, String filename) {
        return getInternalFile(context, Uri.parse(String.format("%s/%s", INTERNAL_TEMP_DIRNAME, filename)));
    }


    //
    // save bitmap to file
    //


    public static File saveBitmapAsPNG(Bitmap bitmap, File file) throws FileNotFoundException {
        return saveBitmapAsFile(bitmap, file, DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY);
    }


    public static File saveBitmapAsFile(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality) throws FileNotFoundException {

        // file or directory
        File dir = file.isDirectory() ? file : file.getParentFile();

        // target dir should not be null
        if (dir == null)
            throw new FileNotFoundException("Src File Pathname does not name a parent.");

        // create path if not exists
        assert dir.exists() || dir.mkdirs();

        /* try output (replace) bitmap to target dst */
        bitmap.compress(format, quality, new FileOutputStream(file, false));

        /* return dst file if no exception caught */
        return file;
    }


    //
    // load bitmap from file
    //


    public static Bitmap loadBitmapFromFile(File srcFile) {
        return BitmapFactory.decodeFile(srcFile.getPath());
    }

}
