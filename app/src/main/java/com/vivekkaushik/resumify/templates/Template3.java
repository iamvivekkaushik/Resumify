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

public class Template3 implements Template{
    private static final String TAG = "Template3";
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
    private int MEDIUM_TEXT = 60;
    private int HEADING_SIZE = 80;
    private int LARGE_TEXT = 125;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    // context field for later use
    private Context mContext;
    private Resume resume;

    // current canvas in use
    private Typeface openSansRegular;
    private PdfDocument document = null;
    private Canvas canvas = null;
    private Paint paint = null;
    private PdfDocument.Page page = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template3(Context context, Resume resume) {
        mContext = context;
        this.resume = resume;
    }

    @Override
    public PdfDocument createPdf() {
        // create typeface and initialize variable
        openSansRegular = Typeface.createFromAsset(mContext.getAssets(), "open_sans_regular.ttf");
        // Create a new PDF Document
        document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber).create();
        //start and initialize the page and variables
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();

        // Draw everything on page here
        initialSetup();

        currentLeftY = MARGIN;

        drawUserImage();

        drawName();

        /*   Left Side is done here  */

        currentRightY = MARGIN;

        drawObjective();

        drawEducation();

        if (isDone[EDUCATION_SECTION]) drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

        drawContactSection();

        drawSkills();

        if (isDone[SKILLS_SECTION]) drawLanguage();

        if (isDone[LANGUAGE_SECTION]) drawHobby();

        while (!isDone[SKILLS_SECTION] || !isDone[EDUCATION_SECTION] || !isDone[EXPERIENCE_SECTION]
                || !isDone[REFERENCE_SECTION] || !isDone[PROJECT_SECTION] || !isDone[LANGUAGE_SECTION]
                || !isDone[HOBBY_SECTION]) {
            document.finishPage(page);
            pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();


            //Start Drawing from here
            initialSetup();

            currentLeftY = MARGIN;
            currentRightY = MARGIN;

            if (!isDone[EDUCATION_SECTION]) {
                drawEducation();
            }

            if (isDone[EDUCATION_SECTION] && !isDone[EXPERIENCE_SECTION]) {
                drawExperience();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && !isDone[PROJECT_SECTION]) {
                drawProjects();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && isDone[PROJECT_SECTION]
                    && !isDone[REFERENCE_SECTION]) {
                drawReference();
            }

            if (!isDone[SKILLS_SECTION]) {
                drawSkills();
            }

            if (isDone[SKILLS_SECTION] && !isDone[LANGUAGE_SECTION]) {
                drawLanguage();
            }

            if (isDone[SKILLS_SECTION] && isDone[LANGUAGE_SECTION] && !isDone[HOBBY_SECTION]) {
                drawHobby();
            }
        }

        // Finish the page
        document.finishPage(page);
        return document;
    }

    private void initialSetup() {
        paint.setColor(Color.parseColor("#259CB5"));
        canvas.drawRect(0, 0, 990, A4_HEIGHT, paint);
    }

    private void drawUserImage() {
        if (resume.getImgLocation().isEmpty()) {
            return;
        }
        Drawable d = Drawable.createFromPath(resume.getImgLocation());
        assert d != null;
        d.setBounds(90, MARGIN, 900, 900);
        d.draw(canvas);
        currentLeftY += 900 + LARGE_GAP + MEDIUM_GAP - MARGIN;
    }

    private void drawName() {
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(LARGE_TEXT);
        canvas.drawText(resume.getFirstName(), (990 / 2) - (paint.measureText(resume.getFirstName()) / 2), currentLeftY, paint);
        currentLeftY += LARGE_TEXT + SMALL_GAP;
        canvas.drawText(resume.getLastName(), (990 / 2) - (paint.measureText(resume.getLastName()) / 2), currentLeftY, paint);
        currentLeftY += LARGE_TEXT;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(MEDIUM_TEXT);
        canvas.drawText(resume.getJob(), (990 / 2) - (paint.measureText(resume.getJob()) / 2), currentLeftY, paint);

        currentLeftY += MEDIUM_TEXT + SMALL_GAP;
    }

    private void drawContactSection() {
        if (resume.getEmail().isEmpty() && resume.getWebsite().isEmpty() && resume.getMobile().isEmpty()
                && resume.getAddress().isEmpty()) {
                return;
        }

        paint.setTypeface(openSansRegular);
        paint.setTextSize(HEADING_SIZE);
        createHeading("CONTACT", MARGIN, currentLeftY, "#FFFFFF", "#259CB5", 810);
        currentLeftY += HEADING_SIZE + 60 + LARGE_GAP;

        // ToDo: Use static Layout here
        paint.setColor(Color.WHITE);
        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);


        //ToDo: add images here
        if (!resume.getMobile().isEmpty()) {
            canvas.drawText(resume.getMobile(), MARGIN, currentLeftY, paint);
            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }

        if (!resume.getAddress().isEmpty()) {
            canvas.drawText(resume.getAddress(), MARGIN, currentLeftY, paint);
            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }

        if (!resume.getEmail().isEmpty()) {
            canvas.drawText(resume.getEmail(), MARGIN, currentLeftY, paint);
            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }

        if (!resume.getWebsite().isEmpty()) {
            canvas.drawText(resume.getWebsite(), MARGIN, currentLeftY, paint);
            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }

        currentLeftY += MEDIUM_GAP;
    }

    private void drawSkills() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("SKILLS", MARGIN, currentLeftY, "#FFFFFF", "#259CB5", 810);
        currentLeftY += MEDIUM_GAP + 80 + HEADING_SIZE;

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            if (!isSpaceAvailable(skill)) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            paint.setColor(Color.WHITE);
            canvas.drawText(skill.getSkill(), MARGIN, currentLeftY, paint);
            currentLeftY += SMALL_TEXT + SMALL_GAP;

            paint.setColor(Color.BLACK);
            canvas.drawRect(MARGIN, currentLeftY - SMALL_TEXT + 15,  MARGIN + 810, currentLeftY - 10, paint);

            paint.setColor(Color.WHITE);
            canvas.drawRect(MARGIN, currentLeftY - SMALL_TEXT + 15, ((810 * skill.getSkillLevel()) / 100) + MARGIN, currentLeftY - 10, paint);

            currentLeftY += SMALL_TEXT + SMALL_GAP;
        }
        isDone[SKILLS_SECTION] = true;
        currentLeftY += SMALL_GAP;
    }

    private void drawLanguage() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.WHITE);
        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint),
                810, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((currentLeftY + staticLayout.getHeight() + MEDIUM_GAP + HEADING_SIZE + 60) > A4_HEIGHT - MARGIN){
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("LANGUAGE", MARGIN, currentLeftY, "#FFFFFF", "#259CB5", 810);
        currentLeftY += MEDIUM_GAP + 80 + HEADING_SIZE;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.WHITE);

        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        isDone[LANGUAGE_SECTION] = true;
    }

    private void drawHobby() {
        if (resume.getLanguage().isEmpty()) {
            isDone[HOBBY_SECTION] = true;
            return;
        }

        paint.setColor(Color.WHITE);
        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint),
                810, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((currentLeftY + staticLayout.getHeight() + MEDIUM_GAP + HEADING_SIZE + 60) > A4_HEIGHT - MARGIN){
            isDone[HOBBY_SECTION] = false;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("INTEREST", MARGIN, currentLeftY, "#FFFFFF", "#259CB5", 810);
        currentLeftY += MEDIUM_GAP + 80 + HEADING_SIZE;

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.WHITE);

        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        isDone[HOBBY_SECTION] = true;
    }

    private void drawObjective() {
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("OBJECTIVE", 1090, currentRightY, "#259CB5", "#FFFFFF", 0);
        currentRightY += HEADING_SIZE + 60 + SMALL_GAP;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.BLACK);
        // ToDo: Change text with the real text
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(1090, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentRightY += staticLayout.getHeight() + LARGE_GAP;
    }

    private void drawEducation() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("EDUCATION", 1090, currentRightY, "#259CB5", "#FFFFFF", 0);
        currentRightY += HEADING_SIZE + 60 + LARGE_GAP;

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), 1090, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(openSansRegular);
            StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight();

            staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + LARGE_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[EDUCATION_SECTION] = true;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("EXPERIENCE", 1090, currentRightY, "#259CB5", "#FFFFFF", 0);
        currentRightY += HEADING_SIZE + 60 + LARGE_GAP;

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), 1090, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(openSansRegular);
            StaticLayout staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight();

            staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + LARGE_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawProjects() {
        if (resume.getEduList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("PROJECTS", 1090, currentRightY, "#259CB5", "#FFFFFF", 0);
        currentRightY += HEADING_SIZE + 60 + LARGE_GAP;

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawText(project.getProjectName(), 1090, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(openSansRegular);
            StaticLayout staticLayout = new StaticLayout(project.getProjectRole(), new TextPaint(paint),
                    1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight();

            staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + LARGE_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[PROJECT_SECTION] = true;
    }

    private void drawReference() {
        if (resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE);
        createHeading("REFERENCE", 1090, currentRightY, "#259CB5", "#FFFFFF", 0);
        currentRightY += HEADING_SIZE + 60 + LARGE_GAP;

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            if (!isSpaceAvailable(ref)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawText(ref.getName(), 1090, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(openSansRegular);
            StaticLayout staticLayout = new StaticLayout(ref.getDesignation(), new TextPaint(paint),
                    1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight();

            staticLayout = new StaticLayout(ref.getEmail(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(1090, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP + 10;

            canvas.drawText(ref.getName(), 1090, currentRightY, paint);
            currentRightY += SMALL_TEXT + LARGE_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[REFERENCE_SECTION] = true;
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT;

        StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint), 1290,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint), 1290,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT;

        StaticLayout staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint),
                1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint), 1290,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT;

        StaticLayout staticLayout = new StaticLayout(project.getProjectRole(), new TextPaint(paint),
                1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT;

        StaticLayout staticLayout = new StaticLayout(ref.getDesignation(), new TextPaint(paint),
                1290, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        staticLayout = new StaticLayout(ref.getEmail(), new TextPaint(paint), 1290, Layout.Alignment.ALIGN_NORMAL,
                1, 1, false);

        spaceRequired += staticLayout.getHeight() + SMALL_GAP + 10;

        spaceRequired += SMALL_TEXT;

        return spaceRequired <= (A4_HEIGHT);
    }

    private boolean isSpaceAvailable(Skill skill) {
        int spaceRequired = currentLeftY;

        spaceRequired += SMALL_TEXT + SMALL_GAP;
        spaceRequired += MEDIUM_TEXT + SMALL_GAP;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    /**
     * This method creates a heading with box
     *
     * @param text      text you want your heading to be
     * @param x         x position of your heading
     * @param y         y position of your heading
     * @param boxColor  color of box
     * @param textColor color of text
     * @param maxLength if you want box to be of fixed size. Pass 0 for auto adjust.
     */
    private void createHeading(String text, int x, int y, String boxColor, String textColor, int maxLength) {
        int textWidth = (int) paint.measureText(text);

        if (maxLength > 0) {
            paint.setColor(Color.parseColor(boxColor));
            canvas.drawRect(x, y, x + maxLength, y + HEADING_SIZE + 60, paint);
            paint.setColor(Color.parseColor(textColor));
            canvas.drawText(text, (maxLength / 2) - (textWidth / 2) + x, y + 15 + HEADING_SIZE, paint);
        } else {
            paint.setColor(Color.parseColor(boxColor));
            canvas.drawRect(x, y, x + textWidth + 100, y + paint.getTextSize() + 60, paint);
            paint.setColor(Color.parseColor(textColor));
            canvas.drawText(text, x + 50, y + 15 + paint.getTextSize(), paint);
        }
    }

}
