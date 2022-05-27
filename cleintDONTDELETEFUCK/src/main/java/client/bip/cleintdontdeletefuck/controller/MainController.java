package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.utils.HTTPUtils;
import com.google.gson.Gson;

public class MainController {

    public static Gson gson = new Gson();
    public static String api = "http://localhost:28242/bipapi/";
    public static HTTPUtils http = new HTTPUtils();

}
