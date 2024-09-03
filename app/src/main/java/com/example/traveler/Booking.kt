package com.example.traveler
import android.os.Parcel
import android.os.Parcelable

data class Booking(
    val bookingId: Int,
    val vehicleName: String,
    val days: Int,
    val totalAmount: String,
    val bookingTime: String,
    val returned: Boolean,
    val canceled: Boolean,
    val cancellationCharge: Double,

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookingId)
        parcel.writeString(vehicleName)
        parcel.writeInt(days)
        parcel.writeString(totalAmount)
        parcel.writeString(bookingTime)
        parcel.writeByte(if (returned) 1 else 0)
        parcel.writeByte(if (canceled) 1 else 0)
        parcel.writeDouble(cancellationCharge)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking> {
        override fun createFromParcel(parcel: Parcel): Booking {
            return Booking(parcel)
        }

        override fun newArray(size: Int): Array<Booking?> {
            return arrayOfNulls(size)
        }
    }
}
