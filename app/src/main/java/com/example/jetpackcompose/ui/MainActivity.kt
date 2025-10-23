package com.example.jetpackcompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.R
import com.example.jetpackcompose.model.Feature
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MotoristHomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MotoristHomeScreen(modifier: Modifier, viewModel: HomeViewModel = viewModel()) {
    val features by viewModel.features.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val initialCount = 10
    val visibleCount = if (expanded) features.size else minOf(initialCount, features.size)
    val columns = 5

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
        ,
        contentPadding = PaddingValues(16.dp)
    ) {
        // 1. Banner
        item {
            BannerWithSearch()
        }

        // 2. Grid Feature
        item {
            FeatureGridRows(
                items = features.take(visibleCount),
                columns = columns,
                horizontalSpacing = 12.dp,
                verticalSpacing = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        // 3. Show More Button
        item {
            ButtonShowMore(
                expanded,
                onToggle = { expanded = !expanded }
            )
        }


        // 4. Advertisement Row
        item {
            AdvertisementSection()
        }

        // 5. Big Feature Row
        item {
            ExploringSection()
        }
    }
}

@Composable
fun BannerWithSearch() {
    val searchShape = RoundedCornerShape(25.dp)
    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_background_motorist),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .zIndex(0f)
                .fillMaxWidth()
        )

        Surface (
            shape = searchShape,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 28.dp)
                .align(Alignment.BottomCenter)
                .zIndex(1f)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = {Icon(Icons.Default.Search, null)},
                placeholder = {Text("Search...")},
                shape = searchShape,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
    Spacer(Modifier.height(28.dp + 8.dp))
}

@Composable
private fun FeatureGridRows(
    items: List<Feature>,
    columns: Int,
    horizontalSpacing: Dp,
    verticalSpacing: Dp,
    modifier: Modifier = Modifier
) {
    val rows = remember(items, columns) { items.chunked(columns) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
            ) {
                row.forEach { item ->
                    Box(Modifier.weight(1f)) { FeatureCell(item) }
                }
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun FeatureCell(feature: Feature) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Image(painter = painterResource(id = feature.iconRes), contentDescription = null)
        Text(
            text = feature.title,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ButtonShowMore(
    expanded : Boolean,
    onToggle: () -> Unit
) {
    Box (

    ){
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            thickness = 1.dp,
            color = Color.LightGray
        )

        TextButton(
            onClick = { onToggle() },
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.textButtonColors(
                containerColor = Color(0xFFF0F0F0)
            )
        ) {
            Text(if (expanded) "Show less" else "Show more")
        }
    }

    Spacer(Modifier.height(10.dp))
}

@Composable
fun AdvertisementSection() {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_background_motorist),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Image(
            painter = painterResource(id = R.drawable.ic_background_motorist),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 120.dp, height = 100.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Image(
            painter = painterResource(id = R.drawable.ic_background_motorist),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Composable
fun ExploringSection() {
    val numberOfItems = 10

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Explore the Motorist App",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 15.dp)
        )
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(numberOfItems) { item ->
                ItemExplore()
            }
        }
    }
}

@Composable
fun ItemExplore() {
    Image(
        painter = painterResource(id = R.drawable.ic_background_motorist),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeTheme {
        MotoristHomeScreen(modifier = Modifier)
    }
}