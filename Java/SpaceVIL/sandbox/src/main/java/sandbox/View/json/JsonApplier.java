package sandbox.View.json;

import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Common.DefaultsService;
import com.spvessel.spacevil.Core.IBaseItem;
import com.spvessel.spacevil.Decorations.Indents;
import com.spvessel.spacevil.Decorations.Spacing;
import com.spvessel.spacevil.Decorations.Style;
import com.spvessel.spacevil.Flags.ItemAlignment;
import com.spvessel.spacevil.Flags.ItemStateType;
import com.spvessel.spacevil.Flags.SizePolicy;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class JsonApplier {
    public void applyJson(String path, Object item) { //Prototype item) {
        JsonTextParser jtp = new JsonTextParser();
        JsonTextParser.JsonStruct jss = jtp.readFile(path);
//        logJsonChecker(jss, 0);
        jsonReflection(jss, item);
    }

    private void parseStyle(JsonTextParser.JsonStruct jss, Style style, Object parentItem) {
//        jsonReflection(jss, style);
        parseObjectInners(style, jss);
        
        if (parentItem instanceof IBaseItem) {
            ((IBaseItem) parentItem).setStyle(style);
        } else if (parentItem instanceof Style) {
            ((Style) parentItem).addInnerStyle(jss.name, style);
        }
    }

    private void logJsonChecker(JsonTextParser.JsonStruct jss, int tab) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab; i++) {
            sb.append("    ");
        }
        System.out.println(sb.toString() + "name: " + jss.name + ", value: " + jss.value + ", is simple: " + jss.isSimple);
        if (jss.jsonObjects != null) {
            // for (Map.Entry<String, JsonTextParser.JsonStruct> pair : jss.jsonObjects.entrySet()) {
            for (JsonTextParser.JsonStruct pair : jss.jsonObjects) {
                // System.out.println(sb.toString() + pair.getKey() + " -> {");
                System.out.println(sb.toString() + pair.type + " -> {");
                // logJsonChecker(pair.getValue(), tab + 1);
                logJsonChecker(pair, tab + 1);
                System.out.println(sb.toString() + "} <-");
            }
        }
        if (jss.jsonArrays != null) {
            // for (Map.Entry<String, List<JsonTextParser.JsonStruct>> pair : jss.jsonArrays.entrySet()) {
            for (List<JsonTextParser.JsonStruct> pair : jss.jsonArrays) {
                // System.out.println(sb.toString() + "array: " + pair.getKey() + " -> [");
                System.out.println(sb.toString() + "array: " + pair.get(0).type + " -> [");
                // for (JsonTextParser.JsonStruct val : pair.getValue()) {
                for (JsonTextParser.JsonStruct val : pair) {
                    logJsonChecker(val, tab + 1);
                    System.out.print(sb.toString() + ", \n");
                }
                System.out.println(sb.toString() + "] <-");
            }
        }
    }

    private void jsonReflection(JsonTextParser.JsonStruct jss, Object item) {// Prototype item) {
        if (jss.isSimple) {
            Object setObj = jss.value;
            if (jss.name.toLowerCase().contains("alignment")) {
                List<ItemAlignment> tmpList = new ArrayList<>();
                tmpList.add(ItemAlignment.valueOf(jss.value));
                setObj = tmpList;
            }

            boolean isFound = setItIfYouCan(jss.name, item, setObj, null);
            // It looks like the function covers all cases. If so, definitely good for me

            if (!isFound) {
                System.out.println("Didn't found anything simple with name " + jss.name);
            }

        } else {
            Object currentObj = null;

            if (jss.name == null || jss.name.equals("")) { //???
                currentObj = item;
            } else {
                boolean isFound = false;
                try {
                    Method method = item.getClass().getMethod("get" + jss.name); //Presumably the method names wrote correctly
                    currentObj = method.invoke(item);
                    isFound = true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException msm) {
//                    System.out.println("no such method, check fields: " + jss.name);
                }

                if (!isFound) {
                    try {
                        Field field = item.getClass().getField(jss.name);
                        currentObj = field.get(item);
                        isFound = true;
                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        System.out.println("no such field, check is': " + jss.name);
                    }
                }

                if (!isFound) {
                    currentObj = ItemsFactory.getObject(jss.name);
                    if (currentObj != null) {
                        if (currentObj instanceof IBaseItem) {
                            if (item instanceof Prototype) {
                                ((Prototype) item).addItem((IBaseItem)currentObj);
                            } else if (item instanceof CoreWindow) {
                                ((CoreWindow) item).addItem((IBaseItem)currentObj);
                            } else {
                                System.out.println("smth strange with adding an item");
                            }
                        } else if (currentObj instanceof Style) {
                            //TODO how to apply?
                            parseStyle(jss, (Style) currentObj, item);
                        }
                        //What to do if it's not?
                        isFound = true;
                    }
                }

                if (!isFound) {
                    //for the special cases. Horizontal/VerticalSplitArea
                    if (((item instanceof HorizontalSplitArea) && (jss.name.equals("BottomItem") || jss.name.equals("TopItem"))) ||
                            ((item instanceof VerticalSplitArea) && (jss.name.equals("LeftItem") || jss.name.equals("RightItem")))) {

                        //assume that there is only one item inside
                        // Map.Entry<String, JsonTextParser.JsonStruct> only = jss.jsonObjects.entrySet().iterator().next();
                        // currentObj = ItemsFactory.getObject(only.getValue().name);
                        currentObj = ItemsFactory.getObject(jss.jsonObjects.get(0).name);
                        if (currentObj != null) {
                            if (currentObj instanceof IBaseItem) {
                                try {
                                    String methodName = "set" + jss.name;
                                    Method method = item.getClass().getMethod(methodName, IBaseItem.class); //to check if method exists
                                    applyMethod(UsedTypes.IBASEITEM, methodName, item, (IBaseItem) currentObj);
                                    isFound = true;
                                } catch (NoSuchMethodException e) {
                                    System.out.println("Problems with the Horizontal/VerticalSplitArea: " + jss.name + " " + e);
                                }
                            } else {
//                                System.out.println("dunno what if it's not");
                            }
                        }
                    }
                }

                if (!isFound) {
                    System.out.println("Nothing complex is found with name " + jss.name);
                }

                //Not sure that is' needed to be checked here, since this section is for the complex items, and is' is not complex
            }

            if (currentObj instanceof Prototype) {
                parseObjectInners(currentObj, jss);
            } else { //what options do we have in this case?
                if (currentObj instanceof Indents) {
                    Indents indents = new Indents();

                    // indents.left = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "left", "0"));
                    indents.left = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "left", "0"));
                    // indents.right = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "right", "0"));
                    indents.right = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "right", "0"));
                    // indents.top = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "top", "0"));
                    indents.top = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "top", "0"));
                    // indents.bottom = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "bottom", "0"));
                    indents.bottom = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "bottom", "0"));

                    boolean isFound = setItIfYouCan(jss.name, item, indents, getClassByType(UsedTypes.INDENTS));
                    if (!isFound) {
                        System.out.println(jss.name + " indents setter is not good");
                    }
                } else if (currentObj instanceof Spacing) {
                    Spacing spacing = new Spacing();
                    // spacing.vertical = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "vertical", "0"));
                    spacing.vertical = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "vertical", "0"));
                    // spacing.horizontal = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "horizontal", "0"));
                    spacing.horizontal = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "horizontal", "0"));

                    boolean isFound = setItIfYouCan(jss.name, item, spacing, getClassByType(UsedTypes.SPACING));
                    if (!isFound) {
                        System.out.println(jss.name + " spacing setter is not good");
                    }
                } else if (currentObj instanceof Font) { //??? Not sure if it will work
                    Font font = DefaultsService.getDefaultFont();
                    // int fontSize = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "Size", Integer.toString(font.getSize())));
                    int fontSize = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "Size", Integer.toString(font.getSize())));
                    // int fontStyle = Integer.parseInt(getFromMapIfExist(jss.jsonObjects, "Style", Integer.toString(font.getStyle())));
                    int fontStyle = Integer.parseInt(getFromListIfExists(jss.jsonObjects, "Style", Integer.toString(font.getStyle())));
                    // String fontFamily = getFromMapIfExist(jss.jsonObjects, "Family", font.getFamily());
                    String fontFamily = getFromListIfExists(jss.jsonObjects, "Family", font.getFamily());

                    font = new Font(fontFamily, fontStyle, fontSize);

                    boolean isFound = setItIfYouCan(jss.name, item, font, getClassByType(UsedTypes.FONT));
                    if (!isFound) {
                        System.out.println(jss.name + " setter is not good");
                    }
                } else {
                    if (!(currentObj instanceof Style)) {
                        //assume that I have just an empty item and will look inside
                        // for (Map.Entry<String, JsonTextParser.JsonStruct> pair : jss.jsonObjects.entrySet()) {
                        for (JsonTextParser.JsonStruct pair : jss.jsonObjects) {
                            // jsonReflection(pair.getValue(), item);
                            jsonReflection(pair, item);
                        }
                    }
                }
            }
        }
    }

    // private String getFromMapIfExist(Map<String, JsonTextParser.JsonStruct> map, String key, String defVal) {
    //     String lowKey = lowFirstChar(key);
    //     if (map.containsKey(lowKey)) {
    //         return map.get(lowKey).value;
    //     }
    //     String upKey = upFirstChar(key);
    //     if (map.containsKey(upKey)) {
    //         return map.get(upKey).value;
    //     }
    //     return defVal;
    // }

    private String getFromListIfExists(List<JsonTextParser.JsonStruct> list, String key, String defVal) {
        String lowKey = lowFirstChar(key);
        String upKey = upFirstChar(key);

        for (JsonTextParser.JsonStruct jss : list) {
            if (jss.type.equals(lowKey) || jss.type.equals(upKey)) {
                return jss.value;
            }
        }

        return defVal;
    }

    private boolean setItIfYouCan(String parName, Object itemClass, Object val, Class<?> valType) {
        boolean isSuccessful = false;
        String caselessName = parName.toLowerCase();
        
        Field[] fieldsArr = itemClass.getClass().getFields();
        List<Field> possibleFields = new ArrayList<>();
        //TODO maybe I should improve checking. Something "precise first, than approximate"
        
        Field foundField = null;
        
        for (Field f : fieldsArr) {
            String tmpName = f.getName().toLowerCase();
            if (tmpName.contains(caselessName)) {
                if (valType == null || (valType != null && f.getType().equals(valType))) {
                    possibleFields.add(f);
                    if (tmpName.equals(caselessName) || tmpName.equals("is" + caselessName)) { //TODO not precise, need checking
                        foundField = f;
                    }
                }
            }
        }
        
        //precise first-------------------------------------------------------------------------------------------------
        if (foundField != null) {
            isSuccessful = setValue(foundField, itemClass, val);
            if (isSuccessful) {
                return true;
            }
        }
        
        Method[] methodsArr = itemClass.getClass().getMethods();
        List<Method> possibleMethods = new ArrayList<>();
        
        Method foundMethod = null;

        for (Method m : methodsArr) {
            String tmpName = m.getName().toLowerCase();
            if (tmpName.contains(caselessName)) {
                Class<?>[] parTypes = m.getParameterTypes();
                if (!(parTypes == null || parTypes.length == 0 || parTypes.length > 1)) {
                    if (valType == null || (valType != null && parTypes[0].equals(valType))) {
                        possibleMethods.add(m);

                        if (tmpName.equals(caselessName) || tmpName.equals("set" + caselessName)) { //TODO not precise, need checking
                            foundMethod = m;
                        }
                    }
                }
            }
        }

        if (foundMethod != null) {
            isSuccessful = applyMethod(foundMethod, itemClass, val);
            if (isSuccessful) {
                return true;
            }
        }
        //--------------------------------------------------------------------------------------------------------------

        //what a mess
        if (possibleFields.size() > 0) {
            foundField = possibleFields.get(0);

            if (possibleFields.size() > 1) {
                //I don't like it, but it is the best idea for now
                int len = foundField.getName().length();
                for (Field f : possibleFields) {
                    int tmpLen = f.getName().length();
                    if (tmpLen < len) {
                        len = tmpLen;
                        foundField = f;
                    }
                }
            }
        }

        if (possibleMethods.size() > 0) {
            foundMethod = possibleMethods.get(0);

            if (possibleMethods.size() > 1) {
                //I don't like it, but it is the best idea for now
                int len = foundMethod.getName().length();
                for (Method m : possibleMethods) {
                    int tmpLen = m.getName().length();
                    if (tmpLen < len) {
                        len = tmpLen;
                        foundMethod = m;
                    }
                }
            }
        }

        if (foundField == null && foundMethod == null) {
            return isSuccessful;
        }

        if (foundMethod == null || ((foundField != null) && foundField.getName().length() < foundMethod.getName().length())) {
            isSuccessful = setValue(foundField, itemClass, val);
        }

        if (!isSuccessful) {
            if (foundField == null || ((foundMethod != null) && foundField.getName().length() >= foundMethod.getName().length())) {
                isSuccessful = applyMethod(foundMethod, itemClass, val);
            }
        }

        return isSuccessful;
    }

    private void parseObjectInners(Object innerItem, JsonTextParser.JsonStruct jss) {
//        Object innerItem = currentObj; //(Prototype)
        // for (Map.Entry<String, JsonTextParser.JsonStruct> pair : jss.jsonObjects.entrySet()) {
        for (JsonTextParser.JsonStruct pair : jss.jsonObjects) {
            // jsonReflection(pair.getValue(), innerItem); //presumably, the name in the object is equal to the key value
            jsonReflection(pair, innerItem); //presumably, the name in the object is equal to the key value
        }

        // for (Map.Entry<String, List<JsonTextParser.JsonStruct>> pair : jss.jsonArrays.entrySet()) {
        for (List<JsonTextParser.JsonStruct> pair : jss.jsonArrays) {
            // if (isColorField(pair.getKey())) {
            String pairKey = pair.get(0).type;
            if (isColorField(pairKey)) {
                //Color, BackgroundColor, ForegroundColor, TextColor...
                //parse rgb or rgba array to color
                //TODO maybe add another color type. hex or whatever
                List<Integer> rgbColor = new ArrayList<>();
                // for (JsonTextParser.JsonStruct val : pair.getValue()) {
                for (JsonTextParser.JsonStruct val : pair) {
                    if (val.value.contains(".")) {
                        float f = Float.parseFloat(val.value);
                        if (f <= 1) {
                            if (f > 0) {
                                f = f * 255.0f;
                            } else {
                                f = 0;
                            }
                        }
                        rgbColor.add((int) f);
                    } else {
                        rgbColor.add(Integer.parseInt(val.value));
                    }
                }
                Color color;
                if (rgbColor.size() == 4) {
                    color = new Color(rgbColor.get(0), rgbColor.get(1), rgbColor.get(2), rgbColor.get(3));
                } else { //??? Don't really like it
                    color = new Color(rgbColor.get(0), rgbColor.get(1), rgbColor.get(2));
                }

                // boolean isFound = setItIfYouCan(pair.getKey(), innerItem, color, getClassByType(UsedTypes.COLOR));
                boolean isFound = setItIfYouCan(pairKey, innerItem, color, getClassByType(UsedTypes.COLOR));
                if (!isFound) {
                    // System.out.println(pair.getKey() + " color setter is not good");
                    System.out.println(pairKey + " color setter is not good");
                }
            // } else if (pair.getKey().toLowerCase().contains("alignment")) { //contains("Alignment")) {
            } else if (pairKey.toLowerCase().contains("alignment")) { //contains("Alignment")) {
                //List<ItemAlignment> and that's all for now
                List<ItemAlignment> align = new ArrayList<>();
                // for (JsonTextParser.JsonStruct val : pair.getValue()) {
                for (JsonTextParser.JsonStruct val : pair) {
                    align.add(ItemAlignment.valueOf(val.value));
                }

                // boolean isFound = setItIfYouCan(pair.getKey(), innerItem, align, getClassByType(UsedTypes.ALIGNMENTLIST));
                boolean isFound = setItIfYouCan(pairKey, innerItem, align, getClassByType(UsedTypes.ALIGNMENTLIST));
                if (!isFound) {
                    // System.out.println(pair.getKey() + " alignment setter is not good");
                    System.out.println(pairKey + " alignment setter is not good");
                }
            // } else if (pair.getKey().toLowerCase().contains("size")) { //.contains("Size")) {
            } else if (pairKey.toLowerCase().contains("size")) { //.contains("Size")) {
                //Block for methods with two parameters. Not good so far
                // if (!pair.getKey().toLowerCase().contains("font")) { //.contains("Font")) {
                if (!pairKey.toLowerCase().contains("font")) { //.contains("Font")) {
                    // if (pair.getKey().toLowerCase().contains("policy")) { //.contains("Policy")) {
                    if (pairKey.toLowerCase().contains("policy")) { //.contains("Policy")) {
                        List<SizePolicy> size = new ArrayList<>();
                        // for (JsonTextParser.JsonStruct val : pair.getValue()) {
                        for (JsonTextParser.JsonStruct val : pair) {
                            size.add(SizePolicy.valueOf(val.value));
                        }

                        try {
                            //Assume, that size policy sets only via setters
                            //which is not in case of Style
                            // Method method = innerItem.getClass().getMethod("set" + pair.getKey(), SizePolicy.class, SizePolicy.class);
                            Method method = innerItem.getClass().getMethod("set" + pairKey, SizePolicy.class, SizePolicy.class);
                            method.invoke(innerItem, size.get(0), size.get(1));
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                            //I think it can't be
                            // System.out.println(pair.getKey() + " size policy setter is not good");
                            System.out.println(pairKey + " size policy setter is not good");
                        }
                    } else {
                        List<Integer> size = new ArrayList<>();
                        // for (JsonTextParser.JsonStruct val : pair.getValue()) {
                        for (JsonTextParser.JsonStruct val : pair) {
                            size.add(Integer.parseInt(val.value));
                        }

                        try {
                            //Assume, that size sets only via setters
                            // Method method = innerItem.getClass().getMethod("set" + pair.getKey(), int.class, int.class);
                            Method method = innerItem.getClass().getMethod("set" + pairKey, int.class, int.class);
                            method.invoke(innerItem, size.get(0), size.get(1));
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                            //I think it can't be
                            // System.out.println(pair.getKey() + " size setter is not good");
                            System.out.println(pairKey + " size setter is not good");
                        }
                    }
                }
            } else {
                // System.out.println("what have we here " + pair.getKey());
                System.out.println("what have we here " + pairKey);
            }
        }
    }

    private String lowFirstChar(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        String sb = str.substring(0, 1).toLowerCase();
        sb += str.substring(1);
        return sb;
    }

    private String upFirstChar(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        String sb = str.substring(0, 1).toUpperCase();
        sb += str.substring(1);
        return sb;
    }

    private boolean isColorField(String fieldName) {
        //TODO comparison with case ignoring
        String tmpName = fieldName.toLowerCase();
//        return (fieldName.contains("Color") || fieldName.contains("Background") || fieldName.contains("Foreground") || fieldName.contains("borderFill"));
        return (tmpName.contains("color") || tmpName.contains("background") || tmpName.contains("foreground") || tmpName.contains("borderfill"));
    }

    private boolean applyMethod(UsedTypes type, String methodName, Object item, Object val) {
        boolean isSuccessful = true;
        try {
            Method method = null;
            method = item.getClass().getMethod(methodName, getClassByType(type));

            if (method != null) {
                isSuccessful = applyMethod(method, item, val);
            }
        } catch (NoSuchMethodException e) { //InvocationTargetException | IllegalAccessException |
            //I think it can't be
            isSuccessful = false;
        }

        return isSuccessful;
    }

    private boolean applyMethod(Method method, Object item, Object val) {
        boolean isSuccessful = true;
        try {
            Object parsVal = parseValue(getValueType(method.getParameterTypes()[0]), val);
            if (parsVal == null) {
                return false;
            }
            method.invoke(item, parsVal);
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            //I think it can't be
            isSuccessful = false;
        }

        return isSuccessful;
    }

    private boolean setValue(UsedTypes type, Field field, Object item, IBaseItem val) {
        boolean isSuccessful = true;
        //dunno if it's necessary
        try {
            if (type == UsedTypes.IBASEITEM) {
                field.set(item, val);
            }
        } catch (IllegalAccessException iae) {
            //I think it can't be but who knows
            isSuccessful = false;
        }

        return isSuccessful;
    }

    private boolean setValue(UsedTypes type, Field field, Object item, Object val) {
        boolean isSuccessful = true;
        try {
            Object parsVal = parseValue(type, val);
            if (parsVal == null) {
                return false;
            }
            field.set(item, parsVal);
        } catch (IllegalAccessException iae) {
            //I think it can't be
            isSuccessful = false;
        }

        return isSuccessful;
    }

    private boolean setValue(Field field, Object item, Object val) {
        return setValue(getValueType(field.getType()), field, item, val);
    }

    private UsedTypes getValueType(Class type) {
        //don't remember if we have any enum sets in the Style
        if (type.isEnum()) {
            String simpleName = type.getSimpleName();

            if (simpleName.equals("SizePolicy")) {
                return UsedTypes.SIZEPOLICY;
            }
            if (simpleName.equals("ItemAlignment")) {
                return UsedTypes.ITEMALIGNMENT;
            }
            if (simpleName.equals("ItemStateType")) {
                return UsedTypes.ITEMSTATETYPE;
            }
        } else {
            if (type.equals(Boolean.TYPE)) {
                return UsedTypes.BOOLEAN;
            }
            if (type.equals(Double.TYPE)) {
                return UsedTypes.DOUBLE;
            }
            if (type.equals(Float.TYPE)) {
                return UsedTypes.FLOAT;
            }
            if (type.equals(Character.TYPE)) {
                return UsedTypes.CHAR;
            }
            if (type.equals(Integer.TYPE)) {
                return UsedTypes.INT;
            }

            //??? TODO totally not sure about the equaling the rest. Works so far
            if (type.equals(IBaseItem.class)) {
                return UsedTypes.IBASEITEM;
            }
            if (type.equals(Color.class)) {
                return UsedTypes.COLOR;
            }
            if (type.equals(String.class)) {
                return UsedTypes.STRING;
            }
            if (type.equals(Indents.class)) {
                return UsedTypes.INDENTS;
            }
            if (type.equals(Spacing.class)) {
                return UsedTypes.SPACING;
            }
            if (type.equals(Font.class)) {
                return UsedTypes.FONT;
            }
            if (type.equals(List.class)) {
                return UsedTypes.ALIGNMENTLIST;
            }
        }

        //default
        return UsedTypes.STRING;
    }

    private Class<?> getClassByType(UsedTypes type) {
        switch (type) {
            case SIZEPOLICY:
                return SizePolicy.class;
            case ITEMALIGNMENT:
                return ItemAlignment.class;
            case ITEMSTATETYPE:
                return ItemStateType.class;
            case STRING:
                return String.class;
            case BOOLEAN:
                return boolean.class;
            case CHAR:
                return char.class;
            case FLOAT:
                return float.class;
            case DOUBLE:
                return double.class;
            case COLOR:
                return Color.class;
            case INT:
                return int.class;
            case ALIGNMENTLIST:
                return List.class;
            case SPACING:
                return Spacing.class;
            case FONT:
                return Font.class;
            case IBASEITEM:
                return IBaseItem.class;
            case INDENTS:
                return Indents.class;
        }
        return Object.class;
    }

    private Object parseValue(UsedTypes type, Object obj) { //String val) {
        //not sure if it will work properly
        try {
            switch (type) {
                case SIZEPOLICY: {
                    String val = (String) obj;
                    return SizePolicy.valueOf(val);
                }
                case ITEMALIGNMENT: {
                    String val = (String) obj;
                    return ItemAlignment.valueOf(val);
                }
                case ITEMSTATETYPE: {
                    String val = (String) obj;
                    return ItemStateType.valueOf(val);
                }
                case BOOLEAN: {
                    String val = (String) obj;
                    return Boolean.parseBoolean(val);
                }
                case CHAR: {
                    String val = (String) obj;
                    return val.charAt(0); //???
                }
                case FLOAT: {
                    String val = (String) obj;
                    return Float.parseFloat(val);
                }
                case DOUBLE: {
                    String val = (String) obj;
                    return Double.parseDouble(val);
                }
                case INT: {
                    String val = (String) obj;
                    return Integer.parseInt(val);
                }
                case COLOR: {
                    //TODO need some color parsing
                    if (obj instanceof String) {
                        String val = (String) obj;
                        return Color.getColor(val);
                    } else {
                        return (Color) obj;
                    }
                }
                case IBASEITEM: {
                    return (IBaseItem) obj;
                }
                case FONT: {
                    return (Font) obj;
                }
                case INDENTS: {
                    return (Indents) obj;
                }
                case SPACING: {
                    return (Spacing) obj;
                }
                case ALIGNMENTLIST: {
                    return (List<ItemAlignment>) obj;
                }

                case STRING:
                default: {
                    String val = (String) obj;
                    return val;
                }
            }
        } catch (ClassCastException cce) {
            return null;
        }
    }

    enum UsedTypes {
        BOOLEAN, DOUBLE, FLOAT, STRING, CHAR, COLOR, INT, SIZEPOLICY, ITEMALIGNMENT, ITEMSTATETYPE, IBASEITEM, INDENTS,
        SPACING, FONT, ALIGNMENTLIST
    }
}
