package icc.personal.jetpack.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import icc.personal.jetpack.layouts.ui.theme.JetpackLayoutsTheme
import kotlinx.coroutines.launch
import kotlin.math.max

@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StaggeredScreen()
        }
    }
}

/**
 * Layout basics / Material
 * - Column
 * - Row
 * - Box
 * - Scaffold
 * - Lazy Column
 */
@ExperimentalCoilApi
@Composable
fun MainScreen() {
    JetpackLayoutsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Layouts Codelab")
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.Favorite,
                                null
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                null
                            )
                        }
                    }
                )
            },

        ) { innerPadding ->
            BodyContent(Modifier.padding(innerPadding))
        }
    }
}

@ExperimentalCoilApi
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
//    SimpleList(modifier.padding(8.dp))
//    LazyList(modifier.padding(8.dp))
    val listSize = 100
    // We save the scrolling position with this state
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier.padding(8.dp)){
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(0.5f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
            ) {
                Text(text = "Scroll to top")
            }
            Spacer(modifier = Modifier.padding(start = 8.dp))
            Button(
                modifier = Modifier.weight(0.5f),
                onClick = {
                    coroutineScope.launch {
                        scrollState.scrollToItem(listSize - 1)
                    }
                }
            ) {
                Text(text = "Scroll to bottom")
            }
        }
        ImageList(
            modifier = modifier.fillMaxWidth(),
            size = listSize,
            scrollState = scrollState
        )
    }
}

@Composable
fun SimpleList(modifier: Modifier) {
    /**
     * Add scroll state if you want to make the column scrollable
     * The problem with columns/rows is that they will render all the
     * elements within it including the ones which are not visible.
     * This is a performance problem and the solution is use
     * LazyColumn/LazyRow composables.
     */
    val scrollState = rememberScrollState()
    Column(
        modifier.verticalScroll(scrollState)
    ) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun LazyList(modifier: Modifier) {
    LazyColumn(modifier) {
        items(100) {
            Text(text = "Item #$it")
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageList(modifier: Modifier, size: Int, scrollState: LazyListState) {
    LazyColumn(
        modifier = modifier,
        state = scrollState
    ) {
        items(100) {
            ImageListItem(modifier, it)
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageListItem(modifier: Modifier = Modifier, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                1.dp,
                MaterialTheme.colors.secondary,
                RoundedCornerShape(4.dp)
            )
    ) {
        Image(
            // Coil usage, getting image from the network
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier
                .size(50.dp)
                .padding(4.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}

/**
 * Custom Layouts
 * - Layout Modifier
 * - Simple Column Layout
 */
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        // Check the composable has a first baseline
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }

    }
)

@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
    JetpackLayoutsTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithNormalPaddingPreview() {
    JetpackLayoutsTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Measure all the children
        val placeables = measurables.map {
            // Measure children
            it.measure(constraints)
        }

        // Init position for y
        var yPosition = 0

        // Set the size of the layout as big as it can be
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach {
                it.placeRelative(
                    x = 0, y = yPosition
                )
                // Move the y coord to place the next placeable
                yPosition += it.height
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun MyOwnColumnPreview() {
    MyOwnColumn(Modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

/**
 * Complex Layouts
 * - Staggered Grid
 */
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->

            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }

    }

}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    Chip(text = "Hi there")
}
val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)


@Composable
fun BodyContentStaggered(modifier: Modifier = Modifier) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid(modifier = modifier) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Composable
fun StaggeredScreen() {
    JetpackLayoutsTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Staggered Screen")
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.Favorite,
                                null
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                null
                            )
                        }
                    }
                )
            },

            ) { innerPadding ->
            BodyContentStaggered(Modifier.padding(innerPadding))
        }
    }
}

@Preview
@Composable
fun StaggeredScreenPreview() {
    StaggeredScreen()
}
