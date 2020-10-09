using System;
using System.Reflection;
using SpaceVIL;
using SpaceVIL.Core;
using System.Collections.Generic;
using System.Drawing;

namespace View
{
    class JsonTest : ActiveWindow
    {
        override public void InitWindow()
        {
            JsonApplier ja = new JsonApplier();
//        ja.applyJson("F:\\spacevil\\Java\\SpaceVIL\\spacevil\\src\\main\\java\\com\\spvessel\\spacevil\\View\\json\\JsonStyle.json", bc2);
            ja.ApplyJson("F:\\spacevil\\CSharp\\SpaceVIL\\SpaceVIL\\View\\json\\JsonWindow.json", this); //te);
            // Console.WriteLine("fsdf " + (FontStyle)Enum.Parse(typeof(FontStyle), "Regular"));

            // Font font = new Font(new FontFamily("Arial"), 16, FontStyle.Regular);
            // Console.WriteLine(font.FontFamily.Name);
            // Console.WriteLine(font.Style.ToString());

            // object te = new Label(); //TextEdit();            
            
            // Type type = te.GetType(); //typeof(TextEdit); //Type.GetType("TextEdit");
            // if (type != null)
            // {
                // Console.WriteLine("Not null type");
                
                // // MethodInfo mi = type.GetMethod("SetMargin", new Type[]{typeof(int), typeof(int), typeof(int), typeof(int)});
                // // currentObj = method.Invoke(item, new object[]{});
                // MethodInfo[] minfo = type.GetMethods();
                // // if (minfo != null)
                // foreach (MethodInfo mi in minfo)
                // {
                //     if (mi.Name.Contains("Alignment")) {
                //     ParameterInfo[] pars = mi.GetParameters();
                //     Console.WriteLine("method " + mi.Name + ", " + pars.Length + ":");
                //     foreach (ParameterInfo p in pars)
                //     {
                //         Console.WriteLine(p.ParameterType);
                //     }
                //     Console.WriteLine("\n");
                //     }
                // }
                // else
                // {
                //     Console.WriteLine("Null method");
                // }

            //     FieldInfo [] fields = type.GetFields(BindingFlags.Public | BindingFlags.Instance);
                
            //     foreach (FieldInfo fi in fields)
            //     {
                    
            //         Console.WriteLine(fi.Name);
            //     }

            //     Console.WriteLine(type.Name);
            //     FieldInfo field = type.GetField("IsFocusable", BindingFlags.Public | BindingFlags.Instance);
            //     // Console.WriteLine(field.GetValue(te).GetType());
            //     // bool b;
            //     // b = (bool)field.GetValue(te);
            //     // Console.WriteLine(b);
            //     // field.SetValue(te, !b);
            //     // Console.WriteLine(((TextEdit)te).IsFocusable);
            // }
            // else
            // {
            //     Console.WriteLine("Null type");
            // }
        }
    }
}