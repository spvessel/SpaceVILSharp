package com.spvessel.spacevil.Core;

import java.util.List;

import com.spvessel.spacevil.Prototype;

/**
 * A class that describe mouse "drag and drop" input (file system).
 */
public final class DropArgs extends InputEventArgs {
    /**
     * An item on which the drop event occurs.
     */
    public Prototype item = null;

    /**
     * Number of selected paths.
     */
    public int count = -1;

    /**
     * List of selected paths.
     */
    public List<String> paths = null;

    /**
     * Clearing DropArgs.
     */
    @Override
    public void clear() {
        count = -1;
        paths = null;
        item = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getItemName() + "\n");
        sb.append(count + " \n");
        if (paths != null)
        {
            for (String p : paths)
            {
                sb.append(p + "\n");
            }
        }
        return sb.toString(); 
    }
}