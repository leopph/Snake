package hu.alkfejl.model;

public final class Point
{
    private int x;
    private int y;


    public Point()
    {
        x = 0;
        y = 0;
    }

    public Point(int px, int py)
    {
        x = px;
        y = py;
    }


    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int px)
    {
        x = px;
    }

    public void setY(int py)
    {
        y = py;
    }


    @Override
    public boolean equals(Object p)
    {
        if (!(p instanceof Point))
            return false;

        return ((Point) p).x == x && ((Point) p).y == y;
    }


    Point add(Point other)
    {
        return new Point(x + other.x, y + other.y);
    }

    Point sub(Point other)
    {
        return new Point(x - other.x, y - other.y);
    }
}
