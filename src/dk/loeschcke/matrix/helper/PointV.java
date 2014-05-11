package dk.loeschcke.matrix.helper;

import dollarrecognizer.P.Point;

public class PointV extends Point {

	public double V;

	public PointV(double x, double y, double v) {
        this(x,y,v,0);
	}

    public PointV(double x, double y, double v, int id) {
        super(x, y, id);
        this.V = v;
    }

    @Override
    public String toString() {
        return "PointV(" + X + "," + Y + "," + V + "," + ID + ")";
    }


}
