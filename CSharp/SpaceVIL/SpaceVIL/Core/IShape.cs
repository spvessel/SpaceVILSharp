using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL.Core
{
    internal interface IShape
    {
        void SetItemName(string name);
        string GetItemName();
        void SetBackground(Color color);
        Color GetBackground();
        List<float[]> GetTriangles();
        void SetTriangles(List<float[]> triangles);
        List<float[]> MakeShape();
    }
}
