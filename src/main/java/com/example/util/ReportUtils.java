package com.example.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportUtils {

    public static File getFileFromResource(String filePath) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:" + filePath);
    }
    	
    public static byte[] createPDFReport( InputStream inputStream,
                                            Map<String, Object> parametros,
                                            Connection conexao) throws JRException, IOException {
 
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        JasperPrint jasperPrint = JasperFillManager.fillReport( inputStream, parametros, conexao );
        JasperExportManager.exportReportToPdfStream( jasperPrint, os);		

		return os.toByteArray();
    }

    public static byte[] createPDFReport( InputStream inputStream,
                                            Map<String, Object> parametros,
                                            List<?> dataSet) throws JRException, IOException {
 
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataSet);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
        JasperPrint jasperPrint = JasperFillManager.fillReport( inputStream, parametros, dataSource );
        JasperExportManager.exportReportToPdfStream( jasperPrint, os);		

		return os.toByteArray();
    }
}