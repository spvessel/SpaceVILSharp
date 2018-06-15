using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class Shape : IShape
    {
        private string _name;
        public void SetItemName(string name)
        {
            _name = name;
        }
        public string GetItemName()
        {
            return _name;
        }
        public Color _bg;
        public void SetBackground(Color color)
        {
            _bg = color;
        }
        public Color GetBackground()
        {
            return _bg;
        }
        private List<float[]> _triangles = new List<float[]>();
        public void SetTriangles(List<float[]> triangles)
        {
            _triangles = triangles;
        }
        public List<float[]> GetTriangles()
        {
            return _triangles;
        }
        public virtual List<float[]> MakeShape()
        {
            return GetTriangles();
        }
    }
}

