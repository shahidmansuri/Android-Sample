package com.baseproject.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.baseproject.R;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by RajeshKushvaha on 24-05-17
 */

public class Utils {


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    public static int dipToPixels(Activity context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static long getDurationInSecVideo(Context context, File file) {
        if (file == null || !file.exists())
            return 0;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(file));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (TextUtils.isEmpty(time))
            return 0;
        long timeInMillisec = Long.parseLong(time);

        retriever.release();
        return TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
    }

    public static long getFileSizeInMB(File file) {
        if (file == null || !file.exists())
            return 0;

        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024;
    }


    public static void preventDoubleClick(final View view) {
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);
    }

    public static String getMessageFromResponseObject(Context context,JSONObject object){
        String message = context.getString(R.string.str_something_went_wrong);
        if(object!=null && object.has("message")){
            message = object.optString("message");
        }
        return message;
    }
}
