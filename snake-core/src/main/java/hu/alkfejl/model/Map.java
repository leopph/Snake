package hu.alkfejl.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;


public class Map
{
    public static final int MIN_SIZE = 3;

    /* PROPERTIES */
    private final ObjectProperty<Pair<Vector2, Food>> m_Food;
    private final ObjectProperty<Vector2> m_Size;
    private final BooleanProperty m_UpWall;
    private final BooleanProperty m_DownWall;
    private final BooleanProperty m_LeftWall;
    private final BooleanProperty m_RightWall;


    /* CONSTRUCTORS */
    public Map()
    {
        m_Size = new SimpleObjectProperty<>(new Vector2(MIN_SIZE, MIN_SIZE));
        m_Food = new SimpleObjectProperty<>();
        m_UpWall = new SimpleBooleanProperty(false);
        m_DownWall = new SimpleBooleanProperty(false);
        m_LeftWall = new SimpleBooleanProperty(false);
        m_RightWall = new SimpleBooleanProperty(false);
    }
    public Map(int width, int height)
    {
        this();
        m_Size.setValue(new Vector2(width, height));
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Pair<Vector2, Food>> foodProperty()
    {
        return m_Food;
    }
    public ObjectProperty<Vector2> sizeProperty() { return m_Size; }
    public BooleanProperty upWallProperty() { return m_UpWall; }
    public BooleanProperty downWallProperty() { return m_DownWall; }
    public BooleanProperty leftWallProperty() { return m_LeftWall; }
    public BooleanProperty rightWallProperty() { return m_RightWall; }

    public Pair<Vector2, Food> getFood() { return m_Food.get(); }
    public Vector2 getSize()
    {
        return new Vector2(m_Size.get());
    }
    public Boolean hasUpWall() { return m_UpWall.get(); }
    public Boolean hasDownWall() { return m_DownWall.get(); }
    public Boolean hasLeftWall() { return m_LeftWall.get(); }
    public Boolean hasRightWall() { return m_RightWall.get(); }

    public void setFood(Pair<Vector2, Food> newValue) { m_Food.setValue(newValue); }
    public void setSize(Vector2 v) { m_Size.setValue(v); }
    public void setUpWall(Boolean newVal) { m_UpWall.set(newVal);}
    public void setDownWall(Boolean newVal) { m_DownWall.set(newVal);}
    public void setLeftWall(Boolean newVal) { m_LeftWall.set(newVal);}
    public void setRightWall(Boolean newVal) { m_RightWall.set(newVal);}
}
