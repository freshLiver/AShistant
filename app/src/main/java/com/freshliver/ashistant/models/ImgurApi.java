package com.freshliver.ashistant.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.freshliver.ashistant.utils.FileUtils;
import com.freshliver.ashistant.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImgurApi {

    private static final String PUBLIC_UPLOAD_URL = "https://api.imgur.com/3/upload";


    /**
     *
     * @param image the image you want to upload
     * @return image link or error msg
     * @throws IOException I/O error while ReadAllBytes or ProbeContentType from src image
     */
    @SuppressLint("DefaultLocale")
    public static String uploadPublicImage(File image) throws IOException {

        // get image mimetype and read as byte array
        byte[] data = Files.readAllBytes(image.toPath());
        MediaType type = MediaType.parse(FileUtils.getMimeTypeFromFile(image));

        // send post request and check response status code
        Response res = NetworkUtils.simplePost(
                PUBLIC_UPLOAD_URL,
                null,
                new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", image.getName(), RequestBody.create(data, type))
                        .build()
        );

        if (res.code() != 200)
            return String.format("Upload Failed. Response : { %d : %s }", res.code(), res.message());

        // build a json object to parse response data, then extract and return link
        String resBody = Objects.requireNonNull(res.body()).string();
        try {
            return new JSONObject(resBody).getJSONObject("data").getString("link");
        }
        catch (JSONException e) {
            Log.e("JSON Parse Failed", "Cannot Convert ResponseBody Content to JSON Object. Content : " + resBody);
            return "Parse HTTP Response Failed.";
        }
    }

}
