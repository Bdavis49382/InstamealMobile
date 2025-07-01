

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Tag(text: String) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(text, color = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.padding(horizontal=8.dp, vertical=4.dp))
    }
}