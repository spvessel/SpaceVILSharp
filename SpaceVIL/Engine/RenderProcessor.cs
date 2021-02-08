using System;
using System.Collections.Generic;
using System.Drawing;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal sealed class RenderProcessor
    {
        private readonly float _intervalVeryLow = 1.0f;
        private readonly float _intervalLow = 1.0f / 10.0f;
        private readonly float _intervalMedium = 1.0f / 30.0f;
        private readonly float _intervalHigh = 1.0f / 60.0f;
        private readonly float _intervalUltra = 1.0f / 120.0f;
        private float _intervalAssigned = 1.0f / 15.0f;

        private RedrawFrequency _frequency = RedrawFrequency.Low;

        private Object _locker = new Object();

        internal VramVertex ScreenSquare;

        internal VramStorage<IImageItem, VramTexture> TextureStorage = new VramStorage<IImageItem, VramTexture>();
        internal VramStorage<ITextContainer, VramTexture> TextStorage = new VramStorage<ITextContainer, VramTexture>();
        internal VramStorage<IBaseItem, VramVertex> VertexStorage = new VramStorage<IBaseItem, VramVertex>();

        internal VramEffectsStorage<IBaseItem, IEffect, VramTexture> BorderStorage = new VramEffectsStorage<IBaseItem, IEffect, VramTexture>();
        internal VramEffectsStorage<IBaseItem, IEffect, VramTexture> SubtractStorage = new VramEffectsStorage<IBaseItem, IEffect, VramTexture>();
        internal VramEffectsStorage<IBaseItem, IEffect, VramTexture> ShadowStorage = new VramEffectsStorage<IBaseItem, IEffect, VramTexture>();

        internal RenderProcessor()
        {
            ScreenSquare = new VramVertex();
        }

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

        internal void DrawDirectVertex(
            Shader shader, List<float[]> vertex, float level,
            int x, int y, int w, int h, Color color, uint type)
        {
            if (vertex == null)
                return;

            shader.UseShader();
            VramVertex store = new VramVertex();
            store.GenBuffers(vertex);
            store.SendColor(shader, color);
            store.SendUniform4f(shader, "position", new float[] { x, y, w, h });
            store.SendUniform1f(shader, "level", level);
            store.Type = type;
            store.Draw();
            store.Clear();
        }

        internal void DrawScreenRectangle(
            Shader shader, float level, int x, int y, int w, int h, Color color, uint type)
        {
            shader.UseShader();
            ScreenSquare.Bind();
            ScreenSquare.SendColor(shader, color);
            ScreenSquare.SendUniform4f(shader, "position", new float[] { 0, 0, w, h });
            ScreenSquare.SendUniform1f(shader, "level", level);
            ScreenSquare.Type = type;
            ScreenSquare.Draw();
            ScreenSquare.Unbind();
        }

        internal void DrawFreshVertex(
            Shader shader, IBaseItem item, float level, int x, int y, int w, int h, Color color, uint type)
        {
            VertexStorage.DeleteResource(item);
            List<float[]> vertex = item.GetTriangles();
            if (vertex == null)
                return;

            shader.UseShader();

            VramVertex store = new VramVertex();
            store.GenBuffers(vertex);
            store.SendColor(shader, color);
            store.SendUniform4f(shader, "position", new float[] { x, y, w, h });
            store.SendUniform1f(shader, "level", level);
            store.Type = type;
            store.Draw();

            VertexStorage.AddResource(item, store);
        }

        internal void DrawStoredVertex(
            Shader shader, IBaseItem item, float level, int x, int y, int w, int h, Color color, uint type)
        {
            VramVertex store = VertexStorage.GetResource(item);
            if (store == null)
            {
                ItemsRefreshManager.SetRefreshShape(item);
                return;
            }

            shader.UseShader();
            store.Bind();
            store.SendColor(shader, color);
            store.SendUniform4f(shader, "position", new float[] { x, y, w, h });
            store.SendUniform1f(shader, "level", level);
            store.Type = type;
            store.Draw();
        }

        internal void DrawFreshVertex(
            Shader shader, IBaseItem item, float[] vertex, float level, int x, int y, int w, int h, Color color, uint type)
        {
            VertexStorage.DeleteResource(item);

            if (vertex == null)
                return;

            shader.UseShader();

            VramVertex store = new VramVertex();
            store.GenBuffers(vertex);
            store.SendColor(shader, color);
            store.SendUniform4f(shader, "position", new float[] { x, y, w, h });
            store.SendUniform1f(shader, "level", level);
            store.Type = type;
            store.Draw();

            VertexStorage.AddResource(item, store);
        }

        internal void DrawFreshText(
            Shader shader, ITextContainer item, ITextImage printer, Scale scale, float w, float h,
            float level, float[] color)
        {
            TextStorage.DeleteResource(item);

            if (printer.IsEmpty())
                return;

            shader.UseShader();
            VramTexture store = new VramTexture();
            store.GenBuffers(0, printer.GetWidth() / scale.GetXScale(), 0, printer.GetHeight() / scale.GetYScale(), true);
            store.GenTexture(printer.GetWidth(), printer.GetHeight(), printer.GetBytes(), ImageQuality.Smooth);
            TextStorage.AddResource(item, store);

            store.SendUniform4f(shader, "position",
                new float[] { printer.GetXOffset(), printer.GetYOffset(), w, h });
            store.SendUniform1f(shader, "level", level);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "rgb", color);
            store.Draw();
            store.Unbind();
        }

        internal void DrawStoredText(
            Shader shader, ITextContainer item, ITextImage printer, float w, float h,
            float level, float[] color)
        {
            VramTexture store = TextStorage.GetResource(item);
            if (store == null)
            {
                ItemsRefreshManager.SetRefreshText(item);
                return;
            }

            shader.UseShader();
            store.BindVboIbo();
            store.Bind();
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "position", new float[] { printer.GetXOffset(), printer.GetYOffset(), w, h });
            store.SendUniform1f(shader, "level", level);
            store.SendUniform4f(shader, "rgb", color);
            store.Draw();
            store.Unbind();
        }

        internal VramTexture DrawDirectShadow(
                    Shader shader, float level, float[] weights, int res, uint[] fboTexture,
                    float x, float y, float w, float h, int width, int height)
        {
            shader.UseShader();
            VramTexture store = new VramTexture();
            store.GenBuffers(0, width, 0, height);
            store.Texture[0] = fboTexture[0];
            store.Bind(fboTexture);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "position", new float[] { x, y, width, height });
            store.SendUniform1f(shader, "level", level);
            store.SendUniform1fv(shader, "weights", 11, weights);
            store.SendUniform2fv(shader, "frame", new float[] { width, height });
            store.SendUniform1f(shader, "res", res / 10.0f);
            store.SendUniform2fv(shader, "point", new float[] { x, y });
            store.SendUniform2fv(shader, "size", new float[] { w, h });
            store.Draw();
            return store;
        }

        internal void DrawRawShadow(
            Shader shader, float level, uint[] fboTexture,
            float x, float y, float w, float h, int width, int height)
        {
            VramTexture store = new VramTexture();
            store.GenBuffers(0, w, 0, h);
            store.Texture[0] = fboTexture[0];
            store.Bind(fboTexture);

            shader.UseShader();
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "position", new float[] { x, y, width, height });
            store.SendUniform1f(shader, "level", level);
            store.SendUniform1i(shader, "overlay", 0);
            store.SendUniform1f(shader, "alpha", 0);

            store.Draw();
            store.Clear();
        }

        internal void DrawFreshShadow(
            Shader shader, IBaseItem item, IShadow shadow, float level, uint[] fboTexture,
            float x, float y, float w, float h, int width, int height)
        {
            ShadowStorage.DeleteResource(item, shadow);

            VramTexture store = new VramTexture();
            store.GenBuffers(0, w, 0, h);
            store.Texture[0] = fboTexture[0];
            store.Bind(fboTexture);

            ShadowStorage.AddResource(item, shadow, store);

            shader.UseShader();
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "position", new float[] { x, y, width, height });
            store.SendUniform1f(shader, "level", level);

            // store.SendUniform1fv(shader, "weights", 11, weights);
            // store.SendUniform2fv(shader, "frame", new float[] { width, height });
            // store.SendUniform1f(shader, "res", (res / 10.0f));
            // store.SendUniform2fv(shader, "point", new float[] { x, y });
            // store.SendUniform2fv(shader, "size", new float[] { w, h });

            store.SendUniform1i(shader, "overlay", 0);
            store.SendUniform1f(shader, "alpha", 0);

            store.Draw();
            store.Unbind();
        }

        internal void DrawStoredShadow(Shader shader, IBaseItem item, IShadow shadow, float level,
            float x, float y, int width, int height)
        {
            VramTexture store = ShadowStorage.GetResources(item)[shadow];
            if (store == null)
            {
                return;
            }

            shader.UseShader();
            store.BindVboIbo();
            store.Bind(store.Texture);
            store.SendUniformSample2D(shader, "tex");
            store.SendUniform4f(shader, "position", new float[] { x, y, width, height });
            store.SendUniform1f(shader, "level", level);
            // store.SendUniform1fv(shader, "weights", 11, weights);
            // store.SendUniform2fv(shader, "frame", new float[] { store.StoredWidth, store.StoredHeight });
            // store.SendUniform1f(shader, "res", res / 10.0f);
            // store.SendUniform2fv(shader, "point", xy);
            // store.SendUniform2fv(shader, "size", wh);
            // store.SendUniform1i(shader, "applyBlur", 0);
            store.SendUniform1i(shader, "overlay", 0);
            store.SendUniform1f(shader, "alpha", 0);

            store.Draw();
            store.Unbind();
        }

        internal void DrawTextureAsIs(
            Shader shader, IImageItem image, float ax, float ay, float aw, float ah,
            int iw, int ih, int width, int height, float level)
        {
            Bitmap bmp = image.GetImage();
            if (bmp == null)
                return;

            shader.UseShader();
            VramTexture store = new VramTexture();
            store.GenBuffers(0, aw, 0, ah);
            store.GenTexture(iw, ih, bmp, image.GetImageQuality());
            store.SendUniformSample2D(shader, "tex");
            if (image.IsColorOverlay())
            {
                float[] argb = {
                        (float) image.GetColorOverlay().R / 255.0f,
                        (float) image.GetColorOverlay().G / 255.0f,
                        (float) image.GetColorOverlay().B / 255.0f,
                        (float) image.GetColorOverlay().A / 255.0f };
                store.SendUniform1i(shader, "overlay", 1);
                store.SendUniform4f(shader, "rgb", argb);
            }
            else
                store.SendUniform1i(shader, "overlay", 0);

            store.SendUniform4f(shader, "position", new float[] { ax, ay, width, height });
            store.SendUniform1f(shader, "level", level);
            store.SendUniform1f(shader, "alpha", image.GetRotationAngle());
            store.Draw();
            store.Clear();
        }

        internal void DrawFreshTexture(
            IImageItem image, Shader shader, float ax, float ay, float aw, float ah,
            int iw, int ih, int width, int height, float level)
        {
            TextureStorage.DeleteResource(image);
            Bitmap bmp = image.GetImage();
            if (bmp == null)
                return;
            // byte[] buffer = image.GetPixMapImage();
            // if (buffer == null)
            // {
            //     return;
            // }

            shader.UseShader();
            VramTexture tex = new VramTexture();
            tex.GenBuffers(0, aw, 0, ah);
            // tex.GenTexture(iw, ih, bitmap);
            tex.GenTexture(iw, ih, bmp, image.GetImageQuality());
            TextureStorage.AddResource(image, tex);

            ItemsRefreshManager.RemoveImage(image);

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

            tex.SendUniform4f(shader, "position", new float[] { ax, ay, width, height });
            tex.SendUniform1f(shader, "level", level);
            tex.SendUniform1f(shader, "alpha", image.GetRotationAngle());
            tex.Draw();
            tex.Unbind();
        }

        internal void DrawStoredTexture(
            IImageItem image, Shader shader, float ax, float ay, int width, int height, float level)
        {
            VramTexture tex = TextureStorage.GetResource(image);
            if (tex == null)
            {
                ItemsRefreshManager.SetRefreshImage(image);
                return;
            }

            shader.UseShader();
            tex.BindVboIbo();
            tex.Bind();
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

            tex.SendUniform4f(shader, "position", new float[] { ax, ay, width, height });
            tex.SendUniform1f(shader, "level", level);
            tex.SendUniform1f(shader, "alpha", image.GetRotationAngle());
            tex.Draw();
            tex.Unbind();
        }

        internal static List<float[]> GetFullWindowRectangle(int w, int h)
        {
            List<float[]> vertex = new List<float[]>();
            // vertex.Add(new float[] { -1.0f, 1.0f });
            // vertex.Add(new float[] { -1.0f, -1.0f });
            // vertex.Add(new float[] { 1.0f, -1.0f });
            // vertex.Add(new float[] { 1.0f, -1.0f });
            // vertex.Add(new float[] { 1.0f, 1.0f });
            // vertex.Add(new float[] { -1.0f, 1.0f });
            vertex.Add(new float[] { 0, 0 });
            vertex.Add(new float[] { 0, h });
            vertex.Add(new float[] { w, h });
            vertex.Add(new float[] { 0, 0 });
            vertex.Add(new float[] { w, h });
            vertex.Add(new float[] { w, 0 });
            return vertex;
        }

        internal void FlushResources()
        {
            TextStorage.Flush();
            TextureStorage.Flush();
            VertexStorage.Flush();

            BorderStorage.Flush();
            ShadowStorage.Flush();
            SubtractStorage.Flush();
        }

        internal void ClearResources()
        {
            TextStorage.Clear();
            TextureStorage.Clear();
            VertexStorage.Clear();

            BorderStorage.Clear();
            ShadowStorage.Clear();
            SubtractStorage.Clear();

            ScreenSquare.Clear();
        }

        internal void FreeResource<T>(T resource)
        {
            ITextContainer text = resource as ITextContainer;
            if (text != null)
            {
                ItemsRefreshManager.RemoveText(text);
                TextStorage.AddForFlushing(text);
            }

            IImageItem image = resource as IImageItem;
            if (image != null)
            {
                ItemsRefreshManager.RemoveImage(image);
                TextureStorage.AddForFlushing(image);
            }

            IBaseItem item = resource as IBaseItem;
            if (item != null)
            {
                ItemsRefreshManager.RemoveShape(item);

                VertexStorage.AddForFlushing(item);

                BorderStorage.AddForFlushing(item);
                ShadowStorage.AddForFlushing(item);
                SubtractStorage.AddForFlushing(item);
            }
        }
    }
}