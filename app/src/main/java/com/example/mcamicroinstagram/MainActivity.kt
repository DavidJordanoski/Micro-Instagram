package com.example.mcamicroinstagram

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mcamicroinstagram.model.Photos
import com.example.mcamicroinstagram.ui.theme.MCAMicroInstagramTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MCAMicroInstagramTheme {
                viewModel.fetchData()
                val model by viewModel.mainViewModel.collectAsState()
                HomeScreen(model.list)
            }
        }
    }
}

@Composable
fun HomeScreen(list: List<Photos>) {
    Surface(
        modifier = Modifier.fillMaxSize(),

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeAppBar(textBar = "Home")
            LazyColumn {
                items(items = list) {
                    Items(
                        it.id,
                        it.title,
                        it.url
                    )
                }
            }
        }
    }
}

@Composable
fun Items(itemId: Int, title: String, url: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .shadow(2.dp),
        shape = RectangleShape
    ) {
        Column {
            Row {
                Icon(Icons.Filled.Person,null, modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically))
                Text(text = title[1].uppercase()+title.drop(0), fontWeight = FontWeight.Medium, fontSize = 16.sp, maxLines = 1 , modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(15.dp))
            }
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(context, InstaDetailsActivity::class.java)
                        intent.putExtra("Url", url)
                        intent.putExtra("Title", title)
                        intent.putExtra("ItemId", itemId)
                        context.startActivity(intent)
                    },
            )
            Icon(Icons.Filled.ThumbUp,null, modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun HomeAppBar(textBar: String){
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.surface,
    ){
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = textBar,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colors.secondary
            )
        }
    }
    Divider(color = Color.LightGray, thickness = 0.dp, modifier = Modifier.shadow(5.dp))
}