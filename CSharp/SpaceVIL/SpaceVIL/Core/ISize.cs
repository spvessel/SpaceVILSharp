using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes the attributes of the size of a shape.
    /// <para/> This interface is part of SpaceVIL.Core.IBaseItem.
    /// </summary>
    public interface ISize
    {
        /// <summary>
        /// Method setting size of an item's shape.
        /// </summary>
        /// <param name="width">Width of a shape.</param>
        /// <param name="height">Height of a shape.</param>
        void SetSize(int width, int height);
        /// <summary>
        /// Method getting size of an item's shape.
        /// </summary>
        /// <returns>Size of the shape as SpaceVIL.Core.Size.</returns>
        Size GetSize();
        /// <summary>
        /// Method setting the minimum width limit. Actual width cannot be less than this limit.
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        void SetMinWidth(int width);
        /// <summary>
        /// Method setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width">Width of the item.</param>
        void SetWidth(int width);
        /// <summary>
        /// Method setting the maximum width limit. Actual width cannot be greater than this limit.
        /// </summary>
        /// <param name="width">Maximum width limit of the item.</param>
        void SetMaxWidth(int width);
        /// <summary>
        /// Method getting the minimum width limit.
        /// </summary>
        /// <returns> Minimum width limit of the item. </returns>
        int GetMinWidth();
        /// <summary>
        /// Method fetting item width.
        /// </summary>
        /// <returns> Width of the item. </returns>
        int GetWidth();
        /// <summary>
        /// Method getting the maximum width limit.
        /// </summary>
        /// <returns> Maximum width limit of the item. </returns>
        int GetMaxWidth();
        /// <summary>
        ///  Method for setting the minimum height limit. Actual height cannot be less than this limit.
        /// </summary>
        /// <param name="height"> Minimum height limit of the item. </param>
        void SetMinHeight(int height);
        /// <summary>
        /// Method for setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        void SetHeight(int height);
        /// <summary>
        ///  Method for setting the maximum height limit. Actual height cannot be greater than this limit.
        /// </summary>
        /// <param name="height"> Maximum height limit of the item. </param>
        void SetMaxHeight(int height);
        /// <summary>
        /// Method for getting the minimum height limit.
        /// </summary>
        /// <returns> Minimum height limit of the item. </returns>
        int GetMinHeight();
        /// <summary>
        /// Method for getting item height.
        /// </summary>
        /// <returns> Height of the item. </returns>
        int GetHeight();
        /// <summary>
        /// Method for getting the maximum height limit.
        /// </summary>
        /// <returns> Maximum height limit of the item. </returns>
        int GetMaxHeight();
    }
}
