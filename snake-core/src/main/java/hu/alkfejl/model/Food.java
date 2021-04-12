package hu.alkfejl.model;

import javafx.beans.property.*;

import java.util.Random;


public class Food
{
    private static final Random s_Random = new Random();

    private ReadOnlyIntegerWrapper m_Point;
    private ReadOnlyStringWrapper m_Sprite;
    private ReadOnlyStringWrapper m_Name;


    private Food()
    {
        m_Point = new ReadOnlyIntegerWrapper();
        m_Sprite = new ReadOnlyStringWrapper();
        m_Name = new ReadOnlyStringWrapper();
    }


    public static Food Apple()
    {
        var ret = new Food();
        ret.m_Point.set(100);
        ret.m_Sprite.set("apple.png");
        ret.m_Name.set("Apple");
        return ret;
    }


    public static Food Pear()
    {
        var ret = new Food();
        ret.m_Point.set(150);
        ret.m_Sprite.set("pear.png");
        ret.m_Name.set("Pear");
        return ret;
    }


    public static Food Peach()
    {
        var ret = new Food();
        ret.m_Point.set(200);
        ret.m_Sprite.set("peach.png");
        ret.m_Name.set("Peach");
        return ret;
    }


    public static Food Banana()
    {
        var ret = new Food();
        ret.m_Point.set(250);
        ret.m_Sprite.set("banana.png");
        ret.m_Name.set("Banana");
        return ret;
    }


    public static Food Random()
    {
        switch (s_Random.nextInt(4))
        {
            case 0: return Apple();
            case 1: return Pear();
            case 2: return Peach();
            case 3: return Banana();
        }

        return null;
    }


    public ReadOnlyIntegerProperty getPointProperty()
    {
        return m_Point.getReadOnlyProperty();
    }
    public ReadOnlyStringProperty getSpriteProperty()
    {
        return m_Sprite.getReadOnlyProperty();
    }
    public ReadOnlyStringProperty getNameProperty()
    {
        return m_Name.getReadOnlyProperty();
    }
    public int getPoint()
    {
        return m_Point.get();
    }
    public String getSprite()
    {
        return m_Sprite.get();
    }
    public String getName()
    {
        return m_Name.get();
    }
}
