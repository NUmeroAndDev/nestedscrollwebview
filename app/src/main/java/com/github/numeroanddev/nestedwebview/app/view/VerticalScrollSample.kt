package com.github.numeroanddev.nestedwebview.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.compose.AndroidFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.numeroanddev.nestedwebview.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val viewVerticalScrollSampleRoute = "viewVerticalScrollSample"

fun NavController.navigateToViewVerticalScrollSample(navOptions: NavOptions? = null) {
    navigate(viewVerticalScrollSampleRoute, navOptions)
}

fun NavGraphBuilder.viewVerticalScrollSampleScreen(
) {
    composable(route = viewVerticalScrollSampleRoute) {
        VerticalScrollSample()
    }
}

@Composable
private fun VerticalScrollSample(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
    ) {
        Box(Modifier.padding(it)) {
            AndroidFragment<NestedWebViewFragment>()
        }
    }
}

class NestedWebViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nested_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<WebView>(R.id.webview).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://developer.android.com/")
        }
        view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            setOnRefreshListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(2_000)
                    isRefreshing = false
                }
            }
        }
    }
}