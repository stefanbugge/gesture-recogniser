package dollarrecognizer.P;

import dollarrecognizer.P.PDollarRecognizer;
import dollarrecognizer.P.Point;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 21/08/13
 * Time: 13.23
 * To change this template use File | Settings | File Templates.
 */
public class DirectionBasedRecogniser extends PDollarRecognizer {

    public DirectionBasedRecogniser() {
        initializePointCloudTable();
    }

    @Override
    protected void initializePointCloudTable() {
        mPntClouds.add(new PointCloud("horizontal line",
                new ArrayList<Point>(Arrays.asList(new Point(12, 347, 1), new Point(119, 347, 1))), mNumPoints));

        mPntClouds.add(new PointCloud("vertical line",
                new ArrayList<Point>(Arrays.asList(new Point(347, 12, 1), new Point(347, 119, 1))), mNumPoints));

        mPntClouds.add(new PointCloud("cross line up",
                new ArrayList<Point>(Arrays.asList(new Point(12, 12, 1), new Point(347, 347, 1))), mNumPoints));

        mPntClouds.add(new PointCloud("cross line down",
                new ArrayList<Point>(Arrays.asList(new Point(347, 12, 1), new Point(12, 347, 1))), mNumPoints));

        mPntClouds.add(new PointCloud("NULL",
                new ArrayList<Point>(Arrays.asList(new Point(382, 310, 1), new Point(377,
                    308, 1), new Point(373, 307, 1), new Point(366, 307, 1), new Point(360, 310, 1),
                    new Point(356, 313, 1), new Point(353, 316, 1), new Point(349, 321, 1), new Point(347, 326, 1),
                    new Point(344, 331, 1), new Point(342, 337, 1), new Point(341, 343, 1), new Point(341, 350, 1),
                    new Point(341, 358, 1), new Point(342, 362, 1), new Point(344, 366, 1), new Point(347, 370, 1),
                    new Point(351, 374, 1), new Point(356, 379, 1), new Point(361, 382, 1), new Point(368, 385, 1),
                    new Point(374, 387, 1), new Point(381, 387, 1), new Point(390, 387, 1), new Point(397, 385, 1),
                    new Point(404, 382, 1), new Point(408, 378, 1), new Point(412, 373, 1), new Point(416, 367, 1),
                    new Point(418, 361, 1), new Point(419, 353, 1), new Point(418, 346, 1), new Point(417, 341, 1),
                    new Point(416, 336, 1), new Point(413, 331, 1), new Point(410, 326, 1), new Point(404, 320, 1),
                    new Point(400, 317, 1), new Point(393, 313, 1), new Point(392, 312, 1), new Point(418, 309, 2),
                    new Point(337, 390, 2))
                ), mNumPoints)
        );

        mPntClouds.add(new PointCloud("X", new ArrayList<Point>(Arrays.asList(new Point(30, 146, 1), new Point(106,
                222, 1), new Point(30, 225, 2), new Point(106, 146, 2))), mNumPoints));

        mPntClouds.add(new PointCloud("arrowhead", new ArrayList<Point>(Arrays.asList(new Point(506, 349, 1),
                new Point(574, 349, 1), new Point(525, 306, 2), new Point(584, 349, 2), new Point(525, 388, 2))),
                mNumPoints));
    }
}
