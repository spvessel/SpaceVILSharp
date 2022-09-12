using System.Collections.Generic;
using Color = System.Drawing.Color;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public class EffectsView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 10);

            AddItems(
                ComponentsFactory.MakeHeaderLabel("Shadow effect:"),
                new ShadowView(),
                ComponentsFactory.MakeHeaderLabel("Subtract effect:"),
                new SubtractView(),
                ComponentsFactory.MakeHeaderLabel("Border effect:"),
                new BorderView()
            );
        }
    }

    class ShadowView : HorizontalStack {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Glass);
            SetHeightPolicy(SizePolicy.Fixed);
            SetHeight(180);
            SetContentAlignment(ItemAlignment.HCenter);
            SetSpacing(50, 0);
            
            Ellipse ellipse = new Ellipse(32);
            ellipse.SetBackground(Palette.Blue);
            ellipse.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            ellipse.SetSize(100, 100);
            ellipse.Effects().Add(new Shadow(10, Palette.Blue));
            
            Triangle triangle = new Triangle();
            triangle.SetBackground(Palette.Green);
            triangle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            triangle.SetSize(100, 100);
            triangle.Effects().Add(new Shadow(10, new Position(20, 20), Palette.BlackShadow));

            Rectangle rectangle = new Rectangle(new CornerRadius(20));
            rectangle.SetBackground(Palette.Purple);
            rectangle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            rectangle.SetSize(100, 100);
            rectangle.Effects().Add(new Shadow(10, new Size(20, 20), Palette.BlackShadow));

            AddItems(ellipse, rectangle, triangle);
        }
    }

    class SubtractView : Frame {
        public override void InitElements()
        {
            base.InitElements();
            SetBackground(Palette.Glass);
            SetHeightPolicy(SizePolicy.Fixed);
            SetHeight(200);

            int diameter = 100;

            IBaseItem cRed = getCircle(diameter, Color.FromArgb(255, 94, 94));
            IBaseItem cGreen = getCircle(diameter, Color.FromArgb(16, 180, 111));
            IBaseItem cBlue = getCircle(diameter, Color.FromArgb(10, 162, 232));
            
            setCircleAlignment(cRed, ItemAlignment.Top);
            setCircleAlignment(cGreen, ItemAlignment.Left, ItemAlignment.Bottom);
            setCircleAlignment(cBlue, ItemAlignment.Right, ItemAlignment.Bottom);

            AddItems(cRed, cGreen, cBlue);

            cRed.Effects().Add(getCircleEffect(cRed, cBlue));
            cGreen.Effects().Add(getCircleEffect(cGreen, cRed));
            cBlue.Effects().Add(getCircleEffect(cBlue, cGreen));

            cRed.Effects().Add(getCircleCenterEffect(cRed));
            cGreen.Effects().Add(getCircleCenterEffect(cGreen));
            cBlue.Effects().Add(getCircleCenterEffect(cBlue));

        }

        private IBaseItem getCircle(int diameter, Color color)
        {
            Ellipse circle = new Ellipse(64);
            circle.SetSize(diameter, diameter);
            circle.SetBackground(color);
            circle.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            circle.Effects().Add(new Shadow(5, new Size(2, 2), Palette.BlackShadow));
            return circle;
        }

        private void setCircleAlignment(IBaseItem circle, params ItemAlignment[] alignment)
        {
            List<ItemAlignment> list = new List<ItemAlignment>(alignment);
    
            int offset = circle.GetWidth() / 3;
    
            if (list.Contains(ItemAlignment.Top))
            {
                circle.SetMargin(circle.GetMargin().Left, circle.GetMargin().Top - offset + 10, circle.GetMargin().Right,
                        circle.GetMargin().Bottom);
            }
    
            if (list.Contains(ItemAlignment.Bottom))
            {
                circle.SetMargin(circle.GetMargin().Left, circle.GetMargin().Top, circle.GetMargin().Right,
                        circle.GetMargin().Bottom - offset);
            }
    
            if (list.Contains(ItemAlignment.Left))
            {
                circle.SetMargin(circle.GetMargin().Left - offset, circle.GetMargin().Top, circle.GetMargin().Right,
                        circle.GetMargin().Bottom);
            }
    
            if (list.Contains(ItemAlignment.Right))
            {
                circle.SetMargin(circle.GetMargin().Left, circle.GetMargin().Top, circle.GetMargin().Right - offset,
                        circle.GetMargin().Bottom);
            }
        }

        private IEffect getCircleEffect(IBaseItem circle, IBaseItem subtract)
        {
            int diameter = circle.GetHeight();
            float scale = 1.1f;
            int diff = (int) (diameter * scale - diameter) / 2;
            int xOffset = subtract.GetX() - circle.GetX() - diff;
            int yOffset = subtract.GetY() - circle.GetY() - diff;
    
            SubtractFigure effect = new SubtractFigure(
                    new Figure(false, GraphicsMathService.GetEllipse(diameter, diameter, 0, 0, 64)));
            effect.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            effect.SetSizeScale(scale, scale);
            effect.SetPositionOffset(xOffset, yOffset);
    
            return effect;
        }

        private SubtractFigure getCircleCenterEffect(IBaseItem circle)
        {
            float scale = 0.4f;
            int diameter = (int) (circle.GetHeight() * scale);
            SubtractFigure effect = new SubtractFigure(
                    new Figure(true, GraphicsMathService.GetEllipse(diameter, diameter, 0, 0, 64)));
            effect.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            return effect;
        }
    }

    class BorderView : HorizontalStack {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Glass);
            SetHeightPolicy(SizePolicy.Fixed);
            SetHeight(180);
            SetContentAlignment(ItemAlignment.HCenter);
            SetSpacing(50, 0);

            BlankItem border1 = new BlankItem();
            border1.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border1.SetSize(100, 100);
            border1.SetBackground(Palette.Blue);
            border1.SetBorder(new Border(Palette.Green, new CornerRadius(20), 10));
            border1.Effects().Add(new Shadow(8, new Position(0, 5), Palette.BlackShadow));
            
            BlankItem border2 = new BlankItem();
            border2.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border2.SetSize(140, 100);
            border2.SetBackground(Palette.Blue);
            border2.SetBorder(new Border(Palette.Green, new CornerRadius(50, 10, 10, 50), 2));
            border2.Effects().Add(new Shadow(8, new Position(0, 5), Palette.BlackShadow));
            
            BlankItem border3 = new BlankItem();
            border3.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            border3.SetSize(100, 100);
            border3.SetBackground(Palette.Blue);
            border3.SetCustomFigure(new Figure(true, GraphicsMathService.GetTriangle(76, 70, 12, 5, 0)));
            border3.SetBorder(new Border(Palette.Green, new CornerRadius(50, 50, 50, 50), 4));
            border3.Effects().Add(new Shadow(10, new Position(0, 5), Palette.BlackShadow));

            AddItems(border1, border2, border3);
        }
    }
}