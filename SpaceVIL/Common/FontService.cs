using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    /// <summary>
    /// FontService is a static class with static methods for working with colors, shapes and images.
    /// </summary>
    public static class FontService
    {
        private static void AddPreloadFont(Font font) {
            FontEngine.SavePreloadFont(font);
        }

        private static void RemovePreloadFont(Font font) {

        }

        private static Font[] GetPreloadFonts() {
            Font[] FontsList = new Font[] { };
            //read keys from _preloadDefFile
            return FontsList;
        }

        /// <summary>
        /// Changing font size.
        /// </summary>
        /// <param name="size">New size of the font.</param>
        /// <param name="oldFont">Font as System.Drawing.Font.</param>
        /// <returns>New sized font as System.Drawing.Font.</returns>
        public static Font ChangeFontSize(int size, Font oldFont)
        {
            return new Font(oldFont.FontFamily, size, oldFont.Style);
        }

        /// <summary>
        /// Changing font style.
        /// </summary>
        /// <param name="style">New style of the font.</param>
        /// <param name="oldFont">Font as System.Drawing.Font.</param>
        /// <returns>New styled font as System.Drawing.Font.</returns>
        public static Font ChangeFontStyle(FontStyle style, Font oldFont)
        {
            return new Font(oldFont.FontFamily, oldFont.Size, style);
        }

        /// <summary>
        /// Changing font family.
        /// </summary>
        /// <param name="fontFamily">New font family of the font.</param>
        /// <param name="oldFont">Font as System.Drawing.Font.</param>
        /// <returns>New font as System.Drawing.Font.</returns>
        public static Font ChangeFontFamily(FontFamily fontFamily, Font oldFont)
        {
            return new Font(fontFamily, oldFont.Size, oldFont.Style);
        }
    }
}
