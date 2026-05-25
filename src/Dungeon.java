import java.util.ArrayList;
import java.util.Scanner;

public class Dungeon {
    private String namaDungeon;
    private ArrayList<Monster> daftarMusuh;
    private Monster boss;
    private int level;
    private String deskripsi;

    public Dungeon(String namaDungeon, ArrayList<Monster> daftarMusuh, Monster boss, int level, String deskripsi) {
        this.namaDungeon = namaDungeon;
        this.daftarMusuh = daftarMusuh;
        this.boss = boss;
        this.level = level;
        this.deskripsi = deskripsi;
    }

    public String getNamaDungeon() {
        return namaDungeon;
    }

    public ArrayList<Monster> getDaftarMusuh() {
        return daftarMusuh;
    }

    public Monster getBoss() {
        return boss;
    }

    public int getLevel() {
        return level;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void masukDungeon(Karakter k, BattleSystem bs, Scanner sc) {
        System.out.println("\n=========================================================");
        System.out.println("MEMASUKI DUNGEON: " + this.namaDungeon + " (Level " + this.level + ")");
        System.out.println("   Deskripsi: " + this.deskripsi);
        System.out.println("   Hadapi kroco-kroco penjaga sebelum melawan Boss!");
        System.out.println("=========================================================");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Loop through minion list (daftarMusuh)
        if (daftarMusuh != null && !daftarMusuh.isEmpty()) {
            for (int i = 0; i < daftarMusuh.size(); i++) {
                Monster minion = daftarMusuh.get(i);
                System.out.println("\n---------------------------------------------------------");
                System.out.println("KROCO TANTANGAN " + (i + 1) + " dari " + daftarMusuh.size());
                System.out.println("Lawan: " + minion.getNama() + " (" + minion.getElemen() + ")");
                System.out.println("---------------------------------------------------------");
                
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // Reset minion HP for combat
                minion.setHp(minion.getMaxHp());
                
                bs.mulaiPertarungan(k, minion, sc);

                // If player is defeated, stop immediately
                if (k.getHp() <= 0) {
                    System.out.println("   Anda gugur sebelum sempat mencapai Boss!");
                    return;
                }
            }
            System.out.println("\nSelamat! Semua kroco berhasil dikalahkan. Pintu Boss kini terbuka!");
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Proceed to Boss Fight
        System.out.println("\n=========================================================");
        System.out.println("BOSS AKHIR DUNGEON: " + boss.getNama() + " (" + boss.getElemen() + ")");
        System.out.println("=========================================================");
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Reset Boss HP before battle
        boss.setHp(boss.getMaxHp());

        bs.mulaiPertarungan(k, boss, sc);

        // Check if player won against the Boss
        if (k.getHp() > 0) {
            System.out.println("\n=========================================================");
            System.out.println("   DUNGEON SELESAI: " + this.namaDungeon);
            System.out.println("=========================================================");
            if (k.getMaxDungeonUnlocked() == this.level) {
                k.setMaxDungeonUnlocked(this.level + 1);
                System.out.println("   Selamat! Anda berhasil membuka akses ke Dungeon selanjutnya!");
            }
        }
    }
}
