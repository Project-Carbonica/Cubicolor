plugins {
    id("java")
    id("pl.allegro.tech.build.axion-release") version "1.21.0"
}

// Axion-release yapılandırması (GitHub Actions ile otomatik versiyonlama)
scmVersion {
    tag {
        prefix.set("v")
        versionSeparator.set("")
    }

    versionCreator { versionFromTag, position ->
        val baseVersion = versionFromTag.split("-").first()
        if (position.branch == "main" || position.branch == "master") {
            baseVersion
        } else {
            "$baseVersion-${position.branch}-SNAPSHOT"
        }
    }

    // GitHub Actions için ayarlar
    checks {
        // CI ortamında uncommitted changes kontrolünü devre dışı bırak
        uncommittedChanges.set(false)
    }
}

group = "net.cubizor.cubicolor"
version = scmVersion.version

// Alt projeler için ortak yapılandırma
subprojects {
    version = rootProject.version
    group = rootProject.group
}
