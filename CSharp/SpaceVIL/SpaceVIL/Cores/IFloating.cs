using System;
namespace SpaceVIL
{
    public interface IFloating
    {
        void Show(IItem sender, MouseArgs args);
        void Hide();
        bool IsOutsideClickClosable();
        void SetOutsideClickClosable(bool value);
    }
}