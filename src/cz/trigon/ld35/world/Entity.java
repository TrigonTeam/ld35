package cz.trigon.ld35.world;

import cz.dat.gaben.util.AABB;
import cz.trigon.ld35.Game;

public abstract class Entity {

    protected World world;
    protected Game game;

    protected boolean collideable;
    protected MapAABB bb, lastBB;
    protected AABB renderBB;
    protected String name;
    protected boolean onGround;

    protected float velX, velY;

    public Entity(World world, int width, int height) {
        this.world = world;
        this.game = world.game;
        this.bb = new MapAABB(0, 0, width, height);
        this.lastBB = new MapAABB(bb);
    }

    public boolean isCollideable() {
        return this.collideable;
    }
    
    public MapAABB getAABB() {
        return this.bb;
    }

    public void move() {
        float[] clip = this.bb.moveCollide(this.world, this.velX, this.velY);

        if(this.velY != clip[1]) {
            this.onGround = this.velY < 0;
            this.velY = 0;
        } else {
            this.onGround = false;
        }

        if(this.velX != clip[0]) {
            this.velX = 0;
        }
    }

    public float getX() {
        return this.bb.x1();
    }

    public float getY() {
        return this.bb.y1();
    }

    public String getName() {
        return this.name;
    }

    public abstract void tick();

    public abstract void render(float ptt);
}
