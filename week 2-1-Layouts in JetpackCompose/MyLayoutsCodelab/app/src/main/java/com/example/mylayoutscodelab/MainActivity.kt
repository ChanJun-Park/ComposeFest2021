package com.example.mylayoutscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.example.mylayoutscodelab.ui.Chip
import com.example.mylayoutscodelab.ui.StaggeredGrid
import com.example.mylayoutscodelab.ui.theme.MyLayoutsCodelabTheme
import com.example.mylayoutscodelab.ui.topics
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MyLayoutsCodelabTheme {
				ScrollingList()
			}
		}
	}
}

@Composable
fun ScrollingList() {
	val listSize = 100
	// We save the scrolling position with this state
	val scrollState = rememberLazyListState()
	// We save the coroutine scope where our animated scroll will be executed
	val coroutineScope = rememberCoroutineScope()

	Column {
		Row {
			Button(onClick = {
				coroutineScope.launch {
					// 0 is the first item index
					scrollState.animateScrollToItem(0)
				}
			}) {
				Text("Scroll to the top")
			}

			Button(onClick = {
				coroutineScope.launch {
					// listSize - 1 is the last index of the list
					scrollState.animateScrollToItem(listSize - 1)
				}
			}) {
				Text("Scroll to the end")
			}
		}

		LazyColumn(state = scrollState) {
			items(listSize) {
				ImageListItem(it)
			}
		}
	}
}

@Composable
fun ImageListItem(index: Int) {
	Row(verticalAlignment = Alignment.CenterVertically) {

		Image(
			painter = rememberImagePainter(
				data = "https://developer.android.com/images/brand/Android_Robot.png"
			),
			contentDescription = "Android Logo",
			modifier = Modifier.size(50.dp)
		)
		Spacer(Modifier.width(10.dp))
		Text("Item #$index", style = MaterialTheme.typography.subtitle1)
	}
}

@Composable
fun LayoutsCodeLab() {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text("LayoutsCodelab")
				},
				actions = {
					IconButton(onClick = {}) {
						Icon(Icons.Filled.Favorite, contentDescription = null)
					}
				}
			)
		},
		bottomBar = {
			BottomAppBar {
				var selectedItem by remember { mutableStateOf(0) }
				val items = listOf("Songs", "Artists", "Playlists")

				BottomNavigation {
					items.forEachIndexed { index, item ->
						BottomNavigationItem(
							icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
							label = { Text(item) },
							selected = selectedItem == index,
							onClick = { selectedItem = index }
						)
					}
				}
			}
		}
	) { innerPadding ->
		BodyContent(Modifier.padding(innerPadding))
	}
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
	Row(modifier = modifier
		.background(color = Color.LightGray)
		.padding(16.dp)
		.size(200.dp)
		.horizontalScroll(rememberScrollState()),
		content = {
			StaggeredGrid {
				for (topic in topics) {
					Chip(modifier = Modifier.padding(8.dp), text = topic)
				}
			}
		})
}

@Preview
@Composable
fun LayoutsCodeLabPreview() {
	MyLayoutsCodelabTheme {
		LayoutsCodeLab()
	}
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
	Row(
		modifier
			.padding(8.dp)
			.clip(RoundedCornerShape(4.dp))
			.background(MaterialTheme.colors.surface)
			.clickable(onClick = {})
			.padding(16.dp)
	) {
		Surface(
			modifier = Modifier.size(50.dp),
			shape = CircleShape,
			color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
		) {

		}
		Column(
			modifier = Modifier
				.padding(start = 8.dp)
				.align(Alignment.CenterVertically)
		) {
			Text("Alfred Sisley", fontWeight = FontWeight.Bold)
			// LocalContentAlpha is defining opacity level of its children
			CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
				Text("3 minutes ago", style = MaterialTheme.typography.body2)
			}
		}
	}
}

@Preview
@Composable
fun PhotographerCardPreview() {
	MyLayoutsCodelabTheme {
		PhotographerCard()
	}
}

@Composable
fun ConstraintLayoutContent() {
	ConstraintLayout {
		// Creates references for the three composables
		// in the ConstraintLayout's body
		val (button1, button2, text) = createRefs()

		Button(
			onClick = { /* Do something */ },
			modifier = Modifier.constrainAs(button1) {
				top.linkTo(parent.top, margin = 16.dp)
			}
		) {
			Text("Button 1")
		}

		Text("Text", Modifier.constrainAs(text) {
			top.linkTo(button1.bottom, margin = 16.dp)
			centerAround(button1.end)
		})

		val barrier = createEndBarrier(button1, text)
		Button(
			onClick = { /* Do something */ },
			modifier = Modifier.constrainAs(button2) {
				top.linkTo(parent.top, margin = 16.dp)
				start.linkTo(barrier)
			}
		) {
			Text("Button 2")
		}
	}
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
	MyLayoutsCodelabTheme {
		ConstraintLayoutContent()
	}
}

@Composable
fun LargeConstraintLayout() {
	ConstraintLayout {
		val text = createRef()

		val guideline = createGuidelineFromStart(fraction = 0.5f)
		Text(
			"This is a very very very very very very very long text",
			Modifier.constrainAs(text) {
				linkTo(start = guideline, end = parent.end)
				width = Dimension.preferredWrapContent
			}
		)
	}
}

@Preview
@Composable
fun LargeConstraintLayoutPreview() {
	MyLayoutsCodelabTheme {
		LargeConstraintLayout()
	}
}

@Composable
fun DecoupledConstraintLayout() {
	BoxWithConstraints {
		val constraints = if (maxWidth < maxHeight) {
			decoupledConstraints(margin = 16.dp) // Portrait constraints
		} else {
			decoupledConstraints(margin = 32.dp) // Landscape constraints
		}

		ConstraintLayout(constraints) {
			Button(
				onClick = { /* Do something */ },
				modifier = Modifier.layoutId("button")
			) {
				Text("Button")
			}

			Text("Text", Modifier.layoutId("text"))
		}
	}
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
	return ConstraintSet {
		val button = createRefFor("button")
		val text = createRefFor("text")

		constrain(button) {
			top.linkTo(parent.top, margin= margin)
		}
		constrain(text) {
			top.linkTo(button.bottom, margin)
		}
	}
}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
	Row(modifier = modifier.height(IntrinsicSize.Min)) {
		Text(
			modifier = Modifier
				.weight(1f)
				.padding(start = 4.dp)
				.wrapContentWidth(Alignment.Start),
			text = text1
		)

		Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
		Text(
			modifier = Modifier
				.weight(1f)
				.padding(end = 4.dp)
				.wrapContentWidth(Alignment.End),
			text = text2
		)
	}
}

@Preview
@Composable
fun TwoTextsPreview() {
	MyLayoutsCodelabTheme {
		Surface {
			TwoTexts(text1 = "Hi", text2 = "there")
		}
	}
}