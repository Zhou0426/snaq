package cn.jagl.util;

import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.aspose.cells.License;

public class Excel2PDF {
	public static boolean getLicense() {
        boolean result = false;
        try {
        	Resource resource = new ClassPathResource("license.xml");
            InputStream is = resource.getInputStream();
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
