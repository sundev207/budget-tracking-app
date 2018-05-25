package com.sundev207.expenses.ui.settings

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
import com.sundev207.expenses.infrastructure.extensions.application
import com.sundev207.expenses.infrastructure.extensions.plusAssign
import com.sundev207.expenses.ui.common.currencyselection.CurrencySelectionDialogFragment
import io.reactivex.disposables.CompositeDisposable

class SettingsFragment : Fragment() {

    companion object {

        fun newInstance() = SettingsFragment()
    }

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: SettingsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: SettingsFragmentModel

    private val compositeDisposable = CompositeDisposable()

    // Lifecycle start

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindWidgets(view)
        setupActionBar()
        setupRecyclerView()
        setupViewModel()
        subscribeViewModel()
    }

    private fun bindWidgets(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    private fun setupActionBar() {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar ?: return
        actionBar.setTitle(R.string.ui_settings_title)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_active_24dp)
        setHasOptionsMenu(true)
    }

    private fun setupRecyclerView() {
        adapter = SettingsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun setupViewModel() {
        val factory = SettingsFragmentModel.Factory(requireContext().application)
        viewModel = ViewModelProviders.of(this, factory).get(SettingsFragmentModel::class.java)
    }

    private fun subscribeViewModel() {
        compositeDisposable += viewModel.itemModels
                .toObservable()
                .subscribe(adapter::submitList)
        compositeDisposable += viewModel.showAddUserDialog
                .toObservable()
                .subscribe { showNewUserDialog() }
        compositeDisposable += viewModel.showCurrencySelectionDialog
                .toObservable()
                .subscribe { showCurrencySelectionDialog() }
    }

    private fun showNewUserDialog() {
        NewUserDialogFragment.newInstance().show(requireFragmentManager(), "NewUserDialogFragment")
    }

    private fun showCurrencySelectionDialog() {
        val dialogFragment = CurrencySelectionDialogFragment.newInstance()
        dialogFragment.onCurrencySelected = { currency -> viewModel.updateDefaultCurrency(currency) }
        dialogFragment.show(requireFragmentManager(), "CurrencySelectionDialogFragment")
    }

    // Lifecycle end

    override fun onDestroyView() {
        super.onDestroyView()
        unsubscribeViewModel()
    }

    private fun unsubscribeViewModel() {
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
}