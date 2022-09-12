using System.Drawing;
using System.Reflection;
namespace View.Decorations
{
    public static class ImageResources
    {
        public static Bitmap Art = null;
        public static Bitmap SpaveVILLogo = null;

        public static void LoadImages()
        {
            Art = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("sandbox.Resources.images.art.jpg"));
            SpaveVILLogo = new Bitmap(Assembly.GetExecutingAssembly().GetManifestResourceStream("sandbox.Resources.images.spacevil_logo.png"));
        }
    }
}