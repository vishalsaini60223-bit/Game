package com.example.data.model

enum class InvestmentStatus {
    ACTIVE,
    COMPLETED,
    SOLD
}

enum class OrderStatus {
    PENDING_VERIFICATION,
    VERIFIED_CREDITED,
    REJECTED
}

data class UserProfile(
    val name: String,
    val phone: String,
    val email: String = "vs1089236@gmail.com",
    val uid: String,
    val balance: Double,
    val investedAmount: Double,
    val totalProfit: Double,
    val referralEarnings: Double,
    val isVip: Boolean = true
)

data class InvestmentPlan(
    val id: String,
    val price: Double,
    val profitRate: Double = 0.10, // 10% profit return
    val durationHours: Int = 24,
    val title: String,
    val tier: String,
    val badge: String
) {
    val profitAmount: Double get() = price * profitRate
    val totalReturn: Double get() = price + profitAmount
}

data class UserInvestment(
    val id: String,
    val planId: String,
    val planAmount: Double,
    val profitAmount: Double,
    val totalReturn: Double,
    val purchaseTimestamp: Long,
    val maturityTimestamp: Long,
    val status: InvestmentStatus = InvestmentStatus.ACTIVE
)

data class PaymentOrder(
    val id: String,
    val planAmount: Double,
    val planName: String,
    val receiverUpi: String = "vishalkumar90@upi",
    val utrNumber: String,
    val screenshotUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: OrderStatus = OrderStatus.PENDING_VERIFICATION,
    val adminNote: String? = "Verification in progress by Admin"
)

data class BankAccount(
    val id: String,
    val bankName: String,
    val accountNumber: String,
    val ifscCode: String,
    val accountHolderName: String,
    val payoutUpiId: String,
    val isPrimary: Boolean = true
)

data class ReferralItem(
    val id: String,
    val userName: String,
    val phoneMasked: String,
    val customerUid: String,
    val joinedDate: String,
    val bonusAmount: Double = 200.0,
    val isClaimed: Boolean = true
)

data class LiveTickerEvent(
    val id: String,
    val customerUid: String,
    val text: String,
    val amount: Double,
    val profit: Double,
    val timeAgo: String
)

data class TransactionRecord(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val referenceId: String
)
