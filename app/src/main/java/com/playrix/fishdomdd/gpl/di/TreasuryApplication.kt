package com.playrix.fishdomdd.gpl.di

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.onesignal.OneSignal
import com.playrix.fishdomdd.gpl.R
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TreasuryApplication : Application() {
    @Inject
    lateinit var afLiveData: MutableLiveData<MutableMap<String, Any>>

    override fun onCreate() {
        super.onCreate()

        AppsFlyerLib.getInstance()
            .init(resources.getString(R.string.apps_dev_key), afListener, this)
        AppsFlyerLib.getInstance().start(this)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(resources.getString(R.string.one_signal_key))
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
    }

    private val afListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
            data?.let {
                afLiveData.postValue(it)
            }
        }

        override fun onConversionDataFail(error: String?) {}

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}

        override fun onAttributionFailure(error: String?) {}

    }
}