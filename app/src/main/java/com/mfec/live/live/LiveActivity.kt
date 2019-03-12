package com.mfec.live.live

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.support.v4.app.ActivityCompat
import android.os.Build
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View.FOCUSABLE_AUTO
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice
import com.wowza.gocoder.sdk.api.devices.WOWZCameraView
import kotlinx.android.synthetic.main.activity_live.*
import com.wowza.gocoder.sdk.api.WowzaGoCoder
import android.widget.Toast
import com.mfec.live.R
import com.mfec.live.live.model.LiveStreamsModel
import com.mfec.live.live.viewmodel.LiveViewModel
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig
import com.wowza.gocoder.sdk.api.devices.WOWZCamera
import com.wowza.gocoder.sdk.api.geometry.WOWZSize
import com.wowza.gocoder.sdk.api.status.WOWZState
import com.wowza.gocoder.sdk.api.status.WOWZStatus
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback
import kotlinx.android.synthetic.main.activity_player.*


class LiveActivity : AppCompatActivity(), WOWZStatusCallback {

    // The top-level GoCoder API interface
    private var goCoder: WowzaGoCoder? = null
    // Properties needed for Android 6+ permissions handling
    private val PERMISSIONS_REQUEST_CODE = 0x1
    private var mPermissionsGranted = true
    private val mRequiredPermissions = arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    // The GoCoder SDK camera view
    private var goCoderCameraView: WOWZCameraView? = null
    // The GoCoder SDK audio device
    private var goCoderAudioDevice: WOWZAudioDevice? = null
    private val liveViewModel by lazy {
        ViewModelProviders.of(this).get(LiveViewModel::class.java)
    }
    private lateinit var listLive: LiveStreamsModel
    // The broadcast configuration settings
    private var goCoderBroadcastConfig: WOWZBroadcastConfig? = null

    private var goCoderBroadcaster: WOWZBroadcast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mfec.live.R.layout.activity_live)

        // Initialize the GoCoder SDK
        goCoder = WowzaGoCoder.init(applicationContext, "GOSK-1946-010C-2578-D88E-F4AE")

        if (goCoder == null) {
            // If initialization failed, retrieve the last error and display it
            val goCoderInitError = WowzaGoCoder.getLastError()
            Toast.makeText(
                this,
                "GoCoder SDK error: " + goCoderInitError.errorDescription,
                Toast.LENGTH_LONG
            ).show()
            return
        }
        btnStartLive.setOnClickListener {
            // return if the user hasn't granted the app the necessary permissions
//            if (!mPermissionsGranted) return

            // Ensure the minimum set of configuration settings have been specified necessary to
            // initiate a broadcast streaming session
            val configValidationError = goCoderBroadcastConfig!!.validateForBroadcast()

            when {
                configValidationError != null -> Toast.makeText(
                    this,
                    configValidationError.errorDescription,
                    Toast.LENGTH_LONG
                ).show()
                goCoderBroadcaster!!.status.isRunning -> // Stop the broadcast that is currently running
                    goCoderBroadcaster!!.endBroadcast(this)
                else -> // Start streaming
                    goCoderBroadcaster!!.startBroadcast(goCoderBroadcastConfig, this)
            }
        }
        btnSwitchCamera.setOnClickListener {
            // Set the active camera to the front camera if it's not active
            if (!goCoderCameraView!!.camera.isFront) {
                goCoderCameraView!!.switchCamera()
            } else {
                goCoderCameraView!!.switchCamera()
            }
        }

        btnTouch.setOnClickListener {
            val activeCamera = goCoderCameraView!!.camera
            if (activeCamera.isTorchOn) {
                activeCamera.isTorchOn = !activeCamera.isTorchOn
                btnTouch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, // Context
                        R.drawable.ic_torch_off // Drawable
                    )
                )
            } else {
                activeCamera.isTorchOn = !activeCamera.isTorchOn
                btnTouch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, // Context
                        R.drawable.ic_torch_on // Drawable
                    )
                )
            }
        }
        mic.setOnClickListener {
            if (goCoderAudioDevice!!.isMuted) {
                goCoderAudioDevice!!.isMuted = false
                mic.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, // Context
                        R.drawable.ic_mic_on // Drawable
                    )
                )
            } else {
                goCoderAudioDevice!!.isMuted = true
                mic.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, // Context
                        R.drawable.ic_mic_off // Drawable
                    )
                )
            }
        }

        initInstance()

    }

    private fun initInstance() {

        intent.getParcelableExtra<LiveStreamsModel>("ListLive")?.let {
            listLive = it
        }

        // Associate the WOWZCameraView defined in the U/I layout with the corresponding class member
        goCoderCameraView = camera_preview as WOWZCameraView

        // Create an audio device instance for capturing and broadcasting audio
        goCoderAudioDevice = WOWZAudioDevice()
        // Create a broadcaster instance
        goCoderBroadcaster = WOWZBroadcast()
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        var mConfig: WOWZMediaConfig = WOWZMediaConfig()
        mConfig.videoFrameSize =
            WOWZSize(width, height)
        // Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = WOWZBroadcastConfig(mConfig)
        goCoderCameraView!!.frameSize =
            WOWZSize(width, height)
        // Set the connection properties for the target Wowza Streaming Engine server or Wowza Streaming Cloud live stream
        goCoderBroadcastConfig!!.hostAddress = listLive.source_connection_information.primary_server
        goCoderBroadcastConfig!!.portNumber = listLive.source_connection_information.host_port
        goCoderBroadcastConfig!!.applicationName = listLive.source_connection_information.application
        goCoderBroadcastConfig!!.streamName = listLive.source_connection_information.stream_name
        goCoderCameraView!!.setCameraConfig(goCoderBroadcastConfig)
        
        mConfig.videoFrameSize =
            WOWZSize(goCoderBroadcastConfig!!.videoFrameWidth, goCoderBroadcastConfig!!.videoFrameHeight)
        // Designate the camera preview as the video source
        goCoderBroadcastConfig!!.videoBroadcaster = goCoderCameraView
//        goCoderBroadcastConfig!!.videoSourceConfig = mConfig
        // Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig!!.audioBroadcaster = goCoderAudioDevice

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

    //
    // Called when an activity is brought to the foreground
    //
    override fun onResume() {
        super.onResume()

        // If running on Android 6 (Marshmallow) and later, check to see if the necessary permissions
        // have been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions)
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE)
        } else
            mPermissionsGranted = true

        // Start the camera preview display
//        goCoderCameraView!!.focusable = FOCUSABLE_AUTO
        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView!!.isPreviewPaused)
                goCoderCameraView!!.onResume()
            else
                Handler().postDelayed({
                    goCoderCameraView!!.startPreview()
                    goCoderCameraView!!.camera.focusMode = WOWZCamera.FOCUS_MODE_CONTINUOUS
                }, 200)
        }

    }

    override fun onPause() {
        super.onPause()
        goCoderCameraView!!.onPause()
    }

    //
    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
    // the results of the permissions request
    //
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mPermissionsGranted = true
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                // Check the result of each permission granted
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false
                    }
                }
            }
        }
    }

    //
    // Utility method to check the status of a permissions request for an array of permission identifiers
    //
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions)
            if (context.checkCallingOrSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED)
                return false

        return true
    }

    //
    // The callback invoked upon changes to the state of the broadcast
    //
    override fun onWZStatus(goCoderStatus: WOWZStatus) {
        // A successful status transition has been reported by the GoCoder SDK
        val statusMessage = StringBuffer("Broadcast status: ")

        when (goCoderStatus.state) {
            WOWZState.STARTING -> statusMessage.append("Broadcast initialization")

            WOWZState.READY -> statusMessage.append("Ready to begin streaming")

            WOWZState.RUNNING -> statusMessage.append("Streaming is active")

            WOWZState.STOPPING -> statusMessage.append("Broadcast shutting down")

            WOWZState.IDLE -> statusMessage.append("The broadcast is stopped")

            else -> return
        }

        // Display the status message using the U/I thread
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                this@LiveActivity,
                statusMessage,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    //
    // The callback invoked when an error occurs during a broadcast
    //
    override fun onWZError(goCoderStatus: WOWZStatus) {
        // If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(
                this@LiveActivity,
                "Streaming error: " + goCoderStatus.lastError.errorDescription,
                Toast.LENGTH_LONG
            ).show()
        })
    }

}
