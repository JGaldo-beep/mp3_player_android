package com.example.player_test.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun <T> FastScrollLazyColumn(
    items: List<T>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    keySelector: (T) -> Any,
    indexSelector: (T) -> String, // Function to get the first character for indexing
    content: LazyListScope.() -> Unit
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    
    // Create alphabet index
    val alphabet = ('A'..'Z').toList()
    val itemIndexMap = remember(items) {
        items.mapIndexed { index, item ->
            indexSelector(item).firstOrNull()?.uppercase()?.first() to index
        }.groupBy({ it.first }, { it.second })
            .mapValues { it.value.first() }
    }
    
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var currentLetter by remember { mutableStateOf<Char?>(null) }
    
    Box(
        modifier = modifier.onSizeChanged { containerSize = it }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = content
        )
        
        // Fast scroll bar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .align(Alignment.CenterEnd)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { 
                            isDragging = true 
                        },
                        onDragEnd = { 
                            isDragging = false
                            currentLetter = null
                        }
                    ) { change, _ ->
                        val y = change.position.y
                        val totalHeight = containerSize.height.toFloat()
                        val letterIndex = (y / totalHeight * alphabet.size).toInt()
                            .coerceIn(0, alphabet.size - 1)
                        val letter = alphabet[letterIndex]
                        currentLetter = letter
                        
                        itemIndexMap[letter]?.let { index ->
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                alphabet.forEach { letter ->
                    val hasItems = itemIndexMap.containsKey(letter)
                    Text(
                        text = letter.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (hasItems) {
                            if (currentLetter == letter) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        },
                        fontWeight = if (currentLetter == letter) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        
        // Current letter indicator
        if (isDragging && currentLetter != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentLetter.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}