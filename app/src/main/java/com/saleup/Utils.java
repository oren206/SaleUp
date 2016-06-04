package com.saleup;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

class Result{
    boolean status;
    Object data;
}

interface RunMe {
    Result run(); // would be in any signature
}

class OnRunMe implements RunMe {
    public Result run(){return null;}
}

interface Callback {
    void callback(Result result); // would be in any signature
}

class OnCallback implements Callback {
    public void callback(Result result){}
}

class MyThread implements Runnable {

    RunMe onRun;
    Callback onSuccess;
    Callback onFailed;

    public MyThread(RunMe onRun, Callback onSuccess, Callback onFailed) {
        this.onRun = onRun;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
    }

    public void run() {
        Result result = this.onRun.run();
        if(result.status){
            this.onSuccess.callback(result);
        }
        else{
            this.onFailed.callback(result);
        }

    }
}

public class Utils {

    public static String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.encodeToString(byte_arr,0);
        return imageStr;
    }

    public static byte[] toPremitiveByteArray(ArrayList<Double> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i).byteValue();
        }
        return ret;
    }
}
