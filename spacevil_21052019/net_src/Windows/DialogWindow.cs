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
        public DialogWindow()
        {
            InitWindow();
        }

        String _title = String.Empty;

        /// <summary>
        /// DialogWindow title text
        /// </summary>
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
    }
}
