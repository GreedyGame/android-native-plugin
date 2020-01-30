package com.greedygame.androidsample

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.greedygame.android.agent.GreedyGameAgent

import com.greedygame.android.core.campaign.CampaignStateListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CampaignStateListener {
    var greedyGameAgent:GreedyGameAgent?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //GreedyGameAgent can be initiated with an Activity or a Context.
        greedyGameAgent = GreedyGameAgent.Builder(this as Context)
                .setGameId("66081223")
                .addUnitId("float-4343")
                .enableAdmob(true)
                .withAgentListener(this)
                .build()

        showAd.setOnClickListener {
            greedyGameAgent?.init()
        }
    }

    override fun onStart() {
        super.onStart()
        greedyGameAgent?.let {
            if (it.isCampaignAvailable){
                nativead.loadAd("float-4343")
            }
        }

    }

    override fun onAvailable(campaignId: String) {
        Snackbar.make(findViewById(android.R.id.content), "Ad Available", Snackbar.LENGTH_SHORT).show()
        //loadAd is an extension function on ImageView
        nativead.loadAd("float-4343")
    }

    override fun onError(message: String) {
        Snackbar.make(findViewById(android.R.id.content), "Ad error $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onUnavailable() {
        Snackbar.make(findViewById(android.R.id.content), "Ad Unavailable", Snackbar.LENGTH_SHORT).show()
    }

    fun ImageView.loadAd(unitId:String){
        val adPath = greedyGameAgent?.getPath(unitId)?:""
        if(adPath.isNotEmpty()){
            val bitmap = BitmapFactory.decodeFile(adPath)
            this.setImageBitmap(bitmap)
            this.setOnClickListener {
                greedyGameAgent?.showUII(unitId)
            }
        }
    }
}
