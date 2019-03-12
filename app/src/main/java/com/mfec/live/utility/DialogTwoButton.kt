package com.mfec.live.utility

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARGS_MSG = "message"
private const val ARGS_POSITIVE = "positive"
private const val ARGS_NEGATIVE = "negative"
private const val ARGS_POSTIVE_COLOR = "positiveColor"
private const val ARGS_DRAWABLE = "drawable"

class DialogTwoButton : DialogFragment() {

    interface OnDialogListener {
        fun onPositiveButtonClick()

        fun onNegativeButtonClick()
    }

    private var message: String = ""
    private var positive: String = ""
    private var negative: String = ""
    private var isChangePositive: Boolean = false
    private var onDialogListener: OnDialogListener? = null

    companion object {
        fun newInstance(message: String, positiveBtn: String, negativeBtn: String) =
            DialogTwoButton().apply {
                arguments = Bundle().apply {
                    putString(ARGS_MSG, message)
                    putString(ARGS_POSITIVE, positiveBtn)
                    putString(ARGS_NEGATIVE, negativeBtn)
                }
            }

        fun newInstance(message: String, positiveBtn: String, negativeBtn: String, isChangePositive: Boolean) =
            DialogTwoButton().apply {
                arguments = Bundle().apply {
                    putString(ARGS_MSG, message)
                    putString(ARGS_POSITIVE, positiveBtn)
                    putString(ARGS_NEGATIVE, negativeBtn)
                    putBoolean(ARGS_POSTIVE_COLOR, isChangePositive)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){

        }else{

        }
    }

    private fun restoreInstanceState(bundle: Bundle){
        message = bundle.getString(ARGS_MSG)
        positive = bundle.getString(ARGS_POSITIVE)
        negative = bundle.getString(ARGS_NEGATIVE)
        isChangePositive = bundle.getBoolean(ARGS_POSTIVE_COLOR, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}