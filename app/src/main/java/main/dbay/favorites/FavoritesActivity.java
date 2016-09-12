package main.dbay.favorites;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import main.dbay.ItemStageCombibedFragment;
import main.dbay.MainActivity;
import main.dbay.R;

/**
 * Created by vladvidavsky on 26/05/15.
 */
public class FavoritesActivity extends Fragment {

    //private static CombinedDatalistAdapter combinedAdapter;
    private ListView listview = null;
    private static TextView empty_list_notice;
    private MainActivity mac = new MainActivity();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        } //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        View favoritesView = inflater.inflate(R.layout.favorites, container, false);
        MainActivity.populateArrayFromSQL();
        final ItemStageCombibedFragment stage = new ItemStageCombibedFragment();

        listview = (ListView) favoritesView.findViewById(android.R.id.list);
        //combinedAdapter = new CombinedDatalistAdapter(getActivity(), favoritesList);
        listview.setAdapter(MainActivity.combinedAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_f = (TextView) view.findViewById(R.id.item_id);
                TextView metal_f = (TextView) view.findViewById(R.id.metal_txt);
                TextView details_f = (TextView) view.findViewById(R.id.details_txt);
                TextView certificate_f = (TextView) view.findViewById(R.id.certificate_txt);
                TextView price_f = (TextView) view.findViewById(R.id.price_txt);
                TextView image_f = (TextView) view.findViewById(R.id.image_big_link);
                TextView camimage_f = (TextView) view.findViewById(R.id.camera_image);

                String stage_id = id_f.getText().toString();
                String stage_metal = metal_f.getText().toString();
                String stage_details = details_f.getText().toString();
                String stage_certificate = certificate_f.getText().toString();
                String stage_price = price_f.getText().toString();
                String stage_image = image_f.getText().toString();
                String stage_camimage = camimage_f.getText().toString();


                if (mac.iAmOnTablet) {
                    stage.fillStageData(getActivity(), stage_id, stage_metal, stage_details, stage_certificate, stage_price, stage_image, stage_camimage);
                } else {
                    stage.importedActivity = getActivity();
                    stage.selItemId = stage_id;
                    stage.selMetal = stage_metal;
                    stage.selDetails = stage_details;
                    stage.selCertificate = stage_certificate;
                    stage.selPrice = stage_price;
                    stage.selImage = stage_image;
                    stage.selCamImage = stage_camimage;
                    mac.fragmentToLoad = new ItemStageCombibedFragment();
                    mac.mainFragman.beginTransaction().replace(R.id.data_screen, mac.fragmentToLoad, "top").addToBackStack(null).commit();
                }
            }
        });
        return favoritesView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(!MainActivity.iAmOnTablet && MainActivity.favoritesIds.size() < 1){
            empty_list_notice = (TextView) view.findViewById(R.id.no_favorites_notice);
            empty_list_notice.setVisibility(View.VISIBLE);
        }
    }
}
