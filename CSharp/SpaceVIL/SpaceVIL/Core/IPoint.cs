using System.Drawing;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    public interface IPoints
    {
        void SetPointThickness(float thickness);
        float GetPointThickness();
        void SetPointColor(Color color);
        Color GetPointColor();
        void SetShapePointer(List<float[]> shape);
        List<float[]> GetShapePointer();
        List<float[]> MakeShape();
        void SetPointsCoord(List<float[]> coord);
    }
}