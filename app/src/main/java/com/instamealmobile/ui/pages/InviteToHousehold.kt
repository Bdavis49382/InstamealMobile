package com.instamealmobile.ui.pages

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.HouseholdViewModel
import com.instamealmobile.viewModels.NavViewModel
import kotlinx.coroutines.launch

@Composable
fun InviteToHousehold() {
    val viewModel: HouseholdViewModel =  viewModel()
    val nav: NavViewModel = viewModel()
    val codeState by viewModel.code.collectAsState()
    val clipboardManager = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getCode()
    }

    Dialog(onDismissRequest = nav::closeAlert) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.padding(30.dp)) {
                Text(stringResource(R.string.household_code_message), textAlign = TextAlign.Center)
                when (codeState) {
                    is ApiState.Loading -> {
                        CircularProgressIndicator()

                    }
                    is ApiState.Success -> {
                        val code = (codeState as ApiState.Success<String>).data
                        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(top = 20.dp)) {
                            OutlinedButton({
                                val clipData = ClipData.newPlainText("plain text",code)
                                coroutineScope.launch {
                                    clipboardManager.setClipEntry(ClipEntry(clipData))
                                    Toast.makeText(context, "Join Code Added to Clipboard", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(painter = painterResource(R.drawable.baseline_content_copy_24),"Copy Code")
                            }
                            Text(code, fontSize = 25.sp, modifier = Modifier.padding(start = 5.dp))
                        }
                    }
                    is ApiState.Error -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }
}
