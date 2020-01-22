using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    public static class RenderService
    {
        public static void RestoreCommonGLSettings(CoreWindow window)
        {
            WindowsBox.RestoreCommonGLSettings(window);
        }

        public static void RestoreViewport(CoreWindow window)
        {
            WindowsBox.RestoreViewport(window);
        }

        public static void SetGLLayerViewport(CoreWindow window, IOpenGLLayer layer)
        {
            WindowsBox.SetGLLayerViewport(window, layer);
        }
    }
}