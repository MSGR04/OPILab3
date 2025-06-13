package org.web.lab3.monitor;

import javax.management.*;
import java.util.concurrent.atomic.AtomicLong;

public class ClickStats extends NotificationBroadcasterSupport implements ClickStatsMBean {
    private final AtomicLong total = new AtomicLong();
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private volatile long seq = 0;
    private volatile long nextMultiple = 10;

    public void register(double x, double y, boolean insideArea) {
        long currentTotal = total.incrementAndGet();

        if (insideArea) {
            hits.incrementAndGet();
        } else {
            misses.incrementAndGet();
        }
        if (currentTotal == nextMultiple) {
            Notification notification = new Notification(
                    "org.web.lab3.multipleOf10",
                    this,
                    seq++,
                    System.currentTimeMillis(),
                    "Total points reached " + nextMultiple
            );

            notification.setUserData(new double[]{x, y, currentTotal});
            sendNotification(notification);
            nextMultiple += 10;

            System.out.println("Notification sent for total: " + currentTotal);
        }
    }

    @Override public long getTotalPoints() { return total.get(); }
    @Override public long getHits() { return hits.get(); }
    @Override public long getMisses() { return misses.get(); }
}