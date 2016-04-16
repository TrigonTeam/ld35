package cz.trigon.ld35.world;

import cz.dat.gaben.api.interfaces.IFont;
import cz.dat.gaben.api.interfaces.IFontRenderer;
import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.util.Anchor;
import cz.trigon.ld35.Game;

import java.util.List;

public abstract class Dialogue {

    class Entry {
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

        this.startX = 20;
        this.maxWidth = game.getWidth() - 40;
        this.lineOffset = 5;
        this.bottomOffset = 10;
    }

    public void nextStep() {
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
            IFontRenderer r = this.game.getApi().getFontRenderer();
            r.setAnchor(Anchor.TOP_LEFT);
            r.setFont(this.font);
            r.setSize(28);

            float w = 0;
            int lines = 1;
            for (String wordN : words) {
                String word = wordN + " ";
                w += r.getStringWidth(word);
                if (w > this.maxWidth) {
                    lines++;
                    w = 0;
                }
            }

            int y = (int)(this.game.getHeight() - this.bottomOffset -
                    lines * (r.getMaxHeight() + this.lineOffset));
            w = 0;

            for (String wordN : words) {
                String word = wordN + " ";
                float wordWidth = r.getStringWidth(word);
                if (w + wordWidth > this.maxWidth) {
                    w = 0;
                    y += r.getMaxHeight() + this.lineOffset;
                }

                r.drawString(word, this.startX + w, y);
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
