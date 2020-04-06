using System.Drawing;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes texture for text rendering.
    /// </summary>
    public interface ITextContainer
    {
        /// <summary>
        /// Method for getting text texture.
        /// </summary>
        /// <returns>Texture as SpaceVIL.Core.ITextImage.</returns>
        ITextImage GetTexture();
        /// <summary>
        /// Method for getting text color.
        /// </summary>
        /// <returns>Text color as System.Drawing.Color.</returns>
        Color GetForeground();
    }
}
