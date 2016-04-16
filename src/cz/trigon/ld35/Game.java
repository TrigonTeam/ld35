package cz.trigon.ld35;

import cz.dat.gaben.util.Vector2;

public class Game extends cz.dat.gaben.api.game.Game {
    @Override
    public void onRenderTick(float ptt) {
        super.onRenderTick(ptt);
        this.getApi().getRenderer().color(0.5f, 1f, 0.5f);
        Vector2 pos = this.getApi().getInput().getMousePosition();
        this.getApi().getRenderer().drawRect(pos.x(), pos.y(), pos.x() + 100, pos.y() + 100);
    }
}
