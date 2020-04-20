using System.Threading;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;


namespace View
{
    public class MyWidget : Prototype
    , IMovable 
    , IDraggable
    {
        public MyWidget()
        {
            SetStyle(Style.GetDefaultCommonStyle());
            SetSize(100, 100);

            EventMouseClick += OnRelease;
            EventMousePress += OnPress;
            EventMouseDrag += OnDrag;
            EventMouseDrop += OnDrop;
            EventMouseMove += OnMove;
        }

        private void OnPress(IItem sender, MouseArgs args)
        {
            Console.WriteLine("Press");
        }
        private void OnRelease(IItem sender, MouseArgs args)
        {
            Console.WriteLine("Release");
        }
        private void OnDrag(IItem sender, MouseArgs args)
        {
            Console.WriteLine("Drag");
        }
        private void OnDrop(IItem sender, MouseArgs args)
        {
            Console.WriteLine("Drop");
        }
        private void OnMove(IItem sender, MouseArgs args)
        {
            Console.WriteLine("Move");
        }
    }
}