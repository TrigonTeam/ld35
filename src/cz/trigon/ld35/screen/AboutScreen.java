package cz.trigon.ld35.screen;


import cz.dat.gaben.api.game.ScreenBase;
import cz.dat.gaben.api.interfaces.IFont;
import cz.dat.gaben.api.interfaces.IFontRenderer;
import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.api.interfaces.ITexture;
import cz.dat.gaben.util.Anchor;
import cz.dat.gaben.util.Color;
import cz.dat.gaben.util.Rectangle;
import cz.dat.gaben.util.Vector2;
import cz.trigon.ld35.Game;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class AboutScreen extends ScreenBase {

    private IFontRenderer f;
    private IFont font;
    private ITexture cloudsTex;

    private float startY;
    private Rectangle linkRect;

    private String[] lines = new String[]
            {
                    "Project Vibrovatchka",
                    "Created for Ludum Dare 35 on the 16th of April 2016",
                    "By LeOndryasO and dax105",
                    "Using GabeNgine Framework",
                    "Made in the Czech Republic",
                    "https://github.com/TrigonTeam/ld35"
            };


    public AboutScreen(Game game) {
        super(game, "About");

        this.f = game.getApi().getFontRenderer();
        this.font = game.getApi().getFont().getFont("basic");
        this.cloudsTex = game.getApi().getTexture().getTexture("clouds");

        this.f.setFont(this.font);
        this.f.setSize(42);
        this.f.setAnchor(Anchor.TOP_CENTER);

        this.startY = this.game.getWindow().getCentre().y() - (((this.f.getMaxHeight() + 10) * lines.length) / 2);
        this.linkRect = new Rectangle(this.game.getWindow().getCentre().x() - this.f.getStringWidth(this.lines[this.lines.length - 1]) / 2,
                this.startY + (this.lines.length - 1) * (this.f.getMaxHeight() + 10), this.f.getStringWidth(this.lines[this.lines.length - 1]), this.f.getMaxHeight());
    }

    @Override
    public void onKeyDown(int key) {
        if (key == IInputManager.Keys.ESCAPE) {
            this.game.openScreen(0);
            this.game.getApi().getSound().playSound(1);
        }
    }

    @Override
    public void onMouseButtonDown(int button) {
        Vector2 pos = this.game.getApi().getInput().getMousePosition();

        if (button == IInputManager.Keys.MOUSE_LEFT && this.linkRect.contains(pos))
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/TrigonTeam/ld35").toURI());
            } catch (IOException | URISyntaxException ignored) {
            }
    }

    @Override
    public void tick() {
    }

    @Override
    public void renderTick(float ptt) {
        MenuScreen.drawClouds(this.game, this.cloudsTex);

        this.game.getApi().getRenderer().color(Color.WHITE);

        this.f.setFont(this.font);
        this.f.setSize(42);
        this.f.setAnchor(Anchor.TOP_CENTER);

        for (int i = 0; i < this.lines.length; i++) {
            if (i == this.lines.length - 1)
                this.game.getApi().getRenderer().color(Game.MAT_BLUE_800);

            this.f.drawString(this.lines[i], this.game.getWindow().getCentre().x(),
                    this.startY + i * (this.f.getMaxHeight() + 10));
        }
    }
}