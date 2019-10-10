using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    public interface ILine : IPosition
    {
        void SetLineThickness(float thickness);
        float GetLineThickness();
        void SetLineColor(Color color);
        Color GetLineColor();
        List<float[]> GetPoints();
        void SetPointsCoord(List<float[]> coord);
    }
}