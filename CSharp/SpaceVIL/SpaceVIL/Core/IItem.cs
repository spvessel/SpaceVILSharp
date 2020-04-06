using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes common item's properties.
    /// <para/> This interface is part of SpaceVIL.Core.IBaseItem.
    /// </summary>
    public interface IItem
    {
        /// <summary>
        /// Method for setting the name of the item.
        /// </summary>
        /// <param name="name">Item name as System.String.</param>
        void SetItemName(string name);
        /// <summary>
        /// Method for getting the name of the item.
        /// </summary>
        /// <returns>Item name as System.String.</returns>
        string GetItemName();
        /// <summary>
        /// Method for setting background color.
        /// </summary>
        /// <param name="color">Background color as System.Drawing.Color.</param>
        void SetBackground(Color color);
        /// <summary>
        /// Method for getting background color.
        /// </summary>
        /// <returns>Background color as System.Drawing.Color.</returns>
        Color GetBackground();
        /// <summary>
        /// Method for getting triangles of item's shape.
        /// </summary>
        /// <returns>Points list of the shape as List of float[2] array (2D).</returns>
        List<float[]> GetTriangles();
        /// <summary>
        /// Method for setting triangles as item's shape.
        /// </summary>
        /// <param name="triangles">Points list of the shape as List of float[2] array (2D).</param>
        void SetTriangles(List<float[]> triangles);
        /// <summary>
        /// Method for making default item's shape. Use in conjunction with 
        /// GetTriangles() and SetTriangles() methods.
        /// </summary>
        void MakeShape();
    }
}
