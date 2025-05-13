package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckItem(name: String, color: Color, fontFamily: FontFamily, modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(false) }
    Row (
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .padding(start = 10.dp)
        ) {
            Text("B", textAlign = TextAlign.Center, modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                .size(40.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Column {
                Text(
                    text = name,
                    color = color,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 10.dp),
                    fontFamily = fontFamily,
                    textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    text= "Good Hamburger",
                    color = color,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 10.dp),
                    fontFamily = fontFamily
                )
            }

        }
        Checkbox(checked = checked, modifier = Modifier.size(30.dp).padding(end = 200.dp), onCheckedChange = {
            checked = it
        })
    }
}
