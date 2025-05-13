package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdPage(onDismiss : () -> Unit, join : () -> Unit, invite: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle()},
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Manage Household",
                style = TextStyle(color = Color.Black, fontSize = 30.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = stringResource(R.string.household_description),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 40.dp)
            )
            Text(text = stringResource(R.string.household_not_started_message),
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
            ) {
                Button(join, modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                ) {
                    Text("Join")
                }
                Button(invite, modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                ) {
                    Text("Invite")
                }
            }

        }
    }
}
