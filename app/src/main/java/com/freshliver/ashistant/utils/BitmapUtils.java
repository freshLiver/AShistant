package com.freshliver.ashistant.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;

public class BitmapUtils {


    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;


    private BitmapUtils() {
    }

    //
    // save as file / load from file
    //


    public static File saveAsPNG(Bitmap bitmap, File file) throws FileNotFoundException {
        return saveAsFile(bitmap, file, DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY);
    }


    public static File saveAsFile(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality) throws FileNotFoundException {

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


    public static Bitmap loadFromFile(File srcFile) {
        return BitmapFactory.decodeFile(srcFile.getPath());
    }

}
