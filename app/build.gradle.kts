/*
 *   Copyright © 2019-2025 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

plugins {
    id("com.android.application")
}

android {
    namespace = "com.pspdfkit.example"
    compileSdk = 35

    defaultConfig {
        applicationId = namespace
        minSdk = 21
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0"

        // This is used to inject the Nutrient license key directly into your app's AndroidManifest.xml file.
        // Doing so will automatically initialize Nutrient during the app startup. Replace the
        // LICENSE_KEY_GOES_HERE placeholder with your personal Nutrient license which you can find in
        // your customer portal at https://my.nutrient.io or keep this unchanged to run Nutrient as a trial.
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

    // Nutrient is integrated from the Nutrient Maven repository. See the `repositories` block at the beginning
    // of this file, which shows how to set up the repository in your app.
    implementation("io.nutrient:nutrient:10.4.1")

}
