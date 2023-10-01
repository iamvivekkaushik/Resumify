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

import com.vivekkaushik.resumify.model.Resume;
import com.vivekkaushik.resumify.template_model.Education;
import com.vivekkaushik.resumify.template_model.Experience;
import com.vivekkaushik.resumify.template_model.Project;
import com.vivekkaushik.resumify.template_model.Reference;
import com.vivekkaushik.resumify.template_model.Skill;

public class Template5 implements Template {
    // page width and height in pixel for A4 page 300 DPI
    private int A4_WIDTH = 2480;
    private int A4_HEIGHT = 3508;
    // Margin
    private int MARGIN_TOP = 90;
    private int MARGIN_LEFT = 173;
    // gap in pixels
    private int SMALL_GAP = 35;
    private int MEDIUM_GAP = 75;
    private int LARGE_GAP = 100;
    // text size
    private int SMALL_TEXT = 35;
    private int MEDIUM_TEXT = 43;
    private int HEADING_SIZE = 55;
    private int LARGE_TEXT = 60;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    // context field for later use
    private Context mContext;
    private Resume resume;

    // current canvas in use
    private Typeface mTypeface;
    private Canvas canvas = null;
    private Paint paint = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template5(Context context, Resume resume) {
        mContext = context;
        this.resume = resume;
    }

    public PdfDocument createPdf() {
        // create typeface and initialize variable
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "open_sans_regular.ttf");
        // Create a new PDF Document
        PdfDocument document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
        //start and initialize the page and variables
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();

        /* Start Drawing from here */
        paint.setColor(Color.parseColor("#010101"));
        canvas.drawRect(0, 0, A4_WIDTH, A4_HEIGHT, paint);

        createUpperSection();

        currentLeftY = 635 + LARGE_GAP;

        drawObjective();

        drawEducation();

        if (isDone[EDUCATION_SECTION]) drawSkill();

        if (isDone[SKILLS_SECTION]) drawLanguage();

        if (isDone[LANGUAGE_SECTION]) drawHobby();

        /* Left section is done, Start Right Section from here*/

        currentRightY = 635 + LARGE_GAP;

        drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

        while (!isDone[SKILLS_SECTION] || !isDone[EDUCATION_SECTION] || !isDone[EXPERIENCE_SECTION]
                || !isDone[REFERENCE_SECTION] || !isDone[PROJECT_SECTION] || !isDone[LANGUAGE_SECTION]
                || !isDone[HOBBY_SECTION]) {
            document.finishPage(page);
            pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();

            //Start Drawing from here
            paint.setColor(Color.parseColor("#010101"));
            canvas.drawRect(0, 0, A4_WIDTH, A4_HEIGHT, paint);

            currentLeftY = MARGIN_TOP;
            currentRightY = MARGIN_TOP;

            if (!isDone[EDUCATION_SECTION]) {
                drawEducation();
            }

            if (isDone[EDUCATION_SECTION] && !isDone[SKILLS_SECTION]) {
                drawSkill();
            }

            if (isDone[EDUCATION_SECTION] && isDone[SKILLS_SECTION] && !isDone[LANGUAGE_SECTION]) {
                drawLanguage();
            }

            if (isDone[EDUCATION_SECTION] && isDone[SKILLS_SECTION] && isDone[LANGUAGE_SECTION] &&
                    !isDone[HOBBY_SECTION]) {
                drawHobby();
            }

            if (!isDone[EXPERIENCE_SECTION]) {
                drawExperience();
            }

            if (isDone[EXPERIENCE_SECTION] && !isDone[PROJECT_SECTION]) {
                drawProjects();
            }

            if (isDone[EXPERIENCE_SECTION] && isDone[PROJECT_SECTION] && !isDone[REFERENCE_SECTION]) {
                drawReference();
            }
        }

        document.finishPage(page);
        return document;
    }

    private void drawSkill() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Professional Skills", MARGIN_LEFT, currentLeftY, paint);
        currentLeftY += HEADING_SIZE + SMALL_GAP;

        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            //Check if enough space is available on page
            if (currentLeftY + 40 > (A4_HEIGHT - MARGIN_TOP)) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            paint.setTypeface(mTypeface);
            canvas.drawText(skill.getSkill(), MARGIN_LEFT, currentLeftY, paint);

            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            canvas.drawText(skill.getSkillLevel() + "%", 892, currentLeftY, paint);

            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }
        isDone[SKILLS_SECTION] = true;
        currentLeftY += MEDIUM_GAP;
    }

    private void drawLanguage() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        paint.setTypeface(mTypeface);
        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if((currentLeftY + staticLayout.getHeight() + HEADING_SIZE + SMALL_GAP) > (A4_HEIGHT - MARGIN_TOP)) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Languages", MARGIN_LEFT, currentLeftY, paint);
        currentLeftY += HEADING_SIZE;


        canvas.save();
        canvas.translate(MARGIN_LEFT, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + LARGE_GAP;

        isDone[LANGUAGE_SECTION] = true;
    }

    private void drawHobby() {
        if (resume.getHobby().isEmpty()) {
            isDone[HOBBY_SECTION] = true;
            return;
        }

        paint.setTypeface(mTypeface);
        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if((currentLeftY + staticLayout.getHeight() + HEADING_SIZE + SMALL_GAP) > (A4_HEIGHT - MARGIN_TOP)) {
            isDone[HOBBY_SECTION] = false;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Interest", MARGIN_LEFT, currentLeftY, paint);
        currentLeftY += HEADING_SIZE;


        canvas.save();
        canvas.translate(MARGIN_LEFT, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + LARGE_GAP;

        isDone[HOBBY_SECTION] = true;
    }

    private void drawEducation() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Education", MARGIN_LEFT, currentLeftY, paint);
        currentLeftY += HEADING_SIZE + SMALL_GAP;

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            paint.setTextSize(MEDIUM_TEXT);
            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));

            canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), MARGIN_LEFT, currentLeftY, paint);
            currentLeftY += MEDIUM_TEXT + 10;

            paint.setTypeface(mTypeface);
            paint.setTextSize(SMALL_TEXT);

            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            canvas.drawText(edu.getCourse(), MARGIN_LEFT, currentLeftY, paint);
            currentLeftY += SMALL_TEXT - 20;

            paint.setTypeface(mTypeface);
            StaticLayout staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

            canvas.save();
            canvas.translate(MARGIN_LEFT, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        isDone[EDUCATION_SECTION] = true;
        currentLeftY += SMALL_GAP;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Work Experience", MARGIN_LEFT + 992 + 148, currentRightY, paint);
        currentRightY += HEADING_SIZE;

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            // Check if page have enough real state available
            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }

            paint.setTextSize(MEDIUM_TEXT);
            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            StaticLayout staticLayout = new StaticLayout(exp.getCompanyName(), new TextPaint(paint),
                    480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            paint.setTypeface(mTypeface);
            paint.setTextSize(SMALL_TEXT);
            canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), MARGIN_LEFT + 992 + 148 + staticLayout.getWidth() + 100, currentRightY + SMALL_TEXT, paint);

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            paint.setTextSize(SMALL_TEXT);
            staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTypeface(mTypeface);
            staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_TEXT;
        }
        isDone[EXPERIENCE_SECTION] = true;
        currentRightY += MEDIUM_GAP;
    }

    private void drawProjects() {
        if (resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Projects", MARGIN_LEFT + 992 + 148, currentRightY, paint);
        currentRightY += HEADING_SIZE;

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            // Check if page have enough real state available
            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTextSize(MEDIUM_TEXT);
            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            StaticLayout staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                    480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTextSize(SMALL_TEXT);
            staticLayout = new StaticLayout(project.getProjectRole(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTypeface(mTypeface);
            staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_TEXT;
        }
        isDone[PROJECT_SECTION] = true;
        currentRightY += MEDIUM_GAP;
    }

    private void drawReference() {
        if (resume.getProjectList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));

        canvas.drawText("Reference", MARGIN_LEFT + 992 + 148, currentRightY, paint);
        currentRightY += HEADING_SIZE;

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            // Check if page have enough real state available
            if (!isSpaceAvailable(ref)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTextSize(MEDIUM_TEXT);
            paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
            StaticLayout staticLayout = new StaticLayout(ref.getName(), new TextPaint(paint),
                    480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTextSize(SMALL_TEXT);
            staticLayout = new StaticLayout(ref.getDesignation(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            paint.setTypeface(mTypeface);
            staticLayout = new StaticLayout(ref.getPhone(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 15;

            staticLayout = new StaticLayout(ref.getEmail(), new TextPaint(paint),
                    992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(MARGIN_LEFT + 992 + 148, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_TEXT;
        }
        isDone[REFERENCE_SECTION] = true;
        currentRightY += MEDIUM_GAP;
    }

    private void drawObjective() {
        if (resume.getObjective().isEmpty()) {
            return;
        }
        paint.setTextSize(HEADING_SIZE);
        paint.setColor(Color.parseColor("#EEEEEE"));
        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));

        canvas.drawText("Objective", MARGIN_LEFT, currentLeftY, paint);
        currentLeftY += HEADING_SIZE;

        paint.setTypeface(mTypeface);
        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        canvas.save();
        canvas.translate(MARGIN_LEFT, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + LARGE_GAP;
    }

    private void createUpperSection() {
        if (!resume.getImgLocation().isEmpty()) {
            Drawable d = Drawable.createFromPath(resume.getImgLocation());
            assert d != null;
            d.setBounds(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT + 545, MARGIN_TOP + 545);
            d.draw(canvas);
        }
        paint.setTypeface(mTypeface);
        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawText("Hello, I am", 773, 310, paint);
        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(MEDIUM_TEXT);
        String name = resume.getFirstName() + " " + resume.getLastName();
        canvas.drawText(name, 773, 380, paint);
        paint.setTypeface(mTypeface);
        paint.setTextSize(SMALL_TEXT);
        canvas.drawText(resume.getJob(), MARGIN_LEFT + 600, 450, paint);

        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        paint.setTextSize(MEDIUM_TEXT);
        canvas.drawText("A", 1313, 310, paint);
        canvas.drawText("M", 1313, 380, paint);
        canvas.drawText("E", 1313, 450, paint);

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypeface);
        canvas.drawText(resume.getAddress(), 1375, 310, paint);
        canvas.drawText(resume.getMobile(), 1375, 380, paint);
        canvas.drawText(resume.getEmail(), 1375, 450, paint);
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRequired = currentRightY;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        StaticLayout staticLayout = new StaticLayout(ref.getName(), new TextPaint(paint),
                480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 15;

        paint.setTextSize(SMALL_TEXT);
        staticLayout = new StaticLayout(ref.getDesignation(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 15;

        paint.setTypeface(mTypeface);
        staticLayout = new StaticLayout(ref.getPhone(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 15;

        staticLayout = new StaticLayout(ref.getEmail(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN_TOP);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentRightY;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        StaticLayout staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight() + 15;

        paint.setTextSize(SMALL_TEXT);
        staticLayout = new StaticLayout(project.getProjectRole(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired  += staticLayout.getHeight() + 15;

        paint.setTypeface(mTypeface);
        staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN_TOP);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentRightY;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
        StaticLayout staticLayout = new StaticLayout(exp.getCompanyName(), new TextPaint(paint),
                480, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 15;

        paint.setTextSize(SMALL_TEXT);
        staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 15;

        paint.setTypeface(mTypeface);
        staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN_TOP);
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentLeftY;

        paint.setTextSize(MEDIUM_TEXT);
        spaceRequired += MEDIUM_TEXT + 10;
        paint.setTextSize(SMALL_TEXT);
        spaceRequired += SMALL_TEXT - 20;
        paint.setTypeface(mTypeface);
        StaticLayout staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                992, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN_TOP);
    }
}