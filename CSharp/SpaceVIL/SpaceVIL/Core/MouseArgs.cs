using System;
using System.Collections.Generic;

namespace SpaceVIL.Core
{
    public sealed class MouseArgs : IInputEventArgs
    {
        public MouseButton Button;
        public InputState State;
        public KeyMods Mods;
        public Position Position = new Position();
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