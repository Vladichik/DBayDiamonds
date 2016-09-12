package main.dbay.combined;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.dbay.R;

/**
 * Created by vladvidavsky on 24/04/15.
 */
public class CombinedFragmentHub extends Fragment {
    private static View combinedHub;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (combinedHub != null) {
            ViewGroup parent = (ViewGroup) combinedHub.getParent();
            if (parent != null)
                parent.removeView(combinedHub);
        }
        try {
            combinedHub = inflater.inflate(R.layout.hub_combined, container, false);
        } catch (InflateException e) {
        }
        return combinedHub;
    }
}
