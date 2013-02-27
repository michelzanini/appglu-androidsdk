AppGlu Android SDK
==================

AppGlu SDK for Android devices. Requires Android 2.2 (API version 8) or above.
This SDK is build on top of AppGlu Java Client adding Android specific funcionality to APIs such as Analytics, Sync and Storage.

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

1. Download and add the AppGlu Android SDK (appglu-android-sdk-1.0.0.jar) and AppGlu Java Client (appglu-java-client-1.0.0.jar) JARs to your classpath.
2. Download and add the following dependencies:

* [Spring Android REST Template](http://www.springsource.org/spring-android) (add spring-android-rest-template-1.0.x.jar and spring-android-core-1.0.x.jar)
* [Jackson Json Parser](http://jackson.codehaus.org) (add jackson-mapper-asl-1.9.x.jar and jackson-core-asl-1.9.x.jar)

# Getting Started

```java
AppGluSettings settings = new AppGluSettings("https://api.appglu.com", "appKey", "appSecret");
AppGlu.initialize(this, settings);

Row row = AppGlu.crudApi().read("table", "id");
```




