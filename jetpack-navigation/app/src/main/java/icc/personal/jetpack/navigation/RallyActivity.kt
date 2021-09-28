/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package icc.personal.jetpack.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navDeepLink
import icc.personal.jetpack.navigation.data.UserData
import icc.personal.jetpack.navigation.ui.accounts.AccountsBody
import icc.personal.jetpack.navigation.ui.accounts.SingleAccountBody
import icc.personal.jetpack.navigation.ui.bills.BillsBody
import icc.personal.jetpack.navigation.ui.components.RallyTabRow
import icc.personal.jetpack.navigation.ui.overview.OverviewBody
import icc.personal.jetpack.navigation.ui.theme.RallyTheme
import icc.personal.jetpack.navigation.RallyScreen.Overview as Overview
import icc.personal.jetpack.navigation.RallyScreen.Accounts as Accounts
import icc.personal.jetpack.navigation.RallyScreen.Bills as Bills

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
//        var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }
        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(
            backStackEntry.value?.destination?.route
        )
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.name,
        modifier = modifier
    ) {
        composable(Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(Accounts.name) },
                onClickSeeAllBills = { navController.navigate(Bills.name) },
                onAccountClick = { name ->
                    navController.navigate("${Accounts.name}/$name")
                },
            )
        }
        composable(Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navController.navigate("Accounts/${name}")
            }
        }
        composable(Bills.name) {
            BillsBody(bills = UserData.bills)
        }
        val accountsName = Accounts.name
        composable(
            "$accountsName/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                },
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "rally://$accountsName/{name}"
            }),
        ) { entry ->
            val accountName = entry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)
            SingleAccountBody(account = account)
        }
    }
}
