package com.david.h.figmapoc.ui.test.fooddetails

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.FoodDetailsBg
import com.david.h.figmapoc.ui.theme.FoodDetailsBottomBg
import com.david.h.figmapoc.ui.theme.FoodDetailsDarkQty
import com.david.h.figmapoc.ui.theme.FoodDetailsGray
import com.david.h.figmapoc.ui.theme.FoodDetailsHeart
import com.david.h.figmapoc.ui.theme.FoodDetailsIconBg
import com.david.h.figmapoc.ui.theme.FoodDetailsIconOrange
import com.david.h.figmapoc.ui.theme.FoodDetailsSizeBg
import com.david.h.figmapoc.ui.theme.FoodDetailsSizeSelected
import com.david.h.figmapoc.ui.theme.RestaurantDarkText
import com.david.h.figmapoc.ui.theme.RestaurantFreeOrange
import com.david.h.figmapoc.ui.theme.RestaurantImagePlaceholder
import com.david.h.figmapoc.ui.theme.RestaurantLightGrayText
import com.david.h.figmapoc.ui.theme.RestaurantMediumText
import com.david.h.figmapoc.ui.theme.SenFontFamily

// ─── Data models ─────────────────────────────────────────────────────────────

private data class Ingredient(
    val name: String,
    val isAllergen: Boolean = false,
)

private val ingredients = listOf(
    Ingredient("Salt"),
    Ingredient("Chicken"),
    Ingredient("Onion", isAllergen = true),
    Ingredient("Garlic"),
    Ingredient("Pappers", isAllergen = true),
    Ingredient("Ginger"),
    Ingredient("Broccoli"),
    Ingredient("Orange"),
    Ingredient("Walnut"),
)

private val sizes = listOf("10\"", "14\"", "16\"")

// ─── Root screen ─────────────────────────────────────────────────────────────

@Composable
fun FoodDetailsScreen(modifier: Modifier = Modifier) {
    var selectedSizeIndex by remember { mutableIntStateOf(1) } // "14"" selected by default
    var quantity by remember { mutableIntStateOf(2) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FoodDetailsBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 200.dp) // space for bottom panel
        ) {
            // ── Hero image ──────────────────────────────────────────────────
            item { HeroSection() }

            // ── Restaurant / dish info ──────────────────────────────────────
            item { FoodInfoSection() }

            // ── Size selector ───────────────────────────────────────────────
            item {
                SizeSelector(
                    sizes = sizes,
                    selectedIndex = selectedSizeIndex,
                    onSizeSelected = { selectedSizeIndex = it }
                )
            }

            // ── Description ─────────────────────────────────────────────────
            item { DescriptionSection() }

            // ── Ingredients header ──────────────────────────────────────────
            item { IngredientsHeader() }

            // ── Ingredient chips ─────────────────────────────────────────────
            item { IngredientsRow(ingredients = ingredients) }

            // ── Bottom spacing so LazyColumn doesn't hide under bottom panel
            item { Spacer(Modifier.height(32.dp)) }
        }

        // ── Sticky bottom: qty stepper + price + add-to-cart ────────────────
        BottomCartPanel(
            quantity = quantity,
            onDecrease = { if (quantity > 1) quantity-- },
            onIncrease = { quantity++ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
        // Hero image (full width, bottom corners rounded 30dp to match frame)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp, topEnd = 0.dp,
                        bottomStart = 30.dp, bottomEnd = 30.dp
                    )
                )
                .background(RestaurantImagePlaceholder)
        )

        // Back button — top-left
        HeroIconButton(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = RestaurantDarkText,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 50.dp)
        )

        // Favorite/save button — top-right
        HeroIconButton(
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Save",
                    tint = FoodDetailsHeart,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = 50.dp)
        )
    }
}

@Composable
private fun HeroIconButton(
    icon: @Composable () -> Unit,
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
        icon()
    }
}

// ─── Food info section ────────────────────────────────────────────────────────

@Composable
private fun FoodInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp)
    ) {
        // Restaurant name + avatar row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(RestaurantImagePlaceholder, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Rose Garden",
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = RestaurantDarkText
            )
        }

        Spacer(Modifier.height(8.dp))

        // Dish name
        Text(
            text = "Burger Bistro",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = RestaurantDarkText
        )

        Spacer(Modifier.height(12.dp))

        // Rating / Free / Delivery row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = RestaurantFreeOrange,
                    modifier = Modifier
                        .size(18.dp)
                        .border(1.dp, RestaurantFreeOrange, CircleShape)
                        .padding(2.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "4.7",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = RestaurantDarkText
                )
            }

            // Free shipping
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalShipping,
                    contentDescription = null,
                    tint = RestaurantFreeOrange,
                    modifier = Modifier.size(16.dp)
                )
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
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = RestaurantFreeOrange,
                    modifier = Modifier.size(16.dp)
                )
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

        Spacer(Modifier.height(16.dp))
    }
}

// ─── Size selector ────────────────────────────────────────────────────────────

@Composable
private fun SizeSelector(
    sizes: List<String>,
    selectedIndex: Int,
    onSizeSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "SIZE:",
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
            color = RestaurantLightGrayText
        )

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            sizes.forEachIndexed { index, size ->
                SizeBubble(
                    label = size,
                    isSelected = index == selectedIndex,
                    onClick = { onSizeSelected(index) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun SizeBubble(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) FoodDetailsSizeSelected else FoodDetailsSizeBg
    val textColor = if (isSelected) Color.White else Color(0xFF121223)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = SenFontFamily,
            fontWeight = fontWeight,
            fontSize = 16.sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Description ──────────────────────────────────────────────────────────────

@Composable
private fun DescriptionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
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

// ─── Ingredients section ──────────────────────────────────────────────────────

@Composable
private fun IngredientsHeader() {
    Text(
        text = "INGRIDENTS",
        fontFamily = SenFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.26.sp,
        color = RestaurantMediumText,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun IngredientsRow(ingredients: List<Ingredient>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(ingredients) { ingredient ->
            IngredientChip(ingredient = ingredient)
        }
    }
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun IngredientChip(ingredient: Ingredient) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(50.dp)
    ) {
        // Icon bubble: 50x50 circle with light peach bg
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(FoodDetailsIconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Ingredient icon placeholder with orange color
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(FoodDetailsIconOrange.copy(alpha = 0.7f), CircleShape)
            )
        }

        Spacer(Modifier.height(6.dp))

        // Ingredient name (possibly 2 lines for allergy items)
        Text(
            text = if (ingredient.isAllergen) "${ingredient.name}\n(Allergy)" else ingredient.name,
            fontFamily = SenFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 14.sp,
            color = FoodDetailsGray,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Bottom cart panel ────────────────────────────────────────────────────────

@Composable
private fun BottomCartPanel(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(184.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(FoodDetailsBottomBg)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Qty stepper row + price
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Price
                Text(
                    text = "$32",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 28.sp,
                    color = RestaurantDarkText,
                    lineHeight = 34.sp
                )

                // Qty stepper
                QtyStepper(
                    quantity = quantity,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease
                )
            }

            Spacer(Modifier.height(16.dp))

            // Add to cart button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(RestaurantFreeOrange)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ADD TO CART",
                    fontFamily = SenFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
private fun QtyStepper(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(125.dp)
            .height(48.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            .background(FoodDetailsDarkQty),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Minus button
            QtyButton(
                label = "−",
                onClick = onDecrease
            )

            // Count
            Text(
                text = quantity.toString(),
                fontFamily = SenFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )

            // Plus button
            QtyButton(
                label = "+",
                onClick = onIncrease
            )
        }
    }
}

@Composable
private fun QtyButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(Color.White.copy(alpha = 0.2f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp
        )
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FoodDetailsScreenPreview() {
    FoodDetailsScreen()
}

