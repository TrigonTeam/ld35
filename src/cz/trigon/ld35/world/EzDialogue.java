package cz.trigon.ld35.world;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

public class EzDialogue extends Dialogue {

    protected BooleanSupplier sup;

    public EzDialogue(Entry[] entries, BooleanSupplier occur, World world) {
        super(world.player1, world.player2, world.game);

        this.entries.addAll(Arrays.asList(entries));
        this.sup = occur;
    }

    @Override
    public boolean shouldOccur() {
        return this.sup.getAsBoolean();
    }
}
