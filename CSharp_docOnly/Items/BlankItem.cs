namespace SpaceVIL
{
    public class BlankItem : Prototype
    {
        static int count = 0;
        public BlankItem()
        {
            SetItemName("BlankItem_" + count);
            count++;
        }
    }
}
