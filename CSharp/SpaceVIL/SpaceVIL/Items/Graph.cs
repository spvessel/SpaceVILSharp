using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// Graph is class representing custom graphs with lines and points.
    /// <para/> Contains SpaceVIL.PointsContainer and SpaceVIL.LinesContainer.
    /// <para/> Supports all events except drag and drop.
    /// </summary>
    public class Graph : Prototype
    {
        private static int count = 0;

        private PointsContainer points;
        private LinesContainer lines;
        /// <summary>
        /// Property to enable or disable mouse events (hover, click, press, scroll).
        /// <para/> True: Graph can receive mouse events. False: cannot receive mouse events.
        /// <para/> Default: False.
        /// </summary>
        public bool IsHover = false;

        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            if (IsHover)
                return base.GetHoverVerification(xpos, ypos);
            return false;
        }
        /// <summary>
        /// Default Graph constructor. The ability to get focus is disabled by default.
        /// </summary>
        public Graph()
        {
            SetItemName("Graph_" + count);
            SetBackground(Color.Transparent);
            count++;

            points = new PointsContainer();
            lines = new LinesContainer();

            IsFocusable = false;
        }
        /// <summary>
        /// Constructs Graph with the ability to enable or disable mouse events.
        /// </summary>
        /// <param name="hover">True: Graph can receive mouse events. 
        /// False: cannot receive mouse events.</param>
        public Graph(bool hover) : this()
        {
            IsHover = hover;
        }
        /// <summary>
        /// Initializing all elements of Graph.
        /// <para/> Notice: This method is mainly for overriding only. SpaceVIL calls 
        /// this method if necessary and no need to call it manually.
        /// </summary>
        public override void InitElements()
        {
            lines.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            points.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            AddItems(lines, points);
        }
        /// <summary>
        /// Setting thickness of Graph points.
        /// </summary>
        /// <param name="thickness">Point thickness.</param>
        public void SetPointThickness(float thickness)
        {
            points.SetPointThickness(thickness);
        }
        /// <summary>
        /// Getting Graph points thickness.
        /// </summary>
        /// <returns>Point thickness.</returns>
        public float GetPointThickness()
        {
            return points.GetPointThickness();
        }
        /// <summary>
        /// Setting Graph points color. Default: White.
        /// </summary>
        /// <param name="color">Points color.</param>
        public void SetPointColor(Color color)
        {
            points.SetPointColor(color);
        }
        /// <summary>
        /// Getting Graph points color.
        /// </summary>
        /// <returns>Points color.</returns>
        public Color GetPointColor()
        {
            return points.GetPointColor();
        }
        /// <summary>
        /// Setting custom shape for points (if one want to use other shape than circle).
        /// </summary>
        /// <param name="shape">Points list of the shape as List of float[2] array.</param>
        public void SetPointShape(List<float[]> shape)
        {
            points.SetPointShape(shape);
        }
        /// <summary>
        /// Getting current shape of points. Default: circle shape.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        public List<float[]> GetPointShape()
        {
            return points.GetPointShape();
        }
        /// <summary>
        /// Setting Graph points coordinates.
        /// </summary>
        /// <param name="coord">Points list as List of float[2] array.</param>
        public void SetPoints(List<float[]> coord)
        {
            points.SetPoints(coord);
            lines.SetPoints(coord);
        }
        /// <summary>
        /// Getting Graph points coordinates.
        /// </summary>
        /// <returns>Points list as List of float[2] array.</returns>
        public List<float[]> GetPoints()
        {
            return points.GetTriangles();
        }
        /// <summary>
        /// Setting Graph thickness of lines.
        /// </summary>
        /// <param name="thickness">Line thickness.</param>
        public void SetLineThickness(float thickness)
        {
            lines.SetLineThickness(thickness);
        }
        /// <summary>
        /// Getting Graph lines thickness.
        /// </summary>
        /// <returns>Lines thickness.</returns>
        public float GetLineThickness()
        {
            return lines.GetLineThickness();
        }
        /// <summary>
        /// Setting Graph lines color.
        /// </summary>
        /// <param name="color">Line color.</param>
        public void SetLineColor(Color color)
        {
            lines.SetLineColor(color);
        }
        /// <summary>
        /// Getting Graph lines color.
        /// </summary>
        /// <returns>Lines color.</returns>
        public Color GetLineColor()
        {
            return lines.GetLineColor();
        }
    }
}