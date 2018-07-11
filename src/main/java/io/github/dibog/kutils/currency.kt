package io.github.dibog.kutils

import java.text.DecimalFormat

private val decimalFormat = ThreadLocal.withInitial { DecimalFormat("0.00") }

private fun Double.round(precision : Int = 0): Double {
    require(precision >=0)

    val factor = Math.pow(10.0, precision.toDouble())
    val padded = Math.floor(this*factor + 0.5)

    return padded / factor
}

fun Double.asCurrencyAmount(currencySign: String) = "$currencySign${decimalFormat.get().format(this)}"
