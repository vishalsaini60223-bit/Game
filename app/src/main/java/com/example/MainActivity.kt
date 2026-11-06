package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.MainTab
import com.example.ui.ViviPayViewModel
import com.example.ui.components.LuxuryTopBar
import com.example.ui.dialogs.AddBankDialog
import com.example.ui.dialogs.AdminVerificationDialog
import com.example.ui.dialogs.BuyPlanBottomSheet
import com.example.ui.dialogs.CustomerSupportDialog
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: ViviPayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                ViviPayApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ViviPayApp(viewModel: ViviPayViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val onlineUsers by viewModel.onlineUsers.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()

    // Dialog & Sheet States
    val selectedPlanForBuy by viewModel.selectedPlanForBuy.collectAsState()
    val utrInput by viewModel.utrInput.collectAsState()
    val selectedScreenshotUri by viewModel.selectedScreenshotUri.collectAsState()
    val showAddBankDialog by viewModel.showAddBankDialog.collectAsState()
    val showSupportDialog by viewModel.showSupportDialog.collectAsState()
    val showAdminOrdersDialog by viewModel.showAdminOrdersDialog.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val pendingOrdersCount = remember(orders) {
        orders.count { it.status == OrderStatus.PENDING_VERIFICATION }
    }

    // Handle back button: if not on Home tab, go back to Home
    BackHandler(enabled = isLoggedIn && currentTab != MainTab.HOME) {
        viewModel.setTab(MainTab.HOME)
    }

    // Display UI messages
    LaunchedEffect(uiMessage) {
        uiMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg.message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearUiMessage()
        }
    }

    if (!isLoggedIn) {
        AuthScreen(viewModel = viewModel)
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(LuxuryBlack),
            topBar = {
                LuxuryTopBar(
                    user = userProfile,
                    onlineUsers = onlineUsers,
                    onSupportClick = { viewModel.toggleSupportDialog(true) },
                    onAdminClick = { viewModel.toggleAdminOrdersDialog(true) },
                    pendingOrderCount = pendingOrdersCount
                )
            },
            bottomBar = {
                ViviPayBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = 80.dp)
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = LuxurySurfaceVariant,
                        contentColor = TextPlatinum,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            containerColor = LuxuryDarkBg,
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        MainTab.HOME -> HomeScreen(viewModel = viewModel)
                        MainTab.HOLDINGS_SELL -> HoldingsSellScreen(viewModel = viewModel)
                        MainTab.REFERRAL -> ReferralScreen(viewModel = viewModel)
                        MainTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                    }
                }
            }
        }

        // Buy Plan Modal Bottom Sheet
        if (selectedPlanForBuy != null) {
            BuyPlanBottomSheet(
                plan = selectedPlanForBuy!!,
                utrNumber = utrInput,
                onUtrChange = { viewModel.onUtrChange(it) },
                screenshotUri = selectedScreenshotUri,
                onScreenshotChange = { viewModel.setScreenshotUri(it) },
                onDismiss = { viewModel.closeBuySheet() },
                onSubmitProof = { viewModel.submitVerificationRequest() },
                onWalletBuy = { viewModel.buyWithWalletBalance(selectedPlanForBuy!!) },
                walletBalance = userProfile.balance
            )
        }

        // Add Bank Account Dialog
        if (showAddBankDialog) {
            AddBankDialog(
                onDismiss = { viewModel.closeAddBankDialog() },
                onAddAccount = { bank, acc, ifsc, holder, upi ->
                    viewModel.addBankAccount(bank, acc, ifsc, holder, upi)
                }
            )
        }

        // Customer Support Dialog (Telegram @sainishaab9076)
        if (showSupportDialog) {
            CustomerSupportDialog(
                onDismiss = { viewModel.toggleSupportDialog(false) }
            )
        }

        // Admin Verification Requests Dialog
        if (showAdminOrdersDialog) {
            AdminVerificationDialog(
                orders = orders,
                onApproveOrder = { viewModel.adminApproveOrder(it) },
                onDismiss = { viewModel.toggleAdminOrdersDialog(false) }
            )
        }
    }
}

@Composable
fun ViviPayBottomNavigation(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = LuxuryBlack,
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = LuxuryCardBorder,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .testTag("bottom_nav_bar"),
        windowInsets = WindowInsets.navigationBars
    ) {
        NavigationBarItem(
            selected = currentTab == MainTab.HOME,
            onClick = { onTabSelected(MainTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == MainTab.HOME) Icons.Default.Storefront else Icons.Default.Storefront,
                    contentDescription = "Buy Plans"
                )
            },
            label = {
                Text(
                    text = "Buy Plans",
                    fontWeight = if (currentTab == MainTab.HOME) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LuxuryBlack,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextSilver,
                unselectedTextColor = TextSilver
            ),
            modifier = Modifier.testTag("tab_home")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.HOLDINGS_SELL,
            onClick = { onTabSelected(MainTab.HOLDINGS_SELL) },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Sell & Earn"
                )
            },
            label = {
                Text(
                    text = "Sell & Earn",
                    fontWeight = if (currentTab == MainTab.HOLDINGS_SELL) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LuxuryBlack,
                selectedTextColor = EmeraldProfit,
                indicatorColor = EmeraldProfit,
                unselectedIconColor = TextSilver,
                unselectedTextColor = TextSilver
            ),
            modifier = Modifier.testTag("tab_sell")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.REFERRAL,
            onClick = { onTabSelected(MainTab.REFERRAL) },
            icon = {
                Icon(
                    imageVector = Icons.Default.GroupAdd,
                    contentDescription = "Refer & Earn"
                )
            },
            label = {
                Text(
                    text = "Refer (₹200)",
                    fontWeight = if (currentTab == MainTab.REFERRAL) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LuxuryBlack,
                selectedTextColor = GoldLight,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextSilver,
                unselectedTextColor = TextSilver
            ),
            modifier = Modifier.testTag("tab_referral")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.PROFILE,
            onClick = { onTabSelected(MainTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Account"
                )
            },
            label = {
                Text(
                    text = "Account",
                    fontWeight = if (currentTab == MainTab.PROFILE) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LuxuryBlack,
                selectedTextColor = GoldPrimary,
                indicatorColor = GoldPrimary,
                unselectedIconColor = TextSilver,
                unselectedTextColor = TextSilver
            ),
            modifier = Modifier.testTag("tab_profile")
        )
    }
}
