# Strong Clone (Progressio)

A free, offline workout tracker inspired by Strong. Built with Kotlin, Jetpack Compose, and Room.

## Features

- **Exercise library** – 38 pre-loaded exercises (Chest, Back, Legs, etc.)
- **Workout templates** – Create and save routines (create from Home → add templates in a future update)
- **Active workout** – Log sets, reps, and weight; see previous workout data
- **Rest timer** – 90s countdown between sets
- **History** – View past workouts and details
- **Progress** – Total workouts and volume
- **Dark theme** – Material 3 dark UI

## Download APK (from GitHub Releases)

1. Open your repo **Releases** (e.g. `https://github.com/YOUR_USERNAME/Progressio/releases`).
2. Download the latest **app-release-unsigned.apk**.
3. On your Android device: **Settings → Security** → enable **Install from unknown sources** (or allow for your browser/files app).
4. Open the APK file and tap **Install**.

## Build APK locally

### Option A: Android Studio (recommended)

1. **Clone** the repo and open the folder in **Android Studio**.
2. Let Gradle sync (it will download the Gradle wrapper if missing).
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. APK path: `app/build/outputs/apk/release/app-release-unsigned.apk`.

### Option B: Command line

If you have the Android SDK and JDK 17 installed:

```bash
# Generate Gradle wrapper if missing (requires Gradle 8.x)
gradle wrapper

# Build release APK
./gradlew assembleRelease   # macOS/Linux
gradlew.bat assembleRelease # Windows
```

## GitHub Actions – Auto-build on release

The project includes `.github/workflows/build.yml` so that:

- **On tag push** (e.g. `v1.0.0`): the workflow builds the APK and creates a **GitHub Release** with the APK attached.
- **Manual run**: use **Actions → Build APK → Run workflow**.

To get an APK from GitHub:

1. Push the repo to GitHub.
2. Create a tag and push it:  
   `git tag v1.0.0 && git push origin v1.0.0`
3. Open **Releases** and download the APK from the new release.

## Tech stack

| Layer        | Technology        |
|-------------|-------------------|
| Language    | Kotlin 1.9        |
| UI          | Jetpack Compose, Material 3 |
| Database    | Room (SQLite)     |
| DI          | Hilt              |
| Architecture| MVVM              |

## Project structure

```
app/src/main/java/com/progressio/strongclone/
├── data/           # entities, DAOs, database, repositories
├── di/             # Hilt modules
├── ui/
│   ├── screens/    # Home, Workout, History, Progress, Profile
│   ├── theme/      # colors, typography
│   └── navigation/
├── MainActivity.kt
└── StrongCloneApp.kt
```

## License

MIT – use and modify freely.
