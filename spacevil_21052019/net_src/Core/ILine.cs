using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    public interface ILine
    {
        void SetLineThickness(float thickness);
        float GetLineThickness();
        void SetLineColor(Color color);
        Color GetLineColor();
        List<float[]> MakeShape();
        void SetPointsCoord(List<float[]> coord);
    }
}