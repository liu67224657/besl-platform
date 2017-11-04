package com.enjoyf.platform.tools.naming;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceFactory;
import com.enjoyf.platform.util.FiveProps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is a shell-like tool that connects to the naming service and
 * queries it. You fire it up, and enter commands on the console.
 * Type 'help' as the first command to see a list of available commands.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
            System.exit(1);
        }

        String propFile = args[0];
        if (!propFile.startsWith("/")) {
            propFile = "/props/env/" + propFile + "/def.properties";
        }

        //GL.lh("Using prop file: " + propFile );
        FiveProps props = new FiveProps(propFile);
        NamingService ns = NamingServiceFactory.instance().createFromProps(props);

        processCmd(ns);
    }

    private static void usage() {
        System.out.println("One args is required, the env prop file, or "
                + "the keyword 'dev', 'prealpha', 'prealpha2', 'alpha', 'beta', 'prod', 'prodb'");
    }

    private static void processCmd(NamingService ns) {
        State state = new State(ns);
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        String cmd;

        while (true) {
            System.out.print("> ");
            try {
                cmd = rdr.readLine();
            } catch (IOException ioe) {
                //GL.lh("IOException caught! " + ioe);
                continue;
            }

            doCmd(state, cmd);
        }
    }

    private static void doCmd(State state, String cmd) {
        Command command = Command.getByName(cmd);
        if (command == null) {
            System.out.println("Did not understand command, type 'help'");
            return;
        }

        String result = command.getProcessor().process(state, cmd);

        System.out.println(result);
    }
}
