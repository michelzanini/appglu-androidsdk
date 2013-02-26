# AppGlu Java Client

A Java client library to access the AppGlu REST API. 
Any Java application running on a JVM can be integrated with AppGlu using this library including java web applications (running on Tomcat, JBoss, etc...) or even a stand alone application running on a command line.

If you want to integrate AppGlu with an Android application please check out the [Android SDK](https://github.com/appglu/appglu-androidsdk/tree/master/appglu-android-sdk) that provides additional Android specific functionality.

# Setup

## Using Maven

```xml
<dependency>
    <groupId>com.appglu</groupId>
    <artifactId>appglu-java-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Manually

1. Download and add the AppGlu Java client JAR to your classpath.
2. Download and add the following dependencies:

* [Spring REST Template](http://www.springsource.org) (add spring-web-3.2.x.jar and its dependencies)
* [Jackson Json Parser](http://jackson.codehaus.org) (add jackson-mapper-asl-1.9.x.jar and its dependencies)

# Getting Started

```java
AppGluTemplate appGluTemplate = new AppGluTemplate("https://api.appglu.com", "appKey", "appSecret");
Row row = appGluTemplate.crudOperations().read("table", "id");
```




