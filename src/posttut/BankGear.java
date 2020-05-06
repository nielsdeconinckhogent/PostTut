package posttut;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class BankGear extends Task {

    String style;

    public BankGear(ClientContext ctx, String style) {
        super(ctx);
        this.style = style;
    }

    @Override
    public boolean activate() {
        return !checkStyleItems();
    }

    private boolean checkStyleItems() {
        switch (style) {
            case "Melee":
                if ((ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == Items.BRONZE_SWORD.getId()) && (ctx.equipment.itemAt(Equipment.Slot.OFF_HAND).id() == Items.WOODEN_SHIELD.getId())) {
                    return true;
                }
                break;
            case "Ranged":
                System.out.println(style);
                if ((ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == Items.SHORTBOW.getId()) ) {
                    return true;
                }
        }
        return false;
    }

    @Override
    public void execute() {
        ((PostTut) ctx.controller.script()).config().setStatus("BANKING");
        if (ctx.bank.opened()) {
            switch (style) {
                case "Melee":
                    if (!(ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).id() == Items.BRONZE_SWORD.getId())
                            && !(ctx.equipment.itemAt(Equipment.Slot.OFF_HAND).id() == Items.WOODEN_SHIELD.getId()))
                    {
                        ctx.bank.withdraw(Items.BRONZE_SWORD.getId(),1);
                        ctx.bank.withdraw(Items.WOODEN_SHIELD.getId(),1);
                        ctx.bank.withdraw(Items.COOKED_CHICKEN.getId(),2);
                        ctx.bank.withdraw(Items.COOKED_MEAT.getId(),2);

                        ctx.inventory.select().id(Items.BRONZE_SWORD.getId()).poll().interact("Wield");
                        ctx.inventory.select().id((Items.WOODEN_SHIELD).getId()).poll().interact("Wield");
                    }
                    break;
                case "Ranged":
                    if (!(ctx.equipment.itemAt(Equipment.Slot.HANDS).id() == Items.SHORTBOW.getId())) {
                        ctx.bank.withdraw(Items.SHORTBOW.getId(),1);
                        ctx.bank.withdraw(Items.BRONZE_ARROW.getId(), org.powerbot.script.rt4.Bank.Amount.ALL);
                        ctx.bank.withdraw(Items.COOKED_CHICKEN.getId(),2);
                        ctx.bank.withdraw(Items.COOKED_MEAT.getId(),2);


                        ctx.inventory.select().id(Items.SHORTBOW.getId()).poll().interact("Wield");
                        ctx.inventory.select().id((Items.BRONZE_ARROW).getId()).poll().interact("Wield");
                    }
                    break;
            }
            ctx.bank.close();
        }
        else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            }
            else {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}
