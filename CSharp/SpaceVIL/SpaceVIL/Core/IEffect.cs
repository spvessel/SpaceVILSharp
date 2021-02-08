using System;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that define visual effect that can be attached to an item.
    /// </summary>
    public interface IEffect
    {
        /// <summary>
        /// Getting the name of the current implementation of the visual effect.
        /// </summary>
        /// <returns>Name of the visual effect as System.String.</returns>
        String GetEffectName();

        /// <summary>
        /// Returns True if the effect is applied, False otherwise.
        /// </summary>
        /// <returns>True: if effect is applied. False: if effect is not applied.</returns>
        bool IsApplied();
    }
}