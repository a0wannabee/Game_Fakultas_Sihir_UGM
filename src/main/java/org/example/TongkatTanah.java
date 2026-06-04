package org.example;
public class TongkatTanah extends Weapon {
    public TongkatTanah(String namaItem, Elemen elemen, int hargaBP, int baseDamage) {
        super(namaItem, elemen, hargaBP, baseDamage);
    }

    @Override
    public String getBasicAttackName() {
        return "Ketukan Tongkat";
    }

    @Override
    public String getNormalSkillName() {
        return "Dinding Tanah";
    }

    @Override
    public String getUltimateSkillName() {
        return "Gempa Bulaksumur";
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
        return new TongkatTanah(getNamaItem(), getElemen(), getHargaBP(), getBaseDamage());
    }
}
