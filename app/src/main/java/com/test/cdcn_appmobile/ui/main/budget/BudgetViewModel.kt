package com.test.cdcn_appmobile.ui.main.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.cdcn_appmobile.data.models.Budget
import com.test.cdcn_appmobile.data.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetViewModel(private val budgetRepository: BudgetRepository) : ViewModel() {

    companion object {
        private val isEditing = MutableLiveData<Boolean>()
        internal val budget: MutableLiveData<Budget?> = MutableLiveData<Budget?>()
        private val idUnitChosen: MutableLiveData<Int> = MutableLiveData<Int>()
        internal val isNewExpenditure: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

        fun changeNewExpenditure() {
            isNewExpenditure.value = !(isNewExpenditure.value ?: false)
        }
    }

    fun getIsEditing(): LiveData<Boolean> = isEditing

    fun getBudget(): LiveData<Budget?> = budget

    fun getIdUnitChosen(): LiveData<Int> = idUnitChosen

    fun getIsNewExpenditure(): LiveData<Boolean> = isNewExpenditure

    fun getBudgetFromServer(auth: String, idUser: String, onSuccess: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.getBudget(auth, idUser).collect {
                budget.postValue(it.resultObj ?: budget.value)
                withContext(Dispatchers.Main) {
                    onSuccess(it.message)
                }
            }
        }
    }

    fun changeEditing() {
        isEditing.value = !(isEditing.value ?: false)
    }

    fun setIdUnitChosen(id: Int) {
        idUnitChosen.value = id
    }

    fun updateBudget(
        authToken: String,
        idUser: String,
        limitMoney: String,
        onSuccess: (String) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            budgetRepository.updateBudget(authToken, idUser, limitMoney, idUnitChosen.value ?: 1)
                .collect {
                    budget.postValue(it.resultObj ?: budget.value)
                    withContext(Dispatchers.Main) {
                        onSuccess(it.message ?: "")
                    }
                }
        }
    }

}
