package com.secure.practice.utils.ca;


import java.util.Date;

public class SignedCertInfo {

	private String CN; //用户名称
	private String OU; //机构名称
	private String O; //组织名称
	private String L; //地市
	private String ST; //省份
	private String C; //国家

	private String keyStorePath;
	private String keyStorePass;
	private String issuerAlias;
	private String issuerAliasPass;
	private String subjectAlias;
	private String subjectAliasPass;
	private Date start;
	private int validity;
	private String subjectPath;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * 获取用户名称
	 * @return 用户名称
	 */
	public String getCN() {
		return CN;
	}

	/**
	 * 设定用户名称
	 * @param cN 用户名称
	 */
	public void setCN(String cN) {
		CN = cN;
	}

	/**
	 * 获取机构名称
	 * @return 机构名称
	 */
	public String getOU() {
		return OU;
	}

	/**
	 * 获取机构名称
	 * @param oU 机构名称
	 */
	public void setOU(String oU) {
		OU = oU;
	}

	/**
	 * 获取组织名称
	 * @return 组织名称
	 */
	public String getO() {
		return O;
	}

	/**
	 * 设置组织名称
	 * @param o 组织名称
	 */
	public void setO(String o) {
		O = o;
	}

	/**
	 * 获取地市
	 * @return 地市
	 */
	public String getL() {
		return L;
	}

	/**
	 * 设置地市
	 * @param l 地市
	 */
	public void setL(String l) {
		L = l;
	}

	/**
	 * 获取省份
	 * @return 省份
	 */
	public String getST() {
		return ST;
	}

	/**
	 * 设置省份
	 * @param sT 省份
	 */
	public void setST(String sT) {
		ST = sT;
	}

	/**
	 * 获取国家
	 * @return 国家
	 */
	public String getC() {
		return C;
	}

	/**
	 * 设置国家
	 * @param c 国家
	 */
	public void setC(String c) {
		C = c;
	}
	public String getKeyStorePath() {
		return keyStorePath;
	}
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}
	public String getKeyStorePass() {
		return keyStorePass;
	}
	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}
	public String getIssuerAlias() {
		return issuerAlias;
	}
	public void setIssuerAlias(String issuerAlias) {
		this.issuerAlias = issuerAlias;
	}
	public String getIssuerAliasPass() {
		return issuerAliasPass;
	}
	public void setIssuerAliasPass(String issuerAliasPass) {
		this.issuerAliasPass = issuerAliasPass;
	}
	public String getSubjectAlias() {
		return subjectAlias;
	}
	public void setSubjectAlias(String subjectAlias) {
		this.subjectAlias = subjectAlias;
	}
	public String getSubjectAliasPass() {
		return subjectAliasPass;
	}
	public void setSubjectAliasPass(String subjectAliasPass) {
		this.subjectAliasPass = subjectAliasPass;
	}
	public int getValidity() {
		return validity;
	}
	public void setValidity(int validity) {
		this.validity = validity;
	}
	public String getSubjectPath() {
		return subjectPath;
	}
	public void setSubjectPath(String subjectPath) {
		this.subjectPath = subjectPath;
	}
	@Override
	public String toString() {
		return "SignedCertInfo [CN=" + CN + ", OU=" + OU + ", O=" + O + ", L="
				+ L + ", ST=" + ST + ", C=" + C + ", keyStorePath="
				+ keyStorePath + ", keyStorePass=" + keyStorePass
				+ ", issuerAlias=" + issuerAlias + ", issuerAliasPass="
				+ issuerAliasPass + ", subjectAlias=" + subjectAlias
				+ ", subjectAliasPass=" + subjectAliasPass + ", validity="
				+ validity + ", subjectPath=" + subjectPath + "]";
	}
	

	
	
}
