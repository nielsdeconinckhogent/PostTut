package posttut;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

public class Heal extends Task{
    public Heal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().healthPercent() < 35;
    }

    @Override
    public void execute() {
        if (ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.inventory.select().id(Items.COOKED_CHICKEN.getId()).poll().interact("Eat");
            ctx.inventory.select().id(Items.COOKED_MEAT.getId()).poll().interact("Eat");
        }
        else {
            ctx.game.tab(Game.Tab.INVENTORY, true);
        }
    }
}
