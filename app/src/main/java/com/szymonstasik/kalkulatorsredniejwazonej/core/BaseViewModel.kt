package com.szymonstasik.kalkulatorsredniejwazonej.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.koin.core.component.KoinComponent

/**
 * Base VM for corresponding sub View Models to extend and take advantage of DI.
 */
abstract class BaseViewModel(appContext: Application)
    : AndroidViewModel(appContext), KoinComponent