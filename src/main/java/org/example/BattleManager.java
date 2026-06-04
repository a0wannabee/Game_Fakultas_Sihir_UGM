package org.example;

import java.util.Scanner;

public class BattleManager implements BattleSystem {
    @Override
    public void mulaiPertarungan(Karakter p, Monster m, Scanner sc) {
        System.out.println("\nPERTARUNGAN AKADEMIK DIMULAI");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int turn = 1;

        while (p.getHp() > 0 && m.getHp() > 0) {
            System.out.println("\n------------------- TURN " + turn + " -------------------");
            
            // Player stats
            System.out.println("Mahasiswa: " + p.getNama() + " (Semester " + p.getLevel() + ")");
            System.out.println("   HP       : " + getHpBar(p.getHp(), p.getMaxHp()) + " " + p.getHp() + "/" + p.getMaxHp());
            System.out.println("   Senjata  : " + p.getItemAktif().getNamaItem() + " (" + p.getItemAktif().getElemen() + ")");
            
            // Normal Skill CD display
            if (p.getItemAktif().getCurrentCdNormal() == 0) {
                System.out.println("   Normal   : READY (" + p.getItemAktif().getNormalSkillName() + ")");
            } else {
                System.out.println("   Normal   : COOLDOWN (" + p.getItemAktif().getCurrentCdNormal() + " turn lagi)");
            }

            // Ultimate Skill CD display
            if (p.getItemAktif().getCurrentCdUltimate() == 0) {
                System.out.println("   Ultimate : READY (" + p.getItemAktif().getUltimateSkillName() + ")");
            } else {
                System.out.println("   Ultimate : COOLDOWN (" + p.getItemAktif().getCurrentCdUltimate() + " turn lagi)");
            }
            
            // Monster stats
            System.out.println("\nMonster: " + m.getNama() + " (" + m.getElemen() + ")");
            System.out.println("   HP       : " + getHpBar(m.getHp(), m.getMaxHp()) + " " + m.getHp() + "/" + m.getMaxHp());
            System.out.println("---------------------------------------------");

            // Player Choice
            System.out.println("Pilih Aksi Anda:");
            System.out.println("1. Basic Attack: " + p.getItemAktif().getBasicAttackName());
            System.out.println("2. Normal Skill: " + p.getItemAktif().getNormalSkillName());
            System.out.println("3. Ultimate Skill: " + p.getItemAktif().getUltimateSkillName());
            System.out.print("Pilihan (1-3): ");

            int action = -1;
            if (sc.hasNextInt()) {
                action = sc.nextInt();
                sc.nextLine(); // Clear buffer
            } else {
                sc.nextLine(); // Clear invalid buffer
                System.out.println("   Pilihan tidak valid! Masukkan angka 1, 2, atau 3.");
                continue;
            }

            boolean turnUsed = false;
            if (action == 1) {
                p.getItemAktif().basicAttack(p, m);
                turnUsed = true;
            } else if (action == 2) {
                if (p.getItemAktif().getCurrentCdNormal() == 0) {
                    p.getItemAktif().normalSkill(p, m);
                    turnUsed = true;
                } else {
                    System.out.println("   Normal Skill sedang cooldown! Silakan pilih aksi lain.");
                    continue;
                }
            } else if (action == 3) {
                if (p.getItemAktif().getCurrentCdUltimate() == 0) {
                    p.getItemAktif().ultimateSkill(p, m);
                    turnUsed = true;
                } else {
                    System.out.println("   Ultimate Skill sedang cooldown! Silakan pilih aksi lain.");
                    continue;
                }
            } else {
                System.out.println("   Pilihan salah! Masukkan angka 1, 2, atau 3.");
                continue;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check if monster dead
            if (m.getHp() <= 0) {
                System.out.println("\nKemenangan Mutlak!");
                System.out.println("   Anda berhasil mengalahkan " + m.getNama() + "!");
                
                // Rewards
                p.tambahBP(m.getDropBP());
                p.tambahExp(m.getDropExp());

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            }

            // Monster Turn
            m.attack(p);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check if player dead
            if (p.getHp() <= 0) {
                System.out.println("\nKELAHAN AKADEMIK MENDERA");
                System.out.println("   Anda tidak sadarkan diri di tengah pengerjaan laporan.");
                System.out.println("   Teman-teman Anda membawa Anda ke GMC UGM...");
                p.setHp(p.getMaxHp()); // Full recovery
                System.out.println("   Setelah beristirahat, HP Anda pulih sepenuhnya (" + p.getHp() + "/" + p.getMaxHp() + ").");
                System.out.println("   Anda dipulangkan kembali ke lobi utama FSUGM.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            }

            // Reduce cooldown if turn was spent
            if (turnUsed) {
                p.getItemAktif().reduceCooldown();
            }
            turn++;
        }
    }

    public static String getHpBar(int current, int max) {
        int totalBlocks = 15;
        double percentage = (double) current / max;
        int filledBlocks = (int) Math.round(percentage * totalBlocks);
        if (filledBlocks < 0) filledBlocks = 0;
        if (filledBlocks > totalBlocks) filledBlocks = totalBlocks;
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < filledBlocks; i++) {
            bar.append("=");
        }
        for (int i = filledBlocks; i < totalBlocks; i++) {
            bar.append(" ");
        }
        bar.append("]");
        return bar.toString();
    }
}
