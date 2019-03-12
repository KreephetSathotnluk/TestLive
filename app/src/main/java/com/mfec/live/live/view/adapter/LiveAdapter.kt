package com.mfec.live.live.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mfec.live.R
import com.mfec.live.live.model.LiveStreamsModel
import com.mfec.live.live.view.viewholder.LiveListViewHolder

class LiveAdapter(private val callBack: (LiveStreamsModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var liveList = mutableListOf<LiveStreamsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.live_list_viewholder, parent, false)
        return LiveListViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return liveList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LiveListViewHolder -> {
                holder.liveListItem.bindLiveItem(liveList[position],callBack)
            }
        }
    }

    fun setLiveList(liveList: MutableList<LiveStreamsModel>) {
        this.liveList = liveList
        notifyDataSetChanged()
    }

}