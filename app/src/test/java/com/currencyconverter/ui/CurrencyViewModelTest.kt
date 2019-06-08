package com.currencyconverter.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.currencyconverter.helper.RxTestRule
import com.currencyconverter.repository.CurrencyRepository
import com.currencyconverter.repository.data.Currency
import com.currencyconverter.ui.data.CurrencyItemModel
import com.currencyconverter.ui.data.CurrencyUpdatedModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class CurrencyViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CurrencyRepository

    private lateinit var viewModel: CurrencyViewModel

    @Before
    fun setUp() {
        whenever(repository.getAmounts(Currency.EUR, 100)).thenReturn(Observable.just(
            mapOf(Currency.EUR to 100,
                Currency.GBP to 200,
                Currency.USD to 300)
        ))
        viewModel = CurrencyViewModel(repository)
    }

    @Test
    fun getRates() {
        viewModel.getRates()
        schedulers.computation.advanceTimeBy(1, TimeUnit.SECONDS)
        assertEquals(
            CurrencyUpdatedModel(
                items = listOf(CurrencyItemModel(Currency.EUR.code, Currency.EUR.nameRes, 100, Currency.EUR.flagRes, true),
                    CurrencyItemModel(Currency.GBP.code, Currency.GBP.nameRes, 200, Currency.GBP.flagRes, false),
                    CurrencyItemModel(Currency.USD.code, Currency.USD.nameRes, 300, Currency.USD.flagRes, false)),
                updateFromRemote = true),
            viewModel.rates.value)
    }

    @Test
    fun testOnItemClick() {
        viewModel.getRates()
        schedulers.computation.advanceTimeBy(1, TimeUnit.SECONDS)

        viewModel.onItemClicked(1)
        // item at 0 and 1 swapped
        assertEquals(
            CurrencyUpdatedModel(
                items = listOf(CurrencyItemModel(Currency.GBP.code, Currency.GBP.nameRes, 200, Currency.GBP.flagRes, true),
                    CurrencyItemModel(Currency.EUR.code, Currency.EUR.nameRes, 100, Currency.EUR.flagRes, false),
                    CurrencyItemModel(Currency.USD.code, Currency.USD.nameRes, 300, Currency.USD.flagRes, false)),
                updateFromRemote = false),
            viewModel.rates.value)
    }

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxTestRule()
    }
}