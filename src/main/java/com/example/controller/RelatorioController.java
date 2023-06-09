package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

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

    public RelatorioController(UsuarioRepository repository) {
        this.repository = repository;
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

        // Obter JasperReport compilando o .jrxml
        // ClassPathResource resourceUsuarios = new ClassPathResource("relatorios/usuario/usuarios.jrxml");
        // ClassPathResource resourceEnderecos = new ClassPathResource("relatorios/usuario/usuario_enderecos.jrxml");
        // ClassPathResource resourceTelefones = new ClassPathResource("relatorios/usuario/usuario_telefone.jrxml");
        // JasperReport relatorio = JasperCompileManager.compileReport(resourceUsuarios.getInputStream());
        // JasperReport subRelatorioEndereco = JasperCompileManager.compileReport(resourceEnderecos.getInputStream());
        // JasperReport subRelatorioTelefone = JasperCompileManager.compileReport(resourceTelefones.getInputStream());
        
        // Obter JasperReport carregando .jasper
        ClassPathResource resourceUsuarios = new ClassPathResource("relatorios/usuario/usuarios.jasper");
        ClassPathResource resourceEnderecos = new ClassPathResource("relatorios/usuario/usuario_enderecos.jasper");
        ClassPathResource resourceTelefones = new ClassPathResource("relatorios/usuario/usuario_telefone.jasper");
        JasperReport relatorio = (JasperReport) JRLoader.loadObject(resourceUsuarios.getInputStream());
        JasperReport subRelatorioEndereco = (JasperReport) JRLoader.loadObject(resourceEnderecos.getInputStream());
        JasperReport subRelatorioTelefone = (JasperReport) JRLoader.loadObject(resourceTelefones.getInputStream());

        // obter imagem local
        // ClassPathResource imagem = new ClassPathResource("relatorios/usuario/naruto.png");
        //InputStream fisImagem = imagem.getInputStream();

        // obter imagem de uma url
        URL url = new URL("https://4.bp.blogspot.com/-cgf6-E-rh5k/XGiXd-TBCdI/AAAAAAAAHuU/WE_N3mzjajkTMB7W9QWNSvQ1Ko-W23gcACLcBGAs/s320/2884_render_rendernarutosennin.png");
        InputStream fisImagem = url.openStream();

        Map<String, Object> parametros = new HashMap<String, Object>();
        parametros.put("imagem", fisImagem);
        parametros.put("subReportEnderecos", subRelatorioEndereco);
        parametros.put("subReportTelefones", subRelatorioTelefone);

        JasperPrint fillReport = JasperFillManager.fillReport(relatorio, parametros, new JRBeanCollectionDataSource(usuarios));
        byte[] exportReportToPdf = JasperExportManager.exportReportToPdf(fillReport);

        return exportReportToPdf;
    }
}