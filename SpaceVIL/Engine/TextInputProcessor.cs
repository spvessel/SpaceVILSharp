using System;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class TextInputProcessor
    {
        private TextInputArgs _tiargs;
        CommonProcessor _commonProcessor;
        internal TextInputProcessor(CommonProcessor commonProcessor)
        {
            _commonProcessor = commonProcessor;
            _tiargs = new TextInputArgs();
            _tiargs.Clear();
        }

        internal void Process(Int64 wnd, uint character, KeyMods mods)
        {
            _tiargs.Character = character;
            _tiargs.Mods = mods;
            _commonProcessor.FocusedItem?.EventTextInput?.Invoke(_commonProcessor.FocusedItem, _tiargs);
        }
    }
}