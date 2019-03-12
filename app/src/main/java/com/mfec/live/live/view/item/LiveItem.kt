package com.mfec.live.live.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.mfec.live.R
import com.mfec.live.live.model.LiveModel
import com.mfec.live.live.model.LiveStreamsModel
import kotlinx.android.synthetic.main.live_item.view.*

class LiveItem : LinearLayout, View.OnClickListener {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.live_item, this)
    }

    lateinit var mLiveModel: LiveStreamsModel
    var mCallback: ((LiveStreamsModel) -> Unit)? = null

    fun bindLiveItem(liveModel: LiveStreamsModel, callBack: (LiveStreamsModel) -> Unit) {
        mCallback = callBack
        mLiveModel = liveModel
        initInstance()

    }

    private fun initInstance() {
        name.text = mLiveModel.name
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter()).placeholder(R.drawable.ic_launcher_background)
        Glide.with(this)
            .load(mLiveModel.logo_url)
            .apply(requestOptions)
            .into(imgMulti)
        this.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        mCallback!!.invoke(mLiveModel)
    }

}