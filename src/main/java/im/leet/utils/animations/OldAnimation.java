/*
 * Decompiled with CFR 0.152.
 */
package im.leet.utils.animations;

import im.leet.utils.animations.Direction;
import im.leet.utils.math.TimeUtility;

public abstract class OldAnimation {
    public TimeUtility timerUtil = new TimeUtility();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public OldAnimation(int ms, double endPoint) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }

    public OldAnimation(int ms, double endPoint, Direction direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean finished(Direction direction) {
        return this.isDone() && this.direction.equals((Object)direction);
    }

    public double getLinearOutput() {
        return 1.0 - (double)this.timerUtil.getTime() / (double)this.duration * this.endPoint;
    }

    public void reset() {
        this.timerUtil.reset();
    }

    public boolean isDone() {
        return this.timerUtil.reached(this.duration);
    }

    public void changeDirection() {
        this.setDirection(this.direction.opposite());
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTime())));
        }
    }

    protected boolean correctOutput() {
        return false;
    }

    public float getOutput() {
        if (this.direction == Direction.FORWARDS) {
            if (this.isDone()) {
                return (float)this.endPoint;
            }
            return (float)(this.getEquation(this.timerUtil.getTime()) * this.endPoint);
        }
        if (this.isDone()) {
            return 0.0f;
        }
        if (this.correctOutput()) {
            double revTime = Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
            return (float)(this.getEquation(revTime) * this.endPoint);
        }
        return (float)((1.0 - this.getEquation(this.timerUtil.getTime())) * this.endPoint);
    }

    protected abstract double getEquation(double var1);

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public double getEndPoint() {
        return this.endPoint;
    }

    public Direction getDirection() {
        return this.direction;
    }
}

