using System;
using System.Drawing;
using SpaceVIL;

namespace View
{
    internal class MyMenuItem : MenuItem
    {
        internal MyMenuItem() 
        {
            SetFont(new Font(new FontFamily("Arial"), 16, FontStyle.Italic));
        }
    }
}