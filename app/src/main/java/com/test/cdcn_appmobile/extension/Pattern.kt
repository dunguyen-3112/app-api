package com.test.cdcn_appmobile.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

internal val EMAIL_ADDRESS: Regex by lazy { "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}".toRegex() }

internal val PASSWORD: Regex by lazy { "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$@\$!#%*?&.,])[A-Za-z\\d\$@\$!#%*?&.,]{6,}".toRegex() }

@SuppressLint("SimpleDateFormat")
fun getDay(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(Date())
}
