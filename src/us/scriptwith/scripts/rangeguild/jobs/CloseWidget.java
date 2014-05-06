package us.scriptwith.scripts.rangeguild.jobs;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.GameObject;
import us.scriptwith.core.job.Job;
import us.scriptwith.scripts.rangeguild.RangeGuild;

import java.util.concurrent.Callable;

/**
 * Date: 2/20/14
 * Time: 2:26 PM
 */

public class CloseWidget extends Job<RangeGuild> {
    public CloseWidget(RangeGuild script) {
        super(script);
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public String status() {
        return "Closing Window";
    }

    @Override
    public boolean activate() {
        final Component close = ctx.widgets.get(325, 0);
        final GameObject target = ctx.objects.select().id(script.target).nearest().poll();
        return close.isValid() && close.isVisible()
                && close.getBoundingRect().contains(target.getCenterPoint());
    }

    @Override
    public void execute() {
        final Component close = ctx.widgets.get(325, 40);
        script.closePoint = close.getNextPoint();
        if (close.getBoundingRect().contains(ctx.mouse.getLocation())) {
            ctx.mouse.click(true);
        } else {
            close.click(true);
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !close.isValid() || !close.isVisible();
            }
        }, 250, 5);
    }
}