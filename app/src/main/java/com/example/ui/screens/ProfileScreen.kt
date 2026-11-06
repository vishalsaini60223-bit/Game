package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.ViviPayViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    viewModel: ViviPayViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val user by viewModel.userProfile.collectAsState()
    val bankAccounts by viewModel.bankAccounts.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Customer UID Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF262012),
                                    LuxurySurface
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Brush.linearGradient(listOf(GoldPrimary, GoldDark))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "VP",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = LuxuryBlack,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Column {
                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPlatinum,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = user.phone,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSilver
                                    )
                                }
                            }

                            Surface(
                                color = GoldContainer,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
                            ) {
                                Text(
                                    text = "👑 VIP MEMBER",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Customer UID One-Tap Copy Strip
                        Surface(
                            color = LuxuryBlack,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Customer UID", user.uid))
                                    Toast.makeText(context, "UID ${user.uid} copied!", Toast.LENGTH_SHORT).show()
                                }
                                .testTag("copy_customer_uid_card")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Official Customer UID",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSilver,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = user.uid,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = GoldLight,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy UID",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Copy UID",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Account Section (Bank Accounts)
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Payout Accounts",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPlatinum,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bank & UPI accounts for 10% profit payouts",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSilver
                            )
                        }

                        Button(
                            onClick = { viewModel.openAddBankDialog() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = LuxuryBlack),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_account_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Account", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (bankAccounts.isEmpty()) {
                        Text(
                            text = "No bank account added yet. Add one to receive your 10% returns.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver
                        )
                    } else {
                        bankAccounts.forEach { bank ->
                            Surface(
                                color = LuxuryBlack,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (bank.isPrimary) GoldBorder else LuxuryCardBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = bank.bankName,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = TextPlatinum,
                                                fontWeight = FontWeight.Bold
                                            )
                                            if (bank.isPrimary) {
                                                Surface(color = GoldContainer, shape = RoundedCornerShape(4.dp)) {
                                                    Text(
                                                        text = "PRIMARY",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = GoldPrimary,
                                                        fontSize = 9.sp,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "A/C: ${bank.accountNumber} • IFSC: ${bank.ifscCode}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSilver,
                                            fontSize = 11.sp
                                        )
                                        if (bank.payoutUpiId.isNotBlank()) {
                                            Text(
                                                text = "UPI: ${bank.payoutUpiId}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = EmeraldProfit,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    if (!bank.isPrimary) {
                                        TextButton(onClick = { viewModel.setPrimaryBank(bank.id) }) {
                                            Text("Set Primary", color = GoldLight, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Deposit Verification Requests Status
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
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Deposit Verification Requests",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { viewModel.toggleAdminOrdersDialog(true) }) {
                            Text("Admin Review", color = GoldLight, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (orders.isEmpty()) {
                        Text(
                            text = "No deposit requests yet. Buy a plan to submit UTR proof.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver
                        )
                    } else {
                        orders.take(3).forEach { order ->
                            Surface(
                                color = LuxuryBlack,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${order.planName} (₹${order.planAmount.toInt()})",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = TextPlatinum,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "UTR: ${order.utrNumber}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSilver,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Surface(
                                        color = if (order.status == OrderStatus.VERIFIED_CREDITED) EmeraldContainer else GoldContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (order.status == OrderStatus.VERIFIED_CREDITED) "Credited" else "Under Review",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (order.status == OrderStatus.VERIFIED_CREDITED) EmeraldProfit else GoldPrimary,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Customer Support Card with Telegram @sainishaab9076
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleSupportDialog(true) }
                    .testTag("support_menu_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = ElectricBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Customer Support",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPlatinum,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Telegram: @sainishaab9076 • 24x7 Helpdesk",
                                style = MaterialTheme.typography.bodySmall,
                                color = ElectricBlue
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextSilver,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Reset VIP 80k Balance Demo Button
        item {
            OutlinedButton(
                onClick = { viewModel.reset80kBalance() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("reset_balance_button"),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Restore ₹80,000 VIP Balance Demo", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }

        // Switch Account / Logout
        item {
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("logout_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LuxurySurfaceVariant,
                    contentColor = TextPlatinum
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Logout / Switch Account", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}
