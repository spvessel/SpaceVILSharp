﻿using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface ITextContainer
    {
        //TextItem GetText();

        TextPrinter GetLetTextures();
        Color GetForeground();
    }
}