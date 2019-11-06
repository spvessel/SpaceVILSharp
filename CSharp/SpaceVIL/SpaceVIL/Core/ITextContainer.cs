using System.Drawing;

namespace SpaceVIL.Core
{
    public interface ITextContainer
    {
        ITextImage GetTexture();
        Color GetForeground();
    }
}
