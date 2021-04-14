package hu.alkfejl.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;


public class Map
{
    /* BITMASK TO REPRESENT SET WALLS */
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

    /* PROPERTIES */
    private ObjectProperty<Pair<Vector2, Food>> m_Food;
    private ObjectProperty<Vector2> m_Size;
    private byte m_Walls;


    /* CONSTRUCTORS */
    public Map()
    {
        m_Size = new SimpleObjectProperty<>(new Vector2(MIN_SIZE, MIN_SIZE));
        m_Food = new SimpleObjectProperty<>();
        m_Walls = 0;
    }
    public Map(int width, int height)
    {
        m_Size = new SimpleObjectProperty<>(new Vector2(width, height));
        m_Food = new SimpleObjectProperty<>();
        m_Walls = 0;
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Pair<Vector2, Food>> foodProperty()
    {
        return m_Food;
    }
    public ObjectProperty<Vector2> sizeProperty() { return m_Size; }

    public Pair<Vector2, Food> getFood() { return m_Food.get(); }
    public Vector2 getSize()
    {
        return new Vector2(m_Size.get());
    }
    public byte getWalls() { return m_Walls; }

    public void setFood(Pair<Vector2, Food> newValue) { m_Food.setValue(newValue); }
    public void setSize(Vector2 v) { m_Size.setValue(v); }


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
}
