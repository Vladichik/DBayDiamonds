package main.dbay;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by vladvidavsky on 3/04/15.
 */
public class JSONParserPost {
    final String TAG = "JsonParser.java";

    static InputStream is = null;
    static JSONArray jObj = null;
    static String json = "";


    public static JSONArray queryRESTurl(String url, String collection, String country, String city, String item) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObj = new JSONObject();

        /**
         * Составляем объект нужных параметров для запроса с сервера
         */
        try {
            jsonObj.put("collection", collection);
            jsonObj.put("country", country);
            jsonObj.put("city", city);
            jsonObj.put("item", item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * Подготавливаем запрос к отправке
         */
        try {
            StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            response = httpclient.execute(httpPost);
            Log.i("TAG", "Status:[" + response.getStatusLine().toString() + "]");
            HttpEntity entity = null;
            try{
                entity = response.getEntity();
            }catch(Exception e){
                Log.e("log_tag", "Error in http connection "+e.toString());
            }
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = RestClient.convertStreamToString(instream);
                instream.close();
                try {
                    jObj = new JSONArray(result);

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                return jObj;

            }
        } catch (ClientProtocolException e) {
            Log.e("REST", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        }

        return jObj;
    }
}
