# MP3 Player App

A modern, offline MP3 player app for Android built with Kotlin and Jetpack Compose following clean architecture principles.

## 📸 Screens

| 🏠 List Music | 📄 Playlist | ▶️ Now Playing | ♥ Favorites |
|--------------|------------------|-------------|-------------|
| <img width="200" src="https://github.com/user-attachments/assets/b8b0da2f-a5b9-4916-bbfa-4c953782698f" /> | <img width="200" src="https://github.com/user-attachments/assets/6a3d74e0-939d-4ed7-ac6f-3f7184da16b7" /> | <img src="https://github.com/user-attachments/assets/5288d8aa-f2ad-4817-aa92-f6a5ddf1e992" width="200"/> | <img src="https://github.com/user-attachments/assets/12312a1d-936d-4ca4-beb2-282d1ed481a5" width="200"/> |


## 🎵 Features Overview

### Core Music Playback
- **Background Playback**: Music continues playing when app is in background
- **Media Controls**: Play, pause, skip, previous with large accessible buttons
- **Seek Control**: Scrub through tracks with progress slider
- **Shuffle Mode**: Toggle between normal (🔄) and shuffle (🔀) playback
- **Queue Management**: View and manage playback queue![Uploading 2.jpg…]()

- **Audio Focus**: Proper audio focus handling with ducking

### Music Library Management
- **Automatic Scanning**: Scans device storage for audio files using MediaStore
- **Fast Search**: Real-time search across song titles, artists, and albums
- **Sort & Filter**: Multiple sorting options for library organization
- **Refresh Library**: Manual refresh to detect new music files
- **Album Organization**: Browse music by albums with cover art support

### Playlist System
- **Create Playlists**: Create custom playlists with any name
- **Add Songs**: Long press any song to add to existing playlists
- **Manage Playlists**: Edit playlist names, delete playlists
- **Playlist Playback**: Play entire playlists or individual songs
- **Remove Songs**: Remove songs from playlists individually
- **Multi-Select Add**: Add multiple songs to playlists at once

### Favorites System
- **Mark Favorites**: Tap heart icon to add/remove songs from favorites
- **Favorites Tab**: Dedicated tab showing only favorited tracks
- **Persistent Storage**: Favorites saved locally using Room database

### User Interface
- **Material Design 3**: Modern UI with gradient themes
- **Bottom Navigation**: Easy access to All Music, Playlists, Albums, Favorites
- **Mini Player**: Persistent mini player on all screens (except Now Playing)
- **Now Playing Screen**: Full-screen player with large controls
- **Fast Scroll**: A-Z letter navigation for large music libraries
- **Multi-Select Mode**: Select multiple songs for batch operations

### Advanced Features
- **Media Notifications**: Rich media notifications with playback controls
- **Lock Screen Controls**: Control playback from lock screen
- **System Integration**: MediaSession for headset button support
- **Safe Area Support**: Respects device notches, status bars, and navigation bars
- **Foreground Service**: Ensures uninterrupted background playback

## 📱 App Structure

### Navigation Tabs
1. **All Music** - Complete library of songs with search and multi-select
2. **Playlists** - User-created playlists with management options
3. **Albums** - Music organized by album with cover art
4. **Favorites** - Quick access to favorited tracks

### Screen Flow
```
Main App
├── All Music Screen
│   ├── Search functionality
│   ├── Multi-select mode
│   ├── Long press → Add to Playlist
│   └── Fast scroll navigation
├── Playlists Screen
│   ├── Create new playlists
│   ├── Edit/delete playlists
│   └── Playlist Detail Screen
│       ├── View playlist songs
│       ├── Add songs dialog
│       ├── Play all/individual songs
│       └── Remove songs
├── Albums Screen
│   └── Album Detail Screen
├── Favorites Screen
└── Now Playing Screen
    ├── Large album art
    ├── Song info (title, artist)
    ├── Progress slider with time
    ├── Main controls (prev, play/pause, next)
    └── Shuffle toggle
```

## 🎮 User Interactions

### Song Actions
- **Single Tap**: Play song immediately
- **Long Press**: Show "Add to Playlist" dialog
- **Heart Icon**: Toggle favorite status
- **In Multi-Select**: Tap checkbox to select/deselect

### Playback Controls
- **Mini Player**: 
  - Tap to expand to Now Playing screen
  - Shows current song with basic controls
- **Now Playing Screen**:
  - Large play/pause button (80dp)
  - Previous/next buttons (72dp)
  - Shuffle toggle (🔄/🔀)
  - Seek bar with current/total time

### Multi-Select Mode
- **Enter**: Tap checkmark button (🔘) in top bar
- **Select**: Tap checkboxes or song rows
- **Actions**: Select All (✓), Delete (🗑️)
- **Exit**: Tap close button (✕) to deselect all

### Playlist Management
- **Create**: Tap + button in Playlists tab
- **Add Songs**: 
  - From playlist detail: + button → select multiple songs
  - From All Music: Long press song → select playlist
- **Play**: Tap "Play All" or individual songs
- **Remove**: Tap delete icon next to songs in playlist

## 🏗️ Technical Architecture

### Architecture Pattern
- **Clean Architecture** with MVVM pattern
- **Separation of Concerns**: Data → Domain → Presentation layers

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Navigation**: androidx.navigation.compose
- **Database**: Room for local storage (playlists, favorites)
- **Media Playback**: Android Media3 / ExoPlayer
- **Background Service**: MediaSessionService with foreground service
- **Image Loading**: Coil for album art
- **Dependency Injection**: Manual injection (no Hilt/Dagger)

### Key Components
```
├── Data Layer
│   ├── MediaStoreScanner - Scans device for audio files
│   ├── Room Database - Local storage for playlists/favorites
│   └── Repositories - Data access abstraction
├── Domain Layer
│   ├── Models - Song, Playlist, Album entities
│   └── Use Cases - Business logic operations
├── Presentation Layer
│   ├── ViewModels - State management
│   ├── Screens - Compose UI screens
│   └── Components - Reusable UI components
└── Playback Layer
    ├── PlayerManager - ExoPlayer wrapper
    ├── PlaybackService - Background playback service
    └── MediaSession - System integration
```

### Data Flow
1. **Media Scanning**: MediaStore → Songs Database
2. **User Interactions**: UI → ViewModel → Use Cases → Repository
3. **Playback**: PlayerManager → ExoPlayer → MediaSession → System
4. **State Updates**: Repository → Use Cases → ViewModel → UI (StateFlow)

## 🔧 Setup & Installation

### Requirements
- **Android Studio**: Latest stable version
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (latest)
- **Compile SDK**: 34

### Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Build the project: `./gradlew assembleDebug`
4. Install on device: `./gradlew installDebug`

### Permissions
The app requires the following permissions:
- **READ_MEDIA_AUDIO** (Android 13+) or **READ_EXTERNAL_STORAGE** (older versions)
- **FOREGROUND_SERVICE** - For background playback
- **WAKE_LOCK** - To maintain playback during sleep

## 🎨 Design Features

### Visual Design
- **Gradient Theme**: Deep indigo → purple → magenta gradients
- **Material 3**: Modern design system with dynamic theming
- **Accessibility**: Large touch targets and clear visual hierarchy
- **Typography**: Consistent text styles with proper contrast

### Responsive Layout
- **Safe Areas**: Respects device notches and system bars
- **Adaptive Sizing**: Buttons and text scale appropriately
- **Text Handling**: Ellipsis for long titles/artists
- **Loading States**: Circular progress indicators during operations

### User Experience
- **Smooth Animations**: Transitions between screens and states
- **Immediate Feedback**: Visual responses to user interactions
- **Error Handling**: Graceful handling of edge cases
- **Empty States**: Helpful messages when no content available

## 📊 Data Management

### Local Storage
- **Room Database**: SQLite database for structured data
  - Playlists and playlist-song relationships
  - Favorite songs
  - Cross-reference tables for many-to-many relationships
- **DataStore**: For simple preferences (theme settings)

### Media Integration
- **MediaStore Queries**: Efficient scanning of device audio
- **Content URIs**: Proper handling of scoped storage
- **Album Art**: Automatic loading from embedded metadata
- **Metadata Extraction**: Title, artist, album, duration parsing

## 🔄 State Management

### Reactive Architecture
- **StateFlow**: Reactive state management
- **Compose Integration**: `collectAsStateWithLifecycle()`
- **Unidirectional Data Flow**: Clear data flow patterns

### Key State Objects
- **Player State**: Currently playing song, position, play/pause status
- **UI State**: Loading states, dialog visibility, selection mode
- **Data State**: Songs, playlists, favorites lists
- **Navigation State**: Current screen, back stack management

## 🚀 Performance Optimizations

### Efficiency Features
- **Lazy Loading**: LazyColumn for large song lists
- **Fast Scroll**: A-Z navigation for quick access
- **Image Caching**: Coil handles album art caching
- **Background Processing**: Heavy operations on background threads

### Memory Management
- **Lifecycle Awareness**: Proper cleanup of resources
- **Player Release**: ExoPlayer released when app destroyed
- **Database Connections**: Efficient Room usage patterns

## 🔐 Privacy & Security

### Privacy First
- **No Network Access**: Completely offline application
- **No Analytics**: No data collection or tracking
- **Local Storage Only**: All data stays on device
- **No Ads**: Clean, ad-free experience

### Security Practices
- **Scoped Storage**: Proper Android storage access patterns
- **Permission Handling**: Runtime permission requests with rationale
- **Input Validation**: Safe handling of user inputs

---

## 📝 Development Notes

This project demonstrates modern Android development practices including:
- Clean Architecture implementation
- Jetpack Compose UI development
- Media3 integration for audio playback
- Room database with complex relationships
- Manual dependency injection
- StateFlow for reactive programming
- Material Design 3 theming

The codebase is well-structured, maintainable, and follows Android best practices for a production-ready music player application.
