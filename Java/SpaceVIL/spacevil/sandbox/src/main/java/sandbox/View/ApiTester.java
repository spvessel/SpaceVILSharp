package sandbox.View;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.TextArea;

public class ApiTester extends ActiveWindow {

    @Override
    public void initWindow() {
        setParameters("ApiTester", "ApiTester", 800, 800, true);
        TextArea textArea = new TextArea();
        addItem(textArea);
    }
    
}
