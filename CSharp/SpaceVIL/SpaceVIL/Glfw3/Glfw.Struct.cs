namespace Glfw3
{
    using System;
    using System.Runtime.InteropServices;

    internal static partial class Glfw
    {
        [StructLayout(LayoutKind.Sequential)]
        internal struct GammaRamp
        {
            [MarshalAs(UnmanagedType.LPArray)]
            internal ushort[] Red;
            [MarshalAs(UnmanagedType.LPArray)]
            internal ushort[] Green;
            [MarshalAs(UnmanagedType.LPArray)]
            internal ushort[] Blue;
            internal uint Size => (uint)Math.Min(Red.Length, Math.Min(Green.Length, Blue.Length));
        }

        [StructLayout(LayoutKind.Sequential)]
        internal unsafe struct InternalGammaRamp
        {
            internal ushort* Red;
            internal ushort* Green;
            internal ushort* Blue;
            internal uint Size;
        }

        internal struct Image
        {
            internal int Width;
            internal int Height;
            internal byte[] Pixels;
        }

        [StructLayout(LayoutKind.Sequential)]
        internal struct InternalImage
        {
            internal int Width;
            internal int Height;
            internal IntPtr Pixels;
        }

        [StructLayout(LayoutKind.Sequential)]
        internal struct Monitor : IEquatable<Monitor>
        {
            internal static readonly Monitor None = new Monitor(IntPtr.Zero);
            internal IntPtr Ptr;

            internal Monitor(IntPtr ptr)
            {
                Ptr = ptr;
            }

            public override bool Equals(object obj)
            {
                if (obj is Monitor)
                    return Equals((Monitor)obj);

                return false;
            }

            public bool Equals(Monitor obj) => Ptr == obj.Ptr;

            public override string ToString() => Ptr.ToString();

            public override int GetHashCode() => Ptr.GetHashCode();

            public static bool operator ==(Monitor a, Monitor b) => a.Equals(b);

            public static bool operator !=(Monitor a, Monitor b) => !a.Equals(b);

            public static implicit operator bool(Monitor obj) => obj.Ptr != IntPtr.Zero;
        }

        [StructLayout(LayoutKind.Sequential)]
        internal struct VideoMode : IEquatable<VideoMode>
        {
            internal int Width;
            internal int Height;
            internal int RedBits;
            internal int GreenBits;
            internal int BlueBits;
            internal int RefreshRate;

            public override bool Equals(object obj)
            {
                if (obj is VideoMode)
                    return Equals((VideoMode)obj);

                return false;
            }

            public bool Equals(VideoMode obj)
            {
                return obj.Width == Width
                    && obj.Height == Height
                    && obj.RedBits == RedBits
                    && obj.GreenBits == GreenBits
                    && obj.BlueBits == BlueBits
                    && obj.RefreshRate == RefreshRate;
            }

            public override string ToString()
            {
                return string.Format("VideoMode(width: {0}, height: {1}, redBits: {2}, greenBits: {3}, blueBits: {4}, refreshRate: {5})",
                    Width.ToString(),
                    Height.ToString(),
                    RedBits.ToString(),
                    GreenBits.ToString(),
                    BlueBits.ToString(),
                    RefreshRate.ToString()
                );
            }

            public override int GetHashCode()
            {
                unchecked
                {
                    int hash = 17;
                    hash = hash * 23 + Width.GetHashCode();
                    hash = hash * 23 + Height.GetHashCode();
                    hash = hash * 23 + RedBits.GetHashCode();
                    hash = hash * 23 + GreenBits.GetHashCode();
                    hash = hash * 23 + BlueBits.GetHashCode();
                    hash = hash * 23 + RefreshRate.GetHashCode();
                    return hash;
                }
            }

            public static bool operator ==(VideoMode a, VideoMode b) => a.Equals(b);

            public static bool operator !=(VideoMode a, VideoMode b) => !a.Equals(b);
        }

        [StructLayout(LayoutKind.Sequential)]
        internal struct Cursor : IEquatable<Cursor>
        {
            public static readonly Cursor None = new Cursor(IntPtr.Zero);

            public IntPtr Ptr;

            internal Cursor(IntPtr ptr)
            {
                Ptr = ptr;
            }

            public override bool Equals(object obj)
            {
                if (obj is Cursor)
                    return Equals((Cursor)obj);
                return false;
            }

            public bool Equals(Cursor obj) => Ptr == obj.Ptr;

            public override string ToString() => Ptr.ToString();

            public override int GetHashCode() => Ptr.GetHashCode();

            public static bool operator ==(Cursor a, Cursor b) => a.Equals(b);

            public static bool operator !=(Cursor a, Cursor b) => !a.Equals(b);

            public static implicit operator bool(Cursor obj) => obj.Ptr != IntPtr.Zero;
        }
    }
}
