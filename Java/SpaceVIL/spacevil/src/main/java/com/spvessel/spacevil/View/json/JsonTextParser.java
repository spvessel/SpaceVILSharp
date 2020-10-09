package com.spvessel.spacevil.View.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;

import javax.sound.sampled.SourceDataLine;

public class JsonTextParser {
    public JsonStruct readFile(String filename) {
        List<String> linesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (!isContainBreaker(line)) {
                        linesList.add(line);
                    } else {
                        linesList.addAll(checkByChar(line));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        if (linesList.size() > 0) {
            linesList = checkColonIsNotLast(linesList);

//            System.out.println("\n\n");
//            for (String l : lines) {
//                System.out.println(l);
//            }
//            System.out.println("\n\n");

            return parseLines(linesList);
        }
        return new JsonStruct(false);
    }

    private boolean isContainBreaker(String str) {
        return (str.contains("{") || str.contains("}") || str.contains("[") || str.contains("]") || str.contains(","));
    }

    private List<String> checkByChar(String line) {
        List<String> list = new ArrayList<>();
        boolean inQuotes = false;

        int inc = -1;
        int N = line.length();
        int splitFrom = 0;
        for (char c : line.toCharArray()) {
            inc++;
            if (c == _quote) {
                if ((inc == 0) || (line.charAt(inc - 1) != '\\')) {
                    inQuotes = !inQuotes;
                }
            } else {
                if (inQuotes) {
                    continue;
                }

                if ((c == _comma.charAt(0)) || isOpenBrace(c)) { //assume that there is no commas after open brackets
                    //split line after that char
                    list.add(line.substring(splitFrom, inc + 1));
                    splitFrom = inc + 1;
                } else if (isCloseBrace(c)) {
                    //split after the brace if there is no comma after and before if it's not start of the line already
                    if (inc > 0 && splitFrom != inc) {
                        list.add(line.substring(splitFrom, inc));
                        splitFrom = inc;
                    }
                    if ((inc >= N - 1) || (line.charAt(inc +  1) != _comma.charAt(0))) {
                        list.add(line.substring(splitFrom, inc + 1));
                        splitFrom = inc + 1;
                        //else do nothing since the next step will solve the problem
                    }
                }
            }
        }

        if (inQuotes) {
            //TODO quotes are unbalanced, attention here
        }

        String tmpStr = line.substring(splitFrom);
        if (tmpStr.trim().length() > 0) {
            list.add(tmpStr);
        }

        return list;
    }

    private List<String> checkColonIsNotLast(List<String> lines) {
        List<String> improvedLines = new ArrayList<>();
        int N = lines.size();
        for (int i = 0; i < N; i++) {
            String line = lines.get(i);
            if (line.trim().endsWith(_colon) && i < N - 1) {
                improvedLines.add(line + " " + lines.get(i + 1));
                i++; //TODO attention here!
            } else {
                improvedLines.add(line);
            }
        }

        return improvedLines;
    }

    private char _leftBrace = '{';
    private char _rightBrace = '}';
    private char _leftSquare = '[';
    private char _rightSquare = ']';
    private String _comma = ",";
    private String _colon = ":";
    private char _quote = '\"';

    private JsonStruct parseLines(List<String> lines) {
        JsonStruct rootObj = new JsonStruct(false);
        parseObject(lines, 1, lines.size() - 2, rootObj); //TODO look out to the boundaries
        return rootObj;
    }

//    private boolean isBrace(char token) {
//        return (isOpenBrace(token) || isCloseBrace(token));
//    }

    private boolean isOpenBrace(char token) {
        return ((token == _leftBrace) || (token == _leftSquare));
    }

    private boolean isCloseBrace(char token) {
        return ((token == _rightBrace) || (token == _rightSquare));
    }

    private int findObjectsEnd(List<String> lines, int lineNum, char openBrace) {
        char closeBrace = getCloseBrace(openBrace);
        String openBraceStr = String.valueOf(openBrace);
        String closeBraceStr = String.valueOf(closeBrace);
        int braceCount = 0;
        int ind = lineNum;
        while (ind < lines.size()) {
            if (lines.get(ind).contains(openBraceStr)) {
                braceCount++;
            }
            if (lines.get(ind).contains(closeBraceStr)) {
                braceCount--;
            }
            if (braceCount == -1) {
                break;
            }
            ind++;
        }

        return ind;
    }

    private char getCloseBrace(char openBrace) {
        if (openBrace == _leftBrace) {
            return _rightBrace;
        }
        // if (openBrace == _leftSquare)
        return _rightSquare;
    }

    private int checkNextLine(List<String> lines, int fromInd, JsonStruct parent) {
        String currentLine = lines.get(fromInd).trim();

        String[] parts = splitByColon(currentLine);
        String name = parts[0].trim();
        name = removeQuotes(name);

        String value = "";
        if (parts.length != 1) {
            value = parts[1].trim();
            value = removeComma(value);
            value = removeQuotes(value);
        }

//        if (value.length() == 0) {
//            //TODO something wrong with syntax mayve need some checking
//            System.out.println("something wrong with syntax -> " + currentLine);
//        }

        char checkingToken = value.charAt(0);
        if (isOpenBrace(checkingToken)) {
            //brace-first cases
            int lineInd = findObjectsEnd(lines, fromInd + 1, checkingToken);
            if (checkingToken == _leftBrace) { //object
                JsonStruct newObj = new JsonStruct(false);
                newObj.name = name; //new String(name);
                parseObject(lines, fromInd + 1, lineInd - 1, newObj);

                // parent.jsonObjects.put(name, newObj);
                newObj.type = name;
                parent.jsonObjects.add(newObj);
            } else if (checkingToken == _leftSquare) { //array
                // parent.jsonArrays.put(name, parseArray(lines, fromInd + 1, lineInd - 1));
                
                parent.jsonArrays.add(parseArray(name, lines, fromInd + 1, lineInd - 1));
            }
            fromInd = lineInd + 1;
        } else {
            //covers all simple cases
            JsonStruct jss = new JsonStruct(true);
            jss.value = value; //no trims here
            jss.name = name;

            // parent.jsonObjects.put(name, jss);
            jss.type = name;
            parent.jsonObjects.add(jss);
            fromInd++;
        }

        return fromInd;
    }

    private String removeQuotes(String str) {
        //remove quotes only if there are some
        if (str.charAt(0) == _quote) { //предполагаем, что если первая ковычка, то и последняя тоже
            str = str.substring(1, str.length() - 1);
        }
        return str;
    }

    private String removeComma(String str) {
        //remove come if line ends with it
        if (str.endsWith(_comma)) {
            str = str.substring(0, str.length() - 1).trim(); //remove comma and trim for the good measure
        }
        return str;
    }

    private String[] splitByColon(String line) {
        //split line by colon which is not inside quotes
        String[] ans = line.split(_colon);
        if (ans.length <= 2) {
            //maybe should do something if equals 1 but not now
            return ans;
        }

        ans = new String[2];
        //default?
        ans[0] = line;
        ans[1] = "";

        boolean inQuotes = false;

        int inc = -1;
        int N = line.length();
        for (char c : line.toCharArray()) {
            inc++;
            if (c == _quote) {
                if ((inc == 0) || (line.charAt(inc - 1) != '\\')) {
                    inQuotes = !inQuotes;
                }
            } else {
                if (inQuotes) {
                    continue;
                }
                if (c == _colon.charAt(0)) {
                    ans[0] = line.substring(0, inc);
                    if (inc + 1 < N) {
                        ans[1] = line.substring(inc + 1);
                    }
                    break;
                }
            }
        }
        return ans;
    }

    private void parseObject(List<String> lines, int fromInd, int toInd, JsonStruct parent) {
        int lineNum = fromInd;
        while (lineNum <= toInd) {
            lineNum = checkNextLine(lines, lineNum, parent);
        }
    }

    private List<JsonStruct> parseArray(String type, List<String> lines, int fromInd, int toInd) {
        List<JsonStruct> ansList = new ArrayList<>();
        int lineNum = fromInd;
        while (lineNum <= toInd) {
            String currentLine = lines.get(lineNum).trim();
            currentLine = removeComma(currentLine);

            if (isOpenBrace(currentLine.charAt(0))) { // && currentLine.length() == 1) {
                char checkingToken = currentLine.charAt(0);
                //1. {, [

                int lineInd = findObjectsEnd(lines, lineNum + 1, checkingToken);

                if (checkingToken == _leftBrace) { //object
                    JsonStruct newObj = new JsonStruct(false);
//                    newObj.name = name; //new String(name);
                    parseObject(lines, lineNum + 1, lineInd - 1, newObj); //???

                    newObj.type = type;
                    ansList.add(newObj);
                } else if (checkingToken == _leftSquare) { //array
                    JsonStruct newObj = new JsonStruct(false);

                    // newObj.jsonArrays.put("", parseArray(lines, lineNum + 1, lineInd - 1));
                    // utterily strange
                    newObj.jsonArrays.add(parseArray("", lines, lineNum + 1, lineInd - 1));

                    newObj.type = type;
                    //Life is strange
                    ansList.add(newObj);
                }

                lineNum = lineInd;
            } else {
                JsonStruct jss = new JsonStruct(true);
                currentLine = removeQuotes(currentLine);
                jss.value = currentLine;

                jss.type = type;
                ansList.add(jss);
            }
            lineNum++;
        }
        return ansList;
    }

    class JsonStruct {
        String type;
        String name;
        String value;
        boolean isSimple;
        // Map<String, JsonStruct> jsonObjects;
        List<JsonStruct> jsonObjects;
        // Map<String, List<JsonStruct>> jsonArrays;
        List<List<JsonStruct>> jsonArrays;

        JsonStruct(boolean isSimple) {
            this.isSimple = isSimple;
            this.name = "";
            this.value = "";
            this.type = ""; //???
            if (!isSimple) {
                jsonObjects = new LinkedList<>(); //new LinkedHashMap<>();
                jsonArrays = new LinkedList<>(); //new LinkedHashMap<>();
            }
        }
    }
}
