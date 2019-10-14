using System.Collections.Generic;
using SpaceVIL.Decorations;

namespace SpaceVIL.Core
{
    public interface ISubtractFigure
    {
        void SetSubtractFigure(CustomFigure figure);
        CustomFigure GetSubtractFigure();
        void SetPositionOffset(int x, int y);
        void SetSizeScale(float wScale, float hScale);
        int GetXOffset();
        int GetYOffset();
        float GetWidthScale();
        float GetHeightScale();
        void SetAlignment(params ItemAlignment[] alignments);
        ItemAlignment GetAlignment();
    }
}