package com.github.numeroanddev.nestedwebview.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.github.numeroanddev.nestedwebview.app.compose.composeHorizontalPagerSampleScreen
import com.github.numeroanddev.nestedwebview.app.compose.composeVerticalScrollSampleScreen
import com.github.numeroanddev.nestedwebview.app.compose.navigateToComposeHorizontalPagerSample
import com.github.numeroanddev.nestedwebview.app.compose.navigateToComposeVerticalScrollSample
import com.github.numeroanddev.nestedwebview.app.view.navigateToViewVerticalScrollSample
import com.github.numeroanddev.nestedwebview.app.view.viewVerticalScrollSampleScreen

@Composable
fun SampleAppNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = sampleListRoute,
    ) {
        sampleListScreen(
            onNavigateComposeVerticalScrollSample = {
                navController.navigateToComposeVerticalScrollSample()
            },
            onNavigateComposeHorizontalPagerSample = {
                navController.navigateToComposeHorizontalPagerSample()
            },
            onNavigateViewVerticalScrollSample = {
                navController.navigateToViewVerticalScrollSample()
            }
        )
        composeVerticalScrollSampleScreen()
        composeHorizontalPagerSampleScreen()
        viewVerticalScrollSampleScreen()
    }
}