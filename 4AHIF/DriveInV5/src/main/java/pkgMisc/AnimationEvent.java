package pkgMisc;

import java.util.EventObject;

public class AnimationEvent extends EventObject {
    private double oldX;
    private double oldY;
    private double newX;
    private double newY;

    public AnimationEvent(Object source, double oldX, double oldY, double newX, double newY) {
        super(source);
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public double getNewX() {
        return newX;
    }

    public double getNewY() {
        return newY;
    }
}
