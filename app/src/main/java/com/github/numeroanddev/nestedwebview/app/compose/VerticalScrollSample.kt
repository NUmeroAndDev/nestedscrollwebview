package com.github.numeroanddev.nestedwebview.app.compose

import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.numeroanddev.nestedscrollwebview.NestedScrollWebView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val composeVerticalScrollSampleRoute = "composeVerticalScrollSample"

fun NavController.navigateToComposeVerticalScrollSample(navOptions: NavOptions? = null) {
    navigate(composeVerticalScrollSampleRoute, navOptions)
}

fun NavGraphBuilder.composeVerticalScrollSampleScreen(
) {
    composable(route = composeVerticalScrollSampleRoute) {
        VerticalScrollSample()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun VerticalScrollSample(
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeFlexibleTopAppBar(
                title = {
                    Text("Compose Sample")
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        var isRefreshing by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                scope.launch {
                    delay(2_000)
                    isRefreshing = false
                }
            },
            modifier = Modifier.padding(it)
        ) {
            AndroidView(
                factory = {
                    NestedScrollWebView(it).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl("https://developer.android.com/")
                    }
                },
                modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection())
            )
        }
    }
}