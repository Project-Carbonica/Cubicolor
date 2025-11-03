 re# Getting Started

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven {
        url = uri("https://nexus.example.com/repository/maven-releases/")
    }
}

dependencies {
    // Core functionality
    implementation("net.cubizor.cubicolor:cubicolor-core:1.0.0")

    // Optional: Text styling
    implementation("net.cubizor.cubicolor:cubicolor-text:1.0.0")

    // Optional: Bukkit/Minecraft
    implementation("net.cubizor.cubicolor:cubicolor-bukkit:1.0.0")

    // Optional: JSON theme loading
    implementation("net.cubizor.cubicolor:cubicolor-exporter:1.0.0")

    // Optional: Multi-plugin management
    implementation("net.cubizor.cubicolor:cubicolor-manager:1.0.0")
}
```

## Basic Usage

### Creating a Color Scheme

```java
import net.cubizor.cubicolor.core.*;

ColorScheme dark = new ColorSchemeBuilderImpl("dark")
    .primary(ColorFactoryImpl.fromHex("#6200EE"))
    .secondary(ColorFactoryImpl.fromHex("#03DAC6"))
    .background(ColorFactoryImpl.fromHex("#121212"))
    .text(ColorFactoryImpl.fromHex("#FFFFFF"))
    .build();
```

### Using Colors

```java
Color primary = dark.getPrimary().orElse(Colors.WHITE);
String hex = primary.toHex();  // "#6200EE"
```

## Loading from JSON

See [json-themes.md](json-themes.md) for JSON theme format and loading.

## Multi-Plugin Management

See [manager.md](manager.md) for sharing ColorSchemes across multiple plugins.
