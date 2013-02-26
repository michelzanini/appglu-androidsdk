AppGlu Android SDK
==================

AppGlu SDK for Android devices. Requires Android 2.2 (API version 8) or above.
This SDK is build on top of AppGLu Java Client adding Android specific funcionality to APIs such as Analytics, Syncronization and Storage.

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

1. Download and add the AppGlu Android SDK and AppGlu Java Client JARs to your classpath.
2. Download and add the following dependencies:

* [Spring Android REST Template](http://www.springsource.org/spring-android) (add spring-android-rest-template-1.0.x.jar and its dependencies)
* [Jackson Json Parser](http://jackson.codehaus.org) (add jackson-mapper-asl-1.9.x.jar and its dependencies)

# Getting Started

```java
AppGluSettings settings = new AppGluSettings("https://api.appglu.com", "appKey", "appSecret");
AppGlu.initialize(this, settings);

Row row = AppGlu.crudApi().read("table", "id");
```




