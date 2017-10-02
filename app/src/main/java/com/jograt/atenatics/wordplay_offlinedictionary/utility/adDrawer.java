package com.jograt.atenatics.wordplay_offlinedictionary.utility;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by John on 2017/10/02.
 */

public class adDrawer {
    public adDrawer(AdView adView, AdRequest adRequest) {
        adView.loadAd(adRequest);
    }
}
