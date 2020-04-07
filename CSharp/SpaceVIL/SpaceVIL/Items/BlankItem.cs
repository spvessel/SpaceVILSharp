namespace SpaceVIL
{
    /// <summary>
    /// BlankItem is pure subclass of Prototype without any extensions.
    /// <para/> Example: used as cheap version of SpaceVIL.ButtonCore 
    /// (SpaceVIL.ButtonCore contains text 
    /// and additional methods extensions).
    /// </summary>
    public class BlankItem : Prototype
    {
        static int count = 0;
        /// <summary>
        /// Default BlankItem constructor.
        /// </summary>
        public BlankItem()
        {
            SetItemName("BlankItem_" + count);
            count++;
        }
    }
}
