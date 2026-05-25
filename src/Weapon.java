public abstract class Weapon extends Item {
    private int baseDamage;
    private int currentCdNormal;
    private int currentCdUltimate;

    public Weapon(String namaItem, Elemen elemen, int hargaBP, int baseDamage) {
        super(namaItem, elemen, hargaBP);
        this.baseDamage = baseDamage;
        this.currentCdNormal = 0;
        this.currentCdUltimate = 0;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getCurrentCdNormal() {
        return currentCdNormal;
    }

    public void setCurrentCdNormal(int currentCdNormal) {
        this.currentCdNormal = currentCdNormal;
    }

    public int getCurrentCdUltimate() {
        return currentCdUltimate;
    }

    public void setCurrentCdUltimate(int currentCdUltimate) {
        this.currentCdUltimate = currentCdUltimate;
    }

    public void reduceCooldown() {
        if (this.currentCdNormal > 0) {
            this.currentCdNormal--;
        }
        if (this.currentCdUltimate > 0) {
            this.currentCdUltimate--;
        }
    }

    // --- MEKANIK ELEMEN SUDAH DIAKTIFKAN KEMBALI ---
    public double getDamageModifier(Elemen targetElemen) {
        Elemen myElemen = this.getElemen();

        // Neutral vs Neutral
        if (myElemen == Elemen.NEUTRAL && targetElemen == Elemen.NEUTRAL) {
            return 1.2;
        }

        // Advantage
        if ((myElemen == Elemen.FIRE && targetElemen == Elemen.WIND) ||
                (myElemen == Elemen.WIND && targetElemen == Elemen.EARTH) ||
                (myElemen == Elemen.EARTH && targetElemen == Elemen.WATER) ||
                (myElemen == Elemen.WATER && targetElemen == Elemen.FIRE)) {
            return 1.5;
        }

        // Disadvantage
        if ((targetElemen == Elemen.FIRE && myElemen == Elemen.WIND) ||
                (targetElemen == Elemen.WIND && myElemen == Elemen.EARTH) ||
                (targetElemen == Elemen.EARTH && myElemen == Elemen.WATER) ||
                (targetElemen == Elemen.WATER && myElemen == Elemen.FIRE)) {
            return 0.75;
        }

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

    public abstract String getBasicAttackName();

    public abstract String getNormalSkillName();

    public abstract String getUltimateSkillName();

    public abstract int basicAttack(Karakter user, Monster target);

    public abstract int normalSkill(Karakter user, Monster target);

    public abstract int ultimateSkill(Karakter user, Monster target);
}