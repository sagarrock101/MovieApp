## MovieDB
It is a demo application made using [TheMovieDB](https://www.themoviedb.org/) with Kotlin and MVVM architecture

## Description
- Shows a list of Movies fetched from [TheMovieDB](https://www.themoviedb.org/) API 
- Movies list can be sorted based on Popular, Up Coming, Top Rated and Favorites
- Movie titles can be saved to favorites
- Movie titles can be shared
- Movie titles can be searched with suggestions 

 ![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/37874000/111022447-d72b4f80-83f8-11eb-9248-e46f64b2dbd9.gif)

## Needs API key to run the app
Get a API key from [TheMovieDB](https://www.themoviedb.org/) API key add in `local.properties` file as
`tmdb_api_key = "9a154e08fc629a156ad3f4dc651b706c"`

## Tech Stack and libraries used
- 100% kotlin
- Jetpack Components
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Room Persistence - construct database.
- Architecture
  - MVVM (Model View ViewModel) using Databinding
  - Repository Pattern
  - Dagger 2 for managing Dependencies
- Retrofit2 - constructing the REST API
- Moshi - is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects
- OkHttp3 - implementing interceptor, logging
- Glide - loading images from url 
- Paging 2 - The Paging Library helps to load and display small chunks of data at a time.
- Stetho - debugging persistence data & network packets
- Expandable Text View for expand/collapsing longer texts
- Circurlar Reveal Animation for search

