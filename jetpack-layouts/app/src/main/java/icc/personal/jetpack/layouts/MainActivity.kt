package icc.personal.jetpack.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import icc.personal.jetpack.layouts.ui.theme.JetpackLayoutsTheme
import kotlinx.coroutines.launch

@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

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
            modifier = Modifier.size(50.dp).padding(4.dp)
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
