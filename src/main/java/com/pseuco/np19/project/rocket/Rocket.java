package com.pseuco.np19.project.rocket;

import java.util.ArrayList;
import java.util.List;

import com.pseuco.np19.project.launcher.cli.CLI;
import com.pseuco.np19.project.launcher.cli.CLIException;
import com.pseuco.np19.project.launcher.cli.Unit;

public class Rocket {

	public static void main(String[] args) {
		List<Thread> threadList = new ArrayList<Thread>();
		try {
			List<Unit> units = CLI.parseArgs(args);
			if (units.isEmpty()) {
				CLI.printUsage(System.out);
			}
			// System.out.println("");
			// System.out.println("num units: " + units.size());
			for (int i = 0; i < units.size() - 1; i++) {
				Thread unitThread = new UnitThread(units.get(i));
				threadList.add(unitThread);
				// System.out.println("start unit thread");
				unitThread.start();
			}

			(new UnitThread(units.get(units.size() - 1))).run();

			for (Thread thread : threadList) {
				thread.join();
				// System.out.println("thread terminated");
			}
			System.exit(0);
		} catch (CLIException error) {
			System.err.println(error.getMessage());
			CLI.printUsage(System.err);
			System.exit(1);
		} catch (Throwable error) {
			// error.printStackTrace();
			System.exit(1);
		}
	}
}
