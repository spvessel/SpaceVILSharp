using System;
using View;

namespace Program
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine(SpaceVIL.Common.CommonService.GetSpaceVILInfo());
            new MainWindow().Show();
        }
    }
}
