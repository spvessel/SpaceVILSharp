using SpaceVIL.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// Class Geometry describes all geometry properties of the item
    /// </summary>
    public class Geometry : ISize
    {
        //width
        private int _minWidth = 0;
        private int _width = 0;
        private int _maxWidth = SpaceVILConstants.SizeMaxValue; //glfw dont like Int32.MaxValue

        /// <param name="width"> width width of the item </param>
        public void SetWidth(int width)
        {
            if (width < 0)
            {
                _width = 0;
                return;
            }

            if (width < _minWidth)
                width = _minWidth;
            if (width > _maxWidth)
                width = _maxWidth;

            _width = width;
        }

        /// <returns> width of the item </returns>
        public int GetWidth()
        {
            return _width;
        }

        /// <param name="width"> width minimum width of the item </param>
        public void SetMinWidth(int width)
        {
            if (width < 0)
            {
                _minWidth = 0;
                return;
            }

            if (width > _width)
                _width = width;
            if (width > _maxWidth)
                _maxWidth = width;

            _minWidth = width;
        }

        /// <returns> minimum width of the item </returns>
        public int GetMinWidth()
        {
            return _minWidth;
        }

        /// <param name="width"> width maximum width of the item </param>
        public void SetMaxWidth(int width)
        {
            if (width < 0)
            {
                _maxWidth = 0;
                return;
            }

            if (width < _width)
                _width = width;
            if (width < _minWidth)
                _minWidth = width;

            _maxWidth = width;
        }

        /// <returns> maximum width of the item </returns>
        public int GetMaxWidth()
        {
            return _maxWidth;
        }

        //height
        private int _minHeight = 0;
        private int _height = 0;
        private int _maxHeight = SpaceVILConstants.SizeMaxValue;//glfw dont like Int32.MaxValue

        /// <param name="height"> height height of the item </param>
        public void SetHeight(int height)
        {
            if (height < 0)
            {
                _height = 0;
                return;
            }

            if (height < _minHeight)
                height = _minHeight;
            if (height > _maxHeight)
                height = _maxHeight;

            _height = height;
        }

        /// <returns> height of the item </returns>
        public int GetHeight()
        {
            return _height;
        }

        /// <param name="height"> minimum height of the item </param>
        public void SetMinHeight(int height)
        {
            if (height < 0)
            {
                _minHeight = 0;
                return;
            }

            if (height > _height)
                _height = height;
            if (height > _maxHeight)
                _maxHeight = height;

            _minHeight = height;
        }

        /// <returns> minimum height of the item </returns>
        public int GetMinHeight()
        {
            return _minHeight;
        }

        /// <param name="height"> maximum height of the item </param>
        public void SetMaxHeight(int height)
        {
            if (height < 0)
            {
                _maxHeight = SpaceVILConstants.SizeMaxValue;
                return;
            }

            if (height < _height)
                _height = height;
            if (height < _minHeight)
                _minHeight = height;

            _maxHeight = height;
        }

        /// <returns> maximum height of the item </returns>
        public int GetMaxHeight()
        {
            return _maxHeight;
        }

        //size
        /// <summary>
        /// Set size (width and height) of the item
        /// </summary>
        /// <param name="width"> width of the item </param>
        /// <param name="height"> height of the item </param>
        public void SetSize(int width, int height)
        {
            SetWidth(width);
            SetHeight(height);
        }

        /// <returns> width amd height of the item </returns>
        public Size GetSize()
        {
            return new Size(_width, _height);
        }
    }
}
