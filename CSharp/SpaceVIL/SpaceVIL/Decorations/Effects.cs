using System;
using System.Collections.Generic;
using System.Threading;
using SpaceVIL.Core;

namespace SpaceVIL.Decorations
{
    /// <summary>
    /// Effects is a static class for controlling the application of effects to a item's shape.
    /// </summary>
    public static class Effects
    {
        private static Object _lock = new Object();

        private static Dictionary<IBaseItem, HashSet<IEffect>> _subtractEffects = new Dictionary<IBaseItem, HashSet<IEffect>>();
        /// <summary>
        /// Adding effect to specified item.
        /// </summary>
        /// <param name="item">A item as SpaceVIL.Core.IBaseItem. </param>
        /// <param name="effect">A effect as SpaceVIL.Core.IEffect. </param>
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
        /// <summary>
        /// Removing specified effect form item.
        /// </summary>
        /// <param name="item">A item as SpaceVIL.Core.IBaseItem. </param>
        /// <param name="effect">A effect as SpaceVIL.Core.IEffect. </param>
        /// <returns>True: if such effect is presented and removed. False: if item has no such effect.</returns>
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
        /// <summary>
        /// Getting list of applyed effects on specified item.
        /// </summary>
        /// <param name="item">An item as SpaceVIL.Core.IBaseItem.</param>
        /// <returns>List of effects of specified item as List&lt;SpaceVIL.Core.IEffect&gt;.</returns>
        public static List<IEffect> GetEffects(IBaseItem item)
        {
            Monitor.Enter(_lock);
            try
            {
                if (_subtractEffects.ContainsKey(item))
                {
                    return new List<IEffect>(_subtractEffects[item]);
                }
                return new List<IEffect>();
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }
    }
}