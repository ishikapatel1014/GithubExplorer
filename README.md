# GitHub Explorer

A modern Android application built with **Kotlin** and **Jetpack Compose** that allows users to search for any GitHub user and browse their public repositories using the GitHub REST API.

The project demonstrates modern Android development practices including **MVVM architecture**, **StateFlow**, **Coroutines**, **Repository Pattern**, **manual dependency injection**, **Material 3**, **clean state management**, and **unit testing**.

---

# Features

## Core Features

- Search GitHub users by username
- Display user avatar and profile information
- Display public repositories
- Repository detail screen
- Open repository directly on GitHub
- Loading state
- Empty state
- Error state
- Retry support

---

# Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM Architecture
- Repository Pattern
- Kotlin Coroutines
- StateFlow
- Retrofit
- OkHttp
- Gson
- Coil 3
- Navigation Compose
- DataStore Preferences
- JUnit

---

# Architecture

This application follows the **MVVM (Model–View–ViewModel)** architecture.

```
Presentation
│
├── UI
├── ViewModel
└── UI State
        │
        ▼
Domain
│
├── Models
├── Repository Interface
└── Business Logic
        │
        ▼
Data
│
├── Repository Implementation
├── Remote API
├── Local DataStore
└── DTO Mapping
```

## Why MVVM?

MVVM was selected because it:

- separates UI from business logic
- provides lifecycle-aware state management
- improves testability
- supports reactive UI using StateFlow
- scales well as features grow

---

# State Management

The application uses **StateFlow** to expose immutable UI state.

Each screen explicitly represents user-facing states including:

- Loading
- Success
- Empty
- Error
- Retry

The UI reacts automatically whenever the ViewModel updates the state.

---

# Project Structure

```
app
│
├── core
│   ├── network
│   ├── util
│   └── ui
│
├── data
│   ├── remote
│   ├── local
│   ├── repository
│   └── mapper
│
├── domain
│   ├── model
│   └── repository
│
├── presentation
│   ├── home
│   ├── detail
│   ├── components
│   ├── navigation
│   └── theme
│
└── di
```

---

# Dependency Injection

The project uses **manual dependency injection**.

Dependencies are created in a central DI container and injected into ViewModels. Manual DI was chosen because:

- no external DI framework required
- easy to understand
- lightweight
- appropriate for a project of this size

---

# Navigation

Navigation is implemented using **Navigation Compose**.

Screens include:

- Home Screen
- Repository Detail Screen

Navigation passes repository information from the home screen to the detail screen while maintaining a clean separation of concerns.

---

# Networking

The application consumes the public GitHub REST API.

Endpoints used:

```
GET /users/{username}
```

```
GET /users/{username}/repos
```

Retrofit and OkHttp are used for network communication.

---

# Image Loading

User avatars are loaded using **Coil 3**, providing efficient asynchronous image loading and caching.

---

# Error Handling

The application gracefully handles:

- No internet connection
- User not found (404)
- Rate limiting
- Server errors
- Timeout
- Unknown errors

User-friendly messages are displayed and retry actions are provided where appropriate.

---

# Testing

The project includes automated **unit tests**.

Current coverage includes:

- Input validation
- Successful search
- Empty repository state
- User not found
- Network error
- Rate limit error
- Refresh state
- Search history
- Retry logic
- Refresh error dismissal

A total of **12 ViewModel unit tests** are included.

### Run Tests

```
./gradlew testDebugUnitTest
```

or

```
gradlew testDebugUnitTest
```

---

# Build Instructions

## Prerequisites

- Android Studio Narwhal (or newer)
- Android SDK 36
- JDK 17
- Gradle (included via wrapper)

## Run

Clone the repository.

```
git clone <repository-url>
```

Open the project in Android Studio.

Allow Gradle Sync to complete.

Run the application on:

- Android Emulator
- Physical Android device

---

# Assumptions

- Only public GitHub repositories are displayed.
- GitHub API rate limits apply for unauthenticated requests.
- Internet connectivity is required to perform searches.
- Search history is stored locally using DataStore.

---

# Trade-offs

- Manual dependency injection was chosen over Hilt to keep the project lightweight and reduce complexity.
- Offline caching was not implemented because the assignment primarily evaluates architecture, networking, and state management.
- Repository pagination was not implemented since the GitHub API response size is sufficient for the assignment requirements.

---

# Future Improvements

Potential future enhancements include:

- Offline caching with Room
- Repository pagination
- Feature flag abstraction
- Analytics abstraction layer
- Multi-module architecture
- Bookmark/favorite repositories
- GitHub authentication
- Dark/Light theme customization
- UI screenshot testing
- Expanded unit and integration test coverage

---

# Design Decisions

The application emphasizes:

- Clean Architecture principles
- Readable Kotlin code
- Reactive state management
- Reusable Compose components
- Maintainable package structure
- Testability
- Scalability

---

# Author

**Ishika Patel**

Android Developer

---

# License

This project was developed as part of an Android take-home technical assignment.