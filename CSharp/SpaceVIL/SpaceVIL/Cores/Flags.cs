using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    [Flags]
    public enum ItemAlignment
    {
        Left = 0x01,
        Top = 0x02,
        Right = 0x04,
        Bottom = 0x08,
        HCenter = 0x10,
        VCenter = 0x20
    }
    [Flags]
    public enum SizePolicy
    {
        Fixed = 0x01,//размер фиксированный, никаких изменений фигуры, только выравнивание
        Strech = 0x02,//размер фиксированный, фигура растягивается в пропорциях фигуры к окну, выравнивае работает некорректно
        Expand = 0x04,//размер плавающий, размер фигуры подстраивается под все доступное пространство, выравнивание работает корректно
        Ignored = 0x08//не используется (будет работать как Fixed)
    }
    [Flags]
    public enum SizeType { Width, Height };
    [Flags]
    public enum ItemStateType
    {
        Base = 0x01,
        Hovered = 0x02,
        Pressed = 0x04,
        Toggled = 0x08,
        Focused = 0x10,
        Disabled = 0x20
    };

    public enum UpdateType
    {
        Critical,
        CoordsOnly
    }

    public enum Orientation
    {
        Vertical,
        Horizontal
    }

    public enum ScrollBarVisibility
    {
        Always,
        AsNeeded,
        Never
    }
    [Flags]
    public enum ListPosition
    {
        No = 0x00,
        Top = 0x01,
        Bottom = 0x02,
        Left = 0x04,
        Right = 0x08,
    }
    public enum ItemRule
    {
        Lazy,
        Strict
    }
}
