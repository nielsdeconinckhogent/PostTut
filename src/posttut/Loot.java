package posttut;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import posttut.Task;
import posttut.util.ItemID;

import java.util.ArrayList;
import java.util.concurrent.Callable;


public class Loot extends Task {
    ItemID itemsid = new ItemID();

    Tile monsterTile = Tile.NIL;
    String monsterChoice;
    ArrayList<Tile> monsterLootTile = new ArrayList<Tile>();

    public Loot(ClientContext ctx, String monsterChoice) {
        super(ctx);
        this.monsterChoice = monsterChoice;
    }

    @Override
    public boolean activate() {
        if(ctx.players.local().interacting().valid() &&
                !ctx.players.local().interacting().tile().equals(monsterTile) &&
                !ctx.players.local().interacting().inMotion() &&
                ctx.players.local().speed()==0
        ){
            monsterTile = ctx.players.local().interacting().tile();

            switch (monsterChoice) {
                case "Cows":
                    Tile newTile = new Tile(monsterTile.x() - 1, monsterTile.y() - 1, monsterTile.floor());
                    monsterLootTile.add(newTile);
                    break;
                case "Goblins":
                case "Chickens":
                    monsterLootTile.add(monsterTile);
                    break;
            }

        }

        boolean lootExists = false;
        for(Tile t : monsterLootTile) {
            for (Items i : Items.values()) {
                if (!ctx.groundItems.select().at(t).id(i.getId()).isEmpty()) {
                    lootExists = true;
                }

            }
        }

        return monsterLootTile!=null && lootExists &&
                !ctx.players.local().interacting().valid() &&
                !(ctx.inventory.select().count() >27);

    }
    @Override
    public void execute() {
        ctx.inventory.select().id(Items.BRONZE_ARROW.getId()).poll().interact("Wield");
        ArrayList<Tile> toRemove = new ArrayList<Tile>();

        for(Tile t : monsterLootTile){
            for (Items i : Items.values()) {
                if (!ctx.groundItems.select().at(t).id(i.getId()).isEmpty()) {
                    GroundItem groundItem = ctx.groundItems.select().at(t).id(i.getId()).poll();
                    groundItem.interact("Take", groundItem.name());

                    Callable<Boolean> booleanCallable = new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !groundItem.valid();
                        }
                    };
                    Condition.wait(booleanCallable, 300, 10);
                    toRemove.add(t);
                }
            }
        }
        monsterLootTile.removeAll(toRemove);
    }
}