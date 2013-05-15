# AppGlu Java Client

A Java client library to access the AppGlu REST API. 
Any Java application running on a JVM can be integrated with AppGlu using this library including java web applications (running on Tomcat, JBoss, etc...) or even a stand alone application running on a command line.

If you want to integrate AppGlu with an Android application please check out the [Android SDK](https://github.com/appglu/appglu-androidsdk/tree/master/appglu-android-sdk) that provides additional Android specific functionality.

# Dependencies

* [Spring REST Template](http://www.springsource.org/spring-framework) (spring-web-3.2.x.jar and its dependencies)
* [Jackson Json Parser](http://jackson.codehaus.org) (jackson-mapper-asl-1.9.x.jar and its dependencies)

# Setup

1. Download the AppGlu Java Client distribution bundle [from here](http://appglu.github.io/appglu-androidsdk/download/1.0.0/appglu-java-client-1.0.0-dist.zip) and extract it.
2. Include all JAR files present on the lib folder to your classpath.

# Getting Started

```java
AppGluTemplate appGluTemplate = new AppGluTemplate("appKey", "appSecret");
Row row = appGluTemplate.crudOperations().read("table", "id");
```

### Documentation

For more information about the available APIs and additional documentation:
  
[Javadocs](http://appglu.github.com/appglu-androidsdk/javadoc/index.html)
