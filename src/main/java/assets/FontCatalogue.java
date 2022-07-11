package assets;

import java.io.IOException;
import java.nio.file.Paths;

import org.jsfml.graphics.Font;

public final class FontCatalogue
{
    private static FontCatalogue INSTANCE = null;
    private static boolean initialized = false;
    
    public static final FontCatalogue get() {
        if (!initialized) {
            INSTANCE = new FontCatalogue();
            FontCatalogue.initialized = true;
        }
        return INSTANCE;
    }

    public final Font FONT_UI_BAR = new Font();
    public final Font FONT_FREESANS = new Font();

    private FontCatalogue() {
        try {
            FONT_UI_BAR.loadFromFile(Paths.get("resources/UI/Font.ttf"));
            FONT_FREESANS.loadFromFile(Paths.get("resources/fonts/freesans/freesans.ttf"));
        } catch (IOException e) {

            try {
                //System.out.println("We get here, before loading");
                FONT_UI_BAR.loadFromFile(Paths.get("Game/resources/UI/Font.ttf"));
                //System.out.println("I've loaded the first font");
                FONT_FREESANS.loadFromFile(Paths.get("Game/resources/fonts/freesans/freesans.ttf"));
            } catch (IOException e2) {
                System.out.println("[ERROR] Could not load fonts");
                e2.printStackTrace();
            }
        }
    }
}

