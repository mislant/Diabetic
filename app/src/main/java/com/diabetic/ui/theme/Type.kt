package com.diabetic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.diabetic.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
private val fontName = GoogleFont("Montserrat")
private val montserrat = FontFamily(
    Font(fontName, provider),
    Font(fontName, provider, FontWeight.Light),
    Font(fontName, provider, FontWeight.Medium),
    Font(fontName, provider, FontWeight.SemiBold),
    Font(fontName, provider, FontWeight.Bold),
)

private val Default = Typography()
val Typography = Typography(
    displayLarge = Default.displayLarge.copy(
        fontFamily = montserrat
    ),
    displayMedium = Default.displayLarge.copy(
        fontFamily = montserrat
    ),
    displaySmall = Default.displaySmall.copy(
        fontFamily = montserrat
    ),
    headlineLarge = Default.headlineLarge.copy(
        fontFamily = montserrat
    ),
    headlineMedium = Default.headlineMedium.copy(
        fontFamily = montserrat
    ),
    headlineSmall = Default.headlineSmall.copy(
        fontFamily = montserrat
    ),
    titleLarge = Default.titleLarge.copy(
        fontFamily = montserrat
    ),
    titleMedium = Default.titleMedium.copy(
        fontFamily = montserrat
    ),
    titleSmall = Default.titleSmall.copy(
        fontFamily = montserrat
    ),
    bodyLarge = Default.bodyLarge.copy(
        fontFamily = montserrat
    ),
    bodyMedium = Default.bodyMedium.copy(
        fontFamily = montserrat
    ),
    bodySmall = Default.bodySmall.copy(
        fontFamily = montserrat
    ),
    labelLarge =  Default.labelLarge.copy(
        fontFamily = montserrat
    ),
    labelMedium = Default.labelMedium.copy(
        fontFamily = montserrat
    ),
    labelSmall = Default.labelSmall.copy(
        fontFamily = montserrat
    )
)