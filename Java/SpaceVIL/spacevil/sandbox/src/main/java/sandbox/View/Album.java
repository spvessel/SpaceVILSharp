package sandbox.View;

import java.awt.Color;
import java.awt.Font;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.*;
import com.spvessel.spacevil.Core.*;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Flags.*;

public class Album extends Prototype {
    public EventCommonMethodState onDoubleClick = new EventCommonMethodState();
    
    @Override
    public void release() {
        onDoubleClick.clear();
    }

    ButtonToggle _expand;// = new ButtonToggle();
    public Label name;// = new Label();
    HorizontalStack _topLayout = new HorizontalStack();
    HorizontalStack _bottomLayout = new HorizontalStack();

    private TextEdit _pathEdit;// = new TextEdit();

    public String getPath() {
        return _pathEdit.getText();
    }

    static int count = 0;

    public Album(String n, String path) {
        setItemName("Album_" + count++);
        setAlignment(ItemAlignment.Left, ItemAlignment.Top);
        setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
        setHeight(30);
        setSpacing(0, 10);
        setMargin(3, 5, 3, 5);
        setBackground(new Color(0, 0, 0, 0));
        Effects.addEffect(this, new Shadow(5, new Color(0, 0, 0, 200)));

        _expand = new ButtonToggle();
        name = new Label();
        _pathEdit = new TextEdit();

        name.setText(n);
        _pathEdit.setText(path);
    }

    @Override
    public void initElements() {
        
        ImageItem _arrow = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.ArrowLeft, EmbeddedImageSize.Size32x32));
        ButtonCore _remove = new ButtonCore();

        
        Label _pathLabel = new Label("Path:");
        ButtonCore _pathBrowse = new ButtonCore();

        _bottomLayout.setVisible(false);
        // top
        _topLayout.setHeightPolicy(SizePolicy.Fixed);
        _topLayout.setHeight(30);
        _topLayout.setSpacing(5, 0);
        _topLayout.setBackground(new Color(255, 255, 255, 20));

        _expand.setSize(20, 30);
        _expand.setBackground(25, 25, 25);
        // _expand.getState(ItemStateType.TOGGLED).background = new Color(25, 25, 25);
        _expand.setPadding(4, 9, 4, 9);
        _arrow.setRotationAngle(180);
        _arrow.setColorOverlay(new Color(210, 210, 210));
        _arrow.keepAspectRatio(true);

        name.setHeightPolicy(SizePolicy.Fixed);
        name.setBackground(new Color(255,0,0,50));
        name.setHeight(30);
        name.setMargin(5, 0, 0, 0);
        name.setFontSize(16);
        name.setFontStyle(Font.BOLD);

        _remove.setItemName("_remove");
        _remove.setSize(12, 12);
        _remove.setCustomFigure(new Figure(false, GraphicsMathService.getCross(12, 12, 3, 45)));
        _remove.setBackground(100, 100, 100);
        _remove.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        _remove.setMargin(0, 0, 5, 0);

        // bottom
        _bottomLayout.setHeightPolicy(SizePolicy.Fixed);
        _bottomLayout.setHeight(30);
        _bottomLayout.setSpacing(5, 0);
        _bottomLayout.setAlignment(ItemAlignment.Left, ItemAlignment.Bottom);

        _pathLabel.setWidthPolicy(SizePolicy.Fixed);
        _pathLabel.setFontSize(14);
        _pathLabel.setWidth(_pathLabel.getTextWidth() + 5);

        _pathBrowse.setSize(30, 30);
        _pathBrowse.setBackground(255, 255, 255, 20);
        _pathBrowse.setPadding(7, 7, 7, 7);
        // _pathBrowse.getState(ItemStateType.HOVERED).background = new Color(255, 255, 255, 150);

        _pathEdit.setBackground(Color.white);

        addItems(_topLayout
                , _bottomLayout
        );

        _topLayout.addItems(_expand, name, _remove);
        _bottomLayout.addItems(_pathLabel, _pathEdit, _pathBrowse);

        _expand.addItem(_arrow);
        _pathBrowse.addItem(new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.Pencil, EmbeddedImageSize.Size32x32), false));

        // events
        _expand.eventToggle.add((sender, args) -> {
            _bottomLayout.setVisible(!_bottomLayout.isVisible());
            if (_bottomLayout.isVisible()) {
                setHeight(70);
                _arrow.setRotationAngle(90);

            } else {
                setHeight(30);
                _arrow.setRotationAngle(180);
            }
        });

        name.eventMouseDoubleClick.add((sender, args) -> {
            onDoubleClick.execute(this);
        });

        _remove.eventMouseClick.add((sender, args) -> {
            System.out.println(_remove.isCustomFigure() == null);
        });
    }
}