using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    internal class Item : IItem
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
        public Color _bg = Color.White;
        public void SetBackground(Color color)
        {
            _bg = color;
        }
        public Color GetBackground()
        {
            return _bg;
        }
        private List<float[]> _triangles = null;//new List<float[]>();
        public void SetTriangles(List<float[]> triangles)
        {
            _triangles = triangles;
        }
        public List<float[]> GetTriangles()
        {
            return _triangles;
        }
        public virtual void MakeShape() { }
    }
}
