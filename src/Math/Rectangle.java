package Math;

public class Rectangle {

    public Point[] points;

    public Rectangle(Point a, Point b, Point c, Point d){
        points = new Point[4];
        points[0] = a;
        points[1] = b;
        points[2] = c;
        points[3] = d;
    }

    public Point[] getPoints() {
        return points;
    }

    //      fig. 1:

//      A////////////////////////////////////////B
//      //                                      //
//      //                                      //
//      D////////////////////////////////////////C
}
