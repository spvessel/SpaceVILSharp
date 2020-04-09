using System.Collections.Generic;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Figure class represents any triangulated shape. It is used to draw any types of custom shapes.
    /// </summary>
    public class Figure
    {
        private List<float[]> _figure;

        /// <summary>
        /// Getting list of pairs - [x, y] coordinates of a shape.
        /// </summary>
        /// <returns> Figure points list as List of float[2] array.</returns>
        public List<float[]> GetFigure()
        {
            return _figure;
        }

        private bool _isFixed = false;

        /// <summary>
        /// Is Figure fixed.
        /// </summary>
        /// <returns> True: if shape can not be resized. False: if shape can be resised.</returns>
        public bool IsFixed()
        {
            return _isFixed;
        }

        /// <summary>
        /// Constructs a Figure with specified triangles and specified fixed flag.
        /// </summary>
        /// <param name="isFixed"> True: if shape can not be resized. False: if shape can be resised. </param>
        /// <param name="triangles"> Triangles list of the Figure's shape. </param>
        public Figure(bool isFixed, List<float[]> triangles)
        {
            _isFixed = isFixed;
            _figure = new List<float[]>(triangles);
        }
        
        /// <summary>
        /// Constructs a Figure with specified triangles.
        /// </summary>
        /// <param name="triangles">Triangles list of the Figure's shape.</param>
        public Figure(List<float[]> triangles)
        {
            _figure = new List<float[]>(triangles);
        }

        /// <summary>
        /// Updating the coordinates of triangles with specified shifts along the X and Y axis.
        /// </summary>
        /// <param name="x">Shift by X-axis.</param>
        /// <param name="y">Shift by Y-axis.</param>
        /// <returns>Updated points list changed according to the new shift by (x, y)</returns>
        public List<float[]> UpdatePosition(int x, int y)
        {
            List<float[]> result = new List<float[]>();
            foreach (var item in _figure)
            {
                result.Add( new float[] {item[0] + x, item[1] + y, 0.0f });
            }
            return result;
        }
    }
}