package cz.trigon.ld35.world;

import cz.dat.gaben.util.AABB;
import cz.trigon.ld35.Game;

public abstract class Entity {

    protected World world;
    protected Game game;

    protected boolean collideable;
    protected AABB aabb;
    protected String name;

    public Entity(World world, Game game) {
        this.world = world;
        this.game = game;
    }

    public boolean isCollideable() {
        return this.collideable;
    }
    
    public AABB getAABB() {
        return this.aabb;
    }

    public float getX() {
        return this.aabb.x1();
    }

    public float getY() {
        return this.aabb.y1();
    }

    public String getName() {
        return this.name;
    }

    public abstract void tick();

    public abstract void render(float ptt);
}
