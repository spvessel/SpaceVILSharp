using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    public interface ISize
    {
        void SetSize(int width, int height);
        Size GetSize();
        void SetMinWidth(int width);
        void SetWidth(int width);
        void SetMaxWidth(int width);
        int GetMinWidth();
        int GetWidth();
        int GetMaxWidth();
        void SetMinHeight(int height);
        void SetHeight(int height);
        void SetMaxHeight(int height);
        int GetMinHeight();
        int GetHeight();
        int GetMaxHeight();
    }
}
