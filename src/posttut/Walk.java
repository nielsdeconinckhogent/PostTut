package posttut;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import posttut.util.Walker;

public class Walk extends Task {

    Walker walker = new Walker(ctx);
    String monster;
    String style;

    Area goblinArea = new Area(new Tile(3266,3218), new Tile(3239,3255));
    Area cowArea = new Area(new Tile(3240,3297), new Tile(3265,3255));
    Area chickenArea = new Area(new Tile(3236, 3294), new Tile(3225, 3300));

    public static final Tile[] pathToCows = {new Tile(3209, 3220, 2), new Tile(3206, 3217, 2), new Tile(3206, 3213, 2), new Tile(3205, 3209, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3210, 0), new Tile(3214, 3210, 0), new Tile(3215, 3214, 0), new Tile(3215, 3218, 0), new Tile(3219, 3218, 0), new Tile(3223, 3218, 0), new Tile(3227, 3218, 0), new Tile(3231, 3218, 0), new Tile(3233, 3222, 0), new Tile(3236, 3225, 0), new Tile(3240, 3225, 0), new Tile(3244, 3225, 0), new Tile(3248, 3226, 0), new Tile(3252, 3226, 0), new Tile(3256, 3226, 0), new Tile(3259, 3230, 0), new Tile(3259, 3234, 0), new Tile(3259, 3238, 0), new Tile(3259, 3242, 0), new Tile(3259, 3246, 0), new Tile(3256, 3249, 0), new Tile(3253, 3252, 0), new Tile(3252, 3256, 0), new Tile(3251, 3260, 0), new Tile(3250, 3264, 0), new Tile(3254, 3266, 0), new Tile(3258, 3268, 0)};
    public static final Tile[] pathToChickens = {new Tile(3209, 3220, 2), new Tile(3206, 3217, 2), new Tile(3206, 3213, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3209, 0), new Tile(3214, 3210, 0), new Tile(3215, 3214, 0), new Tile(3215, 3218, 0), new Tile(3219, 3218, 0), new Tile(3223, 3218, 0), new Tile(3227, 3218, 0), new Tile(3231, 3218, 0), new Tile(3233, 3222, 0), new Tile(3236, 3225, 0), new Tile(3240, 3226, 0), new Tile(3244, 3226, 0), new Tile(3248, 3226, 0), new Tile(3252, 3226, 0), new Tile(3256, 3226, 0), new Tile(3259, 3230, 0), new Tile(3259, 3234, 0), new Tile(3259, 3238, 0), new Tile(3259, 3242, 0), new Tile(3259, 3246, 0), new Tile(3256, 3249, 0), new Tile(3253, 3252, 0), new Tile(3251, 3256, 0), new Tile(3251, 3260, 0), new Tile(3250, 3264, 0), new Tile(3248, 3268, 0), new Tile(3247, 3272, 0), new Tile(3243, 3274, 0), new Tile(3241, 3278, 0), new Tile(3240, 3282, 0), new Tile(3239, 3286, 0), new Tile(3239, 3290, 0), new Tile(3239, 3294, 0), new Tile(3235, 3296, 0), new Tile(3231, 3297, 0)};
    public static final Tile[] pathToGoblins = {new Tile(3209, 3220, 2), new Tile(3206, 3217, 2), new Tile(3206, 3213, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3210, 0), new Tile(3214, 3210, 0), new Tile(3215, 3214, 0), new Tile(3215, 3218, 0), new Tile(3219, 3218, 0), new Tile(3223, 3218, 0), new Tile(3227, 3218, 0), new Tile(3231, 3218, 0), new Tile(3233, 3222, 0), new Tile(3236, 3225, 0), new Tile(3240, 3226, 0), new Tile(3244, 3226, 0), new Tile(3248, 3226, 0), new Tile(3252, 3226, 0), new Tile(3256, 3226, 0), new Tile(3258, 3230, 0), new Tile(3254, 3230, 0), new Tile(3253, 3234, 0), new Tile(3252,3237),new Tile(3252,3241), new Tile(3252,3244),new Tile(3251,3247) };


    public Walk(ClientContext ctx, String monster, String style) {
        super(ctx);
        this.monster = monster;
        this.style = style;
    }

    @Override
    public boolean activate() {
        //SwitchCase just in case I would add stuff later. Not necesarry in the current revision
        switch (monster) {
            case "Chickens":
            case "Cows":
            case "Goblins":
                if (ctx.inventory.select().count() < 27 && !checkArea()) {
                    if (checkStyleItems()) {
                        return true;
                    }
                }
                if (ctx.inventory.select().count() > 27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) > 6) {
                    return true;
                }
                break;
        }
        return false;
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

    private boolean checkStyleItems() {
        switch (style) {
            case "Melee":
                if (ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == Items.BRONZE_SWORD.getId() && ctx.equipment.itemAt(Equipment.Slot.OFF_HAND).id() == Items.WOODEN_SHIELD.getId()) {
                    return true;
                }
                break;
            case "Ranged":
                if (ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == Items.SHORTBOW.getId()) {
                    return true;
                }
        }
        return false;
    }

    @Override
    public void execute() {
        ((PostTut) ctx.controller.script()).config().setStatus("WALKING");
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if (monster.equals("Goblins")) {
                if (ctx.inventory.select().count() > 27 || ctx.inventory.select().id(Items.COOKED_CHICKEN.getId()).count() < 1) {
                    walker.walkPathReverse(pathToGoblins);
                } else {
                    walker.walkPath(pathToGoblins);
                }
            }
            if (monster.equals("Cows")) {
                if (ctx.inventory.select().count() > 27) {
                    walker.walkPathReverse(pathToCows);
                } else {
                    walker.walkPath(pathToCows);
                }
            }
            if (monster.equals("Chickens")) {
                if (ctx.inventory.select().count() > 27) {
                    walker.walkPathReverse(pathToChickens);
                } else {
                    walker.walkPath(pathToChickens);
                }
            }
        }
    }
}