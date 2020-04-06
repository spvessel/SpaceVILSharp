namespace SpaceVIL.Core
{
    /// <summary>
    /// Interface for providing OpenGL within SpaceVIL environment.
    /// </summary>
    public interface IOpenGLLayer
    {
        /// <summary>
        /// Method for initializing OpenGL components before drawing (shaders, vbo and etc.).
        /// </summary>
        void Initialize();
        /// <summary>
        /// Method for checking initializing status of current OpenGL item.
        /// </summary>
        /// <returns>Should be True: if an item is already initialized. Should be False: if an item is not initialed yed.</returns>
        bool IsInitialized();
        /// <summary>
        /// Method to discribe logic of drawing OpenGL objects.
        /// </summary>
        void Draw();
        /// <summary>
        /// Method to describe disposing OpenGL resources if the item was removed.
        /// </summary>
        void Free();
    }
}