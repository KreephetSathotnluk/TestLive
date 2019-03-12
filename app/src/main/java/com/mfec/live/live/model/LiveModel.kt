package com.mfec.live.live.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Suppress("UNREACHABLE_CODE")
data class LiveModel(
    @SerializedName("content") val content: MutableList<LiveStreamsModel>,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("isLast") val isLast: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(LiveStreamsModel),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(content)
        parcel.writeInt(currentPage)
        parcel.writeInt(totalPage)
        parcel.writeByte(if (isLast) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveModel> {
        override fun createFromParcel(parcel: Parcel): LiveModel {
            return LiveModel(parcel)
        }

        override fun newArray(size: Int): Array<LiveModel?> {
            return arrayOfNulls(size)
        }
    }
}