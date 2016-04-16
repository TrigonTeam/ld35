package cz.trigon.ld35;

import cz.dat.gaben.api.interfaces.ITextureManager;
import cz.dat.gaben.util.Color;
import cz.trigon.ld35.screen.AboutScreen;
import cz.trigon.ld35.screen.MenuScreen;

public class Game extends cz.dat.gaben.api.game.Game {

    public static final Color MAT_BLUE_800 = Color.fromArgb(0xFF1565C0);
    public static final Color MAT_GREY_800 = Color.fromArgb(0xFF424242);


    @Override
    public void init() {
        super.init();

        ITextureManager t = this.getApi().getTexture();
        this.getWindow().setTitle("Vibrovachka");
        this.getContent().getFilesFromDirectory("textures").forEach(f -> t.loadTexture(f.substring(9), f));
        this.getApi().getFont().loadFont("basic", "mammagamma", 128);
        t.finishLoading();

        this.addScreen(0, new MenuScreen(this));
        this.addScreen(3, new AboutScreen(this));
        this.openScreen(0);
    }


    @Override
    public void onRenderTick(float ptt) {
        super.onRenderTick(ptt);
    }
}
