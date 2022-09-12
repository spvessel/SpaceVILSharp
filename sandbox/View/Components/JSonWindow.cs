using System.IO;
using SpaceVIL;
using View.json;

namespace View.Components
{
    public class JSonWindow : ActiveWindow {
    public override void InitWindow()
    {
        JsonApplier ja = new JsonApplier();
        string currentPath = Directory.GetCurrentDirectory() + Path.DirectorySeparatorChar;
        string jsWindow = "JsonLayout" + Path.DirectorySeparatorChar + "window.json";
        ja.ApplyJson(currentPath + jsWindow, this);
        string jsStyle = "JsonLayout" + Path.DirectorySeparatorChar + "style.json";
        ButtonCore btn = new ButtonCore("JSON");
        AddItem(btn);
        ja.ApplyJson(currentPath + jsStyle, btn);
    }
}
}