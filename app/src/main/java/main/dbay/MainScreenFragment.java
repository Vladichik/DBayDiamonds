package main.dbay;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import main.dbay.bazar.BazarActivity;
import main.dbay.bazar.BazarFragmentHub;
import main.dbay.combined.CombinedActivity;
import main.dbay.combined.CombinedFragmentHub;
import main.dbay.contacts.ContactsFragment;
import main.dbay.diamonds.DiamondsActivity;
import main.dbay.earings.EaringsActivity;
import main.dbay.earings.EaringsFragmentHub;
import main.dbay.favorites.FavoritesActivity;
import main.dbay.favorites.FavoritesFragmentHub;
import main.dbay.mainwebview.WelcomeWebViewFragment;
import main.dbay.misc.MiscActivity;
import main.dbay.misc.MiscFragmentHub;
import main.dbay.paypal.PaypalViewFragment;
import main.dbay.rapaport.RapaportActivity;
import main.dbay.rings.FragmentHub;
import main.dbay.rings.RingsActivity;

/**
 * Created by vladvidavsky on 24/03/15.
 */
public class MainScreenFragment extends Fragment implements View.OnClickListener {
    static ImageButton diamondsButton, ringsButton, earingsButton, miscButton, combinedBtn, fastDeliveryBtn, favoritesBtn, bazarBtn, rapaportBtn, contactsBtn, paypalBtn;
    MainActivity mainac;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        mainac = new MainActivity();

        diamondsButton = (ImageButton) v.findViewById(R.id.diamondsButton);
        ringsButton = (ImageButton) v.findViewById(R.id.ringsButton);
        earingsButton = (ImageButton) v.findViewById(R.id.earingsButton);
        miscButton = (ImageButton) v.findViewById(R.id.miscButton);
        rapaportBtn = (ImageButton) v.findViewById(R.id.rapaportBtn);
        bazarBtn = (ImageButton) v.findViewById(R.id.bazarBtn);
        combinedBtn = (ImageButton) v.findViewById(R.id.combinedBtn);
        fastDeliveryBtn = (ImageButton) v.findViewById(R.id.fastBtn);
        favoritesBtn = (ImageButton) v.findViewById(R.id.favoritesBtn);
        contactsBtn = (ImageButton) v.findViewById(R.id.aboutBtn);
        paypalBtn = (ImageButton) v.findViewById(R.id.paypalBtn);
        diamondsButton.setOnClickListener(this);
        ringsButton.setOnClickListener(this);
        earingsButton.setOnClickListener(this);
        miscButton.setOnClickListener(this);
        bazarBtn.setOnClickListener(this);
        rapaportBtn.setOnClickListener(this);
        combinedBtn.setOnClickListener(this);
        fastDeliveryBtn.setOnClickListener(this);
        favoritesBtn.setOnClickListener(this);
        contactsBtn.setOnClickListener(this);
        paypalBtn.setOnClickListener(this);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFavoritesBtnState();
            }
        }, 100);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diamondsButton:
                mainac.fragmentToLoad = new DiamondsActivity();
                LoadFragment();
                FlurryAgent.logEvent("Diamonds List Opened");
                break;
            case R.id.ringsButton:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new FragmentHub();
                } else {
                    mainac.fragmentToLoad = new RingsActivity();
                }
                LoadFragment();
                FlurryAgent.logEvent("Rings Catalog Entered");
                break;
            case R.id.earingsButton:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new EaringsFragmentHub();
                } else {
                    mainac.fragmentToLoad = new EaringsActivity();
                }
                LoadFragment();
                FlurryAgent.logEvent("Earings Catalog Entered");
                break;
            case R.id.miscButton:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new MiscFragmentHub();
                } else {
                    mainac.fragmentToLoad = new MiscActivity();
                }
                LoadFragment();
                FlurryAgent.logEvent("Necklace/Pendants Catalog Entered");
                break;
            case R.id.combinedBtn:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new CombinedFragmentHub();
                } else {
                    mainac.fragmentToLoad = new CombinedActivity();
                }
                LoadFragment();
                FlurryAgent.logEvent("Combined Jewels Catalog Entered");
                break;
            case R.id.favoritesBtn:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new FavoritesFragmentHub();
                } else {
                    mainac.fragmentToLoad = new FavoritesActivity();
                }
                LoadFragment();
                FlurryAgent.logEvent("Favorites List Entered");
                break;
            case R.id.fastBtn:
                mainac.fragmentToLoad = new WelcomeWebViewFragment();
                LoadFragment();
                FlurryAgent.logEvent("Discount Sale View Entered");
                break;
            case R.id.bazarBtn:
                if (mainac.iAmOnTablet) {
                    mainac.fragmentToLoad = new BazarFragmentHub();
                } else {
                    mainac.fragmentToLoad = new BazarActivity();
                }
                LoadFragment();
                break;
            case R.id.rapaportBtn:
                mainac.fragmentToLoad = new RapaportActivity();
                LoadFragment();
                FlurryAgent.logEvent("Rapaport Opened");
                break;
            case R.id.aboutBtn:
                if (mainac.iAmOnTablet) {
                    DialogCustom dialogC = new DialogCustom(getActivity());
                    dialogC.showContactUsDialog();
                    FlurryAgent.logEvent("Contacts Dialog Opened");
                } else {
                    mainac.fragmentToLoad = new ContactsFragment();
                    LoadFragment();
                    FlurryAgent.logEvent("Contacts Fragment Opened");
                }
                break;
            case R.id.paypalBtn:
                mainac.fragmentToLoad = new PaypalViewFragment();
                LoadFragment();
                break;
        }

    }

    private void LoadFragment() {
        FragmentManager fragmentman = getFragmentManager();
        fragmentman.beginTransaction().replace(R.id.data_screen, mainac.fragmentToLoad, "top").addToBackStack(null).commit();
    }


    /**
     * Method that checks if favorites list is empty or not and sets the state accordingly
     */
    public static void setFavoritesBtnState() {
        if (MainActivity.favoritesIds.size() < 1) {
            favoritesBtn.setEnabled(false);
        } else {
            favoritesBtn.setEnabled(true);
        }
    }
}
