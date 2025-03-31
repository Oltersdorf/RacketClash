package com.olt.racketclash.ui.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
internal fun RacketClashScrollableScaffold(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    headerContent: @Composable BoxScope.() -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    RacketClashScaffoldBase(
        title = title,
        actions = actions,
        headerContent = headerContent
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            val scrollState = rememberScrollState()

            Body(
                modifier = Modifier.verticalScroll(state = scrollState),
                overlay = overlay,
                contentAlignment = contentAlignment,
                content = content
            )

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

@Composable
internal fun RacketClashScaffold(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    headerContent: @Composable BoxScope.() -> Unit = {},
    overlay: @Composable BoxScope.() -> Unit = {},
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit
) {
    RacketClashScaffoldBase(
        title = title,
        actions = actions,
        headerContent = headerContent
    ) {
        Body(
            modifier = Modifier.padding(it),
            overlay = overlay,
            contentAlignment = contentAlignment,
            content = content
        )
    }
}

@Composable
private fun RacketClashScaffoldBase(
    title: String,
    actions: @Composable RowScope.() -> Unit,
    headerContent: @Composable BoxScope.() -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            Header(
                title = title,
                actions = actions,
                content = headerContent
            )
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    title: String,
    actions: @Composable RowScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Column {
        TopAppBar(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            title = { Text(title) },
            actions = actions
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp),
                content = content
            )
        }
    }
}

@Composable
private fun Body(
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.() -> Unit,
    contentAlignment: Alignment,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.padding(50.dp),
            contentAlignment = contentAlignment,
            content = content
        )

        overlay()
    }
}