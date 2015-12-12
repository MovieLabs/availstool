package availstool;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.*;

import com.movielabs.avails.*;

import org.apache.logging.log4j.*;

/**
 *  Iterate over rows and cells
 */
public class AvailsTool {
    private enum Opts {
        s, f, o, sstoxml, xmltoss, dumpss, dumpsheet, clean, wx, v;
    }

    private final static String msg =
        "java -jar availstool.jar [-v] [-clean] [-wx] [-sstoxml | -xmltoss | -dumpss | -dumpsheet] [-s sheet] -f filename -o outputfile\n" +
        "where:\n" +
        " -v          verbose\n" +
        " -clean      clean up minor validation errors (e.g. trailing whitespace)\n" +
        " -wx         exit immediately if any validation errors are encountered\n" +
        " -sstoxml    convert named file:sheet to XML\n" +
        " -xmltoss    try to convert named XML file to a spreadsheet (NOT IMPLEMENTED)\n" +
        " -dumpss     dump the named spreadsheet file (.xlsx) to the console\n" +
        " -dumpsheet  dump the names file:sheet to the console\n" +
        " -f          the input file (required)\n" +
        " -o          the output file (required for -sstoxml)\n" +
        " -s          the named sheet within the .xlsx file (required for -sstoxml & -dumpsheet)\n" +
        "             (if an integer, treat as a 0-based index into the sheets)";

    public static void usage() {
        System.out.println(msg);
    }

    public static void main(String[] args) throws Exception {
        String fileName, outFile, sheetName;
        int sheetNum = -1;

        Logger log = LogManager.getLogger(AvailsTool.class.getName());
        log.info("Initializing logger");

        Options options = new Options();
        options.addOption(Opts.v.name(), false, "verbose mode");
        options.addOption(Opts.s.name(), true, "specify sheet");
        options.addOption(Opts.f.name(), true, "specify file name");
        options.addOption(Opts.o.name(), true, "specify output file name");
        options.addOption(Opts.sstoxml.name(), false, "convert avails spreadsheet to XML");
        options.addOption(Opts.xmltoss.name(), false, "convert avails XML to a spreadsheet");
        options.addOption(Opts.dumpsheet.name(), false, "dump a single sheet from a spreadsheet");
        options.addOption(Opts.dumpss.name(), false, "dump a spreadsheet file");
        options.addOption(Opts.wx.name(), false, "treat warning as fatal error");
        options.addOption(Opts.clean.name(), false, "clean up data entries");
	 
        CommandLineParser cli = new DefaultParser();
	 
        try {
            CommandLine cmd = cli.parse(options, args);
            boolean optToXML = cmd.hasOption(Opts.sstoxml.name());
            boolean optToSS = cmd.hasOption(Opts.xmltoss.name());
            boolean optDumpSS = cmd.hasOption(Opts.dumpss.name());
            boolean optDumpSheet = cmd.hasOption(Opts.dumpsheet.name());
            fileName = cmd.getOptionValue(Opts.f.name());
            sheetName = cmd.getOptionValue(Opts.s.name());
            boolean clean = cmd.hasOption(Opts.clean.name());
            boolean wx = cmd.hasOption(Opts.wx.name());
            boolean verbose = cmd.hasOption(Opts.v.name());
            AvailSS ss;
            AvailsSheet as;
            String message;

            if (sheetName != null) {
                Pattern pat = Pattern.compile("^\\d+$");
                Matcher m = pat.matcher(sheetName);
                if (m.matches())
                    sheetNum = Integer.parseInt(sheetName);
            }

            if (fileName == null)
                throw new ParseException("input file not specified");
           
            if (!(optToXML | optToSS | optDumpSS | optDumpSheet))
                throw new ParseException("missing operation");

            if (optToXML) {
                if (optToSS | optDumpSS | optDumpSheet)
                    throw new ParseException("more than one operation specified");
                outFile = cmd.getOptionValue(Opts.o.name());
                if (outFile == null)
                    throw new ParseException("output file not specified");

                ss = new AvailSS(fileName, log, wx, clean);
                if (sheetNum < 0)
                    as = ss.addSheet(sheetName);
                else
                    as = ss.addSheet(sheetNum);
                message = "toXML file: " + fileName + " sheet: " + sheetName;
                log.info(message);
                if (verbose)
                    System.out.println(message);
                log.info("Options: -clean:" + clean + " -wx:" + wx + " output file: " + outFile);
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                String shortDesc = String.format("generated XML from %s:%s on %s", fileName, sheetName, timeStamp);
                as.makeXMLFile(outFile, shortDesc);
            } else if (optToSS) {
             if (optToXML | optDumpSS | optDumpSheet)
                    throw new ParseException("more than one operation specified");
                // TODO implement this
                throw new UnsupportedOperationException("Not implemented");
            } else if (optDumpSS) {
                if (optToXML | optToSS | optDumpSheet)
                    throw new ParseException("more than one operation specified");
                message = "dumping file: " + fileName;
                log.info(message);
                if (verbose)
                    System.out.println(message);
                AvailSS.dumpFile(fileName);
            } else { // dumpSheet
                if (sheetName == null)
                    throw new ParseException("sheet name not specified");
                message = "dumping file: " + fileName + " sheet: " + sheetName;
                log.info(message);
                if (verbose)
                    System.out.println(message);
                ss = new AvailSS(fileName, log, wx, clean);
                if (sheetNum < 0)
                    as = ss.addSheet(sheetName);
                else
                    as = ss.addSheet(sheetNum);

                ss.dumpSheet(sheetName);
            }
        }
        catch (ParseException exp) {
            System.out.println( "bad command line: " + exp.getMessage() );
            usage();
            System.exit(-1);
        }
    }
}
