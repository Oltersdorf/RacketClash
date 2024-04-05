package com.olt.racketclash.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun SimpleFilledIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    FilledIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "Back",
        onClick = onClick
    )
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.Add,
        contentDescription = "Add",
        onClick = onClick
    )
}

@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.Delete,
        contentDescription = "Delete",
        onClick = onClick
    )
}

@Composable
fun EditButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.Edit,
        contentDescription = "Edit",
        onClick = onClick
    )
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.Check,
        contentDescription = "Save",
        onClick = onClick
    )
}

@Composable
fun ArrowLeftButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.KeyboardArrowLeft,
        contentDescription = "-",
        onClick = onClick
    )
}

@Composable
fun ArrowRightButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit
) {
    SimpleIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "+",
        onClick = onClick
    )
}

@Composable
fun FilledArrowLeftButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    onClick: () -> Unit
) {
    SimpleFilledIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.KeyboardArrowLeft,
        contentDescription = "-",
        onClick = onClick
    )
}

@Composable
fun FilledArrowRightButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
    onClick: () -> Unit
) {
    SimpleFilledIconButton(
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "+",
        onClick = onClick
    )
}