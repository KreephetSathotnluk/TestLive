package com.mfec.live.common

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object CommonFunction {
    fun msgDialog(
        context: Context,
        msgCode: String,
        hasTwoBtn: Boolean,
        positiveBtnName: String,
        negativeBtnName: String,
        callback: ((String) -> Unit)?
    ) {
        if (hasTwoBtn) {

        } else {

        }
    }

    fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, containerId: Int, tag: String) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .commit()
    }

    fun replaceFragmentWithBackstack(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        containerId: Int,
        tag: String
    ) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }
}