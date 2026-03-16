package com.david.h.figmapoc.ui.test.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.RestaurantFreeOrange
import com.david.h.figmapoc.ui.theme.SenFontFamily

// ─── Design tokens ────────────────────────────────────────────────────────────

private val PaymentBg = Color(0xFFFFFFFF)
private val PaymentCardUnselected = Color(0xFFF0F5FA)
private val PaymentCardSelected = Color(0xFFFF7622)      // orange border
private val PaymentEmptyBg = Color(0xFFF7F8F9)
private val PaymentLabelColor = Color(0xFF464E57)
private val PaymentTitleColor = Color(0xFF181C2E)
private val PaymentSubtitleColor = Color(0xFF2D2D2D)
private val PaymentBackBg = Color(0xFFECF0F4)
private val PaymentTotalLabel = Color(0xFFA0A5BA)
private val PaymentAddNewBorder = Color(0xFFF0F5FA)
private val PaymentMastercardRed = Color(0xFFEB001B)
private val PaymentMastercardOrange = Color(0xFFF79E1B)

// ─── Payment method data ──────────────────────────────────────────────────────

private data class PaymentMethod(
    val name: String,
    val icon: ImageVector,
    val iconTint: Color,
    val cardBg: Color,
)

private val paymentMethods = listOf(
    PaymentMethod("Cash", Icons.Filled.AttachMoney, RestaurantFreeOrange, PaymentCardUnselected),
    PaymentMethod("Visa", Icons.Filled.CreditCard, Color(0xFF2566AF), PaymentCardUnselected),
    PaymentMethod("Mastercard", Icons.Filled.CreditCard, PaymentMastercardRed, Color(0xFFFFFFFF)),
    PaymentMethod("PayPal", Icons.Filled.AttachMoney, Color(0xFF27346A), PaymentCardUnselected),
)

// ─── Root screen ─────────────────────────────────────────────────────────────

@Composable
fun PaymentMethodScreen(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(2) } // Mastercard selected by default

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaymentBg)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp)
    ) {
        // ── Status bar spacer ──────────────────────────────────────────────
        Spacer(Modifier.height(50.dp))

        // ── Top bar ───────────────────────────────────────────────────────
        PaymentTopBar()

        Spacer(Modifier.height(24.dp))

        // ── Payment method tabs ────────────────────────────────────────────
        PaymentMethodRow(
            selectedIndex = selectedIndex,
            onSelect = { selectedIndex = it }
        )

        Spacer(Modifier.height(32.dp))

        // ── Empty state: no mastercard ─────────────────────────────────────
        NoMastercardSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(24.dp))

        // ── Add New button ─────────────────────────────────────────────────
        AddNewButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(24.dp))

        // ── Total row ─────────────────────────────────────────────────────
        TotalRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(20.dp))

        // ── Pay & Confirm button ───────────────────────────────────────────
        PayConfirmButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}

// ─── Top bar ─────────────────────────────────────────────────────────────────

@Composable
private fun PaymentTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Back button: 45×45 circle with light gray bg
        Box(
            modifier = Modifier
                .size(45.dp)
                .shadow(elevation = 2.dp, shape = CircleShape, clip = false)
                .clip(CircleShape)
                .background(PaymentBackBg, CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = PaymentTitleColor,
                modifier = Modifier.size(18.dp)
            )
        }

        // "Payment" title
        Text(
            text = "Payment",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            lineHeight = 22.sp,
            color = PaymentTitleColor
        )
    }
}

// ─── Payment method row ───────────────────────────────────────────────────────

@Composable
private fun PaymentMethodRow(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(paymentMethods) { index, method ->
            PaymentCard(
                method = method,
                isSelected = index == selectedIndex,
                onClick = { onSelect(index) }
            )
        }
    }
}

@Composable
private fun PaymentCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(85.dp)
    ) {
        // Card tile (85×72)
        Box(
            modifier = Modifier
                .width(85.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) Color.White else PaymentCardUnselected,
                    RoundedCornerShape(10.dp)
                )
                .then(
                    if (isSelected) {
                        Modifier.border(
                            width = 2.dp,
                            color = PaymentCardSelected,
                            shape = RoundedCornerShape(10.dp)
                        )
                    } else Modifier
                )
                .clickable(onClick = onClick)
        ) {
            // Payment brand icon centered
            Icon(
                imageVector = method.icon,
                contentDescription = method.name,
                tint = method.iconTint,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp)
            )

            // Selected checkmark badge at top-right
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-8).dp)
                        .size(20.dp)
                        .background(RestaurantFreeOrange, CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 9.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Label below the card
        Text(
            text = method.name,
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            color = PaymentLabelColor,
            textAlign = TextAlign.Center
        )
    }
}

// ─── No Mastercard empty state ────────────────────────────────────────────────

@Composable
private fun NoMastercardSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(257.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(PaymentEmptyBg, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Mastercard illustration placeholder
            MastercardIllustration()

            Spacer(Modifier.height(16.dp))

            // "No master card added"
            Text(
                text = "No master card added",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 19.sp,
                color = Color(0xFF32343E),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "You can add a mastercard and save it for later",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = PaymentSubtitleColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
private fun MastercardIllustration() {
    // Simplified Mastercard brand illustration
    Box(
        modifier = Modifier
            .width(168.dp)
            .height(106.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFF7622), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Mastercard two overlapping circles logo
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(x = 20.dp, y = 18.dp)
        ) {
            // Left red circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(PaymentMastercardRed, CircleShape)
            )
            // Right orange/yellow circle with overlap
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = (-10).dp)
                    .background(PaymentMastercardOrange, CircleShape)
            )
        }
        // Card chip placeholder
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp, top = 14.dp)
                .width(30.dp)
                .height(4.dp)
                .background(Color(0xFFFBFBFC).copy(alpha = 0.5f), RoundedCornerShape(2.dp))
        )
    }
}

// ─── Add New button ───────────────────────────────────────────────────────────

@Composable
private fun AddNewButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(2.dp, PaymentAddNewBorder, RoundedCornerShape(10.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // "+" icon
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = RestaurantFreeOrange,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "ADD NEW",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = RestaurantFreeOrange,
                letterSpacing = 0.sp
            )
        }
    }
}

// ─── Total row ────────────────────────────────────────────────────────────────

@Composable
private fun TotalRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "TOTAL:",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                color = PaymentTotalLabel
            )
        }
        Text(
            text = "$96",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
            lineHeight = 36.sp,
            color = PaymentTitleColor
        )
    }
}

// ─── Pay & Confirm button ─────────────────────────────────────────────────────

@Composable
private fun PayConfirmButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(62.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(RestaurantFreeOrange, RoundedCornerShape(12.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "PAY & CONFIRM",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White,
            letterSpacing = 0.sp
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PaymentMethodScreenPreview() {
    PaymentMethodScreen()
}

