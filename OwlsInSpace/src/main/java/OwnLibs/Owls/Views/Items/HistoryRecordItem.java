package OwnLibs.Owls.Views.Items;

import java.awt.Color;

import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.Label;
import com.spvessel.spacevil.Prototype;
import com.spvessel.spacevil.Core.EventCommonMethod;
import com.spvessel.spacevil.Decorations.CustomFigure;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedCursor;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

public class HistoryRecordItem extends Prototype {
    HorizontalStack layout;
    ButtonCore removeBtn;
    Label nameLabel;

    public EventCommonMethod eventOnRemove = new EventCommonMethod();

    public HistoryRecordItem(String record) {
        layout = new HorizontalStack();
        removeBtn = new ButtonCore();
        nameLabel = new Label(record, false);
        setStyle(Style.getDefaultCommonStyle());
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        setHeight(25);
        setBackground(65, 65, 65);
        setPadding(10, 0, 5, 0);
        addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 20)));
        setCursor(EmbeddedCursor.HAND);
    }

    @Override
    public void initElements() {
        removeBtn.setBackground(120, 120, 120);
        removeBtn.setSize(10, 10);
        removeBtn.setCustomFigure(new CustomFigure(false, GraphicsMathService.getCross(16, 16, 2, 45)));
        removeBtn.addItemState(ItemStateType.HOVERED, new ItemState(new Color(232, 120, 120)));

        removeBtn.eventMouseClick.add((sender, args) -> {
            remove();
        });

        addItem(layout);
        layout.addItems(nameLabel, removeBtn);
    }

    public void remove() {
        getParent().removeItem(this);
        eventOnRemove.execute();
    }
}