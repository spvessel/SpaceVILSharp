using System.IO;
using SpaceVIL;
using View.json;

namespace View
{
    class JsonTest : ActiveWindow
    {
        override public void InitWindow()
        {
            JsonApplier ja = new JsonApplier();
            var currentPath = Directory.GetCurrentDirectory() + Path.DirectorySeparatorChar;
            var jsWindow = "JsonLayout" + Path.DirectorySeparatorChar + "window.json";

            ja.ApplyJson(currentPath + jsWindow, this);

            var jsStyle = "JsonLayout" + Path.DirectorySeparatorChar + "style.json";
            ButtonCore btn = new ButtonCore("JSON");
            AddItem(btn);
            ja.ApplyJson(currentPath + jsStyle, btn);
        }
    }
}