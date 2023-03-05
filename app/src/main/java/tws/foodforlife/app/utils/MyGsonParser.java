package tws.foodforlife.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGsonParser {
    private static Gson gson;

    public static Gson getGsonParser(){
        if(gson == null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        }
        return gson;
    }

}
