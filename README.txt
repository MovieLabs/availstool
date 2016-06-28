README - availstool
-------------------

0) Introduction: "availstool" is a simple Java command-line utility to
   excercise some of the functionality of the "availslib" utility.
   The latter is a separate github project (see below) that contains
   methos for converting EMA avails data contained within an Excel
   spreadsheet into an XML representation.

1) Source control: these files are maintained in a github repository.
   You can use the following shell command (either 'nix or Win32) to
   download the files therefrom:

> mkdir <your target directory> (e.g. availstool)
> cd <your target directory>
> git init
> git remote add github https://github.com/pgj-ml/availstool.git
> git fetch github

2) Building availstool: Eclipse was used for initial development;
   however any IDE should work equally well.  In addition to the
   source files you will need the following libraries:
   
   - JRE System Library (JavaSE-1.8 recommended)
   - The availslib library (this is a separate github project)
   - Apache Commons CLI (https://commons.apache.org/proper/commons-cli/);
     used the following jar:
       * commons-cli-1.3.1.jar
   - Apache Poi (https://poi.apache.org/); used the following jars:
       * poi-3.13-20150929.jar
       * poi-ooxml-3.13-20150929.jar
       * poi-ooxml-schemas-3.13-20150929.jar
   - Apache XML Beans (https://xmlbeans.apache.org/); used the following jar:
       * xmlbeans-2.6.0.jar
   - Apache Apache Log4j 2 (http://logging.apache.org/log4j/2.x/); used the
     following jars:
       * apache-log4j-2.4.1-bin/log4j-api-2.4.1.jar
       * apache-log4j-2.4.1-bin/log4j-core-2.4.1.jar

3) Documentation: running "availstool -help" yields the following

   java -jar availstool.jar [-v] [-clean] [-wx] [-sstoxml | -xmltoss | -dumpss | -dumpsheet]
                            [-s sheet] -f filename -o outputfile\n +
   where:\n +
   -v          verbose\n +
   -clean      clean up minor validation errors (e.g. trailing whitespace)\n +
   -wx         exit immediately if any validation errors are encountered\n +
   -sstoxml    convert named file:sheet to XML\n +
   -xmltoss    try to convert named XML file to a spreadsheet (NOT IMPLEMENTED)\n +
   -dumpss     dump the named spreadsheet file (.xlsx) to the console\n +
   -dumpsheet  dump the names file:sheet to the console\n +
   -f          the input file (required)\n +
   -o          the output file (required for -sstoxml)\n +
   -s          the named sheet within the .xlsx file (required for -sstoxml & -dumpsheet)\n +
               (if an integer, treat as a 0-based index into the sheets);

4) Test and verification: for a simple test perform the following:
        * build availstool
        * cd into the test directory
        * run the command "availstool -v -sstoxml -f test02.xlsx -s Movies -o test02.xml"
        * examine the output file created, "testo2.xml"
