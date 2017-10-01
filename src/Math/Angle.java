package Math;


import static processing.core.PApplet.*;

public class Angle {
    //////////////////////////////////////////
    // LOW LEVEL MATH BEYOND THIS POINT
    // STAY WOKE
    //////////////////////////////////////////

    /**
     * Finds a point in a given angle and distance from a center point.
     *
     * @param center center point * @param radius given distance
     * @param angle  given angle * @return
     */
    public static Point getPointAtAngle(Point center, float radius, float angle) {
        return new Point(
                Float.parseFloat(center.x + radius * cos(angle * PI / 180)+""),
                Float.parseFloat(center.y + radius * sin(angle * PI / 180)+"")
        );
    }

    /**
     * Returns angle of a line vs the horizon
     *
     * @param origin start of the line
     * @param end    end of the line 🚆
     * @return horizontal line returns 0, vertical line returns -90 or 90
     */
    public static float getAngle(Point origin, Point end) {
        return degrees(atan2(end.y - origin.y, end.x - origin.x));
    }

    public static float toRad(float degrees){
        return (float) java.lang.Math.toRadians(degrees);
    }
}
