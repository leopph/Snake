package hu.alkfejl.model;

import javafx.beans.property.*;
import javafx.util.Pair;


public class Map
{
    public static final int MIN_SIZE = 5;

    /* PROPERTIES */
    private final ObjectProperty<Pair<Vector2, Food>> m_Food;
    private final IntegerProperty m_SizeX;
    private final IntegerProperty m_SizeY;
    private final BooleanProperty m_UpWall;
    private final BooleanProperty m_DownWall;
    private final BooleanProperty m_LeftWall;
    private final BooleanProperty m_RightWall;


    /* CONSTRUCTORS */
    public Map()
    {
        m_Food = new SimpleObjectProperty<>();
        m_SizeX = new SimpleIntegerProperty(MIN_SIZE);
        m_SizeY = new SimpleIntegerProperty(MIN_SIZE);
        m_UpWall = new SimpleBooleanProperty(false);
        m_DownWall = new SimpleBooleanProperty(false);
        m_LeftWall = new SimpleBooleanProperty(false);
        m_RightWall = new SimpleBooleanProperty(false);
    }
    public Map(int width, int height)
    {
        this();
        m_SizeX.setValue(width);
        m_SizeY.setValue(height);
    }


    /* PROPERTY GETTERS, GETTERS, SETTERS */
    public ObjectProperty<Pair<Vector2, Food>> foodProperty()
    {
        return m_Food;
    }
    public IntegerProperty sizeXProperty() { return m_SizeX; }
    public IntegerProperty sizeYProperty() { return m_SizeY; }
    public BooleanProperty upWallProperty() { return m_UpWall; }
    public BooleanProperty downWallProperty() { return m_DownWall; }
    public BooleanProperty leftWallProperty() { return m_LeftWall; }
    public BooleanProperty rightWallProperty() { return m_RightWall; }

    public Pair<Vector2, Food> getFood() { return m_Food.get(); }
    public Integer getSizeX() { return m_SizeX.get(); }
    public Integer getSizeY() { return m_SizeY.get(); }
    public Boolean hasUpWall() { return m_UpWall.get(); }
    public Boolean hasDownWall() { return m_DownWall.get(); }
    public Boolean hasLeftWall() { return m_LeftWall.get(); }
    public Boolean hasRightWall() { return m_RightWall.get(); }

    public void setFood(Pair<Vector2, Food> newValue) { m_Food.setValue(newValue); }
    public void setSizeX(Integer newValue) { m_SizeX.setValue(newValue); }
    public void setSizeY(Integer newValue) { m_SizeY.setValue(newValue); }
    public void setUpWall(Boolean newVal) { m_UpWall.set(newVal);}
    public void setDownWall(Boolean newVal) { m_DownWall.set(newVal);}
    public void setLeftWall(Boolean newVal) { m_LeftWall.set(newVal);}
    public void setRightWall(Boolean newVal) { m_RightWall.set(newVal);}
}
