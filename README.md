## MovieDB
It is a demo application made using [TheMovieDB](https://www.themoviedb.org/) with Kotlin and MVVM architecture

## Description
- Shows a list of Movies fetched from [TheMovieDB](https://www.themoviedb.org/) API 
- Movies list can be sorted based on Popular, Up Coming, Top Rated and Favorites
- Movie titles can be saved to favorites
- Movie titles can be shared
- Movie titles can be searched with suggestions 

## ScreenShots
### Light Theme
<img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/movie_list.jpeg" width = 300 height = 500 /> <img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/movie_detail_light.jpeg" width = 300 height = 500 /> 

### Dark Theme
<img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/movie_list_dark.jpeg" width = 300 height = 500 /> <img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/movie_detail_dark.jpeg" width = 300 height = 500 />

### Search

<img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/search_start.jpeg" width = 300 height = 500 /> <img src = "https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/search_end.jpeg" width = 300 height = 500 />  

## API key to run the app
Get a API key from [TheMovieDB](https://www.themoviedb.org/) and add the key in `local.properties` file as
`tmdb_api_key = ""`

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
- Room Database - for storing favorite movie titles 
- Moshi - is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects
- OkHttp3 - implementing interceptor, logging
- Glide - loading images from url 
- Paging 2 - The Paging Library helps to load and display small chunks of data at a time.
- Stetho - debugging persistence data & network packets
- Expandable Text View for expand/collapsing longer texts
- Circurlar Reveal Animation for search

 <a href="https://play.google.com/store/apps/details?id=com.sagaRock101.tmdb" ><img src="https://github.com/sagarrock101/MovieApp/blob/master/app/src/main/java/com/sagaRock101/images/google-play-badge-small.png" width = 300 height = 100 /></a>
