package com.vivekkaushik.resumify.templates;

import android.graphics.pdf.PdfDocument;

public interface Template {
    // page width and height in pixel for A4 page 300 DPI
    int A4_WIDTH = 2480;
    int A4_HEIGHT = 3508;

    //Sections
    int EDUCATION_SECTION = 0;
    int EXPERIENCE_SECTION = 1;
    int SKILLS_SECTION = 2;
    int REFERENCE_SECTION = 3;
    int PROJECT_SECTION = 4;
    int LANGUAGE_SECTION = 5;
    int HOBBY_SECTION = 6;

    PdfDocument createPdf();
}
