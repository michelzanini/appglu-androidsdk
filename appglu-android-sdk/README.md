AppGlu Android SDK
==================

AppGlu SDK for Android devices. Requires Android 2.2 (API version 8) or above.
It simplifies the use of the AppGlu Java Client library on Android and adds asynchronous calls to APIs using AsyncTask.
Moreover, it adds Android specific functionality to APIs such as Analytics, Sync and Storage.

# Dependencies

* [Spring Android REST Template](http://www.springsource.org/spring-android) (spring-android-rest-template-1.0.x.jar and spring-android-core-1.0.x.jar)
* [Jackson Json Parser](http://jackson.codehaus.org) (jackson-mapper-asl-1.9.x.jar and jackson-core-asl-1.9.x.jar)
* [Event Bus](https://github.com/greenrobot/EventBus) (eventbus-2.0.x.jar)

# Setup

## Using Maven

```xml
<dependency>
    <groupId>com.appglu</groupId>
    <artifactId>appglu-android-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Maven and Google Cloud Message (GCM) dependency

If you are using push notifications and your project uses Maven, then you will need to add the GCM jar to your Maven repository, because GCM is not yet available on Maven central.
You can download the GCM library using Android's SDK manager and when you have the JAR file available you can deploy it to your local Maven repository with this command:

```sh
mvn install:install-file -Dfile=/path/to/gcm-jar/gcm.jar -DgroupId=com.google.android.gcm -DartifactId=gcm-client -Dversion=r3 -Dpackaging=jar
```

## Manually

1. Download the AppGlu Android SDK distribution bundle (appglu-android-sdk-1.0.0-dist.zip) and extract it.
2. Include all JAR files present on the libs folder to your classpath.


# Getting Started

```java
AppGluSettings settings = new AppGluSettings("appKey", "appSecret");
AppGlu.initialize(this, settings);

Row row = AppGlu.crudApi().read("table", "id");
```

# Documentation

For more information about the available APIs and additional documentation:

[Integration Guide](https://github.com/appglu/appglu-androidsdk/wiki/Android-SDK-Integration-Guide)

[Javadocs](http://appglu.github.com/appglu-androidsdk/javadoc/index.html)




