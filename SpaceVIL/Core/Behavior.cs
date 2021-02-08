using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    internal class Behavior : IBehavior
    {
        public ItemAlignment _alignment = ItemAlignment.Left | ItemAlignment.Top;
        public void SetAlignment(ItemAlignment alignment)
        {
            if (alignment.HasFlag(ItemAlignment.Left) && alignment.HasFlag(ItemAlignment.Right))
            {
                alignment &= ~ItemAlignment.Right;
            }
            if (alignment.HasFlag(ItemAlignment.Top) && alignment.HasFlag(ItemAlignment.Bottom))
            {
                alignment &= ~ItemAlignment.Bottom;
            }

            if(alignment.HasFlag(ItemAlignment.HCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Left) || alignment.HasFlag(ItemAlignment.Right))
                {
                    alignment &= ~(ItemAlignment.Left | ItemAlignment.Right);
                }
            }
            if (alignment.HasFlag(ItemAlignment.VCenter))
            {
                if (alignment.HasFlag(ItemAlignment.Top) || alignment.HasFlag(ItemAlignment.Bottom))
                {
                    alignment &= ~(ItemAlignment.Top | ItemAlignment.Bottom);
                }
            }
            _alignment = alignment;
        }
        public ItemAlignment GetAlignment()
        {
            return _alignment;
        }

        public SizePolicy _w_policy = SizePolicy.Fixed;
        public void SetWidthPolicy(SizePolicy policy)
        {
            _w_policy = policy;
        }
        public SizePolicy GetWidthPolicy()
        {
            return _w_policy;
        }

        public SizePolicy _h_policy = SizePolicy.Fixed;
        public void SetHeightPolicy(SizePolicy policy)
        {
            _h_policy = policy;
        }
        public SizePolicy GetHeightPolicy()
        {
            return _h_policy;
        }
    }
}
