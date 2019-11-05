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
import com.sundev207.expenses.authentication.AuthenticationManager
import com.sundev207.expenses.data.model.Currency
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
    private val preferenceDataSource: PreferenceDataSource,
    private val authenticationManager: AuthenticationManager
) : AndroidViewModel(application) {

    val itemModels = Variable(emptyList<SettingItemModel>())

    val selectDefaultCurrency = Event()
    val selectFileForImport = Event()
    val requestWriteExternalStorageForExport = Event()
    val showDeleteAllExpensesDialog = Event()
    val showExpenseImportFailureDialog = Event()
    val showExpenseExportFailureDialog = Event()
    val navigateToOnboarding = Event()

    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()

    val observeWorkInfo = DataEvent<UUID>()

    private var expenseImportId: UUID? = null
    private var expenseExportId: UUID? = null
    private var expenseDeletionId: UUID? = null

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        loadItemModels()
    }

    private fun loadItemModels() {
        itemModels.value =
            createAccountSection() + createExpensesSection() + createApplicationSection()
    }

    // Account section

    private fun createAccountSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createAccountHeader(context)
        itemModels += if (authenticationManager.isUserSignedIn()) {
            createSignOut(context)
        } else {
            signUpOrSignIn(context)
        }

        return itemModels
    }

    private fun createAccountHeader(context: Context): SettingItemModel =
        SettingsHeaderModel(context.getString(R.string.account))

    private fun createSignOut(context: Context): SettingItemModel {
        val title = context.getString(R.string.sign_out)
        val summary = authenticationManager.getCurrentUserEmail() ?: ""

        return SummaryActionSettingItemModel(title, summary).apply {
            click = {
                authenticationManager.signOut()
                preferenceDataSource.setIsUserOnboarded(getApplication(), false)
                navigateToOnboarding.next()
            }
        }
    }

    private fun signUpOrSignIn(context: Context): SettingItemModel {
        val title = context.getString(R.string.sign_up_or_sign_in)

        return ActionSettingItemModel(title).apply {
            click = {
                preferenceDataSource.setIsUserOnboarded(getApplication(), false)
                navigateToOnboarding.next()
            }
        }
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
            click = { selectDefaultCurrency.next() }
        }
    }

    private fun createImportExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.import_from_excel)

        return ActionSettingItemModel(title).apply {
            click = {
                selectFileForImport.next()
            }
        }
    }

    private fun createExportExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.export_to_excel)

        return ActionSettingItemModel(title).apply {
            click = { requestWriteExternalStorageForExport.next() }
        }
    }

    private fun createDeleteAllExpenses(context: Context): SettingItemModel {
        val title = context.getString(R.string.delete_all)

        return ActionSettingItemModel(title).apply {
            click = {
                showDeleteAllExpensesDialog.next()
            }
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

    fun defaultCurrencySelect(defaultCurrency: Currency) {
        getApplication<Application>().let {
            preferenceDataSource.setDefaultCurrency(it, defaultCurrency)
        }

        loadItemModels()
    }

    fun fileForImportSelected(fileUri: Uri) {
        importExpenses(fileUri)
    }

    private fun importExpenses(fileUri: Uri) {
        if (expenseImportId != null) return

        val id = ExpenseImportWorker.enqueue(getApplication<Application>(), fileUri)
        expenseImportId = id
        observeWorkInfo.next(id)
    }

    fun permissionGranted() {
        exportExpenses()
    }

    private fun exportExpenses() {
        if (expenseExportId != null) return

        val id = ExpenseExportWorker.enqueue(getApplication<Application>())
        expenseExportId = id
        observeWorkInfo.next(id)
    }

    fun deleteAllExpenses() {
        if (expenseDeletionId != null) return

        val id = ExpenseDeletionWorker.enqueue(getApplication<Application>())
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
                showExpenseImportFailureDialog.next()
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
                showExpenseExportFailureDialog.next()
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

    fun downloadTemplate() {
        showActivity.next(TEMPLATE_XLS_URI)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val preferenceDataSource = PreferenceDataSource()
            val authenticationManager = AuthenticationManager.getInstance(application)
            return SettingsFragmentModel(
                application,
                preferenceDataSource,
                authenticationManager
            ) as T
        }
    }

    companion object {

        private val TEMPLATE_XLS_URI =
            Uri.parse("https://raw.githubusercontent.com/sundev207/expenses/master/resources/template.xls")

        private val EMAIL_URI =
            Uri.parse("mailto:the.sundev207@gmail.com")

        private val GOOGLE_PLAY_URI =
            Uri.parse("https://play.google.com/store/apps/details?id=com.sundev207.expenses")

        private val GITHUB_URI =
            Uri.parse("https://github.com/Justin Padilla/Expenses")

        private val PRIVACY_POLICY_URI =
            Uri.parse("https://raw.githubusercontent.com/sundev207/expenses/master/resources/privacy_policy.md")
    }
}