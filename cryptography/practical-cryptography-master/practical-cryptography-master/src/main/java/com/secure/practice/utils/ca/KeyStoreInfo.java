package com.secure.practice.utils.ca;

import java.util.Date;

/**
 *
 */
public class KeyStoreInfo {

	private String alias;
	private String keyStorePass;
	private String certPass;
	private String CN;
	private String OU;
	private String O;
	private String L;
	private String ST;
	private String C;
	private Date start;
	private long validityDays;
	private String pathAndFileName;

	/**
	 * 别名,库密码,证书密码,CN,OU,O,L,ST,C,开始时间,有效期限(单位:天),存储路径
	 * @param alias 别名
	 * @param keyStorePass 库密码
	 * @param certPass 证书密码
	 * @param cN 用户名称
	 * @param oU 机构名称
	 * @param o 组织名称
	 * @param l 地市
	 * @param sT 省份
	 * @param c 国家
	 * @param start 开始日期
	 * @param validityDays 有效时间
	 * @param pathAndFileName 路径和文件名
	 */
	public KeyStoreInfo(String alias, String keyStorePass, String certPass,
			String cN, String oU, String o, String l, String sT, String c,
			Date start, long validityDays, String pathAndFileName) {
		this.alias = alias;
		this.keyStorePass = keyStorePass;
		this.certPass = certPass;
		CN = cN;
		OU = oU;
		O = o;
		L = l;
		ST = sT;
		C = c;
		this.start = start;
		this.validityDays = validityDays;
		this.pathAndFileName = pathAndFileName;
	}

	public KeyStoreInfo() {
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}

	public String getCertPass() {
		return certPass;
	}

	public void setCertPass(String certPass) {
		this.certPass = certPass;
	}

	public String getCN() {
		return CN;
	}

	public void setCN(String cN) {
		CN = cN;
	}

	public String getOU() {
		return OU;
	}

	public void setOU(String oU) {
		OU = oU;
	}

	public String getO() {
		return O;
	}

	public void setO(String o) {
		O = o;
	}

	public String getL() {
		return L;
	}

	public void setL(String l) {
		L = l;
	}

	public String getST() {
		return ST;
	}

	public void setST(String sT) {
		ST = sT;
	}

	public String getC() {
		return C;
	}

	public void setC(String c) {
		C = c;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public long getValidityDays() {
		return validityDays;
	}

	public void setValidityDays(long validityDays) {
		this.validityDays = validityDays;
	}

	public String getPathAndFileName() {
		return pathAndFileName;
	}

	public void setPathAndFileName(String pathAndFileName) {
		this.pathAndFileName = pathAndFileName;
	}

	@Override
	public String toString() {
		return "X509CertInfo [alias=" + alias + ", keyStorePass="
				+ keyStorePass + ", certPass=" + certPass + ", CN=" + CN
				+ ", OU=" + OU + ", O=" + O + ", L=" + L + ", ST=" + ST
				+ ", C=" + C + ", start=" + start + ", validityDays="
				+ validityDays + ", pathAndFileName=" + pathAndFileName + "]";
	}

}
