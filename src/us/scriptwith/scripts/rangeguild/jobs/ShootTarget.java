package us.scriptwith.scripts.rangeguild.jobs;

import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Tile;
import us.scriptwith.core.job.Job;
import us.scriptwith.scripts.rangeguild.RangeGuild;

import java.util.concurrent.Callable;

/**
 * Date: 2/20/14
 * Time: 1:22 AM
 */

public class ShootTarget extends Job<RangeGuild> {
    private Tile shootTile = new Tile(2673, 3420, 0);

    public ShootTarget(RangeGuild script) {
        super(script);
    }

    @Override
    public String status() {
        return "Shooting Target";
    }

    @Override
    public boolean activate() {
        return ctx.settings.get(3150) >= 1 && ctx.settings.get(3150) <= 11
                && ctx.equipment.contains(script.bronzeArrow)
                && !ctx.players.local().isInMotion();
    }

    @Override
    public void execute() {
        if (!ctx.players.local().getLocation().equals(shootTile)) {
            if (!shootTile.getMatrix(ctx).isInViewport()) {
                ctx.movement.stepTowards(shootTile);
            } else {
                final Component c = ctx.widgets.get(1189, 6);
                if (c.getBoundingRect().contains(shootTile.getMatrix(ctx).getCenterPoint())) {
                    ctx.camera.turnTo(shootTile);
                }
                shootTile.getMatrix(ctx).interact("Walk");
            }
            if (!Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().getLocation().equals(shootTile);
                }
            }, 250, 10)) {
                return;
            }
        }

        final GameObject target = ctx.objects.select().id(script.target).nearest().poll();
        if (!target.isInViewport()) {
            ctx.camera.turnTo(target);
            for (int i = ctx.camera.getPitch(); !target.isInViewport(); i -= 15) {
                if (ctx.camera.getPitch() < 10) {
                    break;
                }
                ctx.camera.setPitch(i);
                sleep(75, 150, 100);
            }
            sleep(250, 300, 100);
        } else {
            final int shots = ctx.settings.get(3150);
            if ((ctx.menu.getItems()[0].contains("Fire-at") && Math.random() > .3 && ctx.mouse.click(true)) || target.interact("Fire-at")) {
                if (script.closePoint.y != -1) {
                    ctx.mouse.move(script.closePoint);
                }
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ctx.settings.get(3150) > shots) || ctx.chat.isContinue() || ctx.players.local().isInMotion();
                    }
                }, 250, 10);
            }
        }
    }
}