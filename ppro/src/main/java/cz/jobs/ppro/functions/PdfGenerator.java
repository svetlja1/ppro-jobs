package cz.jobs.ppro.functions;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import cz.jobs.ppro.model.CV;
import cz.jobs.ppro.model.Education;
import cz.jobs.ppro.model.WorkingExperience;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenerator {

    public static void generatePdf(CV cv, String pdfFilePath) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();

            // Set a blue font for H2 headings
            Font majorFont = getFont(16, new BaseColor(49, 148, 163));

            Font headingFont = getFont(14, new BaseColor(49, 148, 163));

            // Title - Full name
            addTitle(document, cv.getPersonalData().getFullName(), majorFont);
            // 1st Heading - Personal Data
            addHeading(document, "Personal Data", headingFont);
            addParagraph(document, "Residence: " + cv.getPersonalData().getResidence());
            addParagraph(document, "Date Of Birth: " + cv.getPersonalData().getDateOfBirth());
            addParagraph(document, "Phone Number: " + cv.getPersonalData().getPhoneNumber());
            addParagraph(document, "Email: " + cv.getPersonalData().getEmail());

            addHorizontalLine(document);

            // 2nd Heading - Education List (if present)
            if (cv.getEducationList() != null) {
                addHeading(document, "Education", headingFont);
                for (Education education : cv.getEducationList()) {
                    addEducationInfo(document, education.getStartYear() + " - " + education.getEndYear(), education.getEducation());
                }
            }

            addHorizontalLine(document);

            // 3rd Heading - Education List (if present)
            if (cv.getWorkingExperienceList() != null) {
                addHeading(document, "Working Experience", headingFont);
                for (WorkingExperience workingExperience : cv.getWorkingExperienceList()) {
                    addEducationInfo(document, workingExperience.getStartYear() + " - " + workingExperience.getEndYear(), workingExperience.getWorkingExperience());
                }
            }

            addHorizontalLine(document);



            addHeading(document, "Skills", headingFont);
            addParagraph(document, cv.getSkills());



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static void addTitle(Document document, String text, Font font) throws DocumentException {
        Paragraph heading = new Paragraph(text, font);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);
    }

    private static void addHeading(Document document, String text, Font font) throws DocumentException {
        Paragraph heading = new Paragraph(text, font);
        heading.setAlignment(Element.ALIGN_LEFT);
        document.add(heading);
    }

    private static void addParagraph(Document document, String text) throws DocumentException, IOException {
        Paragraph paragraph = new Paragraph(text, getFont(12, BaseColor.BLACK));
        document.add(paragraph);
    }

    private static void addEducationInfo(Document document, String dateRange, String education) throws DocumentException, IOException {
        Paragraph educationParagraph = new Paragraph();
        educationParagraph.setSpacingBefore(10);
        educationParagraph.add(new Chunk(dateRange, getFont(12, BaseColor.BLACK)));
        educationParagraph.add(Chunk.TABBING);
        educationParagraph.add(new Chunk(education, getFont(12, BaseColor.BLACK)));

        document.add(educationParagraph);
    }

    private static void addHorizontalLine(Document document) throws DocumentException {
        LineSeparator line = new LineSeparator();
        Chunk chunk = new Chunk(line);
        document.add(new Phrase(chunk));
    }

    private static Font getFont(float size, BaseColor color) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        return new Font(baseFont, size, Font.NORMAL, color);
    }

}
