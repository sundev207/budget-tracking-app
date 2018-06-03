package com.sundev207.expenses.ui.settings

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.data.Currency
import com.sundev207.expenses.data.database.ApplicationDatabase
import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.infrastructure.utils.Event
import com.sundev207.expenses.infrastructure.utils.Variable
import com.sundev207.expenses.infrastructure.utils.runOnBackground
import com.sundev207.expenses.source.PreferenceDataSource
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class SettingsFragmentModel(
        application: Application,
        private val databaseDataSource: DatabaseDataSource,
        private val preferenceDataSource: PreferenceDataSource)
    : AndroidViewModel(application) {

    val itemModels = Variable(emptyList<SettingItemModel>())
    val showCurrencySelectionDialog = Event()
    val showDeleteAllExpensesDialog = Event()
    val showAllExpensesDeletedMessage = Event()

    private var itemModelsDisposable: Disposable? = null

    init {
        loadItemModels()
    }

    private fun loadItemModels() {
        itemModelsDisposable = getItemModels().subscribe { itemModels.value = it }
    }

    private fun getItemModels(): Observable<List<SettingItemModel>> {
        return Observable.just(createGeneralSection())
    }

    // General section

    private fun createGeneralSection(): List<SettingItemModel> {
        var itemModels = listOf<SettingItemModel>(createGeneralHeaderModel())
        itemModels += createDefaultCurrencyItemModel()
        itemModels += createDeleteAllExpensesItemModel()
        return itemModels
    }

    private fun createGeneralHeaderModel(): GeneralHeaderModel {
        return GeneralHeaderModel()
    }

    private fun createDefaultCurrencyItemModel(): DefaultCurrencyItemModel {
        val context = getApplication<Application>()
        val defaultCurrency = preferenceDataSource.getDefaultCurrency(context)
        val itemModel = DefaultCurrencyItemModel(defaultCurrency)
        itemModel.click = { showCurrencySelectionDialog.next() }
        return itemModel
    }

    private fun createDeleteAllExpensesItemModel(): DeleteAllExpensesItemModel {
        val itemModel = DeleteAllExpensesItemModel()
        itemModel.click = { showDeleteAllExpensesDialog.next() }
        return itemModel
    }

    fun updateDefaultCurrency(currency: Currency) {
        val context = getApplication<Application>()
        preferenceDataSource.setDefaultCurrency(context, currency)
        reloadItemModels()
    }

    fun deleteAllExpenses() {
        databaseDataSource.deleteAllExpenses()
        showAllExpensesDeletedMessage.next()
    }

    private fun reloadItemModels() {
        itemModelsDisposable?.dispose()
        loadItemModels()
    }

    override fun onCleared() {
        super.onCleared()
        itemModelsDisposable?.dispose()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val databaseDataSource = DatabaseDataSource(application.database)
            val preferenceDataSource = PreferenceDataSource()
            return SettingsFragmentModel(application, databaseDataSource, preferenceDataSource) as T
        }
    }
}