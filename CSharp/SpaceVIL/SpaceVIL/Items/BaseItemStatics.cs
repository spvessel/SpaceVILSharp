using System;
using System.Collections.Generic;
using System.Linq;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    internal static class BaseItemStatics
    {
        internal static void UpdateAllLayout(IBaseItem item)
        {
            Prototype parent = item.GetParent();
            if (parent == null)
                return;

            var hLayout = parent as IHLayout;
            var vLayout = parent as IVLayout;
            var grid = parent as IFreeLayout;

            if (hLayout == null && vLayout == null && grid == null)
                UpdateBehavior(item);

            if (hLayout != null)
                hLayout.UpdateLayout();
            if (vLayout != null)
                vLayout.UpdateLayout();
            if (grid != null)
                grid.UpdateLayout();
        }

        internal static void UpdateHLayout(IBaseItem item)
        {
            Prototype parent = item.GetParent();
            if (parent != null && item.GetWidthPolicy() == SizePolicy.Fixed)
            {
                var layout = parent as IHLayout;
                var grid = parent as IFreeLayout;

                if (layout == null && grid == null)
                    UpdateBehavior(item);

                if (layout != null)
                    layout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }

        internal static void UpdateVLayout(IBaseItem item)
        {
            Prototype parent = item.GetParent();
            if (parent != null && item.GetHeightPolicy() == SizePolicy.Fixed)
            {
                var layout = parent as IVLayout;
                var grid = parent as IFreeLayout;

                if (layout == null && grid == null)
                    UpdateBehavior(item);

                if (layout != null)
                    layout.UpdateLayout();
                if (grid != null)
                    grid.UpdateLayout();
            }
        }

        internal static void CastToUpdateBehavior(IBaseItem item)
        {
            Prototype prototype = item as Prototype;
            if (prototype != null)
                UpdateBehavior(prototype.GetCore());
            else
                UpdateBehavior(item);
        }

        internal static void CastToUpdateGeometry(IBaseItem item)
        {
            Prototype prototype = item as Prototype;
            if (prototype != null)
                UpdateGeometry(prototype.GetCore());
            else
                UpdateGeometry(item);
        }

        internal static void UpdateGeometry(IBaseItem item)
        {
            UpdateGeometryAttr(item, GeometryEventType.ResizeWidth, 0);
            UpdateGeometryAttr(item, GeometryEventType.ResizeHeight, 0);
            UpdateGeometryAttr(item, GeometryEventType.MovedX, 0);
            UpdateGeometryAttr(item, GeometryEventType.MovedY, 0);
        }

        internal static void UpdateBehavior(IBaseItem item)
        {
            Prototype parent = item.GetParent();
            if (parent == null)
                return;

            if ((item as VisualItem) != null)
                UpdatePrototypeBehavior(item, parent);
            else
                UpdateBaseItemBehavior(item, parent);
        }

        internal static void UpdatePrototypeBehavior(IBaseItem item, Prototype parent)
        {
            Prototype prt = (item as VisualItem).prototype;
            UpdateBaseItemBehavior(prt, parent);
        }

        internal static void UpdateBaseItemBehavior(IBaseItem item, Prototype parent)
        {
            ItemAlignment alignment = item.GetAlignment();
            Indents itemMargin = item.GetMargin();

            if (alignment.HasFlag(ItemAlignment.Left))
            {
                item.SetX(parent.GetX() + parent.GetPadding().Left + itemMargin.Left);//
            }
            if (alignment.HasFlag(ItemAlignment.Right))
            {
                item.SetX(parent.GetX() + parent.GetWidth() - item.GetWidth() - parent.GetPadding().Right - itemMargin.Right);//
            }
            if (alignment.HasFlag(ItemAlignment.Top))
            {
                item.SetY(parent.GetY() + parent.GetPadding().Top + itemMargin.Top);//
            }
            if (alignment.HasFlag(ItemAlignment.Bottom))
            {
                item.SetY(parent.GetY() + parent.GetHeight() - item.GetHeight() - parent.GetPadding().Bottom - itemMargin.Bottom);//
            }
            if (alignment.HasFlag(ItemAlignment.HCenter))
            {
                item.SetX(parent.GetX() + (parent.GetWidth() - item.GetWidth()) / 2 + itemMargin.Left - itemMargin.Right);//
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                item.SetY(parent.GetY() + (parent.GetHeight() - item.GetHeight()) / 2 + itemMargin.Top - itemMargin.Bottom);//
            }
        }

        internal static void UpdateGeometryAttr(IBaseItem item, GeometryEventType type, int value)
        {
            Prototype parent = item.GetParent();
            if (parent == null)
                return;

            if ((item as VisualItem) != null)
                UpdatePrototypeGeometryAttr(item, parent, type, value);
            else
                UpdateBaseItemGeometryAttr(item, parent, type, value);
        }

        internal static void UpdatePrototypeGeometryAttr(IBaseItem item, Prototype parent, GeometryEventType type, int value)
        {
            Prototype prt = (item as VisualItem).prototype;
            UpdateBaseItemGeometryAttr(prt, parent, type, value);
        }

        internal static void UpdateBaseItemGeometryAttr(IBaseItem item, Prototype parent, GeometryEventType type, int value)
        {
            item.SetConfines();
            switch (type)
            {
                case GeometryEventType.MovedX:
                    item.SetX(item.GetX() + value);
                    break;

                case GeometryEventType.MovedY:
                    item.SetY(item.GetY() + value);
                    break;

                case GeometryEventType.ResizeWidth:
                    if (item.GetWidthPolicy() == SizePolicy.Fixed)
                    {
                        if (item.GetAlignment().HasFlag(ItemAlignment.Right))
                        {
                            item.SetX(parent.GetX() + parent.GetWidth() - item.GetWidth() - parent.GetPadding().Right - item.GetMargin().Right);//
                        }
                        if (item.GetAlignment().HasFlag(ItemAlignment.HCenter))
                        {
                            item.SetX(parent.GetX() + (parent.GetWidth() - item.GetWidth()) / 2 + item.GetMargin().Left - item.GetMargin().Right);
                        }
                    }
                    else if (item.GetWidthPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - item.GetMargin().Right - item.GetMargin().Left;//
                        prefered = (prefered > item.GetMaxWidth()) ? item.GetMaxWidth() : prefered;
                        prefered = (prefered < item.GetMinWidth()) ? item.GetMinWidth() : prefered;
                        item.SetWidth(prefered);

                        if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + item.GetMargin().Right + item.GetMargin().Left == parent.GetWidth())//
                        {
                            item.SetX(parent.GetX() + parent.GetPadding().Left + item.GetMargin().Left);//
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + item.GetMargin().Right + item.GetMargin().Left < parent.GetWidth())//
                        {
                            if (item.GetAlignment().HasFlag(ItemAlignment.Right))
                            {
                                item.SetX(parent.GetX() + parent.GetWidth() - item.GetWidth() - parent.GetPadding().Right - item.GetMargin().Right);//
                            }
                            if (item.GetAlignment().HasFlag(ItemAlignment.HCenter))
                            {
                                item.SetX(parent.GetX() + (parent.GetWidth() - item.GetWidth()) / 2 + item.GetMargin().Left);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Left + parent.GetPadding().Right + item.GetMargin().Right + item.GetMargin().Left > parent.GetWidth())//
                        {
                            //никогда не должен зайти
                            item.SetX(parent.GetX() + parent.GetPadding().Left + item.GetMargin().Left);//
                            prefered = parent.GetWidth() - parent.GetPadding().Left - parent.GetPadding().Right - item.GetMargin().Left - item.GetMargin().Right;//
                            item.SetWidth(prefered);
                        }
                    }
                    break;

                case GeometryEventType.ResizeHeight:
                    if (item.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        if (item.GetAlignment().HasFlag(ItemAlignment.Bottom))
                        {
                            item.SetY(parent.GetY() + parent.GetHeight() - item.GetHeight() - parent.GetPadding().Bottom - item.GetMargin().Bottom);//
                        }
                        if (item.GetAlignment().HasFlag(ItemAlignment.VCenter))
                        {
                            item.SetY(parent.GetY() + (parent.GetHeight() - item.GetHeight()) / 2 + item.GetMargin().Top - item.GetMargin().Bottom);
                        }
                    }
                    else if (item.GetHeightPolicy() == SizePolicy.Expand)
                    {
                        int prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - item.GetMargin().Bottom - item.GetMargin().Top;//
                        prefered = (prefered > item.GetMaxHeight()) ? item.GetMaxHeight() : prefered;
                        prefered = (prefered < item.GetMinHeight()) ? item.GetMinHeight() : prefered;
                        item.SetHeight(prefered);

                        if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + item.GetMargin().Bottom + item.GetMargin().Top == parent.GetHeight())//
                        {
                            item.SetY(parent.GetY() + parent.GetPadding().Top + item.GetMargin().Top);//
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + item.GetMargin().Bottom + item.GetMargin().Top < parent.GetHeight())//
                        {
                            if (item.GetAlignment().HasFlag(ItemAlignment.Bottom))
                            {
                                item.SetY(parent.GetY() + parent.GetHeight() - item.GetHeight() - parent.GetPadding().Bottom - item.GetMargin().Bottom);//
                            }
                            if (item.GetAlignment().HasFlag(ItemAlignment.VCenter))
                            {
                                item.SetY(parent.GetY() + (parent.GetHeight() - item.GetHeight()) / 2 + item.GetMargin().Top);//
                            }
                        }
                        else if (prefered + parent.GetPadding().Top + parent.GetPadding().Bottom + item.GetMargin().Bottom + item.GetMargin().Top > parent.GetHeight())//
                        {
                            //никогда не должен зайти
                            item.SetY(parent.GetY() + parent.GetPadding().Top + item.GetMargin().Top);//
                            prefered = parent.GetHeight() - parent.GetPadding().Top - parent.GetPadding().Bottom - item.GetMargin().Top - item.GetMargin().Bottom;//
                            item.SetHeight(prefered);
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        internal static List<float[]> UpdateShape(IBaseItem item)
        {
            if (item == null)
                return null;
            return UpdateShape(item.GetTriangles(), item.GetWidth(), item.GetHeight());
        }

        internal static List<float[]> UpdateShape(List<float[]> triangles, int w, int h)
        {
            if (triangles == null || triangles.Count == 0)
                return null;

            //clone triangles
            List<float[]> result = new List<float[]>();
            for (int i = 0; i < triangles.Count; i++)
            {
                result.Add(new float[] { triangles.ElementAt(i)[0], triangles.ElementAt(i)[1] });
            }
            //max and min
            float maxX = result.Select(_ => _[0]).Max();
            float maxY = result.Select(_ => _[1]).Max();
            float minX = result.Select(_ => _[0]).Min();
            float minY = result.Select(_ => _[1]).Min();

            //to the left top corner
            foreach (var point in result)
            {
                point[0] = (point[0] - minX) * w / (maxX - minX);// + item.GetX();
                point[1] = (point[1] - minY) * h / (maxY - minY);// + item.GetY();
            }

            return result;
        }
    }
}