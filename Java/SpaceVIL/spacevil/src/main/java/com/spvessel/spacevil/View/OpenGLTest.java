package com.spvessel.spacevil.View;

import java.awt.*;
import com.spvessel.spacevil.ActiveWindow;
import com.spvessel.spacevil.ButtonCore;
import com.spvessel.spacevil.CustomShape;
import com.spvessel.spacevil.GraphicsMathService;
import com.spvessel.spacevil.HorizontalSlider;
import com.spvessel.spacevil.HorizontalStack;
import com.spvessel.spacevil.ImageItem;
import com.spvessel.spacevil.TitleBar;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Flags.EmbeddedImage;
import com.spvessel.spacevil.Flags.EmbeddedImageSize;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.KeyCode;
import com.spvessel.spacevil.Flags.SizePolicy;

public class OpenGLTest extends ActiveWindow {
    @Override
    public void initWindow() {

        setParameters(this.getClass().getName(), this.getClass().getName(), 800, 800, true);

        CustomShape star = new CustomShape(GraphicsMathService.getStar(100, 50, 6));
        star.setBackground(0xFF, 0xB5, 0x6F);
        star.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        star.setAlignment(ItemAlignment.HCENTER, ItemAlignment.VCENTER);
        star.setMargin(50, 80, 50, 50);

//        OpenGLLayer ogl = new OpenGLLayer();
//        ogl.setMargin(0, 30, 0, 0);

        ButtonCore btnRestoreView = new ButtonCore();
        btnRestoreView.isFocusable = false;
        btnRestoreView.setBackground(new Color(0xFF, 0xB5, 0x6F));
        btnRestoreView.setShadow(5, 0, 2, Color.BLACK);
        btnRestoreView.setSize(30, 30);
        btnRestoreView.setPadding(5, 5, 5, 5);
        btnRestoreView.setMargin(0, 0, 20, 20);
        btnRestoreView.setAlignment(ItemAlignment.RIGHT, ItemAlignment.BOTTOM);
        ImageItem imgRefresh = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.REFRESH, EmbeddedImageSize.SIZE_32X32), false);
        imgRefresh.setColorOverlay(Color.WHITE);

        HorizontalSlider zoom = new HorizontalSlider();
        zoom.setStep(0.2f);
        zoom.setMinValue(2f);
        zoom.setMaxValue(10f);
        zoom.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        zoom.setSize(150, 30);
        zoom.setAlignment(ItemAlignment.HCENTER, ItemAlignment.BOTTOM);
        zoom.setMargin(0, 0, 0, 20);
        zoom.handler.setShadow(5, 0, 2, Color.BLACK);

        HorizontalStack toolbar3D = new HorizontalStack();
        toolbar3D.setAlignment(ItemAlignment.BOTTOM, ItemAlignment.LEFT);
        toolbar3D.setSizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
        toolbar3D.setSize(65, 30);
        toolbar3D.setSpacing(5, 0);
        toolbar3D.setMargin(20, 0, 0, 20);

        ButtonCore btnRotateLeft = new ButtonCore();
        btnRotateLeft.isFocusable = false;
        btnRotateLeft.setBackground(new Color(0xFF, 0xB5, 0x6F));
        btnRotateLeft.setShadow(5, 0, 2, Color.BLACK);
        btnRotateLeft.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btnRotateLeft.setPadding(5, 5, 5, 5);
        ImageItem imgLeft = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.ARROW_UP, EmbeddedImageSize.SIZE_32X32), false);
        imgLeft.setRotationAngle(-90);
        imgLeft.setColorOverlay(Color.WHITE);

        ButtonCore btnRotateRight = new ButtonCore();
        btnRotateRight.isFocusable = false;
        btnRotateRight.setBackground(new Color(0xFF, 0xB5, 0x6F));
        btnRotateRight.setShadow(5, 0, 2, Color.BLACK);
        btnRotateRight.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        btnRotateRight.setPadding(5, 5, 5, 5);
        ImageItem imgRight = new ImageItem(
                DefaultsService.getDefaultImage(EmbeddedImage.ARROW_UP, EmbeddedImageSize.SIZE_32X32), false);
        imgRight.setRotationAngle(90);
        imgRight.setColorOverlay(Color.WHITE);

        addItems(new TitleBar("OpenGLLayer")//,
                //star,
                //ogl
                );
//        ogl.addItems(toolbar3D, zoom, btnRestoreView);
        btnRestoreView.addItem(imgRefresh);
        toolbar3D.addItems(btnRotateLeft, btnRotateRight);
        btnRotateLeft.addItem(imgLeft);
        btnRotateRight.addItem(imgRight);

        btnRestoreView.eventMousePress.add((sender, args) -> {
//            ogl.restoreView();
        });

        btnRotateLeft.eventMousePress.add((sender, args) -> {
//            ogl.rotate(KeyCode.LEFT);
        });

        btnRotateRight.eventMousePress.add((sender, args) -> {
//            ogl.rotate(KeyCode.RIGHT);
        });

        zoom.eventValueChanged.add((sender) -> {
//            ogl.setZoom(zoom.getCurrentValue());
        });

//        ogl.setFocus();
        zoom.setCurrentValue(3);
    }
}