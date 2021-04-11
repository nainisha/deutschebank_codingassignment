package com.deutsche.codepairing.tradedemo.customexception;

public class TradeLowerVersion extends Exception{
	
	public TradeLowerVersion(String versionId,int smallVersion, int highVersion) {
		System.out.println("Trade (" +versionId +") is being rejected as we receive a smallVersion "+smallVersion+ ", the system has high version available "+highVersion);
		
	}

}
