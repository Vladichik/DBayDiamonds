package main.dbay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import main.dbay.bazar.BazarActivity;
import main.dbay.camera.CameraViewFragment;
import main.dbay.diamonds.DiamondsActivity;

/**
 * Created by vladvidavsky on 6/04/15.
 */
public class DialogCustom {
    Context cont;
    public Dialog dialog;
    private DiamondsActivity dac = new DiamondsActivity();
    private ToastCustom tc;
    private String certificateType = null;
    private Button hideDialogButton;
    TextView itemTv, metalTv, centstoneTv, shapeTv, weightTv, colorTv, clarityTv, totalTv, certifTv, modelTv, boxTv, docsTv, descTv, sellernameTv, sellphoneTv, sellmailTv, skypeTv, cityTv, dollarsTv, convertedPriceTv;
    LinearLayout watchModelSection, watchBoxSection, watchDocsSection, metalSection, jewlCenterStone, totalWeightSection, shapeSection, weightSection, colorSection, claritySection, certifSection;
    ImageView image1, image2, image3;

    public DialogCustom(Activity act) {
        cont = act;
        dialog = new Dialog(cont);
        tc = new ToastCustom(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public void showSplashScreen() {
        dialog.setContentView(R.layout.splash);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);
    }

    public void showNoInternetConnectionDialog(){
        dialog.setContentView(R.layout.no_internet_connection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        hideDialogButton = (Button) dialog.findViewById(R.id.dialogClose);
        hideDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        FlurryAgent.logEvent("No Internet connection Dialog");
    }


    public void showQuestionVerificationDialog(int type){
        final String itemID = MainActivity.idToSend;
        dialog.setContentView(R.layout.dialog_verify_question);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button proceedBtn = (Button) dialog.findViewById(R.id.proceed_question);
        hideDialogButton = (Button) dialog.findViewById(R.id.cancel_question);
        hideDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.COMPANY_EMAIL});
                intent.putExtra(Intent.EXTRA_SUBJECT, cont.getResources().getString(R.string.question_mail_ttl) + " " + itemID);
                intent.putExtra(Intent.EXTRA_TEXT, "");
                cont.startActivity(intent);
                dialog.dismiss();
                FlurryAgent.logEvent("User Asked Question About Jewel");
            }
        });
        dialog.show();
    }



    public void showContactUsDialog(){
        dialog.setContentView(R.layout.dialog_contacts);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.close_dialog);
        Button sendmail = (Button) dialog.findViewById(R.id.contact_us);
        sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Loading Send Mail Intent
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.COMPANY_EMAIL});
                i.putExtra(Intent.EXTRA_SUBJECT, cont.getResources().getString(R.string.hello_dbay));
                try {
                    cont.startActivity(Intent.createChooser(i, cont.getResources().getString(R.string.contact_dbay)));
                } catch (android.content.ActivityNotFoundException ex) {
                    tc.showCustomToast(3);
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    public void showAskQuestionDialog() {
        dialog.setContentView(R.layout.dialog_diamonds_ques);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ListView diamondsList = (ListView) dialog.findViewById(R.id.picked_diamonds);
        final Spinner certificates = (Spinner) dialog.findViewById(R.id.certificate_picker);
        Button sendMail = (Button) dialog.findViewById(R.id.send_mail);
        ArrayAdapter<String> arad = new ArrayAdapter<String>(cont, R.layout.row_picked_diams_dialog, dac.pickedDiamondsArray);

        ArrayAdapter<CharSequence> certAdapter = ArrayAdapter.createFromResource(cont, R.array.certificates, R.layout.row_certificates_spinner);
        certAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        certificates.setAdapter(certAdapter);

        Button closeDialog = (Button) dialog.findViewById(R.id.close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        certificates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                certificateType = certificates.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (certificateType != null && !certificateType.equals("---")) {
                    FlurryAgent.logEvent("Question about diamond sent");
                    // Creating mail Body string with all selected diamonds
                    String mailBody = cont.getResources().getString(R.string.email_body_start);
                    for (int i = 0; i < dac.pickedDiamondsArray.size(); i++) {
                        mailBody = mailBody + "(" + certificateType + ") " + dac.pickedDiamondsArray.get(i) + "\n";
                    }

                    //Loading Send Mail Intent
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.COMPANY_EMAIL});
                    i.putExtra(Intent.EXTRA_SUBJECT, cont.getResources().getString(R.string.email_subject));
                    i.putExtra(Intent.EXTRA_TEXT, mailBody);
                    try {
                        cont.startActivity(Intent.createChooser(i, "Send mail..."));
                        dialog.dismiss();
                    } catch (android.content.ActivityNotFoundException ex) {
                        tc.showCustomToast(3);
                    }
                } else {
                    tc.showCustomToast(4);
                }
            }
        });

        diamondsList.setAdapter(arad);
        dialog.show();
        FlurryAgent.logEvent("Opened Diamond Question Dialog");
    }


    public void showCameraViewDialog() {
        dialog.setContentView(R.layout.camera_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageButton img = (ImageButton) dialog.findViewById(R.id.close_camera);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }



    public void showAdvertDetailsDialog() {
        dialog.setContentView(R.layout.stage_bazar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageButton img = (ImageButton) dialog.findViewById(R.id.close_advert);

        watchModelSection = (LinearLayout) dialog.findViewById(R.id.watch_model_section);
        watchBoxSection = (LinearLayout) dialog.findViewById(R.id.watch_box_section);
        watchDocsSection = (LinearLayout) dialog.findViewById(R.id.watch_docs_section);
        metalSection = (LinearLayout) dialog.findViewById(R.id.metal_section);
        jewlCenterStone = (LinearLayout) dialog.findViewById(R.id.jewl_center_stone_section);
        totalWeightSection = (LinearLayout) dialog.findViewById(R.id.total_weight_section);
        shapeSection = (LinearLayout) dialog.findViewById(R.id.stone_shape_section);
        colorSection = (LinearLayout) dialog.findViewById(R.id.color_section);
        claritySection = (LinearLayout) dialog.findViewById(R.id.clarity_section);
        weightSection = (LinearLayout) dialog.findViewById(R.id.weight_section);
        certifSection = (LinearLayout) dialog.findViewById(R.id.certf_section);
        itemTv = (TextView) dialog.findViewById(R.id.stage_item);
        descTv = (TextView) dialog.findViewById(R.id.bazar_description);
        sellernameTv = (TextView) dialog.findViewById(R.id.bazar_sellername);
        sellphoneTv = (TextView) dialog.findViewById(R.id.bazar_sellerphone);
        sellmailTv = (TextView) dialog.findViewById(R.id.bazar_sellermail);
        skypeTv = (TextView) dialog.findViewById(R.id.bazar_sellerskype);
        dollarsTv = (TextView) dialog.findViewById(R.id.bazar_dollars);
        convertedPriceTv = (TextView) dialog.findViewById(R.id.bazer_conv_price);
        cityTv = (TextView) dialog.findViewById(R.id.bazar_sellercity);
        image1 = (ImageView) dialog.findViewById(R.id.image1);
        image2 = (ImageView) dialog.findViewById(R.id.image2);
        image3 = (ImageView) dialog.findViewById(R.id.image3);

        switch (BazarActivity.collectionType) {
            case "jewls":
                metalTv = (TextView) dialog.findViewById(R.id.bazar_stage_metal);
                centstoneTv = (TextView) dialog.findViewById(R.id.bazar_center_stone);
                shapeTv = (TextView) dialog.findViewById(R.id.bazar_shape);
                weightTv = (TextView) dialog.findViewById(R.id.bazar_weight);
                colorTv = (TextView) dialog.findViewById(R.id.bazar_color);
                clarityTv = (TextView) dialog.findViewById(R.id.bazar_clarity);
                totalTv = (TextView) dialog.findViewById(R.id.bazar_total);
                certifTv = (TextView) dialog.findViewById(R.id.bazar_certificate);
                metalTv.setText(BazarActivity.phone_metal);
                centstoneTv.setText(BazarActivity.phone_centStone);
                shapeTv.setText(BazarActivity.phone_shape);
                weightTv.setText(BazarActivity.phone_weight);
                colorTv.setText(BazarActivity.phone_color);
                clarityTv.setText(BazarActivity.phone_clarity);
                totalTv.setText(BazarActivity.phone_total);
                certifTv.setText(BazarActivity.phone_certif);
                break;
            case "stones":
                shapeTv = (TextView) dialog.findViewById(R.id.bazar_shape);
                weightTv = (TextView) dialog.findViewById(R.id.bazar_weight);
                colorTv = (TextView) dialog.findViewById(R.id.bazar_color);
                clarityTv = (TextView) dialog.findViewById(R.id.bazar_clarity);
                certifTv = (TextView) dialog.findViewById(R.id.bazar_certificate);
                shapeTv.setText(BazarActivity.phone_shape);
                weightTv.setText(BazarActivity.phone_weight);
                colorTv.setText(BazarActivity.phone_color);
                clarityTv.setText(BazarActivity.phone_clarity);
                certifTv.setText(BazarActivity.phone_certif);
                break;
            case "watches":
                metalTv = (TextView) dialog.findViewById(R.id.bazar_stage_metal);
                modelTv = (TextView) dialog.findViewById(R.id.bazar_model);
                boxTv = (TextView) dialog.findViewById(R.id.bazar_box);
                docsTv = (TextView) dialog.findViewById(R.id.bazar_docs);
                metalTv.setText(BazarActivity.phone_metal);
                modelTv.setText(BazarActivity.phone_model);
                boxTv.setText(BazarActivity.phone_box);
                docsTv.setText(BazarActivity.phone_docs);
                break;
        }

        itemTv.setText(BazarActivity.phone_item);
        descTv.setText(BazarActivity.phone_desc);
        sellernameTv.setText(BazarActivity.phone_sellername);
        sellphoneTv.setText(BazarActivity.phone_phone);
        sellmailTv.setText(BazarActivity.phone_mail);
        skypeTv.setText(BazarActivity.phone_skype);
        cityTv.setText(BazarActivity.phone_city);
        dollarsTv.setText(BazarActivity.phone_price);
        convertedPriceTv.setText("(" + Calculations.calculateLocalCurrency(BazarActivity.phone_cprice) + ")");

        int imgWidth, imgHeight;
        if(MainActivity.screenWidth >= 1080){
            imgWidth = 1200;
            imgHeight = 900;
        } else {
            imgWidth = 800;
            imgHeight = 600;
        }

        showHideUnusedFields(BazarActivity.collectionType);
        if (BazarActivity.phone_i1 != null) {
            image1.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(BazarActivity.phone_i1, image1, imgWidth, imgHeight, false);
        } else {
            image1.setVisibility(View.GONE);
        }
        if (BazarActivity.phone_i2 != null) {
            image2.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(BazarActivity.phone_i2, image2, imgWidth, imgHeight, false);
        } else {
            image2.setVisibility(View.GONE);
        }
        if (BazarActivity.phone_i3 != null) {
            image3.setVisibility(View.VISIBLE);
            BitmapManager.INSTANCE.loadBitmap(BazarActivity.phone_i3, image3, imgWidth, imgHeight, false);
        } else {
            image3.setVisibility(View.GONE);
        }

        sellmailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{sellmailTv.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, cont.getResources().getString(R.string.bazar_question_mail_subj));
                i.putExtra(Intent.EXTRA_TEXT, cont.getResources().getString(R.string.bazar_question_mail_body) + "\n" + "------------------------- \n" + descTv.getText().toString() + "\n-------------------------- \n");
                try {
                    cont.startActivity(Intent.createChooser(i, cont.getResources().getString(R.string.bazar_mail_intent_ttl)));
                    FlurryAgent.logEvent("user contacted seller " + sellmailTv.getText().toString());
                } catch (android.content.ActivityNotFoundException ex) {
                    tc.showCustomToast(3);
                }
            }
        });

        sellphoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) cont.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM || telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + sellphoneTv.getText().toString().trim()));
                    cont.startActivity(callIntent);
                    FlurryAgent.logEvent("user called seller " + sellernameTv.getText().toString() + " " + sellphoneTv.getText().toString());
                } else {
                    tc.showCustomToast(8);
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }



    private void showHideUnusedFields(String collection) {
        switch (collection) {
            case "jewls":
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
                break;
            case "stones":
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
                break;
            case "watches":
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
                break;
        }

    }

}
