/*
 *   Copyright © 2019-2024 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

plugins {
    id("com.android.application")
}

android {
    namespace = "com.pspdfkit.example"
    compileSdk = 34

    defaultConfig {
        applicationId = namespace
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // This is used to inject the PSPDFKit license key directly into your app's AndroidManifest.xml file.
        // Doing so will automatically initialize PSPDFKit during the app startup. Replace the
        // LICENSE_KEY_GOES_HERE placeholder with your personal PSPDFKit license which you can find in
        // your customer portal at https://customers.pspdfkit.com or keep this unchanged to run PSPDFKit as a trial.
        manifestPlaceholders["pspdfkitLicenseKey"] = "LICENSE_KEY_GOES_HERE"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        warningsAsErrors = true
        disable.add("GradleDependency")
    }
}

dependencies {

    // PSPDFKit is integrated from the PSPDFKit Maven repository.
    implementation("com.pspdfkit:pspdfkit:2024.3.0")

}
