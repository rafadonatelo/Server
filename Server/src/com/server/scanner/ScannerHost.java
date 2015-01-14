package com.server.scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;

/**
 * 
 * @author Rafael Gouveia da Silva
 *
 */
public class ScannerHost {
	static OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

	static SystemInfo si = new SystemInfo();

	private final static String getHDSerial() throws IOException {
		String os = System.getProperty("os.name");
		try {
			if (os.startsWith("Windows")) {
				return getHDSerialWindows("C");
			} else if (os.startsWith("Linux")) {
				return getHDSerialLinux();
			} else {
				throw new IOException("unknown operating system: " + os);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(ex.getMessage());
		}
	}

	private final static String getCPUSerial() throws IOException {
		String os = System.getProperty("os.name");

		try {
			if (os.startsWith("Windows")) {
				return getCPUSerialWindows();
			} else if (os.startsWith("Linux")) {
				return getCPUSerialLinux();
			} else {
				throw new IOException("unknown operating system: " + os);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(ex.getMessage());
		}
	}

	private final static String getMotherboardSerial() throws IOException {
		String os = System.getProperty("os.name");

		try {
			if (os.startsWith("Windows")) {
				return getMotherboardSerialWindows();
			} else if (os.startsWith("Linux")) {
				return getMotherboardSerialLinux();
			} else {
				throw new IOException("unknown operating system: " + os);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException(ex.getMessage());
		}
	}

	// Implementacoes

	/*
	 * Captura serial de placa mae no WINDOWS, atraves da execucao de script visual basic
	 */
	public static String getMotherboardSerialWindows() {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n" + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
					+ "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n" + "    exit for  ' do the first cpu only! \n" + "Next \n";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	}

	/*
	 * Captura serial de placa mae em sistemas LINUX, atraves da execucao de comandos em shell.
	 */
	public static String getMotherboardSerialLinux() {
		String result = "";
		try {
			String[] args = { "bash", "-c", "lshw -class bus | grep serial" };
			Process p = Runtime.getRuntime().exec(args);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();

		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_DISK_ID";

		}

		return filtraString(result, "serial: ");

	}

	/*
	 * Captura serial de HD no WINDOWS, atraves da execucao de script visual basic
	 */
	public static String getHDSerialWindows(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n" + "Set colDrives = objFSO.Drives\n" + "Set objDrive = colDrives.item(\"" + drive + "\")\n" +
					"Wscript.Echo objDrive.SerialNumber";
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_DISK_ID";
		}
		return result.trim();
	}

	/*
	 * Captura serial de HD em sistemas Linux, atraves da execucao de comandos em shell.
	 */
	public static String getHDSerialLinux() {
		String result = "";
		try {
			String[] args = { "bash", "-c", "lshw -class disk | grep serial" };
			Process p = Runtime.getRuntime().exec(args);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();

		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_DISK_ID";

		}

		return filtraString(result, "serial: ");

	}

	/*
	 * Captura serial da CPU no WINDOWS, atraves da execucao de script visual basic
	 */
	public static String getCPUSerialWindows() {
		String result = "";
		try {
			File file = File.createTempFile("tmp", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "On Error Resume Next \r\n\r\n" + "strComputer = \".\"  \r\n" + "Set objWMIService = GetObject(\"winmgmts:\" _ \r\n"
					+ "    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\n" + "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n "
					+ "For Each objItem in colItems\r\n " + "    Wscript.Echo objItem.ProcessorId  \r\n " + "    exit for  ' do the first cpu only! \r\n" + "Next                    ";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_CPU_ID";
		}
		return result.trim();
	}

	/*
	 * Captura serial de CPU em sistemas Linux, atraves da execucao de comandos em shell.
	 */
	public static String getCPUSerialLinux() {
		String result = "";
		try {
			String[] args = { "bash", "-c", "lshw -class processor | grep serial" };
			Process p = Runtime.getRuntime().exec(args);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();

		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_DISK_ID";

		}

		return filtraString(result, "serial: ");
	}

	public static void obterPropriedadesDoSO() {
		Enumeration<?> liste = System.getProperties().propertyNames();
		String cle;
		while (liste.hasMoreElements()) {
			cle = (String) liste.nextElement();
			System.out.println(cle + " = " + System.getProperty(cle));
		}
	}

	public static String filtraString(String nome, String delimitador) {
		return nome.split(delimitador)[1];
	}

	public static void getInfosInterfaces(Host host) throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		host.setNetInterfaces(nets);
	}

	public static void displayInterfaceInformation(NetworkInterface netint) {
		try{
			System.out.println("Display name: " + netint.getDisplayName());
			System.out.println("Name: " + netint.getName());
			// byte[] mac = netint.getHardwareAddress(); // a byte array containing the address (usually MAC) or null
	
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				System.out.println("InetAddress: " + inetAddress);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void infoOSHI(Host host) throws Exception {
		// So
		host.setSo(si.getOperatingSystem().toString());
		// Cpu
		HardwareAbstractionLayer hal = si.getHardware();
		host.setProcessors(hal.getProcessors().length);
		List<String> processors = new ArrayList<String>();
		for (oshi.hardware.Processor cpu : hal.getProcessors()) {
			processors.add(cpu.toString());
		}
		host.setListProcessors(processors);
		// hardware: memory
		host.setMemoriaLivre(hal.getMemory().getAvailable());
		host.setMemoriaTotal(hal.getMemory().getTotal());

	}

	public static double cpuLoad() {
		double cpuLoad = -1;
		try {
			Class<?> beanClass = Thread.currentThread().getContextClassLoader().loadClass("com.sun.management.OperatingSystemMXBean");
			Method method = beanClass.getMethod("getSystemCpuLoad");
			cpuLoad = (Double) method.invoke(osBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cpuLoad;
	}
	
	public static void networkInformation(Host host) {
		InetAddress Ip;
		try {
			Ip = InetAddress.getLocalHost();
			host.setHostAdress(Ip.getHostAddress());
			host.setHostName(Ip.getCanonicalHostName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Carrega todas as informações em um objeto Host
	 * @return
	 */
	public static Host loadAllInformation() {
		Host host = new Host();
		try {
			infoOSHI(host);
			// Arch
			host.setArquitetura(osBean.getArch());
			// CPU LOAD
			host.setCpuLoad(cpuLoad());
			// Serial
			host.setHdSerial(getHDSerial());
			host.setCpuSerial(getCPUSerial());
			host.setMotherBoardSerial(getMotherboardSerial());
			// Network
			networkInformation(host);
			getInfosInterfaces(host);
			/* File System Roots */
			host.setRoots(File.listRoots());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return host;
	}
	
	/**
	 * Imprime as informações de um host no console
	 * @param host
	 */
	public static void printAllInformationHost(Host host) {
		// S.O
		System.out.println("S.O : " + host.getSo());
		// Arch
		System.out.println("Arquitetura : " + host.getArquitetura());
		// CPU
		System.out.println("CPU(s) :" + host.getProcessors());
		for (String s : host.getListProcessors()) {
			System.out.println("Vendor : " + s);
		}
		// CPU LOAD
		System.out.println("CPU load : "+host.getCpuLoad());
		// Memória
		System.out.println("Memória : " + FormatUtil.formatBytes(host.getMemoriaLivre()) + "/" + FormatUtil.formatBytes(host.getMemoriaTotal()));
		// Serial
		System.out.println("Serial do HD : " + host.getHdSerial());
		System.out.println("Serial da CPU : " + host.getCpuSerial());
		System.out.println("Serial da Placa Mae : " + host.getMotherBoardSerial());
		// Network
		System.out.println("IP : " + host.getHostAdress());
		System.out.println("HostName : " + host.getHostName());
		System.out.println("Interfaces e endereços de rede :");
		for (NetworkInterface netint : Collections.list(host.getNetInterfaces()))
			displayInterfaceInformation(netint);
		/* Percorrer File systems */
		for (File root : host.getRoots()) {
			System.out.println("File system root : " + root.getAbsolutePath());
			System.out.println("Total space (bytes) : " + FormatUtil.formatBytes(root.getTotalSpace()));
			System.out.println("Free space (bytes) : " + FormatUtil.formatBytes(root.getFreeSpace()));
			System.out.println("Usable space (bytes) : " + FormatUtil.formatBytes(root.getUsableSpace()));
		}

	}

	public static void main(String[] args) {
		try {
			printAllInformationHost(loadAllInformation());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
