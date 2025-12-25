# AI Assistant Instructions for WiFiAnalyzer

## Project Overview

WiFiAnalyzer is an Android application for analyzing WiFi networks. It helps users:
- Identify nearby Access Points
- Graph channel signal strength
- Analyze Wi-Fi networks to rate channels
- Support 2.4 GHz, 5 GHz and 6 GHz Wi-Fi bands
- Export access point details

**Important**: WiFiAnalyzer is NOT a Wi-Fi password cracking or phishing tool.

## Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| Platform | Android |
| Build Tool | Gradle |
| Testing | JUnit, Mockito, Robolectric, Espresso |
| Code Style | ktlint |
| License | GNU General Public License v3.0 (GPLv3) |

## Project Structure

```
app/src/main/kotlin/    # Main source code
app/src/test/kotlin/    # Unit tests
app/src/androidTest/kotlin/  # Android instrumentation tests
```

## Coding Standards

### File Headers

All source files must include the GPLv3 license header:

```kotlin
/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - {current_year} VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
```

### Naming Conventions

- Use descriptive names for classes, methods, and variables
- Follow Kotlin naming conventions: camelCase for variables/methods, PascalCase for classes

### Code Formatting

Use ktlint for code formatting:
- Check: `./gradlew ktlintCheck`
- Format: `./gradlew ktlintFormat`

## Testing Requirements

### Mandatory Testing

All new features and bug fixes MUST include unit tests.

### Test File Naming

Test files follow the pattern `[ClassName]Test.kt`

### Test Structure (AAA Pattern)

```kotlin
@Test
fun testMethodName() {
    // Arrange: Set up test data and mocks

    // Act: Execute the code being tested

    // Assert: Verify the results
}
```

### Testing Patterns Used

**Mockito with Kotlin extensions:**
```kotlin
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
```

**Test teardown pattern:**
```kotlin
@After
fun tearDown() {
    verifyNoMoreInteractions(dependency1, dependency2)
}
```

**Robolectric for Android components:**
```kotlin
val activity = Robolectric
    .buildActivity(MainActivity::class.java)
    .create()
    .resume()
    .get()
```

## Build Commands

| Task | Command |
|------|---------|
| Check code style | `./gradlew ktlintCheck` |
| Format code | `./gradlew ktlintFormat` |
| Run lint | `./gradlew lintDebug` |
| Run unit tests | `./gradlew testDebugUnitTest` |
| Run tests with coverage | `./gradlew jacocoTestCoverageVerification` |
| Run instrumented tests | `./gradlew connectedDebugAndroidTest` |

## Privacy and Security Guidelines

1. **No Data Collection**: WiFiAnalyzer does not collect any personal/device information
2. **No Internet**: The app does not require internet access
3. **Minimal Permissions**: Use only necessary Android permissions
4. **No Secrets**: Never commit API keys, passwords, or other secrets
