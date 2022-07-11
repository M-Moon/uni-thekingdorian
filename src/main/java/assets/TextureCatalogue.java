package assets;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.graphics.Texture;


/**
 * A class that holds information on all graphical elements. Because textures
 * are expensive, all the textures relative to an area a created at the same
 * time and stored in the same accessible class.
 * 
 */
public final class TextureCatalogue 
{

    public static final String GAME_ICON_PATH_STR = "resources/placeholder/armisius_Skeleton_Key_Icon.png";

    // Singleton creational pattern
    private static TextureCatalogue INSTANCE = null;
    private static boolean initialized = false;

    private TextureCatalogue() 
    {
        try {
            // TEMP_BG_01.loadFromFile(Paths.get("resources/placeholder/full-moon_bg.png"));
            TEMP_BG_02.loadFromFile(Paths.get("resources/placeholder/gallerypic.jpeg"));
            TEMP_TILE_03.loadFromFile(Paths.get("resources/placeholder/cethiel_Ground_02.png"));
            TEMP_HERO_01.loadFromFile(Paths.get("resources/placeholder/character-horn-girl-cropped.png"));
            TEMP_TOKEN_CARROT.loadFromFile(Paths.get("resources/placeholder/carrot.png"));
            TEMP_OBST_LAVA.loadFromFile(Paths.get("resources/placeholder/lava_pixel.png"));
            TEMP_OBST_FIRE.loadFromFile(Paths.get("resources/placeholder/pixel_fire.png"));
            TEMP_OBST_POISON.loadFromFile(Paths.get("resources/placeholder/poison.png"));
            TEMP_OBST_SPIKE.loadFromFile(Paths.get("resources/placeholder/spike.png"));

            TEMP_DEMON.loadFromFile(Paths.get("resources/placeholder/demon.jpg"));
            TEMP_Kit.loadFromFile(Paths.get("resources/placeholder/kit.png"));

            SPIKE_WALL.loadFromFile(Paths.get("resources/placeholder/spike_wall.png"));

            BG_GROUND_01.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Gro_Scene.png"));
            BG_SKY_01.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Sky_Scene.png"));
            BG_DUNGEON_01.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Dun_Scene.png"));
            BG_ARENA_01.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/craftpix_arena.png"));
            TILE_DUNGEON.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Dun_Tile.png"));
            TILE_GROUND.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Gro_Tile.png"));
            TILE_SKY.loadFromFile(Paths.get("resources/placeholder/textures/platform_chunks/Sky_Tile.png"));

            NPC_GUIDE.loadFromFile(Paths.get("resources/placeholder/textures/blarumyrran_guide.png"));
            NPC_GUIDE_ZONE.loadFromFile(Paths.get("resources/placeholder/textures/crossroads_zone_1.png"));
            NPC_GUARD.loadFromFile(Paths.get("resources/placeholder/jap_gate.png"));
            NPC_MERCHANT.loadFromFile(Paths.get("resources/placeholder/MerchantWalk.png"));
            NPC_MERCHANT_ZONE.loadFromFile(Paths.get("resources/placeholder/MerchantWalk_Zone.png"));

            UI_BUTTON_LARGE.loadFromFile(Paths.get("resources/UI/LargeButton.png"));
            UI_BUTTON_LARGE_PRESSED.loadFromFile(Paths.get("resources/UI/LargeButtonPress.png"));
            UI_BUTTON_MEDIUM.loadFromFile(Paths.get("resources/UI/MediumButton.png"));
            UI_BUTTON_MEDIUM_PRESSED.loadFromFile(Paths.get("resources/UI/MediumButtonPress.png"));
            UI_BUTTON_SMALL.loadFromFile(Paths.get("resources/UI/SmallButton.png"));
            UI_BUTTON_SMALL_PRESSED.loadFromFile(Paths.get("resources/UI/SmallButtonPress.png"));
            DEATHSCREEN_BACKGROUND.loadFromFile(Paths.get("resources/UI/DeathBackground.png"));
            HOMESCREEN_BACKGROUND.loadFromFile(Paths.get("resources/UI/MenuBackground.png"));
            PANEL_BACKGROUND.loadFromFile(Paths.get("resources/UI/BackGround_Green.png"));
            // PANEL_BACKGROUND_01.loadFromFile(Paths.get("resources/UI/PanelBG.png"));
            UI_INSTRUCTIONS.loadFromFile(Paths.get("resources/UI/instructions.png"));

            // Collectibles
            POTION_TYPE_1.loadFromFile(Paths.get("resources/placeholder/textures/potions/icon9.png"));
            POTION_TYPE_2.loadFromFile(Paths.get("resources/placeholder/textures/potions/icon3.png"));
            COIN_SHEET.loadFromFile(Paths.get("resources/UI/CoinSpriteSheet.png"));
            WEAPON_BOW.loadFromFile(Paths.get("resources/placeholder/bowAndArrow.png"));
            WEAPON_SWORD.loadFromFile(Paths.get("resources/placeholder/sword.png"));
            KEY.loadFromFile(Paths.get("resources/placeholder/armisius_Skeleton_Key_Rotated.png"));
            // GAME_ICON.loadFromFile(Paths.get("resources/placeholder/armisius_Skeleton_Key_Icon.png"));

            HEART_FULL.loadFromFile(Paths.get("resources/UI/HeartFull.png"));
            HEART_HALF.loadFromFile(Paths.get("resources/UI/HeartHalf.png"));
            HEART_EMPTY.loadFromFile(Paths.get("resources/UI/HeartEmpty.png"));

            ProjectileTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Pink_Monster/Rock2.png"));
            ShootingEnemyWalkTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Pink_Monster/walk.png"));
            ShootingEnemyIdleTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Pink_Monster/idle.png"));
            ShootingEnemyThrowTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Pink_Monster/throw.png"));
            MovingEnemyIdleTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Blue_Monster/idle.png"));
            MovingEnemyWalkTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/Blue_Monster/walk.png"));
            RuningEnemyIdleTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/White_Monster/idle.png"));
            RuningEnemyWalkTexture.loadFromFile(Paths.get("resources/placeholder/textures/misc_sprites/White_Monster/run.png"));

            DorianIdleTexture.loadFromFile(Paths.get("resources/placeholder/textures/Dorian/Idle.png"));
            DorianRunTexture.loadFromFile(Paths.get("resources/placeholder/textures/Dorian/Run.png"));
            DorianDeathTexture.loadFromFile(Paths.get("resources/placeholder/textures/Dorian/Death.png"));

        } catch (IOException e) {
            e.printStackTrace();
            try
            {
                // TEMP_BG_01.loadFromFile(Paths.get("Game/resources/placeholder/full-moon_bg.png"));
                TEMP_BG_02.loadFromFile(Paths.get("Game/resources/placeholder/gallerypic.jpeg"));
                TEMP_TILE_03.loadFromFile(Paths.get("Game/resources/placeholder/cethiel_Ground_02.png"));
                TEMP_HERO_01.loadFromFile(Paths.get("Game/resources/placeholder/character-horn-girl-cropped.png"));
                TEMP_TOKEN_CARROT.loadFromFile(Paths.get("Game/resources/placeholder/carrot.png"));
                TEMP_OBST_LAVA.loadFromFile(Paths.get("Game/resources/placeholder/lava_pixel.png"));
                TEMP_OBST_FIRE.loadFromFile(Paths.get("Game/resources/placeholder/pixel_fire.png"));
                TEMP_OBST_POISON.loadFromFile(Paths.get("Game/resources/placeholder/poison.png"));
                TEMP_OBST_SPIKE.loadFromFile(Paths.get("Game/resources/placeholder/spike.png"));

                TEMP_DEMON.loadFromFile(Paths.get("Game/resources/placeholder/demon.jpg"));
                TEMP_Kit.loadFromFile(Paths.get("Game/resources/placeholder/kit.png"));
                SPIKE_WALL.loadFromFile(Paths.get("Game/resources/placeholder/spike_wall.png"));


                // SCENE_BACKGROUNDS.loadFromFile(Paths.get("Game/resources/placeholder/textures/scene_bgs.png"));  //1300x900 times 4
                // SCENE_TILES.loadFromFile(Paths.get("Game/resources/placeholder/textures/scene_tiles_bg.png"));
                BG_GROUND_01.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/Gro_Scene.png"));
                BG_SKY_01.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/Sky_Scene.png"));
                BG_DUNGEON_01.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/Dun_Scene.png"));
                BG_ARENA_01.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/craftpix_arena.png"));
                TILE_DUNGEON.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/Dun_Tile.png"));
                TILE_GROUND.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/cethiel_Ground_01.png"));
                TILE_SKY.loadFromFile(Paths.get("Game/resources/placeholder/textures/platform_chunks/Sky_Tile.png"));

                // Collectibles
                POTION_TYPE_1.loadFromFile(Paths.get("Game/resources/placeholder/textures/potions/icon9.png"));
                POTION_TYPE_2.loadFromFile(Paths.get("Game/resources/placeholder/textures/potions/icon3.png"));
                COIN_SHEET.loadFromFile(Paths.get("Game/resources/UI/CoinSpriteSheet.png"));
                WEAPON_BOW.loadFromFile(Paths.get("Game/resources/placeholder/bowAndArrow.png"));
                WEAPON_SWORD.loadFromFile(Paths.get("Game/resources/placeholder/sword.png"));
                KEY.loadFromFile(Paths.get("Game/resources/placeholder/armisius_Skeleton_Key_Rotated.png"));

                HEART_FULL.loadFromFile(Paths.get("Game/resources/UI/HeartFull.png"));
                HEART_HALF.loadFromFile(Paths.get("Game/resources/UI/HeartHalf.png"));
                HEART_EMPTY.loadFromFile(Paths.get("Game/resources/UI/HeartEmpty.png"));

                NPC_GUIDE.loadFromFile(Paths.get("Game/resources/placeholder/textures/blarumyrran_guide.png"));
                NPC_GUIDE_ZONE.loadFromFile(Paths.get("Game/resources/placeholder/textures/crossroads_zone.png"));
                NPC_MERCHANT.loadFromFile(Paths.get("resources/placeholder/MerchantWalk.png"));
                NPC_GUARD.loadFromFile(Paths.get("resources/placeholder/jap_gate.png"));
                NPC_MERCHANT_ZONE.loadFromFile(Paths.get("resources/placeholder/MerchantWalk_Zone.png"));


                UI_BUTTON_LARGE.loadFromFile(Paths.get("Game/resources/UI/LargeButton.png"));
                UI_BUTTON_LARGE_PRESSED.loadFromFile(Paths.get("Game/resources/UI/LargeButtonPress.png"));
                UI_BUTTON_MEDIUM.loadFromFile(Paths.get("Game/resources/UI/MediumButton.png"));
                UI_BUTTON_MEDIUM_PRESSED.loadFromFile(Paths.get("Game/resources/UI/MediumButtonPress.png"));
                UI_BUTTON_SMALL.loadFromFile(Paths.get("Game/resources/UI/SmallButton.png"));
                UI_BUTTON_SMALL_PRESSED.loadFromFile(Paths.get("Game/resources/UI/SmallButtonPress.png"));
                DEATHSCREEN_BACKGROUND.loadFromFile(Paths.get("Game/resources/UI/DeathBackground.png"));
                HOMESCREEN_BACKGROUND.loadFromFile(Paths.get("Game/resources/UI/MenuBackground.png"));
                PANEL_BACKGROUND.loadFromFile(Paths.get("Game/resources/UI/BackGround_Green.png"));
                // PANEL_BACKGROUND_01.loadFromFile(Paths.get("Game/resources/UI/PanelBG.png"));
                UI_INSTRUCTIONS.loadFromFile(Paths.get("Game/resources/UI/instructions.png"));

                ProjectileTexture.loadFromFile(Paths.get("Game/resources/placeholder/textures/misc_sprites/Pink_Monster/Rock2.png"));
                ShootingEnemyWalkTexture.loadFromFile(Paths.get("Game/resources/placeholder/textures/misc_sprites/Pink_Monster/walk.png"));

            } catch (IOException e2) {
                System.out.println("[ERROR] Could not load texture");
                e2.printStackTrace();
            }
        }
    }

    public static final TextureCatalogue get() {
        if (!initialized) {
            INSTANCE = new TextureCatalogue();
            TextureCatalogue.initialized = true;
        }
        return INSTANCE;
    }

    public static final Texture getTexture(Path pathToFile) {
        Texture texture = new Texture();
        try { texture.loadFromFile(pathToFile); 
        } catch (IOException e) { System.out.println("[ERROR] Could not load textures");}
        return texture;   
    }

    // Data
    public final Texture TEMP_HOMESCREEN = new Texture();
    public final Texture TEMP_BG_01 = new Texture();
    public final Texture TEMP_BG_02 = new Texture();
    public final Texture TEMP_TILE_03 = new Texture();
    public final Texture TEMP_HERO_01 = new Texture();
    public final Texture TEMP_TOKEN_CARROT = new Texture();

    public final Texture TEMP_OBST_LAVA = new Texture();
    public final Texture TEMP_OBST_FIRE = new Texture();
    public final Texture TEMP_OBST_POISON = new Texture();
    public final Texture TEMP_OBST_SPIKE = new Texture();


    public final Texture TEMP_DEMON=new Texture();
    public final Texture TEMP_Kit=new Texture();

    public final Texture SPIKE_WALL = new Texture();

    // Backgrounds
    public final Texture BG_GROUND_01 = new Texture();
    public final Texture BG_SKY_01 = new Texture();
    public final Texture BG_DUNGEON_01 = new Texture();
    public final Texture BG_ARENA_01 = new Texture();
    public final Texture TILE_GROUND = new Texture();
    public final Texture TILE_SKY = new Texture();
    public final Texture TILE_DUNGEON = new Texture();

    // Collectibles
    public final Texture POTION_TYPE_1 = new Texture();
    public final Texture POTION_TYPE_2 = new Texture();
    public final Texture COIN_SHEET = new Texture();
    public final Texture WEAPON_BOW = new Texture();
    public final Texture WEAPON_SWORD = new Texture();
    public final Texture KEY = new Texture();
    // public final Texture GAME_ICON = new Texture();


    public final Texture HEART_FULL = new Texture();
    public final Texture HEART_HALF = new Texture();
    public final Texture HEART_EMPTY = new Texture();

    // Characters
    public final Texture NPC_GUIDE = new Texture();
    public final Texture NPC_GUIDE_ZONE = new Texture();
    public final Texture NPC_GUARD = new Texture();
    public final Texture NPC_MERCHANT = new Texture();
    public final Texture NPC_MERCHANT_ZONE = new Texture();

    // UI Buttons
    public final Texture UI_BUTTON_LARGE = new Texture();
    public final Texture UI_BUTTON_MEDIUM = new Texture();
    public final Texture UI_BUTTON_SMALL = new Texture();
    public final Texture UI_BUTTON_LARGE_PRESSED = new Texture();
    public final Texture UI_BUTTON_MEDIUM_PRESSED = new Texture();
    public final Texture UI_BUTTON_SMALL_PRESSED = new Texture();

    //UI MISC

    public final Texture UI_INSTRUCTIONS = new Texture();

    //UI Backgrounds
    public final Texture DEATHSCREEN_BACKGROUND = new Texture();
    public final Texture HOMESCREEN_BACKGROUND = new Texture();
    public final Texture PANEL_BACKGROUND = new Texture();
    public final Texture PANEL_BACKGROUND_01 = new Texture();

    //Enemy

  public final Texture ShootingEnemyWalkTexture = new Texture();
  public final Texture ShootingEnemyThrowTexture = new Texture();
  public final Texture ProjectileTexture = new Texture();
  public final Texture ShootingEnemyIdleTexture = new Texture();
  public final Texture MovingEnemyWalkTexture = new Texture();
  public final Texture MovingEnemyIdleTexture = new Texture();
  public final Texture RuningEnemyIdleTexture = new Texture();
  public final Texture RuningEnemyWalkTexture = new Texture();

  public final Texture DorianIdleTexture = new Texture();
  public final Texture DorianRunTexture = new Texture();
  public final Texture DorianDeathTexture = new Texture();
}
