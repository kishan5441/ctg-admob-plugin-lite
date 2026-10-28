package com.yourname.admobplugin // Apna package name daalein

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

class AdMobPlugin(godot: Godot) : GodotPlugin(godot) {

    private var adView: AdView? = null
    private var interstitialAd: InterstitialAd? = null

    override fun getPluginName(): String = "AdMobPlugin"

    // 1. SDK Initialize karne ke liye (GDScript se call hoga)
    @UsedByGodot
    fun initialize(appId: String) {
        val activity: Activity = activity!!
        MobileAds.initialize(activity, appId)
    }

    // 2. Banner Ad dikhane ke liye
    @UsedByGodot
    fun showBanner(adUnitId: String) {
        val activity: Activity = activity!!
        activity.runOnUiThread {
            adView = AdView(activity).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
            }
            // Note: Banner ko Godot view hierarchy mein add karne ka logic yahan aayega
        }
    }

    // 3. Interstitial Ad load karne ke liye
    @UsedByGodot
    fun loadInterstitial(adUnitId: String) {
        val activity: Activity = activity!!
        InterstitialAd.load(activity, adUnitId, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    // 4. Loaded Interstitial Ad dikhane ke liye
    @UsedByGodot
    fun showInterstitial() {
        val activity: Activity = activity!!
        activity.runOnUiThread {
            interstitialAd?.show(activity)
        }
    }
}
