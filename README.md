# Earthquake Mobile
<p>
  Native Android application for monitoring and exploring earthquakes in Italy.
</p>

<p align="center">
  <img src="https://github.com/Mik1810/Earthquakes-Mobile/blob/main/app/src/main/res/mipmap-hdpi/icon_earthquake_foreground.webp" width="140" alt="Earthquake Mobile Logo" />
</p>

## Overview

Earthquake Mobile is a native Android application designed to display earthquake information from the last year in Italy.  
The app allows users to browse events, search by location, inspect earthquake details, and visualize positions on a map.

## Features

- Browse recent earthquake events
- Search earthquakes by location
- View detailed information for each event
- Display earthquake positions on a map
- Filter results based on occurrence area
- Store and manage earthquake data locally

## Screenshots

<p align="center">
  <img src="imgs/homepage.jpg" width="250" alt="Details screen" />
  <img src="imgs/search.jpg" width="250" alt="Search screen" />
  <img src="imgs/details.jpg" width="250" alt="Details screen" />
  <img src="imgs/map_detail.jpg" width="250" alt="Details screen" />
  <img src="imgs/map.jpg" width="250" alt="Details screen" />
</p>

<!-- Add the remaining screenshots from /imgs here once the exact file names are confirmed -->
<!-- Example:
<p align="center">
  <img src="imgs/<image-3>.jpg" width="250" alt="Screen 3" />
  <img src="imgs/<image-4>.jpg" width="250" alt="Screen 4" />
  <img src="imgs/<image-5>.jpg" width="250" alt="Screen 5" />
</p>
-->

## Tech Stack

- **Language:** Java
- **Platform:** Android
- **Architecture:** Android native components
- **Local database:** Room
- **Navigation:** Navigation Component
- **Networking:** Cronet
- **UI binding:** ViewBinding / DataBinding
- **Maps:** Google Maps SDK

## Main Functional Areas

### Earthquake List
Users can browse earthquake events collected by the application.

### Search
The app provides a search feature to quickly filter earthquakes by place or area.

### Details
Each earthquake can be opened in a dedicated details page, where the user can inspect its main information.

### Map
Earthquake positions can be visualized geographically to better understand where events occurred.

## Project Structure

```text
app/
└── src/main/
    ├── java/com/example/earthquakemobile/
    ├── res/
    └── AndroidManifest.xml
