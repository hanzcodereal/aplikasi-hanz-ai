package com.hanzdev.hanzai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.hanzdev.hanzai.R

val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

private val base = Typography()

// Semua gaya teks memakai Poppins
val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = PoppinsFontFamily),
    headlineMedium = base.headlineMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = base.headlineSmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    titleLarge = base.titleLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    titleMedium = base.titleMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium),
    bodyLarge = base.bodyLarge.copy(fontFamily = PoppinsFontFamily),
    bodyMedium = base.bodyMedium.copy(fontFamily = PoppinsFontFamily),
    bodySmall = base.bodySmall.copy(fontFamily = PoppinsFontFamily),
    labelLarge = base.labelLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium),
    labelMedium = base.labelMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium),
    labelSmall = base.labelSmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium),
)
