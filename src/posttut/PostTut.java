package posttut;


import org.powerbot.Con;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Combat;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Script.Manifest(name = "PostTut v1.0", description = "Gets you some levels in the combat style you prefer to get you started.", properties = "client=4; author=LoudRex;")

public class PostTut extends PollingScript<ClientContext> implements PaintListener {

    String monsterChoice = "";
    String styleChoice = "";

    int startExpAtt, startExpStr, startExpDef, currentExp, expGain, expPh;
    long runTime;
    String expPhstring = "";

    //Config var
    private ScriptConfig config;

    //List for tasks
    ArrayList<Task> tasks = new ArrayList<Task>();


    // Assign config for script status.
    public PostTut() {
        config = new ScriptConfig();
    }

    @Override
    public void start() {
            startExpAtt = ctx.skills.experience(Constants.SKILLS_ATTACK);
            startExpStr = ctx.skills.experience(Constants.SKILLS_STRENGTH);
            startExpDef = ctx.skills.experience(Constants.SKILLS_DEFENSE);

            System.out.println(ctx.skills.toString());

        if (initGui()) {
            tasks.add(new Bury(ctx));
            tasks.add(new Bank(ctx, styleChoice));
            tasks.add(new Walk(ctx, monsterChoice, styleChoice));
            tasks.add(new Fight(ctx, monsterChoice));
            tasks.add(new Loot(ctx, monsterChoice));
            tasks.add(new BankGear(ctx, styleChoice));
            tasks.add(new Heal(ctx));


        }
        else {ctx.controller.stop();}
    }

    private boolean initGui() {
        //TODO: Add mage in next revision
        String monsterOptions[] = {"Chickens", "Goblins", "Cows"};
        monsterChoice = (String)JOptionPane.showInputDialog(null, "Which monster do you want to kill?", "Post Tutorial Island v1.0", JOptionPane.PLAIN_MESSAGE,null, monsterOptions,monsterOptions[2]);

        String styleOptions[] = {"Melee", "Ranged"};
        styleChoice = (String)JOptionPane.showInputDialog(null, "Which attack style do you want to use?", "Post Tutorial Island v1.0", JOptionPane.PLAIN_MESSAGE,null, styleOptions,styleOptions[0]);

        return !(monsterChoice == null && styleChoice == null);
    }



    @Override
    public void poll() {
        if(!ctx.movement.running()) {
            ctx.movement.running(true);
        }
        for (Task t : tasks) {
            if (t.activate()) {
                t.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {

        graphics.setColor(new Color(0, 0, 0, 180));
        graphics.fillRect(0, 0, 190, 110);

        graphics.setColor(new Color(255, 255, 255));
        graphics.drawRect(0, 0, 190, 110);

        graphics.drawString("PostTut v1.0", 20, 20);

        graphics.drawString("Runtime: " + formatTime(super.getRuntime()),20,60);

        graphics.setColor(Color.yellow);
        graphics.drawString("Choices: " + monsterChoice + " and " + styleChoice,20,80);


        runTime = getRuntime();
        expPh = (int) (3600000d / runTime * (double) (expGain));
        expPhstring = expPh + "";

        if(ctx.combat.style().equals(Combat.Style.ACCURATE)) {
            currentExp = ctx.skills.experience(0);
            expGain = currentExp - startExpAtt;
            graphics.setColor(Color.green);
            graphics.drawString("Attack Exp/hour: " + expPhstring, 20,100);
        }
        else if(ctx.combat.style().equals(Combat.Style.AGGRESSIVE)) {
            currentExp = ctx.skills.experience(1);
            expGain = currentExp - startExpStr;
            graphics.setColor(Color.green);
            graphics.drawString("Strength Exp/hour: " + expPhstring, 20,100);
        }
        else if(ctx.combat.style().equals(Combat.Style.DEFENSIVE)) {
            currentExp = ctx.skills.experience(2);
            expGain = currentExp - startExpDef;
            graphics.setColor(Color.green);
            graphics.drawString("Defence Exp/hour: " + expPhstring, 20,100);
        }



        switch (((PostTut) ctx.controller.script()).config().getStatus()) {
            case "ERROR":
                graphics.setColor(Color.RED);
                graphics.drawString("Status: ERROR", 20, 40);
                break;
            case "WALKING":
                graphics.setColor(Color.green);
                graphics.drawString("Status: WALKING", 20, 40);
                break;
            case "BANKING":
                graphics.setColor(Color.yellow);
                graphics.drawString("Status: BANKING", 20, 40);
                break;
            case "FIGHTING":
                graphics.setColor(Color.MAGENTA);
                graphics.drawString("Status: FIGHTING", 20, 40);
                break;
            case "IDLE":
                graphics.drawString("Status: IDLE", 20, 40);
                break;
        }
    }

    public ScriptConfig config() {
        return config;
    }

    public String formatTime(final long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }
}