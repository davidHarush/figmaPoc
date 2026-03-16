package com.david.h.figmapoc.ui.test.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.RestaurantDarkText
import com.david.h.figmapoc.ui.theme.RestaurantFreeOrange
import com.david.h.figmapoc.ui.theme.RestaurantGrayText
import com.david.h.figmapoc.ui.theme.RestaurantImagePlaceholder
import com.david.h.figmapoc.ui.theme.RestaurantLightGrayText
import com.david.h.figmapoc.ui.theme.RestaurantMediumText
import com.david.h.figmapoc.ui.theme.RestaurantOrange
import com.david.h.figmapoc.ui.theme.RestaurantTagBorder
import com.david.h.figmapoc.ui.theme.SenFontFamily

// ─── Data models ─────────────────────────────────────────────────────────────

data class FoodItem(
    val name: String,
    val subtitle: String,
    val price: String,
)

private val sampleFoodItems = listOf(
    FoodItem("Burger Ferguson", "Spicy restaurant", "$40"),
    FoodItem("Rockin' Burgers", "Cafecafachino", "$40"),
    FoodItem("Meat Pizza", "Spicy burger", "$40"),
    FoodItem("Meat Pizza", "Spicy burger", "$40"),
)

private val categoryTabs = listOf("Burger", "Sandwich", "Pizza", "Sanwich")

// ─── Root screen ─────────────────────────────────────────────────────────────

@Composable
fun RestaurantScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // ── Hero image with overlay controls ─────────────────────────────────
        item {
            HeroSection()
        }

        // ── Restaurant meta info ──────────────────────────────────────────────
        item {
            RestaurantInfoSection()
        }

        // ── Category tabs ─────────────────────────────────────────────────────
        item {
            CategoryTabsRow(
                tabs = categoryTabs,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }

        // ── Section header ────────────────────────────────────────────────────
        item {
            Text(
                text = "Burger (10)",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = RestaurantDarkText,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }

        // ── Food cards grid (2 columns, manual rows) ──────────────────────────
        val rows = sampleFoodItems.chunked(2)
        items(rows.size) { rowIndex ->
            FoodCardRow(items = rows[rowIndex])
        }
    }
}

// ─── Hero section ─────────────────────────────────────────────────────────────

@Composable
private fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(321.dp)
    ) {
        // Hero image placeholder (375×321, bottom corners rounded=24)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 24.dp,
                        bottomStart = 24.dp
                    )
                )
                .background(RestaurantImagePlaceholder)
        )

        // Back button — top-left
        CircleIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 50.dp)
        )

        // More button — top-right
        CircleIconButton(
            icon = Icons.Filled.MoreVert,
            contentDescription = "More",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 50.dp)
        )

        // Slide indicators — bottom-center
        SlideIndicators(
            count = 5,
            activeIndex = 2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }
}

@Composable
private fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(45.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(Color.White, CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = RestaurantDarkText,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SlideIndicators(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            if (index == activeIndex) {
                // Active indicator: outlined circle 20×20
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(
                            width = 1.3.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                )
            } else {
                // Inactive indicator: filled 10×10 opacity 0.41
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.41f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

// ─── Restaurant info ──────────────────────────────────────────────────────────

@Composable
private fun RestaurantInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp)
    ) {
        // Rating row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = RestaurantOrange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "4.7",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = RestaurantDarkText
                )
            }

            // Free delivery
            Row(verticalAlignment = Alignment.CenterVertically) {
                DeliveryTruckIcon(tint = RestaurantFreeOrange)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Free",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = RestaurantDarkText
                )
            }

            // Delivery time
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClockIcon(tint = RestaurantDarkText)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "20 min",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = RestaurantDarkText
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Restaurant name
        Text(
            text = "Spicy restaurant",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = RestaurantDarkText
        )

        Spacer(Modifier.height(8.dp))

        // Description
        Text(
            text = "Maecenas sed diam eget risus varius blandit sit amet non magna. " +
                    "Integer posuere erat a ante venenatis dapibus posuere velit aliquet.",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = RestaurantLightGrayText,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(20.dp))
    }
}

// ─── Delivery / clock inline icons ───────────────────────────────────────────

@Composable
private fun DeliveryTruckIcon(tint: Color) {
    Icon(
        imageVector = Icons.Filled.LocalShipping,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(16.dp)
    )
}

@Composable
private fun ClockIcon(tint: Color) {
    Icon(
        imageVector = Icons.Filled.AccessTime,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(16.dp)
    )
}

// ─── Category tabs ────────────────────────────────────────────────────────────

@Composable
private fun CategoryTabsRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, label ->
            CategoryTab(
                label = label,
                isSelected = index == selectedIndex,
                onClick = { onTabSelected(index) }
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun CategoryTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) RestaurantOrange else Color.Transparent
    val textColor = if (isSelected) Color.White else RestaurantDarkText
    val borderModifier = if (isSelected) {
        Modifier
    } else {
        Modifier.border(
            width = 2.dp,
            color = RestaurantTagBorder,
            shape = RoundedCornerShape(33.dp)
        )
    }

    Box(
        modifier = Modifier
            .height(46.dp)
            .then(borderModifier)
            .clip(RoundedCornerShape(33.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = textColor,
            letterSpacing = (-0.33).sp
        )
    }
}

// ─── Food card row ────────────────────────────────────────────────────────────

@Composable
private fun FoodCardRow(items: List<FoodItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { item ->
            FoodCard(
                item = item,
                modifier = Modifier.weight(1f)
            )
        }
        // If only 1 item in the last row, fill the second slot with empty space
        if (items.size == 1) {
            Spacer(Modifier.weight(1f))
        }
    }
}

// ─── Food card ────────────────────────────────────────────────────────────────

@Composable
private fun FoodCard(
    item: FoodItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(174.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = Color(0x26969696),
                spotColor = Color(0x26969696)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
        ) {
            // Food image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(RestaurantImagePlaceholder)
            )

            Spacer(Modifier.height(8.dp))

            // Food name
            Text(
                text = item.name,
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                color = RestaurantMediumText,
                letterSpacing = (-0.33).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            // Subtitle
            Text(
                text = item.subtitle,
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                color = RestaurantGrayText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.weight(1f))

            // Price row with add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.price,
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = RestaurantDarkText,
                    letterSpacing = (-0.33).sp
                )

                Spacer(Modifier.weight(1f))

                AddButton()
            }
        }
    }
}

@Composable
private fun AddButton() {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(RestaurantOrange, CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        // Plus icon drawn with two lines
        Text(
            text = "+",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp
        )
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RestaurantScreenPreview() {
    RestaurantScreen()
}

