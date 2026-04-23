package com.github.numeroanddev.nestedwebview.app.compose

import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.numeroanddev.nestedscrollwebview.NestedScrollWebView

private const val composeHorizontalPagerSampleRoute = "composeHorizontalPagerSample"

fun NavController.navigateToComposeHorizontalPagerSample(navOptions: NavOptions? = null) {
    navigate(composeHorizontalPagerSampleRoute, navOptions)
}

fun NavGraphBuilder.composeHorizontalPagerSampleScreen(
) {
    composable(route = composeHorizontalPagerSampleRoute) {
        HorizontalPagerSample()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun HorizontalPagerSample(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Compose Sample")
                },
            )
        },
    ) {
        val pageList = remember {
            listOf(
                "https://www.apple.com/store",
                "https://developer.android.com/",
            )
        }
        HorizontalPager(
            state = rememberPagerState { pageList.size },
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) { page ->
            AndroidView(
                factory = {
                    NestedScrollWebView(it).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        loadUrl(pageList[page])
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(rememberNestedScrollInteropConnection())
            )
        }
    }
}