package hu.alkfejl.model;

import java.util.List;
import java.util.ArrayList;


public final class Snake
{
    public enum Direction
    {
        UP, DOWN, LEFT, RIGHT
    }

    public enum Rotation
    {
        CLOCKWISE, COUNTERCLOCKWISE
    }


    private final List<Vector2> m_BodyParts;
    private Direction m_Direction;


    public Snake()
    {
        m_BodyParts = new ArrayList<>();
        for (int i = 2; i >= 0; i--)
            m_BodyParts.add(new Vector2(i, 0));

        m_Direction = Direction.RIGHT;
    }


    public boolean isOnPoint(Vector2 p)
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


    public void relocate(Direction d, int coord)
    {
        switch (d)
        {
            case UP:
            case DOWN:
                m_BodyParts.get(0).setY(coord);
                break;
            case LEFT:
            case RIGHT:
                m_BodyParts.get(0).setX(coord);
        }
    }


    private boolean isSelfEating()
    {
        for (int i = 0; i < m_BodyParts.size(); i++)
            for (int j = 0; j < m_BodyParts.size(); j++)
                if (i != j && m_BodyParts.get(i).equals(m_BodyParts.get(j)))
                    return true;

        return false;
    }


    public Vector2 nextHeadLocation()
    {
        Vector2 diff = null;

        switch (m_Direction)
        {
            case UP:
                diff = new Vector2(0,1);
                break;
            case DOWN:
                diff =  new Vector2(0, -1);
                break;
            case LEFT:
                diff =  new Vector2(-1 , 0);
                break;
            case RIGHT:
                diff = new Vector2(+1 , 0);
        }

        return diff.add(m_BodyParts.get(0));
    }

    public List<Vector2> getBodyCoords()
    {
        return List.copyOf(m_BodyParts);
    }


    public void translate(Vector2 v)
    {
        for (int i = 0; i < m_BodyParts.size(); i++)
            m_BodyParts.set(i, m_BodyParts.get(i).add(v));
    }


    public void flip()
    {
        for (int i = 0, j = m_BodyParts.size() - 1; i < j; i++, j--)
        {
            var tmp = m_BodyParts.get(i);
            m_BodyParts.set(i, m_BodyParts.get(j));
            m_BodyParts.set(j, tmp);
        }
    }


    public void rotate90Deg(Rotation r)
    {
        for (int i = 1; i < m_BodyParts.size(); i++)
            m_BodyParts.set(i, new Vector2((r == Rotation.CLOCKWISE ? -1 : 1) * (m_BodyParts.get(i).getY() - m_BodyParts.get(0).getY()), (r == Rotation.CLOCKWISE ? -1 : 1) * m_BodyParts.get(i).getX() - m_BodyParts.get(0).getX()));

        if (r == Rotation.CLOCKWISE)
            switch (m_Direction)
            {
                case UP: changeDirection(Direction.RIGHT); return;
                case RIGHT: changeDirection(Direction.DOWN); return;
                case DOWN: changeDirection(Direction.LEFT); return;
                case LEFT: changeDirection(Direction.UP);
            }
        else
            switch (m_Direction)
            {
                case UP: changeDirection(Direction.LEFT); return;
                case LEFT: changeDirection(Direction.DOWN); return;
                case DOWN: changeDirection(Direction.RIGHT); return;
                case RIGHT: changeDirection(Direction.UP);
            }
    }
}
