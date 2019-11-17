/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.util;

import proguard.util.*;

import java.io.PrintWriter;
import java.util.List;

/**
 * This class prints out and counts warnings.
 *
 * @author Eric Lafortune
 */
public class WarningPrinter
{
    private final PrintWriter   printWriter;
    private final StringMatcher classFilter;
    private int                 warningCount;


    /**
     * Creates a new WarningPrinter that prints to the given print writer.
     */
    public WarningPrinter(PrintWriter printWriter)
    {
        this.printWriter = printWriter;
        this.classFilter = null;
    }


    /**
     * Creates a new WarningPrinter that prints to the given print stream,
     * except if the names of any involved classes matches the given filter.
     */
    public WarningPrinter(PrintWriter printWriter, List classFilter)
    {
        this.printWriter = printWriter;
        this.classFilter = classFilter == null ? null :
            new ListParser(new ClassNameParser()).parse(classFilter);
    }


    /**
     * Prints out the given warning and increments the warning count, if
     * the given class name passes the class name filter.
     */
    public void print(String className, String warning)
    {
        if (accepts(className))
        {
            print(warning);
        }
    }


    /**
     * Returns whether the given class name passes the class name filter.
     */
    public boolean accepts(String className)
    {
        return classFilter == null ||
            !classFilter.matches(className);
    }


    /**
     * Prints out the given warning and increments the warning count, if
     * the given class names pass the class name filter.
     */
    public void print(String className1, String className2, String warning)
    {
        if (accepts(className1, className2))
        {
            print(warning);
        }
    }


    /**
     * Returns whether the given class names pass the class name filter.
     */
    public boolean accepts(String className1, String className2)
    {
        return classFilter == null ||
            !(classFilter.matches(className1) ||
              classFilter.matches(className2));
    }


    /**
     * Prints out the given warning and increments the warning count.
     */
    private void print(String warning)
    {
        printWriter.println(warning);

        warningCount++;
    }


    /**
     * Returns the number of warnings printed so far.
     */
    public int getWarningCount()
    {
        return warningCount;
    }
}
