# 🎬 Filmoteca (Movie Library App)

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)

> A modern Android application to manage a personal movie collection, built entirely with Jetpack Compose.

---

## 🎯 Project Overview

**Filmoteca** is a comprehensive movie management application designed to showcase modern Android development practices. It provides a clean, responsive user interface to browse, view details, edit, and manage a curated list of films. The project serves as a practical implementation of declarative UI design using Jetpack Compose, robust in-app navigation, and state management.

## 🛠️ Technical Details & Features

* **100% Jetpack Compose UI:** The entire user interface is built using Kotlin and Compose, completely moving away from legacy XML layouts. It utilizes `Scaffold`, `TopAppBar`, and complex custom layouts.
* **Dynamic Lists & Multi-Selection:** Uses `LazyColumn` for highly efficient list rendering. It features a custom multi-selection mode (triggered by a long press) allowing users to perform bulk deletions of movies.
* **Modern Navigation (`NavHost`):** Implements smooth transitions between screens (`FilmListScreen`, `FilmDataScreen`, `FilmEditScreen`, `AboutScreen`) passing dynamic arguments through routes and handling back-stack states gracefully.
* **State Management:** Uses Compose state handling (`remember`, `mutableStateOf`) to manage form inputs, dropdown menus (for genres and formats), and UI updates in real-time.
* **Implicit Intents:** Integrates with the Android OS to open external resources:
  * Opens the native browser to view a movie's IMDB page (`ACTION_VIEW`).
  * Launches the user's email client for support requests (`ACTION_SENDTO`).
* **Internationalization (i18n):** The application is fully localized in English and Spanish, adapting automatically to the device's system language settings via `strings.xml`.
* **Logging & Stability:** Implements strategic `Logcat` events for debugging data mutations and has been stress-tested using the Android UI Exerciser Monkey to ensure crash-free navigation.

## 🏗️ Architecture

The app currently uses a centralized, in-memory singleton (`FilmDataSource`) as the single source of truth for the movie collection. This mimics the behavior of a repository pattern, making it highly scalable and ready for future integration with local databases (like Room/SQLite) or remote REST APIs.

## ⚙️ How to Compile & Run

1. Clone this repository to your local machine:
   ```bash
   git clone [https://github.com/tu-usuario/Filmoteca.git](https://github.com/tu-usuario/Filmoteca.git)
