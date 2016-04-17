package cz.trigon.ld35.world;

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
    protected Entity player1;
    protected Entity player2;
    protected Game game;
    protected int width, height, bs;

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
        if(x < this.width && y <this.height && x >= 0 && y >= 0)
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
        g.setMatrix(g.identityMatrix().scaleFrom(
                this.game.getWidth()/2, this.game.getHeight()/2, 1, -1)
                );

    	g.setFrontFace(false);
        g.enableTexture(false);

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.blocks[x][y] != 0) {
                    g.color(Color.fromArgb(BlockType.getBlock(this.blocks[x][y]).blockLoadingColor));
                    g.drawRect(x * this.bs, y * this.bs, (x + 1) * this.bs, (y + 1) * this.bs);
                }
            }
        }

        this.entities.forEach(e -> e.render(ptt));
    }

    public void onKeyDown(int key) {
        if(this.currentDialogue != null)
            this.currentDialogue.onKeyDown(key);
    }

    public int getBlockSize() {
        return this.bs;
    }

    public void start(World previous) {
        this.entities.clear();
        this.dialogues.clear();
        this.game.getApi().getRenderer().clearColor(Color.DARK_GREY);
    }

    public void end() {
        this.game.getApi().getRenderer().setFrontFace(true);
        this.game.getApi().getRenderer().setMatrix(this.game.getApi().getRenderer().identityMatrix());
        this.game.getApi().getRenderer().clearColor(Color.WHITE);
    }
}

enum BlockType {
    NONE(0, 0xFF000000, false), SOLID(1, 0xFFFFFFFF), WATER(2, 0xFF0000FF, false), EXIT(3, 0xFF00FF00, false), CHARGE(4, 0xFFFF0000, false);

    public int blockNumber;
    public int blockLoadingColor;
    public boolean collidable;

    BlockType(int num, int color) {
        this(num, color, true);
    }

    BlockType(int num, int color, boolean collidable) {
        this.collidable = collidable;
        this.blockNumber = num;
        this.blockLoadingColor = color;
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