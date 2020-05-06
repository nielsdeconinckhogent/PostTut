package posttut;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

public class Bury extends Task {

    public Bury(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.inventory.select().id(Items.BONES.getId()).isEmpty();
    }

    @Override
    public void execute() {
        if (ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.inventory.select().id(Items.BONES.getId()).poll().interact("Bury");
        }
        else {
            ctx.game.tab(Game.Tab.INVENTORY, true);
        }

    }
}



