using SpaceVIL.Core;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SpaceVIL
{
    internal class TextEditRestricted : TextEdit
    {
        internal TextEditRestricted()
        {
            //super();
            
            EventTextInput = OnTextInput;

            numbers = new List<String>() {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

            UpdateCurrentValue();
        }

        private List<String> numbers;
        private InputRestriction inres = InputRestriction.DoubleNumbers;
        private EventInputTextMethodState parentEv;

        private void OnTextInput(Object sender, TextInputArgs args)
        {
            String tmptxt = GetText();
            bool isFirst = (IsBegining() && !tmptxt.Contains("-"));
            bool hasDot = (tmptxt.Contains(".") || tmptxt.Contains(","));

            byte[] input = BitConverter.GetBytes(args.Character);
            string str = Encoding.UTF32.GetString(input);

            bool isValid = false;
            switch (inres)
            {
                case InputRestriction.IntNumbers:
                    if (numbers.Contains(str))
                        isValid = true;
                    else if (isFirst && str.Equals("-"))
                        isValid = true;
                    else isValid = false;
                    break;
                case InputRestriction.DoubleNumbers:
                    if (numbers.Contains(str))
                        isValid = true;
                    else if (isFirst && str.Equals("-"))
                        isValid = true;
                    else if (!isFirst && !hasDot && (str.Equals(".") || str.Equals(",")))
                        isValid = true;
                    else isValid = false;
                    break;
                default:
                    isValid = true;
                    break;
            }

            if (isValid)
            {
                base.PasteText(str);
            }
            else
            {
                base.PasteText("");
            }

            double znc;
            int i1;
            char[] delim = { ',', '.' };
            String[] txt = GetText().Split(delim);
            if (!txt.Equals("-"))
            {
                znc = Int32.Parse(txt[0]);
                if (txt.Length > 1 && txt[1].Length > 0)
                {
                    i1 = Int32.Parse(txt[1]);
                    znc += i1 / Math.Pow(10.0, txt[1].Length);
                }

//            switch (inres) {
//                case INTNUMBERS:
//                    znc = Integer.parseInt(txt);
//                    break;
//                case DOUBLENUMBERS:
//                default:
//                    znc = Double.parseDouble(txt);
//                    break;
//            }
                currentValue = znc;
                UpdateCurrentValue();
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

        internal void SetParameters(double currentValue, double minValue, double maxValue, double step)
        {
            this.currentValue = currentValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.step = step;
            inres = InputRestriction.DoubleNumbers;
            UpdateCurrentValue();
            String[] splitter = (currentValue.ToString()).Split('.');
            int i = 0;
            if (splitter.Length > 1)
                i = splitter[1].Length;
            splitter = (minValue.ToString()).Split('.');
            if (splitter.Length > 1 && i < splitter[1].Length)
                i = splitter[1].Length;
            splitter = (maxValue.ToString()).Split('.');
            if (splitter.Length > 1 && i < splitter[1].Length)
                i = splitter[1].Length;
            splitter = (step.ToString()).Split('.');
            if (splitter.Length > 1 && i < splitter[1].Length)
                i = splitter[1].Length;
            if (i < 2)
                i = 2;
            else if (i > 5) i = 5;
            rou = "F" + (i.ToString());
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

        private void UpdateCurrentValue()
        {
            if (currentValue < minValue)
                currentValue = minValue;
            if (currentValue > maxValue)
                currentValue = maxValue;

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
            currentValue += step;
            UpdateCurrentValue();
        }

        internal void DecreaseValue()
        {
            currentValue -= step;
            UpdateCurrentValue();
        }
    }
}
