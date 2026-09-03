package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme =
  lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenContainer,
    onPrimaryContainer = GreenDark,
    secondary = AmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = AmberSecondary,
    tertiary = BlueVet,
    onTertiary = Color.White,
    tertiaryContainer = BlueVetContainer,
    onTertiaryContainer = BlueVet,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = OutlineLight,
    outlineVariant = BorderLight
  )

private val DarkColorScheme = LightColorScheme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  // Keep false to retain distinctive agricultural theme and prevent unreadable white-on-white text
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = LightColorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun appTextFieldColors(
  focusedBorder: Color = GreenDark,
  unfocusedBorder: Color = BorderLight,
  containerColor: Color = Color.White
) = OutlinedTextFieldDefaults.colors(
  focusedTextColor = TextPrimary,
  unfocusedTextColor = TextPrimary,
  focusedContainerColor = containerColor,
  unfocusedContainerColor = containerColor,
  focusedBorderColor = focusedBorder,
  unfocusedBorderColor = unfocusedBorder,
  focusedLabelColor = focusedBorder,
  unfocusedLabelColor = TextSecondary,
  focusedPlaceholderColor = TextTertiary,
  unfocusedPlaceholderColor = TextTertiary,
  cursorColor = focusedBorder
)


