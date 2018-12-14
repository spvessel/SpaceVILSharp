using System;
namespace SpaceVIL.Core
{
    public interface IFloating
    {
        void Show(IItem sender, MouseArgs args);
        void Hide();
        bool IsOutsideClickClosable();
        void SetOutsideClickClosable(bool value);
        // bool IsLockOutside();
        // void SetLockOutside(bool value);
    }
}