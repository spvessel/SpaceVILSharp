using System.Drawing;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    public class Simple : ActiveWindow
    {

        public override void InitWindow()
        {
            WindowManager.SetRenderType(RenderType.IfNeeded);
            SetSize(400, 400);
            SetAntiAliasingQuality(MSAA.MSAA8x);

            Ellipse ellipse = new Ellipse(64);
            ellipse.SetSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            ellipse.SetBackground(255, 155, 155);
            ellipse.SetMargin(100, 100, 100, 100);
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(140, 140), Color.FromArgb(255, 0, 255)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(120, 120), Color.FromArgb(0, 0, 255)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(100, 100), Color.FromArgb(128, 255, 255)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(80, 80), Color.FromArgb(0, 255, 0)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(60, 60), Color.FromArgb(255, 255, 128)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(40, 40), Color.FromArgb(255, 128, 0)));
            ellipse.Effects().Add(new Shadow(10, new SpaceVIL.Core.Size(20, 20), Color.FromArgb(255, 0, 0)));

            AddItem(ellipse);
        }
    }
}