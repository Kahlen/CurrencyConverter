package com.currencyconverter.ui

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.currencyconverter.R
import com.currencyconverter.databinding.ActivityMainBinding
import com.currencyconverter.di.DaggerMainComponent
import com.currencyconverter.di.InternetComponentInjectHelper
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: CurrencyViewModel
    private lateinit var bindingView: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerMainComponent.builder()
            .internetComponent(InternetComponentInjectHelper.provideInternetComponent(applicationContext))
            .build()
            .injectMainActivity(this)
        super.onCreate(savedInstanceState)
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, factory).get(CurrencyViewModel::class.java)

        initRecyclerView()
        viewModel.getRates()
    }

    private fun initRecyclerView() {
        val adapter = CurrencyListAdapter(viewModel)
        val layoutManager = LinearLayoutManager(this)
        bindingView.currencies.let {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }
        val treeObserver = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layoutManager.findViewByPosition(0)?.let {
                    layoutManager.findViewByPosition(0)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    (bindingView.currencies.getChildViewHolder(it) as CurrencyViewHolder).focusInputView()
                }
            }

        }
        val adapterObserver = object: RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                adapter.unregisterAdapterDataObserver(this)
                layoutManager.scrollToPositionWithOffset(0, 0)
            }
        }
        viewModel.rates.observe(this, Observer { model ->
            adapter.submitList(model.items)
            model.itemBumpedFromIndex?.let {
                layoutManager.findViewByPosition(it)?.viewTreeObserver?.addOnGlobalLayoutListener(treeObserver)
                adapter.registerAdapterDataObserver(adapterObserver)
            }
        })
        viewModel.loadingState.observe(this, Observer {
            bindingView.loadingState = it
        })
    }
}
