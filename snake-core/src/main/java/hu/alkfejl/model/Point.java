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

    public Point(int xp, int yp)
    {
        x = xp;
        y = yp;
    }


    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int xp)
    {
        x = xp;
    }

    public void setY(int yp)
    {
        y = yp;
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
