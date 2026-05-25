public abstract class Item {
    private String namaItem;
    private Elemen elemen;
    private int hargaBP;

    public Item(String namaItem, Elemen elemen, int hargaBP) {
        this.namaItem = namaItem;
        this.elemen = elemen;
        this.hargaBP = hargaBP;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public Elemen getElemen() {
        return elemen;
    }

    public int getHargaBP() {
        return hargaBP;
    }

    public abstract Item cloneItem();
}