# Final Report Guide & Outline

This guide defines the structure and outline for the **Game Fakultas Sihir UGM** final project report (Laporan Akhir PPBO). Mapped using the current project layout and implementation.

---

## 1. Pendahuluan (Introduction)
*   **Latar Belakang**: RPG themed game representing UGM's Bulaksumur magic academy, using modern OOP principles.
*   **Tujuan Proyek**: Implementing design patterns, JavaFX interfaces, AWT Swing layouts, clean packages division, and Java compilation cycles.
*   **Teknologi Utama**: Java 21/25, Maven, FXGL (JavaFX Game Library), and Java Swing.

## 2. Desain Sistem / UML (System Design)
*   **UML Class Diagram**: Incorporate the Mermaid syntax diagram located in `UML_CLASS_DIAGRAM.md`.
*   **Class Relationships**:
    *   *Inheritance*: How subclasses like `Weapon` extend `Item` and weapons extend `Weapon`.
    *   *Associations/Agregasi*: How `Inventory` aggregates `Item` objects and how `Karakter` owns them.
    *   *Interfaces*: How `BattleSystem` enforces method implementation for `BattleManager`.

## 3. Penjelasan Implementasi OOP (OOP Implementation)
*   For each of the OOP aspects, copy details directly from `OOP_IMPLEMENTATION.md`:
    *   **Encapsulation**: `Karakter.java` private fields & public accessors.
    *   **Inheritance**: `Item.java` -> `Weapon.java` -> subclasses hierarchy.
    *   **Polymorphism**: Dynamic call dispatch of `basicAttack` in `BattleScene`.
    *   **Method Overriding**: Concrete implementation of abstract attributes.
    *   **Method Overloading**: Mutated constructor variants in `BattleScene`.
    *   **Abstract Classes**: `Item` and `Weapon` structures.
    *   **Interfaces**: `BattleSystem` implementation contract.

## 4. Penjelasan Struktur Kode Program (Code Structure)
*   Use the exact details in `PROJECT_STRUCTURE.md` to explain the 9 sub-packages under `org.example.*` and the integration of FXGL map exploration with Swing combat scenes.

## 5. Demo Program (Program Demonstration)
*   **Starting Page**: Name registration input using FXGL overlay textfields.
*   **Map Navigation**: Invisible clickable buttons layered on maps for dungeon level triggers, shop screens, and stats check.
*   **Inventory Screen**: Character stats bars (Max HP, Semester) and clickable slot items for equipping.
*   **Shop (Kopma UGM)**: Credit checks, item selection previews, and purchase verification logs.
*   **Battle Interface**: Swing frames rendering animated sprites (idle, hit, cast, defeat) matching currently equipped weapons and armor, alongside active HP bars.

## 6. Analisis (Analysis)
*   **Kelebihan (Strengths)**:
    *   Centralized logic prevents data inconsistency (e.g. `memilikiItem` deduplication).
    *   Strong decoupling of assets from hardcoded paths.
    *   Separation of CLI and GUI entries inside modular packages.
*   **Kekurangan (Weaknesses)**:
    *   Heavy dependency on FXGL platform constraints.
    *   High memory load due to coexistence of AWT and JavaFX thread loops.

## 7. Pembagian Tugas Kelompok (Team Task Matrix)
*   Use the predefined matrix template in `TEAM_CONTRIBUTION_TEMPLATE.md` to document duties.
