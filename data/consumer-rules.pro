# --- Retrofit interfaces ---
# Retrofit uses annotations + dynamic proxies, so interfaces must be kept
-keep interface com.walkmansit.realworld.data.api.** { *; }

# --- Retrofit annotations ---
# Needed so Retrofit can read method + parameter annotations at runtime
-keepattributes Signature
-keepattributes Exceptions

# --- Gson or Moshi models (reflection-based) ---
# Adjust the package to your DTO/data classes
-keep class com.walkmansit.realworld.data.model.** { *; }

-keep class com.walkmansit.realworld.data.util.AuthInterceptor

# --- OkHttp and Okio ---
# Don’t warn about internal classes (safe to ignore)
-dontwarn okhttp3.**
-dontwarn okio.**

# OkHttp uses reflection for some internals
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Okio is used by OkHttp
-keep class okio.** { *; }

# --- Retrofit runtime ---
# Retrofit runtime keeps generated code (safe to keep all public classes)
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# --- Logging Interceptor (if used) ---
-keep class okhttp3.logging.** { *; }

# --- Retrofit converters (e.g., GsonConverterFactory, MoshiConverterFactory) ---
-keep class retrofit2.converter.** { *; }


# --- Keep Hilt and Dagger generated code ---
# (Most of these are already kept automatically, but safe in libraries)

# If your library defines Hilt modules (annotated with @Module)
-keep class com.walkmansit.realworld.data.di.** { *; }

# Keep Hilt modules and generated components
-keep class com.walkmansit.realworld.data.** { *; }
-keepnames class com.walkmansit.realworld.data.** { *; }

# Keep Retrofit/OkHttp models if used via reflection
-keep class com.walkmansit.realworld.data.api.** { *; }
-keep class com.walkmansit.realworld.data.util.** { *; }
-dontwarn com.walkmansit.realworld.data.**
