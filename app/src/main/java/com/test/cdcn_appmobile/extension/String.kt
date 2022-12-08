package com.test.cdcn_appmobile.extension

/*
 * Created by tuyen.dang on 12/1/2022
 */

internal fun String.toDayMonth(): String {
    return "${if (this.length == 1) "0" else ""}$this"
}

internal fun String.toSingleDay(): String {
    return if (this[0] == '0') this[1].toString() else this
}