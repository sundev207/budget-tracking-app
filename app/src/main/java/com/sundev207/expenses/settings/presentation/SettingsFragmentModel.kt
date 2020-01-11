package com.sundev207.expenses.settings.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sundev207.expenses.Application
import com.sundev207.expenses.BuildConfig
import com.sundev207.expenses.R
import com.sundev207.expenses.authentication.AuthenticationManager
import com.sundev207.expenses.common.presentation.Theme
import com.sundev207.expenses.data.model.Currency
import com.sundev207.expenses.data.preference.PreferenceDataSource
import com.sundev207.expenses.util.reactive.DataEvent
import com.sundev207.expenses.util.reactive.Event
import com.sundev207.expenses.util.reactive.Variable
import io.reactivex.disposables.CompositeDisposable

class SettingsFragmentModel(
    application: Application,
    private val preferenceDataSource: PreferenceDataSource,
    private val authenticationManager: AuthenticationManager
) : AndroidViewModel(application) {

    val itemModels = Variable(emptyList<SettingItemModel>())

    val selectDefaultCurrency = Event()
    val navigateToOnboarding = Event()

    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()
    val showThemeSelectionDialog = DataEvent<Theme>()
    val applyTheme = DataEvent<Theme>()

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        loadItemModels()
    }

    private fun loadItemModels() {
        itemModels.value =
            createAccountSection() + createApplicationSection() + createAboutSection()
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

        return ActionSettingItemModel(title).apply {
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

    // Application section

    private fun createApplicationSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createApplicationHeader(context)
        itemModels += createDefaultCurrency(context)
        itemModels += createDarkMode(context)

        return itemModels
    }

    private fun createApplicationHeader(context: Context): SettingItemModel =
        SettingsHeaderModel(context.getString(R.string.application))

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

    private fun createDarkMode(context: Context): SettingItemModel {
        val title = context.getString(R.string.theme)

        val darkMode = preferenceDataSource.getTheme(context)

        val summary = when (darkMode) {
            Theme.LIGHT -> context.getString(R.string.light)
            Theme.DARK -> context.getString(R.string.dark)
            Theme.SYSTEM_DEFAULT -> context.getString(R.string.system_default)
        }

        return SummaryActionSettingItemModel(title, summary).apply {
            click = { showThemeSelectionDialog.next(darkMode) }
        }
    }

    // About section

    private fun createAboutSection(): List<SettingItemModel> {
        val context = getApplication<Application>()

        val itemModels = mutableListOf<SettingItemModel>()
        itemModels += createAboutHeader(context)
        itemModels += createContactMe(context)
        itemModels += createRateApp(context)
        itemModels += createViewSourceCode(context)
        itemModels += createPrivacyPolicy(context)
        itemModels += createVersion(context)

        return itemModels
    }

    private fun createAboutHeader(context: Context): SettingItemModel =
        SettingsHeaderModel(context.getString(R.string.about))

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

    fun defaultCurrencySelected(defaultCurrency: Currency) {
        getApplication<Application>().let {
            preferenceDataSource.setDefaultCurrency(it, defaultCurrency)
        }

        loadItemModels()
    }

    fun themeSelected(theme: Theme) {
        getApplication<Application>().let {
            preferenceDataSource.setTheme(it, theme)
        }

        loadItemModels()

        applyTheme.next(theme)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SettingsFragmentModel(
                application,
                application.preferenceDataSource,
                application.authenticationManager
            ) as T
        }
    }

    companion object {

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