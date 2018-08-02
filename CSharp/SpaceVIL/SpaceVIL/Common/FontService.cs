using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public static class FontService
    {
        public static void AddPreloadFont(Font font) {
            FontEngine.SavePreloadFont(font);
        }

        public static void RemovePreloadFont(Font font) {

        }

        public static Font[] GetPreloadFonts() {
            Font[] FontsList = new Font[] { };
            //read keys from _preloadDefFile
            return FontsList;
        }
    }
}
