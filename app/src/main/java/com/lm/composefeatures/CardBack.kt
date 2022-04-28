package com.lm.composefeatures

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardBack() {
    Card(
        Modifier
        .size(100.dp, 600.dp).padding(start = 40.dp, top = 100.dp),
        elevation = 10.dp, shape = RoundedCornerShape(10.dp)
    ) {}
}
