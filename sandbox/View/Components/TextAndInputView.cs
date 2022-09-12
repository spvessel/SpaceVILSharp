using System.Drawing;
using System.Drawing.Text;
using System.IO;
using SpaceVIL;
using SpaceVIL.Core;
using SpaceVIL.Decorations;
using View.Decorations;

namespace View.Components
{
    public class TextAndInputView: ListBox
    {
        readonly PrivateFontCollection fontCollection = new PrivateFontCollection();
        private Font amaticLocalFont = null;
        public override void InitElements()
        {
            base.InitElements();

            SetBackground(Palette.Transparent);
            SetSelectionVisible(false);

            ListArea layout = GetArea();
            layout.SetPadding(30, 2, 30, 2);
            layout.SetSpacing(0, 20);

            Label simpleLabel = new Label("Label - Font: Ubuntu, Size: 12, Style: Regular.");
            simpleLabel.SetHeightPolicy(SizePolicy.Fixed);
            simpleLabel.SetHeight(simpleLabel.GetTextHeight() + 6);
            simpleLabel.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            Label italicLabel = new Label("Label - Font: Ubuntu, Size: 16, Style: Italic.");
            italicLabel.SetFontSize(16);
            italicLabel.SetFontStyle(FontStyle.Italic);
            italicLabel.SetHeightPolicy(SizePolicy.Fixed);
            italicLabel.SetHeight(italicLabel.GetTextHeight() + 6);
            italicLabel.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            Label boldLabel = new Label("Label - Font: Ubuntu, Size: 16, Style: Bold.");
            boldLabel.SetFontSize(16);
            boldLabel.SetFontStyle(FontStyle.Bold);
            boldLabel.SetHeightPolicy(SizePolicy.Fixed);
            boldLabel.SetHeight(boldLabel.GetTextHeight() + 6);
            boldLabel.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            fontCollection.AddFontFile(Directory.GetCurrentDirectory() + @"/font/AmaticSC-Regular.ttf");
            FontFamily family = fontCollection.Families[0];
            amaticLocalFont = new Font(family, 30);

            Label amaticLabel = new Label(
                    "Label - Font: AmaticSC, Size: 30, Style: Regular.\nAlmost before we knew it, we had left the ground.");
            amaticLabel.SetFont(amaticLocalFont);
            amaticLabel.SetHeightPolicy(SizePolicy.Fixed);
            amaticLabel.SetHeight(amaticLabel.GetTextHeight() + 6);
            amaticLabel.SetTextAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);

            TextView textView = new TextView();
            textView.SetHeightPolicy(SizePolicy.Fixed);
            textView.SetText(GetTextForElements());

            SpinItem spinItem = new SpinItem();
            spinItem.SetPassEvents(false);
            spinItem.SetAccuracy(1);
            spinItem.SetMaxWidth(400);
            spinItem.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            spinItem.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

            TextEdit textEdit = new TextEdit();
            textEdit.SetPassEvents(false);
            textEdit.SetSubstrateText("Login:");
            textEdit.SetMaxWidth(400);
            textEdit.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            textEdit.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

            PasswordLine passwordLine = new PasswordLine();
            passwordLine.SetPassEvents(false);
            passwordLine.SetSubstrateText("Password:");
            passwordLine.SetMaxWidth(400);
            passwordLine.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            passwordLine.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

            TextArea textArea = new TextArea(GetTextForElements());
            textArea.SetPassEvents(false);
            textArea.SetWrapText(true);
            textArea.SetHeightPolicy(SizePolicy.Fixed);
            textArea.SetMaxWidth(400);
            textArea.SetHeight(200);
            textArea.SetAlignment(ItemAlignment.HCenter, ItemAlignment.VCenter);
            textArea.Effects().Add(new Shadow(5, new Position(3, 3), Palette.BlackShadow));

            AddItems(ComponentsFactory.MakeHeaderLabel("Label implementation:"), simpleLabel, italicLabel, boldLabel, amaticLabel,
                    ComponentsFactory.MakeHeaderLabel("TextView implementation:"), textView,
                    ComponentsFactory.MakeHeaderLabel("Text input elements:"), spinItem, textEdit, passwordLine, textArea);
        }

        private string GetTextForElements()
        {
            return "The quick brown fox jumps over the lazy dog. " + "The five boxing wizards jump quickly. "
                    + "Pack my box with five dozen liquor jugs.\n" + "Jived fox nymph grabs quick waltz. "
                    + "Glib jocks quiz nymph to vex dwarf. " + "Sphinx of black quartz, judge my vow.";
        }
    }
}