package com.test.cdcn_appmobile.extension

import kotlin.math.abs

/*
 * Created by tuyen.dang on 11/30/2022
 */

internal fun Long.toStringNumber(): String {
    if (this == 0L) {
        return "0"
    }
    var s = ""
    var count = 0
    var temp = abs(this)
    while (temp != 0L) {
        s += (temp % 10).toString()
        count += 1
        temp /= 10
        if (count == 3 && temp != 0L) {
            s += ","
            count = 0
        }
    }
    s += if (this < 0L) "-" else ""
    return s.reversed()
}