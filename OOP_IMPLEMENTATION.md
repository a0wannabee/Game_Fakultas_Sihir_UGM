# OOP Implementation in Game Fakultas Sihir UGM

This document provides a detailed explanation of the Object-Oriented Programming (OOP) concepts implemented in the codebase, which can be directly copied into the final project report.

---

## 1. Encapsulation (Enkapsulasi)

**Concept:** Hiding class internal state and data fields from direct outside manipulation by making fields `private` and exposing access only via `public` getter and setter methods.

*   **Class Name:** `Karakter`
*   **File Name:** `Karakter.java`
*   **Code Location:** `org.example.character` ([Karakter.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/character/Karakter.java#L4-L24))
*   **Code Snippet:**
    ```java
    public class Karakter {
        private String nama;
        private int hp;
        private int maxHp;
        private int level;
        private int exp;
        private int battlePoint;
        private Weapon itemAktif;
        private Armor armorAktif;
        private Inventory inventory;
        private int maxDungeonUnlocked;

        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }
        // ... other getters and setters
    }
    ```
*   **Explanation:** Fields like `hp`, `maxHp`, `battlePoint` are kept private so they cannot be corrupted by external classes. Access to these values is controlled strictly via getters and setters, maintaining state consistency.

---

## 2. Inheritance (Pewarisan)

**Concept:** Deriving new classes (subclasses) from existing ones (superclasses) to reuse properties, code logic, and establish hierarchical relationships.

*   **Class Name:** `Weapon` (subclass of `Item`), `PisauNeutral` (subclass of `Weapon`)
*   **File Name:** `Weapon.java` and `PisauNeutral.java`
*   **Code Location:** `org.example.items` ([Weapon.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/items/Weapon.java#L2), [PisauNeutral.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/items/PisauNeutral.java#L3))
*   **Code Snippet:**
    ```java
    // In Weapon.java
    public abstract class Weapon extends Item { ... }

    // In PisauNeutral.java
    public class PisauNeutral extends Weapon { ... }
    ```
*   **Explanation:** `Weapon` inherits common fields (`namaItem`, `elemen`, `hargaBP`) from `Item`. Concrete weapons like `PisauNeutral` inherit variables (`baseDamage`, `cooldowns`) and general combat formulas from `Weapon`, avoiding code repetition.

---

## 3. Polymorphism (Polimorfisme)

**Concept:** Enabling objects of different subclasses to be treated as instances of their common superclass, allowing dynamic dispatch where the correct method implementation is called at runtime based on the actual object type.

*   **Class Name:** `BattleScene`, `Karakter`
*   **File Name:** `BattleScene.java`
*   **Code Location:** `org.example.ui` ([BattleScene.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/ui/BattleScene.java#L400))
*   **Code Snippet:**
    ```java
    player.getItemAktif().basicAttack(player, currentEnemy);
    ```
*   **Explanation:** `player.getItemAktif()` returns an instance of `Weapon` (the abstract superclass). When `.basicAttack(...)` is invoked, the Java runtime dynamically dispatches it to the specific overridden method inside `KerisApi`, `BukuAir`, or `PisauNeutral` depending on what weapon is currently equipped by the character.

---

## 4. Method Overriding (Overriding Metode)

**Concept:** Redefining a method of a superclass or interface in a subclass to provide a specific implementation.

*   **Class Name:** `PisauNeutral`
*   **File Name:** `PisauNeutral.java`
*   **Code Location:** `org.example.items` ([PisauNeutral.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/items/PisauNeutral.java#L24-L31))
*   **Code Snippet:**
    ```java
    @Override
    public int basicAttack(Karakter user, Monster target) {
        double mod = getDamageModifier(target.getElemen());
        int damage = (int) (getBaseDamage() * 1.0 * mod);
        System.out.println(user.getNama() + " melakukan " + getBasicAttackName() + "!");
        printElementalEffectMessage(target.getElemen(), mod);
        target.terimaDamage(damage);
        return damage;
    }
    ```
*   **Explanation:** The abstract method `basicAttack(...)` declared in `Weapon.java` is overridden in `PisauNeutral.java` to define specific text output ("Sayatan Pisau") and damage calculation.

---

## 5. Method Overloading (Overloading Metode)

**Concept:** Defining multiple methods or constructors in the same class with the same name but different signatures (different parameters).

*   **Class Name:** `BattleScene`
*   **File Name:** `BattleScene.java`
*   **Code Location:** `org.example.ui` ([BattleScene.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/ui/BattleScene.java#L181-L206))
*   **Code Snippet:**
    ```java
    public BattleScene(Karakter player, int dungeonLevel) {
        this.player = player;
        this.dungeonLevel = dungeonLevel;
        // ...
    }

    public BattleScene(Karakter player) {
        this(player, 1);
    }

    public BattleScene() {
        this(new Karakter("Achwan"), 1);
    }
    ```
*   **Explanation:** The constructor of `BattleScene` is overloaded three times. This provides flexibility: launching a battle at a specific level, starting dungeon level 1 by default, or running a mock standalone scene for development/testing without parameters.

---

## 6. Abstract Class (Kelas Abstrak)

**Concept:** A class that cannot be instantiated directly and serves as a blueprint for other classes, containing abstract methods that subclasses must implement.

*   **Class Name:** `Item`
*   **File Name:** `Item.java`
*   **Code Location:** `org.example.items` ([Item.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/items/Item.java#L3))
*   **Code Snippet:**
    ```java
    public abstract class Item {
        // ... fields and concrete methods
        public abstract Item cloneItem();
    }
    ```
*   **Explanation:** `Item` is abstract because a generic item does not exist in the game—only specific items like `Weapon` or `Armor` do. Subclasses are forced to provide concrete implementations for the abstract `cloneItem()` method.

---

## 7. Interface (Antarmuka)

**Concept:** A contract defining abstract methods that implementing classes must implement. It specifies *what* a class should do, but not *how*.

*   **Class Name:** `BattleSystem`
*   **File Name:** `BattleSystem.java`
*   **Code Location:** `org.example.battle` ([BattleSystem.java](file:///c:/Users/Lenovo/Downloads/Game_Fakultas_Sihir_UGM-main/Game_Fakultas_Sihir_UGM-main/src/main/java/org/example/battle/BattleSystem.java#L5-L7))
*   **Code Snippet:**
    ```java
    public interface BattleSystem {
        void mulaiPertarungan(Karakter p, Monster m, Scanner sc);
    }
    ```
*   **Explanation:** The `BattleSystem` interface defines the protocol for running a battle. It allows changing the battle engine in CLI/GUI launchers seamlessly because both implementers conform to the same interface method.
