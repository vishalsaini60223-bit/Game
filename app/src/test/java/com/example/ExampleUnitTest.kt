package com.example

import com.example.data.repository.ViviPayRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    private val repository = ViviPayRepository.getInstance()

    @Test
    fun testPlansConfigurationAnd10PercentProfit() {
        val expectedPrices = listOf(350.0, 400.0, 500.0, 600.0, 800.0, 950.0, 1050.0, 1500.0, 2000.0, 2500.0, 3000.0, 8500.0, 10000.0, 40000.0)
        val actualPrices = repository.plans.map { it.price }

        assertEquals(14, repository.plans.size)
        assertEquals(expectedPrices, actualPrices)

        // Verify 10% profit rate on every single plan
        repository.plans.forEach { plan ->
            val expectedProfit = plan.price * 0.10
            val expectedReturn = plan.price + expectedProfit
            assertEquals(expectedProfit, plan.profitAmount, 0.001)
            assertEquals(expectedReturn, plan.totalReturn, 0.001)
        }
    }

    @Test
    fun testVipAccountInitialBalance() {
        val user = repository.userProfile.value
        assertEquals("VP-80001", user.uid)
        assertTrue(user.isVip)
        assertTrue(user.balance >= 80000.0)
    }

    @Test
    fun testReferralMinimumBonus() {
        val referrals = repository.referrals.value
        assertTrue(referrals.isNotEmpty())
        referrals.forEach { ref ->
            assertTrue("Referral bonus should be at least 200", ref.bonusAmount >= 200.0)
        }
    }

    @Test
    fun testReceiverUpiIdAndOrderCreation() {
        val plan = repository.plans.first { it.price == 10000.0 }
        val order = repository.submitPaymentOrder(plan, "123456789012", null)

        assertEquals("vishalkumar90@upi", order.receiverUpi)
        assertEquals(10000.0, order.planAmount, 0.001)
        assertEquals("123456789012", order.utrNumber)
    }
}
