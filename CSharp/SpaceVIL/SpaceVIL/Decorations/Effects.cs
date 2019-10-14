using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    public static class Effects
    {
        private static Object _lock = new Object();

        private static Dictionary<IBaseItem, HashSet<IEffect>> _subtractEffects = new Dictionary<IBaseItem, HashSet<IEffect>>();

        public static void AddEffect(IBaseItem item, IEffect effect)
        {
            Monitor.Enter(_lock);
            try
            {
                if (effect is ISubtractFigure)
                {
                    if (_subtractEffects.ContainsKey(item))
                    {
                        _subtractEffects[item].Add(effect);
                        return;
                    }
                    _subtractEffects.Add(item, new HashSet<IEffect>());
                    _subtractEffects[item].Add(effect);
                }
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        public static bool RemoveEffect(IBaseItem item, IEffect effect)
        {
            Monitor.Enter(_lock);
            try
            {
                if (effect is ISubtractFigure)
                {
                    if (_subtractEffects.ContainsKey(item))
                    {
                        return _subtractEffects[item].Remove(effect);
                    }
                }
                return false;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        public static List<IEffect> GetEffects(IBaseItem item)
        {
            Monitor.Enter(_lock);
            try
            {
                if (_subtractEffects.ContainsKey(item))
                {
                    return new List<IEffect>(_subtractEffects[item]);
                }
                return null;
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
    }
}