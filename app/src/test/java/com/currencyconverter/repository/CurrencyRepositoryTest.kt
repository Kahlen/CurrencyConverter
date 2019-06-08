package com.currencyconverter.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.currencyconverter.helper.RxTestRule
import com.currencyconverter.repository.data.Currency
import com.currencyconverter.repository.data.CurrencyRateModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var currencyApi: CurrencyApi

    private lateinit var repository: CurrencyRepository

    @Before
    fun setUp() {
        whenever(currencyApi.getLatest(Currency.EUR.code)).thenReturn(Observable.just(
            CurrencyRateModel(
                base = Currency.EUR,
                rates = mapOf(Currency.AUD to 2.0, Currency.GBP  to 3.0)
            )
        ))
        repository = CurrencyRepository(currencyApi)
    }

    @Test
    fun testGetLatest() {
        val observer = TestObserver<Map<Currency, Int>>()
        repository.getAmounts(Currency.EUR, 100).subscribe(observer)
        observer.assertValueCount(1)
        observer.assertValueAt(0,
            mapOf(Currency.EUR to 100,
                Currency.AUD to 200,
                Currency.GBP to 300))
    }

    @Test
    fun testLatestRate() {
        repository.getAmounts(Currency.EUR, 100).subscribe()
        val latestRate = repository.latestRate
        assertNotNull(latestRate)
        assertEquals(
            mapOf(Currency.EUR to 1.0,
                Currency.AUD to 2.0,
                Currency.GBP to 3.0),
            latestRate
        )
    }

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxTestRule()
    }
}