package com.instamealmobile.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.User
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import com.instamealmobile.viewModels.HouseholdViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseholdPage(onDismiss : () -> Unit, join : () -> Unit, invite: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val viewModel: HouseholdViewModel =  viewModel()
    val householdIdState by viewModel.householdId.observeAsState()
    val householdState by viewModel.users.observeAsState()

    LaunchedEffect(Unit) {
        if (householdIdState is ApiState.Success) {
            val householdId = (householdIdState as ApiState.Success<String>).data
            Log.i("Household ID:",householdId)
            viewModel.getUsers("3hPKx3PwkPkPPlCVs53q")
        } else if (householdIdState is ApiState.Error) {
            val error = (householdIdState as ApiState.Error).message
            Log.i("Household ID","didn't work: $error")
        }
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle()},
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Text(
                text = "Manage Household",
                style = TextStyle(color = Color.Black, fontSize = 30.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = stringResource(R.string.household_description),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 10.dp).padding(bottom = 40.dp)
            )
            when (householdState) {
                is ApiState.Success -> {
                    val household = (householdState as ApiState.Success<List<User>>).data
                    if (household.size < 2) {
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
                    } else {
                        Text(text="Admin", fontSize = 25.sp)
                        Text(text=household[0].full_name, fontSize = 20.sp)
                        Text(text="Other Users", fontSize = 25.sp, modifier = Modifier.padding(top=10.dp))
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                            items(household.subList(1,household.size)) { item ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.full_name, fontSize = 20.sp, modifier = Modifier.padding(end=20.dp))
                                    Button({viewModel.kickUser(item.id)}) {
                                        Text("Kick")
                                    }
                                }
                            }
                            item {
                                Button(invite, modifier = Modifier
                                    .width(100.dp)
                                    .height(40.dp)
                                ) {
                                    Text("Invite")
                                }

                            }
                        }
                    }
                }
                is ApiState.Error -> {
                    val error = (householdState as ApiState.Error).message
                    Text(error)
                }
                is ApiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
                        CircularProgressIndicator()

                    }
                }
                null -> {}
            }

        }
    }
}
