package cn.ac.ict.alpha.Utils;

import android.content.Context;

import static java.util.UUID.randomUUID;

/**
 * Created by yxd on 17/11/2016.
 */

public class FileUtils {
    public static String getFilePath(Context context, String type) {
        String ext;
        switch (type) {
            case "Face":
                ext = ".mp4";
                break;
            case "Sound":
                ext = ".3gp";
                break;
            default:
                ext = ".txt";
        }
        return context.getFilesDir().getAbsolutePath()+"/tempdata/" + randomUUID().toString() + ext;
    }
}
