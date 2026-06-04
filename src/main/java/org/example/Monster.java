package org.example;

public class Monster {
    private String nama;
    private int hp;
    private int maxHp;
    private int baseDamage;
    private int defense;
    private Elemen elemen;
    private int dropBP;
    private int dropExp;

    public Monster(String nama, int hp, int baseDamage, int defense, Elemen elemen, int dropBP, int dropExp) {
        this.nama = nama;
        this.hp = hp;
        this.maxHp = hp;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.elemen = elemen;
        this.dropBP = dropBP;
        this.dropExp = dropExp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public Elemen getElemen() {
        return elemen;
    }

    public void setElemen(Elemen elemen) {
        this.elemen = elemen;
    }

    public int getDropBP() {
        return dropBP;
    }

    public void setDropBP(int dropBP) {
        this.dropBP = dropBP;
    }

    public int getDropExp() {
        return dropExp;
    }

    public void setDropExp(int dropExp) {
        this.dropExp = dropExp;
    }

    public void attack(Karakter target) {
        Elemen targetArmorElemen = (target.getArmorAktif() != null) ? target.getArmorAktif().getElemen()
                : Elemen.NEUTRAL;
        double mod = getDamageModifier(targetArmorElemen);
        int finalDamage = (int) (this.baseDamage * mod);

        System.out.println("\n" + this.nama + " merapalkan sihir tugas [" + getRandomSkillName() + "]!");
        printElementalEffectMessage(targetArmorElemen, mod);
        target.terimaDamage(finalDamage);
    }

    public void terimaDamage(int damage) {
        int netDamage = damage - this.defense;
        if (netDamage < 1)
            netDamage = 1;

        this.hp -= netDamage;
        if (this.hp < 0)
            this.hp = 0;

        System.out.println("   " + this.nama + " menerima " + netDamage + " damage (Defense menahan " + this.defense
                + "). HP sisa: " + this.hp + "/" + this.maxHp);
    }

    public double getDamageModifier(Elemen targetElemen) {
        Elemen myElemen = this.getElemen();

        // Neutral vs Neutral bonus
        if (myElemen == Elemen.NEUTRAL && targetElemen == Elemen.NEUTRAL) {
            return 1.2;
        }

        // Advantage (Kita menang elemen)
        if ((myElemen == Elemen.FIRE && targetElemen == Elemen.WIND) ||
                (myElemen == Elemen.WIND && targetElemen == Elemen.EARTH) ||
                (myElemen == Elemen.EARTH && targetElemen == Elemen.WATER) ||
                (myElemen == Elemen.WATER && targetElemen == Elemen.FIRE)) {
            return 1.5;
        }

        // Disadvantage (Musuh menang elemen)
        if ((targetElemen == Elemen.FIRE && myElemen == Elemen.WIND) ||
                (targetElemen == Elemen.WIND && myElemen == Elemen.EARTH) ||
                (targetElemen == Elemen.EARTH && myElemen == Elemen.WATER) ||
                (targetElemen == Elemen.WATER && myElemen == Elemen.FIRE)) {
            return 0.75;
        }

        // Default (Misal Api vs Tanah, atau Api vs Api)
        return 1.0;
    }

    public void printElementalEffectMessage(Elemen targetElemen, double mod) {
        if (mod == 1.5) {
            System.out.println("   [SUPER EFFECTIVE] Elemen serangan unggul! Damage +50%");
        } else if (mod == 0.75) {
            System.out.println("   [NOT EFFECTIVE] Elemen serangan lemah! Damage -25%");
        } else if (mod == 1.2) {
            System.out.println("   [NEUTRAL CLASH] Resonansi elemen netral! Damage +20%");
        }
    }

    private String getRandomSkillName() {
        String[] skills = { "Ujian Dadakan Sihir", "Revisi Format Laporan", "Pertanyaan Jebakan Presentasi",
                "Kuis Kejutan Praktikum" };
        int index = (int) (Math.random() * skills.length);
        return skills[index];
    }
}
