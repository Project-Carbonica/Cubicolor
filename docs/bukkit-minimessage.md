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

## Example Usage

```java
public void sendWelcome(Player player) {
    Component message = MiniMessageFormatter.format(
        """
        <primary><bold>Welcome!</bold></primary>
        <success>✓</success> <text>Connected</text>
        """,
        player
    );
    player.sendMessage(message);
}
```

## Additional Resources

- **[Manager Module](manager.md)** - ColorSchemeProvider and namespace management
- **[Getting Started](getting-started.md)** - Basic setup
- **[Modules](modules.md)** - All modules
