using System;
using System.Threading;
using SpaceVIL;
using SpaceVIL.Core;

namespace View
{
    public class MyDialogBox : DialogWindow
    {
        public MyDialogBox() { }

        public override void InitWindow()
        {
            // window appearance
            SetParameters("MyDialogBox:", "MyDialogBox:", 400, 300, false);
            SetBackground(55, 55, 55);
            SetSize(400, 300);

            // add titlebar and button to the window
            TitleBar title = new TitleBar("MyDialogBox:");
            ButtonCore okButton = new ButtonCore("Ok");
            okButton.SetWidth(100);
            okButton.SetAlignment(ItemAlignment.Bottom, ItemAlignment.HCenter);
            okButton.SetMargin(0, 0, 0, 20);
            AddItems(title, okButton);

            // change actions of titlebar close button
            title.GetMinimizeButton().SetVisible(false); // hides button
            title.GetMaximizeButton().SetVisible(false); // hides button
            title.GetCloseButton().EventMouseClick = null;
            title.GetCloseButton().EventMouseClick += (sender, args) =>
            {
                Close(); // now it closes the MyDialogBox
            };

            // Ok button will set the result
            okButton.EventMouseClick += (sender, args) =>
            {
                _result = true; // set result
                Close(); // close
            };
        }

        // event which will invoked when MyDialogBox is closed to perform actions.
        CoreWindow _handler = null;

        // show the MyDialogBox.
        public void Show(CoreWindow handler)
        {
            _handler = handler;
            _result = false;
            base.Show();
        }

        // close the MyDialogBox.
        public override void Close()
        {
            // perform assigned actions
            OnCloseDialog?.Invoke();

            base.Close();

            try
            {
                lock (_handler)
                {
                    Monitor.Pulse(_handler);
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        // getting result
        bool _result = false;

        public bool GetResult()
        {
            return _result;
        }

        public void ShowAndWait(CoreWindow handler)
        {
            Show(handler);
            try
            {
                lock(handler) {
                    // handler.Hold();
                    Monitor.Wait(_handler);
                    handler.Proceed();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}