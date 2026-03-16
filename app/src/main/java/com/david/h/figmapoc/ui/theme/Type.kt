package com.david.h.figmapoc.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val RubikGoogleFont = GoogleFont("Rubik")

// Rubik font family — matches Figma design (fontFamily: "Rubik")
val RubikFontFamily = FontFamily(
    Font(googleFont = RubikGoogleFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = RubikGoogleFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = RubikGoogleFont, fontProvider = provider, weight = FontWeight.Medium),
)

private val SenGoogleFont = GoogleFont("Sen")

// Sen font family — matches Figma design (fontFamily: "Sen") for Restaurant View
val SenFontFamily = FontFamily(
    Font(googleFont = SenGoogleFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = SenGoogleFont, fontProvider = provider, weight = FontWeight.Bold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 19.sp,
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)