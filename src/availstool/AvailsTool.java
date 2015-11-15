package availstool;

import java.text.SimpleDateFormat;

import org.apache.commons.cli.*;

import com.movielabs.avails.*;

import org.apache.logging.log4j.*;

/**
 *  Iterate over rows and cells
 */
public class AvailsTool {
    private enum Opts {
        s, f, o, sstoxml, xmltoss, dumpss, clean, wx;
    }

    public static void usage() {
        System.out.println(
            "AvailTool [-clean] [-wx] [-sstoxml | -xmltoss | -dumpss] -s sheet -f filename [-o outputfile]");
    }

    public static void main(String[] args) throws Exception {
        String fileName;
        String outFile;

        // System.out.println(MONGO.Description + " | " + MONGO.Description.ordinal() + " | " +
        //                    MONGO.Description.name());
        // System.exit(0);
        
        Logger log = LogManager.getLogger(AvailsTool.class.getName());
        log.info("Initializing logger");

        Options options = new Options();
        options.addOption(Opts.s.name(), true, "specify sheet");
        options.addOption(Opts.f.name(), true, "specify file name");
        options.addOption(Opts.o.name(), true, "specify output file name");
        options.addOption(Opts.sstoxml.name(), false, "convert avails spreadsheet to XML");
        options.addOption(Opts.xmltoss.name(), false, "convert avails XML to a spreadsheet");
        options.addOption(Opts.dumpss.name(), false, "dump a spreadsheet file");
        options.addOption(Opts.wx.name(), false, "treat warning as fatal error");
        options.addOption(Opts.clean.name(), false, "clean up data entries");
	 
        CommandLineParser cli = new DefaultParser();
	 
        try {
            CommandLine cmd = cli.parse(options, args);
            boolean optToXML = cmd.hasOption(Opts.sstoxml.name());
            boolean optToSS = cmd.hasOption(Opts.xmltoss.name());
            boolean optDump = cmd.hasOption(Opts.dumpss.name());
            fileName = cmd.getOptionValue(Opts.f.name());
            if (fileName == null)
                throw new ParseException("input file not specified");

            outFile = cmd.getOptionValue(Opts.o.name());
            if (outFile == null)
                throw new ParseException("output file not specified");
            
            if (!(optToXML | optToSS | optDump))
                throw new ParseException("missing operation");
            if (optToXML) {
                if (optToSS | optDump)
                    throw new ParseException("more than one operation specified");
                boolean clean = cmd.hasOption(Opts.clean.name());
                boolean wx = cmd.hasOption(Opts.wx.name());
                String sheetName = cmd.getOptionValue(Opts.s.name());
                SS ss = new SS(fileName, sheetName, log);
                log.info("processing file: " + fileName + " sheet: " + sheetName);
                ss.dump();
                log.info("Options: -clean:" + clean + " -wx:" + wx + " output file: " + outFile);
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                String shortDesc = String.format("generated XML from %s:%s on %s", fileName, sheetName, timeStamp);
                AvailSS ss2 = new AvailSS(fileName, log, wx, clean);
                AvailsSheet as = ss2.addSheet(sheetName);
                as.makeXMLFile(outFile, shortDesc);
                // ss.toXML(clean, wx, outFile, shortDesc);
            } else if (optToSS) {
                if (optToXML | optDump)
                    throw new ParseException("more than one operation specified");
            } else { // dumpSS
                if (optToXML | optToSS)
                    throw new ParseException("more than one operation specified");
                SS.dump(fileName);
            }
        }
        catch( ParseException exp ) {
            System.out.println( "bad command line: " + exp.getMessage() );
            usage();
            System.exit(-1);
        }
    }
}
