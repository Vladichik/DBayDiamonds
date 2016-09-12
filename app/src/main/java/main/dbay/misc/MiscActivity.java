package main.dbay.misc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import main.dbay.ItemStageFragment;
import main.dbay.JewelleryDatalistAdapter;
import main.dbay.JewelleryListItemModel;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 11/04/15.
 */
public class MiscActivity extends Fragment {
    private MainActivity mac = new MainActivity();
    private static int requiredCatalog = R.raw.misc;
    private JewelleryDatalistAdapter jewelleryAdapter;
    private ArrayList<JewelleryListItemModel> jewelleryArray;
    private ListView listview = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        } //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        View miscView = inflater.inflate(R.layout.misc, container, false);

        if (MainActivity.iAmOnTablet) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mac.loadJSONFromAsset(requiredCatalog, getActivity(), 3);
                }
            }, 200);
        }

        return miscView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!MainActivity.iAmOnTablet) {
            mac.loadJSONFromAsset(requiredCatalog, getActivity(), 3);
        }
    }

    public void populateMiscList(final Activity activ, ArrayList dataBase) {
        Collections.sort(dataBase);
        final ItemStageFragment stage = new ItemStageFragment();
        listview = (ListView) activ.findViewById(android.R.id.list);
        jewelleryAdapter = new JewelleryDatalistAdapter(activ, dataBase);
        listview.setAdapter(jewelleryAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_f = (TextView) view.findViewById(R.id.item_id);
                TextView metal_f = (TextView) view.findViewById(R.id.metal_txt);
                TextView total_f = (TextView) view.findViewById(R.id.total_txt);
                TextView center_f = (TextView) view.findViewById(R.id.center_txt);
                TextView color_f = (TextView) view.findViewById(R.id.color_txt);
                TextView clarity_f = (TextView) view.findViewById(R.id.clarity_txt);
                TextView certificate_f = (TextView) view.findViewById(R.id.certificate_txt);
                TextView price_f = (TextView) view.findViewById(R.id.price_txt);
                TextView image_f = (TextView) view.findViewById(R.id.image_big_link);
                TextView camimage_f = (TextView) view.findViewById(R.id.camera_image);

                String stage_id = id_f.getText().toString();
                String stage_metal = metal_f.getText().toString();
                String stage_total = total_f.getText().toString();
                String stage_center = center_f.getText().toString();
                String stage_color = color_f.getText().toString();
                String stage_clarity = clarity_f.getText().toString();
                String stage_certificate = certificate_f.getText().toString();
                String stage_price = price_f.getText().toString();
                String stage_image = image_f.getText().toString();
                String stage_camimage = camimage_f.getText().toString();

                if (mac.iAmOnTablet) {
                    stage.fillStageData(activ, stage_id, stage_metal, stage_total, stage_center, stage_color, stage_clarity, stage_certificate, stage_price, stage_image, stage_camimage);
                } else {
                    stage.importedActivity = activ;
                    stage.selItemId = stage_id;
                    stage.selMetal = stage_metal;
                    stage.selTotal = stage_total;
                    stage.selWeight = stage_center;
                    stage.selColor = stage_color;
                    stage.selClarity = stage_clarity;
                    stage.selCertificate = stage_certificate;
                    stage.selPrice = stage_price;
                    stage.selImage = stage_image;
                    stage.selCamImage = stage_camimage;
                    mac.fragmentToLoad = new ItemStageFragment();
                    mac.mainFragman.beginTransaction().replace(R.id.data_screen, mac.fragmentToLoad, "top").addToBackStack(null).commit();
                }
            }
        });
        if (MainActivity.iAmOnTablet) {
            listview.performItemClick(listview.getAdapter().getView(0, null, null), 0, listview.getItemIdAtPosition(0));
        }
    }

}
