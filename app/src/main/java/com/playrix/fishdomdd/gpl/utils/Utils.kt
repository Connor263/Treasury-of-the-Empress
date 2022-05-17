package com.playrix.fishdomdd.gpl.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat

fun treIsInternetAvailable(context: Context): Boolean {
    val treConnectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)
    val treActiveNetwork = treConnectivityManager?.activeNetwork ?: return false
    val treNetworkCapabilities =
        treConnectivityManager.getNetworkCapabilities(treActiveNetwork) ?: return false
    return when {
        treNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        treNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        treNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun String.vigenere(encrypt: Boolean = false): String {
    val sb = StringBuilder()
    val afdadv = "complayrixfishdomddgpl"
    var dakfwp = 0

    this.forEach {
        if (it !in 'a'..'z') {
            sb.append(it)
            return@forEach
        }
        val wlfgflga = if (encrypt) {
            (it.code + afdadv[dakfwp].code - 90) % 26
        } else {
            (it.code - afdadv[dakfwp].code + 26) % 26
        }
        dakfwp++
        if (dakfwp > afdadv.length - 1) dakfwp = 0
        sb.append(wlfgflga.plus(97).toChar())
    }
    return sb.toString()
}