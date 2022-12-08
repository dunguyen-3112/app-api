package com.test.cdcn_appmobile.extension

import android.view.View

/*
 * Created by tuyen.dang on 10/12/2022
 */

internal fun View.setVisibility(statement: Boolean) {
    visibility = if (statement) View.VISIBLE else View.GONE
}