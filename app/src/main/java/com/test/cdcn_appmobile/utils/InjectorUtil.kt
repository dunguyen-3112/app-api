package com.test.cdcn_appmobile.utils

import com.test.cdcn_appmobile.data.repository.BudgetRepository
import com.test.cdcn_appmobile.data.repository.DrawerRepository
import com.test.cdcn_appmobile.data.repository.ExpenditureRepository
import com.test.cdcn_appmobile.data.repository.UserRepository
import com.test.cdcn_appmobile.ui.launch.LaunchViewModelFactory
import com.test.cdcn_appmobile.ui.main.budget.BudgetViewModelFactory
import com.test.cdcn_appmobile.ui.main.settings.UserViewModelFactory
import com.test.cdcn_appmobile.ui.main.statistical.StatisticalViewModelFactory
import com.test.cdcn_appmobile.ui.main.transactions.TransactionsViewModelFactory
import com.test.cdcn_appmobile.ui.main.transactions.detail.ExDetailViewModelFactory

/*
 * Created by tuyen.dang on 10/10/2022
 */

object InjectorUtil {

    fun provideLaunchViewModelFactory(): LaunchViewModelFactory {
        return LaunchViewModelFactory(UserRepository)
    }

    fun provideBudgetViewModelFactory(): BudgetViewModelFactory {
        return BudgetViewModelFactory(BudgetRepository)
    }

    fun transactionsViewModelFactory(): TransactionsViewModelFactory {
        return TransactionsViewModelFactory(ExpenditureRepository)
    }

    fun exDetailViewModelFactory(): ExDetailViewModelFactory {
        return ExDetailViewModelFactory(ExpenditureRepository)
    }

    fun provideUserViewModelFactory(): UserViewModelFactory {
        return UserViewModelFactory(UserRepository)
    }

    fun statisticalViewModelFactory(): StatisticalViewModelFactory {
        return StatisticalViewModelFactory(DrawerRepository)
    }
}
