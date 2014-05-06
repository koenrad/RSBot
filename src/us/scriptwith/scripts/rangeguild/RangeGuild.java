package us.scriptwith.scripts.rangeguild;

import org.powerbot.script.Manifest;
import org.powerbot.script.methods.Skills;
import us.scriptwith.core.script.Script;
import us.scriptwith.scripts.rangeguild.jobs.CloseWidget;
import us.scriptwith.scripts.rangeguild.jobs.EquipArrows;
import us.scriptwith.scripts.rangeguild.jobs.ShootTarget;
import us.scriptwith.scripts.rangeguild.jobs.StartCompetition;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Date: 20/02/2014
 * Time: 1:14 AM
 */

@Manifest(name = "Auto Range Guild", topic = 1157244, version = 1.0,
        description = "Trains Range at the Archery Competition in the Range Guild.")
public class RangeGuild extends Script {
    public int bronzeArrow = 882;
    public int ticket = 1464;
    public int target = 1308;

    public Point closePoint = new Point(-1, -1);

    private int startTickets = 0;

    @Override
    public void start() {
        startTickets = ctx.backpack.select().id(ticket).count(true);

        container.submit(
                new StartCompetition(this),
                new ShootTarget(this),
                new CloseWidget(this),
                new EquipArrows(this)
        );
    }

    @Override
    public void repaint(Graphics graphics) {
        if (!ctx.game.isLoggedIn()) return;

        final long runtime = getRuntime();
        final int tickets = ctx.backpack.select().id(ticket).count(true) - startTickets;
        final int xp = skillTracker.getXpChanges()[Skills.RANGE];
        final int level = ctx.skills.getRealLevel(Skills.RANGE);

        paint.properties(
                "Time: " + paint.formatTime(runtime),
                "Status: " + status,
                "Tickets: " + paint.format(tickets) + " (" + paint.format((int) paint.getHourlyRate(tickets, runtime)) + ")",
                "Range: " + level + " (+" + skillTracker.getLevelChanges()[Skills.RANGE] + ")",
                "XP Gained:",
                String.format("%5s%s", "", paint.format(xp) + " (" + paint.format((int) paint.getHourlyRate(xp, runtime)) + ")"),
                "To " + (level + 1) + ": ",
                String.format("%5s%s", "", paint.format(skillTracker.getExperienceToLevel(Skills.RANGE, level + 1))
                        + " (" + skillTracker.getTimeToLevel(Skills.RANGE, level + 1, (int) paint.getHourlyRate(xp, runtime)) + ")"),
                "To 99:",
                String.format("%5s%s", "", paint.format(skillTracker.getExperienceToMax(Skills.RANGE))
                        + " (" + skillTracker.getTimeToMax(Skills.RANGE, (int) paint.getHourlyRate(xp, runtime)) + ")")
        ).draw(graphics, ctx);
    }
}
