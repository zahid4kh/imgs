-dontwarn kotlinx.serialization.**

-dontwarn sun.font.CFont
-dontwarn sun.swing.SwingUtilities2$AATextInfo
-dontwarn net.miginfocom.swing.MigLayout

-dontnote kotlinx.serialization.**
-dontnote META-INF.**
-dontnote kotlinx.serialization.internal.PlatformKt

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep all serializable classes with their @Serializable annotation
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <fields>;
}

# Keep serializers
-keepclasseswithmembers class **$$serializer {
    static **$$serializer INSTANCE;
}


# Keep serializable classes and their properties
-if @kotlinx.serialization.Serializable class **
-keep class <1> {
    static <1>$Companion Companion;
}

# Keep specific serializer classes
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serialization descriptors
-keep class kotlinx.serialization.descriptors.** { *; }

# SLF4J
-dontwarn org.slf4j.**
-dontnote org.slf4j.**

# OkHttp's optional platform integrations (Android/Conscrypt/BouncyCastle/OpenJSSE)
# are not present on desktop JVM and are never reached at runtime.
-dontwarn okhttp3.internal.platform.**
-dontwarn android.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.openjsse.**
-dontnote dalvik.system.CloseGuard

-keepdirectories META-INF/services/
-keep class META-INF.services.** { *; }

# Sketch loads Fetcher/Decoder implementations reflectively via ServiceLoader
# (see META-INF/services/com.github.panpf.sketch.util.*Provider). Without these
# rules ProGuard strips or renames the provider classes/interfaces since nothing
# references them statically, causing a ServiceConfigurationError at runtime.
-keepnames interface com.github.panpf.sketch.util.FetcherProvider
-keepnames interface com.github.panpf.sketch.util.DecoderProvider
-keep class * implements com.github.panpf.sketch.util.FetcherProvider
-keep class * implements com.github.panpf.sketch.util.DecoderProvider

# ProGuard's bytecode optimizer corrupts Okio's Kotlin-generated covariant-return
# bridge methods (e.g. Okio__JvmOkioKt.sink), producing a
# "java.lang.VerifyError: Bad return type" at runtime. Shrinking/obfuscation are
# still safe and worthwhile; only the optimize pass is unsafe here.
-dontoptimize