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

    public Karakter(String nama) {
        this.nama = nama;
        this.maxHp = 100;
        this.hp = 100;
        this.level = 1;
        this.exp = 0;
        this.battlePoint = 50; // Starting BP
        this.inventory = new Inventory();
        this.maxDungeonUnlocked = 1;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getBattlePoint() {
        return battlePoint;
    }

    public void setBattlePoint(int battlePoint) {
        this.battlePoint = battlePoint;
    }

    public Weapon getItemAktif() {
        return itemAktif;
    }

    public void setItemAktif(Weapon itemAktif) {
        this.itemAktif = itemAktif;
    }

    public Armor getArmorAktif() {
        return armorAktif;
    }

    public void setArmorAktif(Armor armorAktif) {
        this.armorAktif = armorAktif;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void equipWeapon(Weapon w) {
        if (w == null)
            return;

        // Put current weapon back to inventory
        if (this.itemAktif != null) {
            this.inventory.addItem(this.itemAktif);
            System.out
                    .println("   Melepas senjata: " + this.itemAktif.getNamaItem() + " dan menyimpannya ke Inventory.");
        }

        this.itemAktif = w;
        this.inventory.removeItem(w);
        System.out.println("   Mempersiapkan " + w.getNamaItem() + " sebagai senjata aktif Anda!");
    }

    public void equipArmor(Armor a) {
        if (a == null)
            return;

        // Put current armor back to inventory
        if (this.armorAktif != null) {
            this.inventory.addItem(this.armorAktif);
            System.out
                    .println("   Melepas armor: " + this.armorAktif.getNamaItem() + " dan menyimpannya ke Inventory.");
        }

        this.armorAktif = a;
        this.inventory.removeItem(a);
        System.out.println("   Mengenakan " + a.getNamaItem() + " sebagai pelindung aktif Anda!");
    }

    public void terimaDamage(int damage) {
        int finalDef = (this.armorAktif != null) ? this.armorAktif.getDefense() : 0;
        int netDamage = damage - finalDef;
        if (netDamage < 1)
            netDamage = 1; // Min damage is 1

        this.hp -= netDamage;
        if (this.hp < 0)
            this.hp = 0;

        System.out.println("   " + this.nama + " menerima " + netDamage + " damage (Defense menahan " + finalDef
                + "). HP sisa: " + this.hp + "/" + this.maxHp);
    }

    public int getMaxExp() {
        return (int) (100 * Math.pow(1.2, this.level - 1));
    }

    public void tambahExp(int amount) {
        if (this.level >= 14) {
            System.out.println("   Semester Anda sudah maksimal (Semester 14)!");
            return;
        }
        this.exp += amount;
        System.out.println("   + " + amount + " EXP");

        // Logika Level Up (Naik Semester)
        while (this.level < 14 && this.exp >= getMaxExp()) {
            this.exp -= getMaxExp();
            this.level++;

            // --- PERUBAHAN LOGIKA HP DI SINI ---
            // Menghitung 30% dari Max HP saat ini
            int tambahanHp = (int) (this.maxHp * 0.30);
            this.maxHp += tambahanHp; // Menambahkan 30% tersebut ke Max HP

            this.hp = this.maxHp; // Darah langsung full lagi (Full Heal)

            System.out.println("\n=========================================================");
            System.out.println("LEVEL UP! Anda naik ke Semester " + this.level);
            System.out.println(
                    "   Beban Akademik meningkat! Max HP bertambah +" + tambahanHp + " menjadi: " + this.maxHp);
            System.out.println("   Stamina/HP Anda terisi kembali sepenuhnya!");
            System.out.println("=========================================================\n");
        }

        if (this.level >= 14) {
            this.exp = 0; // Kalau udah mentok, EXP di-reset ke 0
        }
    }

    public void tambahBP(int amount) {
        this.battlePoint += amount;
        System.out.println("   + " + amount + " BP (Total BP: " + this.battlePoint + ")");
    }

    public void kurangiBP(int amount) {
        this.battlePoint -= amount;
        if (this.battlePoint < 0)
            this.battlePoint = 0;
        System.out.println("   - " + amount + " BP (Total BP: " + this.battlePoint + ")");
    }

    public int getMaxDungeonUnlocked() {
        return maxDungeonUnlocked;
    }

    public void setMaxDungeonUnlocked(int maxDungeonUnlocked) {
        this.maxDungeonUnlocked = maxDungeonUnlocked;
    }
}
