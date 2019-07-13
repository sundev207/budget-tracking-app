package com.sundev207.expenses.settings.work

import android.content.Context
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import androidx.work.*
import com.sundev207.expenses.R
import com.sundev207.expenses.data.Date
import com.sundev207.expenses.data.Expense
import com.sundev207.expenses.data.database.DatabaseDataSource
import com.sundev207.expenses.util.extensions.application
import io.reactivex.Single
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ExpenseExportWorker(context: Context, workerParams: WorkerParameters) :
    RxWorker(context, workerParams) {

    override fun createWork(): Single<Result> {
        return prepareExpenses().map { export(it) }.map { Result.success() }
    }

    private fun prepareExpenses(): Single<List<Expense>> {
        val database = applicationContext.application.database
        val databaseDataSource = DatabaseDataSource(database)

        return databaseDataSource.getExpenses()
            .map { expenses -> expenses.sortedBy { it.date.utcTimestamp } }
    }

    private fun export(expenses: List<Expense>) {
        val workbook = Workbook.createWorkbook(createFile())
        val sheet = createSheet(workbook)

        addContent(sheet, expenses)

        workbook.write()
        workbook.close()
    }

    private fun createFile(): File {
        val downloads = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
        val appName = applicationContext.getString(R.string.app_name)
        val dateString = Date.now().toString(DATE_PATTERN)
        val fileName = "${appName}_$dateString"
        val extension = ".xls"
        return File(downloads, fileName + extension)
    }

    private fun createSheet(workbook: WritableWorkbook): WritableSheet {
        val name = applicationContext.getString(R.string.expenses)
        return workbook.createSheet(name, 0)
    }

    private fun addContent(sheet: WritableSheet, expenses: List<Expense>) {
        addColumnNames(sheet)
        addExpenses(sheet, expenses)
    }

    private fun addColumnNames(sheet: WritableSheet) {
        val columnNames = createColumnNames()
        columnNames.forEachIndexed { column, columnName -> addCell(sheet, 0, column, columnName) }
    }

    private fun createColumnNames(): List<String> {
        val list = ArrayList<String>()
        list.add(applicationContext.getString(R.string.column_amount))
        list.add(applicationContext.getString(R.string.column_currency))
        list.add(applicationContext.getString(R.string.column_title))
        list.add(applicationContext.getString(R.string.column_date))
        list.add(applicationContext.getString(R.string.column_notes))
        list.add(applicationContext.getString(R.string.column_tags))
        return list
    }

    private fun addExpenses(sheet: WritableSheet, expenses: List<Expense>) {
        expenses.forEachIndexed { index, expense ->
            val expenseColumns = createExpenseColumns(expense)
            expenseColumns.forEachIndexed { column, expenseColumn ->
                addCell(sheet, index + 1, column, expenseColumn)
            }
        }
    }

    private fun createExpenseColumns(expense: Expense): List<String> {
        val list = ArrayList<String>()
        list.add("%.2f".format(expense.amount))
        list.add(expense.currency.code)
        list.add(expense.title)
        list.add(expense.date.toReadableString())
        list.add(expense.notes)
        list.add(expense.tags.joinToString { it.name })
        return list
    }

    private fun addCell(sheet: WritableSheet, row: Int, column: Int, string: String) {
        val label = Label(column, row, string)
        sheet.addCell(label)
    }

    companion object {

        private const val DATE_PATTERN = "yyyyMMdd_HHmmss"

        fun enqueue(): UUID {
            val request = OneTimeWorkRequest.Builder(ExpenseExportWorker::class.java).build()

            WorkManager.getInstance().enqueue(request)

            return request.id
        }
    }
}