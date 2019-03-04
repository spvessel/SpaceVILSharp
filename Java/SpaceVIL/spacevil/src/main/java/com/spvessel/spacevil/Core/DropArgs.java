package com.spvessel.spacevil.Core;

import java.util.List;

import com.spvessel.spacevil.Prototype;

public final class DropArgs extends InputEventArgs {
    public Prototype item = null;
    public int count = -1;
    public List<String> paths = null;

    @Override
    public void clear() {
        count = -1;
        paths = null;
        item = null;
    }
}