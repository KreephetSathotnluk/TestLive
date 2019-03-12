package com.mfec.live.live.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

data class LiveStreamsModel(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("logo_url") val logo_url: String?,
    @SerializedName("source_connection_information") val source_connection_information: SourceModel,
    @SerializedName("created_at") val created_at: Long?,
    @SerializedName("updated_at") val updated_at: Long?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(SourceModel::class.java.classLoader),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(logo_url)
        parcel.writeParcelable(source_connection_information, flags)
        parcel.writeValue(created_at)
        parcel.writeValue(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveStreamsModel> {
        override fun createFromParcel(parcel: Parcel): LiveStreamsModel {
            return LiveStreamsModel(parcel)
        }

        override fun newArray(size: Int): Array<LiveStreamsModel?> {
            return arrayOfNulls(size)
        }
    }

}