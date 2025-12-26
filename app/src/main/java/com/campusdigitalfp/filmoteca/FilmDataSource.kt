package com.campusdigitalfp.filmoteca

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf()

    init {
        val f1 = Film()
        f1.id = films.size
        f1.title = "Harry Potter y la piedra filosofal"
        f1.director = "Chris Columbus"
        f1.imageResId = R.drawable.harry
        f1.comments = "Una aventura mágica en Hogwarts."
        f1.format = Film.FORMAT_DVD
        f1.genre = Film.GENRE_ACTION
        f1.imdbUrl = "http://www.imdb.com/title/tt0241527"
        f1.year = 2001
        films.add(f1)

        val f2 = Film()
        f2.id = films.size
        f2.title = "Regreso al futuro"
        f2.director = "Robert Zemeckis"
        f2.imageResId = R.drawable.regreso
        f2.comments = "Un clásico de los viajes en el tiempo."
        f2.format = Film.FORMAT_DIGITAL
        f2.genre = Film.GENRE_SCIFI
        f2.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f2.year = 1985
        films.add(f2)

        val f3 = Film()
        f3.id = films.size
        f3.title = "El rey león"
        f3.director = "Roger Allers, Rob Minkoff"
        f3.imageResId = R.drawable.leon
        f3.comments = "Una historia de crecimiento y responsabilidad."
        f3.format = Film.FORMAT_BLURAY
        f3.genre = Film.GENRE_DRAMA
        f3.imdbUrl = "http://www.imdb.com/title/tt0110357"
        f3.year = 1994
        films.add(f3)
    }
}