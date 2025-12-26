package com.campusdigitalfp.filmoteca

import android.content.Context
import com.campusdigitalfp.filmoteca.FilmDataSource
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.savedstate.serialization.saved


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "filmList") {
                composable("filmList") { FilmListScreen(navController) }
                composable("filmData/{filmTitle}") { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("filmTitle") ?: stringResource(id = R.string.no_title)
                    FilmDataScreen(navController, title)
                }
                composable("filmEdit") { FilmEditScreen(navController) }
                composable("about") { AboutScreen(navController) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(navController : NavController) {
        val films = FilmDataSource.films

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(films) { film ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("filmData/${film.title}") }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = film.imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = film.title ?: stringResource(id = R.string.no_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = film.director ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDataScreen(navController: NavController, filmTitle: String) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.popcorn),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = stringResource(id = R.string.harry_film),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4A6592)
                    )
                    Text(text = "Director:", fontWeight = FontWeight.Bold)
                    Text(text = "Chris Columbus")
                    Text(text = stringResource(id = R.string.year), fontWeight = FontWeight.Bold)
                    Text(text = "2001")
                    Text(text = "BluRay, Sci-Fi")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { uriHandler.openUri("https://www.imdb.com/title/tt0241527/") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = R.string.IMDB))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(id = R.string.ext_version))

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = stringResource(id = R.string.back))
                }
                Button(
                    onClick = { navController.navigate("filmEdit") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = stringResource(id = R.string.edit))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmEditScreen(navController: NavController) {

    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var anyo by remember { mutableStateOf("1999") }
    var url by remember { mutableStateOf("") }
    var comentarios by remember { mutableStateOf("") }

    var expandedGenero by remember { mutableStateOf(false) }
    var generoSeleccionado by remember { mutableStateOf("Drama")}
    val generos = listOf(stringResource(id = R.string.action), "Drama", stringResource(id = R.string.comedy), "Terror", "Sci-Fi")

    var expandedFormato by remember { mutableStateOf(false) }
    var formatoSeleccionado by remember { mutableStateOf("DVD") }
    val formatos = listOf("DVD", "Blu-ray", "Online")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.popcorn),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text(stringResource(id = R.string.capt_photo), textAlign = TextAlign.Center)
                }
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text(stringResource(id = R.string.choose_img), textAlign = TextAlign.Center)
                }
            }

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text(stringResource(id = R.string.title)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = director,
                onValueChange = { director = it },
                label = { Text("Director") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = anyo,
                onValueChange = { anyo = it },
                label = { Text(stringResource(id = R.string.year)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Box {
                OutlinedTextField(
                    value = generoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.gen)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expandedGenero = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(expanded = expandedGenero, onDismissRequest = { expandedGenero = false }) {
                    generos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                generoSeleccionado = item
                                expandedGenero = false
                            }
                        )
                    }
                }
            }

            Box {
                OutlinedTextField(
                    value = formatoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = R.string.form)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expandedFormato = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(expanded = expandedFormato, onDismissRequest = { expandedFormato = false }) {
                    formatos.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                formatoSeleccionado = item
                                expandedFormato = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("Enlace a IMDB") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = comentarios,
                onValueChange = { comentarios = it },
                label = { Text("Comentarios") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )

            // BOTONES FINALES
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("result", "RESULT_OK")
                    navController.popBackStack()
                }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(id = R.string.save))
                }
                Button(onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("result", "RESULT_CANCELED")
                    navController.popBackStack()
                }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun abrirPaginaWeb(uriHandler: UriHandler, url: String) {
    uriHandler.openUri(url)
}

fun mandarEmail(context: Context, email: String, asunto: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data =  Uri.parse("mailto:carlotalcd@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, asunto)
    }
    context.startActivity(intent)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val asuntoEmail = stringResource(id = R.string.support_header)

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.about)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.author_name),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = stringResource(id = R.string.image_description),
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp)
                    .clip(CircleShape)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {abrirPaginaWeb(uriHandler,"https://www.google.com")}) {
                    Text(text = stringResource(id = R.string.web_site))
                }
                Button(onClick = {mandarEmail(context, "carlotalcd@gmail.com", asuntoEmail)}) {
                    Text(text = stringResource(id = R.string.support))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = stringResource(id = R.string.back))
            }
        }
    }
}