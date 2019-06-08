package com.currencyconverter.util

fun <T> List<T>.bumpToTop(index: Int): List<T> {
    if (index <= 0 || index >= this.size) {
        return this
    }
    return listOf(this[index]) +
            this.subList(0, index) +
            if (index < this.size-1) {
                this.subList(index+1, this.size)
            } else emptyList()
}