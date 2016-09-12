package main.dbay;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import main.dbay.camera.CameraViewFragment;
import main.dbay.favorites.FavoritesActivity;
import main.dbay.sqlitedb.DBHandler;

/**
 * Created by vladvidavsky on 24/04/15.
 */
public class ItemStageCombibedFragment extends Fragment implements View.OnClickListener {
    private TextView itemId, itemMetal, itemDetails, itemCertificate, itemPrice;
    private ImageView itemImage;
    private ImageButton shareBtn, askQuestion, cameraButton, addToFavorites;
    DialogCustom customDialogs;
    public static String selItemId, selMetal, selDetails, selCertificate, selPrice, selImage, selCamImage;
    private static String favtsMetal, favtsDetails, favtsCert, favtsPrice, favtsVideo = "";
    public static Activity importedActivity;
    MainActivity mainActivity;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        } //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        View stageCombined = inflater.inflate(R.layout.stage_poludragi, container, false);
        customDialogs = new DialogCustom(getActivity());
        shareBtn = (ImageButton) stageCombined.findViewById(R.id.share);
        askQuestion = (ImageButton) stageCombined.findViewById(R.id.question);
        cameraButton = (ImageButton) stageCombined.findViewById(R.id.camera_btn);
        addToFavorites = (ImageButton) stageCombined.findViewById(R.id.add_to_favorites);

        cameraButton.setEnabled(false);
        addToFavorites.setEnabled(false);

        shareBtn.setOnClickListener(this);
        askQuestion.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        addToFavorites.setOnClickListener(this);
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.img_loading));
        mainActivity = new MainActivity();

        return stageCombined;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (!MainActivity.iAmOnTablet) {
            fillStageData(importedActivity, selItemId, selMetal, selDetails, selCertificate, selPrice, selImage, selCamImage);
        }
    }

    public void fillStageData(Activity a, String id, String metal, String details, String certificate, String price, String image_link, String cam_image_link) {
        //int imgResource = a.getResources().getIdentifier(id.toLowerCase(), "drawable", a.getPackageName());
        itemId = (TextView) a.findViewById(R.id.stage_id);
        itemMetal = (TextView) a.findViewById(R.id.stage_metal);
        itemDetails = (TextView) a.findViewById(R.id.item_details);
        itemCertificate = (TextView) a.findViewById(R.id.stage_certificate);
        itemPrice = (TextView) a.findViewById(R.id.stage_price);
        itemImage = (ImageView) a.findViewById(R.id.stage_image);
        ImageButton camBut = (ImageButton) a.findViewById(R.id.camera_btn);
        ImageButton addToFavors = (ImageButton) a.findViewById(R.id.add_to_favorites);

        itemId.setText(id);
        itemMetal.setText(metal);
        itemDetails.setText(details);
        itemCertificate.setText(certificate);
        itemPrice.setText(price);
        //itemImage.setImageResource(imgResource);
        MainActivity.idToSend = id;
        MainActivity.bigImageLink = image_link;
        MainActivity.cameraImageLink = cam_image_link;
        favtsMetal = metal;
        favtsDetails = details;
        favtsCert = certificate;
        favtsPrice = price.equals("по запросу") ? "0" : Calculations.formatPriceForSQL(price);
        BitmapManager.INSTANCE.loadBitmap(Constants.BIG_APPLICATION_IMAGES_URL + image_link, itemImage, 720, 520, true);
        addToFavors.setEnabled(true);
        if (cam_image_link.equals("")) {
            camBut.setEnabled(false);
        } else {
            camBut.setEnabled(true);
        }
        addToFavors.setActivated(Calculations.checkItemIsFavorite(id));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                //View element = getActivity().findViewById(R.id.stage_combined);
                View element = (View) v.getParent().getParent().getParent();
                String filename = Constants.FILE_NAME_JEWELERY_COMBINED;
                AdvancedActions.shareElement(element, getActivity(), filename);
                break;

            case R.id.question:
                customDialogs.showQuestionVerificationDialog(2);
                break;
            case R.id.camera_btn:
                customDialogs.showCameraViewDialog();
                FlurryAgent.logEvent("Opened camera for " + MainActivity.idToSend);
                break;
            case R.id.add_to_favorites:
                addToFavorites.setActivated(!addToFavorites.isActivated());
                if (addToFavorites.isActivated()) {
                    MainActivity.dbh.saveItemToFavorites(MainActivity.idToSend, favtsMetal, favtsDetails, favtsCert, favtsPrice, MainActivity.cameraImageLink, MainActivity.bigImageLink, favtsVideo);
                    MainActivity.populateArrayFromSQL();
                    FlurryAgent.logEvent("Added to favorites " + MainActivity.idToSend);
                } else {
                    MainActivity.dbh.deleteItemFromFavorites(MainActivity.idToSend);
                    MainActivity.populateArrayFromSQL();
                    FlurryAgent.logEvent("Removed from favorites " + MainActivity.idToSend);
                }
                break;
        }
    }
}
