package com.david.h.figmapoc.ui.test.instagram

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Colors from Figma ───────────────────────────────────────────────────────
private val BgDark = Color(0xFF0B1014)
private val BtnDark = Color(0xFF24292F)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFF707D8F)
private val LinkBlue = Color(0xFF90D4F7)
private val GradientStart = Color(0xFFFF00C5)
private val GradientMid1 = Color(0xFFFF0000)
private val GradientMid2 = Color(0xFFFF8600)
private val GradientEnd = Color(0xFFFFCA00)

// ─── Story ring gradient ─────────────────────────────────────────────────────
private val storyGradient = Brush.sweepGradient(
    listOf(GradientStart, GradientMid1, GradientMid2, GradientEnd, GradientStart)
)

@Composable
fun InstagramProfileScreen() {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) }  // 0=Grid, 1=Reels, 2=Tags

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ── Status Bar ────────────────────────────────────────────────
            StatusBar()

            // ── Header ────────────────────────────────────────────────────
            ProfileHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // ── Profile Info ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Avatar + Stats row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileAvatar()
                    Spacer(modifier = Modifier.width(20.dp))
                    UserStats()
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name + Pronouns
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Juliantara",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "he/him",
                        color = TextGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Occupation
                Text(
                    text = "Designer",
                    color = TextGray,
                    fontSize = 14.sp
                )

                // Bio
                Text(
                    text = "* UI/UX Designer, Webflow Developer",
                    color = TextWhite,
                    fontSize = 14.sp
                )

                // See Translation
                Text(
                    text = "See Translation",
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Website Link
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = "Website",
                        tint = LinkBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "www.website.com",
                        color = LinkBlue,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Followed By Section
                FollowedByRow()

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                ActionButtons()

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Highlights ────────────────────────────────────────────────
            HighlightsRow()

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tab Bar (Grid / Reels / Tags) ─────────────────────────────
            PostsTabBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            // ── Photo Grid ────────────────────────────────────────────────
            PhotoGrid()

            // Space so content doesn't hide behind bottom nav
            Spacer(modifier = Modifier.height(60.dp))
        }

        // ── Bottom Navigation Bar ──────────────────────────────────────────
        BottomNavBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

// ─── Status Bar ──────────────────────────────────────────────────────────────
@Composable
private fun StatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgDark)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "9:41",
            color = TextWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(
                Icons.Default.SignalCellularAlt,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(18.dp)
            )
            Icon(
                Icons.Default.Wifi,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(16.dp)
            )
            Icon(
                Icons.Default.BatteryFull,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ─── Profile Header ──────────────────────────────────────────────────────────
@Composable
private fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: back arrow + username
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Back",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = "juliantara.uix",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        // Right: bell + options
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = TextWhite
                )
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "Options", tint = TextWhite)
            }
        }
    }
}

// ─── Profile Avatar ──────────────────────────────────────────────────────────
@Composable
private fun ProfileAvatar() {
    Box(contentAlignment = Alignment.Center) {
        // Gradient story ring
        Box(
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .background(storyGradient)
        )
        // Dark separator ring
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(BgDark)
        )
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(94.dp)
                .clip(CircleShape)
                .background(Color(0xFF3A4550)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = TextGray,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

// ─── User Stats ──────────────────────────────────────────────────────────────
@Composable
private fun UserStats() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.wrapContentWidth()
    ) {
        StatItem(value = "100", label = "posts")
        StatItem(value = "299K", label = "followers")
        StatItem(value = "10", label = "following")
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Text(text = label, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Normal)
    }
}

// ─── Followed By Row ─────────────────────────────────────────────────────────
@Composable
private fun FollowedByRow() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Overlapping avatar group
        Box(modifier = Modifier
            .width(72.dp)
            .height(40.dp)) {
            repeat(3) { i ->
                Box(
                    modifier = Modifier
                        .offset(x = (i * 20).dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3A4550))
                        .border(2.dp, BgDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Followed by juliantara.uix, juliantara\nand 100 others",
            color = TextWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        )
    }
}

// ─── Action Buttons ──────────────────────────────────────────────────────────
@Composable
private fun ActionButtons() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Following button
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .height(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BtnDark),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text("Following", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = TextWhite,
                modifier = Modifier.size(16.dp)
            )
        }
        // Message button
        Button(
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .height(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BtnDark),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text("Message", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        // Add Friend icon button
        Button(
            onClick = {},
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BtnDark),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.Default.PersonAdd,
                contentDescription = "Add Friend",
                tint = TextWhite,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ─── Highlights Row ──────────────────────────────────────────────────────────
@Composable
private fun HighlightsRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(5) { index ->
            val labels = listOf("Highlight", "Travel", "Design", "Work", "Life")
            HighlightItem(label = labels.getOrElse(index) { "Highlight" })
        }
    }
}

@Composable
private fun HighlightItem(label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(71.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(71.dp)
        ) {
            // Gray border ring
            Box(
                modifier = Modifier
                    .size(71.dp)
                    .clip(CircleShape)
                    .border(3.dp, TextGray, CircleShape)
            )
            // Image placeholder
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3A4550)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Image,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = TextWhite,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// ─── Posts Tab Bar ───────────────────────────────────────────────────────────
@Composable
private fun PostsTabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabIcon(
            icon = Icons.Default.GridOn,
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        TabIcon(
            icon = Icons.Default.VideoLibrary,
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        TabIcon(
            icon = Icons.Default.BookmarkBorder,
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

@Composable
private fun TabIcon(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(130.dp)
            .height(40.dp)
            .then(
                if (selected) Modifier.border(
                    width = 1.dp,
                    color = TextWhite,
                    shape = RoundedCornerShape(0.dp)
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) TextWhite else TextGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ─── Photo Grid ──────────────────────────────────────────────────────────────
@Composable
private fun PhotoGrid() {
    val images = (1..6).toList()
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        images.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                row.forEach { _ ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(Color(0xFF1E2A32)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Post",
                            tint = TextGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Bottom Navigation Bar ───────────────────────────────────────────────────
@Composable
private fun BottomNavBar(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BgDark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.AddBox,
                    contentDescription = "Create",
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.VideoLibrary,
                    contentDescription = "Reels",
                    tint = TextWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD75353))
                    .border(1.dp, Color(0xFF343434), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = TextWhite,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        // Home indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(144.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(TextWhite)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B1014)
@Composable
fun InstagramProfileScreenPreview() {
    InstagramProfileScreen()
}

