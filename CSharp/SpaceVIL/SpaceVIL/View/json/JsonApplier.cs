using System;
using System.Reflection;
using System.Text;
using System.Collections.Generic;
using System.Drawing;
using SpaceVIL.Decorations;
using SpaceVIL.Core;
using SpaceVIL.Common;

namespace SpaceVIL {
    public class JsonApplier {
        public void ApplyJson(string path, object item)
        {
            JsonTextParser jtp = new JsonTextParser();
            JsonStruct jss = jtp.ReadFile(path);
            // LogJsonChecker(jss, 0);
            JsonReflection(jss, item);
        }

        private void ParseStyle(JsonStruct jss, Style style, object parentItem)
        {
            // JsonReflection(jss, style);
            ParseObjectInners(style, jss);

            if (parentItem is IBaseItem)
            {
                ((IBaseItem) parentItem).SetStyle(style);
            }
            else if (parentItem is Style)
            {
                ((Style) parentItem).AddInnerStyle(jss.name, style);
            }
        }

        private void LogJsonChecker(JsonStruct jss, int tab)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tab; i++)
            {
                sb.Append("    ");
            }
            Console.WriteLine(sb.ToString() + "name: " + jss.name + ", value: " + jss.value + ", is simple: " + jss.isSimple);
            // foreach (KeyValuePair<String, JsonStruct> pair in jss.jsonObjects)
            if (jss.jsonObjects != null && jss.jsonObjects.Count != 0)
            {
                // IEnumerator<KeyValuePair<String, JsonStruct>> objEnum = jss.jsonObjects.GetEnumerator();
                // objEnum.Reset();
                // do
                // {
                foreach (JsonStruct pair in jss.jsonObjects)
                {
                    // KeyValuePair<String, JsonStruct> pair = objEnum.Current;
                    // Console.WriteLine(sb.ToString() + pair.Key + " -> {");
                    Console.WriteLine(sb.ToString() + pair.type + " -> {");
                    // LogJsonChecker(pair.Value, tab + 1);
                    LogJsonChecker(pair, tab + 1);
                    Console.WriteLine(sb.ToString() + "} <-");
                }
                // } while (objEnum.MoveNext());
            }
            // foreach (KeyValuePair<String, List<JsonStruct>> pair in jss.jsonArrays)
            if (jss.jsonArrays != null && jss.jsonArrays.Count != 0)
            {
                // IEnumerator<KeyValuePair<String, List<JsonStruct>>> arrEnum = jss.jsonArrays.GetEnumerator();
                // arrEnum.Reset();
                // do
                // {
                foreach (List<JsonStruct> pair in jss.jsonArrays)
                {
                    // KeyValuePair<String, List<JsonStruct>> pair = arrEnum.Current;
                    // Console.WriteLine(sb.ToString() + "array: " + pair.Key + " -> [");
                    Console.WriteLine(sb.ToString() + "array: " + pair[0].type + " -> [");
                    // foreach (JsonStruct val in pair.Value)
                    foreach (JsonStruct val in pair)
                    {
                        LogJsonChecker(val, tab + 1);
                        Console.WriteLine(sb.ToString() + ", \n");
                    }
                    Console.WriteLine(sb.ToString() + "] <-");
                }
                // } while (arrEnum.MoveNext());
            }
        }

        private void JsonReflection(JsonStruct jss, object item)
        {
            if (jss.isSimple)
            {
                object setObj = jss.value;
                bool isFound = false;
                if (jss.name.ToLower().Contains("alignment"))
                {
                    try
                    {
                        object parseObj = null;
                        parseObj = ParseValue(typeof(ItemAlignment), jss.value);
                        if (parseObj != null)
                        {
                            setObj = parseObj;
                            isFound = SetItIfYouCan(jss.name, item, setObj, typeof(ItemAlignment));
                        }
                    }
                    catch (InvalidCastException) {}
                }
                else
                {
                    isFound = SetItIfYouCan(jss.name, item, setObj, null);
                    // It looks like the function covers all cases. If so, definitely good for me
                }

                if (!isFound)
                {
                    Console.WriteLine("Didn't found anything simple with name " + jss.name);
                }

            }
            else
            {
                object currentObj = null;
                
                if (jss.name == null || jss.name.Equals("")) //???
                {
                    currentObj = item;
                }
                else
                {
                    bool isFound = false;

                    Type itemType = item.GetType();

                    MethodInfo method = itemType.GetMethod("Get" + jss.name, new Type[] {});
                    if (method != null)
                    {
                        try
                        {
                            currentObj = method.Invoke(item, new object[]{});
                            if (currentObj != null) //???
                            {
                                isFound = true;
                            }
                        }
                        catch (Exception) {}
                    }

                    if (!isFound)
                    { //C# only stuff
                        method = itemType.GetMethod("get_" + jss.name, new Type[] {});
                        if (method != null)
                        {
                            try
                            {
                                currentObj = method.Invoke(item, new object[]{});
                                if (currentObj != null) //???
                                {
                                    isFound = true;
                                }
                            }
                            catch (Exception) {}
                        }
                    }

                    if (!isFound)
                    {
                        FieldInfo field = itemType.GetField(jss.name, BindingFlags.Public | BindingFlags.Instance);
                        if (field != null)
                        {
                            currentObj = field.GetValue(item);
                            if (currentObj != null) //???
                            {
                                isFound = true;
                            }
                        }
                    }

                    if (!isFound)
                    {
                        currentObj = ItemsFactory.GetObject(jss.name);
                        if (currentObj != null)
                        {
                            if (currentObj is IBaseItem)
                            {
                                if (item is Prototype)
                                {
                                    ((Prototype) item).AddItem((IBaseItem)currentObj);
                                }
                                else if (item is CoreWindow)
                                {
                                    ((CoreWindow) item).AddItem((IBaseItem)currentObj);
                                }
                                else
                                {
                                    Console.WriteLine("smth strange with adding an item");
                                }
                            }
                            else if (currentObj is Style)
                            {
                                //TODO how to apply?
                                ParseStyle(jss, (Style) currentObj, item);
                            }
                            //What to do if it's not?
                            isFound = true;
                        }
                    }

                    if (!isFound)
                    {
                        //for the special cases. Horizontal/VerticalSplitArea
                        if (((item is HorizontalSplitArea) && (jss.name.Equals("BottomItem") || jss.name.Equals("TopItem"))) ||
                                ((item is VerticalSplitArea) && (jss.name.Equals("LeftItem") || jss.name.Equals("RightItem"))))
                        {
                            //assume that there is only one item inside
                            // JsonStruct onlyVal = jss.jsonObjects.PeekFirst();
                            // currentObj = ItemsFactory.GetObject(onlyVal.name);
                            currentObj = ItemsFactory.GetObject(jss.jsonObjects[0].name);
                            if (currentObj != null)
                            {
                                if (currentObj is IBaseItem)
                                {
                                    bool res = TrySetMethods(jss.name, item, new object[]{(IBaseItem) currentObj}, 
                                                new Type[] {typeof(IBaseItem)});
                                    
                                    if (!res)
                                    {
                                        Console.WriteLine("Problems with the Horizontal/VerticalSplitArea: " + jss.name);
                                    }
                                }
                                else
                                {
                                    // System.out.println("dunno what if it's not");
                                }
                            }
                        }
                    }

                    if (!isFound)
                    {
                        Console.WriteLine("Nothing complex is found with name " + jss.name);
                    }

                    //Not sure that is' needed to be checked here, since this section is for the complex items, and is' is not complex
                }

                if (currentObj is Prototype)
                {
                    ParseObjectInners(currentObj, jss);
                }
                else
                { //what options do we have in this case?
                    if (currentObj is Indents)
                    {
                        Indents indents = new Indents();
                        
                        object parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Left", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Left", "0"));
                        if (parseObj != null)
                        {
                            indents.Left = (int)parseObj;
                        }

                        parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Right", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Right", "0"));
                        if (parseObj != null)
                        {
                            indents.Right = (int)parseObj;
                        }
                        
                        parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Top", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Top", "0"));
                        if (parseObj != null)
                        {
                            indents.Top = (int)parseObj;
                        }

                        parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Bottom", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Bottom", "0"));
                        if (parseObj != null)
                        {
                            indents.Bottom = (int)parseObj;
                        }

                        bool isFound = SetItIfYouCan(jss.name, item, indents, typeof(Indents));
                        if (!isFound)
                        {
                            Console.WriteLine(jss.name + " indents setter is not good");
                        }
                    }
                    else if (currentObj is Spacing)
                    {
                        Spacing spacing = new Spacing();

                        object parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Vertical", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Vertical", "0"));
                        if (parseObj != null)
                        {
                            spacing.Vertical = (int)parseObj;
                        }

                        parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Horizontal", "0"));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Horizontal", "0"));
                        if (parseObj != null)
                        {
                            spacing.Horizontal = (int)parseObj;
                        }

                        bool isFound = SetItIfYouCan(jss.name, item, spacing, typeof(Spacing));
                        if (!isFound)
                        {
                            Console.WriteLine(jss.name + " spacing setter is not good");
                        }
                    }
                    else if (currentObj is Font) //??? Not sure if it will work
                    {
                        Font font = DefaultsService.GetDefaultFont();
                        
                        int fontSize = (int)font.Size;
                        object parseObj = null;
                        // parseObj = ParseValue(typeof(int), GetFromMapIfExist(jss.jsonObjects, "Size", ((int)font.Size).ToString()));
                        parseObj = ParseValue(typeof(int), GetFromListIfExists(jss.jsonObjects, "Size", ((int)font.Size).ToString()));
                        if (parseObj != null)
                        {
                            fontSize = (int)parseObj;
                        }

                        FontStyle fontStyle = font.Style;
                        parseObj = null;
                        // parseObj = ParseValue(typeof(FontStyle), GetFromMapIfExist(jss.jsonObjects, "Style", font.Style.ToString()));
                        parseObj = ParseValue(typeof(FontStyle), GetFromListIfExists(jss.jsonObjects, "Style", font.Style.ToString()));
                        if (parseObj != null)
                        {
                            fontStyle = (FontStyle)parseObj;
                        }

                        FontFamily fontFamily = font.FontFamily;
                        try
                        {
                            // fontFamily = new FontFamily(GetFromMapIfExist(jss.jsonObjects, "Family", font.FontFamily.Name));
                            fontFamily = new FontFamily(GetFromListIfExists(jss.jsonObjects, "Family", font.FontFamily.Name));
                        }
                        catch(Exception) {}

                        font = new Font(fontFamily, fontSize, fontStyle);

                        bool isFound = SetItIfYouCan(jss.name, item, font, typeof(Font));
                        if (!isFound)
                        {
                            Console.WriteLine(jss.name + " setter is not good");
                        }
                    }
                    else
                    {
                        if (!(currentObj is Style))
                        {
                            //assume that I have just an empty item and will look inside
                            // foreach (KeyValuePair<String, JsonStruct> pair in jss.jsonObjects)
                            if (jss.jsonObjects != null && jss.jsonObjects.Count != 0)
                            {
                                foreach (JsonStruct pair in jss.jsonObjects)
                                // IEnumerator<KeyValuePair<String, JsonStruct>> objEnum = jss.jsonObjects.GetEnumerator();
                                // objEnum.Reset();
                                // do
                                {
                                //     KeyValuePair<String, JsonStruct> pair = objEnum.Current;
                                //     JsonReflection(pair.Value, item);
                                    JsonReflection(pair, item);
                                } // while (objEnum.MoveNext());
                            }
                        }
                    }
                }
            }
        }

        // private string GetFromMapIfExist(LinkedDictionary<String, JsonStruct> map, string key, string defVal)
        // {
        //     string lowKey = LowFirstChar(key);
        //     if (map.ContainsKey(lowKey))
        //     {
        //         return map[lowKey].value;
        //     }
        //     string upKey = UpFirstChar(key);
        //     if (map.ContainsKey(upKey))
        //     {
        //         return map[upKey].value;
        //     }
        //     return defVal;
        // }

        private string GetFromListIfExists(List<JsonStruct> list, string key, string defVal)
        {
            string lowKey = LowFirstChar(key);
            string upKey = UpFirstChar(key);

            foreach (JsonStruct jss in list)
            {
                if (jss.type.Equals(lowKey) || jss.type.Equals(upKey))
                {
                    return jss.value;
                }
            }

            return defVal;
        }

        private bool SetItIfYouCan(string parName, object item, object val, Type valType)
        {
            Type itemType = item.GetType();
            bool isSuccessful = false;
            string caselessName = parName.ToLower();

            FieldInfo[] fieldsArr = itemType.GetFields(BindingFlags.Public | BindingFlags.Instance);
            List<FieldInfo> possibleFields = new List<FieldInfo>();
            //TODO maybe I should improve checking. Something "precise first, than approximate"

            FieldInfo foundField = null;

            foreach (FieldInfo f in fieldsArr)
            {
                string tmpName = f.Name.ToLower();
                if (tmpName.Contains(caselessName))
                {
                    if (valType == null || (valType != null && f.FieldType.Equals(valType)))
                    {
                        possibleFields.Add(f);
                        if (tmpName.Equals(caselessName) || tmpName.Equals("is" + caselessName))
                        { //TODO not precise, need checking
                            foundField = f;
                        }
                    }
                }
            }

            //precise first-------------------------------------------------------------------------------------------------
            if (foundField != null)
            {
                isSuccessful = SetValue(foundField, item, val, (valType == null));
                if (isSuccessful)
                {
                    return true;
                }
            }

            MethodInfo[] methodsArr = itemType.GetMethods();
            List<MethodInfo> possibleMethods = new List<MethodInfo>();

            MethodInfo foundMethod = null;

            foreach (MethodInfo m in methodsArr)
            {
                string tmpName = m.Name.ToLower();
                if (tmpName.Contains(caselessName))
                {
                    ParameterInfo[] parsInfo = m.GetParameters();
                    if (!(parsInfo == null || parsInfo.Length == 0 || parsInfo.Length > 1 || parsInfo[0].GetType().IsArray))
                    {
                        if (valType == null || (valType != null && parsInfo[0].ParameterType.Equals(valType)))
                        {
                            possibleMethods.Add(m);

                            if (tmpName.Equals(caselessName) || tmpName.Equals("set" + caselessName) || tmpName.Equals("set_" + caselessName))
                            { //TODO not precise, need checking
                                foundMethod = m;
                            }
                        }
                    }
                }
            }

            if (foundMethod != null)
            {
                isSuccessful = ApplyMethod(foundMethod, item, new object[]{val}, (valType == null));
                if (isSuccessful)
                {
                    return true;
                }
            }
            //--------------------------------------------------------------------------------------------------------------

            //what a mess
            if (possibleFields.Count > 0)
            {
                foundField = possibleFields[0];

                if (possibleFields.Count > 1)
                {
                    //I don't like it, but it is the best idea for now
                    int len = foundField.Name.Length;
                    foreach (FieldInfo f in possibleFields)
                    {
                        int tmpLen = f.Name.Length;
                        if (tmpLen < len)
                        {
                            len = tmpLen;
                            foundField = f;
                        }
                    }
                }
            }

            if (possibleMethods.Count > 0)
            {
                foundMethod = possibleMethods[0];

                if (possibleMethods.Count > 1)
                {
                    //I don't like it, but it is the best idea for now
                    int len = foundMethod.Name.Length;
                    foreach (MethodInfo m in possibleMethods)
                    {
                        int tmpLen = m.Name.Length;
                        if (tmpLen < len)
                        {
                            len = tmpLen;
                            foundMethod = m;
                        }
                    }
                }
            }

            if (foundField == null && foundMethod == null)
            {
                return isSuccessful;
            }

            if (foundMethod == null || ((foundField != null) && foundField.Name.Length < foundMethod.Name.Length))
            {
                isSuccessful = SetValue(foundField, item, val, (valType == null));
            }

            if (!isSuccessful)
            {
                if (foundField == null || ((foundMethod != null) && foundField.Name.Length >= foundMethod.Name.Length))
                {
                    isSuccessful = ApplyMethod(foundMethod, item, new object[]{val}, (valType == null));
                }
            }

            return isSuccessful;
        }

        private void ParseObjectInners(object innerItem, JsonStruct jss)
        {
            // Object innerItem = currentObj; //(Prototype)
            // foreach (KeyValuePair<string, JsonStruct> pair in jss.jsonObjects)
            if (jss.jsonObjects != null && jss.jsonObjects.Count != 0)
            {
                foreach (JsonStruct pair in jss.jsonObjects)
                // IEnumerator<KeyValuePair<String, JsonStruct>> objEnum = jss.jsonObjects.GetEnumerator();
                // objEnum.Reset();
                // do
                {
                //     KeyValuePair<String, JsonStruct> pair = objEnum.Current;
                //     JsonReflection(pair.Value, innerItem); //presumably, the name in the object is equal to the key value
                    JsonReflection(pair, innerItem); //presumably, the name in the object is equal to the key value
                } // while (objEnum.MoveNext());
            }

            // foreach (KeyValuePair<string, List<JsonStruct>> pair in jss.jsonArrays)
            if (jss.jsonArrays != null && jss.jsonArrays.Count != 0)
            {
                foreach (List<JsonStruct> pair in jss.jsonArrays)
                // IEnumerator<KeyValuePair<String, List<JsonStruct>>> arrEnum = jss.jsonArrays.GetEnumerator();
                // arrEnum.Reset();
                // do
            {
                // KeyValuePair<String, List<JsonStruct>> pair = arrEnum.Current;
                string pairKey = pair[0].type;
                if (IsColorField(pairKey))
                {
                    //Color, BackgroundColor, ForegroundColor, TextColor...
                    //parse rgb or rgba array to color
                    //TODO maybe add another color type. hex or whatever
                    List<int> rgbColor = new List<int>();
                    // foreach (JsonStruct val in pair.Value)
                    foreach (JsonStruct val in pair)
                    {
                        if (val.value.Contains("."))
                        {
                            float f = 0;
                            object parseObj = null;
                            parseObj = ParseValue(typeof(float), val.value);
                            if (parseObj != null)
                            {
                                f = (float)parseObj;
                            }
                            if (f <= 1)
                            {
                                if (f > 0)
                                {
                                    f = f * 255.0f;
                                }
                                else
                                {
                                    f = 0;
                                }
                            }
                            rgbColor.Add((int) f);
                        }
                        else
                        {
                            object parseObj = null;
                            parseObj = ParseValue(typeof(int), val.value);
                            int f = 0;
                            if (parseObj != null)
                            {
                                f = (int)parseObj;
                            }
                            rgbColor.Add(f);
                        }
                    }
                    Color color;
                    if (rgbColor.Count == 4)
                    {
                        color = GraphicsMathService.ColorTransform(rgbColor[0], rgbColor[1], rgbColor[2], rgbColor[3]);
                    }
                    else
                    { //??? Don't really like it
                        color = GraphicsMathService.ColorTransform(rgbColor[0], rgbColor[1], rgbColor[2]);
                    }

                    // bool isFound = SetItIfYouCan(pair.Key, innerItem, color, typeof(Color));
                    bool isFound = SetItIfYouCan(pairKey, innerItem, color, typeof(Color));
                    if (!isFound)
                    {
                        // Console.WriteLine(pair.Key + " color setter is not good");
                        Console.WriteLine(pairKey + " color setter is not good");
                    }
                }
                // else if (pair.Key.ToLower().Contains("alignment")) //contains("Alignment")) {
                else if (pairKey.ToLower().Contains("alignment")) //contains("Alignment")) {
                {
                    //List<ItemAlignment> and that's all for now
                    ItemAlignment align = 0;
                    object parseObj;
                    // foreach (JsonStruct val in pair.Value)
                    foreach (JsonStruct val in pair)
                    {
                        parseObj = null;
                        parseObj = ParseValue(typeof(ItemAlignment), val.value);
                        if (parseObj != null)
                        {
                            align |= (ItemAlignment)parseObj;
                        }
                    }

                    // bool isFound = SetItIfYouCan(pair.Key, innerItem, align, typeof(ItemAlignment)); //in java it is list
                    bool isFound = SetItIfYouCan(pairKey, innerItem, align, typeof(ItemAlignment)); //in java it is list
                    if (!isFound)
                    {
                        // Console.WriteLine(pair.Key + " alignment setter is not good");
                        Console.WriteLine(pairKey + " alignment setter is not good");
                    }
                }
                // else if (pair.Key.ToLower().Contains("size")) //.contains("Size"))
                else if (pairKey.ToLower().Contains("size")) //.contains("Size"))
                {
                    //Block for methods with two parameters. Not good so far
                    // if (!pair.Key.ToLower().Contains("font")) //.contains("Font"))
                    if (!pairKey.ToLower().Contains("font")) //.contains("Font"))
                    {
                        // if (pair.Key.ToLower().Contains("policy")) //.contains("Policy"))
                        if (pairKey.ToLower().Contains("policy")) //.contains("Policy"))
                        {
                            List<SizePolicy> size = new List<SizePolicy>();
                            object parseObj;
                            // foreach (JsonStruct val in pair.Value)
                            foreach (JsonStruct val in pair)
                            {
                                parseObj = null;
                                parseObj = ParseValue(typeof(SizePolicy), val.value);
                                if (parseObj != null)
                                {
                                    size.Add((SizePolicy)parseObj);
                                }
                            }
                            
                            // bool res = TrySetMethods(pair.Key, innerItem, new object[]{size[0], size[1]}, 
                            bool res = TrySetMethods(pairKey, innerItem, new object[]{size[0], size[1]}, 
                                        new Type[]{typeof(SizePolicy), typeof(SizePolicy)});
                            if (!res)
                            {
                                // Console.WriteLine(pair.Key + " size policy setter is not good");
                                Console.WriteLine(pairKey + " size policy setter is not good");
                            }
                        }
                        else
                        {
                            List<int> size = new List<int>();
                            object parseObj;
                            // foreach (JsonStruct val in pair.Value)
                            foreach (JsonStruct val in pair)
                            {
                                parseObj = null;
                                parseObj = ParseValue(typeof(int), val.value);
                                if (parseObj != null)
                                {
                                    size.Add((int)parseObj);
                                }
                            }

                            // bool res = TrySetMethods(pair.Key, innerItem, new object[] {size[0], size[1]}, 
                            bool res = TrySetMethods(pairKey, innerItem, new object[] {size[0], size[1]}, 
                                        new Type[]{typeof(int), typeof(int)});

                            if (!res)
                            {
                                // Console.WriteLine(pair.Key + " size setter is not good");
                                Console.WriteLine(pairKey + " size setter is not good");
                            }
                        }
                    }
                }
                else
                {
                    // Console.WriteLine("what have we here " + pair.Key);
                    Console.WriteLine("what have we here " + pairKey);
                }
            } // while (arrEnum.MoveNext());
            }
        }

        private string LowFirstChar(string str)
        {
            if (str == null || str.Length == 0)
            {
                return "";
            }
            string sb = str.Substring(0, 1).ToLower();
            sb += str.Substring(1);
            return sb;
        }

        private string UpFirstChar(string str)
        {
            if (str == null || str.Length == 0)
            {
                return "";
            }
            string sb = str.Substring(0, 1).ToUpper();
            sb += str.Substring(1);
            return sb;
        }

        private bool IsColorField(string fieldName)
        {
            //TODO comparison with case ignoring
            string tmpName = fieldName.ToLower();
            // return (fieldName.Contains("Color") || fieldName.Contains("Background") || fieldName.Contains("Foreground") || fieldName.Contains("borderFill"));
            return (tmpName.Contains("color") || tmpName.Contains("background") || tmpName.Contains("foreground") || tmpName.Contains("borderfill"));
        }

        private bool TrySetMethods(string name, object item, object[] inputVals, Type[] inputTypes)
        { //c# special method
            Type itemType = item.GetType();
            string methodName = "Set" + name;
            MethodInfo method = itemType.GetMethod(methodName, inputTypes);

            if (method != null)
            {
                try
                {
                    method.Invoke(item, inputVals);
                    return true;
                }
                catch (Exception) {}
            }
            else
            {
                methodName = "set_" + name;                                        
                method = itemType.GetMethod(methodName, inputTypes);

                if (method != null)
                {
                    try
                    {
                        method.Invoke(item, inputVals);
                        return true;
                    }
                    catch (Exception) {}
                }
            }
            return false;
        }

        private bool ApplyMethod(MethodInfo method, object item, object[] inputVals, bool isNeedParse)
        {
            ParameterInfo[] pars = method.GetParameters();
            if (inputVals.Length != pars.Length)
            {
                return false;
            }

            object[] parseObjs = inputVals;
            
            if (isNeedParse)
            {
                parseObjs = new object[inputVals.Length];
                for (int i = 0; i < inputVals.Length; i++)
                {
                    parseObjs[i] = ParseValue(pars[i].ParameterType, inputVals[i]);
                    if (parseObjs[i] == null)
                    {
                        return false;
                    }
                }
            }

            try
            {
                method.Invoke(item, parseObjs);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        private bool SetValue(FieldInfo field, object item, object val, bool isNeedParse)
        {
            object parsVal = val;
            if (isNeedParse)
            {
                parsVal = ParseValue(field.FieldType, val);
            }
            if (parsVal == null)
            {
                return false;
            }

            try
            {
                field.SetValue(item, parsVal);
            }
            catch (Exception)
            {
                return false;
            }
            return true;
        }

        private object ParseValue(Type type, object obj)
        {
            //not sure if it will work properly
            try
            {
                if (type.Equals(typeof(Color)))
                {
                    //TODO need some color parsing
                    // if (obj is string)
                    // {
                    //     string val = (string) obj;
                    //     return Color.getColor(val);
                    // }
                    // else
                    // {
                        return (Color) obj; //even more lame than in java
                    // }
                }
                if (type.Equals(typeof(IBaseItem)))
                {
                    return (IBaseItem) obj;
                }
                if (type.Equals(typeof(Font)))
                {
                    return (Font) obj;
                }
                if (type.Equals(typeof(Indents)))
                {
                    return (Indents) obj;
                }
                if (type.Equals(typeof(Spacing)))
                {
                    return (Spacing) obj;
                }
                // case ALIGNMENTLIST:
                // {
                //     return (List<ItemAlignment>) obj;
                // }

                string val = (string) obj;
                return ParseValue(type, val);
            }
            catch (InvalidCastException)
            {
                return null;
            }
        }

        private object ParseValue(Type type, string val)
        {
            try
            {
                if (type.Equals(typeof(SizePolicy)))
                {
                    return Enum.Parse(typeof(SizePolicy), val);
                }
                if (type.Equals(typeof(ItemAlignment)))
                {
                    return Enum.Parse(typeof(ItemAlignment), val);
                }
                if (type.Equals(typeof(ItemStateType)))
                {
                    return Enum.Parse(typeof(ItemStateType), val);
                }
                if (type.Equals(typeof(FontStyle)))
                {
                    return Enum.Parse(typeof(FontStyle), val);                        
                }
                if (type.Equals(typeof(FontFamily)))
                {
                    return new FontFamily(val);
                }
                if (type.Equals(typeof(bool)))
                {
                    return Convert.ToBoolean(val);
                }
                if (type.Equals(typeof(char)))
                {
                    return Convert.ToChar(0);
                }
                if (type.Equals(typeof(float)))
                {
                    return float.Parse(val);
                }
                if (type.Equals(typeof(double)))
                {
                    return Convert.ToDouble(val);
                }
                if (type.Equals(typeof(int)))
                {
                    return Convert.ToInt32(val); //???
                }                

                // case typeof(string):
                // default:
                // val = (string) obj;
                
                return val;
            }
            catch (InvalidCastException)
            {
                return null;
            }
        }
    }
}