package hu.alkfejl.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;


public class Map
{
    public enum Wall
    {
        LEFT((byte) 0x1), RIGHT((byte) 0x2), UP((byte) 0x4), DOWN((byte) 0x8);
        private final byte m_Value;

        Wall(byte i)
        {
            m_Value = i;
        }

        public byte get()
        {
            return m_Value;
        }
    }


    public static final int MIN_SIZE = 3;

    private ObjectProperty<Pair<Vector2, Food>> m_Food;
    private Vector2 m_Size;
    private byte m_Walls = 0;


    public Map()
    {
        m_Size = new Vector2(3, 3);
        m_Food = new SimpleObjectProperty<>();
    }
    public Map(int width, int height)
    {
        m_Size = new Vector2(width, height);
        m_Food = new SimpleObjectProperty<>();
    }


    public ObjectProperty<Pair<Vector2, Food>> getFoodProperty()
    {
        return m_Food;
    }
    public Pair<Vector2, Food> getFood()
    {
        return m_Food.get();
    }
    public Vector2 getSize()
    {
        return new Vector2(m_Size);
    }
    public void setSize(Vector2 v) { m_Size = v; }


    public void addWalls(Wall... walls)
    {
        for (var wall : walls)
            m_Walls |= wall.get();
    }


    public void removeWalls(Wall... walls)
    {
        for (var wall : walls)
            m_Walls &= ~wall.get();
    }


    public byte getWalls()
    {
        return m_Walls;
    }


    public Food getFoodOnPoint(Vector2 v)
    {
        if (v.getX() >= m_Size.getX() || v.getY() >= m_Size.getY())
            throw new IndexOutOfBoundsException("No such tile.");

        return v.equals(m_Food.get().getKey()) ? m_Food.get().getValue() : null;
    }

    public void setFoodOnPoint(Vector2 v, Food f)
    {
        if (v.getX() >= m_Size.getX() || v.getY() >= m_Size.getY())
            throw new IndexOutOfBoundsException("No such tile.");

        m_Food.setValue(new Pair<>(v, f));
    }
}
