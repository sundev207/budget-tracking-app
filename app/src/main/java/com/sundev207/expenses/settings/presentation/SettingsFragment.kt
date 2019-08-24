package com.sundev207.expenses.settings.presentation

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sundev207.expenses.R
import com.sundev207.expenses.util.extensions.application
import com.sundev207.expenses.util.extensions.plusAssign
import com.sundev207.expenses.common.presentation.currencyselection.CurrencySelectionDialogFragment
import io.reactivex.disposables.CompositeDisposable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.sundev207.expenses.util.extensions.startActivitySafely
import com.sundev207.expenses.util.isGranted
import com.sundev207.expenses.util.isPermissionGranted
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var containerLayout: ViewGroup
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: SettingsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var model: SettingsFragmentModel

    private val compositeDisposable = CompositeDisposable()

    // Lifecycle start

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindWidgets(view)
        setupActionBar()
        setupRecyclerView()
        setupModel()
        bindModel()
    }

    private fun bindWidgets(view: View) {
        containerLayout = view.findViewById(R.id.layout_container)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    private fun setupActionBar() {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar ?: return
        actionBar.setTitle(R.string.settings)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_active_light_24dp)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        adapter = SettingsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun setupModel() {
        val factory = SettingsFragmentModel.Factory(requireContext().application)
        model = ViewModelProviders.of(this, factory).get(SettingsFragmentModel::class.java)
    }

    private fun bindModel() {
        compositeDisposable += model.itemModels
            .toObservable()
            .subscribe(adapter::submitList)
        compositeDisposable += model.showCurrencySelectionDialog
            .toObservable()
            .subscribe { showCurrencySelectionDialog() }
        compositeDisposable += model.showDeleteAllExpensesDialog
            .toObservable()
            .subscribe { showDeleteAllExpensesDialog() }
        compositeDisposable += model.showMessage
            .toObservable()
            .subscribe { showMessage(it) }
        compositeDisposable += model.showActivity
            .toObservable()
            .subscribe { showActivity(it) }
        compositeDisposable += model.shareData
            .toObservable()
            .subscribe { shareData(it) }

        compositeDisposable += model.selectFile
            .toObservable()
            .subscribe { showFileSelectionPicker(it) }
        compositeDisposable += model.requestWriteExternalStoragePermission
            .toObservable()
            .subscribe { requestWriteExternalStoragePermission(it) }

        compositeDisposable += model.observeWorkInfo
            .toObservable()
            .subscribe { observeWorkInfo(it) }
    }

    private fun showCurrencySelectionDialog() {
        val dialogFragment = CurrencySelectionDialogFragment.newInstance()
        dialogFragment.onCurrencySelected = { currency -> model.updateDefaultCurrency(currency) }
        dialogFragment.show(requireFragmentManager(), "CurrencySelectionDialogFragment")
    }

    private fun showDeleteAllExpensesDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage(R.string.delete_all_expenses_message)
            .setPositiveButton(R.string.yes) { _, _ -> model.deleteAllExpenses() }
            .setNegativeButton(R.string.no) { _, _ -> }
            .create()
            .show()
    }

    private fun showMessage(messageId: Int) {
        Snackbar.make(containerLayout, messageId, Snackbar.LENGTH_LONG).show()
    }

    private fun showActivity(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireActivity().startActivitySafely(intent)
    }

    private fun shareData(text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)))
    }

    private fun showFileSelectionPicker(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(XLS_MIME_TYPE))
            type = "*/*"
        }
        startActivityForResult(intent, requestCode)
    }

    private fun requestWriteExternalStoragePermission(requestCode: Int) {
        if (isPermissionGranted(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            model.permissionGranted(requestCode)
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
        }
    }

    private fun observeWorkInfo(id: UUID) {
        WorkManager.getInstance().getWorkInfoByIdLiveData(id).observe(this, Observer {
            model.handleWorkInfo(it)
        })
    }

    // Lifecycle end

    override fun onDestroyView() {
        super.onDestroyView()
        unbindModel()
    }

    private fun unbindModel() {
        compositeDisposable.clear()
    }

    // Options

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> backSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun backSelected(): Boolean {
        requireActivity().onBackPressed()
        return true
    }

    // File selection

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uriToSelectedFile = data?.data

        if (resultCode == Activity.RESULT_OK && uriToSelectedFile != null) {
            model.fileForImportSelected(requestCode, uriToSelectedFile)
        }
    }

    // Permissions

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (isGranted(grantResults)) model.permissionGranted(requestCode)
    }

    companion object {
        private const val XLS_MIME_TYPE = "application/vnd.ms-excel"
    }
}