using SpaceVIL.Decorations;
using SpaceVIL.Core;

namespace SpaceVIL
{
    /// <summary>
    /// The Primitive is an abstract extension of BaseItem for primitive non-interactive items.
    /// <para/> Examples of subclasses: SpaceVIL.Ellipse, SpaceVIL.Rectangle, SpaceVIL.Triangle and etc.
    /// </summary>
    abstract public class Primitive : BaseItem
    {
        /// <summary>
        /// Default constructor of Primitive class.
        /// </summary>
        /// <returns></returns>
        public Primitive() : this("Primitive_") { }

        /// <summary>
        /// Constructs a Primitive with the specified name.
        /// </summary>
        /// <param name="name"> Item name of Primitive. </param>
        public Primitive(string name)
        {
            SetItemName(name);
            SetAlignment(ItemAlignment.Top, ItemAlignment.Left);
        }

        /// <summary>
        /// Setting item position.
        /// </summary>
        /// <param name="x"> X position of the left-top corner. </param>
        /// <param name="y"> Y position of the left-top corner. </param>
        public void SetPosition(int x, int y)
        {
            this.SetX(x);
            this.SetY(y);
        }

        /// <summary>
        /// Setting a style that describes the appearance of an item.
        /// </summary>
        /// <param name="style">Style as SpaceVIL.Decorations.Style.</param>
        public override void SetStyle(Style style)
        {
            if (style == null)
                return;

            SetBackground(style.Background);
            SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            SetSize(style.Width, style.Height);
            SetMinSize(style.MinWidth, style.MinHeight);
            SetMaxSize(style.MaxWidth, style.MaxHeight);
            SetAlignment(style.Alignment);
            SetPosition(style.X, style.Y);
            SetMargin(style.Margin);
            SetVisible(style.IsVisible);
            if (style.Shape != null)
                SetTriangles(style.Shape);
        }

        /// <summary>
        /// Getting the core (only appearance properties without inner styles) style of an item.
        /// </summary>
        /// <returns>Style as SpaceVIL.Decorations.Style.</returns>
        public override Style GetCoreStyle()
        {
            Style style = new Style();
            style.SetSize(GetWidth(), GetHeight());
            style.SetSizePolicy(GetWidthPolicy(), GetHeightPolicy());
            style.Background = GetBackground();
            style.MinWidth = GetMinWidth();
            style.MinHeight = GetMinHeight();
            style.MaxWidth = GetMaxWidth();
            style.MaxHeight = GetMaxHeight();
            style.X = GetX();
            style.Y = GetY();
            style.Margin = new Indents(GetMargin().Left, GetMargin().Top, GetMargin().Right, GetMargin().Bottom);
            style.Alignment = GetAlignment();
            style.IsVisible = IsVisible();

            return style;
        }
    }
}
