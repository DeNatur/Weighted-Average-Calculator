package com.szymonstasik.kalkulatorsredniejwazonej

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.kobakei.ratethisapp.RateThisApp
import com.szymonstasik.kalkulatorsredniejwazonej.utils.Statics


class MainActivity : AppCompatActivity() {
    lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-6726838029317783~2574996217"
        //test ad
       // mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        val config = RateThisApp.Config(1, 3)
        RateThisApp.init(config)
        RateThisApp.onCreate(this)
        RateThisApp.showRateDialogIfNeeded(this)
        mInterstitialAd.adListener = object: AdListener(){
            override fun onAdOpened() {
                getPreferences(Context.MODE_PRIVATE).edit().putInt(Statics.AD_COUNTER, 0).commit()
            }
        }
    }
}
