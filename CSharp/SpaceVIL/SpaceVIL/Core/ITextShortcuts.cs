﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    interface ITextShortcuts
    {
        void PasteText(string pasteStr);
        string GetSelectedText();
        string CutText();
    }
}