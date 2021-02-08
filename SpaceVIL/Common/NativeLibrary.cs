using System;
using System.IO;
using System.Reflection;
using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    internal static class NativeLibrary
    {
        private static bool isLoaded = false;

        internal static void ExtractEmbeddedLibrary()
        {
            String glfw = GetNativeLibraryName("glfw3");

            if (File.Exists(glfw))
            {
                return;
            }

            String prefix = "";
            if (CommonService.GetOSType() == OSType.Mac) {
                prefix = "macos.";
            } else if (CommonService.GetOSType() == OSType.Linux) {
                prefix = "linux.";
            } else if (CommonService.GetOSType() == OSType.Windows) {
                prefix = "windows.";
            }

            var resource = Assembly.GetExecutingAssembly().GetManifestResourceStream("SpaceVIL.Native." + prefix + glfw);

            using (FileStream outputFileStream = new FileStream(glfw, FileMode.Create))
            {
                resource.CopyTo(outputFileStream);
            }
        }

        private static string GetNativeLibraryName(String library)
        {
            if (CommonService.GetOSType() == OSType.Mac)
            {
                return "lib" + library.ToString().ToLower() + ".dylib";
            }
            else if (CommonService.GetOSType() == OSType.Linux)
            {
                return "lib" + library.ToString().ToLower() + ".so";
            }
            else if (CommonService.GetOSType() == OSType.Windows)
            {
                return library.ToString().ToLower() + ".dll";
            }

            return null;
        }
    }
}