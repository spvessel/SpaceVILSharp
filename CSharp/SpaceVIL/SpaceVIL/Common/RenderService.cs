using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    /// <summary>
    /// RenderService is static class providing methods to managing OpenGL attributes such as settings and viewport used by SpaceVIL.
    /// <para/> Tip: RenderService is usualy used with SpaceVIL.Core.IOpenGLLayer.
    /// </summary>
    public static class RenderService
    {
        /// <summary>
        /// Restoring initial SpaceVIL OpenGL settings for the specified window (if they have been changed).
        /// </summary>
        /// <param name="window">A window as SpaceVIL.CoreWindow.</param>
        public static void RestoreCommonGLSettings(CoreWindow window)
        {
            WindowsBox.RestoreCommonGLSettings(window);
        }
        /// <summary>
        /// Restoring initial OpenGL viewport of SpaceVIL environment 
        /// for the specified window (if it was changed).
        /// </summary>
        /// <param name="window">A window as SpaceVIL.CoreWindow.</param>
        public static void RestoreViewport(CoreWindow window)
        {
            WindowsBox.RestoreViewport(window);
        }
        /// <summary>
        /// Setting custom viewport by the specified window and SpaceVIL.Core.IOpenGLLayer.
        /// </summary>
        /// <param name="window">A window as SpaceVIL.CoreWindow.</param>
        /// <param name="layer">An item that extends Prototype and implements SpaceVIL.Core.IOpenGLLayer.</param>
        public static void SetGLLayerViewport(CoreWindow window, IOpenGLLayer layer)
        {
            WindowsBox.SetGLLayerViewport(window, layer);
        }
    }
}