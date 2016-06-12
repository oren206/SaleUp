package com.saleup;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static String[] Cities = new String[]{"Unknown","Acre","Afula","Arad","Ariel","Ashdod","Ashkelon","Baqa-Jatt","Bat Yam",
            "Beersheba","Beit She'an","Beit Shemesh","Beitar Illit","Bnei Brak","Dimona","Eilat","El'ad","Giv'atayim","Giv'at Shmuel",
            "Hadera","Haifa","Herzliya","Hod HaSharon","Holon","Jerusalem","Karmiel","Kafr Qasim","Kfar Saba","Kiryat Ata",
            "Kiryat Bialik","Kiryat Gat","Kiryat Malakhi","Kiryat Motzkin","Kiryat Ono","Kiryat Shmona","Kiryat Yam","Lod",
            "Ma'ale Adumim","Ma'alot-Tarshiha","Migdal HaEmek","Modi'in Illit","Modiin","Nahariya","Nazareth","Nazareth Illit",
            "Nesher","Ness Ziona","Netanya","Netivot","Ofakim","Or Akiva","Or Yehuda","Petah Tikva","Qalansawe","Ra'anana",
            "Rahat","Ramat HaSharon","Ramla","Rehovot","Rishon LeZion","Rosh HaAyin","Safed","Sakhnin","Sderot","Tamra",
            "Tel Aviv","Tiberias","Tira","Tirat Carmel","Umm al-Fahm","Yavne","Yehud-Monosson","Yokneam"};
    public static String[] CitiesId = new String[]{"0","1","2","3","4","5","6","7","8",
            "9","10","11","12","13","14","15","16","17","18",
            "19","20","21","22","23","24","25","26","27","28",
            "29","30","31","32","33","34","35","36",
            "37","38","39","40","41","42","43","43",
            "44","45","46","47","48","49","50","51","52","53",
            "54","55","56","57","58","59","60","61","62","63",
            "64","65","66","67","68","69","70","71"};


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

    public static Item[] appendArray(Item[] array, Item x){
        Item[] result = new Item[array.length + 1];

        for(int i = 0; i < array.length; i++)
            result[i] = array[i];

        result[result.length - 1] = x;

        return result;
    }
}
