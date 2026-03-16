package com.david.h.figmapoc.ui.test.taskmanager

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.david.h.figmapoc.ui.theme.FigmaPocTheme
import com.david.h.figmapoc.ui.theme.RubikFontFamily
import com.david.h.figmapoc.ui.theme.TaskBg
import com.david.h.figmapoc.ui.theme.TaskCardBg
import com.david.h.figmapoc.ui.theme.TaskDark
import com.david.h.figmapoc.ui.theme.TaskDivider
import com.david.h.figmapoc.ui.theme.TaskGray
import com.david.h.figmapoc.ui.theme.TaskGreen
import com.david.h.figmapoc.ui.theme.TaskOrange
import com.david.h.figmapoc.ui.theme.TaskPrimary
import com.david.h.figmapoc.ui.theme.TaskPrimaryLight
import com.david.h.figmapoc.ui.theme.TaskYellow

// ── Data model ────────────────────────────────────────────────────────────────

data class Task(
    val id: Int,
    val title: String,
    val subtitle: String,
    val dueDate: String,
    val priority: Priority,
    val isDone: Boolean = false
)

enum class Priority { HIGH, MEDIUM, LOW }

// ── Sample data ───────────────────────────────────────────────────────────────

private val sampleTasks = listOf(
    Task(1, "Design new landing page", "UI / UX Design", "Today, 3:00 PM", Priority.HIGH),
    Task(2, "Weekly team meeting", "Management", "Today, 5:00 PM", Priority.MEDIUM),
    Task(3, "Write unit tests", "Development", "Tomorrow, 10:00 AM", Priority.LOW, isDone = true),
    Task(4, "Review pull requests", "Development", "Tomorrow, 2:00 PM", Priority.HIGH),
    Task(5, "Update project docs", "Documentation", "Mar 10, 11:00 AM", Priority.LOW),
    Task(6, "Client presentation", "Sales", "Mar 12, 9:00 AM", Priority.MEDIUM),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun TaskManagerScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = TaskBg,
        bottomBar = { TaskBottomBar(selectedTab) { selectedTab = it } },
        floatingActionButton = { TaskFab() },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp)
        ) {
            item { TopBar() }
            item { Spacer(Modifier.height(4.dp)) }
            item { SearchBar() }
            item { Spacer(Modifier.height(4.dp)) }
            item { SectionHeader("Today's Tasks", "${sampleTasks.count { !it.isDone }} pending") }
            items(sampleTasks.filter { !it.isDone }) { task ->
                TaskCard(task = task)
            }
            item { Spacer(Modifier.height(4.dp)) }
            item { SectionHeader("Completed", "${sampleTasks.count { it.isDone }} tasks") }
            items(sampleTasks.filter { it.isDone }) { task ->
                TaskCard(task = task)
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good morning, David 👋",
                fontSize = 13.sp,
                color = TaskGray,
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Normal
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Let's start",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TaskDark,
                fontFamily = RubikFontFamily
            )
        }

        // Notification bell
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(TaskCardBg)
                .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TaskDark,
                modifier = Modifier.size(22.dp)
            )
            // notification badge
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(TaskOrange)
                    .align(Alignment.TopEnd)
                    .offset(x = (-4).dp, y = 4.dp)
            )
        }
    }
}

// ── Search bar ────────────────────────────────────────────────────────────────

@Composable
private fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(TaskCardBg)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = TaskGray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Search tasks...",
            color = TaskGray,
            fontSize = 14.sp,
            fontFamily = RubikFontFamily
        )
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String, badge: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TaskDark,
            fontFamily = RubikFontFamily
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(TaskPrimaryLight)
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = badge,
                fontSize = 12.sp,
                color = TaskPrimary,
                fontWeight = FontWeight.Medium,
                fontFamily = RubikFontFamily
            )
        }
    }
}

// ── Task card ─────────────────────────────────────────────────────────────────

@Composable
private fun TaskCard(task: Task) {
    val priorityColor = when (task.priority) {
        Priority.HIGH -> TaskOrange
        Priority.MEDIUM -> TaskYellow
        Priority.LOW -> TaskGreen
    }
    val priorityLabel = when (task.priority) {
        Priority.HIGH -> "High"
        Priority.MEDIUM -> "Medium"
        Priority.LOW -> "Low"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TaskCardBg)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Check icon
        Icon(
            imageVector = if (task.isDone) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
            contentDescription = null,
            tint = if (task.isDone) TaskGreen else TaskDivider,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(14.dp))

        // Title + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (task.isDone) TaskGray else TaskDark,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                fontFamily = RubikFontFamily
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = task.subtitle,
                fontSize = 12.sp,
                color = TaskGray,
                fontFamily = RubikFontFamily
            )
            Spacer(Modifier.height(8.dp))
            // Due date row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = TaskGray,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = task.dueDate,
                    fontSize = 11.sp,
                    color = TaskGray,
                    fontFamily = RubikFontFamily
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        // Priority badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(priorityColor.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = priorityLabel,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = priorityColor,
                fontFamily = RubikFontFamily
            )
        }
    }
}

// ── FAB ───────────────────────────────────────────────────────────────────────

@Composable
private fun TaskFab() {
    FloatingActionButton(
        onClick = {},
        containerColor = TaskPrimary,
        contentColor = Color.White,
        shape = CircleShape,
        modifier = Modifier.size(56.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add task", modifier = Modifier.size(26.dp))
    }
}

// ── Bottom navigation bar ─────────────────────────────────────────────────────

@Composable
private fun TaskBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = TaskCardBg,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(12.dp)
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text("Home", fontSize = 11.sp, fontFamily = RubikFontFamily)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TaskPrimary,
                selectedTextColor = TaskPrimary,
                unselectedIconColor = TaskGray,
                unselectedTextColor = TaskGray,
                indicatorColor = TaskPrimaryLight
            )
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text("Calendar", fontSize = 11.sp, fontFamily = RubikFontFamily)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TaskPrimary,
                selectedTextColor = TaskPrimary,
                unselectedIconColor = TaskGray,
                unselectedTextColor = TaskGray,
                indicatorColor = TaskPrimaryLight
            )
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text("Profile", fontSize = 11.sp, fontFamily = RubikFontFamily)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TaskPrimary,
                selectedTextColor = TaskPrimary,
                unselectedIconColor = TaskGray,
                unselectedTextColor = TaskGray,
                indicatorColor = TaskPrimaryLight
            )
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskManagerScreenPreview() {
    FigmaPocTheme {
        TaskManagerScreen()
    }
}

