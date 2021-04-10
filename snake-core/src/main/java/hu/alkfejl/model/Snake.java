package hu.alkfejl.model;

import java.util.List;
import java.util.ArrayList;


public final class Snake
{
    public enum Direction
    {
        UP, DOWN, LEFT, RIGHT
    }


    private final List<Point> m_BodyParts;
    private Direction m_Direction;


    public Snake()
    {
        m_BodyParts = new ArrayList<>();
        for (int i = 2; i >= 0; i--)
            m_BodyParts.add(new Point(i, 0));

        m_Direction = Direction.RIGHT;
    }


    public boolean isOnPoint(Point p)
    {
        for (var point : m_BodyParts)
            if (p.equals(point))
                return true;

        return false;
    }


    public void changeDirection(Direction d)
    {
        m_Direction = d;
    }


    public boolean move(boolean shouldGrow)
    {
        if (shouldGrow)
            m_BodyParts.add(0, nextHeadLocation());
        else
        {
            for (int i = m_BodyParts.size() - 1; i >= 1; i--)
                m_BodyParts.set(i, m_BodyParts.get(i - 1));

            m_BodyParts.set(0, nextHeadLocation());
        }

        return !isSelfEating();
    }


    private boolean isSelfEating()
    {
        for (int i = 0; i < m_BodyParts.size(); i++)
            for (int j = 0; j < m_BodyParts.size(); j++)
                if (i != j && m_BodyParts.get(i).equals(m_BodyParts.get(j)))
                    return true;

        return false;
    }


    public Point nextHeadLocation()
    {
        Point diff = null;

        switch (m_Direction)
        {
            case UP:
                diff = new Point(0,1);
                break;
            case DOWN:
                diff =  new Point(0, -1);
                break;
            case LEFT:
                diff =  new Point(-1 , 0);
                break;
            case RIGHT:
                diff = new Point(+1 , 0);
        }

        return diff.add(m_BodyParts.get(0));
    }
}
