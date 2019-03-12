package com.mfec.live.live.view.fragmemt

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.mfec.live.R
import com.mfec.live.common.Constant
import com.mfec.live.live.LiveActivity
import com.mfec.live.live.view.adapter.LiveAdapter
import com.mfec.live.live.viewmodel.LiveViewModel
import com.mfec.live.player.PlayerActivity
import kotlinx.android.synthetic.main.fragment_live.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LiveFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LiveFragment : Fragment() {

    private var isFrom: String = ""

    companion object {
        @JvmStatic
        fun newInstance(isForm: String): LiveFragment {
            val bundle = Bundle()
            bundle.putString(Constant.FRAGMENT_TAG.IS_FORM, isForm)
            val fragment = LiveFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val liveViewModel by lazy {
        ViewModelProviders.of(this).get(LiveViewModel::class.java)
    }

    private val liveAdapter by lazy {
        LiveAdapter {
            if (isFrom == Constant.FRAGMENT_TAG.LIVE) {
                val i = Intent(this.context!!, LiveActivity::class.java)
                i.putExtra("ListLive", it)
                startActivity(i)
            } else {
                val i = Intent(this.context!!, PlayerActivity::class.java)
                i.putExtra("ListLive", it)
                startActivity(i)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!arguments!!.isEmpty) {
            isFrom = arguments!!.getString(Constant.FRAGMENT_TAG.IS_FORM)
        }
        Log.wtf("isForm", isFrom)
        loadListLive()
        initRecyclerView()
    }

    private fun loadListLive() {
        liveViewModel.getListWowza(this.context!!).observe(this, Observer {
            it?.let { liveModel ->
                liveAdapter.setLiveList(liveModel.content)
            }
        })
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        recyclerLive.layoutManager = linearLayoutManager
        recyclerLive.adapter = liveAdapter
    }

}
