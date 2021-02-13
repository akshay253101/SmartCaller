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

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends androidx.fragment.app.Fragment

-keepnames class androidx.navigation.fragment.NavHostFragment

# DMO classes for gson request and response
#-keep class com.beetlestance.smartcaller.data.models.** { *; }

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# We're keeping the static fields of referenced inner classes of auto-generatedR classes,
# just in case your code is accessing those fields by introspection. Note that the compiler
# already inlines primitive fields, so ProGuard can generally remove all these classes entirely anyway
# (because the classes are not referenced and therefore not required).
-keepclassmembers class **.R$* {
    public static <fields>;
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}
