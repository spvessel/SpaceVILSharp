using System;
using System.Drawing;

namespace SpaceVIL
{
    public interface IBaseItem : IItem, ISize, IPosition, IEventUpdate, IBehavior
    {
        void SetHandler(WindowLayout handler);
        WindowLayout GetHandler();
        void SetParent(Prototype parent);
        Prototype GetParent();
        void SetConfines();
        void SetConfines(int x0, int x1, int y0, int y1);
        void SetMargin(Indents padding);
        void SetMargin(int left = 0, int top = 0, int right = 0, int bottom = 0);
        Indents GetMargin();
        void InitElements();
        void SetStyle(Style style);
        Style GetCoreStyle();

        bool IsDrawable();
        void SetDrawable(bool value);
        bool IsVisible();
        void SetVisible(bool value);
        bool IsShadowDrop();
        void SetShadowDrop(bool value);
        void SetShadowRadius(float radius);
        float GetShadowRadius();
        Color GetShadowColor();
        void SetShadowColor(Color color);
        Position GetShadowPos();
        void SetShadow(float radius, int x, int y, Color color);
        int[] GetConfines();
    }
}
