package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.model.OrderStatus
import com.example.data.model.PaymentOrder
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminVerificationDialog(
    orders: List<PaymentOrder>,
    onApproveOrder: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .testTag("admin_verification_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = LuxuryBlack),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Deposit Verification Admin",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Receiver: vishalkumar90@upi",
                            style = MaterialTheme.typography.bodySmall,
                            color = GoldLight
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSilver
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No payment verification requests yet.\nSubmit a proof when buying any plan to test!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSilver,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(orders, key = { it.id }) { order ->
                            AdminOrderItemCard(
                                order = order,
                                onApprove = { onApproveOrder(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrderItemCard(
    order: PaymentOrder,
    onApprove: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LuxurySurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (order.status == OrderStatus.PENDING_VERIFICATION) GoldBorder else LuxuryCardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.id,
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.planName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = when (order.status) {
                        OrderStatus.PENDING_VERIFICATION -> GoldContainer
                        OrderStatus.VERIFIED_CREDITED -> EmeraldContainer
                        OrderStatus.REJECTED -> LuxurySurfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = when (order.status) {
                            OrderStatus.PENDING_VERIFICATION -> "Pending Verification"
                            OrderStatus.VERIFIED_CREDITED -> "Verified & Credited"
                            OrderStatus.REJECTED -> "Rejected"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = when (order.status) {
                            OrderStatus.PENDING_VERIFICATION -> GoldPrimary
                            OrderStatus.VERIFIED_CREDITED -> EmeraldProfit
                            OrderStatus.REJECTED -> CrimsonError
                        },
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(LuxurySurfaceVariant)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Amount", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                    Text(
                        text = "₹${order.planAmount.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(text = "12-Digit UTR", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                    Text(
                        text = order.utrNumber,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPlatinum,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Date", style = MaterialTheme.typography.labelSmall, color = TextSilver)
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSilver
                    )
                }
            }

            // Screenshot preview if present
            if (order.screenshotUri != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(order.screenshotUri),
                        contentDescription = "Payment Screenshot",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Payment receipt attached",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldProfit
                    )
                }
            }

            if (order.status == OrderStatus.PENDING_VERIFICATION) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onApprove,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("admin_approve_${order.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldProfit,
                        contentColor = LuxuryBlack
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Approve & Credit ₹${order.planAmount.toInt()} Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
