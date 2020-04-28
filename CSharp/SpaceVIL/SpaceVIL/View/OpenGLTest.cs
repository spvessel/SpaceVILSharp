using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace View
{
    public class OpenGLTest : ActiveWindow
    {
        public override void InitWindow()
        {
            SetParameters(this.GetType().Name, this.GetType().Name, 800, 800, false);
            IsCentered = true;
            IsMaximized = true;

            // OneCubeExample();
            // MultipleCubes();
        }

        // private void OneCubeExample()
        // {
        //     TitleBar titleBar = new TitleBar(this.GetType().Name);

        //     OpenGLLayer ogl = new OpenGLLayer();
        //     ogl.SetMargin(0, titleBar.GetHeight(), 0, 0);

        //     HorizontalStack toolbar = GetToolbarLayout();

        //     ImagedButton btnRotateLeft = GetImagedButton(EmbeddedImage.ArrowUp, -90);
        //     ImagedButton btnRotateRight = GetImagedButton(EmbeddedImage.ArrowUp, 90);

        //     HorizontalSlider zoom = GetSlider();

        //     ImagedButton btnRestoreView = GetImagedButton(EmbeddedImage.Refresh, 0);

        //     // adding
        //     AddItems(titleBar, ogl);

        //     AddItems(toolbar);
        //     toolbar.AddItems(btnRotateLeft, btnRotateRight, zoom, btnRestoreView);

        //     // assign events
        //     btnRestoreView.EventMousePress += (sender, args) =>
        //     {
        //         ogl.RestoreView();
        //     };

        //     btnRotateLeft.EventMousePress += (sender, args) =>
        //     {
        //         ogl.Rotate(KeyCode.Left);
        //     };

        //     btnRotateRight.EventMousePress += (sender, args) =>
        //     {
        //         ogl.Rotate(KeyCode.Right);
        //     };

        //     zoom.EventValueChanged += (sender) =>
        //     {
        //         ogl.SetZoom(zoom.GetCurrentValue());
        //     };

        //     // Set focus
        //     ogl.SetFocus();
        //     zoom.SetCurrentValue(3);
        // }

        // private void MultipleCubes()
        // {
        //     TitleBar titleBar = new TitleBar(this.GetType().Name);

        //     FreeArea area = new FreeArea();
        //     area.SetMargin(0, titleBar.GetHeight(), 0, 0);

        //     AddItems(titleBar, area);

        //     List<IBaseItem> content = new List<IBaseItem>();

        //     for (int row = 0; row < 3; row++)
        //     {
        //         for (int column = 0; column < 3; column++)
        //         {
        //             ResizableItem frame = new ResizableItem();
        //             frame.SetBorder(new Border(Color.Gray, new CornerRadius(), 2));
        //             frame.SetPadding(5, 5, 5, 5);
        //             frame.SetBackground(100, 100, 100);
        //             frame.SetSize(200, 200);
        //             frame.SetPosition(90 + row * 210, 60 + column * 210);
        //             area.AddItem(frame);
        //             content.Add(frame);

        //             frame.EventMousePress += (sender, args) =>
        //             {
        //                 content.Remove(frame);
        //                 content.Add(frame);
        //                 area.SetContent(content);
        //             };

        //             OpenGLLayer ogl = new OpenGLLayer();
        //             ogl.SetMargin(0, 30, 0, 0);
        //             frame.AddItem(ogl);
        //         }
        //     }
        // }

        public static ImagedButton GetImagedButton(EmbeddedImage image, float imageRotationAngle)
        {
            ImagedButton btn = new ImagedButton(DefaultsService.GetDefaultImage(image, EmbeddedImageSize.Size32x32),
                    imageRotationAngle);
            return btn;
        }

        public static HorizontalSlider GetSlider()
        {
            HorizontalSlider slider = new HorizontalSlider();
            slider.SetStep(0.2f);
            slider.SetMinValue(2f);
            slider.SetMaxValue(10f);
            slider.SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            slider.SetSize(150, 30);
            slider.SetAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
            slider.SetMargin(40, 0, 40, 0);
            slider.Handler.SetShadow(5, 0, 2, Color.Black);
            return slider;
        }

        public static HorizontalStack GetToolbarLayout()
        {
            HorizontalStack layout = new HorizontalStack();
            layout.SetContentAlignment(ItemAlignment.HCenter);
            layout.SetAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
            layout.SetSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            layout.SetHeight(30);
            layout.SetSpacing(5, 0);
            layout.SetMargin(20, 0, 20, 20);
            return layout;
        }
    }

    public class ImagedButton : ButtonCore
    {
        ImageItem _image = null;

        public ImagedButton(Bitmap image, float imageRotationAngle)
        {
            SetBackground(0xFF, 0xB5, 0x6F);
            SetSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            SetSize(30, 30);
            SetAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
            SetPadding(5, 5, 5, 5);
            SetShadow(5, 0, 2, Color.Black);
            IsFocusable = false;

            _image = new ImageItem(image, false);
            _image.SetRotationAngle(imageRotationAngle);
            _image.SetColorOverlay(Color.White);
        }

        public override void InitElements()
        {
            base.InitElements();
            AddItem(_image);
        }
    }
}