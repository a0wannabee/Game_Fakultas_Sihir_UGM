package org.example;

public class BukuAir extends Weapon {
    public BukuAir(String namaItem, Elemen elemen, int hargaBP, int baseDamage) {
        super(namaItem, elemen, hargaBP, baseDamage);
    }

    @Override
    public String getBasicAttackName() {
        return "Pukulan Buku Diktat";
    }

    @Override
    public String getNormalSkillName() {
        return "Rapalan Mantra Air";
    }

    @Override
    public String getUltimateSkillName() {
        return "Tsunami Pengetahuan";
    }

    @Override
    public int basicAttack(Karakter user, Monster target) {
        double mod = getDamageModifier(target.getElemen());
        int damage = (int) (getBaseDamage() * 1.0 * mod);
        System.out.println(user.getNama() + " melakukan " + getBasicAttackName() + "!");
        printElementalEffectMessage(target.getElemen(), mod);
        target.terimaDamage(damage);
        return damage;
    }

    @Override
    public int normalSkill(Karakter user, Monster target) {
        double mod = getDamageModifier(target.getElemen());
        int damage = (int) (getBaseDamage() * 1.5 * mod);
        System.out.println(user.getNama() + " melepaskan " + getNormalSkillName() + "!");
        printElementalEffectMessage(target.getElemen(), mod);
        target.terimaDamage(damage);
        setCurrentCdNormal(2);
        return damage;
    }

    @Override
    public int ultimateSkill(Karakter user, Monster target) {
        double mod = getDamageModifier(target.getElemen());
        int damage = (int) (getBaseDamage() * 2.5 * mod);
        System.out.println(user.getNama() + " melepaskan Ultimate: " + getUltimateSkillName() + "!");
        printElementalEffectMessage(target.getElemen(), mod);
        target.terimaDamage(damage);
        setCurrentCdUltimate(4);
        return damage;
    }

    @Override
    public Item cloneItem() {
        return new BukuAir(getNamaItem(), getElemen(), getHargaBP(), getBaseDamage());
    }
}
