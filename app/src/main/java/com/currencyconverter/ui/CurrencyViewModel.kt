package com.currencyconverter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.currencyconverter.repository.CurrencyConstants.CURRENCY_MAP
import com.currencyconverter.repository.CurrencyRepository
import com.currencyconverter.repository.data.Currency
import com.currencyconverter.repository.data.CurrencyAmountModel
import com.currencyconverter.ui.data.CurrencyItemModel
import com.currencyconverter.ui.data.CurrencyUpdatedModel
import com.currencyconverter.util.bumpToTop
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CurrencyViewModel(private val repository: CurrencyRepository)
: ViewModel(), CurrencyItemInteractionListener {

    val rates: MutableLiveData<CurrencyUpdatedModel> = MutableLiveData()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private var disposable: Disposable? = null

    fun getRates() {
        loadingState.value = LoadingState.LOADING
        subscribeToRemoteRate(0)
    }

    private fun subscribeToRemoteRate(initialDelay: Long) {
        disposable = Observable.interval(initialDelay, 1, TimeUnit.SECONDS)
            .flatMap {
                getRemoteRate().subscribeOn(Schedulers.io())
            }
            .doOnNext { loadingState.postValue(LoadingState.REFRESHING) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                rates.postValue(CurrencyUpdatedModel(it))
            }, {
                loadingState.postValue(LoadingState.ERROR)
            })
    }

    private fun getRemoteRate(): Observable<List<CurrencyItemModel>> {
        val currency = getCurrentCurrency()
        val amount = rates.value?.items?.get(0)?.amount ?: 100
        return repository.getAmounts(currency, amount)
            .map { single ->
                val oldRates = rates.value
                oldRates?.items?.map { oldRate ->
                    oldRate.copy(amount = single[CURRENCY_MAP[oldRate.currencyCode]]!!)
                } ?: mutableListOf(
                    CurrencyItemModel(
                        currencyCode = currency.code,
                        currencyName = currency.nameRes,
                        amount = amount,
                        imageRes = currency.flagRes,
                        editable = true
                    )
                ).apply {
                    addAll(single.filter { it.key != currency }
                        .map { (currency, amount) ->
                            CurrencyItemModel(
                                currencyCode = currency.code,
                                currencyName = currency.nameRes,
                                amount = amount,
                                imageRes = currency.flagRes,
                                editable = false
                            )
                        })
                }
            }
            .subscribeOn(Schedulers.io())
    }

    private fun getLocalRate(currencyAmountModel: CurrencyAmountModel): Completable {
        return Completable.fromAction {
            val base = repository.latestRate?.get(currencyAmountModel.currency)
            rates.postValue(CurrencyUpdatedModel(if (base != null) {
                rates.value?.items?.map { currencyItemModel ->
                    if (currencyItemModel.currencyCode == currencyAmountModel.currency.code) {
                        currencyItemModel
                    } else {
                        currencyItemModel.copy(
                            amount = (currencyAmountModel.amount*repository.latestRate!![CURRENCY_MAP[currencyItemModel.currencyCode]]!!/base).toInt())
                    }
                } ?: emptyList()
            } else {
                emptyList()
            }))
        }.subscribeOn(Schedulers.io())
    }

    override fun onItemClicked(position: Int) {
        if (position != 0) {
            disposable?.dispose()
            Completable.fromAction{
                rates.value?.items?.toMutableList()?.apply {
                    this[0] = this[0].copy(editable = false)
                    this[position] = this[position].copy(editable = true)
                }?.bumpToTop(position)?.let {
                    rates.postValue(CurrencyUpdatedModel(it, position))
                }}
                .subscribeOn(Schedulers.io())
                .subscribe {
                    restartRemotePolling()
                }
        }
    }

    override fun onAmountChanged(amount: Int) {
        disposable?.dispose()
        getLocalRate(CurrencyAmountModel(getCurrentCurrency(), amount))
            .subscribe {
                restartRemotePolling()
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    private fun restartRemotePolling() {
        subscribeToRemoteRate(1)
    }

    private fun getCurrentCurrency(): Currency {
        return rates.value?.items?.get(0)?.currencyCode?.let { CURRENCY_MAP[it] } ?: Currency.EUR
    }
}