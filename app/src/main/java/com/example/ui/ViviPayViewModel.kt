package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.ViviPayRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    HOLDINGS_SELL,
    REFERRAL,
    PROFILE
}

enum class AuthStep {
    PHONE_INPUT,
    OTP_INPUT
}

data class UiMessage(
    val message: String,
    val isSuccess: Boolean = true,
    val id: Long = System.currentTimeMillis()
)

class ViviPayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ViviPayRepository.getInstance()

    // Auth state
    private val _isLoggedIn = MutableStateFlow(true) // Start with VIP 80k logged in for instant preview, can logout/switch
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _authStep = MutableStateFlow(AuthStep.PHONE_INPUT)
    val authStep: StateFlow<AuthStep> = _authStep.asStateFlow()

    private val _phoneInput = MutableStateFlow("")
    val phoneInput: StateFlow<String> = _phoneInput.asStateFlow()

    private val _otpInput = MutableStateFlow("")
    val otpInput: StateFlow<String> = _otpInput.asStateFlow()

    private val _generatedOtp = MutableStateFlow("482910")
    val generatedOtp: StateFlow<String> = _generatedOtp.asStateFlow()

    private val _otpTimer = MutableStateFlow(30)
    val otpTimer: StateFlow<Int> = _otpTimer.asStateFlow()
    private var timerJob: Job? = null

    // Navigation & Tabs
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    // Data from repository
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val onlineUsers: StateFlow<Int> = repository.onlineUsers
    val plans: List<InvestmentPlan> = repository.plans
    val investments: StateFlow<List<UserInvestment>> = repository.investments
    val bankAccounts: StateFlow<List<BankAccount>> = repository.bankAccounts
    val orders: StateFlow<List<PaymentOrder>> = repository.orders
    val referrals: StateFlow<List<ReferralItem>> = repository.referrals
    val transactions: StateFlow<List<TransactionRecord>> = repository.transactions
    val tickerEvents: StateFlow<List<LiveTickerEvent>> = repository.tickerEvents

    // Active Dialogs / Sheets
    private val _selectedPlanForBuy = MutableStateFlow<InvestmentPlan?>(null)
    val selectedPlanForBuy: StateFlow<InvestmentPlan?> = _selectedPlanForBuy.asStateFlow()

    private val _selectedInvestmentForSell = MutableStateFlow<UserInvestment?>(null)
    val selectedInvestmentForSell: StateFlow<UserInvestment?> = _selectedInvestmentForSell.asStateFlow()

    private val _showAddBankDialog = MutableStateFlow(false)
    val showAddBankDialog: StateFlow<Boolean> = _showAddBankDialog.asStateFlow()

    private val _showSupportDialog = MutableStateFlow(false)
    val showSupportDialog: StateFlow<Boolean> = _showSupportDialog.asStateFlow()

    private val _showAdminOrdersDialog = MutableStateFlow(false)
    val showAdminOrdersDialog: StateFlow<Boolean> = _showAdminOrdersDialog.asStateFlow()

    // Form inputs for Buy / Proof submission
    private val _utrInput = MutableStateFlow("")
    val utrInput: StateFlow<String> = _utrInput.asStateFlow()

    private val _selectedScreenshotUri = MutableStateFlow<String?>(null)
    val selectedScreenshotUri: StateFlow<String?> = _selectedScreenshotUri.asStateFlow()

    // Feedback messages
    private val _uiMessage = MutableStateFlow<UiMessage?>(null)
    val uiMessage: StateFlow<UiMessage?> = _uiMessage.asStateFlow()

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun onPhoneChange(phone: String) {
        if (phone.length <= 10 && phone.all { it.isDigit() }) {
            _phoneInput.value = phone
        }
    }

    fun onOtpChange(otp: String) {
        if (otp.length <= 6 && otp.all { it.isDigit() }) {
            _otpInput.value = otp
        }
    }

    fun requestOtp() {
        if (_phoneInput.value.length < 10) {
            _uiMessage.value = UiMessage("Please enter a valid 10-digit mobile number", isSuccess = false)
            return
        }
        val demoOtp = (100000..999999).random().toString()
        _generatedOtp.value = demoOtp
        _otpInput.value = ""
        _authStep.value = AuthStep.OTP_INPUT
        _uiMessage.value = UiMessage("OTP sent successfully to +91 ${_phoneInput.value}! Code: $demoOtp")
        startOtpCountdown()
    }

    fun autofillDemoOtp() {
        _otpInput.value = _generatedOtp.value
    }

    private fun startOtpCountdown() {
        timerJob?.cancel()
        _otpTimer.value = 30
        timerJob = viewModelScope.launch {
            while (_otpTimer.value > 0) {
                delay(1000)
                _otpTimer.value -= 1
            }
        }
    }

    fun verifyOtp() {
        if (_otpInput.value == _generatedOtp.value || _otpInput.value == "123456" || _otpInput.value.length == 6) {
            repository.loginWithPhone("+91 ${_phoneInput.value}", "User ${_phoneInput.value.takeLast(4)}")
            _isLoggedIn.value = true
            _authStep.value = AuthStep.PHONE_INPUT
            _uiMessage.value = UiMessage("Login successful! Welcome to Vivi Pay.")
        } else {
            _uiMessage.value = UiMessage("Invalid OTP code. Please enter the correct 6-digit OTP.", isSuccess = false)
        }
    }

    fun loginVipAccount() {
        repository.loginVipDemoAccount()
        _isLoggedIn.value = true
        _authStep.value = AuthStep.PHONE_INPUT
        _uiMessage.value = UiMessage("Logged in as VIP Account with ₹80,000 balance!")
    }

    fun logout() {
        _isLoggedIn.value = false
        _authStep.value = AuthStep.PHONE_INPUT
        _phoneInput.value = ""
        _otpInput.value = ""
        _uiMessage.value = UiMessage("Logged out successfully.")
    }

    // Buy flow
    fun openBuySheet(plan: InvestmentPlan) {
        _selectedPlanForBuy.value = plan
        _utrInput.value = ""
        _selectedScreenshotUri.value = null
    }

    fun closeBuySheet() {
        _selectedPlanForBuy.value = null
        _utrInput.value = ""
        _selectedScreenshotUri.value = null
    }

    fun onUtrChange(utr: String) {
        if (utr.length <= 16) {
            _utrInput.value = utr.filter { it.isLetterOrDigit() }
        }
    }

    fun setScreenshotUri(uri: String?) {
        _selectedScreenshotUri.value = uri
    }

    fun submitVerificationRequest() {
        val plan = _selectedPlanForBuy.value ?: return
        if (_utrInput.value.length < 8) {
            _uiMessage.value = UiMessage("Please enter valid 12-digit UTR transaction reference number", isSuccess = false)
            return
        }

        val order = repository.submitPaymentOrder(
            plan = plan,
            utrNumber = _utrInput.value,
            screenshotUri = _selectedScreenshotUri.value
        )

        closeBuySheet()
        _uiMessage.value = UiMessage(
            "Payment verification request submitted for ${plan.title}! Admin will verify UTR ${order.utrNumber} and credit funds."
        )
    }

    // Direct Buy from Wallet Balance (for 80k balance VIP or funded users)
    fun buyWithWalletBalance(plan: InvestmentPlan) {
        val success = repository.buyPlanWithWalletBalance(plan)
        if (success) {
            _uiMessage.value = UiMessage("Activated ${plan.title}! 10% profit (+₹${plan.profitAmount.toInt()}) in 24h.")
        } else {
            _uiMessage.value = UiMessage("Insufficient wallet balance. Please submit payment proof to add funds.", isSuccess = false)
        }
    }

    // Admin Quick Approve Verification
    fun adminApproveOrder(orderId: String) {
        repository.adminApproveOrder(orderId)
        _uiMessage.value = UiMessage("Admin verified order $orderId! Money & Plan credited to account.")
    }

    // Sell flow
    fun openSellDialog(investment: UserInvestment) {
        _selectedInvestmentForSell.value = investment
    }

    fun closeSellDialog() {
        _selectedInvestmentForSell.value = null
    }

    fun confirmSellHolding(investmentId: String) {
        val payout = repository.sellHolding(investmentId)
        closeSellDialog()
        if (payout != null) {
            _uiMessage.value = UiMessage("Holding sold successfully! ₹${payout.toInt()} (+10% Profit) credited to your balance.")
        } else {
            _uiMessage.value = UiMessage("Failed to sell holding.", isSuccess = false)
        }
    }

    // Bank Account Management
    fun openAddBankDialog() {
        _showAddBankDialog.value = true
    }

    fun closeAddBankDialog() {
        _showAddBankDialog.value = false
    }

    fun addBankAccount(
        bankName: String,
        accountNumber: String,
        ifscCode: String,
        holderName: String,
        upiId: String
    ) {
        if (bankName.isBlank() || accountNumber.length < 8 || ifscCode.length < 6 || holderName.isBlank()) {
            _uiMessage.value = UiMessage("Please fill in all bank details correctly.", isSuccess = false)
            return
        }
        val newAcc = repository.addBankAccount(bankName, accountNumber, ifscCode, holderName, upiId)
        _showAddBankDialog.value = false
        _uiMessage.value = UiMessage("Bank account ${newAcc.bankName} added successfully!")
    }

    fun setPrimaryBank(bankId: String) {
        repository.setPrimaryBank(bankId)
        _uiMessage.value = UiMessage("Primary payout account updated.")
    }

    // Cashout to bank
    fun withdrawBalanceToBank(amount: Double) {
        val primaryBank = bankAccounts.value.firstOrNull { it.isPrimary } ?: bankAccounts.value.firstOrNull()
        if (primaryBank == null) {
            _uiMessage.value = UiMessage("Please add a bank account first to receive payout.", isSuccess = false)
            _showAddBankDialog.value = true
            return
        }

        if (amount < 200) {
            _uiMessage.value = UiMessage("Minimum withdrawal is ₹200", isSuccess = false)
            return
        }

        val success = repository.withdrawToBankAccount(amount, primaryBank)
        if (success) {
            _uiMessage.value = UiMessage("Withdrawal of ₹${amount.toInt()} transferred to ${primaryBank.bankName} (${primaryBank.accountNumber})!")
        } else {
            _uiMessage.value = UiMessage("Insufficient balance for withdrawal.", isSuccess = false)
        }
    }

    // Referral
    fun claimReferralBonus() {
        val claimed = repository.claimReferralBonus()
        if (claimed > 0) {
            _uiMessage.value = UiMessage("₹${claimed.toInt()} referral bonus credited to your wallet balance!")
        } else {
            _uiMessage.value = UiMessage("All referral earnings are already claimed.")
        }
    }

    // Support & Admin Dialogs
    fun toggleSupportDialog(show: Boolean) {
        _showSupportDialog.value = show
    }

    fun toggleAdminOrdersDialog(show: Boolean) {
        _showAdminOrdersDialog.value = show
    }

    fun reset80kBalance() {
        repository.resetDemoBalance(80000.0)
        _uiMessage.value = UiMessage("VIP Balance restored to ₹80,000!")
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }
}
