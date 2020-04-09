using System;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    /// <summary>
    /// A class that describes mouse inputs.
    /// </summary>
    public sealed class MouseArgs : IInputEventArgs
    {
        /// <summary>
        /// Mouse button as SpaceVIL.Core.MouseButton.
        /// </summary>
        public MouseButton Button;

        /// <summary>
        /// State of input as SpaceVIL.Core.InputState. Values: Release, Press, Repeat.
        /// </summary>
        public InputState State;

        /// <summary>
        /// Used modifiers while mouse input.
        /// </summary>
        public KeyMods Mods;

        /// <summary>
        /// Mouse cursor position.
        /// </summary>
        public Position Position = new Position();
        
        /// <summary>
        /// Clearing MouseArgs
        /// </summary>
        public void Clear()
        {
            Button = MouseButton.Unknown;
            State = 0;
            Mods = 0;
            Position.SetPosition(0, 0);
        }

        public override String ToString()
        {
            return
                Button + " " +
                State + " " +
                Mods + " " +
                Position.GetX() + " " +
                Position.GetY() + " ";
        }
    }
}