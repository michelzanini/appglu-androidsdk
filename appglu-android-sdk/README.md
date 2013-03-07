AppGlu Android SDK
==================

AppGlu SDK for Android devices. Requires Android 2.2 (API version 8) or above.
It simplifies the use of the AppGlu Java Client library on Android and adds asynchronous calls to APIs using AsyncTask.
Moreover, it adds Android specific functionality to APIs such as Analytics, Sync and Storage.

# Dependencies

* [Spring Android REST Template](http://www.springsource.org/spring-android) (spring-android-rest-template-1.0.x.jar and spring-android-core-1.0.x.jar)
* [Jackson Json Parser](http://jackson.codehaus.org) (jackson-mapper-asl-1.9.x.jar and jackson-core-asl-1.9.x.jar)

# Setup

## Using Maven

```xml
<dependency>
    <groupId>com.appglu</groupId>
    <artifactId>appglu-android-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
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




