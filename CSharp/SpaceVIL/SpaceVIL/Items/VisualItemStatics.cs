using System;
using System.Collections.Generic;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal static class VisualItemStatics
    {
        internal static void UpdateState(VisualItem item)
        {
            ItemState baseState = item.GetState(ItemStateType.Base);
            ItemState currentState = item.GetState(item.GetCurrentStateType());
            item.SetBackgroundDirect(currentState.Background);

            if (currentState.Shape != null)
            {
                if (item.IsCustomFigure() != currentState.Shape)
                    item.SetCustomFigure(currentState.Shape);
            }

            ItemState disabledState = item.GetState(ItemStateType.Disabled);
            if (item.IsDisabled() && disabledState != null)
            {
                UpdateVisualProperties(item, disabledState, baseState);
                return;
            }

            ItemState focusedState = item.GetState(ItemStateType.Focused);
            if (item.IsFocused() && focusedState != null)
            {
                UpdateVisualProperties(item, focusedState, baseState);
                baseState = focusedState;
            }
            ItemState hoverState = item.GetState(ItemStateType.Hovered);
            if (item.IsMouseHover() && hoverState != null)
            {
                UpdateVisualProperties(item, hoverState, baseState);
                baseState = hoverState;
            }
            ItemState pressedState = item.GetState(ItemStateType.Pressed);
            if (item.IsMousePressed() && pressedState != null)
            {
                UpdateVisualProperties(item, pressedState, baseState);
                baseState = pressedState;
            }

            if (baseState == currentState)
            {
                Border border = CloneBorder(currentState.Border);
                if (!item.GetBorderDirect().GetRadius().Equals(baseState.Border.GetRadius()))
                    ItemsRefreshManager.SetRefreshShape(item.prototype);

                item.border = border;
            }
        }

        internal static void UpdateVisualProperties(VisualItem item, ItemState state, ItemState prevState)
        {
            ItemState currentState = item.GetState(item.GetCurrentStateType());
            item.SetBackgroundDirect(GraphicsMathService.MixColors(currentState.Background, item.GetBackground(), state.Background));

            Border borderCurrentState = CloneBorder(currentState.Border);
            Border borderState = CloneBorder(state.Border);

            //CASE 1: item has CornerRadius(!=0) but other states has CornerRadius(0)
            if (!borderCurrentState.GetRadius().IsCornersZero())
            {
                if (!borderState.GetRadius().IsCornersZero())
                {
                    if (!borderCurrentState.GetRadius().Equals(borderState.GetRadius()))
                    {
                        item.border.SetRadius(borderState.GetRadius());
                        ItemsRefreshManager.SetRefreshShape(item.prototype);
                    }
                }
                else
                {
                    if (!prevState.Border.GetRadius().IsCornersZero())
                    {
                        if (!borderCurrentState.GetRadius().Equals(prevState.Border.GetRadius()))
                        {
                            item.border.SetRadius(prevState.Border.GetRadius());
                            ItemsRefreshManager.SetRefreshShape(item.prototype);
                        }
                    }
                }
            }
            if (borderState.GetThickness() >= 0)
            {
                item.border.SetThickness(borderState.GetThickness());
            }
            else
            {
                if (prevState.Border.GetThickness() >= 0)
                    item.border.SetThickness(prevState.Border.GetThickness());
            }

            if (borderState.GetFill().A > 0)
            {
                item.border.SetFill(borderState.GetFill());
            }
            else
            {
                if (prevState.Border.GetFill().A > 0)
                    item.border.SetFill(prevState.Border.GetFill());
            }
            //     item._border.setFill(prevState.Border.getFill());
            // if (item._border.getFill().getAlpha() == 0)
            //     item._border.setFill(item.getState(ItemStateType.BASE).Border.getFill());

            if (state.Shape != null)
                item.SetCustomFigure(state.Shape);
        }

        internal static Border CloneBorder(Border border)
        {
            Border clone = new Border();
            clone.SetFill(border.GetFill());
            clone.SetRadius(border.GetRadius());
            clone.SetThickness(border.GetThickness());
            return clone;
        }

        internal static bool GetHoverVerification(VisualItem item, float xpos, float ypos)
        {
            switch (item.HoverRule)
            {
                case ItemHoverRule.Lazy:
                    return LazyHoverVerification(item, xpos, ypos);
                case ItemHoverRule.Strict:
                    return StrictHoverVerification(item, xpos, ypos);
                default:
                    return false;
            }
        }

        internal static bool StrictHoverVerification(VisualItem item, float xpos, float ypos)
        {
            List<float[]> tmp = BaseItemStatics.UpdateShape(item);
            if (tmp == null)
                return false;

            float Ax, Ay, Bx, By, Cx, Cy, Px, Py, case1, case2, case3;
            Px = xpos - item.GetX();
            Py = ypos - item.GetY();

            for (int i = 0; i < tmp.Count; i += 3)
            {
                Ax = tmp[i][0];
                Ay = tmp[i][1];

                Bx = tmp[i + 1][0];
                By = tmp[i + 1][1];

                Cx = tmp[i + 2][0];
                Cy = tmp[i + 2][1];

                case1 = (Ax - Px) * (By - Ay) - (Bx - Ax) * (Ay - Py);
                case2 = (Bx - Px) * (Cy - By) - (Cx - Bx) * (By - Py);
                case3 = (Cx - Px) * (Ay - Cy) - (Ax - Cx) * (Cy - Py);

                if ((case1 >= 0 && case2 >= 0 && case3 >= 0) || (case1 <= 0 && case2 <= 0 && case3 <= 0))
                    return true;
            }

            return false;
        }

        internal static bool LazyHoverVerification(VisualItem item, float xpos, float ypos)
        {
            bool result = false;
            float minx = item.GetX();
            float maxx = item.GetX() + item.GetWidth();
            float miny = item.GetY();
            float maxy = item.GetY() + item.GetHeight();

            if (item._confinesX0 > minx)
                minx = item._confinesX0;

            if (item._confinesX1 < maxx)
                maxx = item._confinesX1;

            if (item._confinesY0 > miny)
                miny = item._confinesY0;

            if (item._confinesY1 < maxy)
                maxy = item._confinesY1;

            if (xpos > minx && xpos < maxx && ypos > miny && ypos < maxy)
            {
                result = true;
            }
            return result;
        }

        internal static void SetStyle(VisualItem item, Style style)
        {
            if (style == null)
                return;

            item.SetPosition(style.X, style.Y);
            item.SetSize(style.Width, style.Height);
            item.SetSizePolicy(style.WidthPolicy, style.HeightPolicy);
            item.SetPadding(style.Padding);
            item.SetMargin(style.Margin);
            item.SetAlignment(style.Alignment);
            item.SetSpacing(style.Spacing);
            item.SetMinSize(style.MinWidth, style.MinHeight);
            item.SetMaxSize(style.MaxWidth, style.MaxHeight);
            item.SetBackground(style.Background);
            item.SetBorderRadius(style.BorderRadius);
            item.SetBorderThickness(style.BorderThickness);
            item.SetBorderFill(style.BorderFill);
            item.SetShadow(style.ShadowRadius, style.ShadowXOffset, style.ShadowYOffset, style.ShadowColor);
            item.SetShadowDrop(style.IsShadowDrop);
            item.SetVisible(style.IsVisible);
            item.RemoveAllItemStates();

            ItemState core_state = new ItemState(style.Background);
            core_state.Border.SetRadius(style.BorderRadius);
            core_state.Border.SetThickness(style.BorderThickness);
            core_state.Border.SetFill(style.BorderFill);

            foreach (var state in style.GetAllStates())
            {
                item.AddItemState(state.Key, state.Value);
            }
            if (style.Shape != null)
            {
                item.SetCustomFigure(new Figure(style.IsFixedShape, style.Shape));
                core_state.Shape = item.IsCustomFigure();
            }
            item.AddItemState(ItemStateType.Base, core_state);
        }

        internal static Style ExtractCoreStyle(VisualItem item)
        {
            Style style = new Style();
            style.SetSize(item.GetWidth(), item.GetHeight());
            style.SetSizePolicy(item.GetWidthPolicy(), item.GetHeightPolicy());
            style.Background = item.GetBackground();
            style.MinWidth = item.GetMinWidth();
            style.MinHeight = item.GetMinHeight();
            style.MaxWidth = item.GetMaxWidth();
            style.MaxHeight = item.GetMaxHeight();
            style.X = item.GetX();
            style.Y = item.GetY();
            style.Padding = new Indents(item.GetPadding().Left, item.GetPadding().Top, item.GetPadding().Right, item.GetPadding().Bottom);
            style.Margin = new Indents(item.GetMargin().Left, item.GetMargin().Top, item.GetMargin().Right, item.GetMargin().Bottom);
            style.Spacing = new Spacing(item.GetSpacing().Horizontal, item.GetSpacing().Vertical);
            style.Alignment = item.GetAlignment();
            style.BorderFill = item.GetBorderFill();
            style.BorderRadius = item.GetBorderRadius();
            style.BorderThickness = item.GetBorderThickness();
            style.IsVisible = item.IsVisible();
            if (item.IsCustomFigure() != null)
            {
                style.Shape = item.IsCustomFigure().GetFigure();
                style.IsFixedShape = item.IsCustomFigure().IsFixed();
            }
            foreach (var state in item.states)
            {
                style.AddItemState(state.Key, state.Value);
            }

            return style;
        }
    }
}