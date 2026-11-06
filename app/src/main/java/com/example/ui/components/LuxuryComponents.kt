package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InvestmentPlan
import com.example.data.model.LiveTickerEvent
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun LuxuryTopBar(
    user: UserProfile,
    onlineUsers: Int,
    onSupportClick: () -> Unit,
    onAdminClick: () -> Unit,
    pendingOrderCount: Int
) {
    val context = LocalContext.current

    Surface(
        color = LuxuryBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Customer UID Badge
            Surface(
                color = LuxurySurface,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f)),
                modifier = Modifier
                    .testTag("uid_badge")
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Customer UID", user.uid))
                        Toast.makeText(context, "UID ${user.uid} copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(GoldPrimary, GoldDark))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VP",
                            style = MaterialTheme.typography.labelSmall,
                            color = LuxuryBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }
                    Column {
                        Text(
                            text = "UID: ${user.uid}",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy UID",
                        tint = GoldLight.copy(alpha = 0.8f),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Right side: Online Users Pulse & Support & Admin
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Live Online Users Badge (>100)
                LiveOnlineUsersPill(count = onlineUsers)

                // Admin orders quick toggle
                BadgedBox(
                    badge = {
                        if (pendingOrderCount > 0) {
                            Badge(
                                containerColor = CrimsonError,
                                contentColor = Color.White
                            ) {
                                Text("$pendingOrderCount")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LuxurySurface)
                            .border(1.dp, LuxuryCardBorder, CircleShape)
                            .testTag("admin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "Verify Orders",
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Customer Support Button
                IconButton(
                    onClick = onSupportClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(LuxurySurface)
                        .border(1.dp, GoldBorder.copy(alpha = 0.4f), CircleShape)
                        .testTag("support_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.HeadsetMic,
                        contentDescription = "Support",
                        tint = GoldPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LiveOnlineUsersPill(count: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        color = EmeraldContainer,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldProfit.copy(alpha = 0.4f)),
        modifier = Modifier.testTag("online_users_pill")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(EmeraldProfit.copy(alpha = alpha))
            )
            Text(
                text = "$count Online",
                style = MaterialTheme.typography.labelSmall,
                color = EmeraldProfit,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun LuxuryBalanceCard(
    user: UserProfile,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onAddBankClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = GoldPrimary.copy(alpha = 0.3f))
            .testTag("balance_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF241E10),
                            Color(0xFF161512),
                            Color(0xFF0C0F14)
                        ),
                        radius = 800f
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        listOf(GoldPrimary, GoldDark.copy(alpha = 0.4f), GoldLight)
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top row: Label + VIP Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Vivi Pay Wallet",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSilver
                        )
                    }

                    Surface(
                        color = GoldContainer,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "👑 VIP GOLD",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Balance Number (80k demo or active)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "₹",
                        style = MaterialTheme.typography.headlineMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = String.format("%,.2f", user.balance),
                        style = MaterialTheme.typography.displayLarge,
                        color = TextPlatinum,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats breakdown
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LuxurySurfaceVariant.copy(alpha = 0.6f))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Invested",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSilver
                        )
                        Text(
                            text = "₹${String.format("%,.0f", user.investedAmount)}",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(26.dp)
                            .background(LuxuryCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total Profit (+10%)",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldProfit
                        )
                        Text(
                            text = "+₹${String.format("%,.0f", user.totalProfit)}",
                            style = MaterialTheme.typography.titleSmall,
                            color = EmeraldProfit,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(26.dp)
                            .background(LuxuryCardBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Referral Earned",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight
                        )
                        Text(
                            text = "₹${String.format("%,.0f", user.referralEarnings)}",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onBuyClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("action_buy_plan"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = LuxuryBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Buy Plan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onSellClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("action_sell_plan"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldProfit,
                            contentColor = LuxuryBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sell & Earn",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onWithdrawClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("action_withdraw"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.7f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Payout",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveTickerBanner(tickerEvents: List<LiveTickerEvent>) {
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(tickerEvents) {
        while (true) {
            delay(3500)
            if (tickerEvents.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % tickerEvents.size
            }
        }
    }

    val currentEvent = tickerEvents.getOrNull(currentIndex) ?: return

    Surface(
        color = LuxurySurfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = GoldContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "LIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "${currentEvent.customerUid} ${currentEvent.text}",
                style = MaterialTheme.typography.bodySmall,
                color = TextPlatinum,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = currentEvent.timeAgo,
                style = MaterialTheme.typography.labelSmall,
                color = TextSilver,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun LivePlansCarousel(
    plans: List<InvestmentPlan>,
    onPlanSelect: (InvestmentPlan) -> Unit
) {
    val scrollState = rememberScrollState()

    // Smooth subtle scrolling animation effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(40)
            if (scrollState.value < scrollState.maxValue) {
                scrollState.scrollTo(scrollState.value + 2)
            } else {
                scrollState.scrollTo(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Live Scrolling Plans (10% Profit)",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPlatinum,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "14 Live",
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            plans.forEach { plan ->
                MiniPlanScrollCard(
                    plan = plan,
                    onClick = { onPlanSelect(plan) }
                )
            }
        }
    }
}

@Composable
fun MiniPlanScrollCard(
    plan: InvestmentPlan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .clickable(onClick = onClick)
            .testTag("mini_plan_${plan.price.toInt()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LuxurySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GoldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = plan.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "+10% Net",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldProfit,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "₹${plan.price.toInt()}",
                style = MaterialTheme.typography.titleLarge,
                color = GoldPrimary,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Return: ₹${plan.totalReturn.toInt()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPlatinum,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Profit: +₹${plan.profitAmount.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldProfit,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun PlanDetailCard(
    plan: InvestmentPlan,
    onBuyProofClick: () -> Unit,
    onWalletBuyClick: () -> Unit,
    walletBalance: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("plan_card_${plan.price.toInt()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LuxurySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Tier & Badge & 10% tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = GoldContainer,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = plan.tier,
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = plan.badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSilver
                    )
                }

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldProfit.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = EmeraldProfit,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "+10% Profit Return",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldProfit,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Plan Title & Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cycle Duration: ${plan.durationHours} Hours",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSilver
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${plan.price.toInt()}",
                        style = MaterialTheme.typography.displayMedium,
                        color = GoldPrimary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payout calculation preview box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LuxurySurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Net Profit (10%)",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSilver
                    )
                    Text(
                        text = "+₹${plan.profitAmount.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = EmeraldProfit,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Payout Speed",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSilver
                    )
                    Text(
                        text = "Instant / 24h",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Return",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSilver
                    )
                    Text(
                        text = "₹${plan.totalReturn.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldLight,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Buy Actions: UPI Proof Buy (Primary) & Wallet Buy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Primary: UPI Payment & Proof Verification (vishalkumar90@upi)
                Button(
                    onClick = onBuyProofClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(46.dp)
                        .testTag("buy_upi_proof_${plan.price.toInt()}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = LuxuryBlack
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Buy (UPI Proof)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Wallet direct buy (if balance permits)
                OutlinedButton(
                    onClick = onWalletBuyClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("buy_wallet_${plan.price.toInt()}"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (walletBalance >= plan.price) EmeraldProfit.copy(alpha = 0.8f) else LuxuryCardBorder
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (walletBalance >= plan.price) EmeraldProfit else TextSilver
                    )
                ) {
                    Text(
                        text = "Wallet Pay",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
