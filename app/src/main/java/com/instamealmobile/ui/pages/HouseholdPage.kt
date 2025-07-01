package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.User
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.instamealmobile.OpenAlert
import com.instamealmobile.viewModels.HouseholdViewModel
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun HouseholdPage(reload: () -> Unit) {
    val nav: NavViewModel = viewModel()
    val viewModel: HouseholdViewModel =  viewModel()
    val householdState by viewModel.users.collectAsState()
    val user = Firebase.auth.currentUser

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

    Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
        Text(
            text = "Manage Household",
            fontSize = 30.sp,
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
                        Button(onClick = {nav.navigateTo(OpenAlert.Join)}, modifier = Modifier
                            .width(150.dp)
                            .height(50.dp)
                        ) {
                            Text("Join")
                        }
                        Button(onClick = {nav.navigateTo(OpenAlert.Invite)}, modifier = Modifier
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
                                // Users should only see an option to kick if they are the admin or it is themselves.
                                if (user?.uid == household[0].id || user?.uid == item.id) {
                                    Button({viewModel.kickUser(item.id, reload)}) {
                                        if (user.uid == item.id) {
                                            Text("Leave")
                                        } else {
                                            Text("Kick")
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Button(onClick = {nav.navigateTo(OpenAlert.Invite)}, modifier = Modifier
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
            else -> {}
        }

    }
}
