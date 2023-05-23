package com.experion.jettipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier=Modifier.size(40.dp)
@Composable
fun RoundIconButton(
    imageVector: ImageVector,
    onClick:()->Unit,
    modifier: Modifier= Modifier,
    tint : Color = Color.Black.copy(alpha=0.8f),
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    elevation : Dp =4.dp
){
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .size(50.dp)
            .clip(CircleShape)
            .clickable { onClick.invoke() }
            .then(IconButtonSizeModifier),


        shape = CircleShape,

        ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector, contentDescription = "Plus or minus icon",
                tint = tint,


                )
        }
    }
}