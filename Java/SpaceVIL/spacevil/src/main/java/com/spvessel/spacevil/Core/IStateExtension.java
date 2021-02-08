package com.spvessel.spacevil.Core;

import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Flags.ItemStateType;

public interface IStateExtension {
    void addItemState(ItemStateType type, ItemState state);

    void removeItemState(ItemStateType type);

    ItemState getState(ItemStateType type);

    void removeAllItemStates();
}
