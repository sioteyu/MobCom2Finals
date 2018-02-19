package com.jograt.atenatics.wordplay_offlinedictionary.utility;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by John on 2017/10/02.
 */

public class adDrawer {
    public adDrawer(AdView adView, AdRequest adRequest, Context context) {
        adView.loadAd(adRequest);
    }
}
