package com.github.numeroanddev.nestedwebview.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

internal const val sampleListRoute = "sampleList"

fun NavGraphBuilder.sampleListScreen(
    onNavigateComposeVerticalScrollSample: () -> Unit,
    onNavigateComposeHorizontalPagerSample: () -> Unit,
    onNavigateViewVerticalScrollSample: () -> Unit,
) {
    composable(route = sampleListRoute) {
        SampleList(
            onNavigateComposeVerticalScrollSample = onNavigateComposeVerticalScrollSample,
            onNavigateComposeHorizontalPagerSample = onNavigateComposeHorizontalPagerSample,
            onNavigateViewVerticalScrollSample = onNavigateViewVerticalScrollSample
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleList(
    onNavigateComposeVerticalScrollSample: () -> Unit,
    onNavigateComposeHorizontalPagerSample: () -> Unit,
    onNavigateViewVerticalScrollSample: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text("Sample")
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = it
        ) {
            item {
                ListItem(
                    headlineContent = {
                        Text("Compose Vertical Scroll")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateComposeVerticalScrollSample)
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text("Compose Horizontal Pager")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateComposeHorizontalPagerSample)
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text("View Vertical Scroll")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateViewVerticalScrollSample)
                )
            }
        }
    }
}