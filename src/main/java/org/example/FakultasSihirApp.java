
package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FakultasSihirApp extends GameApplication {

    static class GameItem {
        String name;
        String type;
        String path;
        String folderName;

        public GameItem(String name, String type, String path, String folderName) {
            this.name = name;
            this.type = type;
            this.path = path;
            this.folderName = folderName;
        }
    }

    static class ShopItem {
        GameItem item;
        int price;
        String rowFramePath;
        String previewFramePath;

        public ShopItem(GameItem item, int price, String rowFramePath, String previewFramePath) {
            this.item = item;
            this.price = price;
            this.rowFramePath = rowFramePath;
            this.previewFramePath = previewFramePath;
        }
    }

    private GameItem equippedWeapon;
    private GameItem equippedArmor;
    private final GameItem[] inventory = new GameItem[8];

    private final GameItem defaultWeapon = new GameItem(
            "Pisau Neutral",
            "weapon",
            "item/weapon/pisau_neutral.png",
            "pisau_neutral");

    private int playerCredit = 50;

    private Item selectedShopItem = null;
    private int selectedShopPrice = 0;
    private String selectedShopPreviewFramePath = null;

    private ImageView shopPreviewFrame = null;
    private Text shopCreditLabel = null;

    private Entity titleScreen;
    private Entity inputNameScreen;
    private Entity mapScreen;
    private Entity battleScreen;
    private BattleScene activeBattleScene;
    private Karakter gamePlayer;

    private Button btnTitleClick;
    private Button btnMulaiInput;
    private TextField inputNamaField;

    private Node currentOverlay = null;

    private final List<Button> mapButtons = new ArrayList<>();

    private boolean inTitleScreen = true;
    private boolean inInputNameScreen = false;
    private boolean inBattleScreen = false;

    private String playerName = "Si Master";
    private int dungeonUnlocked = 1;

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static final String TITLE_SCREEN_PATH = "background/title/title_screen.png";
    private static final String INPUT_NAME_SCREEN_PATH = "background/title/Input_name.jpeg";
    private static final String MAP_SCREEN_PATH = "background/map/map_with_objects.png";
    private static final String BATTLE_HALL_PATH = "background/battle/battle_hall.png";

    private static final String INVENTORY_BG_PATH = "ui/inventory/Inventory.png";
    private static final String INVENTORY_EMPTY_SLOT_PATH = "ui/inventory/Union.png";
    private static final String INVENTORY_EMPTY_RED_SLOT_PATH = "ui/inventory/merah.png";

    private static final String SHOP_BG_PATH = "ui/shop/Shop.png";

    private MediaPlayer titleMusicPlayer;
    private MediaPlayer mapMusicPlayer;

    private static final String TITLE_MUSIC_PATH = "/assets/music/Lagu Pionir.m4a";
    private static final String MAP_MUSIC_PATH = "/assets/music/Nadi_Gadjah_Mada.mp3";

    // ================= INVENTORY LAYOUT =================
    // LINE ± 111 - 139
    // BAGIAN INI YANG MENGATUR POSISI INVENTORY.

    private static final double INV_CHAR_X = 100;
    private static final double INV_CHAR_Y = 145;
    private static final double INV_CHAR_W = 292;
    private static final double INV_CHAR_H = 325;

    private static final double INV_EQUIP_X = 422;
    private static final double INV_WEAPON_Y = 217;
    private static final double INV_ARMOR_Y = 301;
    private static final double INV_EQUIP_SIZE = 62;

    private static final double INV_SLOT_START_X = 126;
    private static final double INV_SLOT_START_Y = 490;
    private static final double INV_SLOT_SPACING_X = 81.2;
    private static final double INV_SLOT_SIZE = 62;

    private static final double INV_TEXT_COVER_X = 575;
    private static final double INV_TEXT_COVER_Y = 140;
    private static final double INV_TEXT_COVER_W = 300;
    private static final double INV_TEXT_COVER_H = 210;

    // ================= SHOP LAYOUT =================
    // LINE ± 141 - 177
    // BAGIAN INI YANG MENGATUR POSISI SHOP.

    private static final double SHOP_ROW_X = 80;
    private static final double SHOP_ROW_Y = 118;
    private static final double SHOP_ROW_W = 735;
    private static final double SHOP_ROW_H = 42;
    private static final double SHOP_ROW_GAP = 46;

    private static final double SHOP_CREDIT_X = 130;
    private static final double SHOP_CREDIT_Y = 518;

    private static final double SHOP_PREVIEW_X = 792;
    private static final double SHOP_PREVIEW_Y = 465;
    private static final double SHOP_PREVIEW_SIZE = 62;

    private static final double SHOP_BUY_X = 485;
    private static final double SHOP_BUY_Y = 460;
    private static final double SHOP_BUY_W = 230;
    private static final double SHOP_BUY_H = 60;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setTitle("Fakultas Sihir UGM");
        settings.setVersion("");
        settings.setPreserveResizeRatio(true);
    }

    @Override
    protected void initInput() {
        FXGL.onKeyDown(KeyCode.ENTER, () -> {
            if (inTitleScreen) {
                tampilkanInputNama();
            } else if (inInputNameScreen) {
                mulaiGameDariInputNama();
            }
        });

        FXGL.onKeyDown(KeyCode.ESCAPE, () -> {
            if (inBattleScreen) {
                keluarDariBattleKeMap();
            } else if (currentOverlay != null) {
                tutupOverlayAktif();
            }
        });
    }

    @Override
    protected void initGame() {
        initInventoryData();
        tampilkanTitleScreen();
    }

    @Override
    protected void initUI() {
        buatTombolTitleScreen();
    }

    private void initInventoryData() {
        equippedWeapon = defaultWeapon;
        equippedArmor = null;
        for (int i = 0; i < 8; i++) {
            inventory[i] = null;
        }
    }

    private void tampilkanTitleScreen() {
        try {
            bersihkanSemuaScreen();

            Texture titleTexture = FXGL.texture(TITLE_SCREEN_PATH);
            titleTexture.setFitWidth(SCREEN_WIDTH);
            titleTexture.setFitHeight(SCREEN_HEIGHT);

            titleScreen = FXGL.entityBuilder()
                    .at(0, 0)
                    .zIndex(0)
                    .view(titleTexture)
                    .buildAndAttach();

            inTitleScreen = true;
            inInputNameScreen = false;
            inBattleScreen = false;

            playTitleMusic();
            buatTombolTitleScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buatTombolTitleScreen() {
        if (!inTitleScreen)
            return;

        if (btnTitleClick != null) {
            FXGL.getGameScene().removeUINode(btnTitleClick);
        }

        btnTitleClick = new Button();
        btnTitleClick.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        btnTitleClick.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btnTitleClick.setOnAction(e -> tampilkanInputNama());

        FXGL.getGameScene().addUINode(btnTitleClick);
    }

    private void tampilkanInputNama() {
        if (!inTitleScreen)
            return;

        try {
            if (titleScreen != null) {
                titleScreen.removeFromWorld();
                titleScreen = null;
            }

            if (btnTitleClick != null) {
                FXGL.getGameScene().removeUINode(btnTitleClick);
                btnTitleClick = null;
            }

            Texture inputTexture = FXGL.texture(INPUT_NAME_SCREEN_PATH);
            inputTexture.setFitWidth(SCREEN_WIDTH);
            inputTexture.setFitHeight(SCREEN_HEIGHT);

            inputNameScreen = FXGL.entityBuilder()
                    .at(0, 0)
                    .zIndex(0)
                    .view(inputTexture)
                    .buildAndAttach();

            inTitleScreen = false;
            inInputNameScreen = true;
            inBattleScreen = false;

            buatInputNamaUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buatInputNamaUI() {
        inputNamaField = new TextField();
        inputNamaField.setPromptText("");
        inputNamaField.setTranslateX(330);
        inputNamaField.setTranslateY(534);
        inputNamaField.setPrefSize(620, 46);
        inputNamaField.setAlignment(Pos.CENTER_LEFT);
        inputNamaField.setStyle(
                "-fx-background-color: #eeeeee;" +
                        "-fx-text-fill: #303030;" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-family: 'Consolas';" +
                        "-fx-border-color: transparent;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 0 0 0 18;");
        inputNamaField.setOnAction(e -> mulaiGameDariInputNama());

        btnMulaiInput = new Button();
        btnMulaiInput.setTranslateX(560);
        btnMulaiInput.setTranslateY(590);
        btnMulaiInput.setPrefSize(170, 55);
        btnMulaiInput.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btnMulaiInput.setOnAction(e -> mulaiGameDariInputNama());

        FXGL.getGameScene().addUINode(inputNamaField);
        FXGL.getGameScene().addUINode(btnMulaiInput);

        inputNamaField.requestFocus();
    }

    private void mulaiGameDariInputNama() {
        if (!inInputNameScreen)
            return;

        String namaInput = inputNamaField.getText();
        playerName = (namaInput != null && !namaInput.trim().isEmpty())
                ? namaInput.trim()
                : "Si Master";

        gamePlayer = buatKarakterUntukBattle();
        dungeonUnlocked = gamePlayer.getMaxDungeonUnlocked();
        syncFromPlayer();

        if (inputNameScreen != null) {
            inputNameScreen.removeFromWorld();
            inputNameScreen = null;
        }

        if (inputNamaField != null) {
            FXGL.getGameScene().removeUINode(inputNamaField);
            inputNamaField = null;
        }

        if (btnMulaiInput != null) {
            FXGL.getGameScene().removeUINode(btnMulaiInput);
            btnMulaiInput = null;
        }

        inTitleScreen = false;
        inInputNameScreen = false;
        inBattleScreen = false;

        playMapMusic();
        tampilkanMap();
    }

    private void tampilkanMap() {
        try {
            bersihkanBattleScreen();
            bersihkanMapButtons();
            tutupOverlayAktif();

            if (mapScreen != null) {
                mapScreen.removeFromWorld();
                mapScreen = null;
            }

            Texture mapTexture = FXGL.texture(MAP_SCREEN_PATH);
            mapTexture.setFitWidth(SCREEN_WIDTH);
            mapTexture.setFitHeight(SCREEN_HEIGHT);

            mapScreen = FXGL.entityBuilder()
                    .at(0, 0)
                    .zIndex(0)
                    .view(mapTexture)
                    .buildAndAttach();

            buatTombolMenuKiri();
            buatTombolDungeon();

            inTitleScreen = false;
            inInputNameScreen = false;
            inBattleScreen = false;

            playMapMusic();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buatTombolMenuKiri() {
        tambahMapButton(createInvisibleButton(22, 52, 60, 60, () -> showUiOverlay("Ui/Screens/Element Stats.png")));
        tambahMapButton(createInvisibleButton(22, 150, 60, 60, this::showInventoryScreen));
        tambahMapButton(createInvisibleButton(22, 250, 60, 60, this::showShopScreen));
    }

    private void buatTombolDungeon() {
        tambahMapButton(createInvisibleButton(90, 1, 160, 220, () -> masukDungeon(1)));
        tambahMapButton(createInvisibleButton(150, 350, 220, 200, () -> masukDungeon(2)));
        tambahMapButton(createInvisibleButton(680, 40, 250, 300, () -> masukDungeon(3)));
        tambahMapButton(createInvisibleButton(800, 400, 360, 150, () -> masukDungeon(4)));
    }

    private void masukDungeon(int nomorDungeon) {
        if (gamePlayer == null) {
            gamePlayer = buatKarakterUntukBattle();
        }

        sinkronkanStatusKarakterSebelumBattle();

        if (nomorDungeon > dungeonUnlocked) {
            FXGL.getDialogService().showMessageBox(
                    "Dungeon masih terkunci!\n" +
                            "Selesaikan dungeon sebelumnya terlebih dahulu.");
            return;
        }

        if (nomorDungeon < 1 || nomorDungeon > 4) {
            FXGL.getDialogService().showMessageBox("Dungeon tidak tersedia!");
            return;
        }

        masukBattleScene(nomorDungeon);
    }

    private void masukBattleScene(int nomorDungeon) {
        try {
            tutupOverlayAktif();
            bersihkanMapButtons();

            if (mapScreen != null) {
                mapScreen.removeFromWorld();
                mapScreen = null;
            }

            if (battleScreen != null) {
                battleScreen.removeFromWorld();
                battleScreen = null;
            }

            inTitleScreen = false;
            inInputNameScreen = false;
            inBattleScreen = true;

            stopMusic(mapMusicPlayer);

            final Karakter playerUntukBattle = gamePlayer;
            final int dungeonYangDibuka = nomorDungeon;

            SwingUtilities.invokeLater(() -> {
                activeBattleScene = new BattleScene(playerUntukBattle, dungeonYangDibuka);

                activeBattleScene.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Platform.runLater(() -> selesaiBattleKembaliKeMap());
                    }
                });
            });

        } catch (Exception e) {
            e.printStackTrace();
            FXGL.getDialogService().showMessageBox("Gagal membuka BattleScene!");
            tampilkanMap();
        }
    }

    private void selesaiBattleKembaliKeMap() {
        if (!inBattleScreen) {
            return;
        }

        activeBattleScene = null;
        inBattleScreen = false;

        syncFromPlayer();

        tampilkanMap();
    }

    private void keluarDariBattleKeMap() {
        if (activeBattleScene != null) {
            activeBattleScene.dispose();
            return;
        }

        bersihkanBattleScreen();
        tampilkanMap();
    }

    private void showUiOverlay(String imagePath) {
        tutupOverlayAktif();

        try {
            Texture overlayTex = FXGL.texture(imagePath);
            overlayTex.setFitWidth(1000);
            overlayTex.setFitHeight(600);
            overlayTex.setOnMouseClicked(e -> e.consume());

            Rectangle darkBackground = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.color(0, 0, 0, 0.6));
            darkBackground.setOnMouseClicked(e -> tutupOverlayAktif());

            StackPane overlayContainer = new StackPane(darkBackground, overlayTex);
            overlayContainer.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);

            currentOverlay = overlayContainer;
            FXGL.getGameScene().addUINode(currentOverlay);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================================
    // INVENTORY SYSTEM
    // =========================================================================

    private void showInventoryScreen() {
        tutupOverlayAktif();

        try {
            syncFromPlayer();
            Rectangle darkBackground = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.color(0, 0, 0, 0.6));
            darkBackground.setOnMouseClicked(e -> tutupOverlayAktif());

            Pane inventoryLogicPane = new Pane();
            inventoryLogicPane.setPrefSize(1000, 600);
            inventoryLogicPane.setTranslateX((SCREEN_WIDTH - 1000) / 2.0);
            inventoryLogicPane.setTranslateY((SCREEN_HEIGHT - 600) / 2.0);
            inventoryLogicPane.setOnMouseClicked(e -> e.consume());

            Texture bg = FXGL.texture(INVENTORY_BG_PATH);
            bg.setFitWidth(1000);
            bg.setFitHeight(600);
            bg.setPreserveRatio(false);
            inventoryLogicPane.getChildren().add(bg);

            refreshInventoryUI(inventoryLogicPane);

            Pane rootOverlay = new Pane(darkBackground, inventoryLogicPane);
            currentOverlay = rootOverlay;

            FXGL.getGameScene().addUINode(currentOverlay);

        } catch (Exception e) {
            System.out.println("Gagal memuat sistem Inventory!");
            e.printStackTrace();
        }
    }

    private void refreshInventoryUI(Pane parentPane) {
        parentPane.getChildren().removeIf(node -> "itemContainer".equals(node.getId()) ||
                "charContainer".equals(node.getId()) ||
                "uiText".equals(node.getId()));

        String armorFolder = (equippedArmor != null) ? equippedArmor.folderName : "base";
        String weaponFolder = (equippedWeapon != null) ? equippedWeapon.folderName : "pisau_neutral";
        String charPanelPath = "ui/character_idle_panel/" + armorFolder + "/" + weaponFolder + "/idle_profile.png";

        try {
            StackPane charContainer = new StackPane();
            charContainer.setId("charContainer");
            charContainer.setPrefSize(INV_CHAR_W, INV_CHAR_H);
            charContainer.setTranslateX(INV_CHAR_X);
            charContainer.setTranslateY(INV_CHAR_Y);

            Texture charPanel = FXGL.texture(charPanelPath);
            charPanel.setFitWidth(INV_CHAR_W);
            charPanel.setFitHeight(INV_CHAR_H);
            charPanel.setPreserveRatio(false);

            charContainer.getChildren().add(charPanel);
            parentPane.getChildren().add(charContainer);

        } catch (Exception e) {
            System.out.println("Gagal memuat character idle panel: " + charPanelPath);
            e.printStackTrace();
        }

        Rectangle coverText = new Rectangle(INV_TEXT_COVER_X, INV_TEXT_COVER_Y, INV_TEXT_COVER_W, INV_TEXT_COVER_H);
        coverText.setId("uiText");
        coverText.setFill(Color.web("#e8e8e8"));

        Text nameLabel = new Text("NAMA: " + playerName.toUpperCase());
        nameLabel.setId("uiText");
        nameLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        nameLabel.setFill(Color.DARKRED);
        nameLabel.setTranslateX(580);
        nameLabel.setTranslateY(170);

        int maxHpVal = (gamePlayer != null) ? gamePlayer.getMaxHp() : 100;
        Text hpLabel = new Text("MAX HP : " + maxHpVal);
        hpLabel.setId("uiText");
        hpLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        hpLabel.setFill(Color.web("#303030"));
        hpLabel.setTranslateX(580);
        hpLabel.setTranslateY(235);

        Rectangle hpBar = new Rectangle(580, 245, 200, 8);
        hpBar.setId("uiText");
        hpBar.setFill(Color.LIMEGREEN);

        int levelVal = (gamePlayer != null) ? gamePlayer.getLevel() : 2;
        Text semLabel = new Text("SEMESTER : " + levelVal);
        semLabel.setId("uiText");
        semLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
        semLabel.setFill(Color.web("#303030"));
        semLabel.setTranslateX(580);
        semLabel.setTranslateY(305);

        Rectangle semBar = new Rectangle(580, 315, 200, 8);
        semBar.setId("uiText");
        semBar.setFill(Color.LIMEGREEN);

        parentPane.getChildren().addAll(coverText, nameLabel, hpLabel, hpBar, semLabel, semBar);

        renderEquipmentSlot(parentPane, equippedWeapon, INV_EQUIP_X, INV_WEAPON_Y, "weapon");
        renderEquipmentSlot(parentPane, equippedArmor, INV_EQUIP_X, INV_ARMOR_Y, "armor");

        for (int i = 0; i < 8; i++) {
            final int slotIndex = i;
            GameItem item = inventory[i];

            StackPane slotContainer = new StackPane();
            slotContainer.setId("itemContainer");
            slotContainer.setPrefSize(INV_SLOT_SIZE, INV_SLOT_SIZE);
            slotContainer.setTranslateX(INV_SLOT_START_X + (i * INV_SLOT_SPACING_X));
            slotContainer.setTranslateY(INV_SLOT_START_Y);

            try {
                Texture emptySlot = FXGL.texture(INVENTORY_EMPTY_SLOT_PATH);
                emptySlot.setFitWidth(INV_SLOT_SIZE);
                emptySlot.setFitHeight(INV_SLOT_SIZE);
                emptySlot.setPreserveRatio(false);
                slotContainer.getChildren().add(emptySlot);

                if (item != null) {
                    Texture icon = FXGL.texture(item.path);
                    icon.setPreserveRatio(true);
                    icon.setFitWidth(44);
                    icon.setFitHeight(44);

                    StackPane.setAlignment(icon, Pos.CENTER);
                    slotContainer.getChildren().add(icon);

                    slotContainer.setStyle("-fx-cursor: hand;");
                    slotContainer.setOnMouseClicked(e -> showInventoryActionMenu(
                            slotContainer,
                            item,
                            slotIndex,
                            e.getScreenX(),
                            e.getScreenY(),
                            parentPane));
                }

                parentPane.getChildren().add(slotContainer);

            } catch (Exception e) {
                System.out.println("Gagal memuat slot inventory ke-" + (i + 1));
                e.printStackTrace();
            }
        }
    }

    private void renderEquipmentSlot(Pane parentPane, GameItem item, double x, double y, String slotType) {
        try {
            StackPane equipContainer = new StackPane();
            equipContainer.setId("itemContainer");
            equipContainer.setPrefSize(INV_EQUIP_SIZE, INV_EQUIP_SIZE);
            equipContainer.setTranslateX(x);
            equipContainer.setTranslateY(y);

            Pane equippedBg = createEquippedSlotBackground(INV_EQUIP_SIZE);
            equipContainer.getChildren().add(equippedBg);

            if (item != null) {
                Texture icon = FXGL.texture(item.path);
                icon.setPreserveRatio(true);
                icon.setFitWidth(44);
                icon.setFitHeight(44);
                StackPane.setAlignment(icon, Pos.CENTER);
                equipContainer.getChildren().add(icon);

                if (slotType.equals("armor")) {
                    equipContainer.setStyle("-fx-cursor: hand;");
                    equipContainer.setOnMouseClicked(e -> showEquippedActionMenu(
                            equipContainer,
                            item,
                            slotType,
                            e.getScreenX(),
                            e.getScreenY(),
                            parentPane));
                }
            }

            parentPane.getChildren().add(equipContainer);

        } catch (Exception e) {
            System.out.println("Gagal memuat equipment slot: " + slotType);
            e.printStackTrace();
        }
    }

    private Pane createEquippedSlotBackground(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);

        double darkSize = size - 8;
        double redThickness = 8;

        Rectangle darkMain = new Rectangle(darkSize, darkSize);
        darkMain.setFill(Color.web("#595959"));
        darkMain.setTranslateX(0);
        darkMain.setTranslateY(0);

        Rectangle redRight = new Rectangle(redThickness, darkSize);
        redRight.setFill(Color.web("#ff1f1f"));
        redRight.setTranslateX(darkSize);
        redRight.setTranslateY(4);

        Rectangle redBottom = new Rectangle(darkSize + 4, redThickness);
        redBottom.setFill(Color.web("#ff1f1f"));
        redBottom.setTranslateX(4);
        redBottom.setTranslateY(darkSize);

        pane.getChildren().addAll(darkMain, redRight, redBottom);
        return pane;
    }

    private void showInventoryActionMenu(Node anchor, GameItem item, int slotIndex, double screenX, double screenY,
            Pane parentPane) {
        ContextMenu menu = new ContextMenu();
        String menuText = "Equip Equipment";

        if (item.type.equals("weapon") && equippedWeapon != null) {
            menuText = "Switch Equipment";
        } else if (item.type.equals("armor") && equippedArmor != null) {
            menuText = "Switch Equipment";
        }

        MenuItem actionItem = new MenuItem(menuText);
        actionItem.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold;");
        actionItem.setOnAction(e -> {
            equipItem(slotIndex);
            refreshInventoryUI(parentPane);
        });

        menu.getItems().add(actionItem);
        menu.show(anchor, screenX, screenY);
    }

    private void showEquippedActionMenu(Node anchor, GameItem item, String slotType, double screenX, double screenY,
            Pane parentPane) {
        ContextMenu menu = new ContextMenu();

        MenuItem actionItem = new MenuItem("Unequip Equipment");
        actionItem.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold;");
        actionItem.setOnAction(e -> {
            unequipItem(slotType);
            refreshInventoryUI(parentPane);
        });

        menu.getItems().add(actionItem);
        menu.show(anchor, screenX, screenY);
    }

    private void equipItem(int invIndex) {
        GameItem itemToEquip = inventory[invIndex];
        if (itemToEquip == null)
            return;

        if (itemToEquip.type.equals("weapon")) {
            GameItem oldWeapon = equippedWeapon;
            equippedWeapon = itemToEquip;
            inventory[invIndex] = oldWeapon;
        } else if (itemToEquip.type.equals("armor")) {
            GameItem oldArmor = equippedArmor;
            equippedArmor = itemToEquip;
            inventory[invIndex] = oldArmor;
        }
        syncToPlayer();
    }

    private void unequipItem(String slotType) {
        int emptySlot = getEmptyInventorySlot();

        if (emptySlot == -1) {
            FXGL.getDialogService().showMessageBox("Tas penuh! Kamu harus swap barang.");
            return;
        }

        if (slotType.equals("weapon")) {
            FXGL.getDialogService().showMessageBox("Weapon tidak bisa dilepas, hanya bisa diganti.");
            return;
        }

        if (slotType.equals("armor") && equippedArmor != null) {
            inventory[emptySlot] = equippedArmor;
            equippedArmor = null;
            syncToPlayer();
        }
    }

    private int getEmptyInventorySlot() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    // =========================================================================
    // SHOP SYSTEM
    // =========================================================================

    private String getRowFramePathByItemName(String name) {
        if (name == null)
            return "ui/inventory/merah.png";
        if (name.equalsIgnoreCase("Buku Air")) {
            return "ui/shop/buku.png";
        } else if (name.equalsIgnoreCase("Keris Api")) {
            return "ui/shop/keris.png";
        } else if (name.equalsIgnoreCase("Almamater") || name.equalsIgnoreCase("Almet")) {
            return "ui/shop/almet.png";
        } else if (name.equalsIgnoreCase("Jas Lab") || name.equalsIgnoreCase("Jas Laboratorium")) {
            return "ui/shop/jaslab.png";
        } else if (name.equalsIgnoreCase("Toga") || name.equalsIgnoreCase("Toga Sihir")) {
            return "ui/shop/toga.png";
        } else if (name.equalsIgnoreCase("Tongkat Tanah")) {
            return "ui/shop/tongkat.png";
        } else if (name.equalsIgnoreCase("Panah Angin")) {
            return "ui/shop/panah.png";
        }
        System.out.println("Warning: Missing shop row frame path for item name: " + name);
        return "ui/inventory/merah.png";
    }

    private String getPreviewFramePathByItemName(String name) {
        if (name == null)
            return "ui/inventory/merah.png";
        if (name.equalsIgnoreCase("Buku Air")) {
            return "ui/inventory/Frame 7.png";
        } else if (name.equalsIgnoreCase("Keris Api")) {
            return "ui/inventory/Frame 5.png";
        } else if (name.equalsIgnoreCase("Almamater") || name.equalsIgnoreCase("Almet")) {
            return "ui/inventory/Frame 13.png";
        } else if (name.equalsIgnoreCase("Jas Lab") || name.equalsIgnoreCase("Jas Laboratorium")) {
            return "ui/inventory/Frame 15.png";
        } else if (name.equalsIgnoreCase("Toga") || name.equalsIgnoreCase("Toga Sihir")) {
            return "ui/inventory/Frame 17.png";
        } else if (name.equalsIgnoreCase("Tongkat Tanah")) {
            return "ui/inventory/Frame 9.png";
        } else if (name.equalsIgnoreCase("Panah Angin")) {
            return "ui/inventory/Frame 11.png";
        }
        System.out.println("Warning: Missing shop preview frame path for item name: " + name);
        return "ui/inventory/merah.png";
    }

    private List<Item> getShopItems() {
        return List.of(
                new BukuAir("Buku Air", Elemen.WATER, 50, 18),
                new KerisApi("Keris Api", Elemen.FIRE, 110, 36),
                new TongkatTanah("Tongkat Tanah", Elemen.EARTH, 160, 50),
                new PanahAngin("Panah Angin", Elemen.WIND, 75, 26),
                new Armor("Almamater", Elemen.NEUTRAL, 45, 6, "Almamater"),
                new Armor("Jas Lab", Elemen.NEUTRAL, 90, 12, "Jas Lab"),
                new Armor("Toga", Elemen.NEUTRAL, 180, 22, "Toga"));
    }

    private void showShopScreen() {
        tutupOverlayAktif();

        try {
            syncFromPlayer();
            selectedShopItem = null;
            selectedShopPrice = 0;
            selectedShopPreviewFramePath = null;
            shopPreviewFrame = null;
            shopCreditLabel = null;

            Rectangle darkBackground = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.color(0, 0, 0, 0.6));
            darkBackground.setOnMouseClicked(e -> tutupOverlayAktif());

            Pane shopLogicPane = new Pane();
            shopLogicPane.setPrefSize(1000, 600);
            shopLogicPane.setTranslateX((SCREEN_WIDTH - 1000) / 2.0);
            shopLogicPane.setTranslateY((SCREEN_HEIGHT - 600) / 2.0);
            shopLogicPane.setOnMouseClicked(e -> e.consume());

            Texture bg = FXGL.texture(SHOP_BG_PATH);
            bg.setFitWidth(1000);
            bg.setFitHeight(600);
            bg.setPreserveRatio(false);
            shopLogicPane.getChildren().add(bg);

            shopCreditLabel = new Text(formatCredit(playerCredit));
            shopCreditLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 15));
            shopCreditLabel.setFill(Color.WHITE);
            shopCreditLabel.setStroke(Color.BLACK);
            shopCreditLabel.setStrokeWidth(0.8);
            shopCreditLabel.setTranslateX(SHOP_CREDIT_X);
            shopCreditLabel.setTranslateY(SHOP_CREDIT_Y);
            shopLogicPane.getChildren().add(shopCreditLabel);

            List<Item> shopItems = getShopItems();

            for (int i = 0; i < shopItems.size(); i++) {
                Item item = shopItems.get(i);
                double currentY = SHOP_ROW_Y + (i * SHOP_ROW_GAP);

                String rowFramePath = getRowFramePathByItemName(item.getNamaItem());
                String previewFramePath = getPreviewFramePathByItemName(item.getNamaItem());

                Texture itemFrame = FXGL.texture(rowFramePath);
                itemFrame.setFitWidth(SHOP_ROW_W);
                itemFrame.setFitHeight(SHOP_ROW_H);
                itemFrame.setPreserveRatio(false);
                itemFrame.setTranslateX(SHOP_ROW_X);
                itemFrame.setTranslateY(currentY);

                Rectangle clickArea = new Rectangle(SHOP_ROW_W, SHOP_ROW_H);
                clickArea.setTranslateX(SHOP_ROW_X);
                clickArea.setTranslateY(currentY);
                clickArea.setFill(Color.color(1, 1, 1, 0.01));
                clickArea.setStyle("-fx-cursor: hand;");
                clickArea.setOnMouseClicked(e -> {
                    pilihItemShop(item, previewFramePath);
                    e.consume();
                });

                shopLogicPane.getChildren().add(itemFrame);
                shopLogicPane.getChildren().add(clickArea);
            }

            shopPreviewFrame = new ImageView();
            shopPreviewFrame.setFitWidth(SHOP_PREVIEW_SIZE);
            shopPreviewFrame.setFitHeight(SHOP_PREVIEW_SIZE);
            shopPreviewFrame.setPreserveRatio(false);
            shopPreviewFrame.setTranslateX(SHOP_PREVIEW_X);
            shopPreviewFrame.setTranslateY(SHOP_PREVIEW_Y);
            setImageViewFromTexture(shopPreviewFrame, INVENTORY_EMPTY_RED_SLOT_PATH);
            shopLogicPane.getChildren().add(shopPreviewFrame);

            Rectangle buyClickArea = new Rectangle(SHOP_BUY_W, SHOP_BUY_H);
            buyClickArea.setTranslateX(SHOP_BUY_X);
            buyClickArea.setTranslateY(SHOP_BUY_Y);
            buyClickArea.setFill(Color.color(1, 1, 1, 0.01));
            buyClickArea.setStyle("-fx-cursor: hand;");
            buyClickArea.setOnMouseClicked(e -> {
                beliItemTerpilih();
                e.consume();
            });

            shopLogicPane.getChildren().add(buyClickArea);

            Pane rootOverlay = new Pane(darkBackground, shopLogicPane);
            currentOverlay = rootOverlay;

            FXGL.getGameScene().addUINode(currentOverlay);

        } catch (Exception e) {
            System.out.println("Gagal memuat sistem Toko!");
            e.printStackTrace();
        }
    }

    private void pilihItemShop(Item item, String previewFramePath) {
        selectedShopItem = item;
        selectedShopPrice = item.getHargaBP();
        selectedShopPreviewFramePath = previewFramePath;

        try {
            if (shopPreviewFrame != null) {
                setImageViewFromTexture(shopPreviewFrame, selectedShopPreviewFramePath);
            }

        } catch (Exception e) {
            System.out.println("Gagal menampilkan preview item: " + selectedShopPreviewFramePath);
            e.printStackTrace();
        }
    }

    private void beliItemTerpilih() {
        if (selectedShopItem == null) {
            FXGL.getDialogService().showMessageBox("Pilih item dulu sebelum membeli!");
            return;
        }

        if (gamePlayer == null)
            return;

        if (gamePlayer.getBattlePoint() < selectedShopItem.getHargaBP()) {
            FXGL.getDialogService().showMessageBox("Battlecoin tidak cukup!");
            return;
        }

        if (isItemDuplicated(selectedShopItem.getNamaItem())) {
            FXGL.getDialogService().showMessageBox("Item ini sudah kamu punya!");
            return;
        }

        if (gamePlayer.getInventory().getItems().size() >= 8) {
            FXGL.getDialogService().showMessageBox("Inventory penuh! Maksimal hanya 8 item.");
            return;
        }

        // Deduct BP and add clone item to inventory
        gamePlayer.kurangiBP(selectedShopItem.getHargaBP());
        gamePlayer.getInventory().addItem(selectedShopItem.cloneItem());

        // Sync back to UI variables
        syncFromPlayer();

        if (shopCreditLabel != null) {
            shopCreditLabel.setText(formatCredit(playerCredit));
        }

        if (shopPreviewFrame != null) {
            setImageViewFromTexture(shopPreviewFrame, INVENTORY_EMPTY_RED_SLOT_PATH);
        }

        String boughtItemName = selectedShopItem.getNamaItem();
        int boughtItemPrice = selectedShopItem.getHargaBP();

        selectedShopItem = null;
        selectedShopPrice = 0;
        selectedShopPreviewFramePath = null;

        FXGL.getDialogService().showMessageBox(
                "Pembelian berhasil!\n" +
                        boughtItemName + " masuk to inventory.\n" +
                        "Battlecoin berkurang " + boughtItemPrice + ".");
    }

    private void setImageViewFromTexture(ImageView imageView, String texturePath) {
        try {
            imageView.setImage(FXGL.texture(texturePath).getImage());
        } catch (Exception e) {
            System.out.println("Gagal memuat texture: " + texturePath);
        }
    }

    private String formatCredit(int credit) {
        return String.format("%,d", credit).replace(",", ".");
    }

    private boolean isItemDuplicated(String itemName) {
        if (itemName == null || gamePlayer == null) {
            return false;
        }

        for (Item item : gamePlayer.getInventory().getItems()) {
            if (item != null && item.getNamaItem().equalsIgnoreCase(itemName)) {
                return true;
            }
        }

        if (gamePlayer.getItemAktif() != null && gamePlayer.getItemAktif().getNamaItem().equalsIgnoreCase(itemName)) {
            return true;
        }

        return gamePlayer.getArmorAktif() != null
                && gamePlayer.getArmorAktif().getNamaItem().equalsIgnoreCase(itemName);
    }

    // =========================================================================
    // BRIDGE FXGL APP -> BATTLESCENE SWING
    // =========================================================================

    private Karakter buatKarakterUntukBattle() {
        Karakter karakter = new Karakter(playerName);
        karakter.setBattlePoint(playerCredit);
        karakter.setMaxDungeonUnlocked(dungeonUnlocked);

        Weapon weaponBattle = buatWeaponDariGameItem(equippedWeapon);
        if (weaponBattle == null) {
            weaponBattle = new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);
        }
        karakter.setItemAktif(weaponBattle);

        Armor armorBattle = buatArmorDariGameItem(equippedArmor);
        if (armorBattle != null) {
            karakter.setArmorAktif(armorBattle);
        }

        return karakter;
    }

    private void sinkronkanStatusKarakterSebelumBattle() {
        if (gamePlayer == null) {
            gamePlayer = buatKarakterUntukBattle();
        }

        gamePlayer.setNama(playerName);
        syncToPlayer();

        if (gamePlayer.getHp() <= 0) {
            gamePlayer.setHp(gamePlayer.getMaxHp());
        }
    }

    private Weapon buatWeaponDariGameItem(GameItem item) {
        if (item == null || item.folderName == null) {
            return null;
        }

        switch (item.folderName) {
            case "pisau_neutral":
                return new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);
            case "keris_api":
                return new KerisApi("Keris Api", Elemen.FIRE, 110, 36);
            case "buku_air":
                return new BukuAir("Buku Air", Elemen.WATER, 50, 18);
            case "panah_angin":
                return new PanahAngin("Panah Angin", Elemen.WIND, 75, 26);
            case "tongkat_tanah":
                return new TongkatTanah("Tongkat Tanah", Elemen.EARTH, 160, 50);
            default:
                return new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);
        }
    }

    private Armor buatArmorDariGameItem(GameItem item) {
        if (item == null || item.folderName == null) {
            return null;
        }

        switch (item.folderName) {
            case "almamater":
                return new Armor("Almamater", Elemen.NEUTRAL, 45, 6, "Almamater");
            case "jas_lab":
                return new Armor("Jas Lab", Elemen.NEUTRAL, 90, 12, "Jas Lab");
            case "toga":
                return new Armor("Toga", Elemen.NEUTRAL, 180, 22, "Toga");
            default:
                return null;
        }
    }

    private GameItem buatGameItemDariWeapon(Weapon w) {
        if (w == null)
            return null;
        if (w instanceof PisauNeutral) {
            return new GameItem("Pisau Neutral", "weapon", "item/weapon/pisau_neutral.png", "pisau_neutral");
        } else if (w instanceof KerisApi) {
            return new GameItem("Keris Api", "weapon", "item/weapon/keris_api.png", "keris_api");
        } else if (w instanceof BukuAir) {
            return new GameItem("Buku Air", "weapon", "item/weapon/buku_air.png", "buku_air");
        } else if (w instanceof PanahAngin) {
            return new GameItem("Panah Angin", "weapon", "item/weapon/panah_angin.png", "panah_angin");
        } else if (w instanceof TongkatTanah) {
            return new GameItem("Tongkat Tanah", "weapon", "item/weapon/tongkat_tanah.png", "tongkat_tanah");
        }
        return new GameItem(w.getNamaItem(), "weapon", "item/weapon/pisau_neutral.png", "pisau_neutral");
    }

    private GameItem buatGameItemDariArmor(Armor a) {
        if (a == null)
            return null;
        String tipe = a.getTipeArmor();
        if (tipe == null)
            tipe = "Almamater";
        if (tipe.equalsIgnoreCase("Almamater") || tipe.equalsIgnoreCase("Almet")) {
            return new GameItem("Almamater", "armor", "item/armor/almamater.png", "almamater");
        } else if (tipe.equalsIgnoreCase("Jas Lab")) {
            return new GameItem("Jas Lab", "armor", "item/armor/jas_lab.png", "jas_lab");
        } else if (tipe.equalsIgnoreCase("Toga")) {
            return new GameItem("Toga", "armor", "item/armor/toga.png", "toga");
        }
        return new GameItem(a.getNamaItem(), "armor", "item/armor/almamater.png", "almamater");
    }

    private void syncFromPlayer() {
        if (gamePlayer == null)
            return;

        playerCredit = gamePlayer.getBattlePoint();
        dungeonUnlocked = Math.min(4, gamePlayer.getMaxDungeonUnlocked());

        Weapon w = gamePlayer.getItemAktif();
        if (w != null) {
            equippedWeapon = buatGameItemDariWeapon(w);
        } else {
            equippedWeapon = null;
        }

        Armor a = gamePlayer.getArmorAktif();
        if (a != null) {
            equippedArmor = buatGameItemDariArmor(a);
        } else {
            equippedArmor = null;
        }

        ArrayList<Item> playerItems = gamePlayer.getInventory().getItems();
        for (int i = 0; i < 8; i++) {
            if (i < playerItems.size()) {
                Item item = playerItems.get(i);
                if (item instanceof Weapon) {
                    inventory[i] = buatGameItemDariWeapon((Weapon) item);
                } else if (item instanceof Armor) {
                    inventory[i] = buatGameItemDariArmor((Armor) item);
                } else {
                    inventory[i] = null;
                }
            } else {
                inventory[i] = null;
            }
        }
    }

    private void syncToPlayer() {
        if (gamePlayer == null)
            return;

        gamePlayer.setBattlePoint(playerCredit);
        gamePlayer.setMaxDungeonUnlocked(dungeonUnlocked);

        Weapon w = buatWeaponDariGameItem(equippedWeapon);
        if (w == null) {
            w = new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);
        }
        gamePlayer.setItemAktif(w);

        Armor a = buatArmorDariGameItem(equippedArmor);
        gamePlayer.setArmorAktif(a);

        gamePlayer.getInventory().getItems().clear();
        for (int i = 0; i < 8; i++) {
            if (inventory[i] != null) {
                if (inventory[i].type.equals("weapon")) {
                    gamePlayer.getInventory().addItem(buatWeaponDariGameItem(inventory[i]));
                } else if (inventory[i].type.equals("armor")) {
                    gamePlayer.getInventory().addItem(buatArmorDariGameItem(inventory[i]));
                }
            }
        }
    }

    // =========================================================================
    // CLEANER
    // =========================================================================

    private void bersihkanSemuaScreen() {
        if (titleScreen != null) {
            titleScreen.removeFromWorld();
            titleScreen = null;
        }

        if (inputNameScreen != null) {
            inputNameScreen.removeFromWorld();
            inputNameScreen = null;
        }

        if (mapScreen != null) {
            mapScreen.removeFromWorld();
            mapScreen = null;
        }

        if (battleScreen != null) {
            battleScreen.removeFromWorld();
            battleScreen = null;
        }

        if (activeBattleScene != null) {
            activeBattleScene.dispose();
            activeBattleScene = null;
        }

        if (btnTitleClick != null) {
            FXGL.getGameScene().removeUINode(btnTitleClick);
            btnTitleClick = null;
        }

        if (inputNamaField != null) {
            FXGL.getGameScene().removeUINode(inputNamaField);
            inputNamaField = null;
        }

        if (btnMulaiInput != null) {
            FXGL.getGameScene().removeUINode(btnMulaiInput);
            btnMulaiInput = null;
        }

        bersihkanMapButtons();
        tutupOverlayAktif();
    }

    private void bersihkanBattleScreen() {
        if (battleScreen != null) {
            battleScreen.removeFromWorld();
            battleScreen = null;
        }
    }

    private void bersihkanMapButtons() {
        for (Button btn : mapButtons) {
            FXGL.getGameScene().removeUINode(btn);
        }
        mapButtons.clear();
    }

    private void tambahMapButton(Button button) {
        mapButtons.add(button);
        FXGL.getGameScene().addUINode(button);
    }

    private void tutupOverlayAktif() {
        if (currentOverlay != null) {
            FXGL.getGameScene().removeUINode(currentOverlay);
            currentOverlay = null;
        }
    }

    private Button createInvisibleButton(double x, double y, double width, double height, Runnable action) {
        Button btn = new Button();
        btn.setTranslateX(x);
        btn.setTranslateY(y);
        btn.setPrefSize(width, height);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    // =========================================================================
    // MUSIC
    // =========================================================================

    private void playTitleMusic() {
        stopMusic(mapMusicPlayer);

        if (titleMusicPlayer == null) {
            titleMusicPlayer = createMusicPlayer(TITLE_MUSIC_PATH, 0.55);
        }

        if (titleMusicPlayer != null) {
            titleMusicPlayer.stop();
            titleMusicPlayer.seek(Duration.ZERO);
            titleMusicPlayer.play();
        }
    }

    private void playMapMusic() {
        stopMusic(titleMusicPlayer);

        if (mapMusicPlayer == null) {
            mapMusicPlayer = createMusicPlayer(MAP_MUSIC_PATH, 0.45);
        }

        if (mapMusicPlayer != null && mapMusicPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mapMusicPlayer.play();
        }
    }

    private MediaPlayer createMusicPlayer(String musicPath, double volume) {
        try {
            URL musicURL = getClass().getResource(musicPath);

            if (musicURL == null) {
                System.out.println("File musik tidak ditemukan: " + musicPath);
                return null;
            }

            Media media = new Media(musicURL.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);

            player.setVolume(volume);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setOnEndOfMedia(() -> {
                player.seek(Duration.ZERO);
                player.play();
            });

            return player;

        } catch (Exception e) {
            System.out.println("Gagal memuat musik: " + musicPath);
            return null;
        }
    }

    private void stopMusic(MediaPlayer player) {
        if (player != null) {
            player.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}