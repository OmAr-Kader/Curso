package com.curso.free.global.util

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
internal val Long.toString: String
    get() {
        return java.time.format.DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm"
        ).withZone(java.time.ZoneId.systemDefault()).withLocale(java.util.Locale.getDefault()).format(java.time.Instant.ofEpochMilli(this@toString))
    }


internal val Long.toStr: String
    get() {
        return java.time.format.DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm"
        ).withZone(java.time.ZoneId.systemDefault()).withLocale(java.util.Locale.getDefault()).format(java.time.Instant.ofEpochMilli(this@toStr))
    }

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
internal val android.content.Context.imageBuildr: @androidx.compose.runtime.Composable (String) -> coil.request.ImageRequest
    get() = {
        coil.request.ImageRequest.Builder(this@imageBuildr)
            .data(it)
            .diskCacheKey(it)
            .networkCachePolicy(coil.request.CachePolicy.ENABLED)
            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }


val videoItem: (String, String) -> io.sanghun.compose.video.uri.VideoPlayerMediaItem
    get() = { videoUri, videoTitle ->
        io.sanghun.compose.video.uri.VideoPlayerMediaItem.NetworkMediaItem(
            url = videoUri,
            mediaMetadata = androidx.media3.common.MediaMetadata.Builder().setSubtitle(videoTitle).setAlbumTitle(videoTitle)
                .setDisplayTitle(videoTitle).build(),
            mimeType = videoUri.videoType,
        )
    }

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val videoConfig: io.sanghun.compose.video.controller.VideoPlayerControllerConfig
    get() {
        return io.sanghun.compose.video.controller.VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = true,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = true,
            showNextTrackButton = true,
            showRepeatModeButton = true,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        )
    }


val String.videoType: String
    get() = reversed().split(".").getOrNull(0)?.reversed() ?: ""

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
internal val android.content.Context.videoImageBuildr: coil.ImageLoader
    get() = coil.ImageLoader.Builder(this@videoImageBuildr)
        .components {
            add(coil.decode.VideoFrameDecoder.Factory())
        }
        .networkCachePolicy(coil.request.CachePolicy.ENABLED)
        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
        .crossfade(true)
        .build()

fun android.content.Context.getMimeType(uri: android.net.Uri): String = kotlin.runCatching {
    val extension = if (uri.scheme == android.content.ContentResolver.SCHEME_CONTENT) {
        val mime = android.webkit.MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(contentResolver.getType(uri)) ?: ""
    } else {
        android.webkit.MimeTypeMap.getFileExtensionFromUrl(android.net.Uri.fromFile(java.io.File(uri.path ?: return@runCatching "")).toString())
    }
    return@runCatching ".$extension"
}.getOrDefault("")