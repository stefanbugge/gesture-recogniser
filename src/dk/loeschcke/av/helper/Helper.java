package dk.loeschcke.av.helper;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 08/09/13
 * Time: 12.11
 * To change this template use File | Settings | File Templates.
 */
public class Helper {

    public static double mapRange(double a1, double a2, double b1, double b2, double s){
        return b1 + ((s - a1)*(b2 - b1))/(a2 - a1);
    }

}
