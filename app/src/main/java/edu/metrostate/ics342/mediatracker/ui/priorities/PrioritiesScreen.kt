package edu.metrostate.ics342.mediatracker.ui.priorities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.MediaType
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.ui.library.LibraryUiState
import edu.metrostate.ics342.mediatracker.ui.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritiesScreen(
    onBack: () -> Unit,
    onMediaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableIntStateOf(0) } // 0=All, 1=High, 2=Medium, 3=Low

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Priorities", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Filter options */ }) {
                        Icon(Icons.Outlined.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityFilterChip(
                    selected = selectedFilter == 0,
                    onClick = { selectedFilter = 0 },
                    label = "All"
                )
                PriorityFilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = "High",
                    dotColor = Color(0xFFE53935)
                )
                PriorityFilterChip(
                    selected = selectedFilter == 2,
                    onClick = { selectedFilter = 2 },
                    label = "Medium",
                    dotColor = Color(0xFFFFB300)
                )
                PriorityFilterChip(
                    selected = selectedFilter == 3,
                    onClick = { selectedFilter = 3 },
                    label = "Low",
                    dotColor = Color(0xFF43A047)
                )
            }

            Text(
                text = "Drag to reorder",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (val state = uiState) {
                is LibraryUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LibraryUiState.Success -> {
                    val filteredPriorities = if (selectedFilter == 0) {
                        state.priorities
                    } else {
                        state.priorities.filter { it.priority == selectedFilter }
                    }

                    if (filteredPriorities.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No priorities found", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(filteredPriorities, key = { _, p -> p.mediaId }) { _, priority ->
                                PriorityCard(
                                    priority = priority,
                                    onClick = { onMediaClick(priority.mediaId) },
                                    onRemove = { viewModel.removePriority(priority.mediaId) }
                                )
                            }
                        }
                    }
                }
                is LibraryUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun PriorityFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    dotColor: Color? = null
) {
    Surface(
        selected = selected,
        onClick = onClick,
        shape = CircleShape,
        color = if (selected) Color(0xFFE8EAF6) else Color.White,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)) else null,
        modifier = Modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color(0xFF3949AB) else Color(0xFF616161)
            )
        }
    }
}

@Composable
fun PriorityCard(
    priority: Priority,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = "Reorder",
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(Modifier.width(16.dp))

            val (containerColor, iconRes) = when (priority.media?.mediaType) {
                MediaType.BOOK -> Color(0xFFE8EAF6) to R.drawable.menu_book_24px
                MediaType.MOVIE -> Color(0xFFFCE4EC) to R.drawable.movie_24px
                else -> Color(0xFFE0F2F1) to R.drawable.tv_24px
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = when (priority.media?.mediaType) {
                        MediaType.BOOK -> Color(0xFF3949AB)
                        MediaType.MOVIE -> Color(0xFFC2185B)
                        else -> Color(0xFF00796B)
                    }
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = priority.media?.title ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                
                Spacer(Modifier.height(4.dp))
                
                val (priorityLabel, priorityColor, tagBg) = when (priority.priority) {
                    1 -> Triple("High Priority", Color(0xFF7B1FA2), Color(0xFFF3E5F5))
                    2 -> Triple("Medium Priority", Color(0xFF1976D2), Color(0xFFE3F2FD))
                    else -> Triple("Low Priority", Color(0xFF388E3C), Color(0xFFE8F5E9))
                }
                
                Surface(
                    color = tagBg,
                    shape = CircleShape
                ) {
                    Text(
                        text = priorityLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = priorityColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))

                val stats = buildString {
                    priority.estimatedTimeHours?.let { append("Est. $it hours") }
                    if (!priority.notes.isNullOrBlank()) {
                        if (isNotEmpty()) append(" • ")
                        append("\"${priority.notes}\"")
                    }
                }
                if (stats.isNotEmpty()) {
                    Text(
                        text = stats,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575)
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Options",
                    tint = Color(0xFFBDBDBD)
                )
            }
        }
    }
}

