package cz.trigon.ld35.world;

import cz.dat.gaben.api.exception.ExceptionUtil;
import cz.trigon.ld35.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class WorldOne extends World {
    public WorldOne(Game game) {
        super(game, 0, 0);

        try {
            BufferedImage img = ImageIO.read(game.getContent().openStream("lvl/l1"));
            this.blocks = World.loadBlocks(img);
            this.width = img.getWidth();
            this.height = img.getHeight();
        } catch (IOException e) {
            throw ExceptionUtil.featureBreakingException(e, this);
        }
    }

    private void initDialogue() {
        Dialogue d1 = new EzDialogue(new Dialogue.Entry[] {
                new Dialogue.Entry(this.player1, "Wow", false),
                new Dialogue.Entry(this.player1, "What has happened? Where am I? Who are you?", false),
                new Dialogue.Entry(this.player2, "Hello.", false),
                new Dialogue.Entry(this.player2, "I'm Lucy.", false) }, () -> true, this);

        this.dialogues.add(d1);
    }

    @Override
    public void start(World prev) {
        this.player1 = new Entity(this, game) {
            @Override
            public void tick() {

            }

            @Override
            public void render(float ptt) {

            }
        };

        this.player2 = this.player1;
        this.initDialogue();
    }

    @Override
    public void end() {

    }
}
