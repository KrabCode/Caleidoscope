package Math;

public final class Rectangle {

    public final Point[] points;

    public Rectangle(Point a, Point b, Point c, Point d){
        points = new Point[4];
        points[0] = a;
        points[1] = b;
        points[2] = c;
        points[3] = d;
    }

    public Point getA(){return points[0];}
    public Point getB(){return points[1];}
    public Point getC(){return points[2];}
    public Point getD(){return points[3];}

    public static Rectangle fromPoints(Point start, Point end){
        return new Rectangle(
                new Point(start.x, start.y),
                new Point(end.x, start.y),
                new Point(end.x, end.y),
                new Point(start.x, end.y)
        );
    }



    //      fig. 1:

//      A////////////////////////////////////////B
//      //                                      //
//      //                                      //
//      D////////////////////////////////////////C
}
