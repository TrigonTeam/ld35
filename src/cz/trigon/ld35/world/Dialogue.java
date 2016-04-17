package cz.trigon.ld35.world;

import cz.dat.gaben.api.interfaces.IFont;
import cz.dat.gaben.api.interfaces.IFontRenderer;
import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.api.interfaces.IRenderer;
import cz.dat.gaben.util.Anchor;
import cz.dat.gaben.util.Color;
import cz.trigon.ld35.Game;

import java.util.ArrayList;
import java.util.List;

public abstract class Dialogue {

    public static class Entry {
        public Entry(Entity by, String text, boolean isFinal) {
            this.by = by;
            this.text = text;
            this.isFinal = isFinal;
        }

        public Entity by;
        public String text;
        public boolean isFinal;
    }

    protected List<Entry> entries;
    protected boolean ended;
    protected boolean started;
    protected int currentStep;
    protected Game game;
    protected Entity e1, e2;

    protected IFont font;
    protected int startX;
    protected int maxWidth;
    protected int lineOffset, bottomOffset;

    public Dialogue(Entity e1, Entity e2, Game game) {
        this.game = game;
        this.font = game.getApi().getFont().getFont("dialogue");
        this.e1 = e1;
        this.e2 = e2;
        this.entries = new ArrayList<>();

        this.startX = 20;
        this.maxWidth = game.getWidth() - 40;
        this.lineOffset = 5;
        this.bottomOffset = 10;
    }

    public void nextStep() {
        if(this.entries.get(this.currentStep).isFinal) {
            this.ended = true;
            return;
        }

        this.currentStep++;

        if (this.currentStep >= entries.size()) {
            this.ended = true;
            this.currentStep = entries.size();
        }
    }

    public int getCurrentStep() {
        return this.currentStep;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public boolean hasEnded() {
        return this.ended;
    }

    public void start() {
        this.started = true;
    }

    public void render() {
        if (this.started && !this.ended) {
            Entry e = this.entries.get(this.currentStep);
            String line = e.by.getName() + ": " + e.text;
            String[] words = line.split(" ");

            IFontRenderer fr = this.game.getApi().getFontRenderer();
            IRenderer r = this.game.getApi().getRenderer();

            fr.setAnchor(Anchor.TOP_LEFT);
            fr.setFont(this.font);
            fr.setSize(28);

            float w = 0;
            int lines = 1;
            for (String wordN : words) {
                String word = wordN + " ";
                w += fr.getStringWidth(word);
                if (w > this.maxWidth) {
                    lines++;
                    w = 0;
                }
            }

            int y = (int)(this.game.getHeight() - this.bottomOffset -
                    lines * (fr.getMaxHeight() + this.lineOffset));

            r.enableTexture(false);
            r.color(0, 0, 0, 0.5f);
            r.drawRect(0, y - this.bottomOffset - 3, this.game.getWidth(), this.game.getHeight());
            r.color(0, 0, 0, 0.6f);
            r.drawRect(0, y - this.bottomOffset - 3, this.game.getWidth(), y - this.bottomOffset - 1);
            r.color(Color.WHITE);
            r.enableTexture(true);

            w = 0;

            for (String wordN : words) {
                String word = wordN + " ";
                float wordWidth = fr.getStringWidth(word);
                if (w + wordWidth > this.maxWidth) {
                    w = 0;
                    y += fr.getMaxHeight() + this.lineOffset;
                }

                fr.drawString(word, this.startX + w, y);
                w += wordWidth;
            }
        }
    }

    public void onKeyDown(int key) {
        if(this.started && !this.ended && key == IInputManager.Keys.ENTER) {
            this.nextStep();
        }
    }

    public abstract boolean shouldOccur();
}
