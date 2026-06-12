# Project Structure & Architecture

This document describes the package organization and system architecture of **Game Fakultas Sihir UGM** to be used in the report section *"Penjelasan Struktur Kode Program"*.

---

## 1. Package Directory

The codebase is organized into 9 logical packages under `org.example.*` to ensure separation of concerns and high modularity:

*   **`org.example.core`**: Launcher package. Contains the main entry points of the game.
    *   `FakultasSihirApp.java`: Main JavaFX launcher class utilizing the FXGL framework. Manages the map exploration overlays, music, UI overlays, inventory panels, and shop screens.
    *   `Main.java`: Standalone CLI launcher class for executing the game directly in text mode.
*   **`org.example.character`**: Character management package.
    *   `Karakter.java`: Defines character properties (HP, EXP, Level/Semester, Battle Points), equipment inventory bindings, level up routines, and damage reception formulas.
*   **`org.example.battle`**: Back-end CLI battle and system management package.
    *   `BattleSystem.java`: Interface declaring the text battle execution flow.
    *   `BattleManager.java`: Implements `BattleSystem` for text battles. Handles combat loops and visualizes CLI turn status updates.
    *   `Dungeon.java`: Manages dungeon minion/boss layouts and triggers the sequence of battles.
*   **`org.example.inventory`**: Inventory container management package.
    *   `Inventory.java`: Handles items storage, lists current weapons/armors, and supports equipping operations.
*   **`org.example.items`**: Game item hierarchies.
    *   `Item.java`: Abstract superclass defining properties like price, name, and element.
    *   `Weapon.java`: Abstract class representing weapon logic and cooldown tracking.
    *   `Armor.java`: Subclass representing armor protection values.
    *   `BukuAir.java`, `KerisApi.java`, `PanahAngin.java`, `PisauNeutral.java`, `TongkatTanah.java`: Specialized weapon implementations representing distinct elements and skills.
*   **`org.example.monsters`**: Monster logic.
    *   `Monster.java`: Contains monster base statistics, random attacks, elemental damage calculations, and defense checks.
*   **`org.example.shop`**: Text-based shop.
    *   `Shop.java`: Logic for purchasing items and managing Kopma UGM CLI catalog.
*   **`org.example.ui`**: Front-end graphical Swing UI.
    *   `BattleScene.java`: Integrates graphics, animations, HP bars, skill casting triggers, and window listeners for graphical battles.
*   **`org.example.utils`**: General utilities.
    *   `Elemen.java`: Enumeration class for elemental cycles (`FIRE`, `WATER`, `EARTH`, `WIND`, `NEUTRAL`).

---

## 2. System Architecture

The game uses a **Hybrid Exploration/Combat Architecture** that bridges two frameworks:

1.  **Exploration Mode (JavaFX / FXGL)**:
    *   Handles map navigation, overlay popups, character profile viewing, and purchase transactions via FXGL nodes.
    *   All user settings and active stats are bound to JavaFX property structures.
2.  **Battle Mode (Java Swing)**:
    *   Triggered dynamically when entering a dungeon. The JavaFX event loop temporarily pauses map audio, launches a Swing JFrame wrapper (`BattleScene`) inside the AWT Thread, and passes the updated character object (`Karakter`).
    *   Once the battle terminates (win or loss), a WindowListener catches the closure, coordinates stats synchronization, recovers GUI map focus, and plays the background map music.
