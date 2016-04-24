package cz.trigon.ld35.world;

import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.api.interfaces.IRenderer;
import cz.dat.gaben.util.Color;
import cz.dat.gaben.util.Matrix4;
import cz.trigon.ld35.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class World {

    public static int[][] loadBlocks(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        int[][] ret = new int[w][h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = img.getRGB(x, y);

                int blockID = BlockType.getBlockForColor(color).blockNumber;
                ret[x][h-y-1] = blockID;
            }
        }

        return ret;
    }

    protected int[][] blocks;
    protected List<Entity> entities, entitiesAdd, entitiesRemove;
    protected List<Dialogue> dialogues;
    protected Dialogue currentDialogue;
    protected PlayerEntity player1;
    protected PlayerEntity player2;
    protected Game game;
    protected int width, height, bs;
    protected int selectedPlayer = 1;

    protected Matrix4 worldMatrix;

    public World(Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        this.bs = game.getData().getData("blocksize", Integer.class);

        this.dialogues = new ArrayList<>();
        this.entities = new LinkedList<>();
        this.entitiesAdd = new ArrayList<>();
        this.entitiesRemove = new ArrayList<>();
    }

    public void addEntity(Entity e) {
        this.entitiesAdd.add(e);
    }

    public void removeEntity(Entity e) {
        this.entitiesRemove.add(e);
    }

    public int getBlock(int x, int y) {
        if (x < this.width && y < this.height && x >= 0 && y >= 0)
            return this.blocks[x][y];

        return 0;
    }

    public void tick() {
        this.entities.forEach(Entity::tick);
        this.entitiesAdd.stream().filter(e -> !this.entities.contains(e)).forEach(e -> this.entities.add(e));
        this.entitiesRemove.stream().filter(e -> this.entities.contains(e)).forEach(e -> this.entities.remove(e));

        this.dialogues.stream().filter(d -> !d.hasEnded() && d.shouldOccur()).forEach(d -> this.currentDialogue = d);
        if (this.currentDialogue != null && !this.currentDialogue.hasStarted())
            this.currentDialogue.start();
    }

    public void render(float ptt) {
        IRenderer g = this.game.getApi().getRenderer();

        g.setFrontFace(true);
        g.setMatrix(g.identityMatrix());
        if (this.currentDialogue != null)
            this.currentDialogue.render();

        // TODO: camera
        g.setMatrix(this.worldMatrix);

        g.setFrontFace(false);
        g.enableTexture(false);

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.blocks[x][y] != 0) {
                    g.color(BlockType.getBlock(this.blocks[x][y]).blockColor);
                    g.drawRect(x, y, (x + 1), (y + 1));
                }
            }
        }

        this.entities.forEach(e -> e.render(ptt));

        g.color(Color.WHITE);
        PlayerEntity pl = this.selectedPlayer == 1 ? this.player1 : this.player2;
        g.drawTriangle(pl.renderBB.x1() + pl.renderBB.width() / 2, pl.renderBB.y2() + 0.3f,
                pl.renderBB.x1() + pl.renderBB.width() / 2 + 0.2f, pl.renderBB.y2() + 0.5f,
                pl.renderBB.x1() + pl.renderBB.width() / 2 - 0.2f, pl.renderBB.y2() + 0.5f);
    }

    public void setSelectedPlayer(int pl) {
        this.selectedPlayer = pl;
        this.player1.setMovementLock(pl != 1);
        this.player2.setMovementLock(pl != 2);
    }

    public void onKeyDown(int key) {
        if (this.currentDialogue != null)
            this.currentDialogue.onKeyDown(key);

        if(key == IInputManager.Keys.LEFT_CONTROL || key == IInputManager.Keys.RIGHT_CONTROL) {
            this.setSelectedPlayer(this.selectedPlayer == 1 ? 2 : 1);
        }
    }

    public int getBlockSize() {
        return this.bs;
    }

    public void start(World previous) {
        this.entities.clear();
        this.dialogues.clear();
        this.game.getApi().getRenderer().clearColor(Color.DARK_GREY);
        this.worldMatrix = this.game.getApi().getRenderer().identityMatrix()
                .translate(this.game.getWidth() / (this.bs * 2) - this.width / 2,
                        this.game.getHeight() / (this.bs * 2) - this.height / 2)
                .scaleFrom(0, 0, this.bs, this.bs)
                .scaleFrom(this.game.getWidth() / 2, this.game.getHeight() / 2, 1, -1);
    }

    public void end() {
        this.game.getApi().getRenderer().setFrontFace(true);
        this.game.getApi().getRenderer().setMatrix(this.game.getApi().getRenderer().identityMatrix());
        this.game.getApi().getRenderer().clearColor(Color.WHITE);
    }
}

enum BlockType {
    NONE(0, 0xFF000000), SOLID(1, 0xFFFFFFFF, Color.WHITE, true), WATER(2, 0xFF0000FF),
    EXIT(3, 0xFF00FF00, Game.MAT_GREEN_900), CHARGE(4, 0xFFFF0000, Game.MAT_RED_900);

    public int blockNumber;
    public int blockLoadingColor;
    public Color blockColor;
    public boolean collidable;

    BlockType(int num, int color) { this(num, color, Color.fromArgb(color), false); }

    BlockType(int num, int color, Color bColor) {
        this(num, color, bColor, false);
    }

    BlockType(int num, int color, Color bColor, boolean collidable) {
        this.collidable = collidable;
        this.blockNumber = num;
        this.blockLoadingColor = color;
        this.blockColor = bColor;
    }

    public static BlockType getBlock(int type) {
        for (BlockType b : BlockType.values())
            if (b.blockNumber == type)
                return b;

        return BlockType.NONE;
    }

    public static BlockType getBlockForColor(int color) {
        for (BlockType b : BlockType.values())
            if (b.blockLoadingColor == color)
                return b;

        return BlockType.NONE;
    }
}