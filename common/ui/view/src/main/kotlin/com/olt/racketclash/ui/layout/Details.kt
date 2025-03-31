package com.olt.racketclash.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.base.material.Loading
import com.olt.racketclash.ui.material.RatioBar

@Composable
fun Details(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    isLoading: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        if (isLoading)
            Loading()
        else
            content()
    }
}

@Composable
fun DetailSectionColumn(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold)

        content()
    }
}

@Composable
fun DetailSectionRow(
    title: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(24.dp),
    content: @Composable RowScope.() -> Unit
) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold)

        Row(
            horizontalArrangement = horizontalArrangement,
            content = content
        )
    }
}

@Composable
fun DetailText(
    title: String,
    text: String
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = "$title:",
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontWeight = FontWeight.Bold
        )

        Text(text = text, fontSize = MaterialTheme.typography.labelMedium.fontSize,)
    }
}

@Composable
fun StatisticsDetail(
    doubleGamePoints: Triple<Int, Int, Int>,
    doubleSetPoints: Triple<Int, Int, Int>,
    doublePointPoints: Triple<Int, Int, Int>,
    singleGamePoints: Triple<Int, Int, Int>,
    singleSetPoints: Triple<Int, Int, Int>,
    singlePointPoints: Triple<Int, Int, Int>
) {
    Text("Double", fontWeight = FontWeight.Bold)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text("W/D/L", modifier = Modifier.padding(end = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = doubleGamePoints.first, middle = doubleGamePoints.second, right = doubleGamePoints.third
        )
        Text("Sets", modifier = Modifier.padding(horizontal = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = doubleSetPoints.first, middle = doubleSetPoints.second, right = doubleSetPoints.third
        )
        Text("Points", modifier = Modifier.padding(horizontal = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = doublePointPoints.first, middle = doublePointPoints.second, right = doublePointPoints.third
        )
    }

    Text("Single", fontWeight = FontWeight.Bold)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text("W/D/L", modifier = Modifier.padding(end = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = singleGamePoints.first, middle = singleGamePoints.second, right = singleGamePoints.third
        )
        Text("Sets", modifier = Modifier.padding(horizontal = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = singleSetPoints.first, middle = singleSetPoints.second, right = singleSetPoints.third
        )
        Text("Points", modifier = Modifier.padding(horizontal = 5.dp))
        RatioBar(
            modifier = Modifier.weight(0.5f),
            left = singlePointPoints.first, middle = singlePointPoints.second, right = singlePointPoints.third
        )
    }
}