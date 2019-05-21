package com.spvessel.spacevil;

import com.spvessel.spacevil.Core.TextInputArgs;
import com.spvessel.spacevil.Flags.KeyMods;

final class TextInputProcessor {

    private TextInputArgs _tiargs;
    private CommonProcessor _commonProcessor;

    TextInputProcessor(CommonProcessor processor) {
        _commonProcessor = processor;
        _tiargs = new TextInputArgs();
        _tiargs.clear();
    }

    void process(long wnd, int character, int mods) {
        _tiargs.character = character;
        _tiargs.mods = KeyMods.getEnums(mods);

        if (_commonProcessor.focusedItem != null) {
            if (_commonProcessor.focusedItem.eventTextInput != null)
                _commonProcessor.focusedItem.eventTextInput.execute(_commonProcessor.focusedItem, _tiargs);
        }
    }
}