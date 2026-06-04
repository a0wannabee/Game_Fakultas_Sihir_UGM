public class Armor extends Item {
    private int defense;
    private String tipeArmor;

    public Armor(String namaItem, Elemen elemen, int hargaBP, int defense, String tipeArmor) {
        super(namaItem, elemen, hargaBP);
        this.defense = defense;
        this.tipeArmor = tipeArmor;
    }

    public int getDefense() {
        return defense;
    }

    public String getTipeArmor() {
        return tipeArmor;
    }

    @Override
    public Item cloneItem() {
        return new Armor(getNamaItem(), getElemen(), getHargaBP(), getDefense(), getTipeArmor());
    }
}