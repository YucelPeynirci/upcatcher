package com.hplusbilisim.cibuu.tools;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hplusbilisim.cibuu.MyApp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yucel on 11.01.2018.
 */

public class UpCatcher {
    private HashMap<String,Object> map = new HashMap<>();
    private ActivityManager activityManager;
    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public UpCatcher(Context c){
        activityManager = (ActivityManager) c.getSystemService(ACTIVITY_SERVICE);
        prefs = c.getSharedPreferences("upCatcher", MODE_PRIVATE);
    }

    public void put(String key,Object value){
        map.put(key,value);
        checkMemory();
    }

    public Object get(String key,Class c){
        Object s = map.get(key);
        if(s == null){
            String json = prefs.getString(key,null);
            s = gson.fromJson(json,c);
            map.put(key,s);
        }
        checkMemory();
        return s;
    }

    private void checkMemory(){
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        if (!memoryInfo.lowMemory) {
            int i = 0;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext() && i<=5) {
                i++;
                Map.Entry pair = (Map.Entry)it.next();
                prefs.edit().putString(pair.getKey().toString(), gson.toJson(pair.getValue())).apply();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            System.gc();
        }
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
}
