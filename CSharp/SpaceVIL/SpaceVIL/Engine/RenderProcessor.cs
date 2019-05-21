using System;
using System.Collections.Generic;
using System.Drawing;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class RenderProcessor
    {
        internal RenderProcessor() { }

        private Object _locker = new Object();

        private readonly float _intervalVeryLow = 1.0f;
        private readonly float _intervalLow = 1.0f / 10.0f;
        private readonly float _intervalMedium = 1.0f / 30.0f;
        private readonly float _intervalHigh = 1.0f / 60.0f;
        private readonly float _intervalUltra = 1.0f / 120.0f;
        private float _intervalAssigned = 1.0f / 15.0f;
        private RedrawFrequency _frequency = RedrawFrequency.Low;

        internal void SetFrequency(RedrawFrequency value)
        {
            Monitor.Enter(_locker);
            try
            {
                if (value == RedrawFrequency.VeryLow)
                {
                    _intervalAssigned = _intervalVeryLow;
                }
                if (value == RedrawFrequency.Low)
                {
                    _intervalAssigned = _intervalLow;
                }
                else if (value == RedrawFrequency.Medium)
                {
                    _intervalAssigned = _intervalMedium;
                }
                else if (value == RedrawFrequency.High)
                {
                    _intervalAssigned = _intervalHigh;
                }
                else if (value == RedrawFrequency.Ultra)
                {
                    _intervalAssigned = _intervalUltra;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
            }
            finally
            {
                Monitor.Exit(_locker);
            }
        }

        private float GetFrequency()
        {
            Monitor.Enter(_locker);
            try
            {
                return _intervalAssigned;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
                return _intervalLow;
            }
            finally
            {
                Monitor.Exit(_locker);
            }
        }

        internal RedrawFrequency GetRedrawFrequency()
        {
            Monitor.Enter(_locker);
            try
            {
                return _frequency;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Method - SetFrequency");
                Console.WriteLine(ex.StackTrace);
                _frequency = RedrawFrequency.Low;
                return _frequency;
            }
            finally
            {
                Monitor.Exit(_locker);
            }
        }

        internal void DrawVertex(Shader shader, List<float[]> vertex, float level, Color color, uint type)
        {
            shader.UseShader();
            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(vertex, level);
            store.SendColor(shader, color);
            store.Draw(type);
            store.Clear();
        }

        internal void DrawVertex(Shader shader, float[] vertex, Color color, uint type)
        {
            shader.UseShader();
            VRAMVertex store = new VRAMVertex();
            store.GenBuffers(vertex);
            store.SendColor(shader, color);
            store.Draw(type);
            store.Clear();
        }

        internal void DrawText(Shader shader, float x0, float x1, float y0, float y1, int w, int h, byte[] buffer, float level,
                float[] color)
        {
            shader.UseShader();
            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(x0, x1, y0, y1, level, true);
            store.GenTexture(w, h, buffer);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "rgb", color);
            store.Draw();
            store.Clear();
        }

        internal void DrawShadow(Shader shader, float level, float[] weights, int res, uint[] fboTexture, float[] xy, float[] wh,
                int width, int height)
        {
            float i_x0 = -1.0f;
            float i_y0 = 1.0f;
            float i_x1 = 1.0f;
            float i_y1 = -1.0f;
            shader.UseShader();
            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(i_x0, i_x1, i_y0, i_y1, level);
            store.Bind(fboTexture);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform1fv(shader, "weights", 11, weights);
            store.SendUniform2fv(shader, "frame", new float[] { width, height });
            store.SendUniform1f(shader, "res", (res * 1f / 10));
            store.SendUniform2fv(shader, "point", xy);
            store.SendUniform2fv(shader, "size", wh);
            store.Draw();
            store.Clear();
        }

        internal void DrawTextureAsIs(Shader shader, float x0, float x1, float y0, float y1, int w, int h, byte[] buffer,
                float level, float alpha)
        {
            shader.UseShader();
            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(x0, x1, y0, y1, level);
            store.GenTexture(w, h, buffer);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform1i(shader, "overlay", 0);
            store.SendUniform1f(shader, "alpha", alpha);
            store.Draw();
            store.Clear();
        }

        internal void DrawTextureWithColorOverlay(Shader shader, float x0, float x1, float y0, float y1, int w, int h, byte[] buffer,
                float level, float alpha, Color color)
        {
            float[] argb = {
                        (float) color.R / 255.0f,
                        (float) color.G / 255.0f,
                        (float) color.B / 255.0f,
                        (float) color.A / 255.0f };

            shader.UseShader();
            VRAMTexture store = new VRAMTexture();
            store.GenBuffers(x0, x1, y0, y1, level, true);
            store.GenTexture(w, h, buffer);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform1i(shader, "overlay", 1);
            store.SendUniform4f(shader, "rgb", argb);
            store.SendUniform1f(shader, "alpha", alpha);
            store.Draw();
            store.Clear();
        }

        internal void DrawFreshTexture(ImageItem image, Shader shader, float x0, float x1, float y0, float y1, int w, int h,
                byte[] buffer, float level)
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                VRAMStorage.DeleteTexture(image);
                shader.UseShader();
                VRAMTexture tex = new VRAMTexture();
                tex.GenBuffers(x0, x1, y0, y1, level);
                tex.GenTexture(w, h, buffer);
                VRAMStorage.AddTexture(image, tex);
                image.SetNew(false);

                tex.SendUniformSample2D(shader, "tex");
                if (image.IsColorOverlay())
                {
                    float[] argb = {
                        (float) image.GetColorOverlay().R / 255.0f,
                        (float) image.GetColorOverlay().G / 255.0f,
                        (float) image.GetColorOverlay().B / 255.0f,
                        (float) image.GetColorOverlay().A / 255.0f };
                    tex.SendUniform1i(shader, "overlay", 1);
                    tex.SendUniform4f(shader, "rgb", argb);
                }
                else
                    tex.SendUniform1i(shader, "overlay", 0);

                tex.SendUniform1f(shader, "alpha", image.GetRotationAngle());
                tex.Draw();
                tex.DeleteIBOBuffer();
                tex.DeleteVBOBuffer();
                tex.Unbind();

            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal void DrawStoredTexture(ImageItem image, Shader shader, float x0, float x1, float y0, float y1, float level)
        {
            Monitor.Enter(VRAMStorage.StorageLocker);
            try
            {
                shader.UseShader();
                VRAMTexture tex = VRAMStorage.GetTexture(image);
                if (tex == null)
                {
                    image.SetNew(true);
                    return;
                }
                tex.Bind();
                tex.GenBuffers(x0, x1, y0, y1, level);
                tex.SendUniformSample2D(shader, "tex");
                if (image.IsColorOverlay())
                {
                    float[] argb = { (float) image.GetColorOverlay().R / 255.0f,
                        (float) image.GetColorOverlay().G / 255.0f,
                        (float) image.GetColorOverlay().B / 255.0f,
                        (float) image.GetColorOverlay().A / 255.0f };
                    tex.SendUniform1i(shader, "overlay", 1);
                    tex.SendUniform4f(shader, "rgb", argb);
                }
                else
                    tex.SendUniform1i(shader, "overlay", 0);

                tex.SendUniform1f(shader, "alpha", image.GetRotationAngle());
                tex.Draw();
                tex.DeleteIBOBuffer();
                tex.DeleteVBOBuffer();
                tex.Unbind();

            }
            finally
            {
                Monitor.Exit(VRAMStorage.StorageLocker);
            }
        }

        internal static List<float[]> GetFullWindowRectangle()
        {
            List<float[]> vertex = new List<float[]>();
            vertex.Add(new float[] { -1.0f, 1.0f, 0.0f });
            vertex.Add(new float[] { -1.0f, -1.0f, 0.0f });
            vertex.Add(new float[] { 1.0f, -1.0f, 0.0f });
            vertex.Add(new float[] { 1.0f, -1.0f, 0.0f });
            vertex.Add(new float[] { 1.0f, 1.0f, 0.0f });
            vertex.Add(new float[] { -1.0f, 1.0f, 0.0f });
            return vertex;
        }
    }
}