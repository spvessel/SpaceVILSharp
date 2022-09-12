using SpaceVIL;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class JSonLayoutView : Frame
    {
        private ActiveWindow jsonWindow = null;
        public override void InitElements()
        {
            base.InitElements();

            ButtonCore jsonBtn = new ButtonCore("Run JSon based window");
            jsonBtn.SetAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            jsonBtn.SetSize(200, 46);
            jsonBtn.SetBorderRadius(6);
            jsonBtn.SetBackground(Palette.Purple);
            jsonBtn.EventMouseClick += (sender, args) => {
                if (jsonWindow == null)
                {
                    jsonWindow = new JSonWindow();
                }
                jsonWindow.Show();
            };

            AddItem(jsonBtn);
        }
    }
}