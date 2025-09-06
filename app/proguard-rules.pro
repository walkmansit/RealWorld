# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#########################################
# Core Android and Kotlin
#########################################

# Keep Kotlin metadata (required for reflection and some libraries)
-keepclassmembers class kotlin.Metadata { *; }

# Keep all @Parcelize classes (needed for Parcelable)
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Keep Application classes (entry point)
-keep class com.walkmansit.realworld.RealWorldApplication

# Keep Activities, Fragments, Services, BroadcastReceivers (entry points)
-keep class * extends android.app.Activity { *; }
-keep class * extends androidx.fragment.app.Fragment { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }

#########################################
# Jetpack Compose (if using Compose)
#########################################

# Keep Compose runtime (needed for recomposition)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

#########################################
# Retrofit + OkHttp
#########################################

# Keep Retrofit interfaces (used by reflection)
-keep interface com.example.**.api.** { *; }

# Keep Retrofit annotations and models
-keepattributes Signature
-keepattributes *Annotation*

# Don't warn about OkHttp internal APIs
-dontwarn okhttp3.**
-dontwarn okio.**

#########################################
# Gson or Moshi JSON parsing
#########################################

# Keep model classes (Gson uses reflection)
-keep class com.example.**.model.** { *; }

# If using Moshi code-gen, keep generated adapters
-keep class **JsonAdapter { *; }

#########################################
# Logging and Debug (Optional)
#########################################

# You can strip logging if desired
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
