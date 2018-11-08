package com.spvessel.View;

import com.spvessel.*;
import com.spvessel.Core.InterfaceMouseMethodState;
import com.spvessel.Decorations.Indents;
import com.spvessel.Flags.ItemAlignment;
import com.spvessel.Flags.SizePolicy;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

public class ImageTest extends ActiveWindow {
    @Override
    public void initWindow() {
        WindowLayout Handler = new WindowLayout(this, "ImageTest", "ImageTest", 500, 500, true);
        setHandler(Handler);
        Handler.setBackground(45, 45, 45);
        Handler.setPadding(2, 2, 2, 2);

        // DragAnchor
        TitleBar title = new TitleBar("ImageTest");
        Handler.addItem(title);

        // Frame
        VerticalStack frame = new VerticalStack();
        frame.setBackground(60, 60, 60);
        frame.setItemName("Container");
        frame.setMargin(0, 45, 0, 0);
        frame.setWidthPolicy(SizePolicy.EXPAND);
        frame.setHeightPolicy(SizePolicy.EXPAND);
        frame.setSpacing(0, 20);
        Handler.addItem(frame);

        HorizontalStack h_stack = new HorizontalStack();
        h_stack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
        h_stack.setBackground(255, 255, 255, 10);
        h_stack.setHeight(30);
        h_stack.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        h_stack.setSpacing(5, 0);

        frame.addItem(h_stack);

        Rectangle r1 = new Rectangle();
        r1.setBackground(255, 165, 0);
        r1.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        h_stack.addItem(r1);
        for (int i = 0; i < 4; i++) {
            Rectangle rect = new Rectangle();
            rect.setBackground(255, 255, 0);
            rect.setSize(30, 30);
            rect.setSizePolicy(SizePolicy.FIXED, SizePolicy.EXPAND);
            h_stack.addItem(rect);
        }

        ProgressBar pb = new ProgressBar();
        pb.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
        pb.setMargin(25, 0, 25, 0);

        ButtonCore btn_action = new ButtonCore();
        btn_action.setBackground(100, 255, 150);
        btn_action.setText("Columnar");
        btn_action.setForeground(0, 0, 0);
        btn_action.setItemName("Action");
        btn_action.setWidth(256);
        btn_action.setHeight(128);
        btn_action.setWidthPolicy(SizePolicy.EXPAND);
        btn_action.setHeightPolicy(SizePolicy.FIXED);
        btn_action.setAlignment(ItemAlignment.HCENTER, ItemAlignment.TOP);
        btn_action.setMargin(new Indents(30, 0, 30, 0));
        btn_action.getBorder().setRadius(10);
        btn_action.setTextMargin(new Indents(0, 45, 0, 0));
        InterfaceMouseMethodState btn_action_click = (sender, args) -> {
            MessageBox ms = new MessageBox("Send result?", "Message:");
            ms.show();
            System.out.println(ms.getResult());
        };
        btn_action.eventMouseClick.add(btn_action_click);

        // Image img1 = Image.FromFile("icon.png");
        // Image img1 = Image.FromFile("battery_full.png");
        // Image img1 = Image.FromFile("battery_full_small.png");
        BufferedImage image = null;
        try {
            // image = ImageIO.read(new File("//home//rsedaikin//Documents//columnar.png"));
            image = ImageIO.read(new File("D:\\columnar.png"));
        } catch (IOException e) {
        }
        // Image img1 = Image.FromFile("spacevil_logo.png");
        // Image img1 = Image.FromFile("sample.png");
        // Image img1 = Image.FromFile("icon.jpg");


        ImageItem img = new ImageItem(image);

        img.setBackground(new Color(0, 0, 0, 0));
        img.setSize(256, 134);
        img.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        img.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);

//        HorizontalSlider h_slider = new HorizontalSlider();
//        h_slider.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
//        h_slider.setMargin(25, 0, 25, 0);
//        InterfaceCommonMethodState valueChanged =
//                (sender) -> pb.setCurrentValue((int) h_slider.getCurrentValue());
//        h_slider.eventValueChanged.add(valueChanged);

        //frame.addItems(btn_action, h_slider, pb);
        //btn_action.addItem(img);
        frame.addItem(img);

        BufferedImage bi1 = MakeShade(image, 4);
        ImageItem img1 = new ImageItem(bi1);
        img1.setBackground(new Color(0, 0, 0, 0));
        img1.setSize(256, 134);
        img1.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        img1.setAlignment(ItemAlignment.VCENTER, ItemAlignment.HCENTER);
        frame.addItem(img1);

        // java awt graphics test
//        Font font = DefaultsService.getDefaultFont(Font.BOLD, 30);
//        String message = "Make a Chance!";
//
//        BufferedImage bi = new BufferedImage(400, 100, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D ig2 = bi.createGraphics();
//        RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        ig2.setRenderingHints(rh);
//        ig2.setFont(font);
//        FontMetrics metrics = ig2.getFontMetrics();
//        int stringWidth = metrics.stringWidth(message);
//        int stringHeight = metrics.getAscent();
//        ig2.setPaint(Color.white);
//        ig2.drawString(message, 0, stringHeight);
//
//        ImageItem img_gr = new ImageItem(bi);
//        img_gr.setBackground(new Color(0, 0, 0, 0));
//        img_gr.setSize(400, 100);
//        img_gr.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
//        img_gr.setAlignment(ItemAlignment.TOP, ItemAlignment.HCENTER);
//        frame.addItem(img_gr);
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

                            //db = image.getRGB(i, j);
                        }
                        else {
                            bytes = ByteBuffer.allocate(4).putInt(image.getRGB(i + k, j + l)).array();
                            //db = image.getRGB(i + k, j + l);
                        }

//                        bmp.add(bytes[1]);
//                        bmp.add(bytes[2]);
//                        bmp.add(bytes[3]);
//                        bmp.add(bytes[0]);
                        tmp[0] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[0] * 1f / 255f);
                        tmp[1] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[1] * 1f / 255f);
                        tmp[2] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[2] * 1f / 255f);
                        tmp[3] += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * (bytes[3] * 1f / 255f);

                        //dtmp += (weights[Math.abs(k)] / sum) * (weights[Math.abs(l)] / sum) * db;
                    }
                }
                bufim.setRGB(i, j, ByteBuffer.wrap(new byte[]{(byte)(tmp[0] * 255), (byte)(tmp[1] * 255), (byte)(tmp[2] * 255), (byte)(tmp[3] * 255)}).getInt());
                //bufim.setRGB(i, j, (int)dtmp);

//                outarr[i][j] = tmp;
            }
        }
        return bufim;
    }

    private static double gauss(double x, double sigma) {
        double ans;
        ans = Math.exp(- (x * x) / (2f * sigma * sigma)) / Math.sqrt(2 * Math.PI * sigma * sigma);
        return (float) ans;
    }
}
