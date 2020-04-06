using System;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// A class that describes keyboard key input.
    /// </summary>
    public sealed class KeyArgs : IInputEventArgs
    {
        /// <summary>
        /// Key code as SpaceVIL.Core.KeyCode.
        /// </summary>
        public KeyCode Key;
        /// <summary>
        /// Scancode of key.
        /// </summary>
        public Int32 Scancode;
        /// <summary>
        /// State of input as SpaceVIL.Core.InputState. Values: Release, Press, Repeat.
        /// </summary>
        public InputState State;
        /// <summary>
        /// Used modifiers while typing.
        /// </summary>
        public KeyMods Mods;
        /// <summary>
        /// Clearing KeyArgs.
        /// </summary>
        public void Clear()
        {
            Key = KeyCode.Unknown;
            Scancode = -1;
            State = 0;
            Mods = 0;
        }

        public override String ToString()
        {
            return
                Key + " " +
                Scancode + " " +
                State + " " +
                Mods + " ";
        }
    }
}