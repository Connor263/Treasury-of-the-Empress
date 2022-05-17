package com.playrix.fishdomdd.gpl.ui.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.playrix.fishdomdd.gpl.databinding.FragmentTreWebBinding
import com.playrix.fishdomdd.gpl.utils.vigenere
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TreWebFragment : Fragment() {
    private var _binding: FragmentTreWebBinding? = null
    private val binding get() = _binding!!

    private var treFileData: ValueCallback<Uri>? = null
    private var treFilePath: ValueCallback<Array<Uri>>? = null
    private val treStartFileChooseForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                treProcessFileChooseResult(result.data)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTreWebBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.webView.apply {
                    if (canGoBack() && isFocused) goBack()
                }
            }
        })

        val treNavArgs: TreWebFragmentArgs by navArgs()
        treLoadWebView(treNavArgs.link)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    private fun treLoadWebView(link: String) = with(binding.webView) {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                url?.let {
                    if (it.contains("gfddc=angicfa3x".vigenere()) || it.contains("fwepmlcu.pqrt")) {
                        val action = TreWebFragmentDirections.actionGlobalMenuFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                treFilePath = filePathCallback
                Intent(Intent.ACTION_GET_CONTENT).run {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "kamvp/*".vigenere()
                    treStartFileChooseForResult.launch(this)
                }
                return true
            }
        }
        settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(false)
            builtInZoomControls = true
            useWideViewPort = true
            setAppCacheEnabled(true)
            displayZoomControls = false
            allowFileAccess = true
            lightTouchEnabled = true
        }
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(this@with, true)
        }
        clearCache(false)

        loadUrl(link)
        Log.d("TAG", "treLoadWebView: $link")
    }

    private fun treProcessFileChooseResult(data: Intent?) {
        if (treFileData == null && treFilePath == null) return

        var treResultFileData: Uri? = null
        var treResultsFilePath: Array<Uri>? = null
        data?.let {
            treResultFileData = data.data
            treResultsFilePath = arrayOf(Uri.parse(data.dataString))
        }
        treFileData?.onReceiveValue(treResultFileData)
        treFilePath?.onReceiveValue(treResultsFilePath)
    }
}