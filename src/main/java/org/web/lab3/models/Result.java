package org.web.lab3.models;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Result {
    private final Point point;
    private final boolean isHit;
    private final long timestamp;


    public Result(Point point, boolean isHit) {
        this.point = point;
        this.isHit = isHit;

        this.timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);;
    }

    public Result(Point point, boolean isHit, long timestamp) {
        this.point = point;
        this.isHit = isHit;
        this.timestamp = timestamp;
    }

    public Point getPoint() {
        return point;
    }
    public boolean getIsHit() {
        return isHit;
    }
    public long getTimestamp() {
        return timestamp;
    }
}
