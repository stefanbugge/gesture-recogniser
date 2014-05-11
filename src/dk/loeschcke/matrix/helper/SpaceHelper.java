package dk.loeschcke.matrix.helper;

import dollarrecognizer.P.Point;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 16/08/13
 * Time: 11.53
 * To change this template use File | Settings | File Templates.
 */
public class SpaceHelper {

    public static double distance(Point p1, Point p2) {
        double dx = p2.X - p1.X;
        double dy = p2.Y - p1.Y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
