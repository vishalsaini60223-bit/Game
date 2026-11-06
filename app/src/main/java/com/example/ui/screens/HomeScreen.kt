package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InvestmentPlan
import com.example.ui.MainTab
import com.example.ui.ViviPayViewModel
import com.example.ui.components.LivePlansCarousel
import com.example.ui.components.LiveTickerBanner
import com.example.ui.components.LuxuryBalanceCard
import com.example.ui.components.PlanDetailCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: ViviPayViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.userProfile.collectAsState()
    val tickerEvents by viewModel.tickerEvents.collectAsState()
    val plans = viewModel.plans
    var selectedTier by remember { mutableStateOf("All") }

    val tiers = listOf("All", "Starter (350-600)", "Growth (800-1500)", "Pro (2000-3000)", "Elite (8500-40k)")

    val filteredPlans = remember(selectedTier, plans) {
        when (selectedTier) {
            "Starter (350-600)" -> plans.filter { it.tier == "Starter" }
            "Growth (800-1500)" -> plans.filter { it.tier == "Growth" }
            "Pro (2000-3000)" -> plans.filter { it.tier == "Pro" }
            "Elite (8500-40k)" -> plans.filter { it.tier == "Elite" }
            else -> plans
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Luxury Balance Card (80k Balance default for VIP)
        item {
            LuxuryBalanceCard(
                user = user,
                onBuyClick = {
                    // Open buy sheet for the popular 10,000 plan or scroll
                    val popularPlan = plans.find { it.price == 10000.0 } ?: plans.first()
                    viewModel.openBuySheet(popularPlan)
                },
                onSellClick = { viewModel.setTab(MainTab.HOLDINGS_SELL) },
                onWithdrawClick = { viewModel.setTab(MainTab.PROFILE) },
                onAddBankClick = { viewModel.openAddBankDialog() }
            )
        }

        // Live Real-Time Ticker Banner
        item {
            LiveTickerBanner(tickerEvents = tickerEvents)
        }

        // Live Scrolling Carousel of all 14 plans
        item {
            LivePlansCarousel(
                plans = plans,
                onPlanSelect = { plan -> viewModel.openBuySheet(plan) }
            )
        }

        // Tier Filter Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "All 14 Investment Plans",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+10% Net Profit",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldProfit,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(tiers) { tier ->
                        FilterChip(
                            selected = selectedTier == tier,
                            onClick = { selectedTier = tier },
                            label = { Text(tier, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = LuxuryBlack,
                                containerColor = LuxurySurface,
                                labelColor = TextSilver
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedTier == tier,
                                borderColor = if (selectedTier == tier) GoldPrimary else LuxuryCardBorder
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Plans List (350, 400, 500, 600, 800, 950, 1050, 1500, 2000, 2500, 3000, 8500, 10000, 40000)
        items(filteredPlans, key = { it.id }) { plan ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                PlanDetailCard(
                    plan = plan,
                    onBuyProofClick = { viewModel.openBuySheet(plan) },
                    onWalletBuyClick = { viewModel.buyWithWalletBalance(plan) },
                    walletBalance = user.balance
                )
            }
        }
    }
}
