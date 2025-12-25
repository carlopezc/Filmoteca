package com.campusdigitalfp.filmoteca

import android.app.Activity
import android.content.Context
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import com.campusdigitalfp.filmoteca.ui.theme.FilmotecaTheme
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun FilmListScreen(navController : NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("filmData/Avatar") }) { Text(stringResource(id = R.string.buttonFilm1)) }
        Button(onClick = { navController.navigate("filmData/Titanic") }) { Text(stringResource(id = R.string.buttonFilm2)) }
        Button(onClick = { navController.navigate("about") }) { Text(stringResource(id = R.string.about)) }
    }
}

@Composable
fun FilmDataScreen(navController: NavController, filmTitle: String) {

    val result = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("result")
        ?.observeAsState()

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${stringResource(id = R.string.film_data)}: $filmTitle")

        if (result?.value == "RESULT_OK") {
            Text(text = stringResource(id = R.string.changes_ok))
        } else if (result?.value == "RESULT_CANCELED") {
            Text(text = stringResource(id = R.string.changes_cancel))
        }
        Button(onClick = { navController.navigate("filmData/Película relacionada")}) {Text(stringResource(id = R.string.watch_film))}
        Button(onClick = { navController.navigate("filmEdit")}) {Text(stringResource(id = R.string.edit_film))}
        Button(onClick = {
            navController.navigate("filmList") {
                popUpTo("filmList") { inclusive = true }
            }
        }) { Text(stringResource(id = R.string.back_principal))}
    }
}

@Composable
fun FilmEditScreen(navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.editing_text))
        Button(onClick = {
            navController.previousBackStackEntry?.savedStateHandle?.set("result", "RESULT_OK")
            navController.popBackStack()
        }) {
            Text(stringResource(id = R.string.save))
        }
        Button(onClick = {
            navController.previousBackStackEntry?.savedStateHandle?.set("result", "RESULT_CANCELED")
            navController.popBackStack()
        }) {
            Text(stringResource(id = R.string.cancel))}
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
@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val asuntoEmail = stringResource(id = R.string.support_header)

    Scaffold { paddingValues -> 
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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