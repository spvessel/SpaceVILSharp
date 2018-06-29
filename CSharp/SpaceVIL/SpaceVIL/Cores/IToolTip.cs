using System;
namespace SpaceVIL
{
    public interface IToolTip
    {
        int GetTimeOut();
        void SetTimeOut(int milliseconds);
    }
}