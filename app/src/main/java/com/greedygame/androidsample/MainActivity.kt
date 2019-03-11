package com.greedygame.androidsample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdListener

import com.greedygame.android.agent.AdListener


import com.greedygame.android.agent.GreedyGameAds
import com.greedygame.android.core.campaign.CampaignStateListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val greedyGameAgent = GreedyGameAds.Builder(this)
                .appId("00000010")
                .addUnitId("slot-1000")
                .addUnitId("slot-1001")
                .enableAdmob(true)
                .withAdListener(this)
                .build()

        showAd.setOnClickListener {
            greedyGameAgent?.load()
        }
    }

    override fun onAvailable(campaignId: String) {
        Snackbar.make(findViewById(android.R.id.content), "Ad Available", Snackbar.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Snackbar.make(findViewById(android.R.id.content), "Ad error $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onUnavailable() {
        Snackbar.make(findViewById(android.R.id.content), "Ad Unavailable", Snackbar.LENGTH_SHORT).show()
    }
}
