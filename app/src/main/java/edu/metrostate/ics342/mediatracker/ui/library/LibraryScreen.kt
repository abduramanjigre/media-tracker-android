package edu.metrostate.ics342.mediatracker.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.model.toIconRes
import edu.metrostate.ics342.mediatracker.data.model.creatorCredit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onMediaClick: (Int) -> Unit,
    onNavigateToPriorities: () -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedStatus by remember { mutableStateOf(LibraryStatus.WANT_TO) }
    var selectedType   by remember { mutableStateOf("all") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.library_title)) },
                actions = {
                    IconButton(onClick = onNavigateToPriorities) {
                        Icon(Icons.Outlined.FilterList, "Priorities") // Reusing filter icon as a placeholder for "Priorities" link
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "all"   to edu.metrostate.ics342.mediatracker.R.string.filter_all,
                    "book"  to edu.metrostate.ics342.mediatracker.R.string.filter_books,
                    "movie" to edu.metrostate.ics342.mediatracker.R.string.filter_movies,
                    "show"  to edu.metrostate.ics342.mediatracker.R.string.filter_shows
                )
                    .forEach { (key, labelRes) ->
                        FilterChip(
                            selected = selectedType == key,
                            onClick  = { selectedType = key },
                            label    = { Text(stringResource(labelRes)) }
                        )
                    }
            }

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                LibraryStatus.values().forEachIndexed { index, status ->
                    SegmentedButton(
                        shape    = SegmentedButtonDefaults.itemShape(
                            index = index, count = LibraryStatus.values().size),
                        selected = selectedStatus == status,
                        onClick  = {
                            selectedStatus = status
                            viewModel.loadLibrary(status)
                        },
                        label    = { Text(stringResource(status.labelRes)) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

            when (val state = uiState) {
                is LibraryUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is LibraryUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text  = state.message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.retry() }) {
                                Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.detail_retry))
                            }
                        }
                    }
                }

                is LibraryUiState.Success -> {
                    val filteredItems = state.items
                        .filter { selectedType == "all" || it.media?.mediaType?.apiString == selectedType }

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (selectedStatus == LibraryStatus.WANT_TO && state.priorities.isNotEmpty()) {
                            item {
                                Text(
                                    "PRIORITIES",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            items(state.priorities, key = { "priority-${it.mediaId}" }) { priority ->
                                PriorityItemCard(
                                    priority = priority,
                                    onClick = { onMediaClick(priority.mediaId) },
                                    onRemove = { viewModel.removePriority(priority.mediaId) }
                                )
                            }
                            item {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                            }
                        }

                        if (filteredItems.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier.fillParentMaxSize().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        stringResource(
                                            edu.metrostate.ics342.mediatracker.R.string.library_empty_status,
                                            stringResource(selectedStatus.labelRes)
                                        ),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            item {
                                Text(
                                    if (filteredItems.size == 1) stringResource(edu.metrostate.ics342.mediatracker.R.string.library_item_count, filteredItems.size)
                                    else stringResource(edu.metrostate.ics342.mediatracker.R.string.library_items_count, filteredItems.size),
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            items(filteredItems, key = { it.mediaId }) { item ->
                                val isPriority = state.priorities.any { it.mediaId == item.mediaId }
                                LibraryItemCard(
                                    item = item,
                                    isPriority = isPriority,
                                    onClick = { onMediaClick(item.mediaId) },
                                    onRemove = { viewModel.removeItem(item.mediaId) },
                                    onStatusChange = { newStatus -> viewModel.updateStatus(item.mediaId, newStatus) },
                                    onAddPriority = { level, hours -> viewModel.addPriority(item.mediaId, level, hours) }
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PriorityItemCard(
    priority: Priority,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${priority.orderIndex + 1}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.width(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    priority.media?.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Priority ${priority.priority}" + (priority.estimatedTimeHours?.let { " • ${it}h" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Outlined.MoreVert, null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

@Composable
private fun LibraryItemCard(
    item: LibraryItem,
    isPriority: Boolean,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    onStatusChange: (LibraryStatus) -> Unit,
    onAddPriority: (Int, Int?) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var statusDialogVisible by remember { mutableStateOf(false) }
    var priorityDialogVisible by remember { mutableStateOf(false) }

    if (statusDialogVisible) {
        AlertDialog(
            onDismissRequest = { statusDialogVisible = false },
            title = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.action_change_status)) },
            text = {
                Column {
                    LibraryStatus.values().forEach { s ->
                        TextButton(
                            onClick = { onStatusChange(s); statusDialogVisible = false },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(stringResource(s.labelRes)) }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { statusDialogVisible = false }) { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.settings_cancel_button)) }
            }
        )
    }

    if (priorityDialogVisible) {
        var level by remember { mutableIntStateOf(1) }
        AlertDialog(
            onDismissRequest = { priorityDialogVisible = false },
            title = { Text("Set Priority") },
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
                Button(onClick = { onAddPriority(level, null); priorityDialogVisible = false }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { priorityDialogVisible = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp, 90.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (item.media?.coverUrl != null) {
                    AsyncImage(
                        model = item.media.coverUrl,
                        contentDescription = item.media.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(item.media?.mediaType.toIconRes()),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.media?.title ?: "", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold, maxLines = 2
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    item.media?.creatorCredit(LocalContext.current) ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SuggestionChip(
                        onClick = { statusDialogVisible = true },
                        label = {
                            Text(
                                stringResource(item.status.labelRes),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                    if (item.status == LibraryStatus.WANT_TO && isPriority) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("Priority", style = MaterialTheme.typography.labelSmall) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        Icons.Outlined.MoreVert,
                        stringResource(edu.metrostate.ics342.mediatracker.R.string.action_more_options)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    if (item.status == LibraryStatus.WANT_TO && !isPriority) {
                        DropdownMenuItem(
                            text = { Text("Add to Priorities") },
                            onClick = { menuExpanded = false; priorityDialogVisible = true }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(stringResource(edu.metrostate.ics342.mediatracker.R.string.action_change_status)) },
                        onClick = { menuExpanded = false; statusDialogVisible = true }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                stringResource(edu.metrostate.ics342.mediatracker.R.string.action_remove_from_library),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = { menuExpanded = false; onRemove() }
                    )
                }
            }
        }
    }
}

