/*
 *   Copyright © 2019-2021 PSPDFKit GmbH. All rights reserved.
 *
 *   The PSPDFKit Sample applications are licensed with a modified BSD license.
 *   Please see License for details. This notice may not be removed from this file.
 */

package com.pspdfkit.example;

import android.content.Context;

import androidx.annotation.NonNull;

import com.pspdfkit.document.download.DownloadJob;
import com.pspdfkit.document.download.DownloadRequest;
import com.pspdfkit.document.download.source.AssetDownloadSource;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Helper class for asynchronously pulling a PDF document from the app's assets into the internal device storage.
 */
@SuppressWarnings("SameParameterValue")
public final class ExtractAssetTask {
    /**
     * Extracts the file at {@code assetPath} from the app's assets into the private app directory.
     *
     * @param assetPath         Path pointing to a file inside the app's assets.
     * @param context           Context used to retrieve the referenced file from the app's assets.
     * @return Single emitting the extracted file.
     */
    @NonNull
    static Single<File> extractAsync(@NonNull final String assetPath,
                                     @NonNull final Context context) {
        return Single.<File>create((emitter) -> {
            final File  outputFile = new File(context.getFilesDir(), assetPath);
            final DownloadRequest request = new DownloadRequest.Builder(context)
                .source(new AssetDownloadSource(context, assetPath))
                .outputFile(outputFile)
                .overwriteExisting(false)
                .build();
            final DownloadJob job = DownloadJob.startDownload(request);
            job.setProgressListener(new DownloadJob.ProgressListenerAdapter() {
                @Override
                public void onComplete(@NonNull final File output) {
                    emitter.onSuccess(output);
                }

                @Override
                public void onError(@NonNull final Throwable exception) {
                    super.onError(exception);
                    emitter.tryOnError(exception);
                }
            });
            emitter.setCancellable(job::cancel);
        }).subscribeOn(Schedulers.io());
    }
}
