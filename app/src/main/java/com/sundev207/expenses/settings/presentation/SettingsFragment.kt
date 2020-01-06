package com.sundev207.expenses.settings.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sundev207.expenses.R
import com.sundev207.expenses.common.presentation.DarkMode
import com.sundev207.expenses.currencyselection.CurrencySelectionActivity
import com.sundev207.expenses.data.model.Currency
import com.sundev207.expenses.onboarding.OnboardingActivity
import com.sundev207.expenses.util.extensions.application
import com.sundev207.expenses.util.extensions.plusAssign
import com.sundev207.expenses.util.extensions.startActivitySafely
import io.reactivex.disposables.CompositeDisposable

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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp)
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
            .subscribe(adapter::submitList)

        compositeDisposable += model.selectDefaultCurrency
            .subscribe { selectDefaultCurrency() }
        compositeDisposable += model.navigateToOnboarding
            .subscribe { navigateToOnboarding() }

        compositeDisposable += model.showMessage
            .subscribe(::showMessage)
        compositeDisposable += model.showActivity
            .subscribe(::showActivity)
        compositeDisposable += model.showDarkModeSelectionDialog
            .subscribe(::showDarkModeSelectionDialog)
        compositeDisposable += model.applyNightMode
            .subscribe(::applyNightMode)
    }

    private fun selectDefaultCurrency() {
        CurrencySelectionActivity.start(this, REQUEST_CODE_SELECT_DEFAULT_CURRENCY)
    }

    private fun showMessage(messageId: Int) {
        Snackbar.make(containerLayout, messageId, Snackbar.LENGTH_LONG).show()
    }

    private fun showActivity(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        requireActivity().startActivitySafely(intent)
    }

    private fun showDarkModeSelectionDialog(darkMode: DarkMode) {
        val dialogFragment = DarkModeSelectionDialogFragment.newInstance(darkMode)
        dialogFragment.onThemeSelected = { model.themeSelected(it) }
        dialogFragment.show(requireFragmentManager(), DarkModeSelectionDialogFragment.TAG)
    }

    private fun applyNightMode(nightMode: Int) {
        // Naively add short delay to make sure that all animations are finished.
        Handler().postDelayed({
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }, NIGHT_MODE_APPLICATION_DELAY)
    }

    private fun navigateToOnboarding() {
        OnboardingActivity.start(requireContext())
        requireActivity().finishAffinity()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> backSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun backSelected(): Boolean {
        requireActivity().onBackPressed()
        return true
    }

    // Results

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_SELECT_DEFAULT_CURRENCY -> {
                val currency: Currency? =
                    data?.getParcelableExtra(CurrencySelectionActivity.EXTRA_CURRENCY)
                currency?.let { model.defaultCurrencySelected(it) }
            }
        }
    }

    companion object {

        private const val REQUEST_CODE_SELECT_DEFAULT_CURRENCY = 1
        private const val NIGHT_MODE_APPLICATION_DELAY = 500L
    }
}