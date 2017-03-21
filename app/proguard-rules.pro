# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/catherine/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript feature
# class:
#-keepclassmembers class fqcn.of.javascript.feature.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
#-keep class "com.example.catherine.myapplication.receiver.MIUIMessageReceiverer {*;}
#-keep class com.huawei.android.pushagent.**{*;}
#-keep class com.huawei.android. pushselfshow.**{*;}
#-keep class com.huawei.android. microkernel.**{*;}
#-keep class com.baidu.mapapi.**{*;}