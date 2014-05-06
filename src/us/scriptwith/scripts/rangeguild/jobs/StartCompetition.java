package us.scriptwith.scripts.rangeguild.jobs;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Npc;
import us.scriptwith.core.job.Job;
import us.scriptwith.scripts.rangeguild.RangeGuild;

import java.awt.Point;
import java.util.concurrent.Callable;

/**
 * Date: 2/20/14
 * Time: 1:17 AM
 */

public class StartCompetition extends Job<RangeGuild> {
    public StartCompetition(RangeGuild script) {
        super(script);
    }

    @Override
    public String status() {
        return "Starting Competition";
    }

    @Override
    public boolean activate() {
        return ctx.settings.get(3150) == 0 && !ctx.npcs.select().id(693).isEmpty();
    }

    @Override
    public void execute() {
        final Npc judge = ctx.npcs.nearest().poll();
        final Component chat = ctx.widgets.get(1184, 2);
        if (!judge.isValid()) {
            return;
        } else if (!judge.isInViewport() || chat.getBoundingRect().contains(judge.getCenterPoint())) {
            ctx.camera.turnTo(judge);
        }
        if (!ctx.chat.select().text("Yes.").isEmpty()) {
            ctx.chat.poll().select(true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.settings.get(3150) == 1;
                }
            }, 250, 5);
            script.closePoint = new Point(-1, -1);
        } else {
            judge.interact("Compete");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.chat.isChatting();
                }
            }, 250, 10);
        }
    }
}