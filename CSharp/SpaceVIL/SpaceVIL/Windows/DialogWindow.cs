using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public abstract class DialogWindow : CoreWindow
    {
        
        public DialogWindow()
        {
            InitWindow();
        }

        String _title = String.Empty;
        public String DialogTitle
        {
            get
            {
                return _title;
            }
            set
            {
                _title = value;
            }
        }

        public override void Show()
        {
            base.Show();
        }
    }
}
