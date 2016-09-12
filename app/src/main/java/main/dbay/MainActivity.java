package main.dbay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import main.dbay.combined.CombinedActivity;
import main.dbay.combined.CombinedDatalistAdapter;
import main.dbay.combined.CombinedListItemModel;
import main.dbay.earings.EaringsActivity;
import main.dbay.mainwebview.FastDeliveryListItemModel;
import main.dbay.misc.MiscActivity;
import main.dbay.rings.RingsActivity;
import main.dbay.sqlitedb.DBHandler;


public class MainActivity extends FragmentActivity {
    public Fragment fragmentToLoad;
    public static FragmentManager mainFragman;
    public static boolean iAmOnTablet;
    public ArrayList<JewelleryListItemModel> catalogItemsList;
    public ArrayList<CombinedListItemModel> catalogCombinedList;
    public static ArrayList<FastDeliveryListItemModel> receivedFastDeiveryItems;
    public DialogCustom dialogs;
    public static boolean connectionStatus = false;
    public static String currencyAbriviation = " руб.";
    public static Double currencyValue = 0.0; //Equals to 0.0 because null crashes the app when there is no internet connection
    public static String idToSend, bigImageLink, cameraImageLink, country, latitude, longitude;


    public static DBHandler dbh;
    public static ArrayList<CombinedListItemModel> favoritesList;
    public static ArrayList<String> favoritesIds;
    public static CombinedDatalistAdapter combinedAdapter;

    final String PREFS_NAME = "MyPrefsFile";
    public static Boolean firstLoad = false;

    public static int screenWidth = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new checkDeviceConectivity().execute();
        dialogs = new DialogCustom(MainActivity.this);
        dialogs.showSplashScreen();

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            iAmOnTablet = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            iAmOnTablet = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fragmentToLoad = new MainScreenFragment();
            FragmentManager fr = getSupportFragmentManager();
            fr.beginTransaction().replace(R.id.data_screen, fragmentToLoad, "top").addToBackStack(null).commit();
        }

        mainFragman = getSupportFragmentManager();
        dbh = new DBHandler(MainActivity.this, MainActivity.this);
        favoritesList = new ArrayList<CombinedListItemModel>();
        favoritesIds = new ArrayList<String>();
        combinedAdapter = new CombinedDatalistAdapter(MainActivity.this, favoritesList);
        populateArrayFromSQL();


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            firstLoad = true;
            settings.edit().putBoolean("my_first_time", false).apply();
        }

        if (!iAmOnTablet) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }


//        // configure Flurry
//        FlurryAgent.setLogEnabled(false);
//        // init Flurry
//        FlurryAgent.init(this, Constants.FLURRY_API_KEY);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public JSONArray loadJSONFromAsset(int dbName, Activity aac, int catalogType) {
        String json = null;
        JSONArray jsa = null;

        try {
            InputStream is = aac.getResources().openRawResource(dbName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            try {
                if (catalogType != 4) {
                    catalogItemsList = new ArrayList<JewelleryListItemModel>();
                    jsa = new JSONArray(json);
                    for (int i = 0; i < jsa.length(); i++) {
                        String id = jsa.getJSONObject(i).getString("id");
                        String metal = jsa.getJSONObject(i).getString("metal");
                        String total = jsa.getJSONObject(i).getString("total");
                        String center = jsa.getJSONObject(i).getString("center");
                        String color = jsa.getJSONObject(i).getString("color");
                        String clarity = jsa.getJSONObject(i).getString("clarity");
                        String certificate = jsa.getJSONObject(i).getString("certificate");
                        String price = jsa.getJSONObject(i).getString("price");
                        String image = jsa.getJSONObject(i).getString("image_app");
                        String camera_image = jsa.getJSONObject(i).getString("thumb");
                        catalogItemsList.add(new JewelleryListItemModel(id, metal, total, center, color, clarity, certificate, price, image, camera_image));
                    }
                } else {
                    catalogCombinedList = new ArrayList<CombinedListItemModel>();
                    jsa = new JSONArray(json);
                    for (int i = 0; i < jsa.length(); i++) {
                        String id = jsa.getJSONObject(i).getString("id");
                        String metal = jsa.getJSONObject(i).getString("metal");
                        String details = jsa.getJSONObject(i).getString("details");
                        String certificate = jsa.getJSONObject(i).getString("certificate");
                        String price = jsa.getJSONObject(i).getString("price");
                        String image = jsa.getJSONObject(i).getString("image_app");
                        String camera_image = jsa.getJSONObject(i).getString("thumb");
                        catalogCombinedList.add(new CombinedListItemModel(id, metal, details, certificate, price, image, camera_image));
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            switch (catalogType) {
                case 1:
                    RingsActivity riac = new RingsActivity();
                    riac.populateRingsList(aac, catalogItemsList);
                    break;
                case 2:
                    EaringsActivity erac = new EaringsActivity();
                    erac.populateEaringsList(aac, catalogItemsList);
                    break;
                case 3:
                    MiscActivity miac = new MiscActivity();
                    miac.populateMiscList(aac, catalogItemsList);
                    break;
                case 4:
                    CombinedActivity ciac = new CombinedActivity();
                    ciac.populateCombinedList(aac, catalogCombinedList);
                    break;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsa;

    }


    public class checkDeviceConectivity extends AsyncTask<String, String, String> {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        @Override
        protected String doInBackground(String... params) {
            String connectionIsOk = null;
            if (activeNetworkInfo != null) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection)
                            (new URL("http://clients3.google.com/generate_204")
                                    .openConnection());
                    urlc.setRequestProperty("User-Agent", "Android");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    connectionIsOk = "CONNECTED";
                    return connectionIsOk;
                } catch (IOException e) {

                }
            } else {

            }
            return connectionIsOk;
        }

        @Override
        protected void onPostExecute(String connectionIsOk) {
            if (connectionIsOk == "CONNECTED") {
                connectionStatus = true;
                MainScreenFragment.ringsButton.setEnabled(true);
                MainScreenFragment.earingsButton.setEnabled(true);
                MainScreenFragment.miscButton.setEnabled(true);
                MainScreenFragment.combinedBtn.setEnabled(true);
                MainScreenFragment.rapaportBtn.setEnabled(true);
                MainScreenFragment.fastDeliveryBtn.setEnabled(true);
                MainScreenFragment.diamondsButton.setEnabled(true);
                MainScreenFragment.contactsBtn.setEnabled(true);
                MainScreenFragment.favoritesBtn.setEnabled(true);
                new getUserCountry().execute();
                System.out.println("CONNECTED TO INTERNET");
            } else {
                connectionStatus = false;
                MainScreenFragment.ringsButton.setEnabled(false);
                MainScreenFragment.earingsButton.setEnabled(false);
                MainScreenFragment.miscButton.setEnabled(false);
                MainScreenFragment.combinedBtn.setEnabled(false);
                MainScreenFragment.rapaportBtn.setEnabled(false);
                MainScreenFragment.fastDeliveryBtn.setEnabled(false);
                MainScreenFragment.diamondsButton.setEnabled(false);
                MainScreenFragment.contactsBtn.setEnabled(false);
                MainScreenFragment.favoritesBtn.setEnabled(false);
                if (!connectionStatus) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogs.showNoInternetConnectionDialog();
                        }
                    }, 6000);

                }
                System.out.println("NO INTERNET CONNECTION");
            }
        }
    }


    public class getUserCountry extends AsyncTask<String, String, JSONObject> {
        String requestUrl = "http://ipinfo.io/json";

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.queryRESTurl(requestUrl);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject locationData) {
            String currencyUrl = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22USDRUB%22%29&format=json&env=store://datatables.org/alltableswithkeys&callback=";
            float flLatitude, flLongitude;
            try {
                country = locationData.getString("country");
            } catch (Exception e) {
                country = "RU";
                FlurryAgent.logEvent("IPINFO.io geolocation service unavailable");
                e.printStackTrace();
            }
            try {
                latitude = locationData.getString("loc");
                longitude = latitude.substring(latitude.indexOf(",") + 1, latitude.length());
                latitude = latitude.substring(0, latitude.indexOf(","));
                flLatitude = Float.parseFloat(latitude);
                flLongitude = Float.parseFloat(longitude);
                //System.out.println("USER LOCATION IS " + flLatitude + "/" + flLongitude);
            } catch (Exception e) {
                // IF USER LOCATION IS UNAVAILABLE SET NEW ZEALAND CAPE REINGA ;)
                flLatitude = Float.parseFloat("-34.426735");
                flLongitude = Float.parseFloat("172.683449");

            }
            FlurryAgent.setLocation(flLatitude, flLongitude);
            switch (country) {
                case "UA":
                    currencyUrl = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22USDUAH%22%29&format=json&env=store://datatables.org/alltableswithkeys&callback=";
                    currencyAbriviation = " грив.";
                    FlurryAgent.logEvent("User is Ukrainian");
                    break;
                case "MD":
                    currencyUrl = "http://query.yahooapis.com/v1/public/yql?q=select%20%2a%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22USDMDL%22%29&format=json&env=store://datatables.org/alltableswithkeys&callback=";
                    currencyAbriviation = " lei";
                    FlurryAgent.logEvent("User is Moldovan");
                    break;
                case "RU":
                    FlurryAgent.logEvent("User is Russian");
                    break;
//                case "IL":
//                    currencyUrl = "http://jsonrates.com/get/?from=USD&to=UAH&apiKey=jr-d0ef8ea7dcdfa95cea75e6b47a6b915d";
//                    currencyAbriviation = " грив.";
//                    break;

            }

            new getLocalCurrencyRate().execute(currencyUrl);
        }

    }

    public class getLocalCurrencyRate extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String url = params[0];
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.queryRESTurl(url);
            return json;

        }

        @Override
        protected void onPostExecute(JSONObject value) {
            try {
                JSONObject temp = value.getJSONObject("query");
                temp = temp.getJSONObject("results");
                temp = temp.getJSONObject("rate");
                String tempv = temp.getString("Rate");
                tempv = tempv.replace(",", ".");
                Double doubleValue = new Double(tempv);
                currencyValue = doubleValue;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("LOCAL CURRENCY VAUE " + currencyValue);
            DeviceData.GetUserData(MainActivity.this, country, latitude, longitude);
            CallHomeToVerify.CheckUserKashrut(MainActivity.this);
        }
    }


    public static void populateArrayFromSQL() {
        favoritesList.clear();
        favoritesIds.clear();
        Cursor cursor = MainActivity.dbh.getAllItems();
        while (cursor.moveToNext()) {
            String sqlId = cursor.getString(1),
                    sqlMetal = cursor.getString(2),
                    sqlDetails = cursor.getString(3),
                    sqlCertificate = cursor.getString(4),
                    sqlPrice = cursor.getString(5),
                    sqlCamera = cursor.getString(6),
                    sqlImage = cursor.getString(7);
            favoritesList.add(new CombinedListItemModel(sqlId, sqlMetal, sqlDetails, sqlCertificate, sqlPrice, sqlImage, sqlCamera));
            favoritesIds.add(sqlId);
        }
        if (MainActivity.iAmOnTablet) {
            MainScreenFragment.setFavoritesBtnState();
        }
        combinedAdapter.notifyDataSetChanged();
    }


//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);
//        FlurryAgent.setLogEnabled(true);
//        FlurryAgent.setLogEvents(true);
//        FlurryAgent.setReportLocation(true);
//    }
//
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        FlurryAgent.onEndSession(this);
//    }


}
