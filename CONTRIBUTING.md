# Contributing to Calculator Android

Thank you for your interest in contributing to Calculator Android! We appreciate your help in making this project better for everyone.

## Getting Started

### Prerequisites

Before you start contributing, please ensure you have:

- **Android Studio** (Ladybug | 2024.2.1 or later)
- **JDK 17** or higher
- **Kotlin 2.0+**
- **Git** for version control

### Setting Up the Project

1. **Clone the repository**
   ```bash
   git clone https://github.com/muhammad-fiaz/Calculator-Android.git
   cd Calculator-Android
   ```

2. **Open in Android Studio**
   - File → Open → Select the project directory
   - Wait for Gradle sync to complete

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

## How to Contribute

### Reporting Issues

1. **Check existing issues** - Look through existing GitHub issues to see if your issue has already been reported
2. **Create a new issue** - Use the issue template provided by the repository
3. **Provide details** - Include steps to reproduce, expected vs actual behavior, and any relevant screenshots

### Fixing Bugs

1. **Fork the repository** - Create your own copy of the project
2. **Create a branch** - Use descriptive branch names:
   - `bugfix/` for bug fixes
   - `feature/` for new features
   - `docs/` for documentation improvements
3. **Make your changes** - Fix the bug or implement the feature
4. **Test your changes** - Run the test suite to ensure everything works
5. **Submit a pull request** - Create a PR against the main branch

### Adding Features

1. **Discuss your idea** - Open an issue to discuss your proposed feature
2. **Follow the existing patterns** - Maintain consistency with the codebase
3. **Add tests** - Ensure your feature is properly tested
4. **Update documentation** - Add or update relevant documentation

## Code Quality

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use `ktlint` for formatting: `./gradlew ktlintFormat`
- Use `detekt` for static analysis: `./gradlew detekt`
- Write tests for new features and bug fixes

### Commit Messages

- Use descriptive commit messages
- Follow conventional commit format when possible
- Keep commits focused on single functionality
- Reference issues in commit messages when applicable

## Testing

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run all tests
./gradlew check
```

### Test Structure

- **Unit tests**: Located in `app/src/test/`
- **Instrumented tests**: Located in `app/src/androidTest/`
- **Test coverage**: Target >80%

## Pull Request Process

1. **Ensure tests pass** - All tests must pass before merging
2. **Code review** - Your changes will be reviewed by maintainers
3. **Address feedback** - Make any requested changes
4. **Merge** - Once approved, your changes will be merged

## Maintainer Guidelines

### Reviewing Pull Requests

- Check code quality and style
- Ensure tests pass
- Verify documentation is updated
- Consider the impact on existing functionality

### Merging Pull Requests

- Use squash merge for clean history
- Update the changelog if necessary
- Ensure all CI checks pass

## Thank You!

We appreciate your contributions to make Calculator Android better for everyone. Your help makes this project stronger and more useful for the community.

If you have any questions about contributing, please don't hesitate to ask!