package main.dbay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by vladvidavsky on 6/04/15.
 */
public class ToastCustom {

    Toast toast;
    Context cont;

    public ToastCustom(Activity activity) {
        cont = activity;
        toast = new Toast(cont);
    }

    public void showCustomToast(int type) {
        toast.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        switch (type) {
            case 1:
                // USER DID NOT PICK ANY DIAMOND FOR PRICE QUOTE
                view = inflater.inflate(R.layout.toast_error_cutom, null);
                break;
            case 2:
                // USER REACHED MAXIMUM ALLOWED DIAMONDS AMOUNT FOR PRICE QUOTE
                view = inflater.inflate(R.layout.toast_max_amount_of_diamonds, null);
                break;
            case 3:
                // THERE ARE NO EMAIL CLIENTS INSTALLED
                view = inflater.inflate(R.layout.toast_no_mail_found, null);
                break;
            case 4:
                // PLEASE CHOOSE A CERTIFICATE DIALOG
                view = inflater.inflate(R.layout.toast_choose_certificate, null);
                break;
            case 5:
                // ITEM SUCCESSFULLY ADDED TO FAVORITES TOAST
                view = inflater.inflate(R.layout.toast_item_added_to_db, null);
                break;
            case 6:
                // ITEM SUCCESSFULLY REMOVED FROM FAVORITES TOAST
                view = inflater.inflate(R.layout.toast_item_removed_from_db, null);
                break;
            case 7:
                // PAYPAL AMOUNT TEXT FIELD IS EMPTY
                view = inflater.inflate(R.layout.toast_paypal_empty_price_field, null);
                break;
            case 8:
                //USER DEVICE DOES NOT HAVE ABILITY TO MAKE CALLS
                view = inflater.inflate(R.layout.toast_cannot_make_calls, null);
                break;
            case 9:
                //NO RESULTS ACCORDING TO USER FILTER REQUEST (BAZAR VIEW)
                view = inflater.inflate(R.layout.toast_no_filtered_results, null);
                break;
        }
        toast.setView(view);
        toast.show();
    }
}
