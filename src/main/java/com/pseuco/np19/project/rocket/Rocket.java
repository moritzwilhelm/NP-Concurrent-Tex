package com.pseuco.np19.project.rocket;

import java.util.ArrayList;
import java.util.List;

import com.pseuco.np19.project.launcher.cli.CLI;
import com.pseuco.np19.project.launcher.cli.CLIException;
import com.pseuco.np19.project.launcher.cli.Unit;

/**
 * The concurrent implementation of our typesetting system.
 */

public class Rocket {

	public static void main(String[] args) {

		List<Thread> threadList = new ArrayList<Thread>();

		try {
			List<Unit> units = CLI.parseArgs(args);
			if (units.isEmpty()) {
				CLI.printUsage(System.out);
			}

			// Start processing for each unit concurrently
			for (Unit unit : units) {
				Thread unitThread = new UnitThread(unit);
				threadList.add(unitThread);
				unitThread.start();
			}

			// wait until all UnitThreads have terminated
			for (Thread thread : threadList) {
				thread.join();
			}

			System.exit(0);
		} catch (CLIException error) {
			System.err.println(error.getMessage());
			CLI.printUsage(System.err);
			System.exit(1);
		} catch (Throwable error) {
			error.printStackTrace();
			System.exit(1);
		}
	}
}
