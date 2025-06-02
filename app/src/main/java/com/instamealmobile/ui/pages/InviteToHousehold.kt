package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.HouseholdViewModel

@Composable
fun InviteToHousehold(onDismiss: () -> Unit) {
    val viewModel: HouseholdViewModel =  viewModel()
    val codeState by viewModel.code.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getCode()
    }

    Dialog(onDismissRequest = onDismiss) {
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
                        Text(code)
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
