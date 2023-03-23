package com.example.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Usuario;
import com.example.repository.CustomUsuarioRepository;
import com.example.repository.UsuarioRepository;
import com.example.util.ReportUtils;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RelatorioController {
    
    private UsuarioRepository repository;
    private CustomUsuarioRepository customRepository;

    public RelatorioController(
                UsuarioRepository repository,
                CustomUsuarioRepository customRepository ) {
        this.repository = repository;
        this.customRepository = customRepository;
    }

    @GetMapping("/relatorio.pdf")
    public ResponseEntity<ByteArrayResource> geraRelatorio() {
        try {
            List<Usuario> usuarios = repository.findAll();

            File reportXml = ReportUtils.getFileFromResource("relatorios/usuario/usuarios.jrxml");
            File subReportEnderecoXml = ReportUtils.getFileFromResource("relatorios/usuario/usuario_enderecos.jrxml");
            File subReportTelefoneXml = ReportUtils.getFileFromResource("relatorios/usuario/usuario_telefone.jrxml");

            JasperCompileManager.compileReportToFile(reportXml.getAbsolutePath(), reportXml.getAbsolutePath().replace(".jrxml", ".jasper"));
            JasperCompileManager.compileReportToFile(subReportEnderecoXml.getAbsolutePath(), subReportEnderecoXml.getAbsolutePath().replace(".jrxml", ".jasper"));
            JasperCompileManager.compileReportToFile(subReportTelefoneXml.getAbsolutePath(), subReportTelefoneXml.getAbsolutePath().replace(".jrxml", ".jasper"));

            File jasper = ReportUtils.getFileFromResource("relatorios/usuario/usuarios.jasper");
            InputStream fis = (InputStream) new FileInputStream(jasper);

            String pathSubRelatorio = reportXml.getParent() + "\\";
            System.out.println(pathSubRelatorio);

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("SUBREPORT_DIR", pathSubRelatorio);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usuarios);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            JasperPrint jasperPrint = JasperFillManager.fillReport( fis, parametros, dataSource );
            JasperExportManager.exportReportToPdfStream( jasperPrint, os);		

            byte[] bytes = os.toByteArray();

            ByteArrayResource resource = new ByteArrayResource(bytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
