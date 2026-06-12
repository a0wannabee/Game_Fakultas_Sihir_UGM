package org.example.battle;
import org.example.character.Karakter;
import org.example.monsters.Monster;

import java.util.Scanner;

public interface BattleSystem {
    void mulaiPertarungan(Karakter p, Monster m, Scanner sc);
}
