package org.exobel.routerkeygen;

import java.io.Serializable;

public class WifiNetwork implements Comparable<WifiNetwork>, Serializable{
	
	private static final long serialVersionUID = 1L;
	String ssid;
	String mac;
	String ssidSubpart;
	String encryption;
	boolean supported;
	boolean newThomson;
	int level;
	
	TYPE type;
	static enum TYPE {
		THOMSON , DLINK , DISCUS , VERIZON ,
		EIRCOM , PIRELLI , TELSEY , ALICE};
	public WifiNetwork(String ssid, String mac, int level , String enc){
		this.ssid = ssid;
		this.mac = mac.toUpperCase();
		this.level  = level;
		this.encryption = enc;
		if ( this.encryption.equals(""))
			this.encryption = "Open";
		this.newThomson = false;
		this.supported =  essidFilter();
	}
	
	public int getLevel(){
		return level;
	}
	
	public String getEssid(){
		return ssidSubpart;
	}
	
	public String getMacEnd(){
		return mac.substring(9, 11) + mac.substring(12, 14) + mac.substring(15, 17);
	}
	
	public String getMac(){
		return  mac.substring(0, 2) +  mac.substring(3, 5) +  
		 		mac.substring(6, 8) + mac.substring(9, 11) + 
				mac.substring(12, 14) + mac.substring(15, 17);
	}
	
	private boolean essidFilter() {
		if ( ( ssid.startsWith("BTHomeHub") && ssid.length() == 15 )  ||
		     ( ssid.startsWith("Thomson") && ssid.length() == 13 )    ||
		     ( ssid.startsWith("SpeedTouch") && ssid.length() == 16 ) ||
		     ( ssid.startsWith("O2Wireless") && ssid.length() == 16 ) ||
		     ( ssid.startsWith("Orange") && ssid.length() == 12 ) || 
		     ( ssid.startsWith("Infinitum") && ssid.length() == 15 )  ||
		     ( ssid.startsWith("BigPond") && ssid.length() == 13 )  ||
		     ( ssid.startsWith("Otenet") && ssid.length() == 12 ) ||
		     ( ssid.startsWith("BBox") && ssid.length() == 10 ) ||
		     ( ssid.startsWith("DMax") && ssid.length() == 10 ))
		{
			ssidSubpart = new String (ssid.substring(ssid.length()-6));
			if ( !mac.equals("") )
				if ( ssidSubpart.equals(getMacEnd()) )
					newThomson = true;
			type = TYPE.THOMSON;
			return true;
		}
		if (  ssid.startsWith("DLink-") && ssid.length() == 12 )
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-6));
			type = TYPE.DLINK;
			return true;
		}
		if ( ( ssid.startsWith("Discus-") && ssid.length() == 13 ) ||
			( ssid.startsWith("Discus--") && ssid.length() == 14 )	)
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-6));
			type = TYPE.DISCUS;
			return true;
		}
		if ( ( ssid.startsWith("Eircom") && ssid.length() >= 14 ) ||
		     ( ssid.startsWith("eircom") && ssid.length() >= 14 )	)
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-8));
			if ( mac.equals("") )
				calcEircomMAC();
			type = TYPE.EIRCOM;
			return true;
		}
		if ( ssid.length() == 5  && ( mac.contains("00:1F:90") || 
				mac.contains("00:18:01") || mac.contains("00:20:E0") ||
				mac.contains("00:0F:B3") || mac.contains("00:1E:A7") ||
				mac.contains("00:15:05") || mac.contains("00:24:7B") ) )
		{
			ssidSubpart = ssid;
			type = TYPE.VERIZON;
			return true;
		}
		if ( ( ssid.startsWith("FASTWEB-1-001CA2") && ssid.length() == 22 ) ||
		     ( ssid.startsWith("FASTWEB-1-001DBX") && ssid.length() == 22 )	)
			{
				ssidSubpart = new String ( ssid.substring(ssid.length()-12));
				if ( mac.equals("") )
					calcFastwebMAC();
				type = TYPE.PIRELLI;
				return true;
			}
		/* Still not working
		 * if ( ( ssid.startsWith("FASTWEB-1-00036F") && ssid.length() == 22 ) ||
			     ( ssid.startsWith("FASTWEB-1-002196") && ssid.length() == 22 )	)
				{
					ssidSubpart = new String ( ssid.substring(ssid.length()-12));
					if ( mac.equals("") )
						calcFastwebMAC();
					type = TYPE.TELSEY;
					return true;
				}*/
		if ( ( ssid.startsWith("Alice-96") || ssid.startsWith("Alice-93") || 
			   ssid.startsWith("Alice-56") || ssid.startsWith("Alice-55") || 
			   ssid.startsWith("Alice-54") || ssid.startsWith("Alice-48") ||
			   ssid.startsWith("Alice-46") || ssid.startsWith("Alice-37")  )
			   && ssid.length() == 14 )
		{
			ssidSubpart = new String ( ssid.substring(ssid.length()-8));
			type = TYPE.ALICE;
			return true;
		}
		return false;
	}
	
	public void calcFastwebMAC(){
		this.mac = ssidSubpart.substring(0,2) + ":" + ssidSubpart.substring(2,4) + ":" + 
				   ssidSubpart.substring(4,6) + ":" + ssidSubpart.substring(6,8) + ":" +
				   ssidSubpart.substring(8,10) + ":" + ssidSubpart.substring(10,12);
	}
	
	public void calcEircomMAC(){
		String end = Integer.toHexString( Integer.parseInt(ssidSubpart, 8) ^ 0x000fcc );
		this.mac = "00:0F:CC" +  ":" + end.substring(0,2)+ ":" +
					end.substring(2,4)+ ":" + end.substring(4,6);
	}

	public int compareTo(WifiNetwork another) {
		if ( another.level == this.level && this.ssid.equals(another.ssid) && this.mac.equals(another.mac) )
			return 0;
		if ( this.supported && !this.newThomson )
			return -1;
		return 1;
	}
	
	
}