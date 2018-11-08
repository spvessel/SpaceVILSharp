public class CornerRadius
{
    public float LeftTop;
    public float RightTop;
    public float LeftBottom;
    public float RightBottom;

    public CornerRadius(CornerRadius radius)
    {
        LeftTop = radius.LeftTop;
        RightTop = radius.RightTop;
        LeftBottom = radius.LeftBottom;
        RightBottom = radius.RightBottom;
    }
    public CornerRadius(float radius =  0)
    {
        LeftTop = radius;
        RightTop = radius;
        LeftBottom = radius;
        RightBottom = radius;
    }

    public CornerRadius(float lt, float rt, float lb, float rb)
    {
        LeftTop = lt;
        RightTop = rt;
        LeftBottom = lb;
        RightBottom = rb;
    }
}