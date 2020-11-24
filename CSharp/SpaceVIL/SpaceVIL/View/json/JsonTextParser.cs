using System;
using System.IO;
using System.Text;
using System.Collections.Generic;
using SpaceVIL.Core;

namespace SpaceVIL {
    public class JsonTextParser {
        internal JsonStruct ReadFile(string filename)
        {
            List<string> linesList = new List<string>();
            using (StreamReader sr = new StreamReader(filename))
            {
                while (!sr.EndOfStream)
                {
                    String line = sr.ReadLine();
                    line = line.Trim();
                    if (line.Length > 0)
                    {
                        if (!IsContainBreaker(line))
                        {
                            linesList.Add(line);
                        }
                        else
                        {
                            linesList.AddRange(CheckByChar(line));
                        }
                    }
                }
            }

            if (linesList.Count > 0)
            {
                linesList = CheckColonIsNotLast(linesList);
                return ParseLines(linesList);
            }

            return new JsonStruct(false);
        }

        private bool IsContainBreaker(string str) {
            return (str.Contains("{") || str.Contains("}") || str.Contains("[") || str.Contains("]") || str.Contains(","));
        }

        private List<string> CheckByChar(string line) {
            List<string> list = new List<string>();
            bool inQuotes = false;

            int inc = -1;
            int N = line.Length;
            int splitFrom = 0;
            foreach (char c in line)
            {
                inc++;
                if (c == _quote)
                {
                    if ((inc == 0) || (line[inc - 1] != '\\'))
                    {
                        inQuotes = !inQuotes;
                    }
                }
                else
                {
                    if (inQuotes)
                    {
                        continue;
                    }

                    if ((c == _comma[0]) || IsOpenBrace(c))
                    { //assume that there is no commas after open brackets
                        //split line after that char
                        list.Add(line.Substring(splitFrom, inc + 1 - splitFrom));
                        splitFrom = inc + 1;
                    }
                    else if (IsCloseBrace(c))
                    {
                        //split after the brace if there is no comma after and before if it's not start of the line already
                        if (inc > 0 && splitFrom != inc)
                        {
                            list.Add(line.Substring(splitFrom, inc - splitFrom));
                            splitFrom = inc;
                        }
                        if ((inc >= N - 1) || (line[inc +  1] != _comma[0]))
                        {
                            list.Add(line.Substring(splitFrom, inc + 1 - splitFrom));
                            splitFrom = inc + 1;
                            //else do nothing since the next step will solve the problem
                        }
                    }
                }
            }

            if (inQuotes) {
                //TODO quotes are unbalanced, attention here
            }

            string tmpStr = line.Substring(splitFrom);
            if (tmpStr.Trim().Length > 0) {
                list.Add(tmpStr);
            }

            return list;
        }

        private List<string> CheckColonIsNotLast(List<string> lines)
        {
            List<string> improvedLines = new List<string>();
            int N = lines.Count;
            for (int i = 0; i < N; i++)
            {
                string line = lines[i];
                if (line.Trim().EndsWith(_colon) && i < N - 1)
                {
                    improvedLines.Add(line + " " + lines[i + 1]);
                    i++;
                }
                else
                {
                    improvedLines.Add(line);
                }
            }

            return improvedLines;
        }

        private char _leftBrace = '{';
        private char _rightBrace = '}';
        private char _leftSquare = '[';
        private char _rightSquare = ']';
        private string _comma = ",";
        private string _colon = ":";
        private char _quote = '\"';

        private JsonStruct ParseLines(List<string> lines)
        {
            JsonStruct rootObj = new JsonStruct(false);
            ParseObject(lines, 1, lines.Count - 2, rootObj); //TODO look out to the boundaries
            return rootObj;
        }

        // private bool IsBrace(char token) {
        //     return (IsOpenBrace(token) || IsCloseBrace(token));
        // }

        private bool IsOpenBrace(char token)
        {
            return ((token == _leftBrace) || (token == _leftSquare));
        }

        private bool IsCloseBrace(char token)
        {
            return ((token == _rightBrace) || (token == _rightSquare));
        }

        private int FindObjectsEnd(List<string> lines, int lineNum, char openBrace)
        {
            char closeBrace = GetCloseBrace(openBrace);
            string openBraceStr = openBrace.ToString();
            string closeBraceStr = closeBrace.ToString();
            int braceCount = 0;
            int ind = lineNum;
            while (ind < lines.Count)
            {
                if (lines[ind].Contains(openBraceStr))
                {
                    braceCount++;
                }
                if (lines[ind].Contains(closeBraceStr))
                {
                    braceCount--;
                }
                if (braceCount == -1)
                {
                    break;
                }
                ind++;
            }

            return ind;
        }

        private char GetCloseBrace(char openBrace)
        {
            if (openBrace == _leftBrace)
            {
                return _rightBrace;
            }
            // if (openBrace == _leftSquare)
            return _rightSquare;
        }

        private int CheckNextLine(List<string> lines, int fromInd, JsonStruct parent)
        {
            string currentLine = lines[fromInd].Trim();

            string[] parts = SplitByColon(currentLine);
            string name = parts[0].Trim();
            name = RemoveQuotes(name);

            string value = "";
            if (parts.Length != 1)
            {
                value = parts[1].Trim();
                value = RemoveComma(value);
                value = RemoveQuotes(value);
            }

            // if (value.Length == 0) {
            //     //TODO something wrong with syntax mayve need some checking
            //     System.out.println("something wrong with syntax -> " + currentLine);
            // }

            char checkingToken = value[0];
            if (IsOpenBrace(checkingToken))
            {
                //brace-first cases
                int lineInd = FindObjectsEnd(lines, fromInd + 1, checkingToken);
                if (checkingToken == _leftBrace)
                { //object
                    JsonStruct newObj = new JsonStruct(false);
                    newObj.name = name; //new String(name);
                    ParseObject(lines, fromInd + 1, lineInd - 1, newObj);

                    // parent.jsonObjects.Add(name, newObj);
                    newObj.type = name;
                    parent.jsonObjects.Add(newObj);
                }
                else if (checkingToken == _leftSquare)
                { //array
                    // parent.jsonArrays.Add(name, ParseArray(lines, fromInd + 1, lineInd - 1));
                    parent.jsonArrays.Add(ParseArray(name, lines, fromInd + 1, lineInd - 1));
                }
                fromInd = lineInd + 1;
            }
            else
            {
                //covers all simple cases
                JsonStruct jss = new JsonStruct(true);
                jss.value = value; //no trims here
                jss.name = name;

                // parent.jsonObjects.Add(name, jss);
                jss.type = name;
                parent.jsonObjects.Add(jss);
                fromInd++;
            }

            return fromInd;
        }

        private string RemoveQuotes(string str)
        {
            //remove quotes only if there are some
            if (str[0] == _quote)
            { //предполагаем, что если первая ковычка, то и последняя тоже
                str = str.Substring(1, str.Length - 1 - 1);
            }
            return str;
        }

        private string RemoveComma(string str)
        {
            //remove come if line ends with it
            if (str.EndsWith(_comma))
            {
                str = str.Substring(0, str.Length - 1).Trim(); //remove comma and trim for the good measure
            }
            return str;
        }

        private string[] SplitByColon(string line)
        {
            //split line by colon which is not inside quotes
            string[] ans = line.Split(_colon[0]);
            if (ans.Length <= 2)
            {
                //maybe should do something if equals 1 but not now
                return ans;
            }

            ans = new string[2];
            //default?
            ans[0] = line;
            ans[1] = "";

            bool inQuotes = false;

            int inc = -1;
            int N = line.Length;
            foreach (char c in line)
            {
                inc++;
                if (c == _quote)
                {
                    if ((inc == 0) || (line[inc - 1] != '\\'))
                    {
                        inQuotes = !inQuotes;
                    }
                }
                else
                {
                    if (inQuotes)
                    {
                        continue;
                    }
                    if (c == _colon[0])
                    {
                        ans[0] = line.Substring(0, inc);
                        if (inc + 1 < N)
                        {
                            ans[1] = line.Substring(inc + 1);
                        }
                        break;
                    }
                }
            }
            return ans;
        }

        private void ParseObject(List<string> lines, int fromInd, int toInd, JsonStruct parent)
        {
            int lineNum = fromInd;
            while (lineNum <= toInd)
            {
                lineNum = CheckNextLine(lines, lineNum, parent);
            }
        }

        private List<JsonStruct> ParseArray(string type, List<string> lines, int fromInd, int toInd)
        {
            List<JsonStruct> ansList = new List<JsonStruct>();
            int lineNum = fromInd;
            while (lineNum <= toInd)
            {
                string currentLine = lines[lineNum].Trim();
                currentLine = RemoveComma(currentLine);

                if (IsOpenBrace(currentLine[0])) // && currentLine.length() == 1) {
                {
                    char checkingToken = currentLine[0];
                    //1. {, [
                    int lineInd = FindObjectsEnd(lines, lineNum + 1, checkingToken);

                    if (checkingToken == _leftBrace)
                    { //object
                        JsonStruct newObj = new JsonStruct(false);
                        // newObj.name = name; //new String(name);
                        ParseObject(lines, lineNum + 1, lineInd - 1, newObj); //???

                        newObj.type = type;
                        ansList.Add(newObj);
                    }
                    else if (checkingToken == _leftSquare)
                    { //array
                        JsonStruct newObj = new JsonStruct(false);

                        // newObj.jsonArrays.Add("", ParseArray(lines, lineNum + 1, lineInd - 1));
                        // utterily strange
                        newObj.jsonArrays.Add(ParseArray("", lines, lineNum + 1, lineInd - 1));

                        newObj.type = type;
                        //Life is strange
                        ansList.Add(newObj);
                    }

                    lineNum = lineInd;
                }
                else
                {
                    JsonStruct jss = new JsonStruct(true);
                    currentLine = RemoveQuotes(currentLine);
                    jss.value = currentLine;

                    jss.type = type;
                    ansList.Add(jss);
                }
                lineNum++;
            }
            return ansList;
        }
    }

    internal class JsonStruct {
        internal string type;
        internal string name;
        internal string value;
        internal bool isSimple;
        // internal LinkedDictionary<string, JsonStruct> jsonObjects;
        internal List<JsonStruct> jsonObjects;
        // internal LinkedDictionary<string, List<JsonStruct>> jsonArrays;
        internal List<List<JsonStruct>> jsonArrays;

        internal JsonStruct(bool isSimple) {
            this.isSimple = isSimple;
            this.name = "";
            this.value = "";
            this.type = ""; //???
            if (!isSimple)
            {
                jsonObjects = new List<JsonStruct>(); //new LinkedDictionary<string, JsonStruct>();
                jsonArrays = new List<List<JsonStruct>>(); //new LinkedDictionary<string, List<JsonStruct>>();
            }
        }
    }
}