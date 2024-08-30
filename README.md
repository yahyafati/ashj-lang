# ashj Programming Language

Welcome to **ashj**, a lightweight, interpreted, object-oriented programming language. ashj is inspired
by [Lox](http://www.craftinginterpreters.com/), a language developed in the book *Crafting Interpreters* by Robert
Nystrom. ashj takes the core concepts of Lox and adds a unique twist, making it a powerful tool for developers
interested in simplicity and expressiveness.

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Introduction

ashj is a dynamic, interpreted programming language that supports object-oriented programming, functional programming
paradigms, and more. It's designed to be simple and easy to learn, making it an excellent choice for beginners, as well
as a convenient scripting language for experienced developers.

Inspired by Lox, ashj retains the elegance and minimalism of its predecessor while introducing improvements and
enhancements tailored for modern development.

AshJ is implemented in **Java** and utilizes an **Abstract Syntax Tree (AST)** for interpreting code. This design makes
it straightforward to extend and maintain. Additionally, a **C bytecode implementation** of AshJ, named **Ash**, is
available [here](https://github.com/yahyafati/ash-lang), offering a more performance-oriented alternative.

## Getting Started

### Prerequisites

- **Java 8** or later installed on your machine.

### Installation

Download the latest version of ashj from the [releases page](https://github.com/yahyafati/ashj-lang/releases). Once
downloaded, you can run the installer for your operating system:

- **Windows**: Run the `.exe` installer.
- **macOS**: Run the `.pkg` installer.
- **Linux**: Run the `.deb` or `.rpm` package installer.

### Running ashj

After installation, you can run ashj from the command line:

```bash
ashj path/to/your/script.ashj
```

Or start an interactive REPL session:

```bash
ashj
```

## Features

- **Object-Oriented**: Supports classes and inheritance, allowing for the creation of complex data structures.
- **First-Class Functions**: Functions in ashj can be passed around as first-class citizens, allowing for higher-order
  functions and functional programming techniques.
- **Dynamic Typing**: Variables in ashj are dynamically typed, making it flexible and easy to use.
- **Garbage Collection**: Automatic memory management through garbage collection.
- **Lexical Scoping**: Supports block-scoped variables.
- **Lightweight**: Minimalist language design for quick execution and fast development cycles.

## Usage

### Basic Syntax

Here’s a quick overview of the syntax in ashj:

```java
// Variables
var name = "ashj";
var age = 3 + 4;

// Functions
fun greet() {
    print "Hello, " + name + "!";
}

greet(); // Outputs: Hello, ashj!

// Classes
class Animal {
    init(type) {
        this.type = type;
    }

    speak() {
        print "This is a " + this.type;
    }
}

var dog = Animal("Dog");
dog.

speak(); // Outputs: This is a Dog
```

### Control Structures

```java
// If-Else Statements
if(age >18){
print "Adult";
        }else{
print "Not an Adult";
        }

// While Loop
var count = 0;
while(count< 3){
print count;
count =count +1;
        }

// For Loop
        for(
var i = 0;
i< 5;i =i +1){
print i;
}
```

## Examples

To see more examples, check out the `examples` directory in this repository. You can run these examples using the ashj
interpreter:

```bash
ashj examples/hello_world.ashj
```

## Contributing

Contributions are welcome! If you have ideas for new features or have found bugs, please open an issue or submit a pull
request. See the `CONTRIBUTING.md` file for more details.

## License

ashj is licensed under the MIT License. See the `LICENSE` file for more information.

Certainly! Here’s how you can include an **Acknowledgements** section in your `README.md` file:

---

## Acknowledgements

AshJ would not have been possible without the inspiration and guidance provided by Robert Nystrom's book, *Crafting
Interpreters*. This book offers a comprehensive guide to building interpreters from scratch, and its implementation of
Lox served as the foundation for AshJ.

A special thanks to Robert Nystrom for his incredible work in the programming community and for making *Crafting
Interpreters* freely available to everyone. If you're interested in understanding the inner workings of interpreters, we
highly recommend checking out his book at [craftinginterpreters.com](http://www.craftinginterpreters.com/).