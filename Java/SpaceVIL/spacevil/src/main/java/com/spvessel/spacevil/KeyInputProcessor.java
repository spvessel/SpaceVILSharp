package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.Map;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.ITextShortcuts;
import com.spvessel.spacevil.Core.KeyArgs;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.InputState;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.OSType;

final class KeyInputProcessor {

    private CommonProcessor _commonProcessor;
    private KeyArgs _kargs;
    private Map<KeyMods, Integer> keyMap;

    KeyInputProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
        _kargs = new KeyArgs();
        _kargs.clear();
        if (CommonService.getOSType().equals(OSType.Linux)) {
            keyMap = new HashMap<KeyMods, Integer>();
            keyMap.put(KeyMods.Shift, 0);
            keyMap.put(KeyMods.Control, 0);
            keyMap.put(KeyMods.Alt, 0);
            keyMap.put(KeyMods.Super, 0);
        }
    }

    void process(long wnd, int key, int scancode, int action, int mods) {
        _kargs.key = KeyCode.getEnum(key);
        _kargs.scancode = scancode;
        _kargs.state = InputState.getEnum(action);

        if (CommonService.getOSType().equals(OSType.Linux)) {
            if (key != 0) {
                KeyMods keyMod = getKeyModByKey(key);
                if (!keyMod.equals(KeyMods.No)) {
                    if (action == 1) {
                        if (_kargs.mods.contains(KeyMods.No)) {
                            _kargs.mods.remove(KeyMods.No);
                        }
                        if (keyMap.get(keyMod) == 0) {
                            _kargs.mods.add(keyMod);
                        }
                        keyMap.put(keyMod, keyMap.get(keyMod) + 1);
                    }
                    if (mods != 0 && action == 0) {
                        if (_kargs.mods.contains(keyMod)) {
                            if (keyMap.get(keyMod) == 1)
                                _kargs.mods.remove(keyMod);
                            keyMap.put(keyMod, keyMap.get(keyMod) - 1);
                        }
                    }
                }
                if (_kargs.mods.size() == 0)
                    _kargs.mods.add(KeyMods.No);
            }
            if (action == 0 && mods == 0 && key == 0) {
                _kargs.mods.clear();
                _kargs.mods.add(KeyMods.No);
            }
        } else {
            _kargs.mods = KeyMods.getEnums(mods);
        }

        _commonProcessor.margs.mods = _kargs.mods;

        if ((_commonProcessor.focusedItem instanceof ITextShortcuts) && action == InputState.Press.getValue()) {
            if (action == InputState.Press.getValue()) {
                _commonProcessor.focusedItem.eventKeyPress.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KeyPress, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            }

            if (action == InputState.Repeat.getValue()) {
                _commonProcessor.focusedItem.eventKeyPress.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KeyPress, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            }

            if (action == InputState.Release.getValue()) {
                _commonProcessor.focusedItem.eventKeyRelease.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KeyRelease, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem);
            }
        } else {
            if (action == InputState.Press.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KeyPress, _kargs, _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);

            else if (action == InputState.Repeat.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KeyPress, _kargs, _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);
                        
            else if (action == InputState.Release.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KeyRelease, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);
        }
    }

    private KeyMods getKeyModByKey(int key) {
        boolean isShiftKey = (key == 340 || key == 344);
        if (isShiftKey) {
            return KeyMods.Shift;
        }

        boolean isCtrlKey = (key == 341 || key == 345);
        if (isCtrlKey) {
            return KeyMods.Control;
        }

        boolean isAltKey = (key == 342 || key == 346);
        if (isAltKey) {
            return KeyMods.Alt;
        }

        boolean isSuperKey = (key == 343 || key == 347);
        if (isSuperKey) {
            return KeyMods.Super;
        }

        return KeyMods.No;
    }
}