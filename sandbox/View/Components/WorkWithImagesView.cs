using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class WorkWithImagesView: VerticalStack
    {
        public override void InitElements()
        {
            base.InitElements();

            SetPadding(50, 20, 50, 50);
            SetSpacing(0, 20);

            ImageItem strechable = new ImageItem(GraphicsMathService.ScaleBitmap(ImageResources.Art, 100, 100));
            strechable.SetMaxHeight(500);
            strechable.SetImageQuality(ImageQuality.Rough);

            ImageItem nonstrechable = new ImageItem(ImageResources.Art);
            nonstrechable.KeepAspectRatio(true);
            nonstrechable.SetMaxHeight(500);
            nonstrechable.SetBackground(Palette.BlackShadow);

            ImageItem logo = new ImageItem(ImageResources.SpaveVILLogo, true);
            logo.KeepAspectRatio(true);
            logo.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            logo.SetSize(100, 100);
            logo.EventMouseClick += (sender, args) => {
                System.Console.WriteLine("logo image click!");
            };
            logo.EventMouseHover += (sender, args) => {
                logo.SetCursor(DefaultsService.GetDefaultImage(EmbeddedImage.ArrowUp, EmbeddedImageSize.Size64x64), 32, 32);
            };
            logo.EventMouseLeave += (sender, args) => {
                logo.SetCursor(DefaultsService.GetDefaultCursor());
            };

            HorizontalSlider logoRotator = new HorizontalSlider();
            logoRotator.SetStep(1);
            logoRotator.SetMargin(25, 0, 25, 0);
            logoRotator.SetMaxValue(360);
            logoRotator.EventValueChanged += (sender) => {
                logo.SetRotationAngle(logoRotator.GetCurrentValue());
            };

            AddItems(ComponentsFactory.MakeHeaderLabel("Image items implementation:"), strechable, nonstrechable,
                    ComponentsFactory.MakeHeaderLabel("Rotate image:"), logo, logoRotator);
            }
    }
}