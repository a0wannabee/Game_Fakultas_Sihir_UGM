package org.example.ui;
import org.example.character.Karakter;
import org.example.monsters.Monster;
import org.example.items.*;
import org.example.utils.Elemen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BattleScene extends JFrame {

    private BattlePanel battlePanel;

    private JLabel playerLabel;
    private JLabel monsterLabel;

    private JButton basicButton;
    private JButton skillButton;
    private JButton ultimateButton;

    private ImageHpBar playerHpBar;
    private ImageHpBar enemyHpBar;

    private JLabel playerHpText;
    private JLabel enemyHpText;
    private JLabel enemyNameText;

    private final boolean SHOW_HP_TEXT = true;

    private Karakter player;
    private ArrayList<Monster> enemies;
    private Monster currentEnemy;

    private int dungeonLevel;
    private int enemyIndex = 0;
    private boolean inputLocked = false;

    private boolean battleWon = false;
    private boolean playerLost = false;

    private int initialBP;
    private int initialLevel;
    private int initialExp;
    private int initialMaxHp;
    private int initialMaxDungeonUnlocked;
    private int initialHp;

    private String currentMonsterFolder = "sapi_fapet";
    private String backgroundPath;

    // ==========================
    // PATH ASSET BUTTON
    // ==========================

    private final String BUTTON_BASIC_PATH =
            "/battle/buttons/button_basic.png";

    private final String BUTTON_SKILL_PATH =
            "/battle/buttons/button_skill.png";

    private final String BUTTON_ULTIMATE_PATH =
            "/battle/buttons/button_ulti.png";

    // ==========================
    // PATH HP BAR: FILL + FRAME
    // ==========================

    private final String HP_PLAYER_FILL_PATH =
            "/battle/ui/hp_player_fill.png";

    private final String HP_PLAYER_FRAME_PATH =
            "/battle/ui/hp_player_frame.png";

    private final String HP_KROCO_FILL_PATH =
            "/battle/ui/hp_kroco_fill.png";

    private final String HP_KROCO_FRAME_PATH =
            "/battle/ui/hp_kroco_frame.png";

    private final String HP_BOSS_FILL_PATH =
            "/battle/ui/hp_boss_fill.png";

    private final String HP_BOSS_FRAME_PATH =
            "/battle/ui/hp_boss_frame.png";

    // ==========================
    // POSISI PLAYER
    // JANGAN DIUBAH
    // ==========================

    private final int PLAYER_X = 210;
    private final int PLAYER_Y = 390;
    private final int PLAYER_WIDTH = 330;
    private final int PLAYER_HEIGHT = 330;

    // ==========================
    // POSISI DAN UKURAN MONSTER
    // JANGAN DIUBAH
    // index 0 = Sapi Fapet
    // index 1 = Tupai UGM
    // index 2 = Kucing Vokasi
    // index 3 = Boss
    // ==========================

    private final int[] MONSTER_X_LIST = {
            800,
            820,
            800,
            760
    };

    private final int[] MONSTER_Y_LIST = {
            395,
            410,
            390,
            260
    };

    private final int[] MONSTER_WIDTH_LIST = {
            240,
            220,
            250,
            380
    };

    private final int[] MONSTER_HEIGHT_LIST = {
            240,
            220,
            250,
            430
    };

    // ==========================
    // POSISI BUTTON
    // JANGAN DIUBAH
    // ==========================

    private final int BASIC_X = 1145;
    private final int BASIC_Y = 515;
    private final int BASIC_SIZE = 165;

    private final int SKILL_X = 1050;
    private final int SKILL_Y = 450;
    private final int SKILL_SIZE = 135;

    private final int ULTIMATE_X = 1160;
    private final int ULTIMATE_Y = 400;
    private final int ULTIMATE_SIZE = 135;

    // ==========================
    // POSISI BAR DARAH PLAYER
    // JANGAN DIUBAH
    // ==========================

    private final int PLAYER_HP_X = 280;
    private final int PLAYER_HP_Y = 410;
    private final int PLAYER_HP_WIDTH = 200;
    private final int PLAYER_HP_HEIGHT = 50;

    // ==========================
    // POSISI BAR DARAH KROCO
    // JANGAN DIUBAH
    // ==========================

    private final int KROCO_HP_X = 750;
    private final int KROCO_HP_Y = 350;
    private final int KROCO_HP_WIDTH = 300;
    private final int KROCO_HP_HEIGHT = 55;

    // ==========================
    // POSISI BAR DARAH BOSS
    // JANGAN DIUBAH
    // ==========================

    private final int BOSS_HP_X = 445;
    private final int BOSS_HP_Y = 30;
    private final int BOSS_HP_WIDTH = 480;
    private final int BOSS_HP_HEIGHT = 100;

    // ==========================
    // CONSTRUCTOR UNTUK GAME UTAMA
    // ==========================

    public BattleScene(Karakter player, int dungeonLevel) {
        this.player = player;
        this.dungeonLevel = dungeonLevel;

        this.initialBP = player.getBattlePoint();
        this.initialLevel = player.getLevel();
        this.initialExp = player.getExp();
        this.initialMaxHp = player.getMaxHp();
        this.initialMaxDungeonUnlocked = player.getMaxDungeonUnlocked();
        this.initialHp = player.getHp();

        setupDefaultWeaponIfEmpty();

        this.backgroundPath = getBackgroundPathByDungeon(dungeonLevel);
        this.enemies = createEnemiesByDungeon(dungeonLevel);

        initFrame();
    }

    public BattleScene(Karakter player) {
        this(player, 1);
    }

    public BattleScene() {
        this(new Karakter("Achwan"), 1);
    }

    private void setupDefaultWeaponIfEmpty() {
        if (this.player.getItemAktif() == null) {
            Weapon defaultWeapon = new PisauNeutral("Pisau Neutral", Elemen.NEUTRAL, 0, 10);
            this.player.getInventory().addItem(defaultWeapon);
            this.player.equipWeapon(defaultWeapon);
        }
    }

    // ==========================
    // DATA DUNGEON
    // ==========================

    private String getBackgroundPathByDungeon(int level) {
        if (level == 1) {
            return "/battle/backgrounds/mipa_background.png";
        } else if (level == 2) {
            return "/battle/backgrounds/lembah_background.png";
        } else if (level == 3) {
            return "/battle/backgrounds/hutan_biologi_background.png";
        } else if (level == 4) {
            return "/battle/backgrounds/gedung_pusat_background.png";
        }

        return "/battle/backgrounds/mipa_background.png";
    }

    private ArrayList<Monster> createEnemiesByDungeon(int level) {
        ArrayList<Monster> list = new ArrayList<>();

        if (level == 1) {
            list.add(new Monster("Sapi Fapet", 30, 6, 0, Elemen.EARTH, 10, 20));
            list.add(new Monster("Tupai UGM", 35, 7, 0, Elemen.NEUTRAL, 12, 25));
            list.add(new Monster("Kucing Vokasi", 40, 8, 1, Elemen.WIND, 15, 30));
            list.add(new Monster("Hantu Mbak Yayuk", 100, 12, 2, Elemen.FIRE, 50, 100));
        } else if (level == 2) {
            list.add(new Monster("Sapi Fapet", 50, 10, 1, Elemen.EARTH, 15, 35));
            list.add(new Monster("Tupai UGM", 55, 12, 2, Elemen.NEUTRAL, 18, 40));
            list.add(new Monster("Kucing Vokasi", 60, 14, 2, Elemen.WIND, 22, 45));
            list.add(new Monster("Hantu Muka Rata", 160, 20, 4, Elemen.EARTH, 80, 160));
        } else if (level == 3) {
            list.add(new Monster("Sapi Fapet", 80, 16, 3, Elemen.EARTH, 25, 55));
            list.add(new Monster("Tupai UGM", 85, 18, 3, Elemen.NEUTRAL, 28, 60));
            list.add(new Monster("Kucing Vokasi", 90, 20, 4, Elemen.WIND, 32, 65));
            list.add(new Monster("Kuntilanak Pertanian", 240, 28, 6, Elemen.WIND, 120, 240));
        } else if (level == 4) {
            list.add(new Monster("Sapi Fapet", 120, 24, 5, Elemen.EARTH, 35, 80));
            list.add(new Monster("Tupai UGM", 130, 26, 6, Elemen.NEUTRAL, 40, 90));
            list.add(new Monster("Kucing Vokasi", 140, 28, 7, Elemen.WIND, 45, 100));
            list.add(new Monster("Rektor Gaib", 350, 38, 9, Elemen.WATER, 200, 400));
        } else {
            list.add(new Monster("Sapi Fapet", 30, 6, 0, Elemen.EARTH, 10, 20));
            list.add(new Monster("Tupai UGM", 35, 7, 0, Elemen.NEUTRAL, 12, 25));
            list.add(new Monster("Kucing Vokasi", 40, 8, 1, Elemen.WIND, 15, 30));
            list.add(new Monster("Hantu Mbak Yayuk", 100, 12, 2, Elemen.FIRE, 50, 100));
        }

        return list;
    }

    // ==========================
    // INIT FRAME
    // ==========================

    private void initFrame() {
        setTitle("Battle Scene - Dungeon " + dungeonLevel);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setupKeyBindings();

        battlePanel = new BattlePanel(backgroundPath);
        battlePanel.setLayout(null);
        setContentPane(battlePanel);

        setupHpBars();
        setupCharacters();
        setupButtons();

        startEnemy(0);

        setVisible(true);
    }

    private void setupHpBars() {
        enemyNameText = new JLabel("", SwingConstants.CENTER);
        enemyNameText.setForeground(Color.WHITE);
        enemyNameText.setFont(new Font("Arial", Font.BOLD, 18));
        enemyNameText.setVisible(SHOW_HP_TEXT);
        battlePanel.add(enemyNameText);

        enemyHpBar = new ImageHpBar(
                HP_KROCO_FILL_PATH,
                HP_KROCO_FRAME_PATH,
                30
        );
        battlePanel.add(enemyHpBar);

        enemyHpText = new JLabel("", SwingConstants.CENTER);
        enemyHpText.setForeground(Color.WHITE);
        enemyHpText.setFont(new Font("Arial", Font.BOLD, 13));
        enemyHpText.setVisible(SHOW_HP_TEXT);
        battlePanel.add(enemyHpText);

        playerHpBar = new ImageHpBar(
                HP_PLAYER_FILL_PATH,
                HP_PLAYER_FRAME_PATH,
                player.getMaxHp()
        );
        playerHpBar.setBounds(PLAYER_HP_X, PLAYER_HP_Y, PLAYER_HP_WIDTH, PLAYER_HP_HEIGHT);
        battlePanel.add(playerHpBar);

        playerHpText = new JLabel("", SwingConstants.CENTER);
        playerHpText.setBounds(
                PLAYER_HP_X,
                PLAYER_HP_Y + PLAYER_HP_HEIGHT - 5,
                PLAYER_HP_WIDTH,
                25
        );
        playerHpText.setForeground(Color.WHITE);
        playerHpText.setFont(new Font("Arial", Font.BOLD, 13));
        playerHpText.setVisible(SHOW_HP_TEXT);
        battlePanel.add(playerHpText);
    }

    private void setupCharacters() {
        playerLabel = new JLabel();
        playerLabel.setBounds(PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        setPlayerPose("idle");
        battlePanel.add(playerLabel);

        monsterLabel = new JLabel();
        battlePanel.add(monsterLabel);
    }

    private void setupButtons() {
        ultimateButton = createImageButton(BUTTON_ULTIMATE_PATH, "ULTI", ULTIMATE_SIZE, ULTIMATE_SIZE);
        skillButton = createImageButton(BUTTON_SKILL_PATH, "SKILL", SKILL_SIZE, SKILL_SIZE);
        basicButton = createImageButton(BUTTON_BASIC_PATH, "BASIC", BASIC_SIZE, BASIC_SIZE);

        ultimateButton.setBounds(ULTIMATE_X, ULTIMATE_Y, ULTIMATE_SIZE, ULTIMATE_SIZE);
        skillButton.setBounds(SKILL_X, SKILL_Y, SKILL_SIZE, SKILL_SIZE);
        basicButton.setBounds(BASIC_X, BASIC_Y, BASIC_SIZE, BASIC_SIZE);

        basicButton.addActionListener(e -> basicAttackAction());
        skillButton.addActionListener(e -> normalSkillAction());
        ultimateButton.addActionListener(e -> ultimateSkillAction());

        battlePanel.add(ultimateButton);
        battlePanel.add(skillButton);
        battlePanel.add(basicButton);

        updateButtonState();
    }

    private JButton createImageButton(String path, String fallbackText, int width, int height) {
        JButton button = new JButton();

        ImageIcon icon = loadScaledIcon(path, width, height);

        if (icon != null) {
            button.setIcon(icon);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
        } else {
            button.setText(fallbackText);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(45, 45, 45));
            button.setOpaque(true);
            button.setContentAreaFilled(true);
        }

        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // ==========================
    // PLAYER TURN
    // ==========================

    private void basicAttackAction() {
        if (inputLocked || isBattleOver()) return;

        lockInput();

        setPlayerPose("attack");
        setMonsterPose(getMonsterHitPath());

        player.getItemAktif().basicAttack(player, currentEnemy);

        afterPlayerAttack();
    }

    private void normalSkillAction() {
        if (inputLocked || isBattleOver()) return;

        Weapon weapon = player.getItemAktif();

        if (weapon.getCurrentCdNormal() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Skill masih cooldown " + weapon.getCurrentCdNormal() + " turn lagi.",
                    "Cooldown",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        lockInput();

        setPlayerPose("cast");
        setMonsterPose(getMonsterHitPath());

        weapon.normalSkill(player, currentEnemy);

        afterPlayerAttack();
    }

    private void ultimateSkillAction() {
        if (inputLocked || isBattleOver()) return;

        Weapon weapon = player.getItemAktif();

        if (weapon.getCurrentCdUltimate() > 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ultimate masih cooldown " + weapon.getCurrentCdUltimate() + " turn lagi.",
                    "Cooldown",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        lockInput();

        setPlayerPose("cast");
        setMonsterPose(getMonsterHitPath());

        weapon.ultimateSkill(player, currentEnemy);

        afterPlayerAttack();
    }

    private void afterPlayerAttack() {
        updateHpBarsAndTexts();
        updateButtonState();

        if (currentEnemy.getHp() <= 0) {
            enemyDefeated();
            return;
        }

        Timer timer = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();
            monsterTurn();
        });

        timer.setRepeats(false);
        timer.start();
    }

    // ==========================
    // MONSTER TURN
    // ==========================

    private void monsterTurn() {
        if (isBattleOver()) return;

        setPlayerPose("hit");
        setMonsterPoseWithFallback(getMonsterAttackPath(), getMonsterIdlePath());

        int beforeHp = player.getHp();

        currentEnemy.attack(player);

        int damageTaken = beforeHp - player.getHp();
        if (damageTaken < 0) damageTaken = 0;

        System.out.println(currentEnemy.getNama() + " menyerang balik. Damage diterima: " + damageTaken);

        player.getItemAktif().reduceCooldown();

        updateHpBarsAndTexts();
        updateButtonState();

        if (player.getHp() <= 0) {
            playerDefeated();
            return;
        }

        Timer timer = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();

            setPlayerPose("idle");

            if (currentEnemy.getHp() > 0) {
                setMonsterPose(getMonsterIdlePath());
            }

            unlockInput();
        });

        timer.setRepeats(false);
        timer.start();
    }

    // ==========================
    // WIN / LOSE
    // ==========================

    private void enemyDefeated() {
        disableButtons();

        int oldLevel = player.getLevel();

        player.tambahBP(currentEnemy.getDropBP());
        player.tambahExp(currentEnemy.getDropExp());

        int newLevel = player.getLevel();

        updateHpBarsAndTexts();

        Timer timer = new Timer(700, e -> {
            ((Timer) e.getSource()).stop();

            String rewardMessage =
                    currentEnemy.getNama() + " berhasil dikalahkan!\n"
                            + "Reward: +" + currentEnemy.getDropBP() + " BP, +"
                            + currentEnemy.getDropExp() + " EXP";

            if (newLevel > oldLevel) {
                rewardMessage += "\n\nLEVEL UP!"
                        + "\nKamu naik ke Semester " + newLevel
                        + ".\nMax HP sekarang: " + player.getMaxHp()
                        + ".\nHP kamu telah dipulihkan penuh.";
            }

            if (enemyIndex == enemies.size() - 1) {
                battleWon = true;
                if (player.getMaxDungeonUnlocked() == dungeonLevel) {
                    player.setMaxDungeonUnlocked(dungeonLevel + 1);
                }

                JOptionPane.showMessageDialog(
                        this,
                        rewardMessage + "\n\nBoss berhasil dikalahkan!\nDungeon selesai.",
                        "Dungeon Selesai",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        rewardMessage + "\n\nLanjut ke musuh berikutnya.",
                        "Musuh Kalah",
                        JOptionPane.INFORMATION_MESSAGE
                );

                startEnemy(enemyIndex + 1);
                unlockInput();
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private void playerDefeated() {
        disableButtons();

        setPlayerPose("defeat");

        JOptionPane.showMessageDialog(
                this,
                "Kamu kalah!\nHP dipulihkan penuh dan kembali ke lobi utama.",
                "Kalah",
                JOptionPane.WARNING_MESSAGE
        );

        player.setHp(player.getMaxHp());
        updateHpBarsAndTexts();

        playerLost = true;
        dispose();
    }

    // ==========================
    // START ENEMY
    // ==========================

    private void startEnemy(int index) {
        enemyIndex = index;
        currentEnemy = enemies.get(enemyIndex);

        currentEnemy.setHp(currentEnemy.getMaxHp());
        currentMonsterFolder = getMonsterFolderByIndex(enemyIndex);

        updateMonsterBounds();
        refreshEnemyHpBarSkin();

        setPlayerPose("idle");
        setMonsterPose(getMonsterIdlePath());
        updateHpBarsAndTexts();

        if (enemyIndex == enemies.size() - 1) {
            enemyNameText.setText("BOSS: " + currentEnemy.getNama());
        } else {
            enemyNameText.setText("KROCO " + (enemyIndex + 1) + ": " + currentEnemy.getNama());
        }

        updateButtonState();
    }

    private void updateMonsterBounds() {
        int monsterX = MONSTER_X_LIST[enemyIndex];
        int monsterY = MONSTER_Y_LIST[enemyIndex];
        int monsterWidth = MONSTER_WIDTH_LIST[enemyIndex];
        int monsterHeight = MONSTER_HEIGHT_LIST[enemyIndex];

        monsterLabel.setBounds(monsterX, monsterY, monsterWidth, monsterHeight);
    }

    private void refreshEnemyHpBarSkin() {
        if (enemyHpBar != null) {
            battlePanel.remove(enemyHpBar);
        }

        if (enemyIndex == enemies.size() - 1) {
            enemyHpBar = new ImageHpBar(
                    HP_BOSS_FILL_PATH,
                    HP_BOSS_FRAME_PATH,
                    currentEnemy.getMaxHp()
            );

            enemyHpBar.setBounds(BOSS_HP_X, BOSS_HP_Y, BOSS_HP_WIDTH, BOSS_HP_HEIGHT);

            enemyNameText.setBounds(BOSS_HP_X, BOSS_HP_Y - 25, BOSS_HP_WIDTH, 25);
            enemyHpText.setBounds(BOSS_HP_X, BOSS_HP_Y + BOSS_HP_HEIGHT - 5, BOSS_HP_WIDTH, 25);
        } else {
            enemyHpBar = new ImageHpBar(
                    HP_KROCO_FILL_PATH,
                    HP_KROCO_FRAME_PATH,
                    currentEnemy.getMaxHp()
            );

            enemyHpBar.setBounds(KROCO_HP_X, KROCO_HP_Y, KROCO_HP_WIDTH, KROCO_HP_HEIGHT);

            enemyNameText.setBounds(KROCO_HP_X, KROCO_HP_Y - 25, KROCO_HP_WIDTH, 25);
            enemyHpText.setBounds(KROCO_HP_X, KROCO_HP_Y + KROCO_HP_HEIGHT - 5, KROCO_HP_WIDTH, 25);
        }

        battlePanel.add(enemyHpBar);
        battlePanel.repaint();
        battlePanel.revalidate();
    }

    // ==========================
    // PATH MONSTER
    // ==========================

    private String getMonsterFolderByIndex(int index) {
        if (index == 0) {
            return "sapi_fapet";
        } else if (index == 1) {
            return "tupai_ugm";
        } else if (index == 2) {
            return "kucing_vokasi";
        }

        return getBossFolderByDungeon(dungeonLevel);
    }

    private String getBossFolderByDungeon(int level) {
        if (level == 1) {
            return "mbak_yayuk";
        } else if (level == 2) {
            return "hantu_muka_rata";
        } else if (level == 3) {
            return "kuntilanak_pertanian";
        } else if (level == 4) {
            return "rektor_gaib";
        }

        return "mbak_yayuk";
    }

    private String getMonsterIdlePath() {
        return "/battle/monster/" + currentMonsterFolder + "/idle.png";
    }

    private String getMonsterHitPath() {
        return "/battle/monster/" + currentMonsterFolder + "/hit.png";
    }

    private String getMonsterAttackPath() {
        return "/battle/monster/" + currentMonsterFolder + "/attack.png";
    }

    // ==========================
    // PATH PLAYER OTOMATIS ARMOR + WEAPON
    // ==========================

    private String getArmorFolder() {
        Armor armor = player.getArmorAktif();

        if (armor == null) {
            return "base";
        }

        String tipeArmor = armor.getTipeArmor();

        if (tipeArmor == null) {
            return "base";
        }

        if (tipeArmor.equalsIgnoreCase("Almet") || tipeArmor.equalsIgnoreCase("Almamater")) {
            return "almamater";
        } else if (tipeArmor.equalsIgnoreCase("Jas Lab")) {
            return "jas_lab";
        } else if (tipeArmor.equalsIgnoreCase("Toga")) {
            return "toga";
        }

        return "base";
    }

    private String getWeaponFolder() {
        Weapon weapon = player.getItemAktif();

        if (weapon instanceof KerisApi) {
            return "keris_api";
        } else if (weapon instanceof BukuAir) {
            return "buku_air";
        } else if (weapon instanceof PanahAngin) {
            return "panah_angin";
        } else if (weapon instanceof TongkatTanah) {
            return "tongkat_tanah";
        }

        return "pisau_neutral";
    }

    private String getPlayerPosePath(String pose) {
        return "/battle/player/"
                + getArmorFolder() + "/"
                + getWeaponFolder() + "/"
                + pose + ".png";
    }

    // ==========================
    // UPDATE UI
    // ==========================

    private void updateHpBarsAndTexts() {
        playerHpBar.setHp(player.getHp(), player.getMaxHp());
        enemyHpBar.setHp(currentEnemy.getHp(), currentEnemy.getMaxHp());

        playerHpText.setText("PLAYER HP: " + player.getHp() + " / " + player.getMaxHp());
        enemyHpText.setText("ENEMY HP: " + currentEnemy.getHp() + " / " + currentEnemy.getMaxHp());
    }

    private void updateButtonState() {
        if (basicButton == null || skillButton == null || ultimateButton == null) return;
        if (player.getItemAktif() == null) return;

        Weapon weapon = player.getItemAktif();

        basicButton.setToolTipText("Basic Attack: " + weapon.getBasicAttackName());

        if (weapon.getCurrentCdNormal() > 0) {
            skillButton.setToolTipText("Skill cooldown: " + weapon.getCurrentCdNormal() + " turn lagi");
        } else {
            skillButton.setToolTipText("Skill: " + weapon.getNormalSkillName());
        }

        if (weapon.getCurrentCdUltimate() > 0) {
            ultimateButton.setToolTipText("Ultimate cooldown: " + weapon.getCurrentCdUltimate() + " turn lagi");
        } else {
            ultimateButton.setToolTipText("Ultimate: " + weapon.getUltimateSkillName());
        }

        if (!inputLocked) {
            basicButton.setEnabled(true);
            skillButton.setEnabled(weapon.getCurrentCdNormal() == 0);
            ultimateButton.setEnabled(weapon.getCurrentCdUltimate() == 0);
        }
    }

    private void lockInput() {
        inputLocked = true;

        basicButton.setEnabled(false);
        skillButton.setEnabled(false);
        ultimateButton.setEnabled(false);
    }

    private void unlockInput() {
        inputLocked = false;
        updateButtonState();
    }

    private void disableButtons() {
        inputLocked = true;

        basicButton.setEnabled(false);
        skillButton.setEnabled(false);
        ultimateButton.setEnabled(false);
    }

    private boolean isBattleOver() {
        return player.getHp() <= 0 || currentEnemy.getHp() <= 0;
    }

    // ==========================
    // SET POSE
    // ==========================

    private void setPlayerPose(String pose) {
        String path = getPlayerPosePath(pose);
        ImageIcon icon = loadScaledIcon(path, PLAYER_WIDTH, PLAYER_HEIGHT);

        if (icon == null && pose.equalsIgnoreCase("hit")) {
            icon = loadScaledIcon(getPlayerPosePath("idle"), PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        if (icon == null && pose.equalsIgnoreCase("defeat")) {
            icon = loadScaledIcon(getPlayerPosePath("idle"), PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        if (icon == null) {
            System.out.println("Player pose tidak ditemukan: " + path);
            return;
        }

        playerLabel.setIcon(null);
        playerLabel.setText("");
        playerLabel.setOpaque(false);
        playerLabel.setIcon(icon);
    }

    private void setMonsterPose(String path) {
        int monsterWidth = MONSTER_WIDTH_LIST[enemyIndex];
        int monsterHeight = MONSTER_HEIGHT_LIST[enemyIndex];

        ImageIcon icon = loadScaledIcon(path, monsterWidth, monsterHeight);

        if (icon == null) {
            System.out.println("Monster pose tidak ditemukan: " + path);
            return;
        }

        monsterLabel.setIcon(null);
        monsterLabel.setText("");
        monsterLabel.setOpaque(false);
        monsterLabel.setIcon(icon);
    }

    private void setMonsterPoseWithFallback(String primaryPath, String fallbackPath) {
        int monsterWidth = MONSTER_WIDTH_LIST[enemyIndex];
        int monsterHeight = MONSTER_HEIGHT_LIST[enemyIndex];

        ImageIcon icon = loadScaledIcon(primaryPath, monsterWidth, monsterHeight);

        if (icon == null) {
            icon = loadScaledIcon(fallbackPath, monsterWidth, monsterHeight);
        }

        if (icon == null) {
            System.out.println("Monster pose tidak ditemukan: " + primaryPath);
            return;
        }

        monsterLabel.setIcon(null);
        monsterLabel.setText("");
        monsterLabel.setOpaque(false);
        monsterLabel.setIcon(icon);
    }

    // ==========================
    // IMAGE LOADER
    // ==========================

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        java.net.URL url = getClass().getResource(path);

        if (url == null) {
            System.out.println("Asset tidak ditemukan: " + path);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);

        if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            System.out.println("Asset gagal dibaca: " + path);
            return null;
        }

        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private Image loadImage(String path) {
        java.net.URL url = getClass().getResource(path);

        if (url == null) {
            System.out.println("Asset tidak ditemukan: " + path);
            return null;
        }

        return new ImageIcon(url).getImage();
    }

    // ==========================
    // HP BAR FILL + FRAME
    // VERSI SIMPEL SEBELUMNYA
    // ==========================

    private class ImageHpBar extends JComponent {
        private Image fillImage;
        private Image frameImage;

        private int currentHp;
        private int maxHp;

        public ImageHpBar(String fillPath, String framePath, int maxHp) {
            this.fillImage = loadImage(fillPath);
            this.frameImage = loadImage(framePath);
            this.maxHp = maxHp;
            this.currentHp = maxHp;
            setOpaque(false);
        }

        public void setHp(int currentHp, int maxHp) {
            this.currentHp = Math.max(0, currentHp);
            this.maxHp = Math.max(1, maxHp);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            double ratio = (double) currentHp / maxHp;
            if (ratio < 0) ratio = 0;
            if (ratio > 1) ratio = 1;

            int fillWidth = (int) (getWidth() * ratio);

            if (fillImage != null && fillWidth > 0) {
                Shape oldClip = g2.getClip();

                g2.setClip(0, 0, fillWidth, getHeight());

                g2.drawImage(
                        fillImage,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );

                g2.setClip(oldClip);
            }

            if (frameImage != null) {
                g2.drawImage(
                        frameImage,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }

            g2.dispose();
        }
    }

    // ==========================
    // BACKGROUND PANEL
    // ==========================

    private class BattlePanel extends JPanel {
        private Image background;
        private String backgroundPath;

        public BattlePanel(String backgroundPath) {
            this.backgroundPath = backgroundPath;

            java.net.URL url = getClass().getResource(backgroundPath);

            if (url != null) {
                background = new ImageIcon(url).getImage();
            } else {
                background = null;
                System.out.println("Background tidak ditemukan: " + backgroundPath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            } else {
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(35, 35, 55),
                        0, getHeight(), new Color(80, 45, 35)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 34));
                g2.drawString("BACKGROUND TIDAK DITEMUKAN", 430, 330);

                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                g2.drawString(backgroundPath, 430, 365);
            }
        }
    }

    private void setupKeyBindings() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "ESCAPE"
        );
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void dispose() {
        if (!battleWon) {
            if (playerLost) {
                player.setBattlePoint(initialBP);
                player.setLevel(initialLevel);
                player.setExp(initialExp);
                player.setMaxHp(initialMaxHp);
                player.setMaxDungeonUnlocked(initialMaxDungeonUnlocked);
                player.setHp(player.getMaxHp());
            } else {
                player.setBattlePoint(initialBP);
                player.setLevel(initialLevel);
                player.setExp(initialExp);
                player.setMaxHp(initialMaxHp);
                player.setMaxDungeonUnlocked(initialMaxDungeonUnlocked);
                player.setHp(initialHp);
            }
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BattleScene::new);
    }
}