package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun AddBankDialog(
    onDismiss: () -> Unit,
    onAddAccount: (bankName: String, accountNumber: String, ifscCode: String, holderName: String, upiId: String) -> Unit
) {
    var bankName by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var confirmAccount by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var holderName by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_bank_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = LuxuryBlack),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Add Bank Account",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "For Receiving 10% Profit Payouts",
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

                Spacer(modifier = Modifier.height(16.dp))

                // Account Holder Name
                OutlinedTextField(
                    value = holderName,
                    onValueChange = { holderName = it },
                    label = { Text("Account Holder Name *") },
                    placeholder = { Text("e.g. Vishal Kumar") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bank_holder_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bank Name
                OutlinedTextField(
                    value = bankName,
                    onValueChange = { bankName = it },
                    label = { Text("Bank Name *") },
                    placeholder = { Text("e.g. State Bank of India, HDFC") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bank_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Account Number
                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) accountNumber = it },
                    label = { Text("Bank Account Number *") },
                    placeholder = { Text("Enter account number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Numbers, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bank_account_number_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Account Number
                OutlinedTextField(
                    value = confirmAccount,
                    onValueChange = { if (it.all { ch -> ch.isDigit() }) confirmAccount = it },
                    label = { Text("Confirm Account Number *") },
                    placeholder = { Text("Re-enter account number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Numbers, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_bank_account_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // IFSC Code
                OutlinedTextField(
                    value = ifscCode,
                    onValueChange = { if (it.length <= 11) ifscCode = it.uppercase() },
                    label = { Text("Bank IFSC Code *") },
                    placeholder = { Text("e.g. SBIN0002140") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bank_ifsc_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Payout UPI ID (Optional/Recommended)
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it.trim() },
                    label = { Text("Payout UPI ID (Optional)") },
                    placeholder = { Text("e.g. yourname@okaxis") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = GoldPrimary)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bank_upi_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = LuxuryCardBorder,
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = CrimsonError
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        when {
                            holderName.isBlank() -> errorMessage = "Please enter Account Holder Name"
                            bankName.isBlank() -> errorMessage = "Please enter Bank Name"
                            accountNumber.length < 8 -> errorMessage = "Account number must be at least 8 digits"
                            accountNumber != confirmAccount -> errorMessage = "Account numbers do not match"
                            ifscCode.length < 8 -> errorMessage = "Please enter a valid IFSC code"
                            else -> {
                                errorMessage = null
                                onAddAccount(bankName, accountNumber, ifscCode, holderName, upiId)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_bank_account_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = LuxuryBlack
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Save Bank Account",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
