package com.test.cdcn_appmobile.ui.main.statistical

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.DrawerObject
import com.test.cdcn_appmobile.data.repository.DrawerRepository
import com.test.cdcn_appmobile.extension.toDayMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by tuyen.dang on 12/4/2022
 */

class StatisticalViewModel(private val drawerRepository: DrawerRepository) : ViewModel() {

    private val listDrawerObject: MutableLiveData<MutableList<DrawerObject>> =
        MutableLiveData<MutableList<DrawerObject>>()
    private val idDayStart: MutableLiveData<Int> = MutableLiveData<Int>()
    private val idDayEnd: MutableLiveData<Int> = MutableLiveData<Int>()
    private val idMonthChosen: MutableLiveData<Int> = MutableLiveData<Int>()
    private val idYearChosen: MutableLiveData<Int> = MutableLiveData<Int>()
    private val forDayMonth: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        listDrawerObject.value = mutableListOf()
    }

    fun getListDrawerObject(): MutableLiveData<MutableList<DrawerObject>> = listDrawerObject

    fun getIdDayStart(): LiveData<Int> = idDayStart

    fun getIdDayEnd(): LiveData<Int> = idDayEnd

    fun getIdMonthChosen(): LiveData<Int> = idMonthChosen

    fun getIdYearChosen(): LiveData<Int> = idYearChosen

    fun getForDayMonth(): LiveData<Boolean> = forDayMonth

    fun setIdDayStart(idDay: Int) {
        idDayStart.value = idDay
    }

    fun setIdDayEnd(idDay: Int) {
        idDayEnd.value = idDay
    }

    fun setIdMonthChosen(idMonth: Int) {
        idMonthChosen.value = idMonth
    }

    fun setIdYearChosen(idYear: Int) {
        idYearChosen.value = idYear
    }

    fun setForDayMonth(dayMonth: Boolean) {
        forDayMonth.value = dayMonth
    }

    fun getDrawObjectFromServer(
        authToken: String,
        idUser: String,
        onSuccess: (String?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val options: MutableMap<String, String> = HashMap()
            options["id"] = idUser
            if (forDayMonth.value == true) {
                options["year"] = idYearChosen.value.toString()
                drawerRepository.getDrawObjectByYear(
                    authToken,
                    options
                ).collect {
                    listDrawerObject.postValue(it.resultObj ?: listDrawerObject.value)
                    withContext(Dispatchers.Main) {
                        onSuccess(it.message)
                    }
                }
            } else {
                options["startDate"] = "${idDayStart.value?.toString()?.toDayMonth()}-${
                    idMonthChosen.value?.toString()?.toDayMonth()
                }-${idYearChosen.value}"
                options["endDate"] = "${idDayEnd.value?.toString()?.toDayMonth()}-${
                    idMonthChosen.value?.toString()?.toDayMonth()
                }-${idYearChosen.value}"
                drawerRepository.getDrawObjectByMonth(
                    authToken,
                    options
                ).collect {
                    listDrawerObject.postValue(it.resultObj ?: listDrawerObject.value)
                    withContext(Dispatchers.Main) {
                        onSuccess(it.message)
                    }
                }
            }
        }
    }

}
