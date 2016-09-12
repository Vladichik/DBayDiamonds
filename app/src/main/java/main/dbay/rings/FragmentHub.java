package main.dbay.rings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.dbay.R;

/**
 * Created by vladvidavsky on 8/04/15.
 */
public class FragmentHub extends Fragment {
    private static View fragmentsHub;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (fragmentsHub != null) {
            ViewGroup parent = (ViewGroup) fragmentsHub.getParent();
            if (parent != null)
                parent.removeView(fragmentsHub);
        }
        try {
            fragmentsHub = inflater.inflate(R.layout.fragments_hub_rings, container, false);
        } catch (InflateException e) {
        }
        return fragmentsHub;
    }
}
