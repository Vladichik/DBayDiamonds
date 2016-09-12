package main.dbay;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import com.flurry.android.FlurryAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by vladvidavsky on 11/4/15.
 */
public class CallHomeToVerify {

    private static String postParams, emailForFlurry, emeiForFlurry;

    /**
     * Метод собирающий данные для запроса проверки пользователя
     */
    public static void CheckUserKashrut(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        String emails = null;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails = account.name == null ? "---" : account.name;
            }
        }
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tMgr.getSimSerialNumber() == null ? "---" : tMgr.getSimSerialNumber();
        String IMEI = tMgr.getDeviceId() == null ? "---" : tMgr.getDeviceId();
        emailForFlurry = emails;
        emeiForFlurry = IMEI;
        JSONObject userdataobgect = new JSONObject();
        try {
            userdataobgect.put("imei", IMEI).put("email", emails).put("sim", simSerialNumber);
        } catch (Exception e) {
        }

        postParams = userdataobgect.toString();
        if (postParams != null) {
            new BdikatKashrut().execute();
        }
    }


    /**
     * Связываемся с сервером посылаем данные пользователя
     * и проверяем находится ли пользователь в черном списке
     * если да то вырубаем нахуй приложение
     */
    public static class BdikatKashrut extends AsyncTask<String, String, String> {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppostreq = new HttpPost(Constants.WEBSITE_URL + "/cuk");

        @Override
        protected void onPreExecute() {
            try {
                StringEntity se = new StringEntity(postParams);
                se.setContentType("application/json;charset=UTF-8");
                httppostreq.setEntity(se);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpResponse httpresponse;
            String result = null;
            try {
                httpresponse = httpclient.execute(httppostreq);
                HttpEntity entity = null;
                try {
                    entity = httpresponse.getEntity();
                } catch (Exception e) {
                    Log.e("log_tag", "Error in http connection " + e.toString());
                }
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = RestClient.convertStreamToString(instream);
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String userState) {
            System.out.println(userState);
            if (userState.contains("forbidden")) {
                Map<String, String> flurryParams = new HashMap<String, String>();
                flurryParams.put("email", emailForFlurry);
                flurryParams.put("emei", emeiForFlurry);
                FlurryAgent.logEvent("Forbidden User Blocked", flurryParams);
                System.exit(0);
            }
        }
    }


    /**
     * Метод отсылающий на сервер разные данные о пользовании апликацией для сохранения на сервере.
     * @param context - Activity
     * @param url - Линк на который нужно отослать данные
     * @param dataToSend - JSON объект в стринг формате отправляемый на сервер.
     */
    public static void SendDataHome(Context context, String url, String dataToSend) {
        new SendingDataHome().execute(url, dataToSend);
    }


    public static class SendingDataHome extends AsyncTask<String, String, HttpResponse> {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        @Override
        protected HttpResponse doInBackground(String... params) {
            HttpPost httppostreq = new HttpPost(params[0]);

            try {
                StringEntity se = new StringEntity(params[1]);
                se.setContentType("application/json;charset=UTF-8");
                httppostreq.setEntity(se);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpResponse httpresponse = null;
            try {
                httpresponse = httpclient.execute(httppostreq);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpresponse;
        }

        @Override
        protected void onPostExecute(HttpResponse value) {
        }
    }
}
