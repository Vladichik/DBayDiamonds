package main.dbay.rapaport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flurry.android.FlurryAgent;

import main.dbay.Constants;
import main.dbay.R;

/**
 * Created by vladvidavsky on 1/04/15.
 */
public class RapaportActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container != null){container.removeAllViews();} //CLEARS PREVIOUS FRAGMENT IF IT EXISTS
        View rapView = inflater.inflate(R.layout.rapaport, container, false);

        final ProgressBar preloader = (ProgressBar) rapView.findViewById(R.id.progressBar);
        WebView rapaportView = (WebView) rapView.findViewById(R.id.rapaport_web);
        String rapaportPDFUrl = Constants.WEBSITE_URL + "/view/mainpage/rapaport.pdf";
        String fullURL = "http://docs.google.com/gview?embedded=true&url=" + rapaportPDFUrl;
        rapaportView.getSettings().setJavaScriptEnabled(true);
        rapaportView.loadUrl(fullURL);

        rapaportView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                preloader.setVisibility(View.GONE);
            }
        });

        return rapView;
    }
}
