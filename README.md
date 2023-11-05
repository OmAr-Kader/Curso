
# Curso Demo

Curso is an education technology applicaion with Chat Feature that provides an online learning and teaching platform.
Students take courses primarily to improve job-related skills Some courses generate credit toward technical certification
This is an Android application that allows users to enroll in courses and lecturer articles. The application uses Realm Cloud to store user data and course information. Firebase Cloud Messaging is used to send push notifications to users when new course materials are available. Firebase Storage is used to store course materials such as articles, videos and images. Dropbox is used to backup user data and course materials. Amazon AWS Storage is used to store large files such as videos.

## Getting Started

To get started with this application, you will need to have Android Studio installed on your computer. You will also need to create accounts with Realm Cloud, Firebase, Dropbox, and Amazon AWS.


## Dependencies

- Realm Cloud: Provides cloud-based data storage.
- Firebase Cloud Messaging: Provides push notification services.
- Retrofit: Provides REST API for push notifications.
- Firebase Storage: Provides cloud storage services for videos and images.
- Dropbox: Provides cloud storage services for videos and images.
- pCloud: Provides cloud storage services for videos and images.
- Share File: Provides cloud storage services for videos and images.
- Amazon AWS: Provides cloud storage services for videos and images.
- Dagger Hilt: Provides dependency injection.

## Configuration

### Realm Cloud

1. Create a Realm Cloud account.
2. Create a new Realm app.
3. Add the following to your project-level `build.gradle` file:

```gradle
plugins {
    id("io.realm.kotlin") version "1.12.0" apply false
}

```

4. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation("io.realm.kotlin:library-base:1.12.0")
    implementation("io.realm.kotlin:library-sync:1.12.0")
}
```
### Firebase Cloud Messaging

1. Follow the instructions provided by Firebase to add Firebase to your Android project.
2. Create a Firebase account.
3. Create a new Firebase project.
4. Enable Firebase Messaging.
5. Add the Firebase configuration file to the `app` directory.

6. Add the following to your project-level `build.gradle` file:

```gradle
plugins {
    id("com.google.gms.google-services") version("4.4.0") apply false
}

```
7. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.firebase:firebase-messaging-directboot:23.3.1")
}
```

8. Add the following to your app's manifest file:

```xml
<service
    android:name=".MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

9. Create a new class called `MyFirebaseMessagingService` that extends `FirebaseMessagingService`:

```kotlin
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM message here.
    }
}
```

### Firebase Storage
1. Follow the instructions provided by Firebase to add Firebase to your Android project.
2. Enable Firebase Messaging Storage .
3. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
}
```

4. Use the following code to upload a file to Firebase Storage:

```kotlin
val storage = FirebaseStorage.getInstance()
val storageRef = storage.reference
val fileRef = storageRef.child("path/to/file")
val file = Uri.fromFile(File("path/to/local/file"))
val uploadTask = fileRef.putFile(file)
```

### Dropbox

1. Follow the instructions provided by Dropbox to add Dropbox to your Android project.

2. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation("com.dropbox.core:dropbox-core-sdk:3.1.0")
}
```

3. Use the following code to upload a file to Dropbox:

```kotlin
val config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build()
val client = DbxClientV2(config, ACCESS_TOKEN)
FileInputStream("path/to/local/file").use { inputStream ->
    val metadata = client.files().uploadBuilder("/path/to/dropbox/file")
        .uploadAndFinish(inputStream)
}
```

### Amazon AWS Storage

1. Follow the instructions provided by Amazon to add Amazon AWS Storage to your Android project.

2. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation "aws.sdk.kotlin:s3:0.25.0-beta"
}
```

3. Use the following code to upload a file to Amazon AWS Storage:

```kotlin
val credentialsProvider = CognitoCachingCredentialsProvider(
    applicationContext,
    "identityPoolId",
    Regions.US_EAST_1
)

val s3Client = AmazonS3Client(credentialsProvider)

val transferUtility = TransferUtility.builder()
    .context(applicationContext)
    .awsConfiguration(AWSMobileClient.getInstance().configuration)
    .s3Client(s3Client)
    .build()

val file = File("path/to/local/file")
val transferObserver = transferUtility.upload(
    "bucketName",
    "keyName",
    file
)
```

### pCloud

1. Follow the instructions provided by pCloud to add pCloud to your Android project.

2. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation("com.pcloud.sdk:core-ktx:1.8.0")
    implementation("com.pcloud.sdk:android:1.8.1")
}
```

3. Use the following code to upload a file to pCloud:

```kotlin
PcloudSdk.init(context, "appKey", "appSecret")
PcloudAuth.startAuthActivityForResult(activity, requestCode)
```

### Share File

1. Follow the instructions provided by Share File to add Share File to your Android project.

2. Add the following to your app-level `build.gradle` file:

```gradle
dependencies {
    implementation "com.citrix.sharefile:sfandroidsdk:1.0.0"
}
```

3. Use the following code to upload a file to Share File:

```kotlin
val authManager = SFAuthenticationManager.getInstance()
authManager.login(activity, object : SFOAuthCallback {
    override fun onSuccess() {
        val file = SFSDKFile()
        file.fileName = "file.txt"
        file.filePath = "path/to/local/file"
        SFSDKFileManager.getInstance().uploadFile(file, object : SFSDKFileManager.SFSDKFileManagerCallback {
            override fun onSuccess() {
                // File uploaded successfully.
            }

            override fun onError(e: Exception) {
                // Error occurred while uploading file.
            }
        })
    }

    override fun onError(e: Exception) {
        // Error occurred while logging in.
    }
})
```

## 🔗 Links
[![Android Studio](https://img.shields.io/static/v1?style=for-the-badge&message=Android+Studio&color=222222&logo=Android+Studio&logoColor=3DDC84&label=)](https://developer.android.com/studio?gclsrc=aw.ds)

[![Compose](https://img.shields.io/static/v1?style=for-the-badge&message=Jetpack+Compose&color=4285F4&logo=Jetpack+Compose&logoColor=FFFFFF&label=)](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-getting-started.html#join-the-community)

[![Dagger Hilt](https://img.shields.io/static/v1?style=for-the-badge&message=Android&color=222222&logo=Android&logoColor=3DDC84&label=dagger%20hilt)](https://developer.android.com/training/dependency-injection/hilt-jetpack)

[![Retrofit](https://img.shields.io/badge/retrofit-44A833?style=for-the-badge&logoColor=white)](https://pub.dev/packages/retrofit)

[![MongoDB](https://img.shields.io/static/v1?style=for-the-badge&message=MongoDB&color=47A248&logo=MongoDB&logoColor=FFFFFF&label=)](https://www.mongodb.com/docs/realm/sdk/kotlin/)

[![Realm](https://img.shields.io/static/v1?style=for-the-badge&message=Realm&color=DD344C&logo=Realm&logoColor=FFFFFF&label=)](https://www.mongodb.com/docs/realm/sdk/kotlin/)

[![Firebase Storage](https://img.shields.io/static/v1?style=for-the-badge&message=Firebase&color=FFCA28&logo=Firebase&logoColor=FFCA28&label=storage)](https://firebase.google.com/docs/storage)

[![Firebase Cloud Messaging](https://img.shields.io/static/v1?style=for-the-badge&message=Firebase&color=D9411E&logo=Firebase&logoColor=FFCA28&label=messaging)](https://firebase.google.com/docs/cloud-messaging)

[![Amazon AWS Storage](https://img.shields.io/static/v1?style=for-the-badge&message=Amazon+AWS&color=232F3E&logo=Amazon+AWS&logoColor=FFFFFF&label=storage)](https://docs.aws.amazon.com/s3/)

## Screenshots

### Lecturer
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/a3e66fb9-5b3d-4735-ab7a-dc93fbccf7aa"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/ceef1c41-be61-46a4-86cc-f1312033c489"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/7ec3b0f2-3068-43e6-897e-1357e9d3107b"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/964e345e-120f-4bc3-b65b-3b27779578c6"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/7523781c-a40b-4061-ac7f-34ee78d68a31"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/fe76e41d-9def-4641-a076-e40bec80b742"  width="300" height="667"/>

### Student
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/39b6c1a5-9e93-4b6a-80ce-5d2791c20671"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/d882a859-cc0f-43b1-bdfc-bd24eb168987"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/278e1426-0101-459a-8f35-dcf2f1b653da"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/f78a304c-39a4-4305-bda6-853cc0d1ac3e"  width="300" height="667"/>

### Common
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/1013141c-eb20-489e-b48a-54159dae7ec3"  width="300" height="667"/>
<img src="https://github.com/OmAr-Kader/Curso/assets/137582672/215b9677-e377-471d-bafb-fc8c4df4c595"  width="300" height="667"/>
