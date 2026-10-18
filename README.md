# Stick Hero (JavaFX)

A clone of the mobile game *Stick Hero*, written in Java with JavaFX as a college object-oriented programming project. You grow a stick, drop it across the gap and walk the stickman to the next platform without falling.

## Gameplay and features

- **Start screen** with a styled "Start Game" button that scales up on hover.
- **Grow the stick:** hold `SPACE` to grow it, then release to rotate it 90° into a bridge.
- **Walk or fall:** the stickman walks across the stick. He falls if the stick is too short or overshoots the next platform.
- **Random platforms:** platform widths and gap sizes are random. After a successful crossing, the scene slides left (`TranslateTransition`) and a new platform appears.
- **Cherries:** a cherry can spawn in the gap. Press `SPACE` while walking to flip the stickman under the stick and collect it. If he is still upside down when he reaches the next platform, he falls.
- **Game over** screen with a **Retry** button.

## Tech stack

- Java 20, with the Java module system (`module-info.java`)
- JavaFX 20.0.1 (`javafx-controls`, `javafx-fxml`)
- Maven, with the Maven Wrapper and `javafx-maven-plugin` 0.0.8

## Design

The game uses three classes that work through composition. All three draw onto one shared JavaFX `AnchorPane`.

| Class | Responsibility |
|---|---|
| `StickmanGame` | Extends `javafx.application.Application` and is the entry point. It builds the start, game and game-over scenes, handles `SPACE` press and release, and drives the stick-growing loop. It owns one `Stickman` and one `Platform`. |
| `Stickman` | The player and the stick: growing the stick, rotating it with a `Rotate` transform, walking and falling animations, the upside-down flip, cherry collection and resetting between rounds. |
| `Platform` | Generates platforms of random size, tracks the gap distance, animates the screen shift and creates the next platform. |

There is no single game-loop thread. JavaFX `Timeline`s run the frame-by-frame movement (growing, rotating, walking, falling). Key handlers and `setOnFinished` callbacks then chain the game from one state to the next.

## Project structure

```
src/main/
├── java/
│   ├── module-info.java
│   └── com/example/demo18/
│       ├── StickmanGame.java   # Application entry point, scenes, input
│       ├── Stickman.java       # Player + stick logic and animations
│       └── Platform.java       # Platform generation and scrolling
└── resources/com/example/demo18/
    └── hello-view.fxml         # Unused leftover from the IntelliJ JavaFX template
pom.xml                         # Maven build (JavaFX deps + javafx-maven-plugin)
mvnw, mvnw.cmd, .mvn/           # Maven Wrapper
```

## Running

You need JDK 20 or later. The Maven Wrapper is included, so Maven doesn't have to be installed.

```bash
git clone https://github.com/yashsinha1702/stick_hero2.git
cd stick_hero2
./mvnw clean javafx:run     # Windows: mvnw.cmd clean javafx:run
```

### Known issues

This repository is a snapshot of the course submission. A few leftovers stop it from building or running cleanly as-is:

1. **Package name mismatch.** The `.java` files declare `package com.example.demogame;`, but they live in `com/example/demo18/`, and `module-info.java` exports `com.example.demo18`. To fix it, change the package declarations, and the static import in `Stickman.java`, to `com.example.demo18`.
2. **Wrong main class.** The `javafx-maven-plugin` `mainClass` in `pom.xml` still points to the template's `com.example.demo18.HelloApplication`. It should be `com.example.demo18/com.example.demo18.StickmanGame`.
3. **Missing images.** The stickman, cherry and background images load from absolute Windows paths on the original development machine and are not in the repository. They need to go under `src/main/resources` and be loaded with `getClass().getResource(...)`.
