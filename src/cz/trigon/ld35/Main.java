package cz.trigon.ld35;

import cz.dat.gaben.api.ContentManager;
import cz.dat.gaben.api.game.GameWindowBase;
import cz.dat.gaben.api.game.GameWindowFactory;
import cz.dat.gaben.util.GabeLogger;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        GameWindowFactory.enableSplashscreen(false, null);
        GabeLogger.init(Main.class);

        Game g = new Game();
        ContentManager manager = new ContentManager("./res", false);

        GameWindowBase w = GameWindowFactory.createPreferredGame(1280, 720, manager, g);
        w.start(true);
    }
}
