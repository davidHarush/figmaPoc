package com.david.h.figmapoc.ui.test.signup

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.SignUpBlack
import com.david.h.figmapoc.ui.theme.SignUpGray
import com.david.h.figmapoc.ui.theme.SignUpGrayDark
import com.david.h.figmapoc.ui.theme.SignUpPrimary
import com.david.h.figmapoc.ui.theme.White

/**
 * Typography styles for the Sign Up screen (Figma node 1:18668).
 * Font families: Montserrat, Poppins
 */

/** "Create an\naccount" — Montserrat Bold 36sp, lh=43sp */
val SignUpTitleStyle = TextStyle(
    color = SignUpBlack,
    fontSize = 36.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 43.sp,
    letterSpacing = 0.sp,
)

/** "Create Account" button label — Montserrat SemiBold 20sp */
val SignUpButtonLabelStyle = TextStyle(
    color = White,
    fontSize = 20.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 24.38.sp,
    letterSpacing = 0.sp,
)

/** Input placeholder text — Montserrat Medium 12sp */
val SignUpInputHintStyle = TextStyle(
    color = SignUpGray,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 14.63.sp,
    letterSpacing = 0.sp,
)

/** "By clicking the Register..." caption — Montserrat Regular 12sp */
val SignUpCaptionStyle = TextStyle(
    color = SignUpGray,
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 14.63.sp,
    letterSpacing = 0.sp,
)

/** "- OR Continue with -" — Montserrat Medium 12sp */
val SignUpOrDividerStyle = TextStyle(
    color = SignUpGrayDark,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 14.63.sp,
    letterSpacing = 0.sp,
)

/** "I Already Have an Account" — Poppins Regular 14sp */
val SignUpAlreadyHaveStyle = TextStyle(
    color = SignUpGrayDark,
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 21.sp,
    letterSpacing = 0.sp,
)

/** "Login" link — Montserrat SemiBold 14sp, underline */
val SignUpLoginLinkStyle = TextStyle(
    color = SignUpPrimary,
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 17.07.sp,
    letterSpacing = 0.sp,
)

