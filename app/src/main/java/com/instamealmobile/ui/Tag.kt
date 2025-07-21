

import androidx.compose.foundation.clickable
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
fun Tag(text: String, modifier: Modifier = Modifier,onClick: () -> Unit = {}) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(20.dp))
        .clickable {
            onClick()
        }
    ) {
        Text(text, color = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.padding(horizontal=8.dp, vertical=4.dp))
    }
}