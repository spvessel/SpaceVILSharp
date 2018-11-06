namespace SpaceVIL
{
    abstract public class Prototype : VisualItem
    {
        private VisualItem _core;
        static int count = 0;
        public Prototype()
        {
            SetItemName("VisualItem_" + count);
            count++;
        }
    }
}
