package com.spvessel.spacevil.View;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.SizePolicy;
import com.spvessel.spacevil.Frame;
import com.spvessel.spacevil.View.json.JsonApplier;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTest extends ActiveWindow {
    @Override
    public void initWindow() {
//        setWindowName("JsonTest");
//        setWindowTitle("JsonTest");
//        setSize(800, 800);
//
//        setMinSize(50, 100);
//        setBackground(45, 45, 45);
//        setPadding(0, 0, 0, 0);
//
//        Frame vs = new Frame();
//        addItem(vs);

//        TextEditTest te = new TextEditTest();
//        te.setHeightPolicy(SizePolicy.FIXED);
//        te.setSize(100, 100);

//        addItem(te);

//        ButtonCore bc1 = new ButtonCore();
//        bc1.setText("button1");
////        bc1.setAlignment(ItemAlignment.TOP, ItemAlignment.LEFT);
//        ButtonCore bc2 = new ButtonCore();
//        bc2.setText("button2");
//        vs.addItems(bc1, bc2);

        JsonApplier ja = new JsonApplier();
//        ja.applyJson("F:\\spacevil\\Java\\SpaceVIL\\spacevil\\src\\main\\java\\com\\spvessel\\spacevil\\View\\json\\JsonStyle.json", bc2);
        ja.applyJson("F:\\spacevil\\Java\\SpaceVIL\\spacevil\\src\\main\\java\\com\\spvessel\\spacevil\\View\\json\\JsonWindow.json", this); //te);


//        System.out.println();
//        System.out.println(te.getMargin().left + " " + te.getMargin().top + " " + te.getMargin().right + " " + te.getMargin().bottom);

//        try {
//
//            Method method = te.getClass().getMethod("isEditable");
//            Class returnType = method.getReturnType();
//            System.out.println(returnType.equals(Boolean.TYPE));
////            Color col = Color.BLUE;
////            method.invoke(te, col);
//
////            System.out.println("what have we here " + (SizePolicy)obj);
////            System.out.println("class " + (returnType.equals(SizePolicy) instanceof SizePolicy) + " or " + (obj instanceof ItemAlignment));
////            te.setWidthPolicy(SizePolicy.valueOf("FIXED"));
////            System.out.println("name -> " + returnType.getName() + "\ntype name -> " + returnType.getTypeName() +
////                    "\ncanonical name -> " + returnType.getCanonicalName() + "\nsimple name -> " + returnType.getSimpleName());
//        } catch (NoSuchMethodException msm) {// | InvocationTargetException | IllegalAccessException msm) {
//            System.out.println("no such method, so what " + msm);
//        }

//        try {
//            Field field = te.getClass().getField("isFocusable");
//            Class type = field.getType();
//            System.out.println("type " + type);
//        } catch (NoSuchFieldException e) {
//            System.out.println("no such field, check is': ");
//        }
    }
}
