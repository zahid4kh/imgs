# IMGS

A modern, cross-platform desktop image gallery application built with Kotlin and Jetbrains Compose for Desktop. Browse, view, and manage your images with a beautiful Material Design 3 interface.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-blue.svg?logo=kotlin)](https://kotlinlang.org) [![Compose](https://img.shields.io/badge/Compose-1.9.0--beta03-blue.svg?logo=jetpack-compose)](https://www.jetbrains.com/lp/compose-multiplatform/)

## Features

### Image Gallery & Management

- **Adaptive Grid Layout**: Staggered grid view with lazy loading for smooth performance
- **Image Viewing**: Click any image to open a full-screen viewer in a separate window
- **File System Navigation**: Browse directories with breadcrumb navigation starting from `~/Pictures`
- **Image Deletion**: Right-click context menu for quick image deletion
- **Folder Navigation**: Click folders to navigate through your directory structure

### UI/UX

- **Material Design 3**: Modern, clean interface with Material 3 components
- **Dark Mode**: Toggle between light and dark themes with persistent preferences
- **Custom Window Controls**: Undecorated window with custom draggable title bar
- **Smooth Animations**: Spring-based transitions for a polished experience
- **Platform-Aware Fonts**: Ubuntu on Linux, Roboto on Windows/macOS

### Technical

- **Asynchronous Image Loading**: Powered by Coil 3 for efficient image loading
- **MVVM Architecture**: Clean separation with ViewModel and dependency injection (Koin)
- **Persistent Settings**: User preferences saved to `~/.imgs/settings.json`
- **Cross-platform**: Native packages for Windows (MSI/EXE), macOS (DMG), and Linux (DEB)
- **Hot Reload Support**: Fast development iteration

## Development Setup

### Prerequisites

- JDK 17 or later
- Kotlin 2.1.20 or later
- IntelliJ IDEA (recommended) or Android Studio

### Make Gradle Wrapper Executable (Linux/macOS only)

After cloning the repository, you need to make the Gradle wrapper executable:

```bash
chmod +x gradlew
```

**Note:** This step is not required on Windows as it uses `gradlew.bat`.

### Running the Application

#### Standard Run

```bash
./gradlew run
```

#### Hot Reload (Recommended for Development)

```bash
./gradlew :hotRun --mainClass IMGS --auto
```

This enables automatic recompilation and hot swapping when you modify your code, making development much faster.

### Building a Native Distribution

To build a native distribution for your platform:

```bash
./gradlew packageDistributionForCurrentOS
```

This will create a platform-specific installer in the `build/compose/binaries/main-release/{extension}/` directory.

### Available Gradle Tasks

- `./gradlew run` - Run the application
- `./gradlew :hotRun --mainClass IMGS --auto` - Run with hot reload
- `./gradlew packageDistributionForCurrentOS` - Build native distribution for current OS
- `./gradlew packageDmg` - Build macOS DMG (macOS only)
- `./gradlew packageMsi` - Build Windows MSI (Windows only)
- `./gradlew packageExe` - Build Windows EXE (Windows only)
- `./gradlew packageDeb` - Build Linux DEB (Linux only)

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Kotlin | 2.1.20 | Primary programming language |
| Compose for Desktop | 1.9.0-beta03 | Declarative UI framework |
| Material 3 | Latest | Design system |
| Koin | 4.0.3 | Dependency injection |
| Coil 3 | 3.3.0 | Async image loading |
| Kotlinx Coroutines | 1.10.2 | Asynchronous programming |
| Kotlinx Serialization | 1.8.1 | JSON persistence |
| Lifecycle ViewModel | 2.9.5 | State management |

## Architecture

This application follows the **MVVM (Model-View-ViewModel)** pattern:

```text
Main.kt (Entry Point)
    ↓
AppModule.kt (Dependency Injection - Koin)
    ↓
MainViewModel.kt (State & Business Logic)
    ↓
Database.kt (Persistence Layer)
    ↓
App.kt & TopBar.kt (UI Composables)
```

**State Management**: Reactive `StateFlow<UiState>` propagates changes from ViewModel to UI components, triggering automatic recomposition.

## User Guide

1. **Launch the Application**: The app opens in your `~/Pictures` directory by default
2. **Browse Images**: Scroll through the adaptive grid layout to view your images
3. **Navigate Folders**: Click any folder to navigate into it, or use breadcrumb navigation in the top bar
4. **View Full-Size**: Click an image to open it in a full-screen viewer window
5. **Delete Images**: Right-click any image and select "Delete" from the context menu
6. **Toggle Theme**: Click the settings icon in the top-right, then toggle dark mode
7. **Window Controls**: Use the custom minimize, maximize, and close buttons in the top-right

## Configuration

User settings are stored in `~/.imgs/settings.json` and include:

- Dark mode preference
- Additional settings (expandable)

## Generated with Compose for Desktop Wizard

This project was generated using the [Desktop Client of Compose for Desktop Wizard](https://github.com/zahid4kh/compose-for-desktop/tree/desktop).
