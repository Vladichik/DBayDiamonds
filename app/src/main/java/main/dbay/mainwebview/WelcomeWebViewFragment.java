package main.dbay.mainwebview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import main.dbay.JSONParser;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 1/05/15.
 */
public class WelcomeWebViewFragment extends Fragment {

    MainActivity mact;
    private FastDeliveryDataListAdapter FDDLAdapter;
    private GridView gridView = null;
    ProgressBar preloader;
    TextView title;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews(); //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        }
        View fastView = inflater.inflate(R.layout.webview_main, container, false);
        preloader = (ProgressBar) fastView.findViewById(R.id.fastDeliveryPreloader);
        title = (TextView) fastView.findViewById(R.id.fast_title);
        gridView = (GridView) fastView.findViewById(R.id.items_grid);
        mact = new MainActivity();

        return fastView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.receivedFastDeiveryItems == null) {
                    if (mact.iAmOnTablet) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (MainActivity.connectionStatus) {
                                    new getFastDeliveryItems().execute();
                                } else {
                                    System.out.println("LOADING FAST DELIVERY FAILED");
                                }
                            }
                        }, 5000);
                    } else {
                        new getFastDeliveryItems().execute();
                    }
                } else {
                    populateGridList();
                }
            }
        }, 300);
    }

    private void populateGridList() {
        FDDLAdapter = new FastDeliveryDataListAdapter(getActivity(), mact.receivedFastDeiveryItems);
        title.setVisibility(View.VISIBLE);
        gridView.setAdapter(FDDLAdapter);
        preloader.setVisibility(View.GONE);
    }


    public class getFastDeliveryItems extends AsyncTask<String, String, JSONArray> {

        String url = "http://d-bay.co.il/diamonds_db/fast_dlvry.json";
        JSONArray items;

        @Override
        protected void onPreExecute() {
            preloader.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            JSONObject receivedData = jParser.queryRESTurl(url);
            try {
                items = receivedData.getJSONArray("fast_delivery");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return items;
        }

        @Override
        protected void onPostExecute(JSONArray data) {
            mact.receivedFastDeiveryItems = new ArrayList<FastDeliveryListItemModel>();
            String id, image, metal, description, certificate, price;
            if (data != null && data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        id = data.getJSONObject(i).getString("id");
                        image = data.getJSONObject(i).getString("img");
                        metal = data.getJSONObject(i).getString("metal");
                        description = data.getJSONObject(i).getString("details");
                        certificate = data.getJSONObject(i).getString("certificate");
                        price = data.getJSONObject(i).getString("price");
                        mact.receivedFastDeiveryItems.add(new FastDeliveryListItemModel(id, image, metal, description, certificate, price));
                    } catch (JSONException e) {

                    }
                }
            }
            populateGridList();
        }
    }

}
