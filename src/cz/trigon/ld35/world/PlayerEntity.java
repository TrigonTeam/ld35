package cz.trigon.ld35.world;

import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.util.Color;

public class PlayerEntity extends Entity {

    protected Color color;

    public PlayerEntity(World world, String name, Color color) {
        super(world, 1, 1);

        this.name = name;
        this.color = color;
    }

    @Override
    public void tick() {
        this.lastBB = new MapAABB(this.bb);
        if(this.game.getApi().getInput().isKeyDown(IInputManager.Keys.LEFT))
            this.velX = 0.2f;

        this.move();
    }

    @Override
    public void render(float ptt) {
        this.renderBB = this.lastBB.mix(this.bb, ptt);
        this.game.getApi().getRenderer().color(this.color);
        this.game.getApi().getRenderer().drawRect(this.renderBB.x1() * this.world.getBlockSize(),
                this.renderBB.y1() * this.world.getBlockSize(), this.renderBB.x2() * this.world.getBlockSize(),
                this.renderBB.y2() * this.world.getBlockSize());
    }
}
