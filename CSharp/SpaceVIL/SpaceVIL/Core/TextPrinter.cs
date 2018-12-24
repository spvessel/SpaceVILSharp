using System;
using System.Drawing;
using System.IO;

namespace SpaceVIL.Core
{
    public class TextPrinter
    {
        internal byte[] Texture = null;
        internal int XTextureShift = 0;
        internal int YTextureShift = 0;
        internal int WidthTexture = 0;
        internal int HeightTexture = 0;

        internal TextPrinter()
        {
            Texture = null;
        }

        internal TextPrinter(byte[] bb)
        {
            Texture = bb;
        }
    }
}