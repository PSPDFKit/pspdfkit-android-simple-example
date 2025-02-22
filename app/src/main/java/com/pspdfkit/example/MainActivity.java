/*
 *   Copyright © 2019-2025 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */
package com.pspdfkit.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.pspdfkit.Nutrient;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.configuration.page.PageFitMode;
import com.pspdfkit.configuration.page.PageScrollDirection;
import com.pspdfkit.document.download.DownloadJob;
import com.pspdfkit.document.download.DownloadProgressFragment;
import com.pspdfkit.document.download.DownloadRequest;
import com.pspdfkit.ui.PdfActivityIntentBuilder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final String DEMO_DOCUMENT_ASSET_NAME = "demo.pdf";

    /**
     * Upon launching this activity, we extract the demo PDF from the assets to the app's private
     * files. This is done only once, so that any edits performed in the document are actually
     * persisted. To see how the extraction is implemented, see {@link ExtractAssetTask}.
     */
    private @Nullable Disposable documentExtraction;

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private final ActivityResultLauncher<Intent> filePickerActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() == null) return;
                    final var uri = result.getData().getData();
                    if (uri != null) {
                        prepareAndShowDocument(uri);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button openDocumentButton = findViewById(R.id.main_btn_open_document);
        openDocumentButton.setOnClickListener(v -> launchSystemFilePicker());

        final Button openDemoDocumentButton = findViewById(R.id.main_btn_open_example);
        openDemoDocumentButton.setOnClickListener(v -> prepareAndShowDemoDocument());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final Disposable runningDocumentExtraction = documentExtraction;
        if (runningDocumentExtraction != null) {
            runningDocumentExtraction.dispose();
            documentExtraction = null;
        }
    }

    /**
     * Ensures that the given URI is openable or downloads the document before opening the document
     * using {@link #launchPdfActivity(Uri)}.
     */
    private void prepareAndShowDocument(@NonNull final Uri uri) {
        // Nutrient supports direct opening of documents from various URI locations (including
        // assets,
        // local file URIs, content provider URIs, etc.).
        if (Nutrient.isOpenableUri(this, uri)) {
            launchPdfActivity(uri);
        } else {
            // Only document accessible as files are openable directly with Nutrient so we have to
            // transfer other documents to application cache
            final DownloadRequest request =
                    new DownloadRequest.Builder(this).uri(uri).build();
            // Start download of the PDF document from the given URL in a background thread.
            final DownloadJob job = DownloadJob.startDownload(request);
            // To visualize the download progress, we use a dedicated progress fragment.
            final DownloadProgressFragment fragment = new DownloadProgressFragment();
            fragment.show(getSupportFragmentManager(), "download-fragment");
            fragment.setJob(job);
        }
    }

    /** Opens a demo document from assets directory */
    private void prepareAndShowDemoDocument() {
        // Prevent extracting the document multiple times (for example if the user hits the open
        // button twice).
        if (this.documentExtraction != null) return;
        // Extract the demo document from the assets into the app's private files directory. This is
        // done once, and allows to make changes to the document and to persist them. Every subsequent
        // call to extractAsync() will reuse the previously extracted file.
        this.documentExtraction = ExtractAssetTask.extractAsync(DEMO_DOCUMENT_ASSET_NAME, this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((documentFile) -> {
                    documentExtraction = null;
                    prepareAndShowDocument(Uri.fromFile(documentFile));
                });
    }
    /**
     * Launches the {@link com.pspdfkit.ui.PdfActivity} for showing the document located at the
     * given URI. The URI must be openable (i.e. {@link Nutrient#isOpenableUri(Context, Uri)} has to
     * return {@code true} for the URI).
     */
    private void launchPdfActivity(@NonNull final Uri uri) {
        // Configure features of the PdfActivity to launch. For an extensive set of configuration
        // options, have a look at the available builder methods.
        final PdfActivityConfiguration pspdfkitConfiguration = new PdfActivityConfiguration.Builder(
                        getApplicationContext())
                .scrollDirection(PageScrollDirection.HORIZONTAL)
                .pageNumberOverlayEnabled(true)
                .thumbnailGridEnabled(true)
                .theme(R.style.PSPDFSimple_Theme)
                .themeDark(R.style.PSPDFSimple_Theme_Dark)
                .fitMode(PageFitMode.FIT_TO_WIDTH)
                .build();
        // PdfActivity is launched like any other activity using an Intent. To create the
        // appropriate intent, you need to use the PdfActivityIntentBuilder which will handle adding all
        // relevant extras to the intent.
        final Intent intent = PdfActivityIntentBuilder.fromUri(MainActivity.this, uri)
                .configuration(pspdfkitConfiguration)
                .build();
        startActivity(intent);
    }

    /** Opens the Android file picker for selecting a PDF document to show. */
    private void launchSystemFilePicker() {
        final Intent openIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        openIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openIntent.setType("application/*");
        filePickerActivityResultLauncher.launch(openIntent);
    }
}
