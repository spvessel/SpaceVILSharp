package com.spvessel.spacevil;

import java.util.HashMap;
import java.util.Map;

import com.spvessel.spacevil.Common.CommonService;
import com.spvessel.spacevil.Core.InterfaceTextShortcuts;
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
        if (CommonService.getOSType().equals(OSType.LINUX)) {
            keyMap = new HashMap<KeyMods, Integer>();
            keyMap.put(KeyMods.SHIFT, 0);
            keyMap.put(KeyMods.CONTROL, 0);
            keyMap.put(KeyMods.ALT, 0);
            keyMap.put(KeyMods.SUPER, 0);
        }
    }

    void process(long wnd, int key, int scancode, int action, int mods) {
        _kargs.key = KeyCode.getEnum(key);
        _kargs.scancode = scancode;
        _kargs.state = InputState.getEnum(action);

        if (CommonService.getOSType().equals(OSType.LINUX)) {
            if (key != 0) {
                KeyMods keyMod = getKeyModByKey(key);
                if (!keyMod.equals(KeyMods.NO)) {
                    if (action == 1) {
                        if (_kargs.mods.contains(KeyMods.NO)) {
                            _kargs.mods.remove(KeyMods.NO);
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
                    _kargs.mods.add(KeyMods.NO);
            }
            if (action == 0 && mods == 0 && key == 0) {
                _kargs.mods.clear();
                _kargs.mods.add(KeyMods.NO);
            }
        } else {
            _kargs.mods = KeyMods.getEnums(mods);
        }

        _commonProcessor.margs.mods = _kargs.mods;

        if ((_commonProcessor.focusedItem instanceof InterfaceTextShortcuts) && action == InputState.PRESS.getValue()) {
            if (action == InputState.PRESS.getValue()) {
                _commonProcessor.focusedItem.eventKeyPress.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KEY_PRESS, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            }

            if (action == InputState.REPEAT.getValue()) {
                _commonProcessor.focusedItem.eventKeyPress.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KEY_PRESS, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem);
            }

            if (action == InputState.RELEASE.getValue()) {
                _commonProcessor.focusedItem.eventKeyRelease.execute(_commonProcessor.focusedItem, _kargs);
                _commonProcessor.manager.assignActionsForItemPyramid(InputEventType.KEY_RELEASE, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems);
                // assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem);
            }
        } else {
            if (action == InputState.PRESS.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KEY_PRESS, _kargs, _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);
            // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem, true);
            else if (action == InputState.REPEAT.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KEY_PRESS, _kargs, _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);
            // assignActions(InputEventType.KEY_PRESS, _kargs, focusedItem, true);
            else if (action == InputState.RELEASE.getValue())
                _commonProcessor.manager.assignActionsForSender(InputEventType.KEY_RELEASE, _kargs, 
                        _commonProcessor.focusedItem,
                        _commonProcessor.underFocusedItems, true);
            // assignActions(InputEventType.KEY_RELEASE, _kargs, focusedItem, true);
        }
    }

    private KeyMods getKeyModByKey(int key) {
        boolean isShiftKey = (key == 340 || key == 344);
        if (isShiftKey) {
            return KeyMods.SHIFT;
        }

        boolean isCtrlKey = (key == 341 || key == 345);
        if (isCtrlKey) {
            return KeyMods.CONTROL;
        }

        boolean isAltKey = (key == 342 || key == 346);
        if (isAltKey) {
            return KeyMods.ALT;
        }

        boolean isSuperKey = (key == 343 || key == 347);
        if (isSuperKey) {
            return KeyMods.SUPER;
        }

        return KeyMods.NO;
    }
}