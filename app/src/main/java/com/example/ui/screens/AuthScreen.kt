package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.AuthMode
import com.example.ui.AuthStep
import com.example.ui.ViviPayViewModel
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    viewModel: ViviPayViewModel,
    modifier: Modifier = Modifier
) {
    val authMode by viewModel.authMode.collectAsState()
    val authStep by viewModel.authStep.collectAsState()
    val phoneInput by viewModel.phoneInput.collectAsState()
    val otpInput by viewModel.otpInput.collectAsState()
    val generatedOtp by viewModel.generatedOtp.collectAsState()
    val otpTimer by viewModel.otpTimer.collectAsState()

    val userNameInput by viewModel.userNameInput.collectAsState()
    val newPasswordInput by viewModel.newPasswordInput.collectAsState()
    val confirmPasswordInput by viewModel.confirmPasswordInput.collectAsState()
    val loginPasswordInput by viewModel.loginPasswordInput.collectAsState()

    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showLoginPassword by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LuxuryBlack)
            .testTag("auth_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(LuxurySurface)
                    .border(2.dp, GoldBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_vp),
                    contentDescription = "Vivi Pay Logo",
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(20.dp))

            // Mode Selector: Register (New User) vs Login (Existing)
            Surface(
                color = LuxurySurface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, LuxuryCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Button(
                        onClick = { viewModel.setAuthMode(AuthMode.REGISTER_OTP) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == AuthMode.REGISTER_OTP) GoldPrimary else Color.Transparent,
                            contentColor = if (authMode == AuthMode.REGISTER_OTP) LuxuryBlack else TextSilver
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "New User (OTP + Pass)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.setAuthMode(AuthMode.LOGIN_PASSWORD) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (authMode == AuthMode.LOGIN_PASSWORD) GoldPrimary else Color.Transparent,
                            contentColor = if (authMode == AuthMode.LOGIN_PASSWORD) LuxuryBlack else TextSilver
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Login (Password)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Auth Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, GoldBorder.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    if (authMode == AuthMode.REGISTER_OTP) {
                        // Registration Steps: 1) Mobile -> 2) OTP -> 3) Set Password -> Login
                        when (authStep) {
                            AuthStep.PHONE_INPUT -> {
                                // Step 1: "phle mobile number dalege"
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = GoldContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "STEP 1 OF 3",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Enter Mobile Number",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPlatinum,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "OTP will be sent to verify your phone number",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSilver,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                OutlinedTextField(
                                    value = phoneInput,
                                    onValueChange = { viewModel.onPhoneChange(it) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("phone_input_field"),
                                    label = { Text("Mobile Number") },
                                    placeholder = { Text("Enter 10-digit number") },
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
                                        text = "Get OTP (Aage Badhein)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            AuthStep.OTP_INPUT -> {
                                // Step 2: "far otp ayg"
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = EmeraldContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "STEP 2 OF 3",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = EmeraldProfit,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Verify OTP",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPlatinum,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Enter 6-digit code sent to +91 $phoneInput",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSilver,
                                    modifier = Modifier.padding(top = 4.dp)
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
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "SMS OTP: $generatedOtp",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        TextButton(
                                            onClick = { viewModel.autofillDemoOtp() },
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text("Auto-fill OTP", color = GoldLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                                    placeholder = { Text("Enter OTP") },
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
                                        text = if (otpTimer > 0) "Resend in ${otpTimer}s" else "Didn't receive code?",
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
                                    onClick = { viewModel.verifyOtpAndProceedToPassword() },
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
                                        text = "Verify OTP & Continue",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            AuthStep.SET_PASSWORD -> {
                                // Step 3: "password set krege" -> "fer hoga login"
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = GoldContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "STEP 3 OF 3",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "Set Account Password",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPlatinum,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "Mobile verified! Ab apna secure password set karein",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldProfit,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = userNameInput,
                                    onValueChange = { viewModel.onUserNameChange(it) },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Your Name (Optional)") },
                                    placeholder = { Text("e.g. Vishal Kumar") },
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = GoldPrimary)
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = LuxuryCardBorder,
                                        focusedTextColor = TextPlatinum,
                                        unfocusedTextColor = TextPlatinum
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = newPasswordInput,
                                    onValueChange = { viewModel.onNewPasswordChange(it) },
                                    modifier = Modifier.fillMaxWidth().testTag("new_password_field"),
                                    label = { Text("Create Password *") },
                                    placeholder = { Text("Min 4 characters") },
                                    singleLine = true,
                                    visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = GoldPrimary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                            Icon(
                                                imageVector = if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Toggle Password",
                                                tint = TextSilver
                                            )
                                        }
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GoldPrimary,
                                        unfocusedBorderColor = LuxuryCardBorder,
                                        focusedTextColor = TextPlatinum,
                                        unfocusedTextColor = TextPlatinum
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedTextField(
                                    value = confirmPasswordInput,
                                    onValueChange = { viewModel.onConfirmPasswordChange(it) },
                                    modifier = Modifier.fillMaxWidth().testTag("confirm_password_field"),
                                    label = { Text("Confirm Password *") },
                                    placeholder = { Text("Re-enter password") },
                                    singleLine = true,
                                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.LockReset, contentDescription = null, tint = GoldPrimary)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                            Icon(
                                                imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Toggle Password",
                                                tint = TextSilver
                                            )
                                        }
                                    },
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
                                    onClick = { viewModel.completeRegistrationAndLogin() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("complete_register_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = LuxuryBlack
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Save Password & Login to Vivi Pay",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Existing User Login with Password
                        Text(
                            text = "Login with Mobile & Password",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPlatinum,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Enter your registered mobile number and password",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSilver
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { viewModel.onPhoneChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_field"),
                            label = { Text("Registered Mobile Number") },
                            placeholder = { Text("10-digit number") },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = loginPasswordInput,
                            onValueChange = { viewModel.onLoginPasswordChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_field"),
                            label = { Text("Password") },
                            placeholder = { Text("Enter your password") },
                            singleLine = true,
                            visualTransformation = if (showLoginPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = GoldPrimary)
                            },
                            trailingIcon = {
                                IconButton(onClick = { showLoginPassword = !showLoginPassword }) {
                                    Icon(
                                        imageVector = if (showLoginPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Password",
                                        tint = TextSilver
                                    )
                                }
                            },
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
                            onClick = { viewModel.loginWithPassword() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_password_submit_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = LuxuryBlack
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Login",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // VIP 80,000 Account Helper Box (For user's pre-configured VIP Account)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LuxurySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f))
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
                        Text(
                            text = "👑 VIP Account Credentials",
                            style = MaterialTheme.typography.titleSmall,
                            color = GoldLight,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = EmeraldContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "₹80,000 Balance",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldProfit,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Mobile: +91 90769 84521  •  Pass: ViviPay@80k",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSilver,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.setAuthMode(AuthMode.REGISTER_OTP)
                                viewModel.fillVipAndSendOtp()
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Send VIP OTP", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.setAuthMode(AuthMode.LOGIN_PASSWORD)
                                viewModel.fillVipMobile()
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LuxurySurfaceVariant, contentColor = TextPlatinum),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Auto-fill VIP Login", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
