using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    public interface IStateExtension
    {
        void addItemState(ItemStateType type, ItemState state);

        void removeItemState(ItemStateType type);

        ItemState getState(ItemStateType type);

        void removeAllItemStates();
    }
}