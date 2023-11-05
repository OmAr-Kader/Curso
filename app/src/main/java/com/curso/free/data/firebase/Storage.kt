package com.curso.free.data.firebase

import com.curso.free.data.util.loggerError

suspend fun com.google.firebase.FirebaseApp.upload(
    uri: android.net.Uri,
    nameFile: String,
    invoke: (String) -> Unit,
    failed: () -> Unit,
): Unit = kotlinx.coroutines.coroutineScope {
    com.google.firebase.storage.FirebaseStorage.getInstance(this@upload)
        .getReference(nameFile)
        .putFile(uri).addOnSuccessListener {
            it?.storage?.downloadUrl?.addOnSuccessListener { u ->
                invoke.invoke(u.toString())
            }?.addOnFailureListener { e ->
                loggerError("FirebaseApp.upload", e.toString())
                failed.invoke()
            } ?: failed.invoke()
        }.addOnFailureListener { e ->
            loggerError("FirebaseApp.upload", e.toString())
            failed.invoke()
        }
}

suspend fun com.google.firebase.FirebaseApp.deleteFile(
    uri: String,
): Unit = kotlinx.coroutines.coroutineScope {
    com.google.firebase.storage.FirebaseStorage.getInstance(this@deleteFile)
        .getReferenceFromUrl(uri).delete().addOnSuccessListener {}.addOnFailureListener {}
}

@androidx.compose.runtime.Composable
fun android.content.Context.filePicker(
    isImage: Boolean,
    invoke: (android.net.Uri) -> Unit,
): () -> Unit {
    val photoPicker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            invoke.invoke(it)
        }
    }
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            photoPicker.launch(
                androidx.activity.result.PickVisualMediaRequest(
                    if (isImage)
                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
                    else
                        androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
                )
            )
        }
    }
    return androidx.compose.runtime.remember {
        return@remember {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                if (isImage) android.Manifest.permission.READ_MEDIA_IMAGES else android.Manifest.permission.READ_MEDIA_VIDEO
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }.let {
                androidx.core.content.ContextCompat.checkSelfPermission(
                    this@filePicker,
                    it
                ).let { per ->
                    if (per == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        photoPicker.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                if (isImage)
                                    androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
                                else
                                    androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
                            )
                        )
                    } else {
                        launcher.launch(it)
                    }
                }
            }
        }
    }
}

/*
implementation("com.pcloud.sdk:java-core:1.8.1")
//implementation("com.pcloud.sdk:core-ktx:1.8.0")
implementation("com.pcloud.sdk:android:1.8.1")
suspend fun auth(): com.pcloud.sdk.ApiClient = kotlinx.coroutines.coroutineScope {
    val token = System.getenv("token")
    return@coroutineScope com.pcloud.sdk.PCloudSdk.newClientBuilder()
        .authenticator(com.pcloud.sdk.Authenticators.newOAuthAuthenticator(token))
        .create()
    //apiClient.userInfo.execute()
}

suspend fun android.content.Context.upload(
    apiClient: com.pcloud.sdk.ApiClient,
    name: String,
    uri: android.net.Uri
): com.pcloud.sdk.RemoteFile? = kotlinx.coroutines.coroutineScope {
    val file = java.io.File(getPath(uri))
    logger("pCloud", file.isFile.toString())
    if (!file.isFile) {
        return@coroutineScope null
    }
    return@coroutineScope apiClient.createFile(
        com.pcloud.sdk.RemoteFolder.ROOT_FOLDER_ID.toLong(), name + file.name,
        com.pcloud.sdk.DataSource.create(file), java.util.Date(file.lastModified())
    ) { done, total -> logger("pCloud", "\rUploading... %.1f\n" + done.toDouble() / total.toDouble() * 100.0) }.execute().also {
        logger("pCloud", it.createFileLink().toString())
    }
}*/
/*
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val token = System.getenv("")
        val apiClient: ApiClient = PCloudSdk.newClientBuilder()
            .authenticator(Authenticators.newOAuthAuthenticator(token))
            .create()
        try {
            val userInfo: UserInfo = apiClient.userInfo.execute()
            /*val folder: RemoteFolder = apiClient.listFolder(RemoteFolder.ROOT_FOLDER_ID.toLong()).execute()
            folder.children()
                .get(0)
                .copy(folder)
                .rename("a new File")
                .delete()
            val newFile: RemoteFile = uploadData(apiClient)
            val downloadLink: FileLink = apiClient.createFileLink(newFile, DownloadOptions.DEFAULT).execute()
            logger("pCloud", downloadLink.bestUrl().toString())*/
            val bigFile: RemoteFile = uploadFile(apiClient, File("some file path"))
            logger("pCloud", bigFile.createFileLink().toString())
            //val file = downloadFile(bigFile, File("some directory path"))
            //logger("pCloud", "File name: %s | File last modified %s" + file.name + file.lastModified())
            logger("pCloud", " User email: %s | Total quota %s | Used quota %s " + userInfo.email() + userInfo.totalQuota() + userInfo.usedQuota())
        } catch (e: IOException) {
            e.printStackTrace()
            apiClient.shutdown()
        } catch (e: ApiError) {
            e.printStackTrace()
            apiClient.shutdown()
        }
    }

    @Throws(IOException::class, ApiError::class)
    private fun uploadData(apiClient: ApiClient): RemoteFile {
        val someName = UUID.randomUUID().toString()
        val fileContents = someName.toByteArray()
        return apiClient.createFile(RemoteFolder.ROOT_FOLDER_ID.toLong(), "$someName.txt", com.pcloud.sdk.DataSource.create(fileContents)).execute()
    }

    @Throws(IOException::class, ApiError::class)
    private fun uploadFile(apiClient: ApiClient, file: File): RemoteFile {
        return apiClient.createFile(
            RemoteFolder.ROOT_FOLDER_ID.toLong(), file.name,
            com.pcloud.sdk.DataSource.create(file), java.util.Date(file.lastModified())
        ) { done, total -> logger("pCloud","\rUploading... %.1f\n" + done.toDouble() / total.toDouble() * 100.0) }.execute()
    }

    @Throws(IOException::class, ApiError::class)
    private fun downloadFile(remoteFile: RemoteFile, folder: File): File {
        val destination = File(folder, remoteFile.name())
        remoteFile.download(com.pcloud.sdk.DataSink.create(destination)) { done, total ->
            logger(
                "pCloud",
                "\rDownloading... %.1f\n" + done.toDouble() / total.toDouble() * 100.0
            )
        }
        return destination
    }
}*/
/*
import com.citrix.sharefile.api.exceptions.SFSDKException
import com.citrix.sharefile.api.https.upload.SFUploadRunnable
import com.citrix.sharefile.api.models.SFUploadRequestParams
import com.google.gson.stream.JsonReader

const val SHARE_FILE_CLIENT_ID: String = ""
const val SHARE_FILE_CLIENT_SECRET: String = ""
const val SHARE_FILE_REDIRECT_URI: String = ""
const val SHARE_FILE_DOMAIN: String = ""
const val SHARE_FILE_HOST: String = ""
const val SHARE_FILE_USER_NAME: String = ""
const val SHARE_FILE_PASSWORD: String = ""
const val SHARE_FILE_FOLDER: String = ""

//https://nightrider.sf-api.eu/sf/v3/
const val token: String = ""
suspend fun auth(): com.citrix.sharefile.api.interfaces.ISFApiClient = kotlinx.coroutines.coroutineScope {
    val oAuthService = com.citrix.sharefile.api.authentication.SFOAuthService()
    return@coroutineScope oAuthService.authenticate(
        SHARE_FILE_DOMAIN,
        SHARE_FILE_HOST,
        SHARE_FILE_USER_NAME,
        SHARE_FILE_PASSWORD
    ).let {

        logger("provideShareFileApiClient", it?.refreshToken.toString())
        logger("provideShareFileApiClient", it?.toString().toString())
        return@let com.citrix.sharefile.api.SFApiClient(it)
    }
}

suspend fun android.content.Context.upload(
    apiClient: com.citrix.sharefile.api.interfaces.ISFApiClient,
    name: String,
    uri: android.net.Uri
) = kotlinx.coroutines.coroutineScope {
    val progressListener = object : SFUploadRunnable.IUploadProgress {
        override fun bytesTransfered(byteCount: Long, chunkIndex: Long, previousUploadedByteOffset: Long) {

        }

        override fun bytesTransfered(bytesTrasnfered: Long) {}
        override fun onError(e: SFSDKException, bytesTrasnfered: Long) {
            logger("uploadEE", bytesTrasnfered.toString())
            logger("uploadEEE", e.stackTraceToString())
        }
        override fun onComplete(bytesTrasnfered: Long, itemId: String?) {
            logger("upload", bytesTrasnfered.toString())
            logger("upload", itemId.toString())
        }
    }
    JsonReader
    contentResolver.openFileDescriptor(uri, "r")?.use { file ->
        java.io.FileInputStream(file.fileDescriptor).use { inputStream ->
            val requestParams = SFUploadRequestParams()
            requestParams.fileName = name
            requestParams.details = "details"
            requestParams.fileSize = inputStream.available().toLong()
            requestParams.seturl(java.net.URI(SHARE_FILE_FOLDER))
            val uploader: SFUploadRunnable = apiClient.getUploader(requestParams, inputStream, progressListener)
            uploader.start()
        }
    }
}*/
/*suspend fun dropBox(): com.dropbox.core.v2.DbxClientV2 = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
    val DROPBOX_APP_KEY = ""
    val DROPBOX_APP_SECRET = ""
    val accessCode = ""

    val appInfo = com.dropbox.core.DbxAppInfo(DROPBOX_APP_KEY, DROPBOX_APP_SECRET)
    val config = com.dropbox.core.DbxRequestConfig.newBuilder(DROPBOX_APP_KEY).build();
    val webAuth = com.dropbox.core.DbxWebAuth(config, appInfo)
    val authFinish: com.dropbox.core.DbxAuthFinish = webAuth.finishFromCode(accessCode)
    DBxApi
    /*authFinish.accessToken
    authFinish.refreshToken
    authFinish.scope*/
    com.dropbox.core.v2.DbxClientV2(
        config,
        //authFinish.accessToken,
        authFinish.refreshToken,
        //DROPBOX_APP_KEY
    ).apply {
        refreshAccessToken().also {
            logger("uploadFileToDropbox", it.toString())
        }
    }
}*/
/*
suspend fun android.content.Context.uploadFileToDropbox(
    client: com.dropbox.core.v2.DbxClientV2,
    fileName: String,
    uri: android.net.Uri,
): String? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
    //try {
    return@withContext contentResolver.openFileDescriptor(uri, "r")?.use { file ->
        java.io.FileInputStream(file.fileDescriptor).use {
            client.files().uploadBuilder("/$fileName.jpg")
                .withMode(com.dropbox.core.v2.files.WriteMode.OVERWRITE)
                .uploadAndFinish(it)
        }.also {
            //FileMetadata.newBuilder(it.name)
            it?.fileLockInfo
        }.previewUrl
    }

    /*   } catch (e: com.dropbox.core.DbxException) {
           null
       } catch (e: java.io.IOException) {
           null
       }*/
}
*/
/*
const val DROPBOX_APP_KEY: String = ""

const val DROPBOX_APP_SECRET: String = ""

/*
suspend fun dropBox(): com.dropbox.core.v2.DbxClientV2 = kotlinx.coroutines.coroutineScope {
    com.pcloud.sdk.
    PCloudSdk.newClientBuilder()
        .authenticator(Authenticators.newOAuthAuthenticator(<your OAuth access token here>))
    // Other configuration...
    .create();
}
*/

fun ddfsd() {
    val dirent: Map.Entry<*, *> = mApi.metadata(mPath, 1000, null, true, null)

    for (ent in dirent.contents) {
        var shareAddress: String? = null
        if (!ent.isDir) {
            val shareLink: DropboxLink = mApi.share(ent.path)
            shareAddress = getShareURL(shareLink.url).replaceFirst("https://www", "https://dl")
            Log.d(TAG, "dropbox share link $shareAddress")
        }
    }
}


suspend fun android.content.Context.uploadFileToDropbox(
    client: com.dropbox.core.v2.DbxClientV2,
    fileName: String,
    uri: android.net.Uri,
): String? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
    //try {
    return@withContext contentResolver.openFileDescriptor(uri, "r")?.use { file ->
        java.io.FileInputStream(file.fileDescriptor).use {
            client.files().uploadBuilder("/$fileName.jpg")
                .withMode(com.dropbox.core.v2.files.WriteMode.OVERWRITE)
                .uploadAndFinish(it)
        }.also {
            //FileMetadata.newBuilder(it.name)
            logger("uploadFileToDropbox", it?.id.toString())
            logger("uploadFileToDropbox", it?.name.toString())
            logger("uploadFileToDropbox", it?.pathDisplay.toString())
            logger("uploadFileToDropbox", it?.previewUrl.toString())
            it?.fileLockInfo
        }.previewUrl
    }

    /*   } catch (e: com.dropbox.core.DbxException) {
           null
       } catch (e: java.io.IOException) {
           null
       }*/
}
suspend fun android.content.Context.dropBoxAuth(): Unit = kotlinx.coroutines.coroutineScope {
    DbxRequestConfig.newBuilder("Curso2023")
        .withHttpRequestor(OkHttp3Requestor(OkHttp3Requestor.defaultOkHttpClient()))
        .build()/*.let { requestConfig ->
            Auth.startOAuth2PKCE(
                context = this@dropBoxAuth,
                appKey = DROPBOX_APP_KEY,
                requestConfig = requestConfig,
                host = null,
            )
        }*/
}
*/
/*
val aws_version = "2.73.0"
implementation ("com.amazonaws:aws-android-sdk-s3:$aws_version")
implementation ("com.amazonaws:aws-android-sdk-mobile-client:$aws_version") {
    isTransitive = true
}
implementation ("com.amplifyframework:core:2.14.0")
coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")
implementation ("com.amplifyframework:aws-storage-s3:2.14.0")
implementation ("com.amplifyframework:aws-auth-cognito:2.14.0")*/
/*
//Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy Unrestricted
    //amplify init
    private fun android.content.Context.intiAmplify(){
        try {
            com.amplifyframework.core.Amplify.addPlugin(com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin())
            com.amplifyframework.core.Amplify.addPlugin(com.amplifyframework.storage.s3.AWSS3StoragePlugin())
            com.amplifyframework.core.Amplify.configure(applicationContext)
            com.curso.free.data.util.loggerError("MyAmplifyApp", "Initialized Amplify")
        } catch (error: com.amplifyframework.AmplifyException) {
            com.curso.free.data.util.loggerError("MyAmplifyApp", "Could not initialize Amplify ${error.stackTraceToString()}")
        }
    }
suspend fun uploadFile(file: java.io.File, invoke: (java.net.URL) -> Unit) = kotlinx.coroutines.coroutineScope {
    com.amplifyframework.core.Amplify.Storage.uploadFile(
        System.currentTimeMillis().toString(),
        file,
        com.amplifyframework.storage.options.StorageUploadFileOptions.builder().accessLevel(
            com.amplifyframework.storage.StorageAccessLevel.PUBLIC
        ).build(), {
            com.amplifyframework.core.Amplify.Storage.getUrl(it.key, { uri ->
                invoke.invoke(uri.url)
            }, {

            })
            logger("MyAmplifyApp", "Successfully uploaded: ${it.key}")
        }, {
            loggerError("MyAmplifyApp", "Upload failed${it.stackTraceToString()}")
        })
}*/