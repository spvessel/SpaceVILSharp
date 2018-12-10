package com.spacevil.Flags;

public enum ShaderType {
    VERTEX(0x8B31), FRAGMENT(0x8B30), GEOMETRY(0x8DD9), COMPUTE(0x91B9);

    private final int type;

    ShaderType(int type) {
        this.type = type;
    }

    public int getValue() {
        return type;
    }
}

// int Vertex = 0x8B31;
// int Fragment = 0x8B30;
// int Geometry = 0x8DD9;
// int Compute = 0x91B9;