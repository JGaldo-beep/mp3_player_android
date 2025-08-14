Build an Android MP3 Player App — Kotlin + Jetpack Compose (MVVM)

Goal: Create a modern, offline MP3 player app for Android that reads and plays audio files from both internal and external storage. No login, no analytics, no ads — just a clean, well-structured music player with playlists, albums, favorites, and multi-select delete.

Tech & Standards
Language: Kotlin

UI: Jetpack Compose + Material 3

Architecture: Clean Architecture with MVVM (data → domain → presentation). Do not put everything in MainActivity.

Navigation: androidx.navigation.compose

Playback: Android Media3 / ExoPlayer, MediaSession, audio focus handling, foreground service with media notification.

Storage & Media: Query device audio via MediaStore, support scoped storage. For Android 13+ use READ_MEDIA_AUDIO; for older versions gate with READ_EXTERNAL_STORAGE as needed. Support external (SD) storage via MediaStore / SAF where applicable.

Persistence: Room (for playlists, favorites, play history), DataStore (for simple preferences like theme).

Images: Coil for album art (load from MediaStore embedded art or file).

Min/Target SDK: Min 26+; compile/target latest stable (e.g., 34). Use Kotlin Coroutines + Flow.

App Requirements (must-have)
Library tabs & structure

Bottom navigation with 4 main destinations:

All Music (all songs indexed from storage)

Playlists

Albums

Favorites

Each destination is its own composable screen and ViewModel.

Play audio

Play/pause/seek/next/previous, shuffle and repeat (one/all/off).

A Now Playing mini-player (sticky at bottom) visible across tabs; expands to a full Now Playing screen with queue and scrubber.

Keep playback working in background with media notification controls and lock screen controls via MediaSession.

Scan & display local music

On first launch (and via “Refresh Library” action), scan MediaStore for audio files (audio/\*), reading: title, artist, album, duration, file path/uri, track number, albumId, date added.

Show Albums grouped by albumId/name with cover art.

Favorites

Toggle favorite on any song; persist favorites in Room.

Favorites tab lists only favorited tracks.

Playlists

Create, rename, and delete playlists (persist in Room).

Add/remove tracks to playlists; reorder tracks via drag-and-drop inside a playlist.

Playlist detail screen shows its tracks with play-all and shuffle.

Delete songs (multi-select)

In All Music (and optionally Albums/Favorites), enable multi-selection mode (long press to enter selection; checkboxes for each row).

Provide an action bar with Delete to remove selected files. Use MediaStore/Safely request user consent if required by scoped storage. Show confirmation dialog (“Delete X songs? This will remove files from device.”).

UI/UX

Modern Material 3 + gradient color theme (e.g., deep indigo → purple → magenta). Use gradient backgrounds for app bars and large headers (Brush.linearGradient) and keep content surfaces readable.

Provide light/dark theme variants while preserving gradient look (slightly darker tints for dark mode).

Smooth transitions between mini-player and full player (e.g., AnimatedContent).

Large lists should be performant with LazyColumn, fast scroll, and optional alphabetical jump.

Empty states and permission rationale screens are required.

No login

The app is fully offline, no auth screens of any kind.

Nice functional details (include if straightforward)
Sort & filter on All Music: sort by Title/Artist/Album/Date Added; simple search field (client-side).

Optional settings: enable/disable gradient theme, toggle “Keep screen on” in Now Playing.

Save and restore last queue and playback position.

Permissions & Storage Behavior
Request runtime permissions politely with rationale. For Android 13+: READ_MEDIA_AUDIO. For older: READ_EXTERNAL_STORAGE.

Use MediaStore URIs to play. For deletion, handle via ContentResolver delete (MediaStore) and SAF where needed; handle failures gracefully (e.g., item already missing).

External/SD storage supported through the same MediaStore queries; do not rely on absolute file paths.

Project Structure (example)
pgsql
Copy
Edit
com.example.mp3player/
data/
local/
db/ AppDatabase.kt
dao/ PlaylistDao.kt, FavoriteDao.kt
entities/ SongEntity.kt, PlaylistEntity.kt, PlaylistSongCrossRef.kt, FavoriteEntity.kt
media/
MediaStoreScanner.kt
AlbumArtResolver.kt
repo/
MusicRepository.kt (impl)
PlaylistRepository.kt (impl)
FavoritesRepository.kt (impl)
domain/
models/ Song.kt, Album.kt, Playlist.kt
usecase/
GetAllSongsUseCase.kt
GetAlbumsUseCase.kt
GetFavoritesUseCase.kt
ToggleFavoriteUseCase.kt
CreatePlaylistUseCase.kt
UpdatePlaylistUseCase.kt
DeletePlaylistUseCase.kt
AddToPlaylistUseCase.kt
RemoveFromPlaylistUseCase.kt
ReorderPlaylistUseCase.kt
DeleteTracksUseCase.kt
RefreshLibraryUseCase.kt
playback/
PlayerManager.kt (wraps Media3 ExoPlayer)
PlaybackService.kt (foreground service with MediaSession, notification)
MediaSessionConnector.kt
ui/
navigation/ AppNavHost.kt, Destinations.kt
components/ SongRow.kt, AlbumGridCard.kt, MiniPlayerBar.kt, GradientTopBar.kt, MultiSelectTopBar.kt
screens/
library/ AllMusicScreen.kt (with multi-select)
albums/ AlbumsScreen.kt, AlbumDetailScreen.kt
playlists/ PlaylistsScreen.kt, PlaylistDetailScreen.kt, PlaylistEditorDialog.kt
favorites/ FavoritesScreen.kt
player/ NowPlayingScreen.kt
permissions/ PermissionsScreen.kt
settings/ SettingsScreen.kt (optional)
theme/ Theme.kt, Color.kt, Typography.kt (gradient helpers)
di/ AppModule.kt (Hilt or manual)
MainActivity.kt (hosts NavHost + mini-player; minimal logic)
Data Model Notes
Song: id (MediaStore \_ID), title, artist, album, albumId, duration, contentUri, dateAdded.

Album: albumId, name, artist, songCount, albumArt (uri if available).

Playlist: id (Room), name, createdAt; many-to-many with songs using a cross-ref table (with position for ordering).

Favorite: songId primary key.

Key Behaviors
Multi-select UI: long-press on a SongRow enters selection mode; checkboxes appear; top bar switches to MultiSelectTopBar with count + Delete action.

Deletion: For each selected song, attempt delete via ContentResolver.delete(song.contentUri). If permission denial occurs, prompt with proper intent/Ui to grant deletion access. After deletion, refresh lists.

Albums: Query by albumId and render a grid; AlbumDetailScreen shows only that album’s tracks.

Playlists: PlaylistsScreen lists all playlists with overflow menu (rename/delete). PlaylistDetailScreen supports drag-to-reorder (update position), remove items inline, and “Add songs” picker (from All Music with multi-select).

Favorites: star icon in every SongRow; tapping toggles state; update favorites tab instantly.

Playback: PlayerManager exposes a StateFlow for isPlaying, currentSong, progress, queue. ViewModels observe flows and update UI. Ensure audio focus + ducking. Keep notification in sync.

UI & Theming (Gradients)
Provide a gradient palette and helpers:

Light: #4F46E5 (indigo) → #7C3AED (violet) → #DB2777 (magenta)

Dark: slightly deepened versions of the same.

Utilities:

fun gradientBrush(): Brush = Brush.linearGradient(listOf(Indigo, Violet, Magenta))

Use gradient for large headers/app bars and the Now Playing background; keep list rows on surface for readability.

Dependencies (add latest stable)
Material 3, Navigation Compose, Lifecycle ViewModel, Room (runtime/kapt), DataStore, Coil, Media3 (ExoPlayer, session, ui), Hilt (optional but preferred).

Kotlin Coroutines + Flow.

Deliverables
Full Android Studio project compiling and running.

All screens wired via Navigation Compose; ViewModels per screen; repositories and use cases implemented.

Foreground playback service with MediaSession and media notification.

Room schema + migrations (if needed).

Runtime permission flow and empty-state UIs.

Unit tests for repositories/use cases (basic), and at least one UI test for multi-select delete.

README with build/run instructions and a short explanation of architecture.

Acceptance Criteria (must pass)
I can open the app, see All Music, Playlists, Albums, Favorites tabs.

I can long-press multiple songs in All Music and delete them; files are removed from device and lists refresh.

I can create, rename, and delete playlists; add/remove songs; reorder tracks.

I can mark/unmark favorites and see them in Favorites.

I can tap any song to start playback; mini-player appears and works; full player shows scrubber, queue, shuffle/repeat.

Playback continues when app is backgrounded; notification controls work; lock screen controls work.

The UI uses a gradient theme and the project follows MVVM with clean package structure (not everything in MainActivity).

Do not include any login or network features.
