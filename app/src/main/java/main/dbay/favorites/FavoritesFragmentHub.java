package main.dbay.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import main.dbay.R;

/**
 * Created by vladvidavsky on 26/05/15.
 */
public class FavoritesFragmentHub extends Fragment {
    private static View favoritesFragHub;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS

        if (favoritesFragHub != null) {
            ViewGroup parent = (ViewGroup) favoritesFragHub.getParent();
            if (parent != null)
                parent.removeView(favoritesFragHub);
        }
        try {
            favoritesFragHub = inflater.inflate(R.layout.fragments_hub_favorites, container, false);
        } catch (InflateException e) {
        }
        return favoritesFragHub;
    }
}
