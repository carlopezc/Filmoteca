package com.campusdigitalfp.filmoteca

data class Film(
    var id: Int = 0,
    var imageResId: Int = 0,
    var title: String? = null,
    var director: String? = null,
    var year: Int = 0,
    var genre: Int = 0,
    var format: Int = 0,
    var imdbUrl: String? = null,
    var comments: String? = null
) {
    override fun toString(): String {
        return title ?: "<Sin título>"
    }

    companion object {
        const val FORMAT_DVD = 0
        const val FORMAT_BLURAY = 1
        const val FORMAT_DIGITAL = 2

        const val GENRE_ACTION = 0
        const val GENRE_DRAMA = 1
        const val GENRE_COMEDY = 2
        const val GENRE_TERROR = 3
        const val GENRE_SCIFI = 4
    }
}
