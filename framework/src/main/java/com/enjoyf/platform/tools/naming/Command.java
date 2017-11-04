package com.enjoyf.platform.tools.naming;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents a command.
 */
class Command {
    private static HashMap map = new HashMap();

    private String name;
    private String help;
    private Processor processor;

    static Command HELP = new Command("help",
            "List out all commands.",
            new BaseProcessor.Help());

    static Command LIST = new Command("list",
            "list [<serviceType>] [<serviceName>]\n"
                    + "\tList out all services, both <serviceType> and <serviceName>\n"
                    + "\tcan be a prefix",
            new BaseProcessor.ListCmd());

    static Command LISTALL = new Command("listall",
            "listall\n"
                    + "\tList all services including their registration info\n",
            new BaseProcessor.ListAllCmd());

    static Command REFRESH = new Command("refresh",
            "Refresh the cache of services from the NS.\n"
                    + "\tThis happens initially on demand.\n"
                    + "\tAfterwards, you need to issue this command to"
                    + "refresh the internal cache",
            new BaseProcessor.Refresh());

    static Command QUIT = new Command("quit",
            "Exit the program.",
            new BaseProcessor.Quit());


    private Command(String name, String help, Processor processor) {
        this.name = name;
        this.help = help;
        this.processor = processor;

        map.put(name, this);
    }

    static Collection getAll() {
        //GL.lh("Command.getAll()");
        return map.values();
    }

    static Command getByName(String cmd) {
        //--
        // First look for the exact cmd name.
        //--
        for (Iterator itr = map.values().iterator(); itr.hasNext(); ) {
            Command c = (Command) itr.next();
            if (cmd.equals(c.getName()))
                return c;
        }

        //--
        // If no exact match, look for a partial match.
        //--
        for (Iterator itr = map.values().iterator(); itr.hasNext(); ) {
            Command c = (Command) itr.next();
            if (cmd.startsWith(c.getName()))
                return c;
        }
        return null;
    }

    Processor getProcessor() {
        return processor;
    }

    String getName() {
        return name;
    }

    /**
     * Will match starting chars.
     */
    boolean isMatch(String cmd) {
        return cmd.startsWith(name);
    }

    boolean isExactMatch(String cmd) {
        return cmd.equalsIgnoreCase(cmd);
    }

    String getHelp() {
        return help;
    }
}
