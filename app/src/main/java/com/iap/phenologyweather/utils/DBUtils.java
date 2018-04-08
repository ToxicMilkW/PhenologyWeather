package com.iap.phenologyweather.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by infolife on 2017/2/28.
 */

public class DBUtils {

    private static final String DB_PATH = "/data/data/com.iap.phenologyweather/databases/";
    private static final String DB_NAME = Constants.CITY_DB_NAME;

    private static final String TAG = "DBUtils";


    public static void copyDBToDatabases(Context context, boolean deleteThoughExist) {
        try {
            String outFileName = DB_PATH + DB_NAME;
            File file = new File(DB_PATH);
            if (!file.mkdirs()) {
                file.mkdirs();
            }
            File dataFile = new File(outFileName);
            if (dataFile.exists()) {
                if (deleteThoughExist) {
                    dataFile.delete();
                } else {
                    return;
                }
            }
            InputStream myInput;
            myInput = context.getApplicationContext().getAssets().open(DB_NAME);
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            Log.i(TAG, "error--->" + e.toString());
            e.printStackTrace();
        }

    }
}
