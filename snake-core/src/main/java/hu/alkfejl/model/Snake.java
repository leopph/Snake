package hu.alkfejl.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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


    private final ListProperty<Vector2> m_BodyParts;
    private Direction m_Direction;


    public Snake()
    {
        m_BodyParts = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        for (int i = 2; i >= 0; i--)
            m_BodyParts.add(new Vector2(i, 0));

        m_Direction = Direction.RIGHT;
    }


    public Direction getDirection()
    {
        return m_Direction;
    }
    public ListProperty<Vector2> getBodyPartProperty()
    {
        return m_BodyParts;
    }
    public ObservableList<Vector2> getBodyCoords()
    {
        return m_BodyParts.get();
    }

    public void changeDirection(Direction d)
    {
        m_Direction = d;
    }


    public boolean isSelfEating()
    {
        for (int i = 0; i < m_BodyParts.size(); i++)
            for (int j = 0; j < m_BodyParts.size(); j++)
                if (i != j && m_BodyParts.get(i).equals(m_BodyParts.get(j)))
                    return true;

        return false;
    }


    public Vector2 nextHeadPosition()
    {
        switch (m_Direction)
        {
            case UP: return m_BodyParts.get(0).add(new Vector2(0,-1));
            case DOWN: return m_BodyParts.get(0).add(new Vector2(0, 1));
            case LEFT: return m_BodyParts.get(0).add(new Vector2(-1 , 0));
            default: return m_BodyParts.get(0).add(new Vector2(+1 , 0));
        }
    }

    public Vector2 nextHeadPosition(int mod)
    {
        var pos = nextHeadPosition();
        pos.setX(Math.floorMod(pos.getX(), mod));
        pos.setY(Math.floorMod(pos.getY(), mod));
        return pos;
    }


    public void move(boolean shouldGrow)
    {
        if (shouldGrow)
            m_BodyParts.add(0, nextHeadPosition());
        else
        {
            for (int i = m_BodyParts.size() - 1; i >= 1; i--)
                m_BodyParts.set(i, m_BodyParts.get(i - 1));

            m_BodyParts.set(0, nextHeadPosition());
        }
    }

    public void move(boolean shouldGrow, int mod)
    {
        move(shouldGrow);
        for (var pos : m_BodyParts)
        {
            pos.setX(Math.floorMod(pos.getX(), mod));
            pos.setY(Math.floorMod(pos.getY(), mod));
        }
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
