using System;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using SpaceVIL.Decorations;

namespace SpaceVIL
{
    public class OpenFileDialog : OpenDialog
    {
        public OpenFileDialog()
        {

        }
        public override void InitElements()
        {
            // important!
            base.InitElements();

            // elements
            VerticalStack layout = new VerticalStack();
            layout.SetAlignment(ItemAlignment.Top | ItemAlignment.HCenter);
            layout.SetMargin(0, 30, 0, 0);
            layout.SetPadding(6, 6, 6, 6);
            layout.SetSpacing(0, 2);
            layout.SetBackground(255, 255, 255, 20);

            // Create, View, Backward, Refresh, Rename
            HorizontalStack toolbar = new HorizontalStack();
            toolbar.SetHeightPolicy(SizePolicy.Fixed);
            toolbar.SetHeight(30);
            toolbar.SetBackground(45, 45, 45);
            toolbar.SetSpacing(3, 0);
            toolbar.SetPadding(6, 0, 0, 0);

            Style styleBtn = Style.GetButtonCoreStyle();
            styleBtn.SetSize(24, 24);
            styleBtn.BorderRadius = new CornerRadius();
            ButtonCore btnBackward = new ButtonCore("<");
            ButtonCore btnCreate = new ButtonCore("+");
            ButtonCore btnRename = new ButtonCore("R");
            ButtonCore btnRefresh = new ButtonCore("@");
            ButtonCore btnView = new ButtonCore("=");
            styleBtn.SetStyle(btnBackward, btnCreate, btnRename, btnRefresh, btnView);

            // AdresssLine
            TextEdit addressLine = new TextEdit();
            addressLine.SetFontSize(14);
            addressLine.SetForeground(210, 210, 210);
            addressLine.SetBackground(50, 50, 50);
            addressLine.SetHeight(24);

            // ListBox --> contect menu
            ListBox fileList = new ListBox();

            // SelectionItem: icon, text etc --> context menu

            // Open, Cancel
            Frame controlPanel = new Frame();
            controlPanel.SetHeightPolicy(SizePolicy.Fixed);
            controlPanel.SetHeight(40);
            controlPanel.SetBackground(45, 45, 45);
            controlPanel.SetPadding(6, 6, 6, 6);
            ButtonCore openBtn = new ButtonCore("Open");
            openBtn.SetSize(100, 30);
            openBtn.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Right);
            openBtn.SetMargin(0, 0, 105, 0);
            openBtn.SetBorderRadius(0);
            ButtonCore cancelBtn = new ButtonCore("Cancel");
            cancelBtn.SetSize(100, 30);
            cancelBtn.SetAlignment(ItemAlignment.VCenter | ItemAlignment.Right);
            cancelBtn.SetBorderRadius(0);
            cancelBtn.EventMouseClick += (sender, args) =>
            {
                Close();
            };

            Window.AddItems(layout);
            layout.AddItems(toolbar, addressLine, fileList, controlPanel);
            toolbar.AddItems(btnBackward, btnCreate, btnRename, btnRefresh, btnView);
            controlPanel.AddItems(openBtn, cancelBtn);
            
            fileList.SetHScrollBarVisible(ScrollBarVisibility.AsNeeded);
            fileList.SetVScrollBarVisible(ScrollBarVisibility.AsNeeded);
        }
    }
}