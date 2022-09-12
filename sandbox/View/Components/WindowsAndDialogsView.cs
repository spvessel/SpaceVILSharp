using SpaceVIL;
using SpaceVIL.Common;
using SpaceVIL.Core;
using View.Decorations;

namespace View.Components
{
    public class WindowsAndDialogsView: ListBox
    {
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 20);

            IBaseItem openWindow = ComponentsFactory.MakeActionButton("Open Window", Palette.OrangeLight, () => {
                ActiveWindow window = new CustomActiveWindow();
                window.Show();
            });
            IBaseItem openDialogWindow = ComponentsFactory.MakeActionButton("Open Dialog", Palette.Pink, () => {
                DialogWindow window = new CustomDialogWindow();
                window.Show();
            });
            IBaseItem openMessageBox = ComponentsFactory.MakeActionButton("Open MessageBox", Palette.GreenLight, () => {
                MessageBox messageBox = new MessageBox("This is the MessageBox.\nMessageBox is dialog window.", "Message:");
                messageBox.OnCloseDialog += () => {
                    System.Console.WriteLine("MessageBox result: " + messageBox.GetResult());
                };
                messageBox.Show();
            });
            IBaseItem openMessageItem = ComponentsFactory.MakeActionButton("Open MessageItem", Palette.Blue, () => {
                MessageItem messageItem = new MessageItem(
                        "This is the MessageItem.\nMessageItem is drawn on the current window.", "Message:");
                messageItem.OnCloseDialog += () => {
                    System.Console.WriteLine("MessageBox result: " + messageItem.GetResult());
                };
                messageItem.Show(GetHandler());
            });
            IBaseItem openInputBox = ComponentsFactory.MakeActionButton("Open InputBox", Palette.GreenLight, () => {
                InputBox inputBox = new InputBox("Input text below:", "OK");
                inputBox.OnCloseDialog += () => {
                    System.Console.WriteLine("InputBox result: " + inputBox.GetResult());
                };
                inputBox.Show();
            });
            IBaseItem openInputDialog = ComponentsFactory.MakeActionButton("Open InputDialog", Palette.Blue, () => {
                InputDialog inputDialog = new InputDialog("Input text below:", "OK");
                inputDialog.OnCloseDialog += () => {
                    System.Console.WriteLine("InputDialog result: " + inputDialog.GetResult());
                };
                inputDialog.Show(GetHandler());
            });
            IBaseItem openFileBrowserBox = ComponentsFactory.MakeActionButton("Open OpenEntryBox", Palette.GreenLight, () => {
                OpenEntryBox entryBox = new OpenEntryBox("Open File:", FileSystemEntryType.File, OpenDialogType.Save);
                entryBox.AddFilterExtensions("Text files (*.txt);*.txt",
                        "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
                entryBox.OnCloseDialog += () => {
                    System.Console.WriteLine("OpenEntryBox result: " + entryBox.GetResult());
                };
                entryBox.Show();
            });
            IBaseItem openFileBrowserDialog = ComponentsFactory.MakeActionButton("Open OpenEntryDialog", Palette.Blue, () => {
                OpenEntryDialog entryDialog = new OpenEntryDialog("Open File:", FileSystemEntryType.File,
                        OpenDialogType.Save);
                entryDialog.AddFilterExtensions("Text files (*.txt);*.txt",
                        "Images (*.png, *.bmp, *.jpg) ; *.png, *.bmp, *.jpg");
                entryDialog.OnCloseDialog += () => {
                    System.Console.WriteLine("OpenEntryDialog result: " + entryDialog.GetResult());
                };
                entryDialog.Show(GetHandler());
            });

            AddItems(ComponentsFactory.MakeHeaderLabel("Common window implementations:"), openWindow, openDialogWindow,
                    ComponentsFactory.MakeHeaderLabel("Special window implementations:"), openMessageBox, openMessageItem,
                    openInputBox, openInputDialog, ComponentsFactory.MakeHeaderLabel("File browser window implementations:"),
                    openFileBrowserBox, openFileBrowserDialog);
        }
    }

    internal class CustomActiveWindow: ActiveWindow {
        public override void InitWindow()
        {
            SetPadding(10, 30, 10, 30);
            SetWindowTitle("Ordinary Window.");
            SetIcon(DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size64x64),
                    DefaultsService.GetDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));
            TextView text = new TextView();
            text.SetText("ActiveWindow is an ordinary window.");
            AddItem(text);
        }
    }

    internal class CustomDialogWindow: DialogWindow {
        public override void InitWindow()
        {
            SetPadding(10, 30, 10, 30);
            SetWindowTitle("Dialog Window.");
            SetIcon(DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size64x64),
                    DefaultsService.GetDefaultImage(EmbeddedImage.Eye, EmbeddedImageSize.Size32x32));
            TextView text = new TextView();
            text.SetText("DialogWindow is a dialog implementation.\nThe parent window is locked until the dialog is closed.");
            AddItem(text);
        }
    }
}