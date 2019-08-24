package com.sundev207.expenses.settings.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.sundev207.expenses.Application
import com.sundev207.expenses.BuildConfig
import com.sundev207.expenses.R
import com.sundev207.expenses.data.Currency
import com.sundev207.expenses.data.preference.PreferenceDataSource
import com.sundev207.expenses.settings.work.ExpenseDeletionWorker
import com.sundev207.expenses.settings.work.ExpenseExportWorker
import com.sundev207.expenses.settings.work.ExpenseImportWorker
import com.sundev207.expenses.util.reactive.DataEvent
import com.sundev207.expenses.util.reactive.Event
import com.sundev207.expenses.util.reactive.Variable
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class SettingsFragmentModel(
    application: Application,
    private val preferenceDataSource: PreferenceDataSource
) : AndroidViewModel(application) {

    val itemModels = Variable(emptyList<SettingItemModel>())

    // Events
    val showCurrencySelectionDialog = Event()
    val showDeleteAllExpensesDialog = Event()

    // Common data events
    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()

    val observeWorkInfo = DataEvent<UUID>()

    // Specific data events
    val selectFile = DataEvent<Int>()
    val requestWriteExternalStoragePermission = DataEvent<Int>()

    private var expenseImportId: UUID? = null
    private var expenseExportId: UUID? = null
    private var expenseDeletionId: UUID? = null

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        loadItemModels()
    }

    private fun loadItemModels() {
        itemModels.value = createExpensesSection() + createApplicationSection()
    }

    // Expenses section

    private fun createExpensesSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createExpenseHeader(context)
        itemModels += createDefaultCurrency(context)
        itemModels += createImportExpenses(context)
        itemModels += createExportExpenses(context)
        itemModels += createDeleteAllExpenses(context)

        return itemModels
    }

    private fun createExpenseHeader(context: Context): SettingItemModel =
        SettingsHeaderModel(context.getString(R.string.expenses))

    private fun createDefaultCurrency(context: Context): SettingItemModel {
        val defaultCurrency = preferenceDataSource.getDefaultCurrency(context)

        val title = context.getString(R.string.default_currency)
        val summary = context.getString(
            R.string.default_currency_summary,
            defaultCurrency.flag,
            defaultCurrency.title,
            defaultCurrency.code
        )

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showCurrencySelectionDialog.next() }
        }
    }

    private fun createImportExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.import_from_excel)

        return ActionSettingItemModel(title).apply {
            click = { selectFile.next(REQUEST_CODE_SELECT_FILE) }
        }
    }

    private fun createExportExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.export_to_excel)

        return ActionSettingItemModel(title).apply {
            click = {
                requestWriteExternalStoragePermission.next(REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun createDeleteAllExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.delete_all)

        return ActionSettingItemModel(title).apply {
            click = { showDeleteAllExpensesDialog.next() }
        }
    }

    // About section

    private fun createApplicationSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createApplicationHeader(context)
        itemModels += createContactMe(context)
        itemModels += createRateApp(context)
        itemModels += createViewSourceCode(context)
        itemModels += createPrivacyPolicy(context)
        itemModels += createVersion(context)

        return itemModels
    }

    private fun createApplicationHeader(context: Context): SettingItemModel =
        SettingsHeaderModel(context.getString(R.string.application))

    private fun createContactMe(context: Context): SettingItemModel {
        val title = context.getString(R.string.contact_me)
        val summary = context.getString(R.string.contact_me_summary)

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showActivity.next(EMAIL_URI) }
        }
    }

    private fun createRateApp(context: Context): SettingItemModel {
        val title = context.getString(R.string.rate_app)
        val summary = context.getString(R.string.rate_app_summary)

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showActivity.next(GOOGLE_PLAY_URI) }
        }
    }

    private fun createViewSourceCode(context: Context): SettingItemModel {
        val title = context.getString(R.string.view_source_code)
        val summary = context.getString(R.string.view_source_code_summary)

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showActivity.next(GITHUB_URI) }
        }
    }

    private fun createPrivacyPolicy(context: Context): SettingItemModel {
        val title = context.getString(R.string.privacy_policy)

        return ActionSettingItemModel(title).apply {
            click = { showActivity.next(PRIVACY_POLICY_URI) }
        }
    }

    private fun createVersion(context: Context): SettingItemModel {
        val title = context.getString(R.string.version)
        val summary = BuildConfig.VERSION_NAME

        return SummaryActionSettingItemModel(title, summary)
    }

    // Lifecycle end

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    // Public

    fun updateDefaultCurrency(defaultCurrency: Currency) {
        getApplication<Application>().let {
            preferenceDataSource.setDefaultCurrency(it, defaultCurrency)
        }

        loadItemModels()
    }

    fun fileForImportSelected(requestCode: Int, fileUri: Uri) {
        if (requestCode == REQUEST_CODE_SELECT_FILE) importExpenses(fileUri)
    }

    private fun importExpenses(fileUri: Uri) {
        if (expenseImportId != null) return

        val id = ExpenseImportWorker.enqueue(fileUri)
        expenseImportId = id
        observeWorkInfo.next(id)
    }

    fun permissionGranted(requestCode: Int) {
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) exportExpenses()
    }

    private fun exportExpenses() {
        if (expenseExportId != null) return

        val id = ExpenseExportWorker.enqueue()
        expenseExportId = id
        observeWorkInfo.next(id)
    }

    fun deleteAllExpenses() {
        if (expenseDeletionId != null) return

        val id = ExpenseDeletionWorker.enqueue()
        expenseDeletionId = id
        observeWorkInfo.next(id)
    }

    fun handleWorkInfo(workInfo: WorkInfo) {
        when (workInfo.id) {
            expenseImportId -> handleExpenseImportWorkInfo(workInfo)
            expenseExportId -> handleExpenseExportWorkInfo(workInfo)
            expenseDeletionId -> handleExpenseDeletionWorkInfo(workInfo)
        }
    }

    private fun handleExpenseImportWorkInfo(workInfo: WorkInfo) {
        expenseImportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_import_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showMessage.next(R.string.expense_import_failure_message)
                null
            }
            else -> return
        }
    }

    private fun handleExpenseExportWorkInfo(workInfo: WorkInfo) {
        expenseExportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_export_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showMessage.next(R.string.expense_export_failure_message)
                null
            }
            else -> return
        }
    }

    private fun handleExpenseDeletionWorkInfo(workInfo: WorkInfo) {
        expenseDeletionId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_deletion_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showMessage.next(R.string.expense_deletion_failure_message)
                null
            }
            else -> return
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsFragmentModel(application, PreferenceDataSource()) as T
        }
    }

    companion object {

        private const val REQUEST_CODE_SELECT_FILE = 1
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 2

        private val GITHUB_URI =
            Uri.parse("https://github.com/Justin Padilla/Expenses")

        private val GOOGLE_PLAY_URI =
            Uri.parse("https://play.google.com/store/apps/details?id=com.sundev207.expenses")

        private val EMAIL_URI =
            Uri.parse("mailto:the.sundev207@gmail.com")

        private val PRIVACY_POLICY_URI =
            Uri.parse("https://github.com/sundev207/budget-tracking-app/blob/master/resources/privacy_policy.md")
    }
}