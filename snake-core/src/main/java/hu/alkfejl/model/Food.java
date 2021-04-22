package hu.alkfejl.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Random;


public class Food implements Serializable
{
    /* STATIC MEMBERS */
    private static final Random s_Random = new Random();
    private static final ObjectProperty<Color> APPLE_COLOR;
    private static final ObjectProperty<Color> CUCUMBER_COLOR;
    private static final ObjectProperty<Color> PEACH_COLOR;
    private static final ObjectProperty<Color> BANANA_COLOR;

    static
    {
        APPLE_COLOR = new SimpleObjectProperty<>(Color.RED);
        CUCUMBER_COLOR = new SimpleObjectProperty<>(Color.GREEN);
        PEACH_COLOR = new SimpleObjectProperty<>(Color.PEACHPUFF);
        BANANA_COLOR = new SimpleObjectProperty<>(Color.YELLOW);
    }

    /* INSTANCE PROPERTIES */
    private final ReadOnlyIntegerWrapper m_Point;
    private final ReadOnlyObjectWrapper<Color> m_Color;
    private final ReadOnlyStringWrapper m_Name;


    /* CONSTRUCTOR */
    private Food()
    {
        m_Point = new ReadOnlyIntegerWrapper(0);
        m_Color = new ReadOnlyObjectWrapper<>(Color.PAPAYAWHIP);
        m_Name = new ReadOnlyStringWrapper("UNNAMED FOOD");
    }


    /* FACTORIES */
    public static Food Apple()
    {
        var ret = new Food();
        ret.m_Point.set(100);
        ret.m_Color.set(APPLE_COLOR.get());
        ret.m_Name.set("Apple");
        return ret;
    }


    public static Food Cucumber()
    {
        var ret = new Food();
        ret.m_Point.set(150);
        ret.m_Color.set(CUCUMBER_COLOR.get());
        ret.m_Name.set("Cucumber");
        return ret;
    }


    public static Food Peach()
    {
        var ret = new Food();
        ret.m_Point.set(200);
        ret.m_Color.set(PEACH_COLOR.get());
        ret.m_Name.set("Peach");
        return ret;
    }


    public static Food Banana()
    {
        var ret = new Food();
        ret.m_Point.set(250);
        ret.m_Color.set(BANANA_COLOR.get());
        ret.m_Name.set("Banana");
        return ret;
    }


    public static Food Random()
    {
        switch (s_Random.nextInt(4))
        {
            case 0: return Apple();
            case 1: return Cucumber();
            case 2: return Peach();
            case 3: return Banana();
        }

        return null;
    }


    /* CLASS PROPERTY GETTERS, GETTERS, SETTERS */
    public static ObjectProperty<Color> appleColorProperty() { return APPLE_COLOR; }
    public static ObjectProperty<Color> cucumberColorProperty() { return CUCUMBER_COLOR; }
    public static ObjectProperty<Color> peachColorProperty() { return PEACH_COLOR; }
    public static ObjectProperty<Color> bananaColorProperty() { return BANANA_COLOR; }

    public static Color getAppleColor() { return APPLE_COLOR.get(); }
    public static Color getCucumberColor() { return CUCUMBER_COLOR.get(); }
    public static Color getPeachColor() { return PEACH_COLOR.get(); }
    public static Color getBananaColor() { return BANANA_COLOR.get(); }

    public static void setAppleColor(Color newVal) { APPLE_COLOR.setValue(newVal); }
    public static void setCucumberColor(Color newVal) { CUCUMBER_COLOR.setValue(newVal); }
    public static void setPeachColor(Color newVal) { PEACH_COLOR.setValue(newVal); }
    public static void setBananaColor(Color newVal) { BANANA_COLOR.setValue(newVal); }


    /* INSTANCE PROPERTY GETTERS, GETTERS, SETTERS */
    public ReadOnlyIntegerProperty pointProperty()
    {
        return m_Point.getReadOnlyProperty();
    }
    public ReadOnlyObjectProperty<Color> colorProperty()
    {
        return m_Color.getReadOnlyProperty();
    }
    public ReadOnlyStringProperty nameProperty()
    {
        return m_Name.getReadOnlyProperty();
    }
    
    public int getPoint()
    {
        return m_Point.get();
    }
    public Color getColor()
    {
        return m_Color.get();
    }
    public String getName()
    {
        return m_Name.get();
    }

    public void setPoint(int newVal) { m_Point.setValue(newVal); }
    public void setColor(Color newVal) { m_Color.setValue(newVal); }
    public void setName(String newVal) { m_Name.setValue(newVal);}
}
