package availstool;

import org.apache.commons.cli.*;
import com.movielabs.avails.*;
import org.apache.logging.log4j.*;

/**
 *  Iterate over rows and cells
 */
public class AvailsTool {
    private enum Opts {
        s, f, o, sstoxml, xmltoss, dumpss, strict;
    }

    static Logger log;

    public static void usage() {
        System.out.println("AvailTool [-sstoxml | -xmltoss | -dumpss] -s sheet -f filename [-o outputfile]");
    }

    public static void main(String[] args) throws Exception {
        String filename;
        String outfile;

        // System.out.println(MONGO.Description + " | " + MONGO.Description.ordinal() + " | " +
        //                    MONGO.Description.name());
        // System.exit(0);
        
        log = LogManager.getLogger(AvailsTool.class.getName());
        log.info("Initializing logger");

        Options options = new Options();
        options.addOption(Opts.s.name(), true, "specify sheet");
        options.addOption(Opts.f.name(), true, "specify file name");
        options.addOption(Opts.o.name(), true, "specify output file name");
        options.addOption(Opts.sstoxml.name(), false, "convert avails spreadsheet to XML");
        options.addOption(Opts.xmltoss.name(), false, "convert avails XML to a spreadsheet");
        options.addOption(Opts.dumpss.name(), false, "dump a spreadsheet file");
        options.addOption(Opts.strict.name(), false, "strict processing of entries");
	 
        CommandLineParser cli = new DefaultParser();
	 
        try {
            CommandLine cmd = cli.parse(options, args);
            boolean optToXML = cmd.hasOption(Opts.sstoxml.name());
            boolean optToSS = cmd.hasOption(Opts.xmltoss.name());
            boolean optDump = cmd.hasOption(Opts.dumpss.name());
            filename = cmd.getOptionValue(Opts.f.name());
            if (filename == null)
                throw new ParseException("file not specified");
            
            if (!(optToXML | optToSS | optDump))
                throw new ParseException("missing operation");
            if (optToXML) {
                if (optToSS | optDump)
                    throw new ParseException("more than one operation specified");
                boolean strict = cmd.hasOption(Opts.strict.name());
                String sheetName = cmd.getOptionValue(Opts.s.name());
                SS ss = new SS(filename, sheetName);
                ss.dump();
                ss.toXML(strict);
            } else if (optToSS) {
                if (optToXML | optDump)
                    throw new ParseException("more than one operation specified");
            } else { // dumpSS
                if (optToXML | optToSS)
                    throw new ParseException("more than one operation specified");
                SS.dump(filename);
            }
        }
        catch( ParseException exp ) {
            System.out.println( "bad command line: " + exp.getMessage() );
            usage();
            System.exit(-1);
        }
    }
}
