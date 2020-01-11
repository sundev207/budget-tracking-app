package com.sundev207.expenses.home.presentation

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import com.sundev207.expenses.Application
import com.sundev207.expenses.R
import com.sundev207.expenses.authentication.AuthenticationManager
import com.sundev207.expenses.settings.work.ExpenseExportWorker
import com.sundev207.expenses.settings.work.ExpenseImportWorker
import com.sundev207.expenses.util.reactive.DataEvent
import com.sundev207.expenses.util.reactive.Event
import com.sundev207.expenses.util.reactive.Variable
import java.util.*

class HomeActivityModel(
    application: Application,
    private val authenticationManager: AuthenticationManager
) : AndroidViewModel(application) {

    val isUserSignedIn: Variable<Boolean> by lazy {
        Variable(defaultValue = authenticationManager.isUserSignedIn())
    }
    val userName: Variable<String> by lazy {
        Variable(defaultValue = authenticationManager.getCurrentUserName() ?: "")
    }
    val userEmail: Variable<String> by lazy {
        Variable(defaultValue = authenticationManager.getCurrentUserEmail() ?: "")
    }

    val navigateToOnboarding = Event()
    val selectFileForImport = Event()
    val showExpenseImportFailureDialog = Event()
    val requestExportPermissions = Event()
    val showExpenseExportFailureDialog = Event()
    val navigateToSettings = Event()

    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()
    val observeWorkInfo = DataEvent<UUID>()

    private var expenseImportId: UUID? = null
    private var expenseExportId: UUID? = null

    fun navigateToOnboardingRequested() {
        navigateToOnboarding.next()
    }

    fun importExpensesRequested() {
        selectFileForImport.next()
    }

    fun fileForImportSelected(fileUri: Uri) {
        if (expenseImportId != null) return

        val id = ExpenseImportWorker.enqueue(getApplication<Application>(), fileUri)
        expenseImportId = id
        observeWorkInfo.next(id)
    }

    fun downloadTemplate() {
        showActivity.next(TEMPLATE_XLS_URI)
    }

    fun exportExpensesRequested() {
        requestExportPermissions.next()
    }

    fun exportPermissionsGranted() {
        if (expenseExportId != null) return

        val id = ExpenseExportWorker.enqueue(getApplication<Application>())
        expenseExportId = id
        observeWorkInfo.next(id)
    }

    fun handleWorkInfo(workInfo: WorkInfo) {
        when (workInfo.id) {
            expenseImportId -> handleExpenseImportWorkInfo(workInfo)
            expenseExportId -> handleExpenseExportWorkInfo(workInfo)
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

    fun navigateToSettingsRequested() {
        navigateToSettings.next()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeActivityModel(
                application,
                AuthenticationManager.getInstance(application)
            ) as T
        }
    }

    companion object {
        private val TEMPLATE_XLS_URI =
            Uri.parse("https://raw.githubusercontent.com/sundev207/expenses/master/resources/template.xls")
    }
}