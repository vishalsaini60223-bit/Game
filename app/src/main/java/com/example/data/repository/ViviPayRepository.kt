package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class ViviPayRepository private constructor() {

    private val scope = CoroutineScope(Dispatchers.Default)

    // 14 required plans with exact 10% return profit
    val plans: List<InvestmentPlan> = listOf(
        InvestmentPlan("plan_350", 350.0, 0.10, 24, "Starter Silver 350", "Starter", "🔥 Hot"),
        InvestmentPlan("plan_400", 400.0, 0.10, 24, "Starter Gold 400", "Starter", "⭐ Popular"),
        InvestmentPlan("plan_500", 500.0, 0.10, 24, "Growth Prime 500", "Starter", "⚡ Fast Return"),
        InvestmentPlan("plan_600", 600.0, 0.10, 24, "Growth Plus 600", "Starter", "🚀 10% Net"),
        InvestmentPlan("plan_800", 800.0, 0.10, 24, "Classic Earner 800", "Growth", "💎 Trending"),
        InvestmentPlan("plan_950", 950.0, 0.10, 24, "Advantage 950", "Growth", "✨ High Yield"),
        InvestmentPlan("plan_1050", 1050.0, 0.10, 24, "Super Boost 1050", "Growth", "🔥 Hot"),
        InvestmentPlan("plan_1500", 1500.0, 0.10, 24, "Maxi Profit 1500", "Growth", "⭐ Recommended"),
        InvestmentPlan("plan_2000", 2000.0, 0.10, 24, "Pro Investor 2000", "Pro", "💎 VIP Choice"),
        InvestmentPlan("plan_2500", 2500.0, 0.10, 24, "Pro Elite 2500", "Pro", "🚀 Popular"),
        InvestmentPlan("plan_3000", 3000.0, 0.10, 24, "Royal Shield 3000", "Pro", "👑 High Gain"),
        InvestmentPlan("plan_8500", 8500.0, 0.10, 24, "Executive Club 8500", "Elite", "⭐ 10% Guaranteed"),
        InvestmentPlan("plan_10000", 10000.0, 0.10, 24, "Crown Master 10000", "Elite", "👑 VIP Leader"),
        InvestmentPlan("plan_40000", 40000.0, 0.10, 24, "Imperial Pinnacle 40000", "Elite", "🏆 Maximum Profit")
    )

    // User profile state - Pre-configured VIP Account with 80k balance as requested
    private val defaultVipUser = UserProfile(
        name = "Vishal Saini (VIP)",
        phone = "+91 90769 84521",
        uid = "VP-80001",
        balance = 80000.0, // 80k balance
        investedAmount = 18500.0,
        totalProfit = 14200.0,
        referralEarnings = 1600.0,
        isVip = true
    )

    private val _userProfile = MutableStateFlow(defaultVipUser)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Live online users count (>100 as requested, fluctuating live)
    private val _onlineUsers = MutableStateFlow(148)
    val onlineUsers: StateFlow<Int> = _onlineUsers.asStateFlow()

    // Active Investments / Holdings
    private val _investments = MutableStateFlow<List<UserInvestment>>(
        listOf(
            UserInvestment(
                id = "inv_1",
                planId = "plan_10000",
                planAmount = 10000.0,
                profitAmount = 1000.0,
                totalReturn = 11000.0,
                purchaseTimestamp = System.currentTimeMillis() - (12 * 3600 * 1000),
                maturityTimestamp = System.currentTimeMillis() + (12 * 3600 * 1000),
                status = InvestmentStatus.ACTIVE
            ),
            UserInvestment(
                id = "inv_2",
                planId = "plan_8500",
                planAmount = 8500.0,
                profitAmount = 850.0,
                totalReturn = 9350.0,
                purchaseTimestamp = System.currentTimeMillis() - (18 * 3600 * 1000),
                maturityTimestamp = System.currentTimeMillis() + (6 * 3600 * 1000),
                status = InvestmentStatus.ACTIVE
            )
        )
    )
    val investments: StateFlow<List<UserInvestment>> = _investments.asStateFlow()

    // Bank Accounts
    private val _bankAccounts = MutableStateFlow<List<BankAccount>>(
        listOf(
            BankAccount(
                id = "bank_1",
                bankName = "State Bank of India",
                accountNumber = "XXXX XXXX 8819",
                ifscCode = "SBIN0002140",
                accountHolderName = "Vishal Saini",
                payoutUpiId = "vishalsaini60223@upi",
                isPrimary = true
            )
        )
    )
    val bankAccounts: StateFlow<List<BankAccount>> = _bankAccounts.asStateFlow()

    // Payment verification orders (vishalkumar90@upi)
    private val _orders = MutableStateFlow<List<PaymentOrder>>(
        listOf(
            PaymentOrder(
                id = "ORD-9482",
                planAmount = 3000.0,
                planName = "Royal Shield 3000",
                receiverUpi = "vishalkumar90@upi",
                utrNumber = "431289056421",
                screenshotUri = null,
                timestamp = System.currentTimeMillis() - 7200000,
                status = OrderStatus.VERIFIED_CREDITED,
                adminNote = "Verified & credited by Admin"
            )
        )
    )
    val orders: StateFlow<List<PaymentOrder>> = _orders.asStateFlow()

    // Referrals (minimum ₹200 earning per referral)
    private val _referrals = MutableStateFlow<List<ReferralItem>>(
        listOf(
            ReferralItem("ref_1", "Amit Sharma", "+91 98210 ****", "VP-49102", "Yesterday", 200.0, true),
            ReferralItem("ref_2", "Rahul Verma", "+91 97184 ****", "VP-58219", "2 days ago", 200.0, true),
            ReferralItem("ref_3", "Pooja Yadav", "+91 99341 ****", "VP-61094", "3 days ago", 200.0, true),
            ReferralItem("ref_4", "Karan Singh", "+91 98112 ****", "VP-77281", "4 days ago", 200.0, true),
            ReferralItem("ref_5", "Deepak Kumar", "+91 94501 ****", "VP-83210", "5 days ago", 200.0, true),
            ReferralItem("ref_6", "Sandeep Rawat", "+91 99103 ****", "VP-90142", "6 days ago", 200.0, true),
            ReferralItem("ref_7", "Vikas Dubey", "+91 98290 ****", "VP-93201", "Last week", 200.0, true),
            ReferralItem("ref_8", "Rohan Gupta", "+91 96504 ****", "VP-98124", "Last week", 200.0, true)
        )
    )
    val referrals: StateFlow<List<ReferralItem>> = _referrals.asStateFlow()

    // Transactions History
    private val _transactions = MutableStateFlow<List<TransactionRecord>>(
        listOf(
            TransactionRecord(
                id = "tx_1",
                title = "10% Profit Credited",
                subtitle = "Royal Shield 3000 Matured",
                amount = 3300.0,
                isCredit = true,
                timestamp = System.currentTimeMillis() - 14400000,
                referenceId = "VP-TX-90214"
            ),
            TransactionRecord(
                id = "tx_2",
                title = "Referral Bonus Received",
                subtitle = "Friend VP-49102 Joined",
                amount = 200.0,
                isCredit = true,
                timestamp = System.currentTimeMillis() - 86400000,
                referenceId = "VP-REF-8410"
            ),
            TransactionRecord(
                id = "tx_3",
                title = "Plan Purchased",
                subtitle = "Crown Master 10000 (10% Return)",
                amount = 10000.0,
                isCredit = false,
                timestamp = System.currentTimeMillis() - 43200000,
                referenceId = "VP-BUY-3194"
            )
        )
    )
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    // Live Ticker for Scrolling Recent Activities
    private val _tickerEvents = MutableStateFlow<List<LiveTickerEvent>>(
        listOf(
            LiveTickerEvent("t1", "VP-3914", "activated ₹10,000 Plan", 10000.0, 1000.0, "Just now"),
            LiveTickerEvent("t2", "VP-8219", "claimed ₹1,155 Return", 1050.0, 105.0, "1m ago"),
            LiveTickerEvent("t3", "VP-1940", "sold holding with ₹850 Profit", 8500.0, 850.0, "3m ago"),
            LiveTickerEvent("t4", "VP-6502", "activated ₹40,000 Imperial Plan", 40000.0, 4000.0, "5m ago"),
            LiveTickerEvent("t5", "VP-4112", "earned ₹200 Referral Bonus", 200.0, 200.0, "6m ago"),
            LiveTickerEvent("t6", "VP-9321", "claimed ₹2,750 on ₹2,500 Plan", 2500.0, 250.0, "8m ago")
        )
    )
    val tickerEvents: StateFlow<List<LiveTickerEvent>> = _tickerEvents.asStateFlow()

    init {
        startLiveSimulation()
    }

    private fun startLiveSimulation() {
        // Online users fluctuation (>100 constantly)
        scope.launch {
            while (true) {
                delay(3500)
                val delta = Random.nextInt(-4, 6)
                _onlineUsers.update { current ->
                    val next = current + delta
                    if (next < 118) 124 else if (next > 198) 186 else next
                }
            }
        }

        // Live ticker generation
        scope.launch {
            val randomPlans = listOf(350, 400, 500, 600, 800, 950, 1050, 1500, 2000, 2500, 3000, 8500, 10000, 40000)
            while (true) {
                delay(6000)
                val uidNum = Random.nextInt(1000, 9999)
                val planAmt = randomPlans.random().toDouble()
                val profitAmt = planAmt * 0.10
                val eventType = Random.nextInt(0, 3)
                val text = when (eventType) {
                    0 -> "activated ₹${planAmt.toInt()} Plan (+₹${profitAmt.toInt()} Profit)"
                    1 -> "claimed ₹${(planAmt + profitAmt).toInt()} return on ₹${planAmt.toInt()} Plan"
                    else -> "sold holding with +10% return (+₹${profitAmt.toInt()})"
                }
                val newEvent = LiveTickerEvent(
                    id = UUID.randomUUID().toString(),
                    customerUid = "VP-$uidNum",
                    text = text,
                    amount = planAmt,
                    profit = profitAmt,
                    timeAgo = "Just now"
                )
                _tickerEvents.update { list ->
                    (listOf(newEvent) + list).take(15)
                }
            }
        }
    }

    // Login functions
    fun loginWithPhone(phone: String, userName: String = "Vivi Investor"): UserProfile {
        val existing = _userProfile.value
        val uid = "VP-" + Random.nextInt(10000, 99999)
        val profile = UserProfile(
            name = userName,
            phone = phone,
            uid = uid,
            balance = 500.0, // Welcome signup bonus
            investedAmount = 0.0,
            totalProfit = 0.0,
            referralEarnings = 0.0,
            isVip = false
        )
        _userProfile.value = profile
        return profile
    }

    fun loginVipDemoAccount() {
        _userProfile.value = defaultVipUser
    }

    // Submit Payment Proof with UTR & Screenshot
    fun submitPaymentOrder(
        plan: InvestmentPlan,
        utrNumber: String,
        screenshotUri: String?
    ): PaymentOrder {
        val order = PaymentOrder(
            id = "ORD-" + Random.nextInt(1000, 9999),
            planAmount = plan.price,
            planName = plan.title,
            receiverUpi = "vishalkumar90@upi",
            utrNumber = utrNumber,
            screenshotUri = screenshotUri,
            timestamp = System.currentTimeMillis(),
            status = OrderStatus.PENDING_VERIFICATION,
            adminNote = "Proof received. Admin verifying UTR $utrNumber"
        )
        _orders.update { listOf(order) + it }
        return order
    }

    // Admin Quick Approve / Verification feature (For instant testing or user verification simulation)
    fun adminApproveOrder(orderId: String) {
        val order = _orders.value.find { it.id == orderId } ?: return
        if (order.status != OrderStatus.PENDING_VERIFICATION) return

        _orders.update { list ->
            list.map {
                if (it.id == orderId) {
                    it.copy(
                        status = OrderStatus.VERIFIED_CREDITED,
                        adminNote = "Verified & Approved by Admin! Funds activated."
                    )
                } else it
            }
        }

        // Credit to investments and calculate profit
        val newInvestment = UserInvestment(
            id = "inv_" + UUID.randomUUID().toString().take(6),
            planId = "plan_${order.planAmount.toInt()}",
            planAmount = order.planAmount,
            profitAmount = order.planAmount * 0.10,
            totalReturn = order.planAmount * 1.10,
            purchaseTimestamp = System.currentTimeMillis(),
            maturityTimestamp = System.currentTimeMillis() + (24 * 3600 * 1000),
            status = InvestmentStatus.ACTIVE
        )

        _investments.update { listOf(newInvestment) + it }
        _userProfile.update { user ->
            user.copy(
                investedAmount = user.investedAmount + order.planAmount
            )
        }

        val tx = TransactionRecord(
            id = "tx_" + UUID.randomUUID().toString().take(6),
            title = "Plan Activated (Verified)",
            subtitle = "${order.planName} via UTR ${order.utrNumber}",
            amount = order.planAmount,
            isCredit = true,
            timestamp = System.currentTimeMillis(),
            referenceId = order.id
        )
        _transactions.update { listOf(tx) + it }
    }

    // Buy plan directly from available wallet balance
    fun buyPlanWithWalletBalance(plan: InvestmentPlan): Boolean {
        val user = _userProfile.value
        if (user.balance < plan.price) return false

        _userProfile.update {
            it.copy(
                balance = it.balance - plan.price,
                investedAmount = it.investedAmount + plan.price
            )
        }

        val newInvestment = UserInvestment(
            id = "inv_" + UUID.randomUUID().toString().take(6),
            planId = plan.id,
            planAmount = plan.price,
            profitAmount = plan.profitAmount,
            totalReturn = plan.totalReturn,
            purchaseTimestamp = System.currentTimeMillis(),
            maturityTimestamp = System.currentTimeMillis() + (24 * 3600 * 1000),
            status = InvestmentStatus.ACTIVE
        )

        _investments.update { listOf(newInvestment) + it }

        val tx = TransactionRecord(
            id = "tx_" + UUID.randomUUID().toString().take(6),
            title = "Plan Purchased from Balance",
            subtitle = "${plan.title} (+10% Profit)",
            amount = plan.price,
            isCredit = false,
            timestamp = System.currentTimeMillis(),
            referenceId = "VP-WL-${newInvestment.id}"
        )
        _transactions.update { listOf(tx) + it }
        return true
    }

    // Sell holding (10% profit return payout)
    fun sellHolding(investmentId: String): Double? {
        val investment = _investments.value.find { it.id == investmentId } ?: return null
        if (investment.status != InvestmentStatus.ACTIVE) return null

        val returnAmount = investment.totalReturn
        val profit = investment.profitAmount

        _investments.update { list ->
            list.map {
                if (it.id == investmentId) it.copy(status = InvestmentStatus.SOLD) else it
            }
        }

        _userProfile.update { user ->
            user.copy(
                balance = user.balance + returnAmount,
                investedAmount = (user.investedAmount - investment.planAmount).coerceAtLeast(0.0),
                totalProfit = user.totalProfit + profit
            )
        }

        val tx = TransactionRecord(
            id = "tx_" + UUID.randomUUID().toString().take(6),
            title = "Sold Holding (+10% Profit)",
            subtitle = "Returned ₹${returnAmount.toInt()} (Principal + ₹${profit.toInt()} Profit)",
            amount = returnAmount,
            isCredit = true,
            timestamp = System.currentTimeMillis(),
            referenceId = "VP-SELL-${investment.id}"
        )
        _transactions.update { listOf(tx) + it }

        return returnAmount
    }

    // Direct Cashout / Withdraw to Added Bank Account
    fun withdrawToBankAccount(amount: Double, bankAccount: BankAccount): Boolean {
        val user = _userProfile.value
        if (user.balance < amount || amount <= 0) return false

        _userProfile.update {
            it.copy(balance = it.balance - amount)
        }

        val tx = TransactionRecord(
            id = "tx_" + UUID.randomUUID().toString().take(6),
            title = "Payout to Bank Account",
            subtitle = "${bankAccount.bankName} (${bankAccount.accountNumber})",
            amount = amount,
            isCredit = false,
            timestamp = System.currentTimeMillis(),
            referenceId = "VP-WTH-" + Random.nextInt(10000, 99999)
        )
        _transactions.update { listOf(tx) + it }
        return true
    }

    // Add Bank Account
    fun addBankAccount(
        bankName: String,
        accountNumber: String,
        ifscCode: String,
        holderName: String,
        upiId: String
    ): BankAccount {
        val newAccount = BankAccount(
            id = "bank_" + UUID.randomUUID().toString().take(6),
            bankName = bankName,
            accountNumber = accountNumber,
            ifscCode = ifscCode.uppercase(Locale.getDefault()),
            accountHolderName = holderName,
            payoutUpiId = upiId,
            isPrimary = _bankAccounts.value.isEmpty()
        )
        _bankAccounts.update { listOf(newAccount) + it }
        return newAccount
    }

    // Set primary bank
    fun setPrimaryBank(bankId: String) {
        _bankAccounts.update { list ->
            list.map { it.copy(isPrimary = it.id == bankId) }
        }
    }

    // Claim referral bonus
    fun claimReferralBonus(): Double {
        val unclaimed = _referrals.value.filter { !it.isClaimed }
        val sum = unclaimed.sumOf { it.bonusAmount }
        if (sum > 0) {
            _referrals.update { list -> list.map { it.copy(isClaimed = true) } }
            _userProfile.update { it.copy(balance = it.balance + sum, referralEarnings = it.referralEarnings + sum) }
            val tx = TransactionRecord(
                id = "tx_" + UUID.randomUUID().toString().take(6),
                title = "Referral Earnings Claimed",
                subtitle = "${unclaimed.size} Friends Joined (₹200 each)",
                amount = sum,
                isCredit = true,
                timestamp = System.currentTimeMillis(),
                referenceId = "VP-REF-CLAIM"
            )
            _transactions.update { listOf(tx) + it }
        }
        return sum
    }

    // Reset or top up balance demo
    fun resetDemoBalance(amount: Double = 80000.0) {
        _userProfile.update { it.copy(balance = amount) }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViviPayRepository? = null

        fun getInstance(): ViviPayRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViviPayRepository().also { INSTANCE = it }
            }
        }
    }
}
