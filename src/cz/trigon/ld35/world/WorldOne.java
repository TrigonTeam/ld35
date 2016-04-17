package cz.trigon.ld35.world;

import cz.dat.gaben.api.exception.ExceptionUtil;
import cz.dat.gaben.util.Color;
import cz.dat.gaben.util.Matrix4;
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
        this.dialogues.clear();

        Dialogue d1 = new EzDialogue(new Dialogue.Entry[] {
                new Dialogue.Entry(this.player1, "Wow", false),
                new Dialogue.Entry(this.player1, "What has happened? Where am I? Who are you?", false),
                new Dialogue.Entry(this.player2, "Hello.", false),
                new Dialogue.Entry(this.player2, "I'm Lucy.", false) }, () -> true, this);

        this.dialogues.add(d1);
    }

    @Override
    public void start(World prev) {
        super.start(prev);

        this.player1 = new PlayerEntity(this, "Vibrovatchka", Color.GREEN);
        this.player2 = new PlayerEntity(this, "Sratchka", Color.BLUE);
        this.entities.add(player1);
        this.entities.add(player2);

        this.player1.bb.setPosition(1.5f, 5);
        this.player2.bb.setPosition(3, 5);

        this.initDialogue();
    }
}
