package org.example.inventory;
import java.util.ArrayList;
import org.example.items.*;

public class Inventory {
    private ArrayList<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void listItems() {
        if (items.isEmpty()) {
            System.out.println("   (Inventory kosong)");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String info = "";
            if (item instanceof Weapon) {
                Weapon w = (Weapon) item;
                String type = "";
                if (w instanceof PisauNeutral) type = "Pisau Neutral";
                else if (w instanceof KerisApi) type = "Keris Api";
                else if (w instanceof BukuAir) type = "Buku Air";
                else if (w instanceof PanahAngin) type = "Panah Angin";
                else if (w instanceof TongkatTanah) type = "Tongkat Tanah";
                info = String.format("Weapon (%s) | Base Damage: %d | Elemen: %s", 
                    type, w.getBaseDamage(), w.getElemen());
            } else if (item instanceof Armor) {
                Armor a = (Armor) item;
                info = String.format("Armor (%s) | Defense: %d | Elemen: %s", 
                    a.getTipeArmor(), a.getDefense(), a.getElemen());
            }
            System.out.println("   [" + (i + 1) + "] " + item.getNamaItem() + " (" + info + ")");
        }
    }
}
