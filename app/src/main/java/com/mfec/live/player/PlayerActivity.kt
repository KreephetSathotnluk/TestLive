package com.mfec.live.player

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mfec.live.R
import com.mfec.live.live.model.LiveStreamsModel
import com.wowza.gocoder.sdk.api.WowzaGoCoder
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig
import com.wowza.gocoder.sdk.api.geometry.WOWZSize
import com.wowza.gocoder.sdk.api.player.WOWZPlayerConfig
import com.wowza.gocoder.sdk.api.status.WOWZState
import com.wowza.gocoder.sdk.api.status.WOWZStatus
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(), WOWZStatusCallback {

    //The top-level GoCoder API interface
    private var goCoder: WowzaGoCoder? = null
    private lateinit var listLive: LiveStreamsModel
    override fun onWZStatus(goCoderStatus: WOWZStatus?) {
        val statusMessage = StringBuffer("Broadcast status: ")

        when (goCoderStatus!!.state) {
            WOWZState.STARTING -> {
//                Handler().postDelayed({
                runOnUiThread {
                    tx1.text = "เริ่มทำการเชื่อมต่อ"
                }
//                }, 200)
            }

            WOWZState.READY -> {
//                Handler().postDelayed({
                runOnUiThread {
                    tx1.text = "กำลังเชื่อมต่อ"
                }
//                }, 200)
            }

            WOWZState.RUNNING -> {
//                Handler().postDelayed({
                runOnUiThread {
                    tx1.text = "เชื่อมต่อสำเร็จ"
                }
//                }, 200)
            }

            WOWZState.STOPPING -> {
//                Handler().postDelayed({
                runOnUiThread {
                    tx1.text = "หยุด"
                }
//                }, 200)
            }

            WOWZState.IDLE -> {
//                Handler().postDelayed({
                runOnUiThread {
                    tx1.text = "ยังไม่มี Live นะจ้ะ"
                }
//                }, 200)
            }

            else -> return
        }
        // Display the status message using the U/I thread
//        Handler(Looper.getMainLooper()).post(Runnable {
//            Toast.makeText(
//                this@PlayerActivity,
//                statusMessage,
//                Toast.LENGTH_LONG
//            ).show()
//
//        })
    }

    override fun onWZError(p0: WOWZStatus?) {
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                this@PlayerActivity,
                "Streaming error: " + p0!!.lastError.errorDescription,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        //Initialize the GoCoder SDK
        goCoder = WowzaGoCoder.init(applicationContext, "GOSK-1946-010C-2578-D88E-F4AE")

        if (goCoder == null) {
            //If initialization failed, retrieve the last error and display it
            val goCoderInitError = WowzaGoCoder.getLastError()
            Toast.makeText(
                this,
                "GoCoder SDK error: " + goCoderInitError.errorDescription,
                Toast.LENGTH_LONG
            ).show()
        }
        initInstance()


    }

    //
    // Enable Android's immersive, sticky full-screen mode
    //
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        if (rootView != null)
            rootView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun initInstance() {
        intent.getParcelableExtra<LiveStreamsModel>("ListLive")?.let {
            listLive = it
        }
        val mStreamPlayerView = vwStreamPlayer
        var mStreamPlayerConfig = WOWZPlayerConfig()
        val goCoderStatus: WOWZStatus? = null

        mStreamPlayerConfig.isPlayback = false
        mStreamPlayerConfig.hostAddress = listLive.source_connection_information.primary_server
        mStreamPlayerConfig.applicationName = listLive.source_connection_information.application
        mStreamPlayerConfig.streamName = listLive.source_connection_information.stream_name
        mStreamPlayerConfig.portNumber = listLive.source_connection_information.host_port
        mStreamPlayerConfig.isHLSEnabled = false
        mStreamPlayerConfig.isAudioEnabled = true
        mStreamPlayerConfig.isVideoEnabled = true
//        mStreamPlayerConfig.videoFrameSize = WOWZSize(720,1208)
//        mStreamPlayerView.streamConfig.videoFrameWidth = 720
//        mStreamPlayerView.streamConfig.videoFrameWidth = 1208
        mStreamPlayerView.volume = 3
//         WOWZMediaConfig.FILL_VIEW : WOWZMediaConfig.RESIZE_TO_ASPECT;
//        mStreamPlayerView.scaleMode = WOWZMediaConfig.FILL_VIEW
        Handler().postDelayed({
            mStreamPlayerView.play(mStreamPlayerConfig, this)
        }, 200)
//        tx.setOnClickListener {
//            Handler().postDelayed({
//                mStreamPlayerView.play(mStreamPlayerConfig, this)
//            }, 200)
//        }
    }
}
