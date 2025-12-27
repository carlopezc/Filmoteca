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
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.navArgument
import androidx.compose.runtime.mutableStateListOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "filmList") {
                composable("filmList") { FilmListScreen(navController) }

                composable(
                    route = "filmData/{filmId}",
                    arguments = listOf(navArgument("filmId") { type = androidx.navigation.NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("filmId") ?: 0
                    FilmDataScreen(navController, id)
                }
                composable(
                    route = "filmEdit/{filmId}",
                    arguments = listOf(navArgument("filmId") { type = androidx.navigation.NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("filmId") ?: 0
                    FilmEditScreen(navController, id)
                }
                composable("about") { AboutScreen(navController) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FilmListScreen(navController: NavController) {
    val films = FilmDataSource.films
    val selectedIndices = remember { mutableStateListOf<Int>() }
    val isSelectionMode = selectedIndices.isNotEmpty()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text("${selectedIndices.size} seleccionadas")
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* Scroll to top */ }) {
                            Image(painter = painterResource(id = R.drawable.popcorn),
                                contentDescription = null, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.app_name))
                        }
                    }
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = { selectedIndices.clear() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar")
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(onClick = {
                            selectedIndices.sortedDescending().forEach { index ->
                                films.removeAt(index)
                            }
                            selectedIndices.clear()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar")
                        }
                    } else {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("Añadir película") },
                                onClick = {
                                    showMenu = false
                                    films.add(Film(id = films.size, title = "Nueva Película", imageResId = R.drawable.popcorn))
                                },
                                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.about)) },
                                onClick = { showMenu = false; navController.navigate("about") },
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSelectionMode) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            itemsIndexed(films) { index, film ->
                val isSelected = selectedIndices.contains(index)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                if (isSelectionMode) {
                                    if (isSelected) selectedIndices.remove(index) else selectedIndices.add(index)
                                } else {
                                    navController.navigate("filmData/$index")
                                }
                            },
                            onLongClick = {
                                if (!isSelectionMode) selectedIndices.add(index)
                            }
                        )
                        .background(if (isSelected) Color.LightGray.copy(alpha = 0.4f) else Color.Transparent)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = film.imageResId),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = film.title ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = film.director ?: "", style = MaterialTheme.typography.bodySmall)
                    }
                    if (isSelectionMode) {
                        Checkbox(checked = isSelected, onCheckedChange = null)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDataScreen(navController: NavController, filmId: Int) {
    val film = FilmDataSource.films[filmId]

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val generoList = context.resources.getStringArray(R.array.genero_list)
    val formatoList = context.resources.getStringArray(R.array.formato_list)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.clickable {
                            navController.navigate("filmList") {
                                popUpTo("filmList") { inclusive = true }
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.popcorn),
                                contentDescription = "Ir al inicio",
                                modifier = Modifier
                                    .size(35.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(text = stringResource(R.string.app_name))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = film.imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = film.title ?: stringResource(id = R.string.no_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF4A6592)
                    )
                    Text(text = "Director:", fontWeight = FontWeight.Bold)
                    Text(text = film.director ?: "Desconocido")
                    Text(text = stringResource(id = R.string.year), fontWeight = FontWeight.Bold)
                    Text(text = film.year.toString())

                    Text(text = "${formatoList[film.format]}, ${generoList[film.genre]}")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { uriHandler.openUri(film.imdbUrl ?: "https://www.imdb.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(stringResource(id = R.string.IMDB))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = film.comments ?: "")

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
                    onClick = { navController.navigate("filmEdit/$filmId") },
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
fun FilmEditScreen(navController: NavController, filmId: Int) {
    val film = FilmDataSource.films[filmId]
    val TAG = "FilmEditScreen"

    var titulo by remember { mutableStateOf(film.title ?: "") }
    var director by remember { mutableStateOf(film.director ?: "") }
    var anyo by remember { mutableStateOf(film.year.toString()) }
    var url by remember { mutableStateOf(film.imdbUrl ?: "") }
    var comentarios by remember { mutableStateOf(film.comments ?: "") }

    var genero by remember { mutableIntStateOf(film.genre) }
    var formato by remember { mutableIntStateOf(film.format) }

    var expandedGenero by remember { mutableStateOf(false) }
    var expandedFormato by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val generoList = context.resources.getStringArray(R.array.genero_list).toList()
    val formatoList = context.resources.getStringArray(R.array.formato_list).toList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.clickable {
                            navController.navigate("filmList") {
                                popUpTo("filmList") { inclusive = true }
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.popcorn),
                                contentDescription = "Ir al inicio",
                                modifier = Modifier
                                    .size(35.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(text = stringResource(R.string.app_name))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text(stringResource(id = R.string.title)) },
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                OutlinedTextField(
                    value = generoList[genero],
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
                    generoList.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                genero = index
                                expandedGenero = false
                            }
                        )
                    }
                }
            }

            Box {
                OutlinedTextField(
                    value = formatoList[formato],
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
                    formatoList.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                formato = index
                                expandedFormato = false
                            }
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        Log.i(TAG, "Guardando cambios para la película: $titulo (ID: $filmId)")

                        film.title = titulo
                        film.director = director
                        film.year = anyo.toIntOrNull() ?: 0
                        film.imdbUrl = url
                        film.comments = comentarios
                        film.genre = genero
                        film.format = formato

                        navController.previousBackStackEntry?.savedStateHandle?.set("result", "RESULT_OK")
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(id = R.string.save))
                }

                Button(onClick = {
                    Log.i(TAG, "Edición cancelada por el usuario.")

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
                title = {
                    Box(
                        modifier = Modifier.clickable {
                            navController.navigate("filmList") {
                                popUpTo("filmList") { inclusive = true }
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.popcorn),
                                contentDescription = "Ir al inicio",
                                modifier = Modifier
                                    .size(35.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(text = stringResource(R.string.app_name))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
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