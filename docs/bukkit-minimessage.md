# Bukkit MiniMessage Integration

Bukkit modülü, Cubicolor'ın ColorScheme sistemini MiniMessage ile entegre eder ve oyuncu bazlı otomatik renk çözümlemesi sağlar.

## Temel Kullanım

### ColorScheme.of(player) ile Otomatik Çözümleme

```java
import net.cubizor.cubicolor.bukkit.MiniMessageFormatter;
import net.cubizor.cubicolor.manager.ColorSchemes;
import net.kyori.adventure.text.Component;

// Oyuncuya özel ColorScheme otomatik olarak çözümlenir
Component message = MiniMessageFormatter.format(
    "<primary>Hoşgeldin!</primary> <secondary>İyi eğlenceler.</secondary>",
    player
);

player.sendMessage(message);
```

### Kullanılabilir Renk Tag'leri

- `<primary>` - Ana renk
- `<secondary>` - İkincil renk
- `<tertiary>` - Üçüncül renk
- `<accent>` - Vurgu rengi
- `<error>` - Hata rengi
- `<success>` - Başarı rengi
- `<warning>` - Uyarı rengi
- `<info>` - Bilgi rengi
- `<text>` - Metin rengi
- `<text_secondary>` - İkincil metin rengi
- `<background>` - Arka plan rengi
- `<surface>` - Yüzey rengi
- `<border>` - Kenarlık rengi
- `<overlay>` - Kaplama rengi

### Standard MiniMessage Tag'leri ile Birlikte Kullanım

```java
Component message = MiniMessageFormatter.format(
    "<primary><bold>ÖNEMLİ!</bold></primary> <warning><italic>Dikkat et</italic></warning>",
    player
);
```

## ColorSchemeProvider Kurulumu

```java
public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Oyuncu bazlı tema resolver'ı kaydet
        ColorSchemeProvider.getInstance().register(
            ColorSchemes.DEFAULT_NAMESPACE,
            context -> {
                if (context instanceof Player player) {
                    User user = getUser(player);

                    // Oyuncunun tema tercihine göre scheme döndür
                    return user.isDarkMode() ?
                        MyThemes.DARK :
                        MyThemes.LIGHT;
                }
                return MyThemes.LIGHT;
            }
        );
    }
}
```

## Namespace Desteği

Farklı plugin'ler için ayrı namespace'ler kullanabilirsiniz:

```java
// Chat plugin için özel namespace
ColorSchemeProvider.getInstance().register("chat", context -> {
    if (context instanceof Player player) {
        User user = getUser(player);
        String chatTheme = user.getChatTheme(); // "classic", "modern", vb.
        return ChatThemes.get(chatTheme);
    }
    return ChatThemes.CLASSIC;
});

// Chat namespace ile kullan
Component chatMsg = MiniMessageFormatter.format(
    "<primary>[Chat]</primary> <text>Merhaba!</text>",
    player,
    "chat"
);
```

## ComponentBuilder ile Kullanım

```java
import net.cubizor.cubicolor.bukkit.ComponentBuilder;

// ColorScheme.of(player) ile otomatik
Component component = ComponentBuilder.of(player)
    .primary("Sunucu ")
    .secondary("» ")
    .text("Mesajın", ColorRole.TEXT)
    .build();

player.sendMessage(component);
```

## Örnekler

### Örnek 1: Hoşgeldin Mesajı

```java
public void sendWelcome(Player player) {
    Component message = MiniMessageFormatter.format(
        """
        <primary><bold>Sunucuya Hoşgeldin!</bold></primary>

        <success>✓</success> <text>Bağlandın</text>
        <info>ℹ</info> <text_secondary>/help ile komutları görebilirsin</text_secondary>
        """,
        player
    );

    player.sendMessage(message);
}
```

### Örnek 2: Hata Mesajları

```java
public void sendError(Player player, String error) {
    Component message = MiniMessageFormatter.format(
        "<error><bold>HATA!</bold></error> <warning>%s</warning>".formatted(error),
        player
    );

    player.sendMessage(message);
}
```

### Örnek 3: Chat Mesajları

```java
public void broadcastChat(Player sender, String message) {
    // Her oyuncu kendi temasıyla görür
    for (Player receiver : Bukkit.getOnlinePlayers()) {
        Component formatted = MiniMessageFormatter.format(
            "<primary>[%s]</primary> <text>%s</text>".formatted(
                sender.getName(),
                message
            ),
            receiver  // Her oyuncunun kendi teması
        );
        receiver.sendMessage(formatted);
    }
}
```

### Örnek 4: Programatik ColorScheme Oluşturma

```java
import net.cubizor.cubicolor.core.Colors;

// Kod içinde tema oluştur
ColorScheme customScheme = Colors.scheme("custom")
    .primary(Colors.rgb(255, 100, 150))
    .secondary(Colors.rgb(100, 150, 255))
    .error(Colors.rgb(255, 50, 50))
    .success(Colors.rgb(50, 255, 50))
    .build();

// Direkt kullan
Component msg = MiniMessageFormatter.format(
    "<primary>Özel tema!</primary>",
    customScheme
);
```

### Örnek 5: Komut ile Tema Değiştirme

```java
@Command("theme")
public void handleTheme(Player player, String themeName) {
    // Oyuncunun temasını değiştir
    User user = getUser(player);
    user.setTheme(themeName);

    // Yeni tema ile mesaj gönder
    Component success = MiniMessageFormatter.format(
        "<success>Tema değiştirildi:</success> <primary>" + themeName + "</primary>",
        player
    );
    player.sendMessage(success);
}
```

## API Referansı

### MiniMessageFormatter

```java
// Context tabanlı (default namespace)
static Component format(String message, Object context)

// Context tabanlı (özel namespace)
static Component format(String message, Object context, String namespace)

// Direkt ColorScheme ile
static Component format(String message, ColorScheme scheme)

// Ek resolver'larla
static Component format(String message, ColorScheme scheme, TagResolver... additionalResolvers)
static Component format(String message, Object context, TagResolver... additionalResolvers)

// TagResolver oluşturma
static TagResolver resolver(ColorScheme scheme)
static TagResolver resolver(Object context)
static TagResolver resolver(Object context, String namespace)
```

### ColorSchemeTagResolver

```java
// ColorScheme'den TagResolver oluştur
static TagResolver of(ColorScheme scheme)
```

### ComponentBuilder

```java
// Context tabanlı oluşturma
static ComponentBuilder of(Object context)
static ComponentBuilder of(Object context, String namespace)

// Direkt ColorScheme ile
static ComponentBuilder with(ColorScheme scheme)
```

## Daha Fazla Bilgi

- **[Manager Modülü](manager.md)** - ColorSchemeProvider ve namespace yönetimi
- **[Getting Started](getting-started.md)** - Temel kurulum
- **[Modules](modules.md)** - Tüm modüller
