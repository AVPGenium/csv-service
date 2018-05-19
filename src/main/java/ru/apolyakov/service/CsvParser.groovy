package ru.apolyakov.service

import fuzzycsv.FuzzyCSV
import fuzzycsv.FuzzyCSVTable

import static fuzzycsv.FuzzyCSVTable.parseCsv
import static fuzzycsv.FuzzyCSVTable.tbl
import static java.lang.Double.*
import static java.lang.Float.parseFloat
import static java.lang.Integer.parseInt
import static java.lang.Long.parseLong

class CsvParser {
    static String parse(Reader reader)
    {
        return parseCsv(reader).toCsvString();
    }

    static def toNumber(def val)
    {
        if(val.isInteger())
        {
            return parseInt(val);
        }
        else if(val.isLong())
        {
            return parseLong(val);
        }
        else if(val.isDouble())
        {
            return parseDouble(val);
        }
        else if(val.isFloat())
        {
            return parseFloat(val);
        }
        return val;
    }

    static String process(List originalTable, def maxResult, def sortColumnName)
    {
        def table = tbl(originalTable);
        table.transform {it -> if(it.isNumber()) toNumber(it) else it};
        table = table.sort(sortColumnName);
        return table[1..maxResult].toCsvString();
    }

    static String process(Reader reader, def maxResult, def sortColumnName)
    {
        def table = parseCsv(reader);
        table.transform {it -> if(it.isNumber()) toNumber(it) else it};
        table = table.sort(sortColumnName);
        return table[1..maxResult].toCsvString();
    }

    static def csvFileToList(Reader reader)
    {
        return FuzzyCSV.toUnModifiableCSV(
                FuzzyCSVTable.toListOfLists(
                        FuzzyCSVTable.parseCsv(reader).csv).csv)
    }

    static def csvStringToList(String csvText)
    {
        return FuzzyCSV.toUnModifiableCSV(
                FuzzyCSVTable.toListOfLists(
                        FuzzyCSVTable.parseCsv(csvText).csv).csv)
    }

    static def getCSV(String path) {
        def text = getClass().getResource(path).text
        return FuzzyCSV.toUnModifiableCSV(
                FuzzyCSVTable.toListOfLists(
                        FuzzyCSVTable.parseCsv(text).csv).csv)
    }

}
