using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL.Core
{
    /// <summary>
    /// An interface that defines items that can receive common keyboard shortcuts 
    /// (copy, paste, cut and etc.) and describes its attributes.
    /// </summary>
    public interface ITextShortcuts
    {
        /// <summary>
        /// Method for pasting text.
        /// </summary>
        /// <param name="pasteStr">Text for pasting.</param>
        void PasteText(string pasteStr);
        /// <summary>
        /// Method for getting selected text.
        /// </summary>
        /// <returns>Selected text.</returns>
        string GetSelectedText();
        /// <summary>
        /// Method for cutting selected text.
        /// </summary>
        /// <returns>Cutted text.</returns>
        string CutText();
        /// <summary>
        /// Method for undo last change.
        /// </summary>
        void Undo();
        /// <summary>
        /// Method for redo last undo action.
        /// </summary>
        void Redo();
        /// <summary>
        /// Method for selecting all text in the item.
        /// </summary>
        void SelectAll();
    }
}
