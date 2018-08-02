using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace SpaceVIL
{
    public interface IBehavior
    {
        void SetAlignment(ItemAlignment alignment);
        ItemAlignment GetAlignment();
        void SetWidthPolicy(SizePolicy policy);
        SizePolicy GetWidthPolicy();
        void SetHeightPolicy(SizePolicy policy);
        SizePolicy GetHeightPolicy();
    }
}
