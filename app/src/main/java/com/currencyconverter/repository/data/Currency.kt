package com.currencyconverter.repository.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.currencyconverter.R

enum class Currency(val code: String, @StringRes val nameRes: Int, @DrawableRes val flagRes: Int) {
    AUD("AUD", R.string.currency_name_aud, R.drawable.ic_au),
    BGN("BGN", R.string.currency_name_bgn, R.drawable.ic_bg),
    BRL("BRL", R.string.currency_name_brl, R.drawable.ic_br),
    CAD("CAD", R.string.currency_name_cad, R.drawable.ic_ca),
    CHF("CHF", R.string.currency_name_chf, R.drawable.ic_ch),
    CNY("CNY", R.string.currency_name_cny, R.drawable.ic_cn),
    CZK("CZK", R.string.currency_name_czk, R.drawable.ic_cz),
    DKK("DKK", R.string.currency_name_dkk, R.drawable.ic_dk),
    EUR("EUR", R.string.currency_name_eur, R.drawable.ic_eu),
    GBP("GBP", R.string.currency_name_gbp, R.drawable.ic_gb),
    HKD("HKD", R.string.currency_name_hkd, R.drawable.ic_hk),
    HRK("HRK", R.string.currency_name_hrk, R.drawable.ic_hr),
    HUF("HUF", R.string.currency_name_huf, R.drawable.ic_hu),
    IDR("IDR", R.string.currency_name_idr, R.drawable.ic_id),
    ILS("ILS", R.string.currency_name_ils, R.drawable.ic_il),
    INR("INR", R.string.currency_name_inr, R.drawable.ic_in),
    ISK("ISK", R.string.currency_name_isk, R.drawable.ic_is),
    JPY("JPY", R.string.currency_name_jpy, R.drawable.ic_jp),
    KRW("KRW", R.string.currency_name_krw, R.drawable.ic_kr),
    MXN("MXN", R.string.currency_name_mxn, R.drawable.ic_mx),
    MYR("MYR", R.string.currency_name_myr, R.drawable.ic_my),
    NOK("NOK", R.string.currency_name_nok, R.drawable.ic_no),
    NZD("NZD", R.string.currency_name_nzd, R.drawable.ic_nz),
    PHP("PHP", R.string.currency_name_php, R.drawable.ic_ph),
    PLN("PLN", R.string.currency_name_pln, R.drawable.ic_pl),
    RON("RON", R.string.currency_name_ron, R.drawable.ic_ro),
    RUB("RUB", R.string.currency_name_rub, R.drawable.ic_ru),
    SEK("SEK", R.string.currency_name_sek, R.drawable.ic_se),
    SGD("SGD", R.string.currency_name_sgd, R.drawable.ic_sg),
    THB("THB", R.string.currency_name_thb, R.drawable.ic_th),
    TRY("TRY", R.string.currency_name_try, R.drawable.ic_tr),
    USD("USD", R.string.currency_name_usd, R.drawable.ic_us),
    ZAR("ZAR", R.string.currency_name_zar, R.drawable.ic_za)
}