package com.sundev207.expenses.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sundev207.expenses.R
import com.sundev207.expenses.home.presentation.HomeActivity
import com.sundev207.expenses.util.extensions.application
import com.sundev207.expenses.util.extensions.plusAssign
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_onboarding.*

class OnboardingFragment : Fragment() {

    private lateinit var model: OnboardingFragmentModel

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupModel()
        bindModel()
    }

    private fun setupListeners() {
        buttonContinueWithGoogle.setOnClickListener {
            model.continueWithGoogleRequested()
        }
    }

    private fun setupModel() {
        val factory = OnboardingFragmentModel.Factory(requireContext().application)
        model = ViewModelProviders.of(this, factory).get(OnboardingFragmentModel::class.java)
    }

    private fun bindModel() {
        compositeDisposable += model.requestGoogleSignIn
            .toObservable()
            .subscribe { requestGoogleSignIn(it) }
        compositeDisposable += model.showTestVersionPromptAndNavigateHome
            .toObservable()
            .subscribe { showTestVersionPromptAndNavigateHome() }
    }

    private fun requestGoogleSignIn(intent: Intent) {
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    private fun showTestVersionPromptAndNavigateHome() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.test_version_prompt_title)
            .setMessage(R.string.test_version_prompt_message)
            .setPositiveButton(R.string.ok) { _, _ -> navigateToHome() }
            .setOnCancelListener {navigateToHome()  }
            .create()
            .show()
    }

    private fun navigateToHome() {
        HomeActivity.start(requireContext())
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindModel()
    }

    private fun unbindModel() {
        compositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN && data != null) {
            model.handleGoogleSignInResult(data)
        }
    }

    companion object {
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 1
    }
}