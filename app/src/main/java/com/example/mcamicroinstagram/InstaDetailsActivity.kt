package com.example.mcamicroinstagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mcamicroinstagram.ui.theme.MCAMicroInstagramTheme

class InstaDetailsActivity : ComponentActivity() {

    private val viewModel by viewModels<InstaDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("Title") ?: "No Title"
        val url = intent.getStringExtra("Url") ?: "No Image"
        val itemId = intent.getIntExtra("ItemId", 0)
        viewModel.titleUpdate(title)
        setContent {
            MCAMicroInstagramTheme {
                UserApplication(viewModel = viewModel, itemId = itemId, url = url)
            }
        }
    }

    override fun onBackPressed() {
        // do nothing
    }


    @Composable
    fun UserApplication(viewModel: InstaDetailsViewModel, itemId: Int, url: String){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "details") {
            composable("details") { DetailsScreen(viewModel,itemId,url,navController) }
            composable("image") { ImageScreen(navController, url) }
        }
    }


    @Composable
    fun DetailsScreen(viewModel: InstaDetailsViewModel, itemId: Int, url: String , navController: NavController) {
        val context = LocalContext.current
        val model = viewModel.instaViewModel.collectAsState()
        var editTextField by remember {
            mutableStateOf(false)
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    elevation = 4.dp,
                    title = {
                        Text("Details",color = MaterialTheme.colors.secondary)
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    navigationIcon = {
                        IconButton(onClick = {
                            finish()
                        }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    }, actions = {
                        Text(text = "Save", modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {
                                viewModel.onUpdateItem(context,itemId, model.value.title)
                                editTextField = false
                            },
                        color = MaterialTheme.colors.secondary)
                    })
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("image")
                            }
                            .size(400.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = model.value.title,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    IconButton(onClick = {
                        editTextField = !editTextField
                    }) {
                        Icon(Icons.Sharp.Edit, null)
                    }
                }
                if (editTextField) {
                    EditText(viewModel = viewModel)
                }
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Button(

                        onClick = {
                        viewModel.onDeleteItem(context,itemId)
                        finish()
                    },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text(text = "Delete", color = MaterialTheme.colors.surface,modifier = Modifier.padding(6.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun ImageScreen(navController: NavController, url: String) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    elevation = 4.dp,
                    title = {
                        Text("Image",color = MaterialTheme.colors.secondary)
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate("details")
                        }) {
                            Icon(Icons.Filled.ArrowBack, null)
                        }
                    })
                AsyncImage(model = url, contentDescription = null, contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
            }
        }
    }

    @Composable
    fun EditText(viewModel: InstaDetailsViewModel) {
        val model = viewModel.instaViewModel.collectAsState()
        TextField(
            colors = TextFieldDefaults.textFieldColors(MaterialTheme.colors.secondary),
            value = model.value.title, onValueChange = {
                viewModel.titleUpdate(it)
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(10.dp)
        )
    }
}