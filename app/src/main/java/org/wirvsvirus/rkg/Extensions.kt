package org.wirvsvirus.rkg

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

fun Context.getPrefs(): SharedPreferences =
    getSharedPreferences("app", Context.MODE_PRIVATE)

@SuppressLint("ApplySharedPref")
fun SharedPreferences.putVendorId(vendorId: String) {
    edit().putString("vendorId", vendorId).commit()
}

fun SharedPreferences.getVendorId(): String? =
    getString("vendorId", null)

@SuppressLint("ApplySharedPref")
fun SharedPreferences.putVendorEmail(vendorEmail: String) {
    edit().putString("vendorEmail", vendorEmail).commit()
}

fun SharedPreferences.getVendorEmail(): String? =
    getString("vendorEmail", null)

@SuppressLint("ApplySharedPref")
fun SharedPreferences.putStoreId(storeId: String) {
    edit().putString("storeId", storeId).commit()
}

fun SharedPreferences.getStoreId(): String? =
    getString("storeId", null)