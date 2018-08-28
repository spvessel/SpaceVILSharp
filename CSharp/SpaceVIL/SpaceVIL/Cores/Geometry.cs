using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public class Geometry : IGeometry, ISize
    {
        //width
        private int _minWidth = 0;
        private int _width = 0;
        private int _maxWidth = Int16.MaxValue;//glfw dont like Int32.MaxValue
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
        public int GetWidth()
        {
            return _width;
        }
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
        public int GetMinWidth()
        {
            return _minWidth;
        }
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
        public int GetMaxWidth()
        {
            return _maxWidth;
        }

        //height
        private int _minHeight = 0;
        private int _height = 0;
        private int _maxHeight = Int16.MaxValue;//glfw dont like Int32.MaxValue
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
        public int GetHeight()
        {
            return _height;
        }
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
        public int GetMinHeight()
        {
            return _minHeight;
        }
        public void SetMaxHeight(int height)
        {
            if (height < 0)
            {
                _maxHeight = Int32.MaxValue;
                return;
            }

            if (height < _height)
                _height = height;
            if (height < _minHeight)
                _minHeight = height;

            _maxHeight = height;
        }
        public int GetMaxHeight()
        {
            return _maxHeight;
        }

        //size
        public void SetSize(int width, int height)
        {
            SetWidth(width);
            SetHeight(height);
        }
        public int[] GetSize()
        {
            return new int[] { _width, _height };
        }
    }
}
