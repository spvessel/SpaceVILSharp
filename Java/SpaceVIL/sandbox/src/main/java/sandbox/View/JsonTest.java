package sandbox.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Frame;
import sandbox.View.json.JsonApplier;

import java.io.*;
import java.nio.file.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTest extends ActiveWindow {
    @Override
    public void initWindow() {
        JsonApplier ja = new JsonApplier();
        String currentPath = Paths.get("").toAbsolutePath().toString() + File.separator;

        String jsWindow = "JsonLayout" + File.separator + "window.json";
        ja.applyJson(currentPath + jsWindow, this);

        String jsStyle = "JsonLayout" + File.separator + "style.json";
        ButtonCore btn = new ButtonCore("JSON");
        addItem(btn);
        ja.applyJson(currentPath + jsStyle, btn);
    }
}
