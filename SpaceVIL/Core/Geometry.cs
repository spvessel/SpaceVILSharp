using SpaceVIL.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Class Geometry describes all geometry properties of the item.
    /// </summary>
    public class Geometry : ISize
    {
        //width
        private int _minWidth = 0;
        private int _width = 0;
        private int _maxWidth = SpaceVILConstants.SizeMaxValue; //glfw dont like Int32.MaxValue

        /// <summary>
        /// Setting item width. If the value is greater/less than the maximum/minimum 
        /// value of the width, then the width becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        public void SetWidth(int width)
        {
            if (width < 0)
            {
                _width = 0;
                return;
            }

            if (width < _minWidth)
            {
                width = _minWidth;
            }
            if (width > _maxWidth)
            {
                width = _maxWidth;
            }

            _width = width;
        }

        /// <summary>
        /// Getting item width.
        /// </summary>
        /// <returns> Width of the item. </returns>
        public int GetWidth()
        {
            return _width;
        }

        /// <summary>
        /// Setting the minimum width limit. Actual width cannot be less than this limit.
        /// </summary>
        /// <param name="width"> Minimum width limit of the item. </param>
        public void SetMinWidth(int width)
        {
            if (width < 0)
            {
                _minWidth = 0;
                return;
            }

            if (width > _width)
            {
                _width = width;
            }
            if (width > _maxWidth)
            {
                _maxWidth = width;
            }

            _minWidth = width;
        }

        /// <summary>
        /// Getting the minimum width limit.
        /// </summary>
        /// <returns> Minimum width limit of the item. </returns>
        public int GetMinWidth()
        {
            return _minWidth;
        }

        /// <summary>
        /// Setting the maximum width limit. Actual width cannot be greater than this limit.
        /// </summary>
        /// <param name="width"> Maximum width limit of the item. </param>
        public void SetMaxWidth(int width)
        {
            if (width < 0)
            {
                _maxWidth = 0;
                return;
            }

            if (width < _width)
            {
                _width = width;
            }
            if (width < _minWidth)
            {
                _minWidth = width;
            }

            _maxWidth = width;
        }

        /// <summary>
        /// Getting the maximum width limit.
        /// </summary>
        /// <returns> Maximum width limit of the item. </returns>
        public int GetMaxWidth()
        {
            return _maxWidth;
        }

        //height
        private int _minHeight = 0;
        private int _height = 0;
        private int _maxHeight = SpaceVILConstants.SizeMaxValue;//glfw dont like Int32.MaxValue

        /// <summary>
        /// Setting item height. If the value is greater/less than the maximum/minimum 
        /// value of the height, then the height becomes equal to the maximum/minimum value.
        /// </summary>
        /// <param name="height"> Height of the item. </param>
        public void SetHeight(int height)
        {
            if (height < 0)
            {
                _height = 0;
                return;
            }

            if (height < _minHeight)
            {
                height = _minHeight;
            }
            if (height > _maxHeight)
            {
                height = _maxHeight;
            }

            _height = height;
        }

        /// <summary>
        /// Getting item height.
        /// </summary>
        /// <returns> Height of the item. </returns>
        public int GetHeight()
        {
            return _height;
        }

        /// <summary>
        /// Setting the minimum height limit. Actual height cannot be less than this limit.
        /// </summary>
        /// <param name="height"> Minimum height limit of the item. </param>
        public void SetMinHeight(int height)
        {
            if (height < 0)
            {
                _minHeight = 0;
                return;
            }

            if (height > _height)
            {
                _height = height;
            }
            if (height > _maxHeight)
            {
                _maxHeight = height;
            }

            _minHeight = height;
        }

        /// <summary>
        /// Getting the minimum height limit.
        /// </summary>
        /// <returns> Minimum height limit of the item. </returns>
        public int GetMinHeight()
        {
            return _minHeight;
        }

        /// <summary>
        /// Setting the maximum height limit. Actual height cannot be greater than this limit.
        /// </summary>
        /// <param name="height"> Maximum height limit of the item. </param>
        public void SetMaxHeight(int height)
        {
            if (height < 0)
            {
                _maxHeight = SpaceVILConstants.SizeMaxValue;
                return;
            }

            if (height < _height)
            {
                _height = height;
            }
            if (height < _minHeight)
            {
                _minHeight = height;
            }

            _maxHeight = height;
        }

        /// <summary>
        /// Getting the maximum height limit.
        /// </summary>
        /// <returns> Maximum height limit of the item. </returns>
        public int GetMaxHeight()
        {
            return _maxHeight;
        }

        /// <summary>
        /// Setting item size (width and height).
        /// </summary>
        /// <param name="width"> Width of the item. </param>
        /// <param name="height"> Height of the item. </param>
        public void SetSize(int width, int height)
        {
            SetWidth(width);
            SetHeight(height);
        }

        /// <summary>
        /// Getting current item size.
        /// </summary>
        /// <returns>Item size as SpaceVIL.Core.Size.</returns>
        public Size GetSize()
        {
            return new Size(_width, _height);
        }
    }
}
