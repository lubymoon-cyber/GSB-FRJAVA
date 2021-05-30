package com.ares.gsb_fr;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class APIService {
    private final String USER_AGENT = "Mozilla/5.0";

    private String dataSend="";

    public static String urlApi = "http://192.168.108.203/gsb-fr/api/";

    // HTTP request
    public String sendRequest(String url, String method, Map<String, Object> parameters) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        JSONObject params = new JSONObject();

        String urlParameters = "";
        int i = 1;
        if (method.equals("GET")) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                urlParameters += entry.getKey()+"="+entry.getValue();
                if (i < parameters.size()) {
                    urlParameters += "&";
                }
                i++;
            }
        } else {
            urlParameters = "{";
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                if(entry.getValue() instanceof Integer ) {
                    urlParameters += '"'+entry.getKey()+'"'+":"+entry.getValue();
                } else {
                    urlParameters += '"'+entry.getKey()+'"'+":"+'"'+entry.getValue()+'"';
                }

                if (i < parameters.size()) {
                    urlParameters += ",";
                } else {
                    urlParameters += "}";
                }
                i++;
            }
        }


        // Send request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        //System.out.println(urlParameters);
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        //SI on souhaite recuperer le code de retour de la requete
        //int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        dataSend = response.toString();
        return dataSend;
    }
}