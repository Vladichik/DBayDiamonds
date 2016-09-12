package main.dbay.bazar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import main.dbay.BitmapManager;
import main.dbay.Calculations;
import main.dbay.Constants;
import main.dbay.MainActivity;
import main.dbay.R;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 9/15/15.
 */
public class BazarStageActivity extends Fragment implements View.OnClickListener {

    TextView itemTv, metalTv, centstoneTv, shapeTv, weightTv, colorTv, clarityTv, totalTv, certifTv, modelTv, boxTv, docsTv, descTv, sellernameTv, sellphoneTv, sellmailTv, skypeTv, cityTv, dollarsTv, convertedPriceTv;
    LinearLayout watchModelSection, watchBoxSection, watchDocsSection, metalSection, jewlCenterStone, totalWeightSection, shapeSection, weightSection, colorSection, claritySection, certifSection;
    ImageView image1, image2, image3;
    private static String AlreadyHideElements;
    private Boolean fieldsAreLoaded = false;
    private static Context context;
    private static Activity activ;
    private ToastCustom tc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        if (container != null) {
            container.removeAllViews();
        }
        context = getActivity();
        activ = getActivity();
        View bazarView = inflater.inflate(R.layout.stage_bazar, container, false);
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.img_loading));
        return bazarView;
    }

    public void populateBazarStage(String collection, Activity act, String item, String metal, String centStone, String shape, String weight, String color, String clarity, String total, String certif, String model, String box, String docs, String desc, String sellername, String phone, String mail, String skype, String city, String i1, String i2, String i3, String price, String cprice) {
        if (!fieldsAreLoaded) { //Обозначение селекторов должно происходить только когда метод запущен впервые. Нет нужды обозначать селекторы при каждом запуске метода
            watchModelSection = (LinearLayout) act.findViewById(R.id.watch_model_section);
            watchBoxSection = (LinearLayout) act.findViewById(R.id.watch_box_section);
            watchDocsSection = (LinearLayout) act.findViewById(R.id.watch_docs_section);
            metalSection = (LinearLayout) act.findViewById(R.id.metal_section);
            jewlCenterStone = (LinearLayout) act.findViewById(R.id.jewl_center_stone_section);
            totalWeightSection = (LinearLayout) act.findViewById(R.id.total_weight_section);
            shapeSection = (LinearLayout) act.findViewById(R.id.stone_shape_section);
            colorSection = (LinearLayout) act.findViewById(R.id.color_section);
            claritySection = (LinearLayout) act.findViewById(R.id.clarity_section);
            weightSection = (LinearLayout) act.findViewById(R.id.weight_section);
            certifSection = (LinearLayout) act.findViewById(R.id.certf_section);
            itemTv = (TextView) act.findViewById(R.id.stage_item);
            descTv = (TextView) act.findViewById(R.id.bazar_description);
            sellernameTv = (TextView) act.findViewById(R.id.bazar_sellername);
            sellphoneTv = (TextView) act.findViewById(R.id.bazar_sellerphone);
            sellmailTv = (TextView) act.findViewById(R.id.bazar_sellermail);
            skypeTv = (TextView) act.findViewById(R.id.bazar_sellerskype);
            dollarsTv = (TextView) act.findViewById(R.id.bazar_dollars);
            convertedPriceTv = (TextView) act.findViewById(R.id.bazer_conv_price);
            cityTv = (TextView) act.findViewById(R.id.bazar_sellercity);
            image1 = (ImageView) act.findViewById(R.id.image1);
            image2 = (ImageView) act.findViewById(R.id.image2);
            image3 = (ImageView) act.findViewById(R.id.image3);
            sellmailTv.setOnClickListener(this);
            sellphoneTv.setOnClickListener(this);

            fieldsAreLoaded = true;
        }
        switch (collection) {
            case "jewls":
                metalTv = (TextView) act.findViewById(R.id.bazar_stage_metal);
                centstoneTv = (TextView) act.findViewById(R.id.bazar_center_stone);
                shapeTv = (TextView) act.findViewById(R.id.bazar_shape);
                weightTv = (TextView) act.findViewById(R.id.bazar_weight);
                colorTv = (TextView) act.findViewById(R.id.bazar_color);
                clarityTv = (TextView) act.findViewById(R.id.bazar_clarity);
                totalTv = (TextView) act.findViewById(R.id.bazar_total);
                certifTv = (TextView) act.findViewById(R.id.bazar_certificate);
                metalTv.setText(metal);
                centstoneTv.setText(centStone);
                shapeTv.setText(shape);
                weightTv.setText(weight);
                colorTv.setText(color);
                clarityTv.setText(clarity);
                totalTv.setText(total);
                certifTv.setText(certif);
                break;
            case "stones":
                shapeTv = (TextView) act.findViewById(R.id.bazar_shape);
                weightTv = (TextView) act.findViewById(R.id.bazar_weight);
                colorTv = (TextView) act.findViewById(R.id.bazar_color);
                clarityTv = (TextView) act.findViewById(R.id.bazar_clarity);
                certifTv = (TextView) act.findViewById(R.id.bazar_certificate);
                shapeTv.setText(shape);
                weightTv.setText(weight);
                colorTv.setText(color);
                clarityTv.setText(clarity);
                certifTv.setText(certif);
                break;
            case "watches":
                metalTv = (TextView) act.findViewById(R.id.bazar_stage_metal);
                modelTv = (TextView) act.findViewById(R.id.bazar_model);
                boxTv = (TextView) act.findViewById(R.id.bazar_box);
                docsTv = (TextView) act.findViewById(R.id.bazar_docs);
                metalTv.setText(metal);
                modelTv.setText(model);
                boxTv.setText(box);
                docsTv.setText(docs);
                break;
        }
        itemTv.setText(item);
        descTv.setText(desc);
        sellernameTv.setText(sellername);
        sellphoneTv.setText(phone);
        sellmailTv.setText(mail);
        skypeTv.setText(skype);
        cityTv.setText(city);
        dollarsTv.setText(price);
        convertedPriceTv.setText("(" + Calculations.calculateLocalCurrency(cprice) + ")");
        showHideUnusedFields(collection);
        if (i1 != null) {
            image1.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(i1, image1, 1200, 800, false);
        } else {
            image1.setVisibility(View.GONE);
        }
        if (i2 != null) {
            image2.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(i2, image2, 1200, 800, false);
        } else {
            image2.setVisibility(View.GONE);
        }
        if (i3 != null) {
            image3.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(i3, image3, 1200, 800, false);
        } else {
            image3.setVisibility(View.GONE);
        }

    }

    /**
     * Метод показывающий только нужные поля в той или иной категории товаров.
     * @param collection стринг определяющий какой список товаров просматривает пользователь
     */
    private void showHideUnusedFields(String collection) {
        switch (collection) {
            case "jewls":
                if (AlreadyHideElements != "jewls") {
                    watchModelSection.setVisibility(View.GONE);
                    watchBoxSection.setVisibility(View.GONE);
                    watchDocsSection.setVisibility(View.GONE);
                    metalSection.setVisibility(View.VISIBLE);
                    jewlCenterStone.setVisibility(View.VISIBLE);
                    shapeSection.setVisibility(View.VISIBLE);
                    weightSection.setVisibility(View.VISIBLE);
                    colorSection.setVisibility(View.VISIBLE);
                    claritySection.setVisibility(View.VISIBLE);
                    totalWeightSection.setVisibility(View.VISIBLE);
                    certifSection.setVisibility(View.VISIBLE);
                    AlreadyHideElements = "jewls";
                }
                break;
            case "stones":
                if (AlreadyHideElements != "stones") {
                    watchModelSection.setVisibility(View.GONE);
                    watchBoxSection.setVisibility(View.GONE);
                    watchDocsSection.setVisibility(View.GONE);
                    metalSection.setVisibility(View.GONE);
                    jewlCenterStone.setVisibility(View.GONE);
                    shapeSection.setVisibility(View.VISIBLE);
                    weightSection.setVisibility(View.VISIBLE);
                    colorSection.setVisibility(View.VISIBLE);
                    claritySection.setVisibility(View.VISIBLE);
                    totalWeightSection.setVisibility(View.GONE);
                    certifSection.setVisibility(View.VISIBLE);
                    AlreadyHideElements = "stones";
                }
                break;
            case "watches":
                if (AlreadyHideElements != "watches") {
                    watchModelSection.setVisibility(View.VISIBLE);
                    watchBoxSection.setVisibility(View.VISIBLE);
                    watchDocsSection.setVisibility(View.VISIBLE);
                    metalSection.setVisibility(View.VISIBLE);
                    jewlCenterStone.setVisibility(View.GONE);
                    shapeSection.setVisibility(View.GONE);
                    weightSection.setVisibility(View.GONE);
                    colorSection.setVisibility(View.GONE);
                    claritySection.setVisibility(View.GONE);
                    totalWeightSection.setVisibility(View.GONE);
                    certifSection.setVisibility(View.GONE);
                    AlreadyHideElements = "watches";
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bazar_sellermail:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{sellmailTv.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.bazar_question_mail_subj));
                i.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.bazar_question_mail_body) + "\n" + "------------------------- \n" + descTv.getText().toString() + "\n-------------------------- \n");
                try {
                    context.startActivity(Intent.createChooser(i, context.getResources().getString(R.string.bazar_mail_intent_ttl)));
                    FlurryAgent.logEvent("user contacted seller " + sellmailTv.getText().toString());
                } catch (android.content.ActivityNotFoundException ex) {
                    tc.showCustomToast(3);
                }
                break;

            case R.id.bazar_sellerphone:
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM || telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + sellphoneTv.getText().toString().trim()));
                    startActivity(callIntent);
                    FlurryAgent.logEvent("user called seller " + sellernameTv.getText().toString() + " " + sellphoneTv.getText().toString());
                } else {
                    tc = new ToastCustom(activ);
                    tc.showCustomToast(8);
                }
                break;
        }
    }
}
