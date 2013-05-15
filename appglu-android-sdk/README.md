AppGlu Android SDK
==================

AppGlu SDK for Android devices. Requires Android 2.2 (API version 8) or above.
It simplifies the use of the AppGlu Java Client library on Android and adds asynchronous calls to APIs using AsyncTask.
Moreover, it adds Android specific functionality such as Content Synchronization, Analytics and Push Notifications/GCM integration.

### Dependencies

* [Spring Android REST Template](http://www.springsource.org/spring-android) (spring-android-rest-template-1.0.x.jar and spring-android-core-1.0.x.jar)
* [Jackson Json Parser](http://jackson.codehaus.org) (jackson-mapper-asl-1.9.x.jar and jackson-core-asl-1.9.x.jar)
* [Event Bus](https://github.com/greenrobot/EventBus) (eventbus-2.0.x.jar)

### Setup

1. Download the AppGlu Android SDK distribution bundle [from here](http://appglu.github.io/appglu-androidsdk/download/index.html) and extract it.
2. Include all JAR files present on the libs folder to your classpath.

### Getting Started

```java
// Initialize SDK with API key and secret
AppGluSettings settings = new AppGluSettings("appKey", "appSecret");
AppGlu.initialize(this, settings);

// Start making API calls
Row row = AppGlu.crudApi().read("table", "id");
```

### Documentation

For more information about the available APIs and additional documentation:

[Integration Guide](https://github.com/appglu/appglu-androidsdk/wiki/Android-SDK-Integration-Guide)    
[Javadocs](http://appglu.github.com/appglu-androidsdk/javadoc/index.html)




