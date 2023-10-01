package com.vivekkaushik.resumify.templates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.vivekkaushik.resumify.R;
import com.vivekkaushik.resumify.model.Resume;
import com.vivekkaushik.resumify.template_model.Education;
import com.vivekkaushik.resumify.template_model.Experience;
import com.vivekkaushik.resumify.template_model.Project;
import com.vivekkaushik.resumify.template_model.Reference;
import com.vivekkaushik.resumify.template_model.Skill;

public class Template8 implements Template {
    // page width and height in pixel for A4 page 300 DPI
    private int A4_WIDTH = 2480;
    private int A4_HEIGHT = 3508;
    // Margin
    private int MARGIN = 90;
    // gap in pixels
    private int SMALL_GAP = 35;
    private int MEDIUM_GAP = 75;
    private int LARGE_GAP = 100;
    // text size
    private int SMALL_TEXT = 40;
    private int MEDIUM_TEXT = 40;
    private int HEADING_SIZE = 55;
    private int LARGE_TEXT = 100;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    // context field for later use
    private Context mContext;
    private Resume resume;

    // current canvas in use
    private Typeface fontName;
    private Typeface fontJosefin;
    private Typeface fontVarela;
    private Canvas canvas = null;
    private Paint paint = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template8(Context context, Resume resume) {
        mContext = context;
        this.resume = resume;
    }

    public PdfDocument createPdf() {
        // create typeface and initialize variable
        fontName = Typeface.createFromAsset(mContext.getAssets(), "doHyeon_regular.ttf");
        fontVarela = Typeface.createFromAsset(mContext.getAssets(), "VarelaRound_Regular.ttf");
        fontJosefin = Typeface.createFromAsset(mContext.getAssets(), "JosefinSans_Regular.ttf");
        // Create a new PDF Document
        PdfDocument document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
        //start and initialize the page and variables
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();

        /* Start Drawing from here */
        paint.setColor(Color.parseColor("#263533"));
        canvas.drawRect(0, 0, A4_WIDTH, 350, paint);
        paint.setColor(Color.parseColor("#030303"));
        canvas.drawRect(0, 350, A4_WIDTH, A4_HEIGHT, paint);
        paint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawRect(0, 360, 1636, A4_HEIGHT, paint);

        paint.setTypeface(fontName);
        paint.setTextSize(LARGE_TEXT);
        String name = resume.getFirstName() + " " + resume.getLastName();
        canvas.drawText(name, A4_WIDTH - (paint.measureText(name + 80)), 250, paint);

        currentLeftY = 360 + MARGIN;
        currentRightY = 350 + MARGIN;

        drawObjective();

        drawEducationSection();

        if (isDone[EDUCATION_SECTION]) drawExperienceSection();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

        drawContactSection();

        drawSkillSection();

        if (isDone[SKILLS_SECTION]) drawLanguage();

        if (isDone[LANGUAGE_SECTION]) drawHobby();

        while (!isDone[SKILLS_SECTION] || !isDone[EDUCATION_SECTION] || !isDone[EXPERIENCE_SECTION]
                || !isDone[REFERENCE_SECTION] || !isDone[PROJECT_SECTION] || !isDone[LANGUAGE_SECTION]
                || !isDone[HOBBY_SECTION]) {
            document.finishPage(page);
            pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();

            paint.setColor(Color.parseColor("#263533"));
            canvas.drawRect(0, 0, A4_WIDTH, 350, paint);
            paint.setColor(Color.parseColor("#030303"));
            canvas.drawRect(0, 350, A4_WIDTH, A4_HEIGHT, paint);
            paint.setColor(Color.parseColor("#EEEEEE"));
            canvas.drawRect(0, 360, 1636, A4_HEIGHT, paint);

            //Start Drawing from here
            paint.setTypeface(fontName);
            paint.setTextSize(LARGE_TEXT);
            canvas.drawText(name, A4_WIDTH - (paint.measureText(name + 80)), 250, paint);

            currentLeftY = 360 + MARGIN;
            currentRightY = 350 + MARGIN;

            if (!isDone[EDUCATION_SECTION]) {
                drawEducationSection();
            }

            if (isDone[EDUCATION_SECTION] && !isDone[EXPERIENCE_SECTION]) {
                drawExperienceSection();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && !isDone[PROJECT_SECTION]) {
                drawProjects();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && isDone[PROJECT_SECTION]
                    && !isDone[REFERENCE_SECTION]) {
                drawReference();
            }

            if (!isDone[SKILLS_SECTION]) {
                drawSkillSection();
            }

            if (isDone[SKILLS_SECTION] && !isDone[LANGUAGE_SECTION]) {
                drawLanguage();
            }

            if (isDone[SKILLS_SECTION] && isDone[LANGUAGE_SECTION] && !isDone[HOBBY_SECTION]) {
                drawHobby();
            }
        }

        /* The End Is Near */
        document.finishPage(page);
        return document;
    }

    private void drawSkillSection() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));
        paint.setTypeface(Typeface.create(fontJosefin, Typeface.BOLD));

        canvas.drawText("SKILLS", 1616 + MARGIN, currentRightY, paint);
        canvas.drawLine(1646 + MARGIN + paint.measureText("SKILLS"), currentRightY - (HEADING_SIZE / 2) + 5,
                A4_WIDTH - MARGIN, currentRightY - (HEADING_SIZE / 2) + 5, paint);

        currentRightY += MEDIUM_GAP;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(fontVarela);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            //Check if enough space is available on page
            if (currentRightY + 25 + MEDIUM_TEXT > (A4_HEIGHT - MARGIN)) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            paint.setColor(Color.parseColor("#EEEEEE"));
            canvas.drawText(skill.getSkill(), 1616 + MARGIN, currentRightY, paint);

            currentRightY += 25;

            paint.setColor(Color.parseColor("#45615C"));
            canvas.drawRect(1616 + MARGIN, currentRightY, A4_WIDTH - MARGIN, currentRightY + 10, paint);
            paint.setColor(Color.parseColor("#EEEEEE"));
            canvas.drawRect(1616 + MARGIN, currentRightY, 1636 + MARGIN + ((664 * skill.getSkillLevel()) / 100),
                    currentRightY + 10, paint);

            currentRightY += SMALL_GAP + MEDIUM_TEXT;
        }
        currentRightY += MEDIUM_GAP;
        isDone[SKILLS_SECTION] = true;
    }

    private void drawLanguage() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(fontVarela);
        paint.setColor(Color.parseColor("#EEEEEE"));
        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint),
                700, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((currentRightY + staticLayout.getHeight() + SMALL_GAP) > (A4_HEIGHT - MARGIN)) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        paint.setTextSize(HEADING_SIZE);
        paint.setTypeface(Typeface.create(fontJosefin, Typeface.BOLD));
        canvas.drawText("LANGUAGES", 1616 + MARGIN, currentRightY, paint);
        canvas.drawLine(1646 + MARGIN + paint.measureText("LANGUAGES"), currentRightY - (HEADING_SIZE / 2) + 5,
                A4_WIDTH - MARGIN, currentRightY - (HEADING_SIZE / 2) + 5, paint);
        currentRightY += SMALL_GAP;

        canvas.save();
        canvas.translate(1616 + MARGIN, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentRightY += staticLayout.getHeight() + MEDIUM_GAP + SMALL_GAP;
        isDone[LANGUAGE_SECTION] = true;
    }

    private void drawHobby() {
        if (resume.getHobby().isEmpty()) {
            isDone[HOBBY_SECTION] = true;
            return;
        }

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(fontVarela);
        paint.setColor(Color.parseColor("#EEEEEE"));
        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint),
                700, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((currentRightY + staticLayout.getHeight() + SMALL_GAP) > (A4_HEIGHT - MARGIN)) {
            isDone[HOBBY_SECTION] = false;
            return;
        }

        paint.setTextSize(HEADING_SIZE);
        paint.setTypeface(Typeface.create(fontJosefin, Typeface.BOLD));
        canvas.drawText("HOBBIES", 1616 + MARGIN, currentRightY, paint);
        canvas.drawLine(1646 + MARGIN + paint.measureText("HOBBIES"), currentRightY - (HEADING_SIZE / 2) + 5,
                A4_WIDTH - MARGIN, currentRightY - (HEADING_SIZE / 2) + 5, paint);
        currentRightY += SMALL_GAP;

        canvas.save();
        canvas.translate(1616 + MARGIN, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentRightY += staticLayout.getHeight() + MEDIUM_GAP + SMALL_GAP;
        isDone[HOBBY_SECTION] = true;
    }

    private void drawContactSection() {
        if (!resume.getImgLocation().isEmpty()) {
            Drawable d = Drawable.createFromPath(resume.getImgLocation());
            assert d != null;
            d.setBounds(1636 + 71, 350 + MARGIN, A4_WIDTH - 71, 350 + 702 + MARGIN);
            d.draw(canvas);
            currentRightY = 1142 + MEDIUM_GAP;
        } else {
            currentRightY = 350 + MARGIN;
        }

        if (!resume.getAddress().isEmpty()) drawDetails(1623 + MARGIN, currentRightY + 12,
                1648 + MARGIN, currentRightY + 45, R.drawable.white_location, resume.getAddress());

        if (!resume.getMobile().isEmpty()) drawDetails(1616 + MARGIN, currentRightY + 15,
                1656 + MARGIN, currentRightY + 40, R.drawable.white_phone, resume.getMobile());

        if (!resume.getEmail().isEmpty()) drawDetails(1620 + MARGIN, currentRightY + 18,
                1650 + MARGIN, currentRightY + 40, R.drawable.white_mail, resume.getEmail());

        if (!resume.getWebsite().isEmpty()) drawDetails(1620 + MARGIN, currentRightY + 20,
                1650 + MARGIN, currentRightY + 45, R.drawable.white_web, resume.getWebsite());

        currentRightY += MEDIUM_GAP;
    }

    private void drawExperienceSection() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        drawHeading(MARGIN + 20, currentLeftY + 8, MARGIN + MARGIN - 10,
                currentLeftY + MARGIN - 19, R.drawable.white_person, "EXPERIENCE");
        currentLeftY += MARGIN + MEDIUM_GAP + 10;

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            // Check if page have enough real state available
            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(fontVarela, Typeface.BOLD));
            paint.setTextSize(MEDIUM_TEXT);
            canvas.drawText(exp.getJobTitle(), MARGIN, currentLeftY, paint);

            paint.setTypeface(fontVarela);
            canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), MARGIN + 50 +
                    paint.measureText(exp.getJobTitle()), currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT + 18;

            paint.setTypeface(Typeface.create(fontVarela, Typeface.BOLD));
            canvas.drawText(exp.getCompanyName(), MARGIN, currentLeftY, paint);
            currentLeftY += MEDIUM_TEXT - 20;

            paint.setTypeface(fontVarela);
            StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                    1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawEducationSection() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        drawHeading(MARGIN + 25, currentLeftY + 2, MARGIN + MARGIN - 15,
                currentLeftY + MARGIN - 13, R.drawable.white_bulb, "EDUCATION");
        currentLeftY += MARGIN + MEDIUM_GAP + 10;

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(fontVarela, Typeface.BOLD));
            paint.setTextSize(MEDIUM_TEXT);
            canvas.drawText(edu.getCourse(), MARGIN, currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT + 18;

            paint.setTypeface(fontVarela);
            canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), MARGIN, currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT - 20;

            StaticLayout staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                    1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        isDone[EDUCATION_SECTION] = true;
    }

    private void drawProjects() {
        if (resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        drawHeading(MARGIN + 20, currentLeftY + 8, MARGIN + MARGIN - 10,
                currentLeftY + MARGIN - 19, R.drawable.project_white, "PROJECTS");
        currentLeftY += MARGIN + MEDIUM_GAP + 10;

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(fontVarela, Typeface.BOLD));
            paint.setTextSize(MEDIUM_TEXT);
            canvas.drawText(project.getProjectName(), MARGIN, currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT + 18;

            paint.setTypeface(fontVarela);
            canvas.drawText(project.getProjectRole(), MARGIN, currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT - 20;

            StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        isDone[PROJECT_SECTION] = true;
    }

    private void drawReference() {
        if (resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        drawHeading(MARGIN + 20, currentLeftY + 8, MARGIN + MARGIN - 10,
                currentLeftY + MARGIN - 19, R.drawable.reference_white, "REFERENCE");
        currentLeftY += MARGIN + MEDIUM_GAP + 10;

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(ref)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(fontVarela, Typeface.BOLD));
            paint.setTextSize(MEDIUM_TEXT);
            canvas.drawText(ref.getName(), MARGIN, currentLeftY, paint);

            currentLeftY += MEDIUM_TEXT + 18;

            paint.setTypeface(fontVarela);
            canvas.drawText(ref.getDesignation(), MARGIN, currentLeftY, paint);
            currentLeftY += MEDIUM_TEXT + 18;

            canvas.drawText(ref.getPhone(), MARGIN, currentLeftY, paint);
            currentLeftY += MEDIUM_TEXT + 18;

            canvas.drawText(ref.getEmail(), MARGIN, currentLeftY, paint);
            currentLeftY += MEDIUM_TEXT + SMALL_GAP;
        }
        isDone[REFERENCE_SECTION] = true;
    }

    private void drawObjective() {
        paint.setTypeface(Typeface.create(fontJosefin, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#263533"));

        canvas.drawText("HELLO !", MARGIN, currentLeftY, paint);
        currentLeftY += HEADING_SIZE;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setColor(Color.parseColor("#040606"));
        paint.setTypeface(fontVarela);
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
    }

    private void drawDetails(int left, int top, int right, int bottom, int image, String text) {
        paint.setColor(Color.parseColor("#263533"));
        canvas.drawCircle(1636 + MARGIN, currentRightY + 30, 30, paint);

        Drawable d = mContext.getResources().getDrawable(image);
        d.setBounds(left, top, right, bottom);
        d.draw(canvas);

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(fontVarela);
        paint.setColor(Color.parseColor("#EEEEEE"));
        StaticLayout staticLayout = new StaticLayout(text, new TextPaint(paint),
                610, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(1636 + MARGIN + 50, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentRightY += staticLayout.getHeight() + SMALL_GAP;
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentLeftY;

        spaceRequired += MEDIUM_TEXT + 18;
        spaceRequired += MEDIUM_TEXT - 20;
        StaticLayout staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + MEDIUM_GAP;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentLeftY;

        spaceRequired += MEDIUM_TEXT + 18;

        spaceRequired += MEDIUM_TEXT - 20;

        StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight() + MEDIUM_GAP;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentLeftY;

        spaceRequired += MEDIUM_TEXT + 18;
        spaceRequired += MEDIUM_TEXT - 20;
        StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                1400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + MEDIUM_GAP;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRequired = currentLeftY;

        spaceRequired += MEDIUM_TEXT + 18;
        spaceRequired += MEDIUM_TEXT + 18;
        spaceRequired += MEDIUM_TEXT + 18;
        spaceRequired += MEDIUM_TEXT;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private void drawHeading(int left, int top, int right, int bottom, int image, String heading) {
        paint.setColor(Color.parseColor("#263533"));
        canvas.drawCircle(MARGIN + 50, currentLeftY + 40, 50, paint);

        Drawable d = mContext.getResources().getDrawable(image);
        d.setBounds(left, top, right, bottom);
        d.draw(canvas);

        paint.setTypeface(Typeface.create(fontJosefin, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);

        canvas.drawText(heading, MARGIN + MARGIN + 50, currentLeftY + HEADING_SIZE, paint);
        canvas.drawLine((MARGIN * 2) + 100 + paint.measureText(heading), currentLeftY + (MARGIN / 2) - 5,
                1500, currentLeftY + (MARGIN / 2) - 5, paint);
    }
}