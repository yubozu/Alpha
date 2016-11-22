package cn.ac.ict.alpha.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

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
        return context.getFilesDir().getAbsolutePath()+"/temp_data/" + randomUUID().toString() + ext;
    }
    public static void initFileDir(Context context)
    {
        String[] filePaths={"memoryFilePath","standFilePath","faceFilePath","soundFilePath","tappingFilePath","strideFilePath"};
        SharedPreferences sharedPreferences = context.getSharedPreferences("Alpha",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String s:filePaths) {
            editor.remove(s);
        }
        editor.apply();
        String dir = context.getFilesDir().getAbsolutePath()+"/temp_data/";
        File fdir = new File(dir);
        if(fdir.exists())
        {
            fdir.delete();
        }
        fdir.mkdirs();
    }
}
