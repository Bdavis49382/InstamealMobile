package com.instamealmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.ui.theme.InstamealMobileTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstamealMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.board)
                ) {
                    MealsArea()
                }
            }
        }
    }
}

@Composable
fun MealsArea(modifier: Modifier = Modifier) {
    Column {
        Text(
            text = stringResource(R.string.chalkboard_title),
            color = colorResource(R.color.chalk),
            modifier = Modifier.padding(15.dp),
            fontSize = 55.sp
        )
        MealList(mealList = makeLongList())
    }
    Box (
        modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            horizontalArrangement = Arrangement.End,
            ) {
            Button(onClick = { /*TODO*/ },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Icon(Icons.Default.Share, contentDescription = "Add Recipe")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { /*TODO*/ },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
               Icon(Icons.Default.List, contentDescription = "Add Recipe")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { /*TODO*/ },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
            }
        }

    }
}

fun makeLongList() : List<String> {
    val meals = mutableListOf("Spaghetti","Hamburgers")
    for (i in 1..20) {
        meals.add("bla")
    }
    return meals
}

@Composable
fun MealName(name: String, modifier: Modifier = Modifier) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "- $name",
            color = colorResource(R.color.chalk),
            fontSize = 30.sp,
            modifier = Modifier.padding(start = 10.dp)
        )
        Checkbox(checked = false, modifier = Modifier.size(30.dp), onCheckedChange = {

        })

    }
}

@Composable
fun MealList(mealList: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 120.dp)
        ,
    ) {
        items(mealList) {meal ->
            MealName(name = meal)
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    InstamealMobileTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(R.color.board)
        ) {
            MealsArea()
        }
    }
}