using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    public static class PrintService
    {
        public static void PrintArray<TValue>(bool flat, TValue[] array)
        {
            foreach (var item in array)
            {
                if (!flat)
                    Console.WriteLine(item);
                else
                    Console.Write(item + " ");
            }
            Console.WriteLine();
        }

        public static void PrintInputs<TValue>(bool flat, params TValue[] array)
        {
            foreach (var item in array)
            {
                if (!flat)
                    Console.WriteLine(item);
                else
                    Console.Write(item + " ");
            }
            Console.WriteLine();
        }

        public static void PrintList<TValue>(List<TValue[]> list)
        {
            foreach (var item in list)
            {
                foreach (var part in item)
                {
                    Console.Write(part + " ");
                }
                Console.WriteLine();
            }
        }
        public static void PrintList<TValue>(List<TValue> list)
        {
            foreach (var item in list)
            {
                Console.WriteLine(item);
            }
        }
    }
}
