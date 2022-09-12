using System;
using System.Threading.Tasks;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class SlidersAndProgressBarsView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 10);

            // Sliders
            Grid slidersContainer = new Grid(1, 2);
            slidersContainer.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            slidersContainer.SetSize(430, 200);
            slidersContainer.SetSpacing(30, 30);
            slidersContainer.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            VerticalStack horizontalSliders = new VerticalStack();
            horizontalSliders.SetSpacing(0, 10);
            horizontalSliders.SetContentAlignment(ItemAlignment.HCenter);
            horizontalSliders.SetPadding(0, 50, 0, 50);
            HorizontalStack smoothHSliderContainer = new HorizontalStack();
            smoothHSliderContainer.SetSpacing(10, 0);
            Label smoothHSliderValue = new Label("0.0");
            smoothHSliderValue.SetMaxWidth(30);
            HorizontalSlider smoothHSlider = new HorizontalSlider();
            smoothHSlider.SetMaxValue(10);
            smoothHSlider.EventValueChanged += (sender) => {
                smoothHSliderValue.SetText(string.Format("{0:n2}", smoothHSlider.GetCurrentValue()));
            };
            HorizontalStack digitalHSliderContainer = new HorizontalStack();
            digitalHSliderContainer.SetSpacing(10, 0);
            Label digitalHSliderValue = new Label("0.0");
            digitalHSliderValue.SetMaxWidth(30);
            HorizontalSlider digitalHSlider = new HorizontalSlider();
            digitalHSlider.SetMaxValue(10);
            digitalHSlider.SetIgnoreStep(false);
            digitalHSlider.SetStep(2);
            digitalHSlider.EventValueChanged += (sender) => {
                digitalHSliderValue.SetText(string.Format("{0:n1}", digitalHSlider.GetCurrentValue()));
            };

            HorizontalStack verticalSliders = new HorizontalStack();
            verticalSliders.SetSpacing(30, 0);
            verticalSliders.SetContentAlignment(ItemAlignment.HCenter);
            verticalSliders.SetPadding(50, 0, 50, 0);
            VerticalStack smoothVSliderContainer = new VerticalStack();
            smoothVSliderContainer.SetSpacing(0, 10);
            Label smoothVSliderValue = new Label("0.0");
            smoothVSliderValue.SetMaxHeight(30);
            VerticalSlider smoothVSlider = new VerticalSlider();
            smoothVSlider.SetMaxValue(10);
            smoothVSlider.EventValueChanged += (sender) => {
                smoothVSliderValue.SetText(string.Format("{0:n2}", smoothVSlider.GetCurrentValue()));
            };
            VerticalStack digitalVSliderContainer = new VerticalStack();
            digitalVSliderContainer.SetSpacing(0, 10);
            Label digitalVSliderValue = new Label("0.0");
            digitalVSliderValue.SetMaxHeight(30);
            VerticalSlider digitalVSlider = new VerticalSlider();
            digitalVSlider.SetMaxValue(10);
            digitalVSlider.SetIgnoreStep(false);
            digitalVSlider.SetStep(2);
            digitalVSlider.EventValueChanged += (sender) => {
                digitalVSliderValue.SetText(string.Format("{0:n1}", digitalVSlider.GetCurrentValue()));
            };

            // ProgressBars
            HorizontalStack progressBarContainer = new HorizontalStack();
            progressBarContainer.SetSpacing(10, 0);
            progressBarContainer.SetContentAlignment(ItemAlignment.HCenter);
            progressBarContainer.SetPadding(150, 0, 150, 0);
            progressBarContainer.SetHeightPolicy(SizePolicy.Fixed);
            progressBarContainer.SetHeight(50);
            ProgressBar progressBar = new ProgressBar();
            progressBar.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            progressBar.SetCurrentValue(50);
            progressBar.SetMaxValue(100);
            ImageItem reloadIcon = new ImageItem(
                    DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32), false);
            reloadIcon.KeepAspectRatio(true);
            bool isReloading = false;
            Prototype reloadButton = ComponentsFactory.MakeActionButton("", Palette.WhiteGlass, () => {
                if (isReloading)
                {
                    return;
                }
                isReloading = true;
                WindowManager.SetRenderFrequency(RedrawFrequency.Ultra);
                try
                {
                    new Task(() => {
                        for (int i = 0; i <= progressBar.GetMaxValue(); i++)
                        {
                            progressBar.SetCurrentValue(i);
                            try
                            {
                                Task.Delay(20);
                            }
                            catch (Exception e) {}
                        }
                        WindowManager.SetRenderFrequency(RedrawFrequency.Low);
                        isReloading=  false;
                    }).Start();
                }
                catch (Exception e) {}
            });
            reloadButton.SetBorderRadius(15);
            reloadButton.SetMaxWidth(30);
            reloadButton.SetPadding(4, 4, 4, 4);

            Prototype loadButton = ComponentsFactory.MakeActionButton("LoadingScreen", () => {
                if (isReloading)
                {
                    return;
                }
                LoadingScreen loadScreen = new LoadingScreen();
                loadScreen.Show(GetHandler());
                isReloading = true;
                try
                {
                    new Task(() => {
                        for (int i = 0; i <= progressBar.GetMaxValue(); i++)
                        {
                            loadScreen.SetValue(i);
                            try
                            {
                                Task.Delay(20).Wait();
                            }
                            catch (Exception e) {}
                        }
                        isReloading = false;
                        loadScreen.SetToClose();
                    }).Start();
                }
                catch (Exception e) {}
            });

            AddItems(ComponentsFactory.MakeHeaderLabel("Slider implementations:"), slidersContainer,
                    ComponentsFactory.MakeHeaderLabel("ProgressBar implementations:"), progressBarContainer, loadButton);
            slidersContainer.AddItems(horizontalSliders, verticalSliders);
            horizontalSliders.AddItems(smoothHSliderContainer, digitalHSliderContainer);
            smoothHSliderContainer.AddItems(smoothHSlider, smoothHSliderValue);
            digitalHSliderContainer.AddItems(digitalHSlider, digitalHSliderValue);
            verticalSliders.AddItems(smoothVSliderContainer, digitalVSliderContainer);
            smoothVSliderContainer.AddItems(smoothVSlider, smoothVSliderValue);
            digitalVSliderContainer.AddItems(digitalVSlider, digitalVSliderValue);

            progressBarContainer.AddItems(progressBar, reloadButton);
            reloadButton.AddItem(reloadIcon);
        }
    }
}