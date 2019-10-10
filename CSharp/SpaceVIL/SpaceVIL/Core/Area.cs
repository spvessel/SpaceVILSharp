public class Area
{
    private int _x, _y, _w, _h;
    public Area()
    {
        _x = _y = _w = _h = 0;
    }
    public Area(int x, int y, int w, int h)
    {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }
    public void SetX(int value)
    {
        _x = value;
    }
    public void SetY(int value)
    {
        _y = value;
    }
    public void SetWidth(int value)
    {
        _w = value;
    }
    public void SetHeight(int value)
    {
        _h = value;
    }

    public int GetX()
    {
        return _x;
    }

    public int GetY()
    {
        return _y;
    }

    public int GetWidth()
    {
        return _w;
    }

    public int GetHeight()
    {
        return _h;
    }
    public void SetAttr(int x, int y, int w, int h)
    {
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    public override string ToString()
    {
        return _x + " " + _y + " " + _w + " " + _h;
    }
}