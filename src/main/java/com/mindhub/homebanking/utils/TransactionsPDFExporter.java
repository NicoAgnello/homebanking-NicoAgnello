package com.mindhub.homebanking.utils;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mindhub.homebanking.models.Transaction;

public class TransactionsPDFExporter {
    private List<Transaction> transactions;

    public TransactionsPDFExporter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(6);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Account", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);


        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(3);
        for (Transaction transaction : transactions) {
            cell.setPhrase(new Phrase(String.valueOf(transaction.getAccount().getNumber())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(transaction.getType())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(transaction.getDate())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(transaction.getDescription())));
            table.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(transaction.getAmount())));
            table.addCell(cell);
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Transactions", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 1.5f, 3.5f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
