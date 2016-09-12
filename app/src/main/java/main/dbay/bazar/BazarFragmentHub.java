package main.dbay.bazar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.dbay.R;

/**
 * Created by vladvidavsky on 9/15/15.
 */
public class BazarFragmentHub extends Fragment {
    private static View bazarHub;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (bazarHub != null) {
            ViewGroup parent = (ViewGroup) bazarHub.getParent();
            if (parent != null)
                parent.removeView(bazarHub);
        }
        try {
            bazarHub = inflater.inflate(R.layout.fragments_hub_bazar, container, false);
        } catch (InflateException e) {
        }
        return bazarHub;
    }
}
