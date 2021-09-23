package icc.personal.jetpack.basics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import icc.personal.jetpack.basics.ui.theme.JetpackBasicsTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    JetpackBasicsTheme {
        Text(text = "Hello world!")
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen()
}