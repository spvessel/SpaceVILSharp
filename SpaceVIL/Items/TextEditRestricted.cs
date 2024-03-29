﻿using SpaceVIL.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class TextEditRestricted : TextEditStorage
    {
        internal TextEditRestricted()
        {
            //super();
            EventTextInput = null;
            EventTextInput += OnTextInput;
            EventKeyPress += OnKeyPress;
            EventMouseDoubleClick = OnMouseDoubleClick;

            numbers = new List<String>() {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

            UpdateCurrentValue();
            // IsEditable = false;
        }

        private List<String> numbers;
        private InputRestriction inres = InputRestriction.DoubleNumbers;

        private void OnMouseDoubleClick(Object sender, MouseArgs args) {
            if (args.Button == MouseButton.ButtonLeft)
            {
                SelectAll();
                // IsEditable = true;
            }
        }

        private void OnKeyPress(IItem sender, KeyArgs args) {
            if (args.Key == KeyCode.Enter || args.Key == KeyCode.NumpadEnter)
            {
                ConstructCurrentValue();
            }
        }

        private void ConstructCurrentValue()
        {
            double znc;
            int i1, sgc;
            String t0 = GetText();
            char[] delim = { ',', '.' };
            if (!t0.Equals("-") && t0.Length > 0)
            {
                String[] txt = t0.Split(delim);

                try
                {
                    znc = Int32.Parse(txt[0]);
                    if (txt.Length > 1 && txt[1].Length > 0)
                    {
                        i1 = Int32.Parse(txt[1]);
                        sgc = txt[1].Length;
                        znc += i1 / Math.Pow(10.0, sgc);
                    }
                    currentValue = znc;
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.StackTrace);
                    if (t0.StartsWith("-"))
                    {
                        currentValue = minValue;
                    }
                    else
                    {
                        currentValue = maxValue;
                    }
                }
            }
            UpdateCurrentValue();
            // IsEditable = false;
        }

        private void OnTextInput(Object sender, TextInputArgs args)
        {
            String tmptxt = GetText();
            bool isFirst = (IsBeginning() && !tmptxt.Contains("-"));
            bool hasDot = (tmptxt.Contains(".") || tmptxt.Contains(","));

            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);

            bool isValid = false;
            switch (inres)
            {
                case InputRestriction.IntNumbers:
                    if (numbers.Contains(str))
                    {
                        isValid = true;
                    }
                    else if (isFirst && str.Equals("-"))
                    {
                        isValid = true;
                    }
                    else
                    {
                        isValid = false;
                    }
                    break;

                case InputRestriction.DoubleNumbers:
                    if (numbers.Contains(str))
                    {
                        isValid = true;
                    }
                    else if (isFirst && str.Equals("-"))
                    {
                        isValid = true;
                    }
                    else if (!isFirst && !hasDot && (str.Equals(".") || str.Equals(",")))
                    {
                        isValid = true;
                    }
                    else
                    {
                        isValid = false;
                    }
                    break;

                default:
                    isValid = true;
                    break;
            }

            if (isValid)
            {
                base.CutText();
                base.PasteText(str);
            }
            else
            {
                base.PasteText("");
            }
        }

        private bool CheckValidity(String inputStr)
        {
            String str = inputStr;
            if (inputStr.StartsWith("-"))
            {
                str = inputStr.Substring(1);
            }

            if (str.StartsWith(".") || str.StartsWith(","))
            {
                return false;
            }

            bool hasDot = false;

            for (int i = 0; i < str.Length; i++)
            {
                String s = str.Substring(i, 1);
                if (!numbers.Contains(s))
                {
                    if ((inres == InputRestriction.DoubleNumbers) && !hasDot && (s.Equals(".") || s.Equals(",")))
                    {
                        hasDot = true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }

            return true;
        }

        public override void PasteText(String pasteStr)
        {
            if (CheckValidity(pasteStr))
            {
                base.PasteText(pasteStr);
                ConstructCurrentValue();
            }
        }

        internal void SetInputRestriction(InputRestriction ir)
        {
            inres = ir;
        }

        private double minValue = -100;
        private double maxValue = 100;
        private double currentValue = 0;
        private double step = 1;
        private String rou = "F2";
        private int signsCount = 2;
        private int minSignsCount = 2;
        private int maxSignsCount = 5;

        internal void SetParameters(double currentValue, double minValue, double maxValue, double step)
        {
            this.currentValue = currentValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.step = step;
            inres = InputRestriction.DoubleNumbers;
            UpdateCurrentValue();
            int i = 0;
            String[] splitter = (currentValue.ToString()).Split('.');
            if (splitter.Length > 1)
            {
                i = splitter[1].Length;
            }

            splitter = (minValue.ToString()).Split('.');            
            if (splitter.Length > 1 && i < splitter[1].Length)
            {
                i = splitter[1].Length;
            }

            splitter = (maxValue.ToString()).Split('.');            
            if (splitter.Length > 1 && i < splitter[1].Length)
            {
                i = splitter[1].Length;
            }

            splitter = (step.ToString()).Split('.');            
            if (splitter.Length > 1 && i < splitter[1].Length)
            {
                i = splitter[1].Length;
            }
            
            if (i < minSignsCount)
            {
                i = minSignsCount;
            }
            else if (i > maxSignsCount) 
            {
                i = maxSignsCount;
            }

            signsCount = i;
            rou = "F" + (signsCount.ToString());
        }

        internal void SetParameters(int currentValue, int minValue, int maxValue, int step)
        {
            this.currentValue = currentValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.step = step;
            inres = InputRestriction.IntNumbers;
            UpdateCurrentValue();
        }

        public void SetValue(int value)
        {
            currentValue = value;
            UpdateCurrentValue();
        }

        public void SetValue(double value)
        {
            currentValue = value;
            inres = InputRestriction.DoubleNumbers;
            UpdateCurrentValue();
        }

        public double GetValue()
        {
            return currentValue;
        }

        public void SetAccuracy(int accuracy) {
            signsCount = accuracy;
            rou = "F" + (signsCount.ToString());
        }

        private void UpdateCurrentValue()
        {
            if (currentValue < minValue)
            {
                currentValue = minValue;
            }
            if (currentValue > maxValue)
            {
                currentValue = maxValue;
            }

            switch (inres)
            {
                case InputRestriction.IntNumbers:
                    SetText(((int)currentValue).ToString());
                    break;
                default: //case DOUBLENUMBERS:
                    SetText((currentValue.ToString(rou)));
                    break;
            }
        }

        internal void IncreaseValue()
        {
            // IsEditable = false;
            currentValue += step;
            UpdateCurrentValue();
        }

        internal void DecreaseValue()
        {
            // IsEditable = false;
            currentValue -= step;
            UpdateCurrentValue();
        }

        internal enum InputRestriction
        {
            IntNumbers, DoubleNumbers, Letters, All
        }
    }
}
