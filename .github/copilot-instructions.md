# GitHub Copilot Instructions for WiFiAnalyzer

## Project Overview

WiFiAnalyzer is an Android application for analyzing WiFi networks. It helps users:
- Identify nearby Access Points
- Graph channel signal strength
- Analyze Wi-Fi networks to rate channels
- Support 2.4 GHz, 5 GHz and 6 GHz Wi-Fi bands
- Export access point details

**Important**: WiFiAnalyzer is NOT a Wi-Fi password cracking or phishing tool.

## Technology Stack

- **Language**: Kotlin
- **Platform**: Android
- **Build Tool**: Gradle
- **Testing**: JUnit, Mockito, Robolectric
- **Code Style**: ktlint
- **License**: GNU General Public License v3.0 (GPLv3)

## Coding Standards and Conventions

### Kotlin Style Guide

1. **Code Formatting**: Use ktlint for code formatting
   - Run `./gradlew ktlintCheck` to check code style
   - Run `./gradlew ktlintFormat` to auto-format code

2. **File Headers**: All source files must include the GPLv3 license header:
   ```kotlin
   /*
    * WiFiAnalyzer
    * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

3. **Naming Conventions**:
   - Use descriptive names for classes, methods, and variables
   - Follow Kotlin naming conventions (camelCase for variables/methods, PascalCase for classes)

4. **Code Organization**:
   - Main source code: `app/src/main/kotlin/`
   - Test code: `app/src/test/kotlin/`
   - Android tests: `app/src/androidTest/kotlin/`

### Testing Requirements

1. **Unit Tests are Mandatory**:
   - All new features and bug fixes MUST include unit tests
   - Maintain or improve code coverage (currently tracked via Codecov)
   - Use Mockito for mocking dependencies
   - Use Robolectric for Android-specific testing

2. **Test File Naming**: Test files should follow the pattern `[ClassName]Test.kt`

3. **Test Structure**: Follow the AAA pattern:
   - Arrange: Set up test data and mocks
   - Act: Execute the code being tested
   - Assert: Verify the results

4. **Running Tests**:
   - Unit tests: `./gradlew testDebugUnitTest`
   - Unit tests with coverage: `./gradlew jacocoTestCoverageVerification`
   - Test reports: `app/build/reports/tests/testDebugUnitTest/index.html`
   - Coverage reports: `app/build/reports/jacoco/jacocoTestReport/html/index.html`

## Build and Development Workflow

### Building the Project

1. **Initial Setup**:
   - Install Android Studio
   - Import the project into Android Studio
   - The project will build automatically

2. **Gradle Commands**:
   - Check code style: `./gradlew ktlintCheck`
   - Format code: `./gradlew ktlintFormat`
   - Run lint: `./gradlew lintDebug`
   - Run tests: `./gradlew testDebugUnitTest`
   - Coverage: `./gradlew jacocoTestCoverageVerification`

3. **Reports Location**:
   - Lint: `app/build/reports/lint-results.html`
   - Tests: `app/build/reports/tests/testDebugUnitTest/index.html`
   - Coverage: `app/build/reports/jacoco/jacocoTestReport/html/index.html`

### Continuous Integration

GitHub Actions automatically runs on every push and pull request:
- Code style check with ktlint
- Lint analysis
- Unit tests with coverage (uploaded to Codecov)
- APK build (debug)
- Artifacts (reports and APK) available for download

## Pull Request Guidelines

1. **Before Submitting**:
   - Search existing issues (open and closed) to avoid duplicates
   - Comment on the issue you're working on
   - Keep changes focused and avoid large change sets
   - Include unit tests for all changes

2. **PR Description**:
   - Provide a clear description of changes
   - Reference related issues (use "Closes #123" to auto-close issues)
   - Include testing details (OS, platform, toolchain version)

3. **Commit Messages**:
   - Use clear, descriptive commit messages
   - Reference issues in commit messages when applicable

## Project-Specific Guidelines

### Privacy and Security

1. **No Data Collection**: WiFiAnalyzer does not collect any personal/device information
2. **No Internet**: The app does not require internet access
3. **Minimal Permissions**: Use only necessary Android permissions
4. **No Secrets**: Never commit API keys, passwords, or other secrets

### Feature Development

1. **Open Source Focus**: Remember this is an open-source project under GPLv3
2. **User Privacy**: Prioritize user privacy in all features
3. **Android Compatibility**: Test on various Android versions when possible
4. **Documentation**: Update README.md or USER_MANUAL.md if adding user-facing features

### Bug Fixes

1. **Reproducibility**: Ensure bugs are reproducible before fixing
2. **Device Information**: Consider that bugs may be device or Android version-specific
3. **Screenshots**: Provide screenshots for UI-related bugs when applicable

### Translation and Localization

- WiFiAnalyzer supports multiple languages via Weblate
- Translation changes should be made through the Weblate project
- Do not manually edit translation files unless absolutely necessary

## Common Patterns in the Codebase

1. **Mockito Usage**: Tests use Mockito with Kotlin extensions:
   ```kotlin
   import org.mockito.kotlin.mock
   import org.mockito.kotlin.verify
   import org.mockito.kotlin.whenever
   ```

2. **Test Teardown**: Tests typically verify no unexpected interactions:
   ```kotlin
   @After
   fun tearDown() {
       verifyNoMoreInteractions(dependency1, dependency2)
   }
   ```

3. **Robolectric Tests**: Android component tests use Robolectric:
   ```kotlin
   val activity = Robolectric
       .buildActivity(MainActivity::class.java)
       .create()
       .resume()
       .get()
   ```

## Resources

- [Contributing Guidelines](../CONTRIBUTING.md)
- [Pull Request Template](../PULL_REQUEST_TEMPLATE.md)
- [User Manual](../USER_MANUAL.md)
- [Weblate Translation Project](https://hosted.weblate.org/engage/wifianalyzer/)

## Questions or Issues?

- Open an issue on GitHub for bugs or feature requests
- Check existing issues before creating new ones
- Use GitHub Discussions for general questions
