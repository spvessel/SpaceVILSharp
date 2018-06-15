using System;
using System.Drawing;
using SpaceVIL;
using System.Diagnostics;
using View;

namespace Program
{
    class Program
    {
        static void Main(string[] args)
        {
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start();

            //MainWindow mw = new MainWindow();
            //Settings sets = new Settings();
            //LayoutsTest lt = new LayoutsTest();
            LabelTest label = new LabelTest();
            //GridTest grid = new GridTest();
            //Settings.Show();
            //mw.Show();
            //lt.Show();
            label.Show();
            //grid.Show();
            //mw = null;
            //sets = null;
            //lt = null;
            //label = null;
            //grid = null;
            Console.WriteLine("Ready.");
            stopWatch.Stop();
            TimeSpan ts = stopWatch.Elapsed;
            string elapsedTime = String.Format("{0:00}:{1:00}:{2:00}.{3:00}",
            ts.Hours, ts.Minutes, ts.Seconds,
            ts.Milliseconds / 10);
            Console.WriteLine("RunTime " + elapsedTime);
        }
    }
}
