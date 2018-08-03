using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace SpaceVIL
{
    public class StyleTheme
    {
        ConcurrentDictionary<BaseItem,Style> SpecificItemStyle = new ConcurrentDictionary<BaseItem, Style>();
        ConcurrentDictionary<BaseItem,Style> DefaultItemStyle = new ConcurrentDictionary<BaseItem, Style>();
    }
}