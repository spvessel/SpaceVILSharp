using System;
namespace SpaceVIL.Core
{
    public interface IToolTip
    {
        int GetTimeOut();
        void SetTimeOut(int milliseconds);
    }
}