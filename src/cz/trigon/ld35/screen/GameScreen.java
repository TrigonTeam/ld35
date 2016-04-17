package cz.trigon.ld35.screen;

import cz.dat.gaben.api.game.ScreenBase;
import cz.dat.gaben.api.impl.lwjgl.LwjglRenderer;
import cz.dat.gaben.api.interfaces.IInputManager;
import cz.dat.gaben.util.Matrix4;
import cz.trigon.ld35.Game;
import cz.trigon.ld35.world.World;
import cz.trigon.ld35.world.WorldOne;

public class GameScreen extends ScreenBase {

    private World currentWorld;

    public GameScreen(Game game) {
        super(game, "Game");

        this.currentWorld = new WorldOne(game);
    }

    @Override
    public void onOpening() {
        super.onOpening();
        this.currentWorld.start(null);
    }

    @Override
    public void onKeyDown(int key) {
        super.onKeyDown(key);
        this.currentWorld.onKeyDown(key);

        if(key == IInputManager.Keys.ESCAPE) {
            this.currentWorld.end();
            this.game.openScreen(0);
        }
    }

    @Override
    public void tick() {
        this.currentWorld.tick();
    }

    @Override
    public void renderTick(float ptt) {
        this.currentWorld.render(ptt);
    }
}
