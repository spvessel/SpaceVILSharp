using System;
namespace SpaceVIL.Core
{
    internal interface IPopUp
    {
        int GetTimeOut();
        void SetTimeOut(int milliseconds);
        void Execute(WindowLayout wnd, String message);
    }
}