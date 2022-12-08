package com.test.cdcn_appmobile.extension

import com.test.cdcn_appmobile.data.models.ItemChoice

internal fun MutableList<ItemChoice>.OnItemChoice(idChosen: Int) {
    for(i in this) {
        i.isCheck = (i.id == idChosen)
    }
}