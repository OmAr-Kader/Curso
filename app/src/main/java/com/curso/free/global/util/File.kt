package com.curso.free.global.util

import androidx.core.net.toUri

private inline val String.removePre: String
    get() = replace("/root", "").replace("/external_files", "")

fun android.content.Context.getPath(uri: android.net.Uri): String {
    return kotlin.runCatching {
        java.io.File(uri.path.toString()).let { file ->
            if (file.exists()) {
                file.absolutePath
            } else {
                java.io.File(uri.encodedPath.toString()).let { a ->
                    if (a.exists()) {
                        a.absolutePath
                    } else {
                        java.io.File(java.net.URLEncoder.encode(uri.path, "UTF-8")).absolutePath.run {
                            java.net.URLDecoder.decode(this@run, "UTF-8").removePre
                        }.let {
                            if (java.io.File(it).exists()) {
                                it
                            } else {
                                getPathTwo(uri)
                            }
                        }
                    }
                }
            }
        }
    }.getOrElse {
        getPathTwo(uri)
    }
}

private fun android.content.Context.getPathTwo(uri: android.net.Uri): String = run {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && uri.path != null) {
        kotlin.runCatching {
            java.net.URLDecoder.decode(uri.toString(), "UTF-8").split("/root").getOrNull(1)?.removePre?.let { p ->
                if (java.io.File(p).exists()) {
                    p
                } else {
                    getPathFromUri(uri)
                }
            } ?: getPathFromUri(uri)
        }.getOrElse {
            getPathFromUri(uri)
        }
    } else {
        getPathFromUri(uri)
    }
}

private fun android.content.Context.getPathFromUri(uri: android.net.Uri): String = run {
    if (android.provider.DocumentsContract.isDocumentUri(this@getPathFromUri, uri)) {
        if (uri.isExternalStorageDocument) {
            val docId = android.provider.DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return@run android.os.Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (uri.isDownloadsDocument) {
            val id = android.provider.DocumentsContract.getDocumentId(uri)
            val contentUri: android.net.Uri = android.content.ContentUris.withAppendedId(
                "content://downloads/public_downloads".toUri(),
                java.lang.Long.valueOf(
                    id
                )
            )
            return@run getDataColumn(contentUri, null, null)
        } else if (uri.isMediaDocument) {
            val docId = android.provider.DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: android.net.Uri? = null
            when (type) {
                "image" -> {
                    contentUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "ao" -> {
                    contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return@run getDataColumn(contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return@run if (uri.isGooglePhotosUri) uri.lastPathSegment else getDataColumn(
            uri,
            null,
            null
        )
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return@run uri.path
    }
    return@run uri.path.toString()
}?.let {
    if (java.io.File(it).exists()) {
        it
    } else {
        it.removePre
    }
} ?: uri.path.toString().let {
    if (java.io.File(it).exists()) {
        it
    } else {
        it.removePre
    }
}

private fun android.content.Context.getDataColumn(
    uri: android.net.Uri?,
    selection: String?,
    selectionArgs: Array<String>?,
): String? = runCatching {
    val column = android.provider.MediaStore.Files.FileColumns.DATA
    val projection = arrayOf(column)
    return@runCatching contentResolver.query(
        uri!!,
        projection,
        selection,
        selectionArgs,
        null
    )?.use {
        return@use if (it.moveToFirst()) {
            val index: Int = it.getColumnIndexOrThrow(column)
            it.getString(index)
        } else {
            null
        }
    }
}.getOrNull()

@Suppress("SpellCheckingInspection")
inline val android.net.Uri.isExternalStorageDocument: Boolean
    get() {
        return "com.android.externalstorage.documents" == authority
    }

inline val android.net.Uri.isDownloadsDocument: Boolean
    get() {
        return "com.android.providers.downloads.documents" == authority
    }

inline val android.net.Uri.isMediaDocument: Boolean
    get() {
        return "com.android.providers.media.documents" == authority
    }

inline val android.net.Uri.isGooglePhotosUri: Boolean
    get() {
        return "com.gg.android.apps.photos.content" == authority
    }
