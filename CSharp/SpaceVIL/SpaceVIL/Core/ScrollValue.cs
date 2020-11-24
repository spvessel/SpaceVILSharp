using System;

namespace SpaceVIL.Core
{
    public sealed class ScrollValue
    {
        public double DX = 0;
        public double DY = 0;

        public void SetValues(double dx, double dy) {
            DX = dx;
            DY = dy;
        }

        public override String ToString() {
            return DX + " " + DY;
        }
    }
}