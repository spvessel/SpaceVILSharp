namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that describes the alignment of the item and size policy of the item.
    /// <para/> This interface is part of SpaceVIL.Core.IBaseItem.
    /// </summary>
    public interface IBehavior
    {
        /// <summary>
        /// Setting an alignment of an item's shape relative to its container. 
        /// Combines with alignment by vertically (Top, VCenter, Bottom) and horizontally (Left, HCenter, Right). 
        /// </summary>
        /// <param name="alignment">Alignment as SpaceVIL.Core.ItemAlignment.</param>
        void SetAlignment(ItemAlignment alignment);
        /// <summary>
        /// Getting an alignment of an item's shape relative to its container. 
        /// </summary>
        /// <returns>Alignment as SpaceVIL.Core.ItemAlignment.</returns>
        ItemAlignment GetAlignment();
        /// <summary>
        /// Setting width policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Width policy as SpaceVIL.Core.SizePolicy.</param>
        void SetWidthPolicy(SizePolicy policy);
        /// <summary>
        /// Getting width policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Width policy as SpaceVIL.Core.SizePolicy.</returns>
        SizePolicy GetWidthPolicy();
        /// <summary>
        /// Setting height policy of an item's shape. Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <param name="policy">Height policy as SpaceVIL.Core.SizePolicy.</param>
        void SetHeightPolicy(SizePolicy policy);
        /// <summary>
        /// Getting height policy of an item's shape.Can be Fixed (shape not changes its size) 
        /// or Expand (shape is stretched to all available space).
        /// </summary>
        /// <returns>Height policy as SpaceVIL.Core.SizePolicy.</returns>
        SizePolicy GetHeightPolicy();
    }
}
