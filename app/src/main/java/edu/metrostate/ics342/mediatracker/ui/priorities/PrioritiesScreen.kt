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
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.MediaType
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.ui.library.LibraryUiState
import edu.metrostate.ics342.mediatracker.ui.library.LibraryViewModel

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritiesScreen(
    onBack: () -> Unit,
    onMediaClick: (Int) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
    var draggingOffset by remember { mutableFloatStateOf(0f) }

    // Dialog state
    var editingPriority by remember { mutableStateOf<Priority?>(null) }

    // Use updated state to avoid restarting pointerInput when priorities change
    val updatedFilter by rememberUpdatedState(selectedFilter)

    if (editingPriority != null) {
        val priority = editingPriority!!
        var level by remember { mutableIntStateOf(priority.priority) }
        AlertDialog(
            onDismissRequest = { editingPriority = null },
            title = { Text("Edit Priority") },
            text = {
                Column {
                    Text("Urgency Level (1-3)", style = MaterialTheme.typography.labelMedium)
                    Row {
                        (1..3).forEach { l ->
                            FilterChip(
                                selected = level == l,
                                onClick = { level = l },
                                label = { Text(l.toString()) },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { 
                    viewModel.updatePriority(priority.mediaId, level, null)
                    editingPriority = null 
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingPriority = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Priorities", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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

            if (selectedFilter == 0 && uiState is LibraryUiState.Success && (uiState as LibraryUiState.Success).priorities.isNotEmpty()) {
                Text(
                    text = "Long press and drag to reorder",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

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
                            Text(
                                text = "No priorities set — mark a 'Want To' item as a priority to see it here.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(32.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { offset ->
                                            if (updatedFilter != 0) return@detectDragGesturesAfterLongPress
                                            listState.layoutInfo.visibleItemsInfo
                                                .firstOrNull { item ->
                                                    offset.y.toInt() in item.offset..(item.offset + item.size)
                                                }
                                                ?.let {
                                                    draggedItemIndex = it.index
                                                }
                                        },
                                        onDrag = { change, dragAmount ->
                                            if (updatedFilter != 0) return@detectDragGesturesAfterLongPress
                                            change.consume()
                                            draggingOffset += dragAmount.y

                                            val currentDraggedIndex = draggedItemIndex ?: return@detectDragGesturesAfterLongPress
                                            val currentItemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == currentDraggedIndex } ?: return@detectDragGesturesAfterLongPress
                                            
                                            val draggedCenter = currentItemInfo.offset + draggingOffset + currentItemInfo.size / 2
                                            
                                            val targetItemInfo = listState.layoutInfo.visibleItemsInfo.find { item ->
                                                draggedCenter.roundToInt() in item.offset..(item.offset + item.size) && item.index != currentDraggedIndex
                                            }

                                            if (targetItemInfo != null) {
                                                viewModel.movePriority(currentDraggedIndex, targetItemInfo.index)
                                                draggedItemIndex = targetItemInfo.index
                                                draggingOffset = 0f
                                            }
                                        },
                                        onDragEnd = {
                                            draggedItemIndex = null
                                            draggingOffset = 0f
                                        },
                                        onDragCancel = {
                                            draggedItemIndex = null
                                            draggingOffset = 0f
                                        }
                                    )
                                },
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(filteredPriorities, key = { _, p -> p.mediaId }) { index, priority ->
                                val isDragging = draggedItemIndex == index
                                PriorityCard(
                                    priority = priority,
                                    onClick = { onMediaClick(priority.mediaId) },
                                    onEdit = { editingPriority = priority },
                                    onRemove = { viewModel.removePriority(priority.mediaId) },
                                    modifier = Modifier
                                        .graphicsLayer {
                                            translationY = if (isDragging) draggingOffset else 0f
                                            scaleX = if (isDragging) 1.05f else 1f
                                            scaleY = if (isDragging) 1.05f else 1f
                                            alpha = if (isDragging) 0.9f else 1f
                                            shadowElevation = if (isDragging) 8.dp.toPx() else 0f
                                        }
                                        .zIndex(if (isDragging) 1f else 0f)
                                )
                            }
                        }
                    }
                }
                is LibraryUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.retry() }) {
                                Text("Retry")
                            }
                        }
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
    onEdit: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
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
                tint = Color(0xFF9E9E9E), // Darker gray for better visibility
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

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Options",
                        tint = Color(0xFF757575) // Darker gray for better visibility
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Priority") },
                        onClick = { menuExpanded = false; onEdit() }
                    )
                    DropdownMenuItem(
                        text = { Text("Remove from Priorities") },
                        onClick = { menuExpanded = false; onRemove() }
                    )
                }
            }
        }
    }
}

