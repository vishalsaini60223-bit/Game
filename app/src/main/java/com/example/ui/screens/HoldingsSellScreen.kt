package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.InvestmentStatus
import com.example.data.model.UserInvestment
import com.example.ui.ViviPayViewModel
import com.example.ui.theme.*

@Composable
fun HoldingsSellScreen(
    viewModel: ViviPayViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.userProfile.collectAsState()
    val investments by viewModel.investments.collectAsState()
    val bankAccounts by viewModel.bankAccounts.collectAsState()

    val activeInvestments = investments.filter { it.status == InvestmentStatus.ACTIVE }
    val soldInvestments = investments.filter { it.status == InvestmentStatus.SOLD }

    var calcAmount by remember { mutableStateOf("10000") }
    val calcInput = calcAmount.toDoubleOrNull() ?: 0.0
    val calcProfit = calcInput * 0.10
    val calcTotal = calcInput + calcProfit

    var withdrawAmountInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("holdings_sell_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header: Sell & Earn Dashboard
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Sell & Liquidation Center",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPlatinum,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Sell holdings and claim your 10% profit return",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSilver
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EmeraldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = EmeraldProfit,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LuxuryBlack)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Active Holdings Value",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSilver
                            )
                            Text(
                                text = "₹${String.format("%,.0f", activeInvestments.sumOf { it.planAmount })}",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Pending 10% Profits",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldProfit
                            )
                            Text(
                                text = "+₹${String.format("%,.0f", activeInvestments.sumOf { it.profitAmount })}",
                                style = MaterialTheme.typography.titleMedium,
                                color = EmeraldProfit,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Active Holdings Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Active Investments (${activeInvestments.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPlatinum,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Instant Sell Available",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldProfit
                )
            }
        }

        if (activeInvestments.isEmpty()) {
            item {
                Surface(
                    color = LuxurySurface,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Active Holdings Yet",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Buy any of the 14 plans from Home to start earning 10% returns!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        } else {
            items(activeInvestments, key = { it.id }) { investment ->
                ActiveInvestmentSellCard(
                    investment = investment,
                    onSell = { viewModel.confirmSellHolding(investment.id) }
                )
            }
        }

        // Instant Bank Payout / Liquidation Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Instant Payout to Bank Account",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Transfer your balance directly into your added bank account",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSilver
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val primaryBank = bankAccounts.firstOrNull { it.isPrimary } ?: bankAccounts.firstOrNull()

                    if (primaryBank != null) {
                        Surface(
                            color = LuxuryBlack,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = primaryBank.bankName,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPlatinum,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "A/C: ${primaryBank.accountNumber} • IFSC: ${primaryBank.ifscCode}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSilver,
                                        fontSize = 11.sp
                                    )
                                }

                                Surface(
                                    color = EmeraldContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Primary Payout",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = EmeraldProfit,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { viewModel.openAddBankDialog() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = LuxuryBlack),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Bank Account to Receive Payout")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = withdrawAmountInput,
                        onValueChange = { if (it.all { ch -> ch.isDigit() }) withdrawAmountInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("withdraw_amount_input"),
                        label = { Text("Enter Amount to Withdraw (₹)") },
                        placeholder = { Text("Available: ₹${String.format("%,.0f", user.balance)}") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = LuxuryCardBorder,
                            focusedTextColor = TextPlatinum,
                            unfocusedTextColor = TextPlatinum
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            TextButton(onClick = { withdrawAmountInput = user.balance.toInt().toString() }) {
                                Text("MAX", color = GoldLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val amt = withdrawAmountInput.toDoubleOrNull() ?: 0.0
                            viewModel.withdrawBalanceToBank(amt)
                            withdrawAmountInput = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("confirm_withdraw_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldProfit,
                            contentColor = LuxuryBlack
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Withdraw to Bank Account", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 10% Profit Return Calculator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "10% Return Profit Calculator",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = calcAmount,
                        onValueChange = { if (it.all { ch -> ch.isDigit() }) calcAmount = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calculator_input"),
                        label = { Text("Investment Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = LuxuryCardBorder,
                            focusedTextColor = TextPlatinum,
                            unfocusedTextColor = TextPlatinum
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(LuxuryBlack)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Guaranteed Profit (10%)", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                            Text(
                                text = "+₹${String.format("%,.0f", calcProfit)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = EmeraldProfit,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Total Return on Sell", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                            Text(
                                text = "₹${String.format("%,.0f", calcTotal)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = GoldLight,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveInvestmentSellCard(
    investment: UserInvestment,
    onSell: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("holding_card_${investment.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LuxurySurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Holding #${investment.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${investment.planAmount.toInt()} Investment",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "+10% Matured",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldProfit,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(LuxurySurfaceVariant)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Principal", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                    Text(
                        text = "₹${investment.planAmount.toInt()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column {
                    Text(text = "Profit (+10%)", style = MaterialTheme.typography.labelSmall, color = EmeraldProfit)
                    Text(
                        text = "+₹${investment.profitAmount.toInt()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EmeraldProfit,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total Payout", style = MaterialTheme.typography.labelSmall, color = GoldLight)
                    Text(
                        text = "₹${investment.totalReturn.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSell,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("sell_button_${investment.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldProfit,
                    contentColor = LuxuryBlack
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sell & Cashout ₹${investment.totalReturn.toInt()} (+10% Profit)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
