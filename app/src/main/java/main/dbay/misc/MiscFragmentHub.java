package main.dbay.misc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.dbay.R;

/**
 * Created by vladvidavsky on 11/04/15.
 */
public class MiscFragmentHub extends Fragment {
    private static View miscHub;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        } //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (miscHub != null) {
            ViewGroup parent = (ViewGroup) miscHub.getParent();
            if (parent != null)
                parent.removeView(miscHub);
        }
        try {
            miscHub = inflater.inflate(R.layout.hub_misc, container, false);
        } catch (InflateException e) {
        }
        return miscHub;

    }

}
