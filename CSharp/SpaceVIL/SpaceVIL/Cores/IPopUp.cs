using System;
namespace SpaceVIL
{
    internal interface IPopUp
    {
        int GetTimeOut();
        void SetTimeOut(int milliseconds);
        void Execute(WindowLayout wnd, String message);
    }
}