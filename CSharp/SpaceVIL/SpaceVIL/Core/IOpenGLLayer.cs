namespace SpaceVIL.Core
{
    public interface IOpenGLLayer
    {
        void Initialize();
        bool IsInitialized();
        void Draw();
        void Free();
    }
}