namespace SpaceVIL
{
    public class BlankItem : VisualItem
    {
        static int count = 0;
        public BlankItem()
        {
            SetItemName("BlankItem_" + count);
            count++;
        }
    }
}
