using System;
using System.Collections.Generic;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class KeyInputProcessor
    {
        private KeyArgs _kargs;
        private Dictionary<KeyMods, Int32> keyMap;
        CommonProcessor _commonProcessor;
        internal KeyInputProcessor(CommonProcessor commonProcessor)
        {
            _commonProcessor = commonProcessor;
            _kargs = new KeyArgs();
            _kargs.Clear();
#if LINUX
                keyMap = new Dictionary<KeyMods, Int32>();
                keyMap.Add(KeyMods.Shift, 0);
                keyMap.Add(KeyMods.Control, 0);
                keyMap.Add(KeyMods.Alt, 0);
                keyMap.Add(KeyMods.Super, 0);
#endif
        }

        internal void Process(Glfw.Window glfwwnd, KeyCode key, int scancode, InputState action, KeyMods mods)
        {
            _kargs.Key = key;
            _kargs.Scancode = scancode;
            _kargs.State = action;
#if LINUX
            if (key != 0)
            {
                KeyMods keyMod = GetKeyModByKey(key);
                if (keyMod != 0)
                {
                    if (action == InputState.Press)
                    {
                        _kargs.Mods |= keyMod;
                        keyMap[keyMod]++;
                    }
                    if (mods != 0 && action == InputState.Release)
                    {
                        if (_kargs.Mods.HasFlag(keyMod))
                        {
                            if(keyMap[keyMod] == 1)
                                _kargs.Mods &= ~keyMod;
                            keyMap[keyMod]--;
                        }
                    }
                }
            }
            if ((action == InputState.Release) && (mods == 0) && (key == 0))
            {
                _kargs.Mods = 0;
            }
#else
            _kargs.Mods = mods;
#endif
            _commonProcessor.Margs.Mods = _kargs.Mods;

            if ((_commonProcessor.FocusedItem is ITextShortcuts) && action == InputState.Press)
            {
                // if (action == InputState.Press)
                {
                    _commonProcessor.FocusedItem.EventKeyPress(_commonProcessor.FocusedItem, _kargs);
                    _commonProcessor.Manager.AssignActionsForItemPyramid(InputEventType.KeyPress, _kargs,
                                        _commonProcessor.FocusedItem, _commonProcessor.UnderFocusedItems);
                }
                if (action == InputState.Repeat)
                {
                    _commonProcessor.FocusedItem.EventKeyPress(_commonProcessor.FocusedItem, _kargs);
                    _commonProcessor.Manager.AssignActionsForItemPyramid(InputEventType.KeyPress, _kargs,
                                        _commonProcessor.FocusedItem, _commonProcessor.UnderFocusedItems);
                }
                if (action == InputState.Release)
                {
                    _commonProcessor.FocusedItem.EventKeyRelease(_commonProcessor.FocusedItem, _kargs);
                    _commonProcessor.Manager.AssignActionsForItemPyramid(InputEventType.KeyRelease, _kargs,
                                        _commonProcessor.FocusedItem, _commonProcessor.UnderFocusedItems);
                }
            }
            else
            {
                if (action == InputState.Press)
                    _commonProcessor.Manager.AssignActionsForSender(InputEventType.KeyPress, _kargs, _commonProcessor.FocusedItem,
                                        _commonProcessor.UnderFocusedItems, true);
                else if (action == InputState.Repeat)
                    _commonProcessor.Manager.AssignActionsForSender(InputEventType.KeyPress, _kargs, _commonProcessor.FocusedItem,
                                        _commonProcessor.UnderFocusedItems, true);
                else if (action == InputState.Release)
                    _commonProcessor.Manager.AssignActionsForSender(InputEventType.KeyRelease, _kargs, _commonProcessor.FocusedItem,
                                        _commonProcessor.UnderFocusedItems, true);
            }
        }

        private KeyMods GetKeyModByKey(KeyCode key)
        {
            bool isShiftKey = (key == KeyCode.LeftShift || key == KeyCode.RightShift);
            if (isShiftKey)
            {
                return KeyMods.Shift;
            }
            bool isCtrlKey = (key == KeyCode.LeftControl || key == KeyCode.RightControl);
            if (isCtrlKey)
            {
                return KeyMods.Control;
            }
            bool isAltKey = (key == KeyCode.LeftAlt || key == KeyCode.RightAlt);
            if (isAltKey)
            {
                return KeyMods.Alt;
            }
            bool isSuperKey = (key == KeyCode.LeftSuper || key == KeyCode.RightSuper);
            if (isSuperKey)
            {
                return KeyMods.Super;
            }
            return 0;
        }
    }
}