package com.test.cdcn_appmobile.extension

/*
 * Created by tuyen.dang on 10/14/2022
 */

internal val page: (Int, Int) -> Int = { listSize: Int, step: Int ->
    if (listSize != 0) (listSize / 10 * step) + 1 else 0
}
