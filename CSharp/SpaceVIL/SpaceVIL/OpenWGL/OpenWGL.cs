using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.ComponentModel;
using System.Runtime.InteropServices;
using System.Reflection;

namespace GL.WGL
{
    public partial class OpenWGL
    {
        #region OpenGL_Basic_Functions
        public const string LIBRARY_OPENGL = "opengl32.dll";

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
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexEnvi(uint target, uint pname, uint param);
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
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexParameteriv(uint target, uint pname, uint[] params_notkeyword);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage1D(uint target, int level, int xoffset, int width, uint format, uint type, int[] pixels);
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern void glTexSubImage2D(uint target, int level, int xoffset, int yoffset, int width, int height, uint format, uint type, byte[] pixels);
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

        #region GLU_Basic_Functions
        internal const string LIBRARY_GLU = "Glu32.dll";

        [DllImport(LIBRARY_GLU, SetLastError = true)] public static unsafe extern sbyte* gluErrorString(uint errCode);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static unsafe extern sbyte* gluGetString(int name);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluOrtho2D(double left, double right, double bottom, double top);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluPerspective(double fovy, double aspect, double zNear, double zFar);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluPickMatrix(double x, double y, double width, double height, int[] viewport);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluLookAt(double eyex, double eyey, double eyez, double centerx, double centery, double centerz, double upx, double upy, double upz);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluProject(double objx, double objy, double objz, double[] modelMatrix, double[] projMatrix, int[] viewport, double[] winx, double[] winy, double[] winz);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluUnProject(double winx, double winy, double winz, double[] modelMatrix, double[] projMatrix, int[] viewport, ref double objx, ref double objy, ref double objz);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluScaleImage(int format, int widthin, int heightin, int typein, int[] datain, int widthout, int heightout, int typeout, int[] dataout);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluBuild1DMipmaps(uint target, uint components, int width, uint format, uint type, IntPtr data);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluBuild2DMipmaps(uint target, uint components, int width, int height, uint format, uint type, IntPtr data);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern IntPtr gluNewQuadric();
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluDeleteQuadric(IntPtr state);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluQuadricNormals(IntPtr quadObject, uint normals);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluQuadricTexture(IntPtr quadObject, int textureCoords);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluQuadricOrientation(IntPtr quadObject, int orientation);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluQuadricDrawStyle(IntPtr quadObject, uint drawStyle);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluCylinder(IntPtr qobj, double baseRadius, double topRadius, double height, int slices, int stacks);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluDisk(IntPtr qobj, double innerRadius, double outerRadius, int slices, int loops);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluPartialDisk(IntPtr qobj, double innerRadius, double outerRadius, int slices, int loops, double startAngle, double sweepAngle);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluSphere(IntPtr qobj, double radius, int slices, int stacks);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern IntPtr gluNewTess();
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluDeleteTess(IntPtr tess);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessBeginPolygon(IntPtr tess, IntPtr polygonData);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessBeginContour(IntPtr tess);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessVertex(IntPtr tess, double[] coords, double[] data);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessEndContour(IntPtr tess);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessEndPolygon(IntPtr tess);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessProperty(IntPtr tess, int which, double value);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluTessNormal(IntPtr tess, double x, double y, double z);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluGetTessProperty(IntPtr tess, int which, double value);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern IntPtr gluNewNurbsRenderer();
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluDeleteNurbsRenderer(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluBeginSurface(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluBeginCurve(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluEndCurve(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluEndSurface(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluBeginTrim(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluEndTrim(IntPtr nobj);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluPwlCurve(IntPtr nobj, int count, float array, int stride, uint type);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluNurbsCurve(IntPtr nobj, int nknots, float[] knot, int stride, float[] ctlarray, int order, uint type);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluNurbsSurface(IntPtr nobj, int sknot_count, float[] sknot, int tknot_count, float[] tknot, int s_stride, int t_stride, float[] ctlarray, int sorder, int torder, uint type);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluLoadSamplingMatrices(IntPtr nobj, float[] modelMatrix, float[] projMatrix, int[] viewport);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluNurbsProperty(IntPtr nobj, int property, float value);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void gluGetNurbsProperty(IntPtr nobj, int property, float value);
        [DllImport(LIBRARY_GLU, SetLastError = true)] public static extern void IntPtrCallback(IntPtr nobj, int which, IntPtr Callback);
        #endregion

        //Extensions
        private Dictionary<string, Delegate> extensionFunctions = new Dictionary<string, Delegate>();
        [DllImport(LIBRARY_OPENGL, SetLastError = true)] public static extern IntPtr wglGetProcAddress(string name);

        private Delegate InvokeWGL<T>(string name)
        {
            Type delegateType = typeof(T);
            Delegate del = null;
            if (extensionFunctions.ContainsKey(name) == false)
            {
                IntPtr proc = wglGetProcAddress(name);
                if (proc == IntPtr.Zero)
                    throw new Exception("Extension function " + name + " not supported");

                del = Marshal.GetDelegateForFunctionPointer(proc, delegateType);
                if (del == null)
                    throw new Exception("Extension function " + name + " not supported");

                extensionFunctions.Add(name, del);
            }
            del = extensionFunctions[name];

            return del;
        }

        //OpenGL Extensions Functions
        private delegate uint createShader(uint shader);
        public uint glCreateShader(uint shader)
        {
            Delegate wgl = InvokeWGL<createShader>("glCreateShader");
            return (uint)wgl.DynamicInvoke(shader);
        }

        public delegate void shaderSource(uint shader, int count, string[] source, int[] length);
        public void glShaderSource(uint shader, int count, string[] source, int[] length)
        {
            Delegate wgl = InvokeWGL<shaderSource>("glShaderSource");
            wgl.DynamicInvoke(shader, count, source, length);
        }

        public delegate void compileShader(uint shader);
        public void glCompileShader(uint shader)
        {
            Delegate wgl = InvokeWGL<compileShader>("glCompileShader");
            wgl.DynamicInvoke(shader);
        }

        public delegate uint createProgram();
        public uint glCreateProgram()
        {
            Delegate wgl = InvokeWGL<createProgram>("glCreateProgram");
            return (uint)wgl.DynamicInvoke();
        }

        public delegate void attachShader(uint program, uint shader);
        public void glAttachShader(uint program, uint shader)
        {
            Delegate wgl = InvokeWGL<attachShader>("glAttachShader");
            wgl.DynamicInvoke(program, shader);
        }

        public delegate void linkProgram(uint program);
        public void glLinkProgram(uint program)
        {
            Delegate wgl = InvokeWGL<linkProgram>("glLinkProgram");
            wgl.DynamicInvoke(program);
        }

        public delegate void genVertexArrays(int n, uint[] arrays);
        public void glGenVertexArrays(int n, uint[] arrays)
        {
            Delegate wgl = InvokeWGL<genVertexArrays>("glGenVertexArrays");
            wgl.DynamicInvoke(n, arrays);
        }

        public delegate void bindVertexArray(uint array);
        public void glBindVertexArray(uint array)
        {
            Delegate wgl = InvokeWGL<bindVertexArray>("glBindVertexArray");
            wgl.DynamicInvoke(array);
        }

        public delegate void useProgram(uint program);
        public void glUseProgram(uint program)
        {
            Delegate wgl = InvokeWGL<useProgram>("glUseProgram");
            wgl.DynamicInvoke(program);
        }

        public delegate void detachShader(uint program, uint shader);
        public void glDetachShader(uint program, uint shader)
        {
            Delegate wgl = InvokeWGL<detachShader>("glDetachShader");
            wgl.DynamicInvoke(program, shader);
        }

        public delegate void deleteShader(uint shader);
        public void glDeleteShader(uint shader)
        {
            Delegate wgl = InvokeWGL<deleteShader>("glDeleteShader");
            wgl.DynamicInvoke(shader);
        }

        public delegate void deleteVertexArrays(int n, uint[] arrays);
        public void glDeleteVertexArrays(int n, uint[] arrays)
        {
            Delegate wgl = InvokeWGL<deleteVertexArrays>("glDeleteVertexArrays");
            wgl.DynamicInvoke(n, arrays);
        }

        public delegate void deleteProgram(uint program);
        public void glDeleteProgram(uint program)
        {
            Delegate wgl = InvokeWGL<deleteProgram>("glDeleteProgram");
            wgl.DynamicInvoke(program);
        }

        public delegate void genBuffers(int n, uint[] buffers);
        public void glGenBuffers(int n, uint[] buffers)
        {
            Delegate wgl = InvokeWGL<genBuffers>("glGenBuffers");
            wgl.DynamicInvoke(n, buffers);
        }

        public delegate void bindBuffer(uint target, uint buffer);
        public void glBindBuffer(uint target, uint buffer)
        {
            Delegate wgl = InvokeWGL<bindBuffer>("glBindBuffer");
            wgl.DynamicInvoke(target, buffer);
        }

        public delegate void bufferData(uint target, int size, IntPtr data, uint usage);
        public void glBufferData(uint target, float[] data, uint usage)
        {
            IntPtr ptr = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, ptr, data.Length);

            Delegate wgl = InvokeWGL<bufferData>("glBufferData");
            wgl.DynamicInvoke(target, data.Length * sizeof(float), ptr, usage);

            Marshal.FreeHGlobal(ptr);
        }
        public void glBufferData(uint target, int[] data, uint usage)
        {
            IntPtr ptr = Marshal.AllocHGlobal(data.Length * sizeof(float));
            Marshal.Copy(data, 0, ptr, data.Length);

            Delegate wgl = InvokeWGL<bufferData>("glBufferData");
            wgl.DynamicInvoke(target, data.Length * sizeof(uint), ptr, usage);

            Marshal.FreeHGlobal(ptr);
        }

        public delegate void vertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer);
        public void glVertexAttribPointer(uint index, int size, uint type, bool normalized, int stride, IntPtr pointer)
        {
            Delegate wgl = InvokeWGL<vertexAttribPointer>("glVertexAttribPointer");
            wgl.DynamicInvoke(index, size, type, normalized, stride, pointer);
        }

        public delegate void disableVertexAttribArray(uint index);
        public void glDisableVertexAttribArray(uint index)
        {
            Delegate wgl = InvokeWGL<disableVertexAttribArray>("glDisableVertexAttribArray");
            wgl.DynamicInvoke(index);
        }

        public delegate void enableVertexAttribArray(uint index);
        public void glEnableVertexAttribArray(uint index)
        {
            Delegate wgl = InvokeWGL<enableVertexAttribArray>("glEnableVertexAttribArray");
            wgl.DynamicInvoke(index);
        }

        public delegate void deleteBuffers(int n, uint[] buffers);
        public void glDeleteBuffers(int n, uint[] buffers)
        {
            Delegate wgl = InvokeWGL<deleteBuffers>("glDeleteBuffers");
            wgl.DynamicInvoke(n, buffers);
        }

        //Work with Textures
        public delegate void generateMipmap(uint target);
        public void glGenerateMipmap(uint target)
        {
            Delegate wgl = InvokeWGL<generateMipmap>("glGenerateMipmap");
            wgl.DynamicInvoke(target);
        }

        public delegate void activeTexture(uint texture);
        public void glActiveTexture(uint texture)
        {
            Delegate wgl = InvokeWGL<activeTexture>("glActiveTexture");
            wgl.DynamicInvoke(texture);
        }

        public delegate void uniform1f(int location, float v0);
        public void glUniform1f(int location, float v0)
        {
            Delegate wgl = InvokeWGL<uniform1f>("glUniform1f");
            wgl.DynamicInvoke(location, v0);
        }

        public delegate void uniform2f(int location, float v0, float v1);
        public void glUniform2f(int location, float v0, float v1)
        {
            Delegate wgl = InvokeWGL<uniform2f>("glUniform2f");
            wgl.DynamicInvoke(location, v0, v1);
        }

        public delegate void uniform3f(int location, float v0, float v1, float v2);
        public void glUniform3f(int location, float v0, float v1, float v2)
        {
            Delegate wgl = InvokeWGL<uniform3f>("glUniform3f");
            wgl.DynamicInvoke(location, v0, v1, v2);
        }

        public delegate void uniform4f(int location, float v0, float v1, float v2, float v3);
        public void glUniform4f(int location, float v0, float v1, float v2, float v3)
        {
            Delegate wgl = InvokeWGL<uniform4f>("glUniform4f");
            wgl.DynamicInvoke(location, v0, v1, v2, v3);
        }

        public delegate void uniform1i(int location, int v0);
        public void glUniform1i(int location, int v0)
        {
            Delegate wgl = InvokeWGL<uniform1i>("glUniform1i");
            wgl.DynamicInvoke(location, v0);
        }

        public delegate void uniform2i(int location, int v0, int v1);
        public void glUniform2i(int location, int v0, int v1)
        {
            Delegate wgl = InvokeWGL<uniform2i>("glUniform2i");
            wgl.DynamicInvoke(location, v0, v1);
        }

        public delegate void uniform3i(int location, int v0, int v1, int v2);
        public void glUniform3i(int location, int v0, int v1, int v2)
        {
            Delegate wgl = InvokeWGL<uniform3i>("glUniform3i");
            wgl.DynamicInvoke(location, v0, v1, v2);
        }

        public delegate void uniform4i(int location, int v0, int v1, int v2, int v3);
        public void glUniform4i(int location, int v0, int v1, int v2, int v3)
        {
            Delegate wgl = InvokeWGL<uniform4i>("glUniform4i");
            wgl.DynamicInvoke(location, v0, v1, v2, v3);
        }

        public delegate void uniform1fv(int location, int count, float[] value);
        public void glUniform1fv(int location, int count, float[] value)
        {
            Delegate wgl = InvokeWGL<uniform1fv>("glUniform1fv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform2fv(int location, int count, float[] value);
        public void glUniform2fv(int location, int count, float[] value)
        {
            Delegate wgl = InvokeWGL<uniform2fv>("glUniform2fv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform3fv(int location, int count, float[] value);
        public void glUniform3fv(int location, int count, float[] value)
        {
            Delegate wgl = InvokeWGL<uniform3fv>("glUniform3fv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform4fv(int location, int count, float[] value);
        public void glUniform4fv(int location, int count, float[] value)
        {
            Delegate wgl = InvokeWGL<uniform4fv>("glUniform4fv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform1iv(int location, int count, int[] value);
        public void glUniform1iv(int location, int count, int[] value)
        {
            Delegate wgl = InvokeWGL<uniform1iv>("glUniform1iv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform2iv(int location, int count, int[] value);
        public void glUniform2iv(int location, int count, int[] value)
        {
            Delegate wgl = InvokeWGL<uniform2iv>("glUniform2iv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform3iv(int location, int count, int[] value);
        public void glUniform3(int location, int count, int[] value)
        {
            Delegate wgl = InvokeWGL<uniform3iv>("glUniform3iv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate void uniform4iv(int location, int count, int[] value);
        public void glUniform4(int location, int count, int[] value)
        {
            Delegate wgl = InvokeWGL<uniform4iv>("glUniform4iv");
            wgl.DynamicInvoke(location, count, value);
        }

        public delegate int getuniformlocation(uint shader, char[] value);
        public int glGetUniformLocation(uint shader, char[] value)
        {
            Delegate wgl = InvokeWGL<getuniformlocation>("glGetUniformLocation");
            return (int)wgl.DynamicInvoke(shader, value);
        }

        public delegate void texStorage2D(uint target, int level, uint internalformat, int width, int height);
        public void glTexStorage2D(uint target, int level, uint internalformat, int width, int height)
        {
            Delegate wgl = InvokeWGL<texStorage2D>("glTexStorage2D");
            wgl.DynamicInvoke(target, level, internalformat, width, height);
        }
    }
}
