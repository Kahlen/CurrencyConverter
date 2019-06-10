package com.currencyconverter.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.currencyconverter.databinding.CurrencyItemBinding
import com.currencyconverter.ui.data.CurrencyItemModel
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager

class CurrencyViewHolder(
    private val bindingView: CurrencyItemBinding,
    private val interactionListener: CurrencyItemInteractionListener
): RecyclerView.ViewHolder(bindingView.root) {

    private lateinit var textWatcher: TextWatcher

    init {
        bindingView.root.setOnClickListener { _ ->
            interactionListener.onItemClicked(adapterPosition)
            focusInputView()
        }

        textWatcher = object: TextWatcher {
            var oldText = ""
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val currentText = bindingView.currencyInput.text.toString()
                val newText = when {
                    currentText.isBlank() -> "0"
                    currentText.startsWith("0") -> currentText.substring(1)
                    else -> currentText
                }

                if (oldText != newText) {
                    bindingView.currencyInput.removeTextChangedListener(textWatcher)
                    if (newText != currentText) {
                        bindingView.currencyInput.setText(newText)
                        bindingView.currencyInput.setSelection(bindingView.currencyInput.text?.length ?: 0)
                    }
                    interactionListener.onAmountChanged(newText.toInt())
                    oldText = newText
                    bindingView.currencyInput.addTextChangedListener(textWatcher)
                }
            }
        }
    }

    fun bindModel(model: CurrencyItemModel) {
        bindingView.currencyInput.removeTextChangedListener(textWatcher)
        if (model.editable) {
            bindingView.currencyInput.visibility = View.VISIBLE
            bindingView.currencyText.visibility = View.GONE
        } else {
            bindingView.currencyInput.visibility = View.GONE
            bindingView.currencyText.visibility = View.VISIBLE
        }
        bindingView.model = model
        bindingView.root.post {
            if (model.editable) {
                bindingView.currencyInput.setText(bindingView.currencyText.text)
                bindingView.currencyInput.addTextChangedListener(textWatcher)
                if (bindingView.currencyInput.selectionStart == bindingView.currencyInput.selectionEnd) {
                    bindingView.currencyInput.setSelection(bindingView.currencyInput.text?.length ?: 0)
                }
            }
        }
    }

    fun focusInputView() {
        if (bindingView.currencyInput.requestFocus()) {
            val inputMethodManager = bindingView.currencyInput.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(bindingView.currencyInput, InputMethodManager.SHOW_IMPLICIT)
            bindingView.currencyInput.post {
                bindingView.currencyInput.selectAll()
            }
        }
    }
}