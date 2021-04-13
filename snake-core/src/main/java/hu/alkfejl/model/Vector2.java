package hu.alkfejl.model;

public final class Vector2
{
    private int x;
    private int y;


    public Vector2()
    {
        x = 0;
        y = 0;
    }

    public Vector2(int px, int py)
    {
        x = px;
        y = py;
    }

    public Vector2(Vector2 other)
    {
        x = other.x;
        y = other.y;
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
        if (!(p instanceof Vector2))
            return false;

        return ((Vector2) p).x == x && ((Vector2) p).y == y;
    }


    Vector2 add(Vector2 other)
    {
        return new Vector2(x + other.x, y + other.y);
    }

    Vector2 sub(Vector2 other)
    {
        return new Vector2(x - other.x, y - other.y);
    }


    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
