package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Contract;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.ContractRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class PdfGenerationService {

    @Autowired
    private ContractRepository contractRepository;

    // Constantes de style
    private static final PDType1Font TITLE_FONT = PDType1Font.HELVETICA_BOLD;
    private static final PDType1Font HEADER_FONT = PDType1Font.HELVETICA_BOLD;
    private static final PDType1Font BODY_FONT = PDType1Font.HELVETICA;
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 15;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private PDPage page;


    public byte[] generateContractPdf(Long contractId, String borrowerName, double amount) throws IOException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
        return generateStyledContractPdf(contract);
    }

    private byte[] generateStyledContractPdf(Contract contract) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;

                // Titre principal
                yPosition = drawText(contentStream, "CONTRAT DE LOCATION", MARGIN, yPosition, TITLE_FONT, 16);
                yPosition -= LINE_HEIGHT * 2;

                // Section Informations
                yPosition = drawSection(contentStream, "INFORMATIONS DU CONTRAT", MARGIN, yPosition);
                yPosition = drawText(contentStream, "Numéro: " + contract.getId(), MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition = drawText(contentStream, "Locataire: " + contract.getBorrower().getName(), MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition = drawText(contentStream, "Montant total: " + contract.getCommande().getTotalPrice() + " DT",
                        MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition -= LINE_HEIGHT;

                // Section Dates
                yPosition = drawSection(contentStream, "DATES", MARGIN, yPosition);
                yPosition = drawText(contentStream, "Début: " + formatDate(contract.getStartDate()),
                        MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition = drawText(contentStream, "Fin: " + formatDate(contract.getEndDate()),
                        MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition -= LINE_HEIGHT;

                // Section Conditions
                yPosition = drawSection(contentStream, "CONDITIONS", MARGIN, yPosition);
                yPosition = drawWrappedText(contentStream, contract.getTerms(),
                        MARGIN + 10, yPosition, 500, BODY_FONT, 12);
                yPosition -= LINE_HEIGHT * 2;

                // Section Signatures
                yPosition = drawSection(contentStream, "SIGNATURES", MARGIN, yPosition);
                yPosition = drawText(contentStream, "Propriétaire: ___________________",
                        MARGIN + 10, yPosition, BODY_FONT, 12);
                yPosition = drawText(contentStream, "Locataire: ___________________",
                        MARGIN + 10, yPosition, BODY_FONT, 12);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Méthode drawText complète (6 paramètres)
    private float drawText(PDPageContentStream contentStream, String text,
                           float x, float y, PDType1Font font, int fontSize) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y - LINE_HEIGHT;
    }

    // Méthode pour les sections
    private float drawSection(PDPageContentStream contentStream, String title,
                              float x, float y) throws IOException {
        return drawText(contentStream, title, x, y, HEADER_FONT, 14);
    }

    // Méthode pour le texte avec retour à la ligne
    private float drawWrappedText(PDPageContentStream contentStream, String text,
                                  float x, float y, float maxWidth,
                                  PDType1Font font, int fontSize) throws IOException {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float currentY = y;

        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, currentY);

        for (String word : words) {
            if (line.length() + word.length() > maxWidth/5) {
                contentStream.showText(line.toString());
                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                currentY -= LINE_HEIGHT;
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }

        if (line.length() > 0) {
            contentStream.showText(line.toString());
        }
        contentStream.endText();

        return currentY - LINE_HEIGHT;
    }

    private String formatDate(java.util.Date date) {
        return Optional.ofNullable(date)
                .map(DATE_FORMAT::format)
                .orElse("Non spécifiée");
    }

//inovooice*--------------------
public byte[] generateInvoicePdf(Contract contract) throws IOException {
    try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Couleurs modernes
            Color accentColor = new Color(59, 89, 152);  // Bleu professionnel
            Color lightGray = new Color(240, 240, 240);

            float margin = 50;
            float yPosition = page.getMediaBox().getHeight() - margin;

            // 1. En-tête avec logo
            yPosition = drawInvoiceHeader(contentStream, contract, margin, yPosition, accentColor, page);
            yPosition -= 30;

            // 2. Informations client/fournisseur
            yPosition = drawInvoiceParties(contentStream, contract, margin, yPosition, lightGray, page);
            yPosition -= 20;

            // 3. Détails de la facture
            yPosition = drawInvoiceDetails(contentStream, contract, margin, yPosition, page);
            yPosition -= 30;

            // 4. Tableau des articles (un seul item)
            yPosition = drawInvoiceItemsTable(contentStream, contract, margin, yPosition, accentColor, lightGray, page);
            yPosition -= 20;

            // 5. Totaux et mentions légales
            drawInvoiceTotals(contentStream, contract, margin, yPosition, page);
            drawLegalMentions(contentStream, margin, 50, page);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        return outputStream.toByteArray();
    }
}

    private float drawInvoiceHeader(PDPageContentStream cs, Contract contract,
                                    float x, float y, Color color, PDPage page) throws IOException {
        // Logo et numéro de facture
        cs.setNonStrokingColor(color);
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText("FACTURE N°" + contract.getId());
        cs.endText();

        // Date et mentions
        cs.setNonStrokingColor(Color.BLACK);
        cs.setFont(PDType1Font.HELVETICA, 10);
        cs.beginText();
        cs.newLineAtOffset(page.getMediaBox().getWidth() - 150, y);
        cs.showText("Date: " + DATE_FORMAT.format(new Date()));
        cs.endText();

        return y - 40;
    }

    private float drawInvoiceParties(PDPageContentStream cs, Contract contract,
                                     float x, float y, Color bgColor, PDPage page) throws IOException {
        // Rectangle de fond
        cs.setNonStrokingColor(bgColor);
        cs.addRect(x, y-100, page.getMediaBox().getWidth() - 2*x, 100);
        cs.fill();

        // Titre section
        cs.setNonStrokingColor(Color.BLACK);
        drawText(cs, "FACTURÉ À", x+10, y-20, PDType1Font.HELVETICA_BOLD, 12);
        drawText(cs, "ÉMETTEUR", page.getMediaBox().getWidth() - x - 150, y-20, PDType1Font.HELVETICA_BOLD, 12);

        // Informations client
        User borrower = contract.getBorrower();
        drawText(cs, borrower.getName(), x+10, y-40, PDType1Font.HELVETICA, 10);
        drawText(cs, borrower.getEmail(), x+10, y-55, PDType1Font.HELVETICA, 10);
        drawText(cs, borrower.getAddress(), x+10, y-70, PDType1Font.HELVETICA, 10);
        drawText(cs, borrower.getPhone(), x+10, y-85, PDType1Font.HELVETICA, 10);

        // Informations fournisseur
        User owner = contract.getOwner();
        drawText(cs, owner.getName(), page.getMediaBox().getWidth() - x - 150, y-40, PDType1Font.HELVETICA, 10);
        drawText(cs, "contact@votresociete.com", page.getMediaBox().getWidth() - x - 150, y-55, PDType1Font.HELVETICA, 10);
        drawText(cs, "123 Rue des Entreprises", page.getMediaBox().getWidth() - x - 150, y-70, PDType1Font.HELVETICA, 10);
        drawText(cs, "+216 12 345 678", page.getMediaBox().getWidth() - x - 150, y-85, PDType1Font.HELVETICA, 10);

        return y - 110;
    }

    private float drawInvoiceDetails(PDPageContentStream cs, Contract contract,
                                     float x, float y, PDPage page) throws IOException {
        // Ligne des détails
        float detailY = y;
        float detailX = page.getMediaBox().getWidth() - x - 200;

        drawText(cs, "Date d'émission:", detailX, detailY, PDType1Font.HELVETICA, 10);
        drawText(cs, DATE_FORMAT.format(new Date()), detailX + 80, detailY, PDType1Font.HELVETICA, 10);
        detailY -= 15;

        drawText(cs, "Date d'échéance:", detailX, detailY, PDType1Font.HELVETICA, 10);
        drawText(cs, DATE_FORMAT.format(contract.getEndDate()), detailX + 80, detailY, PDType1Font.HELVETICA, 10);
        detailY -= 15;

        drawText(cs, "Référence:", detailX, detailY, PDType1Font.HELVETICA, 10);
        drawText(cs, "CMD-" + contract.getCommande().getId(), detailX + 80, detailY, PDType1Font.HELVETICA, 10);

        return y - 50;
    }

    private float drawInvoiceItemsTable(PDPageContentStream cs, Contract contract,
                                        float x, float y, Color headerColor, Color rowColor, PDPage page) throws IOException {
        float tableWidth = page.getMediaBox().getWidth() - 2*x;
        float[] columnWidths = {tableWidth*0.6f, tableWidth*0.2f, tableWidth*0.2f};

        // En-tête du tableau
        cs.setNonStrokingColor(headerColor);
        cs.addRect(x, y-20, tableWidth, 20);
        cs.fill();

        cs.setNonStrokingColor(Color.WHITE);
        String[] headers = {"DESCRIPTION", "PRIX UNITAIRE", "TOTAL"};
        float currentX = x;
        for (int i = 0; i < headers.length; i++) {
            drawTextCentered(cs, headers[i], currentX + columnWidths[i]/2, y-15, PDType1Font.HELVETICA_BOLD, 10);
            currentX += columnWidths[i];
        }

        // Ligne de l'article unique
        cs.setNonStrokingColor(Color.BLACK);
        float rowY = y - 40;
        Item item = contract.getCommande().getItem();

        // Fond de la ligne
        cs.setNonStrokingColor(rowColor);
        cs.addRect(x, rowY-15, tableWidth, 20);
        cs.fill();
        cs.setNonStrokingColor(Color.BLACK);

        // Contenu des cellules
        currentX = x;
        drawText(cs, item.getName(), currentX + 5, rowY, PDType1Font.HELVETICA, 10);
        currentX += columnWidths[0];

        drawTextRightAligned(cs, String.format("%.2f DT", item.getPrice()),
                currentX + columnWidths[1] - 5, rowY, PDType1Font.HELVETICA, 10);
        currentX += columnWidths[1];

        drawTextRightAligned(cs, String.format("%.2f DT", item.getPrice()),
                currentX + columnWidths[2] - 5, rowY, PDType1Font.HELVETICA, 10);

        return rowY - 20;
    }

    private void drawInvoiceTotals(PDPageContentStream cs, Contract contract,
                                   float x, float y, PDPage page) throws IOException {
        float tableWidth = page.getMediaBox().getWidth() - 2*x;
        float totalX = x + tableWidth*0.6f;
        Item item = contract.getCommande().getItem();
        double price = item.getPrice();
        double total = price ;

        // Sous-total
        drawTextRightAligned(cs, "Sous-total:", totalX, y, PDType1Font.HELVETICA, 10);
        drawTextRightAligned(cs, String.format("%.2f DT", price),
                x+tableWidth, y, PDType1Font.HELVETICA, 10);
        y -= 15;


        // Total
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        drawTextRightAligned(cs, "TOTAL:", totalX, y, PDType1Font.HELVETICA_BOLD, 12);
        drawTextRightAligned(cs, String.format("%.2f DT", total),
                x+tableWidth, y, PDType1Font.HELVETICA_BOLD, 12);
        cs.setFont(PDType1Font.HELVETICA, 10);
    }

    private void drawLegalMentions(PDPageContentStream cs, float x, float y, PDPage page) throws IOException {
        cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
        String mentions = "Facture établie en vertu des dispositions légales en vigueur. ";
        mentions += "TVA non applicable, article 293B du CGI.";

        drawWrappedText(cs, mentions, x, y, page.getMediaBox().getWidth() - 2*x, PDType1Font.HELVETICA_OBLIQUE, 8);
    }

    private void drawTextCentered(PDPageContentStream cs, String text,
                                  float centerX, float y,
                                  PDType1Font font, int size) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000f * size;
        drawText(cs, text, centerX - textWidth/2, y, font, size);
    }

    private void drawTextRightAligned(PDPageContentStream cs, String text,
                                      float rightX, float y,
                                      PDType1Font font, int size) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000f * size;
        drawText(cs, text, rightX - textWidth, y, font, size);
    }

    // Méthode utilitaire pour dessiner du texte

}