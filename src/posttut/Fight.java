package posttut;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;

public class Fight extends Task {
    Area goblinArea = new Area(new Tile(3266,3218), new Tile(3239,3255));
    Area chickenArea = new Area(new Tile(3236, 3294), new Tile(3225, 3300));
    Area cowArea = new Area(new Tile(3240,3297), new Tile(3265,3255));

    String monster;
    int[] goblinIds = {3029, 3031,3032, 3033};
    int[] cowIds = {2791, 2793, 2790};
    int[] chickenIds = {1174}; //TODO: Add rest of chickens

    public Fight(ClientContext ctx, String monster) {
        super(ctx);
        this.monster = monster;
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().healthBarVisible()
                && ctx.players.local().healthPercent() >=30
                && !ctx.players.local().interacting().valid()
                && !(ctx.inventory.select().count() > 27)
                && checkArea();

    }

    private boolean checkArea() {
        switch (monster) {
            case "Chickens":
                if (chickenArea.contains(ctx.players.local().tile())) {
                    return true;
                }
                break;
            case "Goblins":
                if (goblinArea.contains(ctx.players.local().tile())) {
                    return true;
                }
                break;
            case "Cows":
                if (cowArea.contains(ctx.players.local().tile())) {
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public void execute() {
        ((PostTut) ctx.controller.script()).config().setStatus("FIGHTING");
        Filter<Npc> filter = new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                boolean fighting = npc.healthBarVisible();
                return !fighting;
            }
        };

        Npc monsterToKill;

        switch (monster) {
            case "Goblins":
                monsterToKill = ctx.npcs.select().id(goblinIds).nearest().select(filter).poll();
                attack(monsterToKill);
                break;
            case "Cows":
                monsterToKill = ctx.npcs.select().id(cowIds).nearest().select(filter).poll();
                attack(monsterToKill);
                break;
            case "Chickens":
                monsterToKill = ctx.npcs.select().id(chickenIds).nearest().select(filter).poll();
                attack(monsterToKill);
                break;
        }

    }

    private void attack(Npc monsterToKill) {
        if (monsterToKill.inViewport()) {
            if(ctx.movement.reachable(ctx.players.local().tile(), monsterToKill.tile()))
            monsterToKill.interact("Attack");
            Condition.sleep(750);
        }
        else {
            ctx.camera.turnTo(monsterToKill);
        }
    }

}
