package com.aerospike.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ResultCode;
import com.aerospike.client.ScanCallback;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;

public class SetDelete {
	private static Logger log = Logger.getLogger(SetDelete.class);
	static int count = 0;
	public static void main(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("h", "host", true, "Server hostname (default: localhost)");
		options.addOption("p", "port", true, "Server port (default: 3000)");
		options.addOption("n", "namespace", true, "Namespace (default: test)");
		options.addOption("s", "set", true, "Set to delete (default: test)");
		options.addOption("u", "usage", false, "Print usage.");

		CommandLineParser parser = new PosixParser();
		CommandLine cl = parser.parse(options, args, false);

		if (args.length == 0 || cl.hasOption("u")) {
			logUsage(options);
			return;
		}


		String host = cl.getOptionValue("h", "127.0.0.1");
		String portString = cl.getOptionValue("p", "3000");
		int port = Integer.parseInt(portString);
		String set = cl.getOptionValue("s", null);
		String namespace = cl.getOptionValue("n","test");

		log.info("Host: " + host);
		log.info("Port: " + port);
		log.info("Name space: " + namespace);
		log.info("Set: " + set);
		try {
			final AerospikeClient client = new AerospikeClient(host, port);


			ScanPolicy scanPolicy = new ScanPolicy();
			/*
			 * scan the entire Set using scannAll(). This will scan each node 
			 * in the cluster and return the record Digest to the call back object
			 */
			client.scanAll(scanPolicy, namespace, set, new ScanCallback() {

				public void scanCallback(Key key, Record record) throws AerospikeException {
					/*
					 * for each Digest returned, delete it using delete()
					 */
					client.delete(new WritePolicy(), key);
					count++;
					/*
					 * after 25,000 records delete, return print the count.
					 */
					if (count % 25000 == 0){
						log.info("Deleted "+ count);
					}
				}
			}, new String[] {});
			log.info("Deleted "+ count + " records from set " + set);
		} catch (AerospikeException e) {
			int resultCode = e.getResultCode();
			log.info(ResultCode.getResultString(resultCode));
			log.debug("Error details: ", e);
		}
	}


	private static void logUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		String syntax = SetDelete.class.getName() + " [<options>]";
		formatter.printHelp(pw, 100, syntax, "options:", options, 0, 2, null);
		log.info(sw.toString());
	}


}
