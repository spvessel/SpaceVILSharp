using System;
namespace SpaceVIL.Core
{
    public interface IFloating
    {
        void Show();
        void Show(IItem sender, MouseArgs args);
        void Hide();
        void Hide(MouseArgs args);
        bool IsOutsideClickClosable();
        void SetOutsideClickClosable(bool value);
        // bool IsLockOutside();
        // void SetLockOutside(bool value);
    }
}