namespace Glfw3
{
    using System;
    using System.Runtime.InteropServices;
    using System.Text;

    internal static partial class Glfw
    {

        const string kLibrary = "glfw";

        internal readonly static int DontCare = -1;
        internal readonly static int VersionMajor = 3;
        internal readonly static int VersionMinor = 2;
        internal readonly static int VersionRevision = 1;

        internal static IntPtr ToUTF8(string text)
        {
            int len = Encoding.UTF8.GetByteCount(text);
            byte[] buffer = new byte[len + 1];
            Encoding.UTF8.GetBytes(text, 0, text.Length, buffer, 0);
            var nativeUtf8 = Marshal.AllocHGlobal(buffer.Length);
            Marshal.Copy(buffer, 0, nativeUtf8, buffer.Length);
            return nativeUtf8;
        }

        internal static string FromUTF8(IntPtr ptr)
        {
            int len = 0;
            while (Marshal.ReadByte(ptr, len) != 0)
                ++len;
            byte[] buffer = new byte[len];
            Marshal.Copy(ptr, buffer, 0, buffer.Length);
            return Encoding.UTF8.GetString(buffer);
        }
    }
}
