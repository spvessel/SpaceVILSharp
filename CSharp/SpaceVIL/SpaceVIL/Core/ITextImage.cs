namespace SpaceVIL.Core
{
    public interface ITextImage
    {
        byte[] GetBytes();
        bool IsEmpty();
        int GetWidth();
        int GetHeight();
        int GetXOffset();
        int GetYOffset();
    }
}