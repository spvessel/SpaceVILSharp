using System;
using System.Threading;
using System.Collections.Generic;
using System.Collections.Concurrent;

namespace SpaceVIL.Core
{
    public abstract class InputEventArgs 
    { 
        public abstract void Clear();
    }
    public sealed class MouseArgs : InputEventArgs
    {
        public MouseButton Button;
        public InputState State;
        public KeyMods Mods;
        public Pointer Position = new Pointer();
        public override void Clear()
        {
            Button = MouseButton.Unknown;
            State = 0;
            Mods = 0;
            Position.Clear();
        }
    }

    public sealed class KeyArgs : InputEventArgs
    {
        public KeyCode Key;
        public Int32 Scancode;
        public InputState State;
        public KeyMods Mods;
        public override void Clear()
        {
            Key = KeyCode.Unknown;
            Scancode = -1;
            State = 0;
            Mods = 0;
        }
    }

    public sealed class TextInputArgs : InputEventArgs
    {
        public UInt32 Character;
        public KeyMods Mods;
        public override void Clear()
        {
            Character = 0;
            Mods = 0;
        }
    }

    internal static class PrintArgs
    {
        public static void MouseArgs(MouseArgs args)
        {
            Console.WriteLine(
                args.Button + " " +
                args.State + " " +
                args.Mods + " " +
                args.Position.GetX() + " " +
                args.Position.GetY() + " "
            );
        }
        public static void KeyArgs(KeyArgs args)
        {
            Console.WriteLine(
                args.Key + " " +
                args.Scancode + " " +
                args.State + " " +
                args.Mods + " "
            );
        }
        public static void TextInputArgs(TextInputArgs args)
        {
            Console.WriteLine(
                args.Character + " " +
                args.Mods + " "
            );
        }
    }
}
