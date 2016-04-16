package cz.trigon.ld35.world;

import cz.trigon.ld35.Game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class World {

    protected int[][] blocks;
    protected List<Entity> entities, entitiesAdd, entitiesRemove;
    protected List<Dialogue> dialogues;
    protected Dialogue currentDialogue;
    protected Entity player1;
    protected Entity player2;
    protected Game game;

    public World(Game game) {
        this.game = game;
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

    public void tick() {
        this.entities.forEach(Entity::tick);
        this.entitiesAdd.stream().filter(e -> !this.entities.contains(e)).forEach(e -> this.entities.add(e));
        this.entitiesRemove.stream().filter(e -> this.entities.contains(e)).forEach(e -> this.entities.remove(e));

        this.dialogues.stream().filter(Dialogue::shouldOccur).forEach(d -> this.currentDialogue = d);
        if(this.currentDialogue != null && !this.currentDialogue.hasStarted())
            this.currentDialogue.start();
    }

    public void render(float ptt) {
        this.entities.forEach(e -> e.render(ptt));
        if(this.currentDialogue != null)
            this.currentDialogue.render();
    }
}

enum BlockType {
    SOLID(0), WATER(1);

    public int blockNumber;
    BlockType(int num) {
        this.blockNumber = num;
    }

    public static BlockType getBlock(int type) {
        for(BlockType b : BlockType.values())
            if(b.blockNumber == type)
                return b;

        return null;
    }
}