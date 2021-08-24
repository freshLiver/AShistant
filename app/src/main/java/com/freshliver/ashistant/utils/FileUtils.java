package com.freshliver.ashistant.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

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
    // Mimetypes
    //


    /**
     * @param file the file whose MimeType you want to know
     * @return MimeType String of this src file
     * @throws IOException I/O error while probing content type of src file
     */
    public static String getMimeTypeFromFile(File file) throws IOException {
        return Files.probeContentType(file.getAbsoluteFile().toPath());
    }


    public static String getExtensionFromMimetype(String mimetype) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimetype);
    }


    public static String getExtensionFromFile(File file) throws IOException {
        return getExtensionFromMimetype(getMimeTypeFromFile(file));
    }

    //
    // get external file
    //


    public static File getExternalDownloadFile(String filename) {
        return new File(EXTERNAL_DOWNLOAD_DIR, filename);
    }

    //
    // get an internal file or its uri
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


    public static File getInternalCacheFile(Context context, String filename) {
        return new File(context.getCacheDir(), filename);
    }
}
