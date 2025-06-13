package org.web.lab3.monitor;

public class MissPercentage implements MissPercentageMBean {

    private final ClickStats stats;

    public MissPercentage(ClickStats stats) { this.stats = stats; }

    @Override
    public double getMissPercentage() {
        long total  = stats.getTotalPoints();
        long misses = stats.getMisses();
        return total == 0 ? 0.0 : (double) misses * 100.0 / total;
    }
}
