package com.example.traveler

import android.os.Parcel
import android.os.Parcelable

data class Car(
    val name: String,
    val imageResource: Int,
    val passengerCapacity: Int,
    val baggageCapacity: Int,
    val numberOfDoors: Int,
    val price: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(imageResource)
        parcel.writeInt(passengerCapacity)
        parcel.writeInt(baggageCapacity)
        parcel.writeInt(numberOfDoors)
        parcel.writeString(price)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car = Car(parcel)
        override fun newArray(size: Int): Array<Car?> = arrayOfNulls(size)
    }
}
