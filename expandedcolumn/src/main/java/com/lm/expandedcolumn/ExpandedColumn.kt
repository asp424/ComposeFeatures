package com.lm.expandedcolumn

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpandedColumn(
	listItems: List<ExpandedItem>,
	itemSize: Dp = 40.dp,
	withChangeSpeed: Int = 300,
	paddingChangeSpeed: Int = 300,
	expandedItemPadding: Dp = 60.dp,
	expandedItemWidth: Dp = 120.dp,
	iconPadding: Dp = 3.dp,
	paddingBetweenItems: Dp = 10.dp,
	@SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
	var isLongPressed by remember { mutableStateOf(false) }
	var offsetY by remember { mutableStateOf(-1f) }
	var offsetYFact by remember { mutableStateOf(0f) }
	rememberLazyListState().apply {
		LocalDensity.current.also { dens ->
			with(dens) {
				(itemSize + paddingBetweenItems).toPx().also { sizePx ->
					LazyColumn(
						modifier
							.motionEventSpy {
								if (isLongPressed) offsetY =
									it.y + firstVisibleItemIndex * sizePx +
											firstVisibleItemScrollOffset - offsetYFact
								if (it.action == 1) isLongPressed = false
							}
							.width(expandedItemWidth + expandedItemPadding + itemSize)
							.onPlaced { offsetYFact = it.positionInWindow().y }, this@apply
					) {
						itemsIndexed(listItems) { i, item ->
							remember { mutableStateOf(false) }.apply {
								checkForOverlap(offsetY, i, sizePx, isLongPressed) {
									value = it
								}
								
								LaunchedEffect(isLongPressed) {
									if (!isLongPressed) value = false
								}
								
								Item(
									value,
									item,
									itemSize,
									withChangeSpeed,
									paddingChangeSpeed,
									expandedItemPadding,
									expandedItemWidth,
									iconPadding,
									paddingBetweenItems
								) {
									isLongPressed = true; value = true
								}
							}
						}
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun Item(
	startAnim: Boolean,
	item: ExpandedItem,
	itemSize: Dp,
	withChangeSpeed: Int,
	paddingChangeSpeed: Int,
	expandedItemPadding: Dp,
	expandedItemWidth: Dp,
	itemContentPadding: Dp,
	paddingBetweenItems: Dp,
	onMotionEvent: () -> Unit
) {
	Box(Modifier.padding(top = paddingBetweenItems / 2, bottom = paddingBetweenItems / 2)) {
		ElevatedCard(
			Modifier
				.width(
					animateDpAsState(
						if (startAnim) expandedItemWidth else itemSize, tween(withChangeSpeed)
					).value
				)
				.height(itemSize)
				.offset(
					animateDpAsState(
						if (startAnim) expandedItemPadding else 0.dp,
						tween(paddingChangeSpeed)
					).value, 0.dp
				)
				.pointerInput(Unit) {
					detectTapGestures(onLongPress = { onMotionEvent() })
				}, containerColor = item.background
		) {
			Row(
				Modifier
					.padding(itemContentPadding)
					.width(
						animateDpAsState(
							if (startAnim) expandedItemWidth else 60.dp,
							tween(withChangeSpeed)
						).value
					), Center, CenterVertically
			) {
				Icon(
					item.icon, null, tint = item.iconTint,
					modifier = Modifier.size(itemSize)
				)
				Text(
					item.text, color = item.textColor, maxLines = 1, fontSize =
					(itemSize.value / 3).sp,
					modifier = Modifier
						.padding()
						.width(
							animateDpAsState(
								if (startAnim) expandedItemWidth else 0.dp,
								tween(withChangeSpeed)
							).value
						)
				)
			}
		}
	}
}

@Composable
internal fun checkForOverlap(
	offsetY: Float,
	i: Int,
	sizePx: Float,
	isLongPressed: Boolean,
	onCheck: (Boolean) -> Unit
) = (offsetY in sizePx * i..sizePx * i + sizePx).also { isOverlap ->
	LaunchedEffect(isOverlap) {
		if (isLongPressed) if (isOverlap) onCheck(true)
		else {
			delay(100); onCheck(false)
		}
	}
}

data class ExpandedItem(
	val text: String = "",
	val icon: ImageVector,
	val background: Color = Black,
	val textColor: Color = White,
	val iconTint: Color = White
)











