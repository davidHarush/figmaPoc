package com.david.h.figmapoc.ui.test.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.FigmaPocTheme
import com.david.h.figmapoc.ui.theme.SignUpBackground
import com.david.h.figmapoc.ui.theme.SignUpBlack
import com.david.h.figmapoc.ui.theme.SignUpFacebook
import com.david.h.figmapoc.ui.theme.SignUpGray
import com.david.h.figmapoc.ui.theme.SignUpGrayDark
import com.david.h.figmapoc.ui.theme.SignUpIconGray
import com.david.h.figmapoc.ui.theme.SignUpInputBg
import com.david.h.figmapoc.ui.theme.SignUpInputBorder
import com.david.h.figmapoc.ui.theme.SignUpPrimary
import com.david.h.figmapoc.ui.theme.SignUpSocialBg

/**
 * Sign Up Screen — Figma node 1:18668
 * Frame: 375×812 — eCommerce App UI Kit
 */
@Composable
fun SignUpScreen(
    onCreateAccountClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SignUpBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 29.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(63.dp))

        // ── Title ──────────────────────────────────────────────────────────────
        Text(
            text = "Create an \naccount",
            style = SignUpTitleStyle,
            color = SignUpBlack,
        )

        Spacer(modifier = Modifier.height(33.dp))

        // ── Username / Email field ─────────────────────────────────────────────
        SignUpInputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Username or Email",
            leadingIcon = { UserIcon() },
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Password field ─────────────────────────────────────────────────────
        SignUpInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            leadingIcon = { LockIcon() },
            trailingIcon = {
                EyeIconButton(visible = passwordVisible) {
                    passwordVisible = !passwordVisible
                }
            },
            isPassword = true,
            passwordVisible = passwordVisible,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Confirm Password field ─────────────────────────────────────────────
        SignUpInputField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "ConfirmPassword",
            leadingIcon = { LockIcon() },
            trailingIcon = {
                EyeIconButton(visible = confirmPasswordVisible) {
                    confirmPasswordVisible = !confirmPasswordVisible
                }
            },
            isPassword = true,
            passwordVisible = confirmPasswordVisible,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Caption ───────────────────────────────────────────────────────────
        Text(
            text = "By clicking the Register button, you agree to the public offer",
            style = SignUpCaptionStyle,
            color = SignUpGray,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Create Account Button ─────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SignUpPrimary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onCreateAccountClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Create Account",
                style = SignUpButtonLabelStyle,
            )
        }

        Spacer(modifier = Modifier.height(49.dp))

        // ── Social sign-in section ────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "- OR Continue with -",
                style = SignUpOrDividerStyle,
                color = SignUpGrayDark,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Social buttons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SocialIconButton(label = "G", iconColor = Color(0xFFDB4437)) // Google
                SocialIconButton(label = "\uF8FF", iconColor = SignUpBlack)   // Apple
                SocialIconButton(label = "f", iconColor = SignUpFacebook)      // Facebook
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Already have account ──────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "I Already Have an Account",
                    style = SignUpAlreadyHaveStyle,
                    color = SignUpGrayDark,
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = SignUpPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = TextDecoration.Underline,
                            )
                        ) {
                            append("Login")
                        }
                    },
                    modifier = Modifier.clickable(onClick = onLoginClick),
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ── Reusable input field ───────────────────────────────────────────────────────
@Composable
private fun SignUpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
) {
    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .border(
                width = 1.dp,
                color = SignUpInputBorder,
                shape = RoundedCornerShape(10.dp),
            ),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        textStyle = SignUpInputHintStyle.copy(color = SignUpBlack),
        placeholder = {
            Text(
                text = placeholder,
                style = SignUpInputHintStyle,
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = if (isPassword) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            KeyboardOptions.Default
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = SignUpInputBg,
            focusedContainerColor = SignUpInputBg,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedLeadingIconColor = SignUpIconGray,
            focusedLeadingIconColor = SignUpIconGray,
            unfocusedTrailingIconColor = SignUpIconGray,
            focusedTrailingIconColor = SignUpIconGray,
        ),
    )
}

// ── Icon composables ───────────────────────────────────────────────────────────

@Composable
private fun UserIcon() {
    Icon(
        imageVector = Icons.Outlined.Person,
        contentDescription = "User",
        tint = SignUpIconGray,
        modifier = Modifier.size(20.dp),
    )
}

@Composable
private fun LockIcon() {
    Icon(
        imageVector = Icons.Outlined.Lock,
        contentDescription = "Lock",
        tint = SignUpIconGray,
        modifier = Modifier.size(20.dp),
    )
}

@Composable
private fun EyeIconButton(visible: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            contentDescription = if (visible) "Hide password" else "Show password",
            tint = SignUpIconGray,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun SocialIconButton(
    label: String,
    iconColor: Color,
) {
    Box(
        modifier = Modifier
            .size(55.dp)
            .clip(CircleShape)
            .background(SignUpSocialBg)
            .border(
                border = BorderStroke(1.dp, SignUpPrimary),
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = iconColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
fun SignUpScreenPreview() {
    FigmaPocTheme {
        SignUpScreen()
    }
}
