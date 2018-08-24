using System;
namespace SpaceVIL
{
    internal interface IFloating
    {
        void Show(IItem sender, MouseArgs args);
        void Hide();
        bool IsOutsideClickClosable();
        void SetOutsideClickClosable(bool value);
    }
}