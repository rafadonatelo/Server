package com.server.scanner;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @author Rafael Gouveia da Silva
 *
 */
public class Host {
	private String so;

	private Integer processors;

	private List<String> listProcessors;

	private double cpuLoad;

	private long memoriaTotal;

	private long memoriaLivre;

	private String hdSerial;

	private String cpuSerial;

	private String motherBoardSerial;

	private String arquitetura;

	// Rede
	private String hostAdress;

	private String hostName;

	private Enumeration<NetworkInterface> netInterfaces;

	private File[] roots;

	public Host() {

	}

	public String getSo() {
		return so;
	}

	public void setSo(String so) {
		this.so = so;
	}

	public Integer getProcessors() {
		return processors;
	}

	public void setProcessors(Integer processors) {
		this.processors = processors;
	}

	public List<String> getListProcessors() {
		return listProcessors;
	}

	public void setListProcessors(List<String> listProcessors) {
		this.listProcessors = listProcessors;
	}

	public long getMemoriaTotal() {
		return memoriaTotal;
	}

	public void setMemoriaTotal(long memoriaTotal) {
		this.memoriaTotal = memoriaTotal;
	}

	public long getMemoriaLivre() {
		return memoriaLivre;
	}

	public void setMemoriaLivre(long memoriaLivre) {
		this.memoriaLivre = memoriaLivre;
	}

	public String getHdSerial() {
		return hdSerial;
	}

	public void setHdSerial(String hdSerial) {
		this.hdSerial = hdSerial;
	}

	public String getCpuSerial() {
		return cpuSerial;
	}

	public void setCpuSerial(String cpuSerial) {
		this.cpuSerial = cpuSerial;
	}

	public String getMotherBoardSerial() {
		return motherBoardSerial;
	}

	public void setMotherBoardSerial(String motherBoardSerial) {
		this.motherBoardSerial = motherBoardSerial;
	}

	public String getArquitetura() {
		return arquitetura;
	}

	public void setArquitetura(String arquitetura) {
		this.arquitetura = arquitetura;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	public String getHostAdress() {
		return hostAdress;
	}

	public void setHostAdress(String hostAdress) {
		this.hostAdress = hostAdress;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Enumeration<NetworkInterface> getNetInterfaces() {
		return netInterfaces;
	}

	public void setNetInterfaces(Enumeration<NetworkInterface> netInterfaces) {
		this.netInterfaces = netInterfaces;
	}

	public File[] getRoots() {
		return roots;
	}

	public void setRoots(File[] roots) {
		this.roots = roots;
	}

}
