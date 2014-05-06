package us.scriptwith.scripts.rangeguild.jobs;

import us.scriptwith.core.job.Job;
import us.scriptwith.scripts.rangeguild.RangeGuild;

/**
 * Date: 2/20/14
 * Time: 1:36 AM
 */

public class EquipArrows extends Job<RangeGuild> {
    public EquipArrows(RangeGuild script) {
        super(script);
    }

    @Override
    public String status() {
        return "Equipping Arrows";
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean activate() {
        return !ctx.equipment.contains(script.bronzeArrow) && ctx.backpack.contains(script.bronzeArrow);
    }

    @Override
    public void execute() {
        if (ctx.backpack.getFirst(script.bronzeArrow).interact("Wield")) {
            sleep(350, 500, 100);
        }
    }
}