package com.olt.racketclash.ui.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.olt.racketclash.state.detail.DetailModel
import com.olt.racketclash.state.list.ListModel
import com.olt.racketclash.ui.base.layout.AddOrUpdateFormOverlay
import com.olt.racketclash.ui.base.layout.FilterFormOverlay
import com.olt.racketclash.ui.base.material.SimpleIconButton

@Composable
internal fun SimpleRacketClashDetailScaffold(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Base(
        title = title,
        content = content
    ) {}
}

@Composable
internal fun RacketClashDetailScaffold(
    title: String,
    model: DetailModel<*, *>,
    headerContent: @Composable (ColumnScope.() -> Unit)? = null,
    editOverlayContent: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var showEditOverlay by remember { mutableStateOf(false) }
    val state by model.state.collectAsState()

    Base(
        title = title,
        content = content,
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        headerContent = {
            if (headerContent != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Details(
                        isLoading = state.isLoading,
                        modifier = Modifier.padding(start = 40.dp, end = 40.dp, bottom = 24.dp),
                        content = headerContent
                    )
                }
            }
        }
    ) {
        AddOrUpdateFormOverlay(
            title = "Update",
            visible = showEditOverlay,
            dismissOverlay = { showEditOverlay = false },
            canConfirm = state.canUpdate,
            onConfirm = model::applyUpdatedItem,
            content = editOverlayContent
        )
    }
}

@Composable
internal fun RacketClashTableScaffold(
    title: String,
    model: ListModel<*, *, *>,
    filterOverlayContent: @Composable ColumnScope.() -> Unit,
    addOverlayContent: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }
    val state by model.state.collectAsState()

    Base(
        title = title,
        content = content,
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showAddOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            ) {
                showFilterOverlay = false
                showAddOverlay = !showAddOverlay
            }
        }
    ) {
        FilterFormOverlay(
            visible = showFilterOverlay,
            dismissOverlay = { showFilterOverlay = false },
            onConfirm = model::applyFilter,
            content = filterOverlayContent
        )

        AddOrUpdateFormOverlay(
            title = "Add",
            visible = showAddOverlay,
            dismissOverlay = { showAddOverlay = false },
            canConfirm = state.canAdd,
            onConfirm = model::addNewItem,
            content = addOverlayContent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Base(
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    headerContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
    overlay: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            Column {
                TopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    title = { Text(title) },
                    actions = actions
                )

                headerContent()
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            val scrollState = rememberScrollState()

            Box(modifier = Modifier.verticalScroll(state = scrollState).fillMaxSize()) {
                Column(
                    modifier = Modifier.padding(50.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    content = content
                )

                overlay()
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState = scrollState),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colorScheme.primary,
                    unhoverColor = MaterialTheme.colorScheme.primary,
                    shape = RectangleShape
                )
            )
        }
    }
}