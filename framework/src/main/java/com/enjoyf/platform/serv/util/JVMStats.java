/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.thread.ThreadLister;

public class JVMStats {

	private static final Logger logger = LoggerFactory.getLogger(JVMStats.class);
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss:SSS");
	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance();
	private static final String LOG_STAMP = "-STATS-";

	private long memoryGrowth = 0;
	private long JVMSnapshot = 0;
	private long totalThreads = 0;
	private long totalMemory = 0;
	private long freeMemory = 0;
	private Thread[] threads;

	public JVMStats() {
		// nothing to do for now
	}

	protected String p_formatTime(long time) {
		synchronized (timeFormat) {
			return timeFormat.format(new Date(time));
		}
	}

	protected String p_formatNumber(long number) {
		synchronized (numberFormat) {
			return numberFormat.format(number);
		}
	}

	public synchronized void takeJVMSnapshot() {
		long jvmSnapshot = 0, totalThreads = 0, totalMemory = 0, freeMemory = 0;

		// take a new snapshot
		jvmSnapshot = System.currentTimeMillis();

		Thread[] threads = ThreadLister.getCurrentThreads();
		if (threads != null) {
			totalThreads = threads.length;
		}
		totalMemory = Runtime.getRuntime().totalMemory();
		freeMemory = Runtime.getRuntime().freeMemory();

		// calculate memory growth based on the last two snapshots
		memoryGrowth = p_calcMemoryGrowth(JVMSnapshot, this.totalMemory,
				jvmSnapshot, totalMemory);

		// save the new snapshot
		this.threads = threads;
		JVMSnapshot = jvmSnapshot;
		this.totalThreads = totalThreads;
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
	}

	/* simple getters */
	public synchronized long getMemoryGrowth() {
		return memoryGrowth;
	}

	public synchronized long getJVMSnapshot() {
		return JVMSnapshot;
	}

	public synchronized long getTotalThreads() {
		return totalThreads;
	}

	public synchronized long getTotalMemory() {
		return totalMemory;
	}

	public synchronized int getHeapInUsePercentage() {
		if (totalMemory == 0) {
			return 0;
		}

		return (int) (100.0 * (float) (totalMemory - freeMemory) / (float) totalMemory);
	}

	public Thread[] getThreads() {
		return threads;
	}

	public synchronized long getFreeMemory() {
		return freeMemory;
	}

	public void logAllStats() {

		long before = 0, after = 0;

		before = System.currentTimeMillis();

		takeJVMSnapshot();

		logger.info(LOG_STAMP + "      JVM: " + p_formatNumber(getFreeMemory())
				+ " free memory, " + p_formatNumber(getTotalMemory())
				+ " total memory, " + p_formatNumber(getTotalThreads())
				+ " threads");

		after = System.currentTimeMillis();

		logger.info(LOG_STAMP + "    Stats: " + p_formatNumber(after - before)
				+ " msec took to generate");

	}

	/**
	 * Calculates percentage of memory growth per second.
	 */
	public long p_calcMemoryGrowth(long oldSnapshot, long oldMemory,
			long newSnapshot, long newMemory) {

		// make sure that all data is valid and
		// different snapshots are being compared
		if (oldSnapshot == 0 || oldMemory == 0 || newSnapshot == 0
				|| newMemory == 0 || oldSnapshot == newSnapshot) {
			return 0;
		}

		long timeUnit = 1000;
		long timeDiff = newSnapshot - oldSnapshot;

		// make sure that memory growth isn't calulated
		// from snapshots taken too close to each other
		if (timeDiff < timeUnit) {
			return 0;
		}

		return 100 * timeUnit * (newMemory - oldMemory)
				/ (timeDiff * oldMemory);
	}
}
