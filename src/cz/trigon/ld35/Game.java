package cz.trigon.ld35;

import cz.dat.gaben.api.data.IDataProvider;
import cz.dat.gaben.api.data.TextDataProvider;
import cz.dat.gaben.api.exception.DataSyncException;
import cz.dat.gaben.api.exception.ExceptionUtil;
import cz.dat.gaben.api.interfaces.ITextureManager;
import cz.dat.gaben.util.Color;
import cz.trigon.ld35.screen.AboutScreen;
import cz.trigon.ld35.screen.GameScreen;
import cz.trigon.ld35.screen.MenuScreen;

import java.io.FileNotFoundException;

public class Game extends cz.dat.gaben.api.game.Game {

    public static final Color MAT_BLUE_800 = Color.fromArgb(0xFF1565C0);
    public static final Color MAT_GREY_800 = Color.fromArgb(0xFF424242);
    public static final Color MAT_LGREEN_500 = Color.fromArgb(0xFF8BC34A);
    public static final Color MAT_LBLUE_500 = Color.fromArgb(0xFF03A9F4);
    public static final Color MAT_RED_900 = Color.fromArgb(0xFFB71C1C);
    public static final Color MAT_GREEN_900 = Color.fromArgb(0xFF1B5E20);
    public static final Color MAT_BLGREY_600 = Color.fromArgb(0xFF546E7A);

    private TextDataProvider data;

    public TextDataProvider getData() {
        return this.data;
    }

    @Override
    public void init() {
        super.init();

        this.getWindow().setFpsLimit(120, true);

        ITextureManager t = this.getApi().getTexture();
        this.getWindow().setTitle("Vibrovatchka");
        this.getContent().getFilesFromDirectory("textures").forEach(f -> t.loadTexture(f.substring(9), f));
        this.getApi().getFont().loadFont("basic", "mammagamma", 128);
        this.getApi().getFont().loadFont("dialogue", "Slabo27px-Regular", 64);

        t.finishLoading();

        try {
            this.data = new TextDataProvider(this.getContent().getPath("config"));
            this.data.setData("blocksize", 32, Integer.class);
            this.data.sync(IDataProvider.MergeType.PREFER_LOCAL);
        } catch (Exception e) {
            throw ExceptionUtil.gameBreakingException(e, this);
        }

        this.addScreen(0, new MenuScreen(this));
        this.addScreen(1, new GameScreen(this));
        this.addScreen(3, new AboutScreen(this));
        this.openScreen(0);
    }

    @Override
    public void onRenderTick(float ptt) {
        super.onRenderTick(ptt);
    }
}
