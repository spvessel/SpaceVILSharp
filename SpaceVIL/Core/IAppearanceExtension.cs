
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// IAppearanceExtension is an interface for managing effects of the item's shape.
    /// </summary>
    public interface IAppearanceExtension
    {
        /// <summary>
        /// Adding effect to the item.
        /// </summary>
        /// <param name="effect">An effect as SpaceVIL.Core.IEffect.</param>
        void Add(IEffect effect);

        /// <summary>
        /// Removing specified effect from item.
        /// </summary>
        /// <param name="effect">An effect as SpaceVIL.Core.IEffect.</param>
        void Remove(IEffect effect);

        /// <summary>
        /// Getting list of applyed effects on the item.
        /// </summary>
        /// <param name="type">List of effects of specified item as List&lt;SpaceVIL.Core.IBaseItem&gt;.</param>
        /// <returns></returns>
        List<IEffect> Get(EffectType type);

        /// <summary>
        /// Clearing all specified effects from the item.
        /// </summary>
        /// <param name="type">An effect type as SpaceVIL.Flags.EffectType.</param>
        void Clear(EffectType type);

        /// <summary>
        /// Clearing all effects.
        /// </summary>
        void Clear();
    }
}