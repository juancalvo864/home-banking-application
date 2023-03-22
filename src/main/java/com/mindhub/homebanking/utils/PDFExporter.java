package com.mindhub.homebanking.utils;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;


public class PDFExporter {
    private List<TransactionDTO> listTranasactions;

    public PDFExporter(List<TransactionDTO> listUsers) {
        this.listTranasactions = listUsers;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("DATE", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("DESCRIPTION", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TYPE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("AMOUNT", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("BALANCE", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (TransactionDTO user : listTranasactions ) {
            table.addCell(String.valueOf(user.getDate()));
            table.addCell(user.getDescription());
            table.addCell(String.valueOf(user.getType()));
            table.addCell(String.valueOf(user.getAmount()).toString());
            table.addCell(String.valueOf(user.getBalance()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("NUMBA BANK - TRANSACTIONS", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 5.0f, 2.0f, 2.0f, 2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}