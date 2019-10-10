using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface IItem
    {
        void SetItemName(string name);
        string GetItemName();
        void SetBackground(Color color);
        Color GetBackground();
        List<float[]> GetTriangles();
        void SetTriangles(List<float[]> triangles);
        void MakeShape();
    }
}
