package com.spvessel.spacevil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.spvessel.spacevil.Core.IAppearanceExtension;
import com.spvessel.spacevil.Core.IBorder;
import com.spvessel.spacevil.Core.IEffect;
import com.spvessel.spacevil.Core.IShadow;
import com.spvessel.spacevil.Core.ISubtractFigure;
import com.spvessel.spacevil.Flags.EffectType;

class Effects implements IAppearanceExtension {
    private Set<IEffect> _subtract = new HashSet<>();
    private ArrayList<IEffect> _shadow = new ArrayList<>();
    private ArrayList<IEffect> _border = new ArrayList<>();

    Effects() {
    }

    @Override
    public void add(IEffect effect) {
        if (effect instanceof ISubtractFigure) {
            _subtract.add(effect);
            return;
        } else if (effect instanceof IShadow) {
            _shadow.add(effect);
            return;
        } else if (effect instanceof IBorder) {
            _border.add(effect);
            return;
        }
    }

    @Override
    public void remove(IEffect effect) {
        if (effect instanceof ISubtractFigure) {
            _subtract.remove(effect);
            return;
        } else if (effect instanceof IShadow) {
            _shadow.remove(effect);
            return;
        } else if (effect instanceof IBorder) {
            _border.remove(effect);
            return;
        }
    }

    @Override
    public List<IEffect> get(EffectType type) {
        switch (type) {
            case Border:
                return new ArrayList<>(_border);
            case Shadow:
                return new ArrayList<>(_shadow);
            case Subtract:
                return new ArrayList<>(_subtract);
        }
        return new ArrayList<IEffect>();
    }

    @Override
    public void clear(EffectType type) {
        switch (type) {
            case Border:
                _border.clear();
                _border.trimToSize();
                break;
            case Shadow:
                _shadow.clear();
                _shadow.trimToSize();
                break;
            case Subtract:
                _subtract.clear();
                break;
        }
    }

    @Override
    public void clear() {
        _border.clear();
        _shadow.clear();
        _subtract.clear();
        _border.trimToSize();
        _shadow.trimToSize();
    }
}
