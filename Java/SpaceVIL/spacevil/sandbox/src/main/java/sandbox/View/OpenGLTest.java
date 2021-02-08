// package sandbox.View;

// import java.util.*;
// import java.awt.Color;
// import java.awt.image.BufferedImage;
// import com.spvessel.spacevil.*;
// import com.spvessel.spacevil.Common.*;
// import com.spvessel.spacevil.Core.*;
// import com.spvessel.spacevil.Flags.*;
// import com.spvessel.spacevil.Decorations.*;

// public class OpenGLTest extends ActiveWindow {
//    @Override
//    public void initWindow() {
//       setParameters(this.getClass().getName(), this.getClass().getName(), 800, 800, false);
//       isCentered = true;
//       isMaximized = true;

//       // oneCubeExample();
//       multipleCubes();
//    }

//    private void oneCubeExample() {
//       TitleBar titleBar = new TitleBar(this.getClass().getName());

//       OpenGLLayer ogl = new OpenGLLayer();
//       ogl.setMargin(0, titleBar.getHeight(), 0, 0);

//       HorizontalStack toolbar = getToolbarLayout();

//       ImagedButton btnRotateLeft = getImagedButton(EmbeddedImage.ArrowUp, -90);
//       ImagedButton btnRotateRight = getImagedButton(EmbeddedImage.ArrowUp, 90);

//       HorizontalSlider zoom = getSlider();

//       ImagedButton btnRestoreView = getImagedButton(EmbeddedImage.Refresh, 0);

//       // adding
//       addItems(titleBar, ogl);
//       ogl.addItems(toolbar);
//       toolbar.addItems(btnRotateLeft, btnRotateRight, zoom, btnRestoreView);

//       // assign events
//       btnRestoreView.eventMousePress.add((sender, args) -> {
//          ogl.restoreView();
//       });

//       btnRotateLeft.eventMousePress.add((sender, args) -> {
//          ogl.rotate(KeyCode.Left);
//       });

//       btnRotateRight.eventMousePress.add((sender, args) -> {
//          ogl.rotate(KeyCode.Right);
//       });

//       zoom.eventValueChanged.add((sender) -> {
//          ogl.setZoom(zoom.getCurrentValue());
//       });

//       // set focus
//       ogl.setFocus();
//       zoom.setCurrentValue(3);
//    }

//    private void multipleCubes() {
//       TitleBar titleBar = new TitleBar(this.getClass().getName());

//       FreeArea area = new FreeArea();
//       area.setMargin(0, titleBar.getHeight(), 0, 0);

//       addItems(titleBar, area);

//       List<IBaseItem> content = new ArrayList<>();

//       for (int row = 0; row < 3; row++) {
//          for (int column = 0; column < 3; column++) {
//             ResizableItem frame = new ResizableItem();
//             frame.setBorder(new Border(Color.gray, new CornerRadius(), 2));
//             frame.setPadding(5, 5, 5, 5);
//             frame.setBackground(100, 100, 100);
//             frame.setSize(200, 200);
//             frame.setPosition(90 + row * 210, 60 + column * 210);
//             area.addItem(frame);
//             content.add(frame);

//             frame.eventMousePress.add((sender, args) -> {
//                content.remove(frame);
//                content.add(frame);
//                area.setContent(content);
//             });

//             OpenGLLayer ogl = new OpenGLLayer();
//             ogl.setMargin(0, 30, 0, 0);
//             frame.addItem(ogl);
//          }
//       }
//    }

//    public static ImagedButton getImagedButton(EmbeddedImage image, float imageRotationAngle) {
//       ImagedButton btn = new ImagedButton(DefaultsService.getDefaultImage(image, EmbeddedImageSize.Size32x32),
//             imageRotationAngle);
//       return btn;
//    }

//    public static HorizontalSlider getSlider() {
//       HorizontalSlider slider = new HorizontalSlider();
//       slider.setStep(0.2f);
//       slider.setMinValue(2f);
//       slider.setMaxValue(10f);
//       slider.setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
//       slider.setSize(150, 30);
//       slider.setAlignment(ItemAlignment.HCenter, ItemAlignment.Bottom);
//       slider.setMargin(40, 0, 40, 0);
//       EffectsDeprecated.addEffect(slider.handler, new Shadow(5, new Position(0, 2), Color.black));
//       return slider;
//    }

//    public static HorizontalStack getToolbarLayout() {
//       HorizontalStack layout = new HorizontalStack();
//       layout.setContentAlignment(ItemAlignment.HCenter);
//       layout.setAlignment(ItemAlignment.Bottom, ItemAlignment.Left);
//       layout.setSizePolicy(SizePolicy.Expand, SizePolicy.Fixed);
//       layout.setHeight(30);
//       layout.setSpacing(5, 0);
//       layout.setMargin(20, 0, 20, 20);
//       return layout;
//    }

//    public static class ImagedButton extends BlankItem {
//       ImageItem _image = null;

//       public ImagedButton(BufferedImage image, float imageRotationAngle) {
//          addItemState(ItemStateType.Hovered, new ItemState(new Color(255, 255, 255, 40)));
//          addItemState(ItemStateType.Pressed, new ItemState(new Color(0, 0, 0, 40)));
//          setBackground(0xFF, 0xB5, 0x6F);
//          setSizePolicy(SizePolicy.Fixed, SizePolicy.Fixed);
//          setSize(30, 30);
//          setAlignment(ItemAlignment.Right, ItemAlignment.Bottom);
//          setPadding(5, 5, 5, 5);
//          EffectsDeprecated.addEffect(this, new Shadow(5, new Position(0, 2), Color.black));
//          isFocusable = false;

//          _image = new ImageItem(image, false);
//          _image.setRotationAngle(imageRotationAngle);
//          _image.setColorOverlay(Color.WHITE);
//       }

//       @Override
//       public void initElements() {
//          super.initElements();
//          addItem(_image);
//       }
//    }
// }