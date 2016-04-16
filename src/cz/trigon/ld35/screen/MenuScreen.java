package cz.trigon.ld35.screen;

import cz.dat.gaben.api.game.ScreenBase;
import cz.dat.gaben.api.interfaces.*;
import cz.dat.gaben.util.Anchor;
import cz.dat.gaben.util.Color;
import cz.trigon.ld35.Game;

public class MenuScreen extends ScreenBase {
    private IFontRenderer f;
    private IFont font;
    private ITexture logoTex, cloudsTex;

    private String[] lines = new String[] { "Play game", "Options", "About", "Exit" };
    private Integer[] defScreens = new Integer[] { 1, 2, 3, null };

    private int selectedOption = 0;

    public MenuScreen(Game game) {
        super(game, "Menu");

        this.f = game.getApi().getFontRenderer();
        this.font = game.getApi().getFont().getFont("basic");
        this.logoTex = game.getApi().getTexture().getTexture("logo");
        this.cloudsTex = game.getApi().getTexture().getTexture("clouds");
    }

    @Override
    public void onKeyDown(int key) {
        if(key == IInputManager.Keys.DOWN) {
            this.selectedOption++;

            if(this.selectedOption == this.lines.length)
                this.selectedOption = 0;

            this.game.getApi().getSound().playSound(0);
        }

        if(key == IInputManager.Keys.UP) {
            this.selectedOption--;

            if(this.selectedOption < 0)
                this.selectedOption = this.lines.length - 1;

            this.game.getApi().getSound().playSound(0);
        }

        if(key == IInputManager.Keys.ENTER){
            if(this.defScreens[this.selectedOption] != null) {
                this.game.openScreen(this.defScreens[this.selectedOption]);
            } else {
                if(this.selectedOption == 3) {
                    try {
                        this.game.getApi().getSound().playSound(1);
                        // TODO: sound sleep
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.game.getWindow().shutdown(0);
                    return;
                }
            }

            this.game.getApi().getSound().playSound(1);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void renderTick(float ptt) {
        this.drawClouds();

        this.game.getApi().getRenderer().color(Color.WHITE);

        this.f.setFont(this.font);
        this.f.setSize(48);
        this.f.setAnchor(Anchor.TOP_CENTER);

        int y = 0;
        int i = 0;

        this.game.getApi().getRenderer().drawTexture(this.logoTex, this.game.getWindow().getCentre().x(), 10, Anchor.TOP_CENTER);
        y += this.logoTex.getSize().y() + 30;

        for(String s : this.lines) {
            if(i == this.selectedOption)
                this.game.getApi().getRenderer().color(Game.MAT_BLUE_800);
            else
                this.game.getApi().getRenderer().color(Color.WHITE);

            this.f.drawString(s, this.game.getWindow().getCentre().x(), y);

            i++;
            y += this.f.getStringHeight(s) + 5;
        }
    }

    public void drawClouds() {
        float so = (float) (System.nanoTime() / 10000000000D);
        float som = so * 0.5f;
        float son = so * 0.42f;
        int size = this.game.getWidth();
        IRenderer r = this.game.getApi().getRenderer();

        r.color(Game.MAT_GREY_800);

        r.texture(this.cloudsTex);

        r.texCoord(0 + so, 0 + so);
        r.vertex(0, 0);
        r.texCoord(1 + so, 0 + so);
        r.vertex(size, 0);
        r.texCoord(1 + so, 1 + so);
        r.vertex(size, size);

        r.texCoord(0 + so, 0 + so);
        r.vertex(0, 0);
        r.texCoord(1 + so, 1 + so);
        r.vertex(size, size);
        r.texCoord(so, 1 + so);
        r.vertex(0, size);

        r.texCoord(0 + som, 0 + son);
        r.vertex(0, 0);
        r.texCoord(1 + som, 0 + son);
        r.vertex(size, 0);
        r.texCoord(1 + som, 1 + son);
        r.vertex(size, size);

        r.texCoord(0 + som, 0 + son);
        r.vertex(0, 0);
        r.texCoord(1 + som, 1 + son);
        r.vertex(size, size);
        r.texCoord(som, 1 + son);
        r.vertex(0, size);

        r.flush();
    }
}