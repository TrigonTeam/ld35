package cz.trigon.ld35.world;

import cz.dat.gaben.util.AABB;

public class MapAABB extends AABB {
    public MapAABB(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public MapAABB(MapAABB bb) {
        super(bb);
    }

    public float[] moveCollide(World world, float xa, float ya) {
        AABB exp = this.expand(xa, ya);

        int minX = (int) exp.getPosition().x() - 1;
        int maxX = (int) exp.getMaxPosition().x() + 1;

        int minY = (int) exp.getPosition().y() - 1;
        int maxY = (int) exp.getMaxPosition().y() + 1;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                int bl = world.getBlock(x, y);
                if (bl != 0 && BlockType.getBlock(bl).collidable) {
                    xa = new AABB(x, y, 1, 1).clipXCollide(this, xa);
                }
            }
        }

        this.move(xa, 0);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                int bl = world.getBlock(x, y);
                if (bl != 0 && BlockType.getBlock(bl).collidable) {
                    ya = new AABB(x, y, 1, 1).clipYCollide(this, ya);
                }

            }
        }

        this.move(0, ya);
        return new float[]{xa, ya};
    }
}
