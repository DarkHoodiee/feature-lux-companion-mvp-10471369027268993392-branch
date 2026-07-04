# LUX Hoodie ProGuard Rules

# Preserve Compose
-keepclassmembers class * extends androidx.compose.runtime.Composer { *; }
-keep class androidx.compose.** { *; }

# Preserve Domain Models (serialization readiness)
-keep class com.lux.companion.domain.** { *; }

# Preserve ViewModels
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }
