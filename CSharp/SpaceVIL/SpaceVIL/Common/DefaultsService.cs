using System.Drawing;
using System;
using System.Collections.Generic;

namespace SpaceVIL
{
    public static class DefaultsService
    {
        public static void SetDefaultBackground(BaseItem item)
        {
            item.SetBackground(Color.White);
        }
    }
}