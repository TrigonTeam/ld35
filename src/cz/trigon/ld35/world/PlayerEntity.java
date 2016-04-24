package cz.trigon.ld35.world;

import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.util.Color;

public class PlayerEntity extends Entity {

    protected Color color;
    protected boolean shH, shV;

    protected float shiftingEnergyDecayFactor = 0.05f;
    protected float speed = 0.1f;
    protected float shiftingEnergy = 1f;
    protected boolean movEnabled = false;

    public PlayerEntity(World world, String name, Color color) {
        super(world, 1, 1);

        this.name = name;
        this.color = color;
    }

    public void setShiftingDirection(boolean horizontally, boolean vertically) {
        this.shH = horizontally;
        this.shV = vertically;
    }

    public void setMovementLock(boolean locked) {
        this.movEnabled = !locked;
    }

    @Override
    public void tick() {
        this.lastBB = new MapAABB(this.bb);

        if (this.movEnabled) {
            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.LEFT)) {
                this.velX -= this.speed;
            }

            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.RIGHT)) {
                this.velX += this.speed;
            }

            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.SPACE) && this.onGround) {
                this.velY += this.speed * 8;
            }
        }

        if (this.shV && this.shiftingEnergy > 0.1) {
            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.W)) {
                this.bb = new MapAABB(this.bb.x1(), this.bb.y1(), this.bb.width(), this.bb.height() + 0.1f);
                this.shiftingEnergy -= this.shiftingEnergyDecayFactor;
            }

            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.S) && this.bb.height() > 0.1f) {
                this.bb = new MapAABB(this.bb.x1(), this.bb.y1(), this.bb.width(), this.bb.height() - 0.1f);
                this.shiftingEnergy -= this.shiftingEnergyDecayFactor;
            }
        }

        if (this.shH && this.shiftingEnergy > 0.1) {
            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.A) && this.bb.width() > 0.1f) {
                this.bb = new MapAABB(this.bb.x1(), this.bb.y1(), this.bb.width() - 0.1f, this.bb.height());
                this.shiftingEnergy -= this.shiftingEnergyDecayFactor;
            }

            if (this.game.getApi().getInput().isKeyDown(IInputManager.Keys.D)) {
                this.bb = new MapAABB(this.bb.x1(), this.bb.y1(), this.bb.width() + 0.1f, this.bb.height());
                this.shiftingEnergy -= this.shiftingEnergyDecayFactor;
            }
        }

        this.velY -= 0.1f;
        this.velX *= 0.65f;

        if (this.shiftingEnergy < 1) {
            for (int i : this.getBlocksUnder()) {
                if (i == BlockType.CHARGE.blockNumber) {
                    this.shiftingEnergy += 0.005f;
                    this.shiftingEnergy = Math.min(this.shiftingEnergy, 1f);
                    break;
                }
            }
        }

        this.move();
    }

    private int[] getBlocksUnder() {
        int[] ret = new int[4];
        ret[0] = this.world.getBlock((int) this.bb.x1(), (int) this.bb.y1());
        ret[1] = this.world.getBlock((int) this.bb.x1(), (int) this.bb.y2());
        ret[2] = this.world.getBlock((int) this.bb.x2(), (int) this.bb.y2());
        ret[3] = this.world.getBlock((int) this.bb.x2(), (int) this.bb.y1());
        return ret;
    }

    @Override
    public void render(float ptt) {
        this.renderBB = this.lastBB.mix(this.bb, ptt);
        this.game.getApi().getRenderer().color(this.color);
        this.game.getApi().getRenderer().drawRect(this.renderBB.x1(),
                this.renderBB.y1(), this.renderBB.x2(),
                this.renderBB.y2());
    }
}
