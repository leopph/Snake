package hu.alkfejl.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.Random;


public class Food
{
    private static final Random s_Random = new Random();

    private ReadOnlyIntegerWrapper m_Point;
    private ReadOnlyObjectWrapper<Color> m_Color;
    private ReadOnlyStringWrapper m_Name;


    private Food()
    {
        m_Point = new ReadOnlyIntegerWrapper(0);
        m_Color = new ReadOnlyObjectWrapper<>(Color.PAPAYAWHIP);
        m_Name = new ReadOnlyStringWrapper("UNNAMED FOOD");
    }


    public static Food Apple()
    {
        var ret = new Food();
        ret.m_Point.set(100);
        ret.m_Color.set(Color.RED);
        ret.m_Name.set("Apple");
        return ret;
    }


    public static Food Cucumber()
    {
        var ret = new Food();
        ret.m_Point.set(150);
        ret.m_Color.set(Color.GREEN);
        ret.m_Name.set("Cucumber");
        return ret;
    }


    public static Food Peach()
    {
        var ret = new Food();
        ret.m_Point.set(200);
        ret.m_Color.set(Color.PEACHPUFF);
        ret.m_Name.set("Peach");
        return ret;
    }


    public static Food Banana()
    {
        var ret = new Food();
        ret.m_Point.set(250);
        ret.m_Color.set(Color.YELLOW);
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
}
