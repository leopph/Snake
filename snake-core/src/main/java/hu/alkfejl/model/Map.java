package hu.alkfejl.model;


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

        private byte get()
        {
            return m_Value;
        }
    }


    public static final int MIN_SIZE = 3;

    private Food[][] m_Foods;
    private byte m_Walls = 0;


    public Map()
    {
        m_Foods = new Food[3][3];
    }
    public Map(int width, int height)
    {
        m_Foods = new Food[width][height];
    }


    public Vector2 getSize()
    {
        return new Vector2(m_Foods.length, m_Foods[0].length);
    }
    public void setSize(Vector2 v) { m_Foods = new Food[v.getX()][v.getY()]; }


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
        if (v.getX() >= m_Foods.length || v.getY() >= m_Foods[0].length)
            throw new IndexOutOfBoundsException("No such tile.");

        return m_Foods[v.getX()][v.getY()];
    }

    public void setFoodOnPoint(Vector2 v, Food f)
    {
        if (v.getX() >= m_Foods.length || v.getY() >= m_Foods[0].length)
            throw new IndexOutOfBoundsException("No such tile.");

        m_Foods[v.getX()][v.getY()] = f;
    }
}
