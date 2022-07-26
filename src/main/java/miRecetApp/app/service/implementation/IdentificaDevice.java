package miRecetApp.app.service.implementation;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Service
public class IdentificaDevice {
	
	private String deviceEnUso = "browser";
	
	public String getDevice(Device device) {
		
		if (device.isNormal()) {
			deviceEnUso = "browser";
	    } else if (device.isMobile()) {
	    	deviceEnUso = "mobile";
	    } else if (device.isTablet()) {
	    	deviceEnUso = "mobile";
	    }
		
		return deviceEnUso;
	} 
}
