package main.dbay.earings;

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
public class EaringsFragmentHub extends Fragment{
    private static View earingsHub;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (earingsHub != null) {
            ViewGroup parent = (ViewGroup) earingsHub.getParent();
            if (parent != null)
                parent.removeView(earingsHub);
        }
        try {
            earingsHub = inflater.inflate(R.layout.earings_hub, container, false);
        } catch (InflateException e) {
        }
        return earingsHub;
    }

}
