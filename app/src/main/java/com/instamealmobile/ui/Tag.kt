

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Tag(text: String, onClick: () -> Unit = {}) {
    var disabled by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .shadow(4.dp, RoundedCornerShape(20.dp))
        .background(if (!disabled) MaterialTheme.colorScheme.secondary else Color.Gray)
        .border(1.dp, shape=RoundedCornerShape(20.dp), color=MaterialTheme.colorScheme.onSecondary)
        .clickable { onClick()
            disabled = !disabled
        }
    ) {
        Text(text, color = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.padding(horizontal=8.dp, vertical=4.dp))
    }
}