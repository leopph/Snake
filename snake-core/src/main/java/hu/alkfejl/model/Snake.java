package hu.alkfejl.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

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


    /* PROPERTIES */
    private final ReadOnlyListWrapper<Vector2> m_BodyCoords;
    private final ReadOnlyObjectWrapper<Direction> m_CurrentDirection;
    private final ObjectProperty<Direction> m_NextDirection;
    private final ObjectProperty<Color> m_HeadColor;
    private final ObjectProperty<Color> m_BodyColor;


    /* CONSTRUCTOR */
    public Snake()
    {
        m_BodyCoords = new ReadOnlyListWrapper<>(FXCollections.observableList(new ArrayList<>()));
        for (int i = 2; i >= 0; i--)
            m_BodyCoords.add(new Vector2(i, 0));

        m_CurrentDirection = new ReadOnlyObjectWrapper<>(Direction.RIGHT);
        m_NextDirection = new SimpleObjectProperty<>(Direction.RIGHT);

        m_HeadColor = new SimpleObjectProperty<>(Color.DEEPPINK);
        m_BodyColor = new SimpleObjectProperty<>(Color.WHITE);
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ReadOnlyObjectProperty<Direction> currentDirectionProperty() { return m_CurrentDirection; }
    public ObjectProperty<Direction> nextDirectionProperty() { return m_NextDirection; }
    public ListProperty<Vector2> bodyCoordsProperty() { return m_BodyCoords; }
    public ObjectProperty<Color> headColorProperty() { return m_HeadColor; }
    public ObjectProperty<Color> bodyColorProperty() { return m_BodyColor; }

    public Direction getCurrentDirection()
    {
        return m_CurrentDirection.get();
    }
    public Direction getNextDirection() { return m_NextDirection.get(); }
    public ObservableList<Vector2> getBodyCoords()
    {
        return m_BodyCoords.get();
    }
    public Color getHeadColor() { return m_HeadColor.get(); }
    public Color getBodyColor() { return m_BodyColor.get(); }

    public void setNextDirection(Direction d)
    {
        m_NextDirection.setValue(d);
    }
    public void setHeadColor(Color c) { m_HeadColor.setValue(c); }
    public void setBodyColor(Color c) { m_BodyColor.setValue(c); }


    /* BEHAVIOR FUNCTIONS */
    public boolean isSelfEating()
    {
        return m_BodyCoords.lastIndexOf(m_BodyCoords.get(0)) != 0;
    }


    public Vector2 nextHeadPosition()
    {
        switch (m_NextDirection.get())
        {
            case UP: return m_BodyCoords.get(0).add(new Vector2(0,-1));
            case DOWN: return m_BodyCoords.get(0).add(new Vector2(0, 1));
            case LEFT: return m_BodyCoords.get(0).add(new Vector2(-1 , 0));
            default: return m_BodyCoords.get(0).add(new Vector2(1 , 0));
        }
    }
    public Vector2 nextHeadPosition(int modX, int modY)
    {
        var pos = nextHeadPosition();
        pos.setX(Math.floorMod(pos.getX(), modX));
        pos.setY(Math.floorMod(pos.getY(), modY));
        return pos;
    }


    public void move(boolean shouldGrow, int modX, int modY)
    {
        if (shouldGrow)
            m_BodyCoords.add(0, nextHeadPosition());
        else
        {
            for (int i = m_BodyCoords.size() - 1; i >= 1; i--)
                m_BodyCoords.set(i, m_BodyCoords.get(i - 1));

            m_BodyCoords.set(0, nextHeadPosition());
        }

        m_CurrentDirection.setValue(m_NextDirection.get());
        m_BodyCoords.set(0, new Vector2(Math.floorMod(m_BodyCoords.get(0).getX(), modX), Math.floorMod(m_BodyCoords.get(0).getY(), modY)));
    }


    public void translate(Vector2 v)
    {
        for (int i = 0; i < m_BodyCoords.size(); i++)
            m_BodyCoords.set(i, m_BodyCoords.get(i).add(v));
    }

    public void flip()
    {
        for (int i = 0, j = m_BodyCoords.size() - 1; i < j; i++, j--)
        {
            var tmp = m_BodyCoords.get(i);
            m_BodyCoords.set(i, m_BodyCoords.get(j));
            m_BodyCoords.set(j, tmp);
        }
    }

    public void rotate90Deg(Rotation r)
    {
        for (int i = 1; i < m_BodyCoords.size(); i++)
            m_BodyCoords.set(i, new Vector2((r == Rotation.CLOCKWISE ? -1 : 1) * (m_BodyCoords.get(i).getY() - m_BodyCoords.get(0).getY()), (r == Rotation.CLOCKWISE ? -1 : 1) * m_BodyCoords.get(i).getX() - m_BodyCoords.get(0).getX()));

        if (r == Rotation.CLOCKWISE)
            switch (m_CurrentDirection.get())
            {
                case UP: setNextDirection(Direction.RIGHT); return;
                case RIGHT: setNextDirection(Direction.DOWN); return;
                case DOWN: setNextDirection(Direction.LEFT); return;
                case LEFT: setNextDirection(Direction.UP);
            }
        else
            switch (m_CurrentDirection.get())
            {
                case UP: setNextDirection(Direction.LEFT); return;
                case LEFT: setNextDirection(Direction.DOWN); return;
                case DOWN: setNextDirection(Direction.RIGHT); return;
                case RIGHT: setNextDirection(Direction.UP);
            }
    }
}
