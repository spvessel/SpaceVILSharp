using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    abstract class DialogWindow : CoreWindow
    {
        
        public DialogWindow(String message, String title) : base()
        {
            _message = message;
            _title = title;
            InitWindow();
        }
        
        String _message;
        public String DialogMessage
        {
            get
            {
                return _message;
            }
            /*set
            {
                _message = value;
            }*/
        }

        String _title;
        public String DialogTitle
        {
            get
            {
                return _title;
            }
            /*set
            {
                _title = value;
            }*/
        }

        bool _result = false;
        public bool DialogResult
        {
            /*
            get
            {
                return _result;
            }*/
            set
            {
                _result = value;
            }
        }

        public bool Result()
        {
            return _result;
        }
    }
}
