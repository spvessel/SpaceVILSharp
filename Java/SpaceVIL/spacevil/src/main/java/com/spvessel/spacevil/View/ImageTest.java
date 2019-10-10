package com.spvessel.spacevil.View;

import com.spvessel.spacevil.Core.InterfaceMouseMethodState;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Decorations.Border;
import com.spvessel.spacevil.Decorations.CornerRadius;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.ItemState;
import com.spvessel.spacevil.Decorations.Shadow;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.InputEventType;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.MSAA;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

public class ImageTest extends ActiveWindow {
    ImageItem img;

    @Override
    public void initWindow() {
        isCentered = true;
        // isBorderHidden = true;
        setSize(500, 500);
        setWindowName("ImageTest");
        setWindowTitle("ImageTest");
        setAntiAliasingQuality(MSAA.MSAA_8X);

        setBackground(45, 45, 45);
        setPadding(2, 2, 2, 2);

        img = new ImageItem(
                // DefaultsService.getDefaultImage(EmbeddedImage.FILTER,
                // EmbeddedImageSize.SIZE_64X64), false);
                DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_64X64), false);

        // DragAnchor
        TitleBar title = new TitleBar("ImageTest");
        addItem(title);

        boolean isVertTest = false;

        if (isVertTest) {
            VerticalStack h_stack = new VerticalStack();
            h_stack.setMargin(0, title.getHeight(), 0, 0);
            h_stack.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
            h_stack.setBackground(255, 255, 255, 200);
            h_stack.setWidth(30);
            h_stack.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            h_stack.setSpacing(0, 5);
            // h_stack.setContentAlignment(ItemAlignment.RIGHT);
            // h_stack.setVisible(false);

            addItem(h_stack);

            Rectangle r1 = new Rectangle();
            r1.setBackground(255, 165, 0);
            r1.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
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
            rect.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            h_stack.addItem(rect);

        } else {
            // Frame
            VerticalStack frame = new VerticalStack();
            frame.setBackground(60, 60, 60);
            frame.setItemName("Container");
            frame.setMargin(0, title.getHeight(), 0, 0);
            frame.setWidthPolicy(SizePolicy.EXPAND);
            frame.setHeightPolicy(SizePolicy.EXPAND);
            frame.setSpacing(0, 20);
            frame.setPadding(20, 60, 20, 20);
            addItem(frame);

            HorizontalStack h_stack = new HorizontalStack();
            h_stack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
            h_stack.setBackground(255, 255, 255, 200);
            h_stack.setHeight(30);
            h_stack.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
            h_stack.setSpacing(5, 0);
            // h_stack.setContentAlignment(ItemAlignment.VCENTER);
            // h_stack.setVisible(false);

            frame.addItem(h_stack);

            Rectangle r1 = new Rectangle();
            r1.setBackground(255, 165, 0);
            r1.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
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
            rect.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            h_stack.addItem(rect);

            ProgressBar pb = new ProgressBar();
            pb.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
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
            btn_action.setWidthPolicy(SizePolicy.FIXED);
            btn_action.setHeightPolicy(SizePolicy.FIXED);
            btn_action.setAlignment(ItemAlignment.HCENTER, ItemAlignment.TOP);
            btn_action.setMargin(new Indents(30, 0, 30, 0));
            btn_action.setBorderRadius(10);
            btn_action.setTextMargin(new Indents(0, 45, 0, 0));
            btn_action.setShadow(8, 0, 0, new Color(0, 0, 0, 180));
            btn_action.setShadowExtension(4, 4);
            InterfaceMouseMethodState btn_action_click = (sender, args) -> {
                // MessageBox ms = new MessageBox("Send result?", "Message:");
                // ms.show();
                // System.out.println(ms.getResult());
                // h_stack.setVisible(!h_stack.isVisible());

                changeImage(btn_action);
            };
            btn_action.eventMouseClick.add(btn_action_click);

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
            img.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            img.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
            img.setColorOverlay(Color.black);

            HorizontalSlider h_slider = new HorizontalSlider();
            h_slider.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
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
                    DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
            res.setSize(16, 16);
            res.setBackground(0, 0, 0, 0);
            res.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
            res.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
            MenuItem restore = new MenuItem("Custom item for selection #RESTORE");
            restore.addItem(res);
            // combo.addItem(restore);
            // combo.setVisible(false);

            // for (int i = 2; i < 5; i++) {
            // MenuItem menu_item = new MenuItem("Custom item for selection #" + i);
            // combo.addItem(menu_item);
            // }
            // combo.setCurrentIndex(0);

            MenuItem filterItem = getMenuItem("Open Filter Function Menu",
                    DefaultsService.getDefaultImage(EmbeddedImage.FILTER, EmbeddedImageSize.SIZE_32X32));
            MenuItem recycleItem = getMenuItem("Open Recycle Bin",
                    DefaultsService.getDefaultImage(EmbeddedImage.RECYCLE_BIN, EmbeddedImageSize.SIZE_32X32));
            MenuItem refreshItem = getMenuItem("Refresh UI",
                    DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_32X32));
            MenuItem addMenuItemItem = getMenuItem("Add New Function...",
                    DefaultsService.getDefaultImage(EmbeddedImage.ADD, EmbeddedImageSize.SIZE_32X32));

            ComboBox combo = new ComboBox(filterItem, recycleItem, refreshItem, addMenuItemItem);
            combo.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
            combo.setText("Operations");

            addMenuItemItem.eventMouseClick.add((sender, args) -> {
                InputDialog inDialog = new InputDialog("Add new function...", "Add", "NewFunction");
                inDialog.onCloseDialog.add(() -> {
                    if (inDialog.getResult() != "")
                        combo.addItem(getMenuItem(inDialog.getResult(),
                                DefaultsService.getDefaultImage(EmbeddedImage.IMPORT, EmbeddedImageSize.SIZE_32X32)));
                });
                inDialog.show(addMenuItemItem.getHandler());
            });

            frame.addItem(combo);
            // combo.setStyle(getComboBoxStyle());
            // combo.addItem(getDot());
            // combo.setCurrentIndex(0);

            ToolTip.setShadow(this, new Shadow(10, 3, 3, Color.black));
            ToolTip.setFont(this, DefaultsService.getDefaultFont(Font.ITALIC, 18));
            ToolTip.setBorder(this, new Border(new Color(10, 162, 232), new CornerRadius(10), 2));
            ToolTip.setTimeOut(this, 0);
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
        img.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        img.setSize(20, 20);
        img.setAlignment(ItemAlignment.LEFT, ItemAlignment.VCENTER);
        img.keepAspectRatio(true);
        menuItem.addItem(img);

        ButtonCore infoBtn = new ButtonCore("?");
        infoBtn.setBackground(40, 40, 40);
        infoBtn.setWidth(20);
        infoBtn.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
        infoBtn.setFontStyle(Font.BOLD);
        infoBtn.setForeground(210, 210, 210);
        infoBtn.setAlignment(ItemAlignment.VCENTER, ItemAlignment.RIGHT);
        infoBtn.setMargin(0, 0, 10, 0);
        infoBtn.setBorderRadius(3);
        infoBtn.addItemState(ItemStateType.HOVERED, new ItemState(new Color(10, 140, 210)));
        infoBtn.setPassEvents(false, InputEventType.MOUSE_PRESS, InputEventType.MOUSE_RELEASE,
                InputEventType.MOUSE_DOUBLE_CLICK);

        infoBtn.eventMouseClick.add((sender, args) -> {
            PopUpMessage popUpInfo = new PopUpMessage("This is decorated MenuItem:\n" + menuItem.getText());
            popUpInfo.setTimeOut(3000);
            popUpInfo.setHeight(60);
            popUpInfo.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
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
        style.borderRadius = new CornerRadius(3);
        style.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 45)));
        return style;
    }

    private Style getComboBoxDropDownStyle() {
        Style style = Style.getComboBoxDropDownStyle();
        style.setBackground(50, 50, 50);
        style.setBorder(new Border(new Color(100, 100, 100), new CornerRadius(0, 0, 5, 5), 1));
        style.setShadow(new Shadow(10, 3, 3, new Color(0, 0, 0, 150)));
        style.isShadowDrop = true;
        return style;
    }

    private Style getComboBoxStyle() {
        Style style = Style.getComboBoxStyle();
        style.setBackground(45, 45, 45);
        style.setForeground(210, 210, 210);
        style.setBorder(new Border(new Color(255, 181, 111), new CornerRadius(10, 0, 0, 10), 2));
        style.setShadow(new Shadow(10, 3, 3, new Color(0, 0, 0, 150)));
        style.isShadowDrop = true;

        style.removeInnerStyle("dropdownarea");
        Style dropDownAreaStyle = getComboBoxDropDownStyle();
        style.addInnerStyle("dropdownarea", dropDownAreaStyle);

        Style selectionStyle = style.getInnerStyle("selection");
        if (selectionStyle != null) {
            selectionStyle.borderRadius = new CornerRadius(10, 0, 0, 10);
            selectionStyle.setBackground(0, 0, 0, 0);
            selectionStyle.setPadding(25, 0, 0, 0);
            selectionStyle.addItemState(ItemStateType.HOVERED, new ItemState(new Color(255, 255, 255, 20)));
        }

        Style dropDownButtonStyle = style.getInnerStyle("dropdownbutton");
        if (dropDownButtonStyle != null) {
            dropDownButtonStyle.borderRadius = new CornerRadius(0, 0, 0, 10);
        }

        return style;
    }

    private Ellipse getDot() {
        Ellipse ellipse = new Ellipse(12);
        ellipse.setSize(8, 8);
        ellipse.setAlignment(ItemAlignment.VCENTER, ItemAlignment.LEFT);
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
        img = new ImageItem(DefaultsService.getDefaultImage(EmbeddedImage.DISKETTE, EmbeddedImageSize.SIZE_64X64),
                false);
        btn.addItem(img);
    }
}
