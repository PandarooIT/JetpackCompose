package com.example.jetpackcompose.ui

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcompose.MVVMApplication
import com.example.jetpackcompose.R
import com.example.jetpackcompose.di.component.ActivityComponent
import com.example.jetpackcompose.di.component.DaggerActivityComponent
import com.example.jetpackcompose.model.ExploreItem
import com.example.jetpackcompose.model.Feature
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var activityComponent: ActivityComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = (application as MVVMApplication).appComponent
        activityComponent = DaggerActivityComponent.factory()
            .create(appComponent, this)
        activityComponent.inject(this)

        testingFirebaseStore()

        enableEdgeToEdge()
        setContent {
            JetpackComposeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val homeViewModel: HomeViewModel = viewModel(factory = vmFactory)
                    MotoristHomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        homeViewModel
                    )
                }
            }
        }
    }

    private fun testingFirebaseStore() {
        val db = Firebase.firestore

        val feature = Feature(
            id = 1,
            iconRes = R.drawable.ic_traffic_camera,
            title = "Smart Parking"
        )

        // add
//        db.collection("features")
//            .add(feature)
//            .addOnSuccessListener { docRef ->
//                Log.d("Firestore", "Document added with ID: ${docRef.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("Firestore", "Error adding document", e)
//            }

        // get
        db.collection("features")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Firestore-Get", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore-Get", "Error getting documents.", exception)
            }
    }
}

@Composable
fun MotoristHomeScreen(modifier: Modifier, viewModel: HomeViewModel) {
    val features by viewModel.features.collectAsState()
    val exploreItems by viewModel.exploreItems.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // searching
    var query by rememberSaveable { mutableStateOf("") }
    val filteredItems by remember(query, features) {
        derivedStateOf {
            if (query.isBlank()) features
            else features.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
    }

    val initialCount = 10
    val visibleCount = if (expanded) filteredItems.size else minOf(initialCount, filteredItems.size)
    val columns = 5

    /*
    Explanation
    1.State
    features (State is converted by StateFlow)
    query and filteredItems are rememberSaveable and remember
    visible count depends on expanded (expanded is remember too)

    2. Interval mechanics
    every 5s, features change -> recompose
    FeatureGridRows is built based on filteredItems and visibleCount

    3. Recomposition mechanics
    if filteredItems and visibleCount change
    FeatureGridRows will be recomposed with the states (expanded and query) is the same at the last changes
    */

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
        ,
        contentPadding = PaddingValues(16.dp)
    ) {
        // 1. Banner
        item {
            BannerWithSearch(
                query = query,
                onQueryChange = { query = it },
                onSearch = {}
            )
        }

        // 2. Grid Feature
        item {
            FeatureGridRows(
                items = filteredItems.take(visibleCount),
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
            ExploringSection(exploreItems)
        }
    }
}

@Composable
fun BannerWithSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val searchShape = RoundedCornerShape(25.dp)
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.height(200.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_background_motorist),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .zIndex(0f)
                .fillMaxWidth()
        )

        Surface(
            shape = searchShape,
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 28.dp)
                .align(Alignment.BottomCenter)
                .zIndex(1f)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search...") },
                singleLine = true,
                shape = searchShape,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
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
fun ExploringSection(
    items: List<ExploreItem>
) {
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
            items(items) { item ->
                ItemExplore(item)
            }
        }
    }
}

@Composable
fun ItemExplore(item: ExploreItem) {
    Image(
        painter = painterResource(id = item.iconRes),
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
//        MotoristHomeScreen(modifier = Modifier, hom)
    }
}