using System;
using System.Runtime.InteropServices;

namespace OpenGL
{
    static internal class UnixGL
    {
        #region OpenGL_Basic_Functions
#if MAC
        public const string LIBRARY_OPENGL = "//System//Library//Frameworks//OpenGL.framework//Libraries//libGL.dylib";
#else
        public const string LIBRARY_OPENGL = "libGL.so.1";
#endif

        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glAccum(uint op, float value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glAlphaFunc(uint func, float ref_notkeword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern byte glAreTexturesResident(int n, uint[] textures, byte[] residences);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glArrayElement(int i);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBegin(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindTexture(uint target, uint texture);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, byte[] bitmap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBlendFunc(uint sfactor, uint dfactor);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCallList(uint list);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCallLists(int n, uint type, IntPtr lists);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCallLists(int n, uint type, uint[] lists);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCallLists(int n, uint type, byte[] lists);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClear(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearAccum(float red, float green, float blue, float alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearColor(float red, float green, float blue, float alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearDepth(double depth);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearIndex(float c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClearStencil(int s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glClipPlane(uint plane, double[] equation);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3b(byte red, byte green, byte blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3bv(byte[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3d(double red, double green, double blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3f(float red, float green, float blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3i(int red, int green, int blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3s(short red, short green, short blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3ub(byte red, byte green, byte blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3ubv(byte[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3ui(uint red, uint green, uint blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3uiv(uint[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3us(ushort red, ushort green, ushort blue);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor3usv(ushort[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4b(byte red, byte green, byte blue, byte alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4bv(byte[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4d(double red, double green, double blue, double alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4f(float red, float green, float blue, float alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4i(int red, int green, int blue, int alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4s(short red, short green, short blue, short alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4ub(byte red, byte green, byte blue, byte alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4ubv(byte[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4ui(uint red, uint green, uint blue, uint alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4uiv(uint[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4us(ushort red, ushort green, ushort blue, ushort alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColor4usv(ushort[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColorMask(byte red, byte green, byte blue, byte alpha);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColorMaterial(uint face, uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glColorPointer(int size, uint type, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCopyPixels(int x, int y, int width, int height, uint type);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCopyTexImage1D(uint target, int level, uint internalFormat, int x, int y, int width, int border);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCopyTexImage2D(uint target, int level, uint internalFormat, int x, int y, int width, int height, int border);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCopyTexSubImage1D(uint target, int level, int xoffset, int x, int y, int width);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCopyTexSubImage2D(uint target, int level, int xoffset, int yoffset, int x, int y, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCullFace(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteLists(uint list, int range);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteTextures(int n, uint[] textures);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDepthFunc(uint func);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDepthMask(byte flag);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDepthRange(double zNear, double zFar);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDisable(uint cap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDisableClientState(uint array);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawArrays(uint mode, int first, int count);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawBuffer(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawElements(uint mode, int count, uint type, IntPtr indices);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawElements(uint mode, int count, uint type, uint[] indices);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawPixels(int width, int height, uint format, uint type, float[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawPixels(int width, int height, uint format, uint type, uint[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawPixels(int width, int height, uint format, uint type, ushort[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawPixels(int width, int height, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawPixels(int width, int height, uint format, uint type, IntPtr pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEdgeFlag(byte flag);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEdgeFlagPointer(int stride, int[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEdgeFlagv(byte[] flag);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnable(uint cap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnableClientState(uint array);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnd();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEndList();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord1d(double u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord1dv(double[] u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord1f(float u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord1fv(float[] u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord2d(double u, double v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord2dv(double[] u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord2f(float u, float v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalCoord2fv(float[] u);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalMesh1(uint mode, int i1, int i2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalMesh2(uint mode, int i1, int i2, int j1, int j2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalPoint1(int i);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEvalPoint2(int i, int j);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFeedbackBuffer(int size, uint type, float[] buffer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFinish();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFlush();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFogf(uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFogfv(uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFogi(uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFogiv(uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFrontFace(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFrustum(double left, double right, double bottom, double top, double zNear, double zFar);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glGenLists(int range);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenTextures(int n, uint[] textures);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetBooleanv(uint pname, byte[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetClipPlane(uint plane, double[] equation);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetDoublev(uint pname, double[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glGetError();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetFloatv(uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetIntegerv(uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetLightfv(uint light, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetLightiv(uint light, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetMapdv(uint target, uint query, double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetMapfv(uint target, uint query, float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetMapiv(uint target, uint query, int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetMaterialfv(uint face, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetMaterialiv(uint face, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetPixelMapfv(uint map, float[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetPixelMapuiv(uint map, uint[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetPixelMapusv(uint map, ushort[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetPointerv(uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetPolygonStipple(byte[] mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public unsafe static extern sbyte* glGetString(uint name);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexEnvfv(uint target, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexEnviv(uint target, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexGendv(uint coord, uint pname, double[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexGenfv(uint coord, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexGeniv(uint coord, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexImage(uint target, int level, uint format, uint type, int[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexLevelParameterfv(uint target, int level, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexLevelParameteriv(uint target, int level, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexParameterfv(uint target, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGetTexParameteriv(uint target, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glHint(uint target, uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexMask(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexPointer(uint type, int stride, int[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexd(double c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexdv(double[] c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexf(float c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexfv(float[] c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexi(int c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexiv(int[] c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexs(short c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexsv(short[] c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexub(byte c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glIndexubv(byte[] c);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glInitNames();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glInterleavedArrays(uint format, int stride, int[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern byte glIsEnabled(uint cap);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern byte glIsList(uint list);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern byte glIsTexture(uint texture);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightModelf(uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightModelfv(uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightModeli(uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightModeliv(uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightf(uint light, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightfv(uint light, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLighti(uint light, uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLightiv(uint light, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLineStipple(int factor, ushort pattern);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLineWidth(float width);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glListBase(uint base_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLoadIdentity();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLoadMatrixd(double[] m);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLoadMatrixf(float[] m);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLoadName(uint name);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLogicOp(uint opcode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMap1d(uint target, double u1, double u2, int stride, int order, double[] points);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMap1f(uint target, float u1, float u2, int stride, int order, float[] points);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMap2d(uint target, double u1, double u2, int ustride, int uorder, double v1, double v2, int vstride, int vorder, double[] points);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMap2f(uint target, float u1, float u2, int ustride, int uorder, float v1, float v2, int vstride, int vorder, float[] points);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMapGrid1d(int un, double u1, double u2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMapGrid1f(int un, float u1, float u2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMapGrid2d(int un, double u1, double u2, int vn, double v1, double v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMapGrid2f(int un, float u1, float u2, int vn, float v1, float v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMaterialf(uint face, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMaterialfv(uint face, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMateriali(uint face, uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMaterialiv(uint face, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMatrixMode(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMultMatrixd(double[] m);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glMultMatrixf(float[] m);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNewList(uint list, uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3b(byte nx, byte ny, byte nz);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3bv(byte[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3d(double nx, double ny, double nz);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3f(float nx, float ny, float nz);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3i(int nx, int ny, int nz);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3s(short nx, short ny, short nz);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormal3sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormalPointer(uint type, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glNormalPointer(uint type, int stride, float[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPassThrough(float token);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelMapfv(uint map, int mapsize, float[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelMapuiv(uint map, int mapsize, uint[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelMapusv(uint map, int mapsize, ushort[] values);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelStoref(uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelStorei(uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelTransferf(uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelTransferi(uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPixelZoom(float xfactor, float yfactor);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPointSize(float size);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPolygonMode(uint face, uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPolygonOffset(float factor, float units);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPolygonStipple(byte[] mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPopAttrib();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPopClientAttrib();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPopMatrix();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPopName();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPrioritizeTextures(int n, uint[] textures, float[] priorities);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPushAttrib(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPushClientAttrib(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPushMatrix();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glPushName(uint name);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2d(double x, double y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2f(float x, float y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2i(int x, int y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2s(short x, short y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos2sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3d(double x, double y, double z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3f(float x, float y, float z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3i(int x, int y, int z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3s(short x, short y, short z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos3sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4d(double x, double y, double z, double w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4f(float x, float y, float z, float w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4i(int x, int y, int z, int w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4s(short x, short y, short z, short w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRasterPos4sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glReadBuffer(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glReadPixels(int x, int y, int width, int height, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glReadPixels(int x, int y, int width, int height, uint format, uint type, IntPtr pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectd(double x1, double y1, double x2, double y2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectdv(double[] v1, double[] v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectf(float x1, float y1, float x2, float y2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectfv(float[] v1, float[] v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRecti(int x1, int y1, int x2, int y2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectiv(int[] v1, int[] v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRects(short x1, short y1, short x2, short y2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRectsv(short[] v1, short[] v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern int glRenderMode(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRotated(double angle, double x, double y, double z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glRotatef(float angle, float x, float y, float z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glScaled(double x, double y, double z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glScalef(float x, float y, float z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glScissor(int x, int y, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glSelectBuffer(int size, uint[] buffer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glShadeModel(uint mode);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilFunc(uint func, int ref_notkeword, uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilMask(uint mask);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glStencilOp(uint fail, uint zfail, uint zpass);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1d(double s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1f(float s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1i(int s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1s(short s);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord1sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2d(double s, double t);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2f(float s, float t);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2i(int s, int t);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2s(short s, short t);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord2sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3d(double s, double t, double r);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3f(float s, float t, float r);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3i(int s, int t, int r);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3s(short s, short t, short r);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord3sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4d(double s, double t, double r, double q);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4f(float s, float t, float r, float q);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4i(int s, int t, int r, int q);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4s(short s, short t, short r, short q);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoord4sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoordPointer(int size, uint type, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexCoordPointer(int size, uint type, int stride, float[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexEnvf(uint target, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexEnvfv(uint target, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexEnvi(uint target, uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexEnviv(uint target, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGend(uint coord, uint pname, double param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGendv(uint coord, uint pname, double[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGenf(uint coord, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGenfv(uint coord, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGeni(uint coord, uint pname, int param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexGeniv(uint coord, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexImage1D(uint target, int level, uint internalformat, int width, int border, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexImage2D(uint target, int level, uint internalformat, int width, int height, int border, uint format, uint type, IntPtr pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameterf(uint target, uint pname, float param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameterfv(uint target, uint pname, float[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameteri(uint target, uint pname, uint param);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameteriv(uint target, uint pname, int[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage1D(uint target, int level, int xoffset, int width, uint format, uint type, int[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexStorage2D(uint target, int level, uint internalformat, int width, int height);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTranslated(double x, double y, double z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTranslatef(float x, float y, float z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2d(double x, double y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2f(float x, float y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2i(int x, int y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2s(short x, short y);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex2sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3d(double x, double y, double z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3f(float x, float y, float z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3i(int x, int y, int z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3s(short x, short y, short z);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex3sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4d(double x, double y, double z, double w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4dv(double[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4f(float x, float y, float z, float w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4fv(float[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4i(int x, int y, int z, int w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4iv(int[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4s(short x, short y, short z, short w);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertex4sv(short[] v);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexPointer(int size, uint type, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexPointer(int size, uint type, int stride, short[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexPointer(int size, uint type, int stride, int[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexPointer(int size, uint type, int stride, float[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexPointer(int size, uint type, int stride, double[] pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glViewport(int x, int y, int width, int height);
        #endregion

        //OpenGL Extensions Functions
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern int glGetUniformLocation(uint shader, string value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glCreateShader(uint type);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glShaderSource(uint shader, int count, string[] source, int[] length);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCompileShader(uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern uint glCreateProgram();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glAttachShader(uint program, uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glLinkProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenVertexArrays(int n, uint[] arrays);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindVertexArray(uint array);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUseProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDetachShader(uint program, uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteShader(uint shader);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteVertexArrays(int n, uint[] arrays);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteProgram(uint program);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenBuffers(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenFramebuffersEXT(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBlendFuncSeparate(uint srcRGB, uint dstRGB, uint srcAlpha, uint dstAlpha);
        static public void glGenFramebuffers(int n, uint[] buffers)
        {
            glGenFramebuffersEXT(n, buffers);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenRenderbuffersEXT(int n, uint[] buffers);
        static public void glGenRenderbuffers(int n, uint[] buffers)
        {
            glGenRenderbuffersEXT(n, buffers);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindBuffer(uint target, uint buffer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBindFramebufferEXT(uint target, uint buffer);
        static public void glBindFramebuffer(uint target, uint buffers)
        {
            glBindFramebufferEXT(target, buffers);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenRenderbuffersEXT(uint target, uint buffer);
        static public void glGenRenderbuffers(uint target, uint buffers)
        {
            glGenRenderbuffersEXT(target, buffers);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glBufferData(uint target, int size, IntPtr data, uint usage);
        public static void glBufferData(uint target, float[] data, uint usage)
        {
            IntPtr vp = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, vp, data.Length);
            glBufferData(target, data.Length * sizeof(float), vp, usage);
            Marshal.FreeHGlobal(vp);
        }
        public static void glBufferData(uint target, int[] data, uint usage)
        {
            IntPtr vp = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, vp, data.Length);
            glBufferData(target, data.Length * sizeof(float), vp, usage);
            Marshal.FreeHGlobal(vp);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glVertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDisableVertexAttribArray(uint index);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glEnableVertexAttribArray(uint index);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteBuffers(int n, uint[] buffers);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteFramebuffersEXT(int n, uint[] buffers);
        static public void glDeleteFramebuffers(int n, uint[] buffers)
        {
            glDeleteFramebuffersEXT(n, buffers);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDeleteRenderbuffersEXT(int n, uint[] buffers);
        static public void glDeleteRenderbuffers(int n, uint[] buffers)
        {
            glDeleteRenderbuffersEXT(n, buffers);
        }

        //Work with Textures
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glGenerateMipmap(uint target);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glActiveTexture(uint texture);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1f(int location, float v0);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform2f(int location, float v0, float v1);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform3f(int location, float v0, float v1, float v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform4f(int location, float v0, float v1, float v2, float v3);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1i(int location, int v0);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform2i(int location, int v0, int v1);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform3i(int location, int v0, int v1, int v2);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform4i(int location, int v0, int v1, int v2, int v3);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform2fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform3fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform4fv(int location, int count, float[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform1iv(int location, int count, int[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform2iv(int location, int count, int[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform3iv(int location, int count, int[] value);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glUniform4iv(int location, int count, int[] value);

        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFramebufferTextureEXT(uint target, uint attachment, uint texture, int level);
        static public void glFramebufferTexture(uint target, uint attachment, uint texture, int level)
        {
            glFramebufferTextureEXT(target, attachment, texture, level);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glFramebufferTexture2DEXT(uint target, uint attachment, uint textarget, uint texture, int level);
        static public void glFramebufferTexture2D(uint target, uint attachment, uint textarget, uint texture, int level)
        {
            glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
        }
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glDrawBuffers(int n, uint[] bufs);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glCheckFramebufferStatusEXT(uint target);
        static public void glCheckFramebufferStatus(uint target)
        {
            glCheckFramebufferStatusEXT(target);
        }
    }
}
