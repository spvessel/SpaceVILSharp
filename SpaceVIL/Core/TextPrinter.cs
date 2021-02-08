namespace SpaceVIL.Core
{
    internal class TextPrinter : ITextImage
    {
        private byte[] texture = null;
        private int xShift = 0;
        private int yShift = 0;
        private int width = 0;
        private int height = 0;

        internal TextPrinter()
        {
            texture = null;
        }

        internal TextPrinter(byte[] bb)
        {
            texture = bb;
        }

        internal void SetAttr(int width, int height, int xOffset, int yOffset)
        {
            this.width = width;
            this.height = height;
            xShift = xOffset;
            yShift = yOffset;
        }

        internal void SetPosition(int xOffset, int yOffset)
        {
            xShift = xOffset;
            yShift = yOffset;
        }

        internal void SetXOffset(int x)
        {
            xShift = x;
        }
        
        internal void SetYOffset(int y)
        {
            yShift = y;
        }

        internal void SetSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }

        public byte[] GetBytes()
        {
            return texture;
        }

        public int GetWidth()
        {
            return width;
        }

        public int GetHeight()
        {
            return height;
        }

        public int GetXOffset()
        {
            return xShift;
        }

        public int GetYOffset()
        {
            return yShift;
        }

        public bool IsEmpty()
        {
            if (texture == null || texture.Length == 0)
                return true;
            return false;
        }
    }
}