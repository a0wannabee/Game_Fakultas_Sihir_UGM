import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
    private ArrayList<Item> catalog;

    public Shop() {
        this.catalog = new ArrayList<>();
        // Weapons
        catalog.add(new KerisApi("Keris Api", Elemen.FIRE, 50, 18));
        catalog.add(new BukuAir("Buku Air", Elemen.WATER, 50, 18));
        catalog.add(new PanahAngin("Panah Angin", Elemen.WIND, 50, 18));
        catalog.add(new TongkatTanah("Tongkat Tanah", Elemen.EARTH, 50, 18));
        
        // Armors
        catalog.add(new Armor("Jas Almamater (Almet)", Elemen.NEUTRAL, 30, 4, "Almet"));
        catalog.add(new Armor("Jas Laboratorium (Jas Lab)", Elemen.NEUTRAL, 60, 8, "Jas Lab"));
        catalog.add(new Armor("Toga Sihir (Toga)", Elemen.NEUTRAL, 120, 15, "Toga"));
    }

    public void bukaShop(Karakter p, Scanner sc) {
        while (true) {
            System.out.println("\n=========================================================");
            System.out.println("KOPMA UGM (TOKO PERALATAN SIHIR FAKULTAS)");
            System.out.println("   Stamina dompet/BP Anda saat ini: " + p.getBattlePoint() + " BP");
            System.out.println("=========================================================");
            
            for (int i = 0; i < catalog.size(); i++) {
                Item item = catalog.get(i);
                String details = "";
                if (item instanceof Weapon) {
                    Weapon w = (Weapon) item;
                    details = String.format("Weapon | Dmg: %d | Elemen: %s", w.getBaseDamage(), w.getElemen());
                } else if (item instanceof Armor) {
                    Armor a = (Armor) item;
                    details = String.format("Armor  | Def: %d | Elemen: %s | Tipe: %s", a.getDefense(), a.getElemen(), a.getTipeArmor());
                }
                
                String namaTampil = item.getNamaItem();
                if (memilikiItem(p, item)) {
                    namaTampil += " [SUDAH DIMILIKI]";
                }
                
                System.out.printf("   [%d] %-25s - %-28s | Harga: %d BP\n", 
                    (i + 1), namaTampil, details, item.getHargaBP());
            }
            System.out.println("   [0] Kembali ke Menu Utama");
            System.out.print("\nPilih barang yang ingin dibeli (0-" + catalog.size() + "): ");
            
            int choice = -1;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // Clear buffer
            } else {
                sc.nextLine(); // Clear invalid buffer
                System.out.println("   Input tidak valid! Silakan masukkan angka.");
                continue;
            }
            
            if (choice == 0) {
                System.out.println("   Terima kasih telah berbelanja di KOPMA UGM!");
                break;
            }
            
            if (choice < 1 || choice > catalog.size()) {
                System.out.println("   Pilihan tidak tersedia!");
                continue;
            }
            
            Item catalogItem = catalog.get(choice - 1);
            beliItem(p, catalogItem);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean memilikiItem(Karakter p, Item item) {
        // Cek inventory
        for (Item i : p.getInventory().getItems()) {
            if (i.getNamaItem().equalsIgnoreCase(item.getNamaItem())) {
                return true;
            }
        }
        // Cek senjata aktif
        if (p.getItemAktif() != null && p.getItemAktif().getNamaItem().equalsIgnoreCase(item.getNamaItem())) {
            return true;
        }
        // Cek armor aktif
        if (p.getArmorAktif() != null && p.getArmorAktif().getNamaItem().equalsIgnoreCase(item.getNamaItem())) {
            return true;
        }
        return false;
    }

    public void beliItem(Karakter p, Item catalogItem) {
        if (memilikiItem(p, catalogItem)) {
            System.out.println("   Gagal: Item ini sudah kamu miliki!");
            return;
        }
        
        if (p.getBattlePoint() >= catalogItem.getHargaBP()) {
            p.kurangiBP(catalogItem.getHargaBP());
            Item bought = catalogItem.cloneItem();
            
            if (bought != null) {
                p.getInventory().addItem(bought);
                System.out.println("   Berhasil membeli " + catalogItem.getNamaItem() + "! Item dimasukkan ke Inventory.");
            }
        } else {
            System.out.println("   Battle Point (BP) Anda tidak mencukupi!");
        }
    }
}
