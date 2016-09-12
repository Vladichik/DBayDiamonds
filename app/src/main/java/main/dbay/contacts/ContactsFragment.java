package main.dbay.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import main.dbay.Constants;
import main.dbay.MainActivity;
import main.dbay.R;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 6/05/15.
 */
public class ContactsFragment extends Fragment {
    private ToastCustom tc;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews(); //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        }
        tc = new ToastCustom(getActivity());
        View contactsVeiw = inflater.inflate(R.layout.contacts, container, false);
        if(!MainActivity.iAmOnTablet){
            Button contactUs = (Button) contactsVeiw.findViewById(R.id.contact_us);
            contactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Loading Send Mail Intent
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.COMPANY_EMAIL});
                    i.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources().getString(R.string.hello_dbay));
                    try {
                        getActivity().startActivity(Intent.createChooser(i, getActivity().getResources().getString(R.string.contact_dbay)));
                    } catch (android.content.ActivityNotFoundException ex) {
                        tc.showCustomToast(3);
                    }
                }
            });
        }
        return contactsVeiw;
    }
}
