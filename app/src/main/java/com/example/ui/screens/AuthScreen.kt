package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AuthStep
import com.example.ui.ViviPayViewModel
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    viewModel: ViviPayViewModel,
    modifier: Modifier = Modifier
) {
    val authStep by viewModel.authStep.collectAsState()
    val phoneInput by viewModel.phoneInput.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    val generatedOtp by viewModel.generatedOtp.collectAsState()
    val otpTimer by viewModel.otpTimer.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LuxuryBlack)
            .testTag("auth_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(LuxurySurface)
                    .border(2.dp, GoldBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_vp),
                    contentDescription = "Vivi Pay Logo",
                    modifier = Modifier.size(68.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "VIVI PAY",
                style = MaterialTheme.typography.displayMedium,
                color = GoldPrimary,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )

            Text(
                text = "Luxury Buy & Sell Platform • 10% Profit Return",
                style = MaterialTheme.typography.bodySmall,
                color = TextSilver,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Auth Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldBorder.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (authStep == AuthStep.PHONE_INPUT) {
                        Text(
                            text = "Login with Mobile",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "OTP will be sent to your mobile number to verify",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { viewModel.onPhoneChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("phone_input_field"),
                            label = { Text("Mobile Number") },
                            placeholder = { Text("Enter 10 digit number") },
                            leadingIcon = {
                                Row(
                                    modifier = Modifier.padding(start = 12.dp, end = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+91", color = GoldLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = LuxuryCardBorder,
                                focusedTextColor = TextPlatinum,
                                unfocusedTextColor = TextPlatinum
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.requestOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("send_otp_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = LuxuryBlack
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Get OTP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        // OTP Verification Step
                        Text(
                            text = "Verify OTP",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Enter 6-digit code sent to +91 $phoneInput",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Demo OTP hint banner
                        Surface(
                            color = GoldContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Demo OTP: $generatedOtp",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                TextButton(
                                    onClick = { viewModel.autofillDemoOtp() },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Auto-fill", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { viewModel.onOtpChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_input_field"),
                            label = { Text("6-Digit OTP Code") },
                            placeholder = { Text("e.g. $generatedOtp") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = GoldPrimary)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = LuxuryCardBorder,
                                focusedTextColor = TextPlatinum,
                                unfocusedTextColor = TextPlatinum
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (otpTimer > 0) "Resend in ${otpTimer}s" else "Didn't get code?",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSilver,
                                fontSize = 11.sp
                            )
                            if (otpTimer == 0) {
                                TextButton(onClick = { viewModel.requestOtp() }) {
                                    Text("Resend OTP", color = GoldLight, fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.verifyOtp() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("verify_otp_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldProfit,
                                contentColor = LuxuryBlack
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Verify & Login",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Instant VIP Account Entry Button (80k Balance)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "VIP Access Account",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pre-loaded with ₹80,000 balance & VIP status",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSilver,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.loginVipAccount() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("vip_quick_login_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Brush.linearGradient(listOf(GoldPrimary, GoldDark)).let { GoldPrimary },
                            contentColor = LuxuryBlack
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "👑 Login as VIP (₹80,000 Balance)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
