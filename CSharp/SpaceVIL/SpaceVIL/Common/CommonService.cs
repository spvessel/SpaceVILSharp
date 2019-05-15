using System.Drawing;
using System;
using System.Collections.Generic;
using System.Reflection;
using Glfw3;
using SpaceVIL.Core;

namespace SpaceVIL.Common
{
    public static class CommonService
    {
        private static String _version = "0.3.1.4-ALPHA - February 2019";

#if STANDARD
    private static String _platform = "Standard";
#else
        private static String _platform = "Core";
#endif

        public static String GetSpaceVILInfo()
        {
#if LINUX
                return "SpaceVIL version: " + _version + "\n"
                + "Platform: .Net " + _platform + "\n"
                + "OS type: Linux\n";
#elif WINDOWS
                return "SpaceVIL version: " + _version + "\n"
                + "Platform: .Net " + _platform + "\n"
                + "OS type: Windows\n";
#elif MAC
                return "SpaceVIL version: " + _version + "\n"
                + "Platform: .Net " + _platform + "\n"
                + "OS type: Mac OS X\n";
#else
            return "SpaceVIL version: " + _version + "\n"
                + "Platform: .Net " + _platform + "\n"
                + "OS type: Windows\n";
#endif
        }

        public static String ClipboardTextStorage = String.Empty;
        internal static readonly object GlobalLocker = new object();

        //os type
#if LINUX
                private const SpaceVIL.Core.OSType _os_type = SpaceVIL.Core.OSType.Linux;
#elif WINDOWS
                private const SpaceVIL.Core.OSType _os_type = SpaceVIL.Core.OSType.Windows;
#elif MAC
                private const SpaceVIL.Core.OSType _os_type = SpaceVIL.Core.OSType.Mac;
#else
        private const SpaceVIL.Core.OSType _os_type = SpaceVIL.Core.OSType.Windows;
#endif

        public static SpaceVIL.Core.OSType GetOSType()
        {
            return _os_type;
        }

        //cursors 
        public static Glfw.Cursor CursorArrow;
        public static Glfw.Cursor CursorInput;
        public static Glfw.Cursor CursorHand;
        public static Glfw.Cursor CursorResizeH;
        public static Glfw.Cursor CursorResizeV;
        public static Glfw.Cursor CursorResizeAll;

        public static bool InitSpaceVILComponents()
        {
            if (!Glfw.Init())
            {
                Console.WriteLine("Init SpaceVIL framework failed. Abort.\nReason: Init GLFW failed.");
                return false;
            }
            //cursors
            CursorArrow = Glfw.CreateStandardCursor(Glfw.CursorType.Arrow);
            CursorInput = Glfw.CreateStandardCursor(Glfw.CursorType.Beam);
            CursorHand = Glfw.CreateStandardCursor(Glfw.CursorType.Hand);
            CursorResizeH = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeX);
            CursorResizeV = Glfw.CreateStandardCursor(Glfw.CursorType.ResizeY);
            CursorResizeAll = Glfw.CreateStandardCursor(Glfw.CursorType.Crosshair);

            DisplayService.SetDisplaySize(Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Width,
                        Glfw.GetVideoMode(Glfw.GetPrimaryMonitor()).Height);

            DefaultsService.InitImages();
            DefaultsService.InitDefaultTheme();

            return true;
        }

        public static String GetClipboardString()
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
                return "";
            Glfw.Window id = window.GetGLWID();
            if (!id)
                return String.Empty;
            return Glfw.GetClipboardString(id);
        }

        public static void SetClipboardString(String text)
        {
            CoreWindow window = WindowsBox.GetCurrentFocusedWindow();
            if (window == null)
                return;
            Glfw.Window id = window.GetGLWID();
            if (!id)
                return;
            Glfw.SetClipboardString(id, text);
        }


        // internal static String ScanCodeToASCII(int scancode, KeyMods mods, int layout)
        // {
        //     String str = String.Empty;
        //     int result = 0;
        //     if (ScanCodeDictionary.TryGetValue(scancode, out result))
        //     {
        //         str = ASCIIDictionary[result];
        //         if (mods != KeyMods.Shift)
        //             str = str.ToLower();
        //     }
        //     return str;
        // }
        // #region  ASCII
        // public static readonly Dictionary<int, string> ASCIIDictionary = new Dictionary<int, string>()
        // {
        //     {9,"\t"},

        //     {32," "},
        //     {33,"!"},
        //     {34,"\""},
        //     {35,"#"},
        //     {36,"$"},
        //     {37,"%"},
        //     {38,"&"},
        //     {39,"\'"},
        //     {40,"("},
        //     {41,")"},
        //     {42,"*"},
        //     {43,"+"},
        //     {44,","},
        //     {45,"-"},
        //     {46,"."},
        //     {47,"/"},

        //     {48,"0"},
        //     {49,"1"},
        //     {50,"2"},
        //     {51,"3"},
        //     {52,"4"},
        //     {53,"5"},
        //     {54,"6"},
        //     {55,"7"},
        //     {56,"8"},
        //     {57,"9"},

        //     {58,":"},
        //     {59,";"},
        //     {60,"<"},
        //     {61,"="},
        //     {62,">"},
        //     {63,"?"},
        //     {64,"@"},

        //     {65,"A"},
        //     {66,"B"},
        //     {67,"C"},
        //     {68,"D"},
        //     {69,"E"},
        //     {70,"F"},
        //     {71,"G"},
        //     {72,"H"},
        //     {73,"I"},
        //     {74,"J"},
        //     {75,"K"},
        //     {76,"L"},
        //     {77,"M"},
        //     {78,"N"},
        //     {79,"O"},
        //     {80,"P"},
        //     {81,"Q"},
        //     {82,"R"},
        //     {83,"S"},
        //     {84,"T"},
        //     {85,"U"},
        //     {86,"V"},
        //     {87,"W"},
        //     {88,"X"},
        //     {89,"Y"},
        //     {90,"Z"},

        //     {91,"["},
        //     {92,"\\"},
        //     {93,"]"},
        //     {94,"^"},
        //     {95,"_"},
        //     {96,"`"},

        //     {97, "a"},
        //     {98, "b"},
        //     {99, "c"},
        //     {100,"d"},
        //     {101,"e"},
        //     {102,"f"},
        //     {103,"g"},
        //     {104,"h"},
        //     {105,"i"},
        //     {106,"j"},
        //     {107,"k"},
        //     {108,"l"},
        //     {109,"m"},
        //     {110,"n"},
        //     {111,"o"},
        //     {112,"p"},
        //     {113,"q"},
        //     {114,"r"},
        //     {115,"s"},
        //     {116,"t"},
        //     {117,"u"},
        //     {118,"v"},
        //     {119,"w"},
        //     {120,"x"},
        //     {121,"y"},
        //     {122,"z"},

        //     {123,"{"},
        //     {124,"|"},
        //     {125,"}"},
        //     {126,"~"},
        //     {127,"del"},

        //     {128,"А"},
        //     {129,"Б"},
        //     {130,"В"},
        //     {131,"Г"},
        //     {132,"Д"},
        //     {133,"Е"},
        //     {134,"Ж"},
        //     {135,"З"},
        //     {136,"И"},
        //     {137,"Й"},
        //     {138,"К"},
        //     {139,"Л"},
        //     {140,"М"},
        //     {141,"Н"},
        //     {142,"О"},
        //     {143,"П"},
        //     {144,"Р"},
        //     {145,"С"},
        //     {146,"Т"},
        //     {147,"У"},
        //     {148,"Ф"},
        //     {149,"Х"},
        //     {150,"Ц"},
        //     {151,"Ч"},
        //     {152,"Ш"},
        //     {153,"Щ"},
        //     {154,"Ъ"},
        //     {155,"Ы"},
        //     {156,"Ь"},
        //     {157,"Э"},
        //     {158,"Ю"},
        //     {159,"Я"},

        //     {160,"а"},
        //     {161,"б"},
        //     {162,"в"},
        //     {163,"г"},
        //     {164,"д"},
        //     {165,"е"},
        //     {166,"ж"},
        //     {167,"з"},
        //     {168,"и"},
        //     {169,"й"},
        //     {170,"к"},
        //     {171,"л"},
        //     {172,"м"},
        //     {173,"н"},
        //     {174,"о"},
        //     {224,"п"},
        //     {225,"р"},
        //     {226,"с"},
        //     {227,"т"},
        //     {228,"у"},
        //     {229,"ф"},
        //     {230,"х"},
        //     {231,"ц"},
        //     {232,"ш"},
        //     {233,"щ"},
        //     {234,"ъ"},
        //     {235,"Ы"},
        //     {236,"ь"},
        //     {237,"э"},
        //     {238,"ю"},
        //     {239,"я"},
        //     {240,"Ё"},
        //     {241,"ё"},
        // };
        // #endregion

        // #region ScanCode
        // internal static readonly Dictionary<int, int> ScanCodeDictionary = new Dictionary<int, int>()
        // {
        //     {2,49},
        //     {3,50},
        //     {4,51},
        //     {5,52},
        //     {6,53},
        //     {7,54},
        //     {8,55},
        //     {9,56},
        //     {10,57},
        //     {11,48},
        //     {12,45},
        //     {13,61},
        //     {15,9},
        //     {16,81},
        //     {17,87},
        //     {18,69},
        //     {19,82},
        //     {20,84},
        //     {21,89},
        //     {22,85},
        //     {23,73},
        //     {24,79},
        //     {25,80},
        //     {26,91},
        //     {27,93},
        //     {30,65},
        //     {31,83},
        //     {32,68},
        //     {33,70},
        //     {34,71},
        //     {35,72},
        //     {36,74},
        //     {37,75},
        //     {38,76},
        //     {39,59},
        //     {40,39},
        //     {41,96},
        //     {43,92},
        //     {44,90},
        //     {45,88},
        //     {46,67},
        //     {47,86},
        //     {48,66},
        //     {49,78},
        //     {50,77},
        //     {51,44},
        //     {52,46},
        //     {53,47},
        //     {57,32},
        //     {74,45},
        //     {78,43},
        //     {96,42},
        // };
        // #endregion
    }
}
