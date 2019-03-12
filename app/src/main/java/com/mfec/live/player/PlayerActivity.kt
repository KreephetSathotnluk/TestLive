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
                statusMessage.append("Broadcast initialization")
            }

            WOWZState.READY -> statusMessage.append("Ready to begin streaming")

            WOWZState.RUNNING -> statusMessage.append("Streaming is active")

            WOWZState.STOPPING -> {
                statusMessage.append("Broadcast shutting down")
            }

            WOWZState.IDLE -> statusMessage.append("The broadcast is stopped")

            else -> return
        }
//        when (goCoderStatus!!.state) {
//            WOWZState.STARTING -> {
//                Log.wtf("ggwprr", statusMessage.toString())
//                this@PlayerActivity.tx.visibility = View.VISIBLE
//                this@PlayerActivity.tx.text = "กำลังเชื่อมต่อ"
//            }
//            WOWZState.READY -> {
//
//            }
//            WOWZState.RUNNING -> {
//                this@PlayerActivity.tx.visibility = View.VISIBLE
//                this@PlayerActivity.tx.text = "เชื่อมต่อสำเร็จ"
//                this@PlayerActivity.tx.visibility = View.GONE
//            }
//            WOWZState.STOPPING -> {
//                this@PlayerActivity.tx.visibility = View.VISIBLE
//                this@PlayerActivity.tx.text = "หยุด live"
//            }
//            WOWZState.IDLE -> {
//                this@PlayerActivity.tx.visibility = View.VISIBLE
//                this@PlayerActivity.tx.text = "ยังไม่มี live"
//            }
//        }
        // Display the status message using the U/I thread
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                this@PlayerActivity,
                statusMessage,
                Toast.LENGTH_LONG
            ).show()

        })
        // A successful status transition has been reported by the GoCoder SDK
//        var statusMessage = 0
//        Handler(Looper.getMainLooper()).post(Runnable {
//            when (goCoderStatus!!.state) {
//                WOWZState.STARTING -> {
//                    Log.wtf("ggwprr", statusMessage.toString())
//                    this@PlayerActivity.tx.visibility = View.VISIBLE
//                    this@PlayerActivity.tx.text = "กำลังเชื่อมต่อ"
//                }
//                WOWZState.READY  -> {
//
//                }
//                WOWZState.RUNNING -> {
//                    this@PlayerActivity.tx.visibility = View.VISIBLE
//                    this@PlayerActivity.tx.text = "เชื่อมต่อสำเร็จ"
//                    this@PlayerActivity.tx.visibility = View.GONE
//                }
//                WOWZState.STOPPING -> {
//                    this@PlayerActivity.tx.visibility = View.VISIBLE
//                    this@PlayerActivity.tx.text = "หยุด live"
//                }
//                WOWZState.IDLE -> {
//                    this@PlayerActivity.tx.visibility = View.VISIBLE
//                    this@PlayerActivity.tx.text = "ยังไม่มี live"
//                }
//            }
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
//        mStreamPlayerView.scaleMode = WOWZPlayerConfig.FILL_VIEW
        mStreamPlayerView.volume = 3
//         WOWZMediaConfig.FILL_VIEW : WOWZMediaConfig.RESIZE_TO_ASPECT;
        mStreamPlayerView.scaleMode = WOWZMediaConfig.DEFAULT_SCALE_MODE
//        Handler().postDelayed({
//            mStreamPlayerView.play(mStreamPlayerConfig, this)
//        }, 200)
        tx.setOnClickListener {
            Handler().postDelayed({
                mStreamPlayerView.play(mStreamPlayerConfig, this)
            }, 200)
        }
    }
}
