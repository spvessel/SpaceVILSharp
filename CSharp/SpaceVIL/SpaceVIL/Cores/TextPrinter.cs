using System;
using System.IO;

namespace SpaceVIL
{
    internal class TextPrinter
    {
        public byte[] Texture = null;
        public int XTextureShift = 0;
        public int YTextureShift = 0;
        public int WidthTexture = 0;
        public int HeightTexture = 0;

        public TextPrinter()
        {
            Texture = null;
        }

        internal TextPrinter(byte[] bb)
        {
            Texture = bb;
        }
    }
}