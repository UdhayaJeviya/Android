package com.example.triplist;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StorageOperation {
    Context context;
    public String title;
    public String description;
    SharedPreferences sharedPreferences;

    public StorageOperation(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Trip",Context.MODE_PRIVATE);
    }

    /**
     * set loginStatus
     * @param status
     */
    public void setLogin(int status){
        SharedPreferences.Editor loginEdit = sharedPreferences.edit();
        loginEdit.putInt("status",status);
        loginEdit.commit();
    }

    /**
     * getLogin status
     * @return
     */
    public int getLogin(){
        int status = sharedPreferences.getInt("status",0);
        return status;
    }
   /*public boolean removeAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public void remove(String place){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(place);
        editor.apply();
    }

    public void setValue(String title, String description){
        this.title = title;
        this.description = description;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(title,description);
        editor.commit();
    }
    public Map<String,ArrayList> getValue(){
        ArrayList<String> keyList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();
        Map<String,ArrayList> TotalList = new HashMap<String, ArrayList>();
        Map<String,?> key = sharedPreferences.getAll();
        for(Map.Entry entry : key.entrySet()){
            keyList.add(String.valueOf(entry.getKey()));
            valueList.add(entry.getValue().toString());
        }
        TotalList.put("Key",keyList);
        TotalList.put("Val",valueList);
        return TotalList;
    }*/
}
