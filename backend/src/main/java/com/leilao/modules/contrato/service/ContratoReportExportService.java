package com.leilao.modules.contrato.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.leilao.modules.contrato.dto.ContratoVencendoDto;
import com.leilao.modules.contrato.dto.ContratoVencendoRelatorioDto;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service para exportação de relatórios de contratos vencendo
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContratoReportExportService {

    private final MessageSourceAccessor messageSourceAccessor;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Exporta relatório de contratos vencendo em formato CSV
     */
    public ResponseEntity<byte[]> exportarCSV(ContratoVencendoRelatorioDto relatorio) {
        try {
            log.info("Iniciando exportação CSV de relatório de contratos vencendo - {} contratos", 
                    relatorio.getContratos().size());

            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);

            // Cabeçalho
            String[] header = {
                "ID do Contrato",
                "Vendedor",
                "Empresa",
                "Categoria",
                "Data de Vencimento",
                "Dias Restantes",
                "Status",
                "Urgência",
                "Taxa de Comissão (%)",
                "Notificado",
                "Data de Criação"
            };
            csvWriter.writeNext(header);

            // Dados
            for (ContratoVencendoDto contrato : relatorio.getContratos()) {
                String[] row = {
                    contrato.getId(),
                    contrato.getVendedorNome() != null ? contrato.getVendedorNome() : "",
                    contrato.getVendedorEmpresa() != null ? contrato.getVendedorEmpresa() : "",
                    contrato.getCategoria() != null ? contrato.getCategoria() : "Geral",
                    contrato.getValidTo() != null ? contrato.getValidTo().format(DATE_FORMATTER) : "",
                    contrato.getDiasRestantes() != null ? contrato.getDiasRestantes().toString() : "0",
                    contrato.getStatus() != null ? contrato.getStatus() : "",
                    contrato.getUrgencia() != null ? contrato.getUrgencia().name() : "",
                    contrato.getTaxaComissao() != null ? 
                        String.format("%.2f", contrato.getTaxaComissao().multiply(java.math.BigDecimal.valueOf(100))) : "0.00",
                    contrato.getNotificado() != null && contrato.getNotificado() ? "Sim" : "Não",
                    contrato.getCreatedAt() != null ? contrato.getCreatedAt().format(DATE_FORMATTER) : ""
                };
                csvWriter.writeNext(row);
            }

            // Resumo
            csvWriter.writeNext(new String[]{}); // Linha vazia
            csvWriter.writeNext(new String[]{"RESUMO"});
            csvWriter.writeNext(new String[]{"Total de Contratos", relatorio.getResumo().getTotal().toString()});
            csvWriter.writeNext(new String[]{"Urgência Alta", relatorio.getResumo().getUrgenciaAlta().toString()});
            csvWriter.writeNext(new String[]{"Urgência Média", relatorio.getResumo().getUrgenciaMedia().toString()});
            csvWriter.writeNext(new String[]{"Urgência Baixa", relatorio.getResumo().getUrgenciaBaixa().toString()});
            csvWriter.writeNext(new String[]{"Notificados", relatorio.getResumo().getNotificados().toString()});
            csvWriter.writeNext(new String[]{"Pendentes de Notificação", relatorio.getResumo().getPendentesNotificacao().toString()});

            csvWriter.close();

            byte[] csvBytes = stringWriter.toString().getBytes("UTF-8");
            
            String fileName = String.format("contratos_vencendo_%s.csv", 
                    LocalDateTime.now().format(FILE_DATE_FORMATTER));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", fileName);
            headers.add("Content-Length", String.valueOf(csvBytes.length));

            log.info("Exportação CSV concluída - arquivo: {}, tamanho: {} bytes", fileName, csvBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvBytes);

        } catch (Exception e) {
            log.error("Erro ao exportar relatório CSV: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar relatório CSV", e);
        }
    }

    /**
     * Exporta relatório de contratos vencendo em formato PDF
     */
    public ResponseEntity<byte[]> exportarPDF(ContratoVencendoRelatorioDto relatorio) {
        try {
            log.info("Iniciando exportação PDF de relatório de contratos vencendo - {} contratos", 
                    relatorio.getContratos().size());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Título
            Paragraph title = new Paragraph("Relatório de Contratos Vencendo")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Data de geração
            Paragraph dateGenerated = new Paragraph(
                    String.format("Gerado em: %s", LocalDateTime.now().format(DATE_FORMATTER)))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(dateGenerated);

            // Resumo
            document.add(new Paragraph("\nResumo Executivo").setFontSize(14).setBold());
            
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 1}));
            summaryTable.setWidth(UnitValue.createPercentValue(100));
            
            addSummaryRow(summaryTable, "Total de Contratos:", relatorio.getResumo().getTotal().toString());
            addSummaryRow(summaryTable, "Urgência Alta (≤ 7 dias):", relatorio.getResumo().getUrgenciaAlta().toString());
            addSummaryRow(summaryTable, "Urgência Média (8-15 dias):", relatorio.getResumo().getUrgenciaMedia().toString());
            addSummaryRow(summaryTable, "Urgência Baixa (16-30 dias):", relatorio.getResumo().getUrgenciaBaixa().toString());
            addSummaryRow(summaryTable, "Já Notificados:", relatorio.getResumo().getNotificados().toString());
            addSummaryRow(summaryTable, "Pendentes de Notificação:", relatorio.getResumo().getPendentesNotificacao().toString());
            
            document.add(summaryTable);

            // Filtros aplicados
            if (relatorio.getFiltros() != null) {
                document.add(new Paragraph("\nFiltros Aplicados").setFontSize(14).setBold());
                
                Table filtersTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}));
                filtersTable.setWidth(UnitValue.createPercentValue(100));
                
                addSummaryRow(filtersTable, "Período (dias):", relatorio.getFiltros().getDias().toString());
                addSummaryRow(filtersTable, "Incluir Notificados:", relatorio.getFiltros().getIncluirNotificados() ? "Sim" : "Não");
                
                if (relatorio.getFiltros().getVendedorId() != null) {
                    addSummaryRow(filtersTable, "Vendedor:", relatorio.getFiltros().getVendedorId());
                }
                
                if (relatorio.getFiltros().getCategoria() != null) {
                    addSummaryRow(filtersTable, "Categoria:", relatorio.getFiltros().getCategoria());
                }
                
                if (relatorio.getFiltros().getUrgencia() != null) {
                    addSummaryRow(filtersTable, "Urgência:", relatorio.getFiltros().getUrgencia().name());
                }
                
                document.add(filtersTable);
            }

            // Detalhes dos contratos
            if (!relatorio.getContratos().isEmpty()) {
                document.add(new Paragraph("\nDetalhes dos Contratos").setFontSize(14).setBold());
                
                Table contractsTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1.5f, 1, 1, 1}));
                contractsTable.setWidth(UnitValue.createPercentValue(100));
                
                // Cabeçalho da tabela
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Vendedor").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Empresa").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Categoria").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Vencimento").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Dias").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Urgência").setBold()));
                contractsTable.addHeaderCell(new Cell().add(new Paragraph("Taxa %").setBold()));
                
                // Dados dos contratos
                for (ContratoVencendoDto contrato : relatorio.getContratos()) {
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getVendedorNome() != null ? contrato.getVendedorNome() : "")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getVendedorEmpresa() != null ? contrato.getVendedorEmpresa() : "")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getCategoria() != null ? contrato.getCategoria() : "Geral")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getValidTo() != null ? contrato.getValidTo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getDiasRestantes() != null ? contrato.getDiasRestantes().toString() : "0")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getUrgencia() != null ? getUrgenciaText(contrato.getUrgencia()) : "")));
                    contractsTable.addCell(new Cell().add(new Paragraph(
                            contrato.getTaxaComissao() != null ? 
                                String.format("%.2f", contrato.getTaxaComissao().multiply(java.math.BigDecimal.valueOf(100))) : "0.00")));
                }
                
                document.add(contractsTable);
            }

            // Rodapé
            document.add(new Paragraph(String.format("\nRelatório gerado automaticamente pelo Sistema de Leilão em %s", 
                    LocalDateTime.now().format(DATE_FORMATTER)))
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            
            String fileName = String.format("contratos_vencendo_%s.pdf", 
                    LocalDateTime.now().format(FILE_DATE_FORMATTER));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.add("Content-Length", String.valueOf(pdfBytes.length));

            log.info("Exportação PDF concluída - arquivo: {}, tamanho: {} bytes", fileName, pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Erro ao exportar relatório PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }
    }

    /**
     * Adiciona uma linha ao resumo
     */
    private void addSummaryRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label)));
        table.addCell(new Cell().add(new Paragraph(value)));
    }

    /**
     * Converte enum de urgência para texto amigável
     */
    private String getUrgenciaText(ContratoVencendoDto.UrgenciaEnum urgencia) {
        return switch (urgencia) {
            case ALTA -> "Alta";
            case MEDIA -> "Média";
            case BAIXA -> "Baixa";
        };
    }
}