/*
 *   Copyright © 2023-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example

import android.app.Application
import com.pspdfkit.example.di.database
import com.pspdfkit.example.di.repository
import com.pspdfkit.example.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App() : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // inject Android context
            androidContext(applicationContext)
            koin.loadModules(listOf(viewModel, database, repository))
        }
    }
}
