package main.dbay.diamonds;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import main.dbay.DialogCustom;
import main.dbay.JSONParser;
import main.dbay.R;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 3/04/15.
 */
public class DiamondsActivity extends Fragment implements View.OnClickListener {
    String[] shapes = {"Круг", "Принцесса", "Сердце", "Груша", "Радиант", "Изумруд", "Овал", "Маркиз"};
    int thumbs[] = {R.drawable.p_diamond_round, R.drawable.p_diamond_princess, R.drawable.p_diamond_heart, R.drawable.p_diamond_pear, R.drawable.p_diamond_radiant, R.drawable.p_diamond_emerald, R.drawable.p_diamond_oval, R.drawable.p_diamond_marquise};
    private TextView diamondShapeName;
    private ImageView thumbImage;
    private static int requiredFile = R.raw.rounds;
    public Button askPriceButton, backButton;
    ProgressBar preloader;
    DialogCustom dg;

    private DiamondsDataListAdapter diamondsRowAdapter;
    private ArrayList<DiamondsListItemModel> diamondsArray;
    private ListView listview = null;
    public static ArrayList pickedDiamondsArray;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        if (container != null) {
            container.removeAllViews();
        }
        View diamondsView = inflater.inflate(R.layout.diamonds, container, false);
        Spinner diamondsSpinner = (Spinner) diamondsView.findViewById(R.id.diams_picker);
        diamondsSpinner.setAdapter(new SpinnerAdapter(getActivity(), R.layout.row_custom_spinner, shapes));
        preloader = (ProgressBar) diamondsView.findViewById(R.id.diamonds_preloader);
        dg = new DialogCustom(getActivity());

        listview = (ListView) diamondsView.findViewById(android.R.id.list);

        askPriceButton = (Button) diamondsView.findViewById(R.id.ask_question);
        askPriceButton.setOnClickListener(this);


        diamondsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 0:
                        requiredFile = R.raw.rounds;
                        break;
                    case 1:
                        requiredFile = R.raw.princess;
                        break;
                    case 2:
                        requiredFile = R.raw.heart;
                        break;
                    case 3:
                        requiredFile = R.raw.pear;
                        break;
                    case 4:
                        requiredFile = R.raw.radiant;
                        break;
                    case 5:
                        requiredFile = R.raw.emerald;
                        break;
                    case 6:
                        requiredFile = R.raw.oval;
                        break;
                    case 7:
                        requiredFile = R.raw.marquise;
                        break;
                }
                loadDiamondsJSONFromAsset(requiredFile);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return diamondsView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ask_question:
                if (pickedDiamondsArray.size() < 1) {
                    ToastCustom tc = new ToastCustom(getActivity());
                    tc.showCustomToast(1);
                }else{
                    dg.showAskQuestionDialog();
                }
                break;
        }
    }


    public class SpinnerAdapter extends ArrayAdapter<String> {
        public SpinnerAdapter(Context context, int resource, String[] object) {
            super(context, resource, object);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater(null);
            View mySpinner = inflater.inflate(R.layout.row_custom_spinner, parent, false);
            diamondShapeName = (TextView) mySpinner.findViewById(R.id.diam_shape_name);
            thumbImage = (ImageView) mySpinner.findViewById(R.id.diam_thumb);
            diamondShapeName.setText(shapes[position]);
            thumbImage.setImageResource(thumbs[position]);
            return mySpinner;
        }


    }


    private JSONArray loadDiamondsJSONFromAsset(int dbName) {
        String json = null;
        JSONArray jsa = null;

        try {
            InputStream is = getActivity().getResources().openRawResource(dbName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsa = new JSONArray(json);

            diamondsArray = new ArrayList<DiamondsListItemModel>();
            String shapeDB, weightDB, colorDB, clarityDB, polishDB, symmetryDB, fluoricenceDB, certificateDB, dimmentionsDB;
            if (json.length() > 0) {
                for (int i = 0; i < jsa.length(); i++) {
                    try {
                        shapeDB = jsa.getJSONObject(i).getString("forma");
                        weightDB = jsa.getJSONObject(i).getString("carat");
                        colorDB = jsa.getJSONObject(i).getString("color");
                        clarityDB = jsa.getJSONObject(i).getString("clarity");
                        polishDB = jsa.getJSONObject(i).getString("polish");
                        symmetryDB = jsa.getJSONObject(i).getString("cymmetry");
                        fluoricenceDB = jsa.getJSONObject(i).getString("florescence");
                        certificateDB = jsa.getJSONObject(i).getString("certificate");
                        dimmentionsDB = jsa.getJSONObject(i).getString("size");
                        diamondsArray.add(new DiamondsListItemModel(shapeDB, weightDB, colorDB, clarityDB, polishDB, symmetryDB, fluoricenceDB, certificateDB, dimmentionsDB));
                    } catch (JSONException e) {
                    }
                }
                Collections.sort(diamondsArray);
                diamondsRowAdapter = new DiamondsDataListAdapter(getActivity(), diamondsArray);
                listview.setAdapter(diamondsRowAdapter);
                preloader.setVisibility(View.GONE);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}


