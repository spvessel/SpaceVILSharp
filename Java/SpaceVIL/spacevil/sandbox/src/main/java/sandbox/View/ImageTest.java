package sandbox.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.CheckBox;
import com.spvessel.spacevil.ComboBox;
import com.spvessel.spacevil.Ellipse;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.HorizontalSlider;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.InputDialog;
import com.spvessel.spacevil.MenuItem;
import com.spvessel.spacevil.PopUpMessage;
import com.spvessel.spacevil.ProgressBar;
import com.spvessel.spacevil.RadioButton;
import com.spvessel.spacevil.Rectangle;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.ToolTip;
import com.spvessel.spacevil.VerticalStack;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IMouseMethodState;
import com.spvessel.spacevil.Core.Position;
import com.spvessel.spacevil.Core.Size;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Figure;
import com.spvessel.spacevil.Decorations.Effects;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Decorations.SubtractFigure;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.KeyMods;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.SizePolicy;

public class ImageTest extends ActiveWindow {
    ImageItem img;

    @Override
    public void initWindow() {
        isCentered = true;
        // isBorderHidden = true;
        setSize(500, 500);
        setWindowName("ImageTest");
        setWindowTitle("ImageTest");
        setAntiAliasingQuality(MSAA.MSAA8x);

        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);

        img = new ImageItem(
                // DefaultsService.getDefaultImage(EmbeddedImage.FILTER,
                // EmbeddedImageSize.SIZE_64X64), false);
                DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size64x64), false);
        // DragAnchor
        TitleBar title = new TitleBar("ImageTest");
        addItem(title);

        boolean isVertTest = false;

        if (isVertTest) {
            VerticalStack h_stack = new VerticalStack();
            h_stack.setMargin(0, title.getHeight(), 0, 0);
            h_stack.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
            h_stack.setBackground(255, 255, 255, 200);
            h_stack.setWidth(30);
            h_stack.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
            h_stack.setSpacing(0, 5);
            // h_stack.setContentAlignment(ItemAlignment.RIGHT);
            // h_stack.setVisible(false);

            addItem(h_stack);

            Rectangle r1 = new Rectangle();
            r1.setBackground(255, 165, 0);
            r1.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            r1.setMaxHeight(150);
            h_stack.addItem(r1);
            // for (int i = 0; i < 4; i++) {
            // Rectangle rect = new Rectangle();
            // rect.setBackground(0, 255, 0);
            // rect.setSize(30, 30);
            // rect.setMaxHeight(30);
            // rect.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            // h_stack.addItem(rect);
            // }
            Rectangle rect = new Rectangle();
            rect.setBackground(125, 125, 0);
            rect.setSize(30, 30);
            rect.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            h_stack.addItem(rect);

        } else {
            // Frame
            VerticalStack frame = new VerticalStack();
            frame.setBackground(60, 60, 60);
            frame.setItemName("Container");
            frame.setMargin(0, title.getHeight(), 0, 0);
            frame.setWidthPolicy(SizePolicy.Expand);
            frame.setHeightPolicy(SizePolicy.Expand);
            frame.setSpacing(0, 20);
            frame.setPadding(20, 60, 20, 20);
            addItem(frame);

            frame.addItems(new RadioButton("text1"), new RadioButton("text2"));
            frame.addItems(new CheckBox("text1"), new CheckBox("text2"));

            HorizontalStack h_stack = new HorizontalStack();
            h_stack.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
            h_stack.setBackground(255, 255, 255, 200);
            h_stack.setHeight(30);
            h_stack.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
            h_stack.setSpacing(5, 0);
            // h_stack.setContentAlignment(ItemAlignment.VCENTER);
            // h_stack.setVisible(false);

            frame.addItem(h_stack);

            Rectangle r1 = new Rectangle();
            r1.setBackground(255, 165, 0);
            r1.setSizePolicy(SizePolicy.Expand, SizePolicy.Expand);
            r1.setMaxWidth(150);
            h_stack.addItem(r1);
            // for (int i = 0; i < 4; i++) {
            // Rectangle rect = new Rectangle();
            // rect.setBackground(0, 255, 0);
            // rect.setSize(30, 30);
            // rect.setMaxWidth(30);
            // rect.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
            // h_stack.addItem(rect);
            // }
            h_stack.setVisible(false);
            Rectangle rect = new Rectangle();
            rect.setBackground(125, 125, 0);
            rect.setSize(30, 30);
            rect.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            h_stack.addItem(rect);

            ProgressBar pb = new ProgressBar();
            pb.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
            pb.setMargin(25, 0, 25, 0);
            pb.setCurrentValue(50);
            pb.setMaxValue(360);
            // pb.setValueVisibility(false);

            // ImageItem img = new ImageItem(
            // // DefaultsService.getDefaultImage(EmbeddedImage.FILTER,
            // // EmbeddedImageSize.SIZE_64X64), false);
            // DefaultsService.getDefaultImage(EmbeddedImage.REFRESH,
            // EmbeddedImageSize.SIZE_64X64), false);
            ButtonCore btn_action = new ButtonCore();
            btn_action.setBackground(100, 255, 150);
            // btn_action.setText("Columnar");
            btn_action.setForeground(0, 0, 0);
            btn_action.setItemName("Action");
            btn_action.setWidth(128);
            btn_action.setHeight(128);
            btn_action.setWidthPolicy(SizePolicy.Fixed);
            btn_action.setHeightPolicy(SizePolicy.Fixed);
            btn_action.setAlignment(ItemAlignment.HCenter, ItemAlignment.Top);
            btn_action.setMargin(new Indents(30, 0, 30, 0));
            btn_action.setBorderRadius(10);
            btn_action.setTextMargin(new Indents(0, 45, 0, 0));
            Effects.addEffect(btn_action, new Shadow(8, new Size(4, 4), new Color(0, 0, 0, 180)));
            IMouseMethodState btn_action_click = (sender, args) -> {
                // MessageBox ms = new MessageBox("Send result?", "Message:");
                // ms.show();
                // System.out.println(ms.getResult());
                // h_stack.setVisible(!h_stack.isVisible());

                // changeImage(btn_action);

                InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
                inDialog.show(this);
            };
            btn_action.eventMouseClick.add(btn_action_click);

            SubtractFigure effect = new SubtractFigure(
                    new Figure(false, GraphicsMathService.getEllipse(20, 20, 0, 0, 64)));
                    // new CustomFigure(false, GraphicsMathService.getRectangle(20, 20, 0, 0)));
            effect.setAlignment(
                // ItemAlignment.VCENTER
            // , 
            ItemAlignment.HCenter
            );
            effect.setPositionOffset(0, 0);
            Effects.addEffect(btn_action, effect);

            // Image img1 = Image.FromFile("icon.png");
            // Image img1 = Image.FromFile("battery_full.png");
            // Image img1 = Image.FromFile("battery_full_small.png");
            // BufferedImage image = null;
            // try {
            // // image = ImageIO.read(new
            // File("//home//rsedaikin//Documents//columnar.png"));
            // image = ImageIO.read(new File("D:\\columnar.png"));
            // } catch (IOException e) {
            // }
            // Image img1 = Image.FromFile("spacevil_logo.png");
            // Image img1 = Image.FromFile("sample.png");
            // Image img1 = Image.FromFile("icon.jpg");

            // img.setBackground(new Color(100, 0, 0, 50));
            img.setSize(64, 64);
            // img.setPadding(32, 32, 32, 32);
            img.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            img.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            img.setColorOverlay(Color.black);

            HorizontalSlider h_slider = new HorizontalSlider();
            h_slider.setStep(1);
            h_slider.setAlignment(ItemAlignment.Top, ItemAlignment.Left);
            h_slider.setMargin(25, 0, 25, 0);
            h_slider.setMaxValue(360);
            h_slider.eventValueChanged.add((sender) -> {
                img.setRotationAngle(-h_slider.getCurrentValue());
                pb.setCurrentValue((int) h_slider.getCurrentValue());
            });

            // btn_action.eventMouseHover.add((sender, args) -> {
            // img.setColorOverlay(new Color(50, 50, 150));
            // });
            // btn_action.eventMouseLeave.add((sender, args) -> {
            // img.setColorOverlay(new Color(0, 0, 0));
            // });

            // frame.addItem(img);

            // BufferedImage bi1 = MakeShade(image, 4);
            // ImageItem img1 = new ImageItem(bi1);
            // img1.setBackground(new Color(0, 0, 0, 0));
            // img1.setSize(256, 134);
            // img1.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            // img1.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
            // frame.addItem(img1);

            // java awt graphics test
            // Font font = DefaultsService.getDefaultFont(Font.BOLD, 30);
            // String message = "Make a Chance!";
            //
            // BufferedImage bi = new BufferedImage(400, 100, BufferedImage.TYPE_INT_ARGB);
            //// Graphics2D ig2 = bi.createGraphics();
            //// RenderingHints rh = new
            // RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
            //// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            //// ig2.setRenderingHints(rh);
            //// ig2.setFont(font);
            //// FontMetrics metrics = ig2.getFontMetrics();
            //// int stringWidth = metrics.stringWidth(message);
            //// int stringHeight = metrics.getAscent();
            //// ig2.setPaint(Color.white);
            //// ig2.drawString(message, 0, stringHeight);
            ////
            //// ImageItem img_gr = new ImageItem(bi);
            //// img_gr.setBackground(new Color(0, 0, 0, 0));
            //// img_gr.setSize(400, 100);
            //// img_gr.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            //// img_gr.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
            //// frame.addItem(img_gr);

            // ComboBox combo = new ComboBox(new MenuItem("Custom item for selection #0"),
            //         new MenuItem("Custom item for selection #1"));
            // combo.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
            // combo.setMargin(25, 0, 25, 0);
            // combo.selectionChanged.add(() -> {
            //     Thread task = new Thread(() -> {
            //         for (int i = 0; i <= 10 * combo.getCurrentIndex(); i++) {
            //             pb.setCurrentValue(i);
            //             try {
            //                 Thread.sleep(30);
            //             } catch (InterruptedException e) {
            //                 e.printStackTrace();
            //             }
            //         }
            //     });
            //     task.start();
            // });

            frame.addItems(btn_action,
                    //  combo, 
                    h_slider, pb);
            btn_action.addItem(img);

            btn_action.setToolTip("Refresh image");

            ImageItem res = new ImageItem(
                    DefaultsService.getDefaultImage(EmbeddedImage.RecycleBin, EmbeddedImageSize.Size32x32));
            res.setSize(16, 16);
            res.setBackground(0, 0, 0, 0);
            res.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
            res.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
            MenuItem restore = new MenuItem("Custom item for selection #RESTORE");
            restore.setItemName("restore");
            restore.addItem(res);
            // combo.addItem(restore);
            // combo.setVisible(false);

            // for (int i = 2; i < 5; i++) {
            // MenuItem menu_item = new MenuItem("Custom item for selection #" + i);
            // combo.addItem(menu_item);
            // }
            // combo.setCurrentIndex(0);

            MenuItem filterItem = getMenuItem("Open Filter Function Menu",
                    DefaultsService.getDefaultImage(EmbeddedImage.Filter, EmbeddedImageSize.Size32x32));
            filterItem.setItemName("filterItem");
            MenuItem recycleItem = getMenuItem("Open Recycle Bin",
                    DefaultsService.getDefaultImage(EmbeddedImage.RecycleBin, EmbeddedImageSize.Size32x32));
            MenuItem refreshItem = getMenuItem("Refresh UI",
                    DefaultsService.getDefaultImage(EmbeddedImage.Refresh, EmbeddedImageSize.Size32x32));
            MenuItem addMenuItemItem = getMenuItem("Add New Function...",
                    DefaultsService.getDefaultImage(EmbeddedImage.Add, EmbeddedImageSize.Size32x32));

            ComboBox combo = new ComboBox(filterItem, recycleItem, refreshItem, addMenuItemItem);
            combo.setAlignment(ItemAlignment.VCenter, ItemAlignment.HCenter);
            combo.setText("Operations");

            addMenuItemItem.eventMouseClick.add((sender, args) -> {
                InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
                inDialog.onCloseDialog.add(() -> {
                    if (inDialog.getResult() != "")
                        combo.addItem(getMenuItem(inDialog.getResult(),
                                DefaultsService.getDefaultImage(EmbeddedImage.Import, EmbeddedImageSize.Size32x32)));
                });
                inDialog.show(addMenuItemItem.getHandler());
            });

            frame.addItem(combo);

            combo.setStyle(getComboBoxStyle());
            combo.addItem(getDot());
            // combo.setCurrentIndex(0);

            ToolTip.setShadow(this, new Shadow(10, new Position(3, 3), Color.black));
            ToolTip.setFont(this, DefaultsService.getDefaultFont(Font.ITALIC, 18));
            ToolTip.setBorder(this, new Border(new Color(10, 162, 232), new CornerRadius(10), 2));
            ToolTip.setTimeOut(this, 0);
            eventKeyPress.add((sender, args) -> {
                if (args.key == KeyCode.Alpha9 && args.mods.contains(KeyMods.No)) {
                    combo.open();
                }
            });

            // что-то сбивает фокус
            // setFocus();
            // btn_action.setFocus();

            eventKeyPress.add((s, a) -> {
                System.out.println(getFocusedItem().getItemName());
            });
        }
    }

    private MenuItem getMenuItem(String name, BufferedImage bitmap) {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setStyle(getMenuItemStyle());
        menuItem.setTextMargin(new Indents(25, 0, 0, 0));
        menuItem.eventMouseClick.add((sender, args) -> {
            System.out.println(menuItem.getText() + " was clicked!");
        });

        ImageItem img = new ImageItem(bitmap, false);
        img.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
        img.setSize(20, 20);
        img.setAlignment(ItemAlignment.Left, ItemAlignment.VCenter);
        img.keepAspectRatio(true);
        menuItem.addItem(img);

        ButtonCore infoBtn = new ButtonCore("?");
        infoBtn.setBackground(40, 40, 40);
        infoBtn.setWidth(20);
        infoBtn.setSizePolicy(SizePolicy.Fixed, SizePolicy.Expand);
        infoBtn.setFontStyle(Font.BOLD);
        infoBtn.setForeground(210, 210, 210);
        infoBtn.setAlignment(ItemAlignment.VCenter, ItemAlignment.Right);
        infoBtn.setMargin(0, 0, 10, 0);
        infoBtn.setBorderRadius(3);
        infoBtn.addItemState(ItemStateType.Hovered, new ItemState(new Color(10, 140, 210)));
        infoBtn.setPassEvents(false, InputEventType.MousePress, InputEventType.MouseRelease,
                InputEventType.MouseDoubleClick);

        infoBtn.eventMouseClick.add((sender, args) -> {
            PopUpMessage popUpInfo = new PopUpMessage("This is decorated MenuItem:\n" + menuItem.getText());
            popUpInfo.setTimeOut(3000);
            popUpInfo.setHeight(60);
            popUpInfo.setAlignment(ItemAlignment.Top, ItemAlignment.HCenter);
            popUpInfo.setMargin(0, 50, 0, 0);
            popUpInfo.show(infoBtn.getHandler());
        });

        menuItem.addItem(infoBtn);

        return menuItem;
    }

    private Style getMenuItemStyle() {
        Style style = Style.getMenuItemStyle();
        style.setBackground(255, 255, 255, 7);
        style.foreground = new Color(210, 210, 210);
        style.border.setRadius(new CornerRadius(3));
        style.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 45)));
        return style;
    }

    private Style getComboBoxDropDownStyle() {
        Style style = Style.getComboBoxDropDownStyle();
        style.setBackground(50, 50, 50);
        style.setBorder(new Border(new Color(100, 100, 100), new CornerRadius(0, 0, 5, 5), 1));
        style.setShadow(new Shadow(10, new Position(3, 3), new Color(0, 0, 0, 150)));
        return style;
    }

    private Style getComboBoxStyle() {
        Style style = Style.getComboBoxStyle();
        style.setBackground(45, 45, 45);
        style.setForeground(210, 210, 210);
        style.setBorder(new Border(new Color(255, 181, 111), new CornerRadius(10, 0, 0, 10), 2));
        style.setShadow(new Shadow(10, new Position(3, 3), new Color(0, 0, 0, 150)));

        style.removeInnerStyle("dropdownarea");
        Style dropDownAreaStyle = getComboBoxDropDownStyle();
        style.addInnerStyle("dropdownarea", dropDownAreaStyle);

        Style selectionStyle = style.getInnerStyle("selection");
        if (selectionStyle != null) {
            selectionStyle.border.setRadius(new CornerRadius(10, 0, 0, 10));
            selectionStyle.setBackground(0, 0, 0, 0);
            selectionStyle.setPadding(25, 0, 0, 0);
            selectionStyle.addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 20)));
        }

        Style dropDownButtonStyle = style.getInnerStyle("dropdownbutton");
        if (dropDownButtonStyle != null) {
            dropDownButtonStyle.border.setRadius(new CornerRadius(0, 0, 0, 10));
        }

        return style;
    }

    private Ellipse getDot() {
        Ellipse ellipse = new Ellipse(12);
        ellipse.setSize(8, 8);
        ellipse.setAlignment(ItemAlignment.VCenter, ItemAlignment.Left);
        ellipse.setMargin(10, 0, 0, 0);
        return ellipse;
    }

    private static BufferedImage MakeShade(BufferedImage image, int dep) {
        double[] weights = new double[dep + 1];
        double sum, sigma2 = 4.0;
        weights[0] = gauss(0, sigma2);
        sum = weights[0];
        for (int i = 1; i <= dep; i++) {
            weights[i] = gauss(i, sigma2);
            sum += 2 * weights[i];
        }

        int h = image.getHeight();
        int w = image.getWidth();
        BufferedImage bufim = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        double tmp[];
        double dtmp;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                tmp = new double[4];
                dtmp = 0;
                for (int k = -dep; k <= dep; k++) {
                    for (int l = -dep; l <= dep; l++) {
                        byte[] bytes;
                        double db = 0;
                        if (i + k < 0 || j + l < 0 || i + k >= w || j + l >= h) {
                            bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i, j)).array();

                            // db = image.getRGB(i, j);
                        } else {
                            bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i + k, j + l)).array();
                            // db = image.getRGB(i + k, j + l);
                        }

                        // bmp.add(bytes[1]);
                        // bmp.add(bytes[2]);
                        // bmp.add(bytes[3]);
                        // bmp.add(bytes[0]);
                        tmp[0] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[0] * 1f / 255f);
                        tmp[1] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[1] * 1f / 255f);
                        tmp[2] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[2] * 1f / 255f);
                        tmp[3] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[3] * 1f / 255f);

                        // dtmp += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * db;
                    }
                }
                bufim.setRGB(i, j, ByteBuffer.wrap(new byte[] { (byte) (tmp[0] * 255), (byte) (tmp[1] * 255),
                        (byte) (tmp[2] * 255), (byte) (tmp[3] * 255) }).getInt());
                // bufim.setRGB(i, j, (int)dtmp);

                // outarr[i][j] = tmp;
            }
        }
        return bufim;
    }

    private static double gauss(double x, double sigma) {
        double ans;
        ans = Math.exp(-(x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }

    private void changeImage(ButtonCore btn) {

        // img.setImage(DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE,
        // EmbeddedImageSize.SIZE_64X64));
        btn.clear();
        img = new ImageItem(DefaultsService.getDefaultImage(EmbeddedImage.Diskette, EmbeddedImageSize.Size64x64),
                false);
        btn.addItem(img);
    }
}
