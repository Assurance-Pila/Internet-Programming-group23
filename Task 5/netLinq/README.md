# NetLinq

Native Android app for Quality of Experience (QoE) mobile network monitoring in Cameroon.

**Stack:** Kotlin · Jetpack Compose · Room · WorkManager · Supabase

See [SRS.md](./SRS.md) for the full software requirements specification.

---

## Quick start (if you're coming from Flutter)

| Flutter habit | Native Android equivalent |
|---------------|---------------------------|
| `flutter run` | Click **Run ▶** in Android Studio, or `./gradlew installDebug` |
| `flutter pub get` | **Sync Project with Gradle Files** (elephant icon) |
| `pubspec.yaml` | `gradle/libs.versions.toml` + `app/build.gradle.kts` |
| `lib/main.dart` | `app/src/main/java/com/netlinq/MainActivity.kt` |
| Hot reload | **Apply Changes** (⚡ lightning bolt) — not as seamless as Flutter |
| `flutter doctor` | Android Studio SDK Manager + `./gradlew --version` |

---

## Prerequisites

You already have most of this from Flutter setup:

- [x] Android SDK (`~/Library/Android/sdk`)
- [x] JDK 17+
- [x] Android Studio

---

## 1. First-time setup

### Create `local.properties`

```bash
cd netLinq
cp local.properties.example local.properties
```

Edit `local.properties`:

```properties
sdk.dir=/Users/user/Library/Android/sdk
supabase.url=https://YOUR_PROJECT_REF.supabase.co
supabase.anon.key=YOUR_SUPABASE_ANON_KEY
```

> **Note:** The app runs without Supabase keys for now. Keys are wired for when we add sync.

### Open the project in Android Studio

1. Open **Android Studio**
2. **File → Open** → select the `netLinq` folder (the one containing `settings.gradle.kts`)
3. Wait for **Gradle sync** to finish (first sync downloads dependencies — can take a few minutes)
4. If prompted, accept SDK licenses or install missing components

---

## 2. Run the app

### Option A — Android Studio (recommended for beginners)

1. Open **Device Manager** (phone icon in toolbar)
2. Create or start an **Android Virtual Device (AVD)** — same emulators you use for Flutter work
3. Select the emulator (or a plugged-in phone) in the device dropdown
4. Click the green **Run ▶** button (or `Ctrl+R` / `Shift+F10`)

You should see the NetLinq home screen: *"QoE Mobile Network Monitoring — Project scaffold ready."*

### Option B — Command line

```bash
cd netLinq

# Build and install on connected device/emulator
./gradlew installDebug

# Launch the app (requires adb in PATH)
adb shell am start -n com.netlinq/.MainActivity
```

Add to `~/.zshrc` if `adb` is not found:

```bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Option C — VS Code / Cursor

Native Android is best developed in **Android Studio**. VS Code/Cursor work for editing Kotlin files, but you still need Android Studio (or command line) to build, run, and manage emulators.

---

## 3. Project structure

```
netLinq/
├── SRS.md                          # Requirements reference
├── README.md                       # This file
├── app/
│   ├── build.gradle.kts            # App dependencies & config
│   └── src/main/
│       ├── AndroidManifest.xml     # Permissions, app entry
│       └── java/com/netlinq/
│           ├── MainActivity.kt     # App entry (like main.dart)
│           └── ui/theme/           # Compose theme
├── gradle/libs.versions.toml       # Dependency versions (like pubspec)
├── build.gradle.kts                # Root build config
└── settings.gradle.kts             # Project modules
```

Planned package layout as features are added:

```
com.netlinq/
├── presentation/    # Screens, ViewModels
├── domain/          # Use cases, business logic
├── data/            # Room, Supabase, repositories
├── monitoring/      # Network metric collectors
└── sync/            # WorkManager sync
```

---

## 4. Supabase setup (when you're ready)

Create a project at [supabase.com](https://supabase.com), then share:

1. **Project URL** — `https://xxxx.supabase.co`
2. **Anon public key** — safe to embed in the app (with RLS enabled)

Add both to `local.properties` as shown above.

We will add tables for:

- `network_metrics` — signal, latency, network type
- `qoe_feedback` — subjective ratings
- Aggregated views for operator analytics

Row Level Security (RLS) will be enabled on all tables.

---

## 5. Common issues

### Gradle sync failed

- Ensure `local.properties` exists with `sdk.dir=...`
- **File → Invalidate Caches → Restart** in Android Studio
- Check internet connection (Gradle downloads from Maven)

### No devices found

- Start an emulator from Device Manager, or
- Enable **USB debugging** on a physical phone and connect via USB

### JDK version mismatch

This project uses **Java 17**. Flutter may already point to it:

```bash
flutter config --jdk-dir="/Users/user/Homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
```

---

## 6. What's next

- [ ] Architecture foundation (Hilt, navigation, Room)
- [ ] Anonymous device ID + consent flow
- [ ] Network monitoring (signal, latency, connectivity)
- [ ] QoE feedback forms + notifications
- [ ] Supabase sync
- [ ] User dashboard

---

## License

Academic project — Internet Programming Group 23.
