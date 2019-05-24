using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public abstract class DialogWindow : CoreWindow
    {
        /// <summary>
        /// Constructs a DialogWindow
        /// </summary>
        public DialogWindow() : base()
        {
            IsDialog = true;
            IsCentered = true;
        }

        public override void Show()
        {
            InitWindow();
            base.Show();
        }
    }
}
