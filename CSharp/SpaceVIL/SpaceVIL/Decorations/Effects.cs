using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL
{
    internal class Effects : IAppearanceExtension
    {
        private HashSet<IEffect> _subtract = new HashSet<IEffect>();
        private List<IEffect> _shadow = new List<IEffect>();
        private List<IEffect> _border = new List<IEffect>();

        internal Effects() { }

        public void Add(IEffect effect)
        {
            if (effect is ISubtractFigure)
            {
                _subtract.Add(effect);
                return;
            }
            else if (effect is IShadow)
            {
                _shadow.Add(effect);
                return;
            }
            else if (effect is IBorder)
            {
                _border.Add(effect);
                return;
            }
        }

        public void Remove(IEffect effect)
        {
            if (effect is ISubtractFigure)
            {
                _subtract.Remove(effect);
                return;
            }
            else if (effect is IShadow)
            {
                _shadow.Remove(effect);
                return;
            }
            else if (effect is IBorder)
            {
                _border.Remove(effect);
                return;
            }
        }

        public List<IEffect> Get(EffectType type)
        {
            switch (type)
            {
                case EffectType.Border:
                    return new List<IEffect>(_border);
                case EffectType.Shadow:
                    return new List<IEffect>(_shadow);
                case EffectType.Subtract:
                    return new List<IEffect>(_subtract);
            }
            return new List<IEffect>();
        }

        public void Clear(EffectType type)
        {
            switch (type)
            {
                case EffectType.Border:
                    _border.Clear();
                    break;
                case EffectType.Shadow:
                    _shadow.Clear();
                    break;
                case EffectType.Subtract:
                    _subtract.Clear();
                    break;
            }
        }

        public void Clear()
        {
            _border.Clear();
            _shadow.Clear();
            _subtract.Clear();
        }
    }
}