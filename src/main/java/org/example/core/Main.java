package org.example.core;

import java.util.ArrayList;
import java.util.Scanner;
import org.example.character.Karakter;
import org.example.items.*;
import org.example.battle.*;
import org.example.shop.Shop;
import org.example.monsters.Monster;
import org.example.utils.Elemen;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=========================================================");
        System.out.println("        SELAMAT DATANG DI FAKULTAS SIHIR UGM");
        System.out.println("           - RPG Akademik Lembah Bulaksumur -");
        System.out.println("=========================================================");

        System.out.print("Masukkan nama Anda (Nama Mahasiswa): ");
        String namaMhs = sc.nextLine().trim();
        if (namaMhs.isEmpty()) {
            namaMhs = "Si Master";
        }

        // Instansiasi Karakter
        Karakter player = new Karakter(namaMhs);

        // Setup awal: diberikan default item (Pisau Neutral)
        Weapon startingWeapon = new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);

        player.getInventory().addItem(startingWeapon);
        player.equipWeapon(startingWeapon);

        System.out.println("\nPersiapan Selesai! Anda mendapatkan 50 BP sebagai modal awal.");
        player.setBattlePoint(50);

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BattleSystem battleSystem = new BattleManager();
        Shop kopmaUgm = new Shop();

        // Main Loop Game
        boolean running = true;
        while (running) {
            System.out.println("\n=========================================================");
            System.out.println("FAKULTAS SIHIR UGM - MENU UTAMA");
            System.out.println("=========================================================");
            System.out.println("   [1] Cek Status & Inventory");
            System.out.println("   [2] Informasi Elemen");
            System.out.println("   [3] Kunjungi KOPMA UGM (Shop)");
            System.out.println("   [4] Masuk Dungeon (Lab Praktikum)");
            System.out.println("   [5] Keluar Game");
            System.out.print("\nMasukkan pilihan Anda (1-5): ");

            int menuChoice = -1;
            if (sc.hasNextInt()) {
                menuChoice = sc.nextInt();
                sc.nextLine(); // Clear buffer
            } else {
                sc.nextLine(); // Clear invalid buffer
                System.out.println("   Input tidak valid! Silakan pilih 1-5.");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            switch (menuChoice) {
                case 1:
                    cekStatusDanInventory(player, sc);
                    break;
                case 2:
                    printInformasiElemen();
                    break;
                case 3:
                    kopmaUgm.bukaShop(player, sc);
                    break;
                case 4:
                    masukDungeonMenu(player, battleSystem, sc);
                    break;
                case 5:
                    System.out.println("\nAnda memutuskan untuk tidur. Sampai jumpa di semester depan!");
                    running = false;
                    break;
                default:
                    System.out.println("   Pilihan tidak tersedia! Masukkan nomor 1-5.");
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
            }
        }

        sc.close();
    }

    private static void cekStatusDanInventory(Karakter p, Scanner sc) {
        while (true) {
            System.out.println("\n=========================================================");
            System.out.println("STATUS MAHASISWA FAKULTAS SIHIR UGM");
            System.out.println("=========================================================");
            System.out.println("   Nama         : " + p.getNama());
            System.out.println("   Semester     : " + p.getLevel());
            System.out.println("   HP           : " + p.getHp() + " / " + p.getMaxHp());
            System.out.println("   EXP          : " + p.getExp() + " / " + p.getMaxExp());
            System.out.println("   Battle Point : " + p.getBattlePoint() + " BP");
            System.out.println("   Senjata Aktif: "
                    + (p.getItemAktif() != null
                            ? p.getItemAktif().getNamaItem() + " (" + p.getItemAktif().getElemen() + " | Dmg: "
                                    + p.getItemAktif().getBaseDamage() + ")"
                            : "Tidak ada"));
            System.out.println("   Armor Aktif  : " + (p.getArmorAktif() != null
                    ? p.getArmorAktif().getNamaItem() + " (" + p.getArmorAktif().getElemen() + " | Def: "
                            + p.getArmorAktif().getDefense() + " | Tipe: " + p.getArmorAktif().getTipeArmor() + ")"
                    : "Tidak ada"));
            System.out.println("\nISI INVENTORY:");
            p.getInventory().listItems();
            System.out.println("=========================================================");
            System.out.println("   [1] Gunakan (Equip) Item dari Inventory");
            System.out.println("   [0] Kembali ke Menu Utama");
            System.out.print("\nPilih opsi Anda: ");

            int opt = -1;
            if (sc.hasNextInt()) {
                opt = sc.nextInt();
                sc.nextLine(); // Clear buffer
            } else {
                sc.nextLine(); // Clear invalid buffer
                System.out.println("   Input tidak valid!");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            if (opt == 0) {
                break;
            } else if (opt == 1) {
                ArrayList<Item> items = p.getInventory().getItems();
                if (items.isEmpty()) {
                    System.out.println("   Inventory kosong! Tidak ada item yang bisa dipakai.");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                System.out.print("Pilih nomor item yang ingin digunakan (1-" + items.size() + "): ");
                int itemIdx = -1;
                if (sc.hasNextInt()) {
                    itemIdx = sc.nextInt();
                    sc.nextLine(); // Clear buffer
                } else {
                    sc.nextLine(); // Clear invalid buffer
                    System.out.println("   Input tidak valid!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                if (itemIdx < 1 || itemIdx > items.size()) {
                    System.out.println("   Nomor item salah!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }

                Item selectedItem = items.get(itemIdx - 1);
                if (selectedItem instanceof Weapon) {
                    p.equipWeapon((Weapon) selectedItem);
                } else if (selectedItem instanceof Armor) {
                    p.equipArmor((Armor) selectedItem);
                }
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("   Pilihan tidak tersedia!");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static void printInformasiElemen() {
        System.out.println("\n=========================================================");
        System.out.println("INFORMASI SISTEM ELEMEN (ELEMENTAL CYCLE)");
        System.out.println("=========================================================");
        System.out.println("Berikut adalah rantai hubungan elemen sihir di Fakultas:");
        System.out.println();
        System.out.println("           +-------> [ FIRE ] -------+");
        System.out.println("           |                         |");
        System.out.println("           |                         v");
        System.out.println("       [ WATER ]                 [ WIND ]");
        System.out.println("           ^                         |");
        System.out.println("           |                         v");
        System.out.println("           +------- [ EARTH ] <------+");
        System.out.println();
        System.out.println("Hubungan Siklus Kelemahan:");
        System.out.println("   - FIRE membakar WIND");
        System.out.println("   - WIND mengikis EARTH");
        System.out.println("   - EARTH membendung WATER");
        System.out.println("   - WATER memadamkan FIRE");
        System.out.println("=========================================================");
    }

    private static void masukDungeonMenu(Karakter player, BattleSystem bs, Scanner sc) {
        int maxUnlocked = player.getMaxDungeonUnlocked();

        System.out.println("\n=========================================================");
        System.out.println("PILIH DUNGEON FAKULTAS SIHIR UGM");
        System.out.println("=========================================================");
        System.out.println("   [1] Gedung MIPA Selatan (Level 1) - " + (maxUnlocked >= 1 ? "TERBUKA" : "TERKUNCI"));
        System.out.println("   [2] Lembah Bulaksumur (Level 2) - " + (maxUnlocked >= 2 ? "TERBUKA" : "TERKUNCI"));
        System.out.println("   [3] Hutan Biologi UGM (Level 3) - " + (maxUnlocked >= 3 ? "TERBUKA" : "TERKUNCI"));
        System.out.println("   [4] Gedung Pusat UGM (Level 4) - " + (maxUnlocked >= 4 ? "TERBUKA" : "TERKUNCI"));
        System.out.println("   [0] Kembali ke Menu Utama");
        System.out.print("\nPilih Dungeon (0-4): ");

        int choice = -1;
        if (sc.hasNextInt()) {
            choice = sc.nextInt();
            sc.nextLine();
        } else {
            sc.nextLine();
            System.out.println("   Input tidak valid!");
            return;
        }

        if (choice == 0)
            return;

        if (choice < 1 || choice > 4) {
            System.out.println("   Pilihan tidak tersedia!");
            return;
        }

        if (choice > maxUnlocked) {
            System.out.println("   Dungeon ini masih terkunci! Selesaikan dungeon sebelumnya terlebih dahulu.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }

        Dungeon selectedDungeon = null;
        ArrayList<Monster> krocoList = new ArrayList<>();

        switch (choice) {
            case 1:
                krocoList.add(new Monster("Sapi Fapet", 30, 6, 0, Elemen.EARTH, 10, 20));
                krocoList.add(new Monster("Tupai UGM", 35, 7, 0, Elemen.NEUTRAL, 12, 25));
                krocoList.add(new Monster("Kucing Vokasi", 40, 8, 1, Elemen.WIND, 15, 30));
                Monster boss1 = new Monster("Hantu Mbak Yayuk", 100, 12, 2, Elemen.FIRE, 50, 100);
                selectedDungeon = new Dungeon("Gedung MIPA Selatan", krocoList, boss1, 1,
                        "Gedung legendaris di MIPA Selatan yang terkenal sunyi setelah jam kuliah selesai. Katanya ada penunggu koridor bernama Mbak Yayuk yang hobi menatap kosong mahasiswa yang telat ngerjain laporan.");
                break;
            case 2:
                krocoList.add(new Monster("Sapi Fapet", 50, 10, 1, Elemen.EARTH, 15, 35));
                krocoList.add(new Monster("Tupai UGM", 55, 12, 2, Elemen.NEUTRAL, 18, 40));
                krocoList.add(new Monster("Kucing Vokasi", 60, 14, 2, Elemen.WIND, 22, 45));
                Monster boss2 = new Monster("Hantu Muka Rata", 160, 20, 4, Elemen.EARTH, 80, 160);
                selectedDungeon = new Dungeon("Lembah Bulaksumur", krocoList, boss2, 2,
                        "Tempat nongkrong sore mahasiswa UGM yang teduh dan asri. Namun, jangan sekali-kali jalan sendirian di sini saat larut malam, karena hantu bermuka rata sering muncul untuk menanyakan progres skripsimu.");
                break;
            case 3:
                krocoList.add(new Monster("Sapi Fapet", 80, 16, 3, Elemen.EARTH, 25, 55));
                krocoList.add(new Monster("Tupai UGM", 85, 18, 3, Elemen.NEUTRAL, 28, 60));
                krocoList.add(new Monster("Kucing Vokasi", 90, 20, 4, Elemen.WIND, 32, 65));
                Monster boss3 = new Monster("Kuntilanak Pertanian", 240, 28, 6, Elemen.WIND, 120, 240);
                selectedDungeon = new Dungeon("Hutan Biologi UGM", krocoList, boss3, 3,
                        "Rimbunnya pepohonan di Hutan Biologi UGM selalu mengundang rasa penasaran. Saat maghrib tiba, tawa melengking dari Kuntilanak Pertanian siap meneror siapa saja yang nekat melintas.");
                break;
            case 4:
                krocoList.add(new Monster("Sapi Fapet", 120, 24, 5, Elemen.EARTH, 35, 80));
                krocoList.add(new Monster("Tupai UGM", 130, 26, 6, Elemen.NEUTRAL, 40, 90));
                krocoList.add(new Monster("Kucing Vokasi", 140, 28, 7, Elemen.WIND, 45, 100));
                Monster boss4 = new Monster("Rektor Gaib", 350, 38, 9, Elemen.WATER, 200, 400);
                selectedDungeon = new Dungeon("Gedung Pusat UGM", krocoList, boss4, 4,
                        "Pusat birokrasi kampus yang megah di siang hari, namun terasa angker di malam hari. Di sinilah Rektor Gaib bersemayam, siap menggagalkan pengajuan banding penurunan UKT mahasiswa dengan kekuatan magisnya.");
                break;
        }

        if (selectedDungeon != null) {
            selectedDungeon.masukDungeon(player, bs, sc);
        }
    }
}
