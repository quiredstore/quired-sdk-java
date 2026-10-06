## Quired SDK for Java
Java SDK for the Quired REST API.

## Installation

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.quiredstore</groupId>
    <artifactId>quired-sdk-java</artifactId>
    <version>version</version>
</dependency>
```

Gradle:
```gradle
repositories {
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.quiredstore:quired-sdk-java:version'
}
```

## Usage
```java
package org.example;

import store.quired.api.QuiredClient;

public class Main {
    static void main(String[] args) {
        // init
        QuiredClient client = QuiredClient.builder()
                .apiKey("Your API Key")
                .build();

        // get shop information
        var shop = client.shop().get();
        System.out.println("Shop: " + shop.name());

        // get products
        var products = client.products().list();

        products.items().forEach(product ->
                System.out.println(
                        product.id() + " | " +
                                product.title() + " | " +
                                product.price()
                )
        );
    }
}
```
