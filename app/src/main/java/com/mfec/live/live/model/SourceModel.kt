package com.mfec.live.live.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SourceModel(
    @SerializedName("primary_server") val primary_server: String?,
    @SerializedName("host_port") val host_port: Int,
    @SerializedName("application") val application: String?,
    @SerializedName("stream_name") val stream_name: String?,
    @SerializedName("disable_authentication") val disable_authentication: Boolean,
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(primary_server)
        parcel.writeInt(host_port)
        parcel.writeString(application)
        parcel.writeString(stream_name)
        parcel.writeByte(if (disable_authentication) 1 else 0)
        parcel.writeString(username)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourceModel> {
        override fun createFromParcel(parcel: Parcel): SourceModel {
            return SourceModel(parcel)
        }

        override fun newArray(size: Int): Array<SourceModel?> {
            return arrayOfNulls(size)
        }
    }

}