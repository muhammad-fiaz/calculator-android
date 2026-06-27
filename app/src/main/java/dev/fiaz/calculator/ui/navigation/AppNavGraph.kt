package dev.fiaz.calculator.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.fiaz.calculator.ui.screens.*
import dev.fiaz.calculator.ui.viewmodel.*

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CALCULATOR,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Routes.CALCULATOR) {
            val viewModel = hiltViewModel<CalculatorViewModel>()
            CalculatorScreen(
                viewModel = viewModel,
                onOpenHistory = { navController.navigateSingleTop(Routes.HISTORY) },
                onOpenSettings = { navController.navigateSingleTop(Routes.SETTINGS) },
                onOpenConverter = { navController.navigateSingleTop(Routes.UNIT_CONVERTER) }
            )
        }
        composable(Routes.HISTORY) {
            val viewModel = hiltViewModel<HistoryViewModel>()
            val calculatorEntry = remember(navController) { navController.getBackStackEntry(Routes.CALCULATOR) }
            val calculatorViewModel = hiltViewModel<CalculatorViewModel>(calculatorEntry)
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onUseExpression = { expression ->
                    calculatorViewModel.overwriteExpression(expression)
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SETTINGS) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onAppearance = { navController.navigateSingleTop(Routes.APPEARANCE) },
                onCustomization = { navController.navigateSingleTop(Routes.CALCULATION) },
                onAbout = { navController.navigateSingleTop(Routes.ABOUT) },
                onLegal = { navController.navigateSingleTop(Routes.LEGAL) },
                onLicenses = { navController.navigateSingleTop(Routes.LICENSES) }
            )
        }
        composable(Routes.APPEARANCE) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            AppearanceSettingsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.CALCULATION) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            CalculationSettingsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            val viewModel = hiltViewModel<SettingsViewModel>()
            AboutScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable(Routes.LEGAL) {
            LegalScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.LICENSES) {
            LicensesScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.UNIT_CONVERTER) {
            val viewModel = hiltViewModel<UnitConverterViewModel>()
            UnitConverterScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigateSingleTop(Routes.SETTINGS) }
            )
        }
    }
}
