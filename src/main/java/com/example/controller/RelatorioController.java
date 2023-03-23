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

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioController {

    private UsuarioRepository repository;

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
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] processaRelatorio() throws FileNotFoundException, JRException, IOException {
        List<Usuario> usuarios = repository.findAll();
        usuarios.forEach(u -> u.setTipoPessoa(EnumTipoPessoa.FISICA));

        // caso seja necessário, pode compilar os jrxml
        compilaRelatorios();
        File jasper = ReportUtils.getFileFromResource("relatorios/usuario/usuarios.jasper");
        InputStream fis = (InputStream) new FileInputStream(jasper);

        String pathSubRelatorio = jasper.getParent() + "\\";

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("SUBREPORT_DIR", pathSubRelatorio);

        byte[] bytes = ReportUtils.createPDFReport(fis, parametros, usuarios);
        return bytes;
    }

    private void compilaRelatorios() throws FileNotFoundException, JRException {
        File reportXml = ReportUtils.getFileFromResource("relatorios/usuario/usuarios.jrxml");
        File subReportEnderecoXml = ReportUtils.getFileFromResource("relatorios/usuario/usuario_enderecos.jrxml");
        File subReportTelefoneXml = ReportUtils.getFileFromResource("relatorios/usuario/usuario_telefone.jrxml");

        JasperCompileManager.compileReportToFile(reportXml.getAbsolutePath(),
                reportXml.getAbsolutePath().replace(".jrxml", ".jasper"));
        JasperCompileManager.compileReportToFile(subReportEnderecoXml.getAbsolutePath(),
                subReportEnderecoXml.getAbsolutePath().replace(".jrxml", ".jasper"));
        JasperCompileManager.compileReportToFile(subReportTelefoneXml.getAbsolutePath(),
                subReportTelefoneXml.getAbsolutePath().replace(".jrxml", ".jasper"));
    }
}