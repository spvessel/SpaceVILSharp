public struct CornerRadius
{
    public float leftTop;
    public float rightTop;
    public float leftBottom;
    public float rightBottom;

    public CornerRadius(CornerRadius radius)
    {
        leftTop = radius.leftTop;
        rightTop = radius.rightTop;
        leftBottom = radius.leftBottom;
        rightBottom = radius.rightBottom;
    }
    public CornerRadius(float radius = 0)
    {
        leftTop = radius;
        rightTop = radius;
        leftBottom = radius;
        rightBottom = radius;
    }

    public CornerRadius(float lt, float rt, float lb, float rb)
    {
        leftTop = lt;
        rightTop = rt;
        leftBottom = lb;
        rightBottom = rb;
    }
}