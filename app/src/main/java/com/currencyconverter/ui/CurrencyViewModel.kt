package com.currencyconverter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.currencyconverter.repository.CurrencyRepository
import com.currencyconverter.repository.data.Currency
import com.currencyconverter.ui.data.CurrencyItemModel
import com.currencyconverter.ui.data.CurrencyUpdatedModel
import com.currencyconverter.util.bumpToTop
import com.currencyconverter.util.getCurrency
import com.currencyconverter.util.toCurrencyAmountModel
import com.currencyconverter.util.toCurrencyItemModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
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
            .map { CurrencyUpdatedModel(it) }
            .doOnNext { loadingState.postValue(LoadingState.REFRESHING) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                rates.postValue(it)
            }, {
                loadingState.postValue(LoadingState.ERROR)
            })
    }

    private fun getRemoteRate(): Observable<List<CurrencyItemModel>> {
        return repository.getRemoteRates(
            rates.value?.items?.map { it.getCurrency() } ?: Currency.values().toList(),
            rates.value?.items?.get(0)?.amount ?: 100)
            .map { list ->
                list.mapIndexed { index, currencyAmountModel ->
                    currencyAmountModel.toCurrencyItemModel(index == 0)
                }
            }
    }

    private fun getLocalRate(amount: Int): Single<CurrencyUpdatedModel> {
        return repository.getLocalRates(rates.value!!.items.map { it.toCurrencyAmountModel() }, amount)
            .map { model ->
                CurrencyUpdatedModel(model.mapIndexed { index, currencyAmountModel ->
                    currencyAmountModel.toCurrencyItemModel(index == 0)
                })
            }
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
        getLocalRate(amount)
            .subscribeOn(Schedulers.io())
            .subscribe { model ->
                rates.postValue(model)
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
}