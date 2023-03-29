package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.EnumTipoPessoa;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;
import com.example.util.ReportUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private UsuarioRepository repository;

    // @Value("classpath:relatorios/usuario/usuarios.jrxml")
    // private Resource resourceUsuarios;
    
    // @Value("classpath:relatorios/usuario/usuario_enderecos.jrxml")
    // private Resource resourceEnderecos;

    // @Value("classpath:relatorios/usuario/usuario_telefone.jrxml")
    // private Resource resourceTelefones;

    public RelatorioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/save/usuarios")
    public ResponseEntity geraRelatorioLocal() {
        try {
            byte[] bytes = processaRelatorio();

            File reportXml = ReportUtils.getFileFromResource("relatorios/usuario/usuarios.jrxml");

            File pdfGerado = new File(reportXml.getAbsolutePath() + ".pdf");

            FileOutputStream fos = new FileOutputStream(pdfGerado);
            fos.write(bytes);
            fos.flush();
            fos.close();
            
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuarios.pdf")
    public ResponseEntity<ByteArrayResource> geraRelatorio() {
        try {
            byte[] bytes = processaRelatorio();

            ByteArrayResource resource = new ByteArrayResource(bytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] processaRelatorio() throws FileNotFoundException, JRException, IOException {
        List<Usuario> usuarios = repository.findAll();
        usuarios.forEach(u -> u.setTipoPessoa(EnumTipoPessoa.FISICA));

        ClassPathResource resourceUsuarios = new ClassPathResource("relatorios/usuario/usuarios.jrxml");
        ClassPathResource resourceEnderecos = new ClassPathResource("relatorios/usuario/usuario_enderecos.jrxml");
        ClassPathResource resourceTelefones = new ClassPathResource("relatorios/usuario/usuario_telefone.jrxml");

        JasperReport relatorio = JasperCompileManager.compileReport(resourceUsuarios.getInputStream());
        JasperReport subRelatorioEndereco = JasperCompileManager.compileReport(resourceEnderecos.getInputStream());
        JasperReport subRelatorioTelefone = JasperCompileManager.compileReport(resourceTelefones.getInputStream());
        
        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("subReportEnderecos", subRelatorioEndereco);
        parametros.put("subReportTelefones", subRelatorioTelefone);

        JasperPrint fillReport = JasperFillManager.fillReport(relatorio, parametros, new JRBeanCollectionDataSource(usuarios));
        byte[] exportReportToPdf = JasperExportManager.exportReportToPdf(fillReport);

        return exportReportToPdf;
    }

}