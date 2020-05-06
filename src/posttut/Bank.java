package posttut;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class Bank extends Task {

    public Bank(ClientContext ctx, String style) {
        super(ctx);
    }


    @Override
    public boolean activate() {
            return ctx.inventory.select().count() > 27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 6;
    }

    @Override
    public void execute() {
        ((PostTut) ctx.controller.script()).config().setStatus("BANKING");
        if (ctx.bank.opened()) {
            ctx.bank.depositInventory();
            ctx.bank.withdraw(Items.COOKED_CHICKEN.getId(),2);
            ctx.bank.withdraw(Items.COOKED_MEAT.getId(),2);
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

