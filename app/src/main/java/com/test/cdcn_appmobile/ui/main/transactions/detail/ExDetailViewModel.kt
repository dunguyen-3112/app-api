package com.test.cdcn_appmobile.ui.main.transactions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.Category
import com.test.cdcn_appmobile.data.models.Expenditure
import com.test.cdcn_appmobile.data.repository.ExpenditureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
 * Created by tuyen.dang on 12/3/2022
 */

class ExDetailViewModel(
    private val expenditureRepository: ExpenditureRepository,
) : ViewModel() {

    companion object {
        private val isEditing = MutableLiveData<Boolean>()
        private val expenditureDetail = MutableLiveData<Expenditure?>()
        private val categoryType = MutableLiveData<Int>()
        private val listCategory = MutableLiveData<MutableList<Category>?>()
        private val idCategory = MutableLiveData<String?>()
    }

    init {
        isEditing.value = false
    }

    fun getIsEditing(): LiveData<Boolean> = isEditing

    fun getExpenditureDetail(): LiveData<Expenditure?> = expenditureDetail

    fun getCategoryType(): LiveData<Int> = categoryType

    fun getListCategory(): LiveData<MutableList<Category>?> = listCategory

    fun getIdCategory(): LiveData<String?> = idCategory

    fun setExpenditureDetail(expenditure: Expenditure) {
        expenditureDetail.value = expenditure
    }

    fun setCategoryType(categoryId: Int) {
        categoryType.value = categoryId
    }

    fun changeEditing() {
        isEditing.value = !(isEditing.value ?: false)
    }

    fun setIdCategory(id: String) {
        idCategory.value = id
    }

    fun getListCategoryFromServer(
        authToken: String,
        idType: Int,
        onSuccess: (String?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            expenditureRepository.getCategory(authToken, idType).collect {
                listCategory.postValue(it.resultObj)
                withContext(Dispatchers.Main) {
                    onSuccess(it.message)
                }
            }
        }
    }

    fun addOrUpdateExpenditure(
        authToken: String,
        userId: String,
        expenditure: Expenditure,
        onSuccess: (Boolean, String?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val options: MutableMap<String, String> = HashMap()
            options["userId"] = userId
            options["categoryId"] = expenditure.categoryId
            options["cost"] = expenditure.cost.toString()
            options["creationDate"] = expenditure.date
            options["note"] = expenditure.note
            options["id"] = expenditure.id
            if (expenditure.id == "") {
                options["creationDate"] = expenditure.date
                expenditureRepository.addExpenditure(
                    authToken,
                    options
                ).collect {
                    withContext(Dispatchers.Main) {
                        onSuccess(it.isSuccessed, it.message ?: "")
                    }
                }
            } else {
                options["dateCreation"] = expenditure.date
                expenditureRepository.updateExpenditure(
                    authToken,
                    expenditure.id,
                    options
                ).collect {
                    withContext(Dispatchers.Main) {
                        onSuccess(it.isSuccessed, it.message ?: "")
                    }
                }
            }
        }
    }

    fun deleteExpenditure(
        authToken: String,
        expenditureId: String,
        onSuccess: (Boolean, String?) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            expenditureRepository.delExpenditure(
                authToken,
                expenditureId
            ).collect {
                withContext(Dispatchers.Main) {
                    onSuccess(it.isSuccessed, it.message ?: "")
                }
            }
        }
    }

}
