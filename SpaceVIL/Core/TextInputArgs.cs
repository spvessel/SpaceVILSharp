using System;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// A class that describe keyboard text typing input.
    /// </summary>
    public sealed class TextInputArgs : IInputEventArgs
    {
        /// <summary>
        /// Character code.
        /// </summary>
        public UInt32 Character;

        /// <summary>
        /// Used modifiers while typing.
        /// </summary>
        public KeyMods Mods;
        
        /// <summary>
        /// Clearing TextInputArgs.
        /// </summary>
        public void Clear()
        {
            Character = 0;
            Mods = 0;
        }

        public override String ToString()
        {
            return
                Character + " " +
                Mods + " ";
        }
    }
}