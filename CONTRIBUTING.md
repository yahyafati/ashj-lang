# Contributing to AshJ

Thank you for your interest in contributing to AshJ! By contributing, you’re helping to improve a dynamic, lightweight
programming language that’s open to everyone. Whether you’re reporting a bug, proposing a feature, or submitting a pull
request, your contributions are valuable.

## Table of Contents

- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
    - [Reporting Bugs](#reporting-bugs)
    - [Suggesting Enhancements](#suggesting-enhancements)
    - [Pull Requests](#pull-requests)
- [Code Style](#code-style)
- [Testing](#testing)
- [Branching Model](#branching-model)
- [License](#license)

## Getting Started

1. **Fork the Repository**: Start by forking the repository to your GitHub account.
2. **Clone Your Fork**: Clone your forked repository to your local machine.

    ```bash
    git clone https://github.com/yahyafati/ashj-lang.git
    cd ashj
    ```

3. **Create a New Branch**: Create a new branch for your work.

    ```bash
    git checkout -b feature/your-feature-name
    ```

4. **Make Your Changes**: Start making changes to the codebase.

## How to Contribute

### Reporting Bugs

If you find a bug in AshJ, we’d love to hear about it. To report a bug:

1. **Check Existing Issues**: Before submitting a new issue, please check
   the [issue tracker](https://github.com/yahyafati/ashj-lang/issues) to see if the bug has already been reported.

2. **Submit a New Issue**: If the bug is not listed, create a new issue and include:
    - A clear and descriptive title.
    - A detailed description of the bug.
    - Steps to reproduce the bug.
    - Expected and actual results.
    - Any relevant logs or screenshots.

### Suggesting Enhancements

If you have an idea for a new feature or an improvement to existing functionality:

1. **Check Existing Issues/Discussions**: See if your suggestion is already being discussed in
   the [issue tracker](https://github.com/yahyafati/ashj-lang/issues)
   or [discussions](https://github.com/yahyafati/ashj-lang/discussions).

2. **Submit a New Issue**: If your suggestion is new, submit an issue with:
    - A clear and descriptive title.
    - A detailed explanation of the feature or enhancement.
    - Any relevant examples or use cases.

### Pull Requests

1. **Fork and Clone**: Make sure your fork is up to date with the latest `main` branch from the original repository.

    ```bash
    git pull upstream main
    ```

2. **Commit Your Changes**: Write clear, descriptive commit messages and group related changes into a single commit.

    ```bash
    git commit -m "Add feature X"
    ```

3. **Push Your Branch**: Push your branch to your forked repository.

    ```bash
    git push origin feature/your-feature-name
    ```

4. **Open a Pull Request**: Go to the original repository on GitHub and open a pull request. Provide a detailed
   description of your changes and link to any relevant issues.

## Code Style

Please adhere to the following guidelines to maintain a consistent code style:

- **Indentation**: Use 4 spaces for indentation.
- **Line Length**: Limit lines to 80 characters.
- **Naming Conventions**: Use `camelCase` for variables and functions, and `PascalCase` for classes.
- **Comments**: Use comments to explain why something is done, not what is done.

## Testing

Before submitting a pull request, ensure that your changes are covered by tests and that all tests pass:

1. **Run Tests**: Use the provided test suite to run all tests.

    ```bash
    ./run_tests.sh
    ```

2. **Add Tests**: If you’re adding a new feature, include corresponding tests in the `tests` directory.

## Branching Model

We follow a simple branching model:

- **main**: The stable branch containing the latest release.
- **feature/your-feature-name**: Feature branches for new development.
- **bugfix/your-bugfix-name**: Branches for bug fixes.

## License

By contributing to AshJ, you agree that your contributions will be licensed under the MIT License.