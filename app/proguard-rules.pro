# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Keep Room schema and generated implementation classes.
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# Keep Kotlin metadata for reflection-based frameworks.
-keep class kotlin.Metadata { *; }

# Dagger Hilt rules
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.ContentProvider
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.app.Activity
-keep class * extends androidx.fragment.app.Fragment

-keep @dagger.hilt.android.AndroidEntryPoint class * {
    public <init>(...);
}
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-keep class * implements dagger.hilt.internal.UnsafeCasts { *; }

# Google Play Core (In-App Updates & Reviews)
-keep class com.google.android.play.core.common.PlayCoreDialogWrapperActivity { *; }
-keep class com.google.android.play.core.review.** { *; }
-keep class com.google.android.play.core.appupdate.** { *; }
-keep class com.google.android.play.core.tasks.** { *; }

# Jetpack Compose rules
-keepclassmembers class * extends androidx.compose.runtime.RecomposeScope { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *** *(...);
}