package com.playrix.fishdomdd.gpl.data.web.model

import android.content.Context
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.utils.vigenere


data class TreLink(
    var treGoogleId: String? = null,
    var treAppsFlyerUserId: String? = null,
    var treSubAll: List<String> = listOf("", "", "", "", "", "", "", "", "", ""),
    var treDeepLink: String? = null,
    var treMediaSource: String? = null,
    var treAfChannel: String? = null,
    var treCampaign: String? = null,
    var treUrl: String? = null,
    var treOrganicAccess: Boolean? = null
) {
    fun treCollect(context: Context): String {
        val treResources = context.resources
        val trePackageName = context.packageName
        val treAppsFlyerDevKey = treResources.getString(R.string.apps_dev_key)
        val treFbToken = treResources.getString(R.string.fb_at)
        val treFbAppId = treResources.getString(R.string.fb_app_id)

        var treIndex = 0
        val treSubsString = treSubAll.joinToString(separator = "") {
            treIndex++
            "&sub$treIndex=$it"
        }

        val treStrMediaSource = "?ospxl_smlzzj=".vigenere()
        val treStrGoogleId = "&icavwe_yuqa=".vigenere()
        val treStrAppsFlyerUserId = "&ct_ghprgu=".vigenere()
        val treStrPackageName = "&dizswe=".vigenere()
        val treStrAppsFlyerDevKey = "&fsh_zpy=".vigenere()
        val treStrFbToken = "&hp_mi=".vigenere()
        val treStrFbAppId = "&hp_mea_ib=".vigenere()
        val treStrAfChannel = "&ct_owlnlvt=".vigenere()
        val treStrCampaign = "&eoyeliee=".vigenere()

        return "${this.treUrl}" +
                "$treStrMediaSource${this.treMediaSource}" +
                "$treStrGoogleId${this.treGoogleId}" +
                "$treStrAppsFlyerUserId${this.treAppsFlyerUserId}" +
                "$treStrPackageName$trePackageName" +
                "$treStrAppsFlyerDevKey$treAppsFlyerDevKey" +
                "$treStrFbToken$treFbToken" +
                "$treStrFbAppId$treFbAppId" +
                "$treStrAfChannel${this.treAfChannel}" +
                "$treStrCampaign${this.treCampaign}" +
                treSubsString
    }
}











