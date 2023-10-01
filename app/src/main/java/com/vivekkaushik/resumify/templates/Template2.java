package com.vivekkaushik.resumify.templates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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

public class Template2 implements Template{
    private static final String TAG = "Template2";
    // Margin
    private int MARGIN = 150;
    // gap in pixels
    private int SMALL_GAP = 35;
    private int MEDIUM_GAP = 75;
    private int LARGE_GAP = 100;
    // text size
    private int SMALL_TEXT = 37;
    private int MEDIUM_TEXT = 62;
    private int LARGE_TEXT = 125;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    // context field for later use
    private Context mContext;
    private Resume resume;

    private Typeface openSans700, openSansRegular;

    // current canvas in use
    private Canvas canvas = null;
    private Paint paint = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template2(Context context, Resume resume) {
        mContext = context;
        this.resume = resume;
    }

    @Override
    public PdfDocument createPdf() {
        // Create a new PDF Document
        PdfDocument document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
        //start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();

        openSans700 = Typeface.createFromAsset(mContext.getAssets(), "open_sans_700.ttf");
        openSansRegular = Typeface.createFromAsset(mContext.getAssets(), "open_sans_regular.ttf");


        initialize();

        currentLeftY = 815;
        currentRightY = 815;

        drawObjective();

        drawEducation();

        if (isDone[EDUCATION_SECTION]) drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

        drawContactInfo();

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

        document.finishPage(page);
        return document;
    }

    private void drawObjective() {
        if (resume.getObjective().isEmpty()) {
            return;
        }
        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Objective", 890, currentRightY, paint);
        currentRightY += MEDIUM_GAP;

        int lineStartPosition = currentRightY + 25;
        canvas.drawCircle(900, currentRightY + 25, 8, paint);

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(openSansRegular);
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(975, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentRightY += staticLayout.getHeight();
        paint.setStrokeWidth(3);
        canvas.drawLine(900, lineStartPosition, 900, currentRightY, paint);
        currentRightY += LARGE_GAP + SMALL_GAP;
    }

    private void drawEducation() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Education", 890, currentRightY, paint);
        currentRightY += MEDIUM_GAP;

        int lineStartPosition = currentRightY + 25;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            //Check if there is enough real state available on the page
            if (!isSpaceAvailable(edu)) {
                canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));

            canvas.drawCircle(900, currentRightY + 25, 8, paint);

            StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP + 10;

            canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), 950, currentRightY + 10, paint);

            paint.setTypeface(openSansRegular);
            currentRightY += SMALL_TEXT;

            staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY - 5);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP;
        }
        canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
        isDone[EDUCATION_SECTION] = true;
        currentRightY += MEDIUM_GAP + SMALL_GAP;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Experience", 890, currentRightY, paint);
        currentRightY += MEDIUM_GAP;

        int lineStartPosition = currentRightY + 25;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            //Check if there is enough real state available on the page
            if (!isSpaceAvailable(exp)) {
                canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawCircle(900, currentRightY + 25, 8, paint);
            StaticLayout staticLayout = new StaticLayout(exp.getJobTitle() + ",   "
                    + exp.getCompanyName(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP;

            canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), 950, currentRightY + 15, paint);

            paint.setTypeface(openSansRegular);
            currentRightY += SMALL_TEXT;

            staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP;
        }
        canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
        currentRightY += MEDIUM_GAP + SMALL_GAP;
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawProjects() {
        if (resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Project", 890, currentRightY, paint);
        currentRightY += MEDIUM_GAP;

        int lineStartPosition = currentRightY + 25;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            //Check if there is enough real state available on the page
            if (!isSpaceAvailable(project)) {
                canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));

            canvas.drawCircle(900, currentRightY + 25, 8, paint);

            StaticLayout staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                    1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP + 10;

            canvas.drawText(project.getProjectRole(), 950, currentRightY + 10, paint);

            paint.setTypeface(openSansRegular);
            currentRightY += SMALL_TEXT;

            staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY - 5);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP;
        }
        canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP, paint);
        isDone[PROJECT_SECTION] = true;
        currentRightY += MEDIUM_GAP + SMALL_GAP;
    }

    private void drawReference() {
        if (resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Reference", 890, currentRightY, paint);
        currentRightY += MEDIUM_GAP;

        int lineStartPosition = currentRightY + 25;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(openSansRegular);

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            //Check if there is enough real state available on the page
            if (!isSpaceAvailable(ref)) {
                canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP - 25, paint);
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
            canvas.drawCircle(900, currentRightY + 25, 8, paint);
            StaticLayout staticLayout = new StaticLayout(ref.getName() + ",   "
                    + ref.getDesignation(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL,
                    1, 1, false);
            canvas.save();
            canvas.translate(950, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + SMALL_GAP;

            paint.setTypeface(openSansRegular);
            canvas.drawText(ref.getPhone(), 950, currentRightY + 15, paint);

            currentRightY += SMALL_TEXT + SMALL_GAP;

            canvas.drawText(ref.getEmail(), 950, currentRightY, paint);

            currentRightY += SMALL_TEXT + SMALL_GAP;
        }
        canvas.drawLine(900, lineStartPosition, 900, currentRightY - SMALL_GAP - 25, paint);
        currentRightY += MEDIUM_GAP;
        isDone[REFERENCE_SECTION] = true;
    }

    private void drawContactInfo() {
        if (resume.getAddress().isEmpty() && resume.getMobile().isEmpty() && resume.getEmail().isEmpty() &&
                resume.getWebsite().isEmpty()) {
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Contact Info", MARGIN, currentLeftY, paint);

        currentLeftY += MEDIUM_GAP;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);

        StaticLayout staticLayout;

        if (!resume.getAddress().isEmpty()) {
            staticLayout = new StaticLayout(resume.getAddress(), new TextPaint(paint),
                    550, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

            canvas.save();
            canvas.translate(250, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }

        if (!resume.getMobile().isEmpty()) {
            staticLayout = new StaticLayout(resume.getMobile(), new TextPaint(paint), 550,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(250, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }


        if (!resume.getEmail().isEmpty()) {
            staticLayout = new StaticLayout(resume.getEmail(), new TextPaint(paint), 550,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(250, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }

        if (!resume.getWebsite().isEmpty()) {
            staticLayout = new StaticLayout(resume.getWebsite(), new TextPaint(paint), 550,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(250, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }

        currentLeftY += MEDIUM_GAP + SMALL_GAP;
    }

    private void drawSkills() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Skills", 150, currentLeftY, paint);

        currentLeftY += MEDIUM_GAP;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(openSansRegular);
        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            if (!isSpaceAvailable(skill)){
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            canvas.drawText(skill.getSkill(), 150, currentLeftY, paint);
            currentLeftY += SMALL_TEXT;

            canvas.drawLine(150, currentLeftY, 700, currentLeftY, paint);
            canvas.drawCircle( 150 + ((550 * skill.getSkillLevel())/100), currentLeftY, 8, paint);

            currentLeftY += MEDIUM_GAP;
        }
        isDone[SKILLS_SECTION] = true;
        currentLeftY += MEDIUM_GAP;
    }

    private void drawLanguage() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint), 550,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        if(currentLeftY + staticLayout.getHeight() > A4_HEIGHT - MARGIN) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }
        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Language", MARGIN, currentLeftY, paint);

        currentLeftY += SMALL_GAP;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);

        canvas.save();
        canvas.translate(150, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP + SMALL_GAP;
        isDone[LANGUAGE_SECTION] = true;
    }

    private void drawHobby() {
        if (resume.getLanguage().isEmpty()) {
            isDone[HOBBY_SECTION] = true;
            return;
        }

        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint), 550,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        if(currentLeftY + staticLayout.getHeight() > A4_HEIGHT - MARGIN) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(openSansRegular, Typeface.BOLD));
        canvas.drawText("Interest", MARGIN, currentLeftY, paint);

        currentLeftY += SMALL_GAP;

        paint.setTypeface(openSansRegular);
        paint.setTextSize(SMALL_TEXT);

        canvas.save();
        canvas.translate(150, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP + SMALL_GAP;
        isDone[HOBBY_SECTION] = true;
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRquired = currentRightY;

        StaticLayout staticLayout = new StaticLayout(ref.getName() + ",   "
                + ref.getDesignation(), new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL,
                1, 1, false);

        spaceRquired += staticLayout.getHeight() + SMALL_GAP;

        spaceRquired += SMALL_TEXT + SMALL_GAP;

        spaceRquired += SMALL_TEXT;

        return spaceRquired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentRightY;

        StaticLayout staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();
        spaceRequired += SMALL_TEXT + 30;
        staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint), 1355,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= A4_HEIGHT - MARGIN;
    }

    private boolean isSpaceAvailable(Skill skill) {
        int spaceAvailable = currentLeftY;

        spaceAvailable += SMALL_TEXT + MEDIUM_GAP;

        return spaceAvailable <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentRightY;

        StaticLayout staticLayout = new StaticLayout(exp.getJobTitle() + ", " + exp.getCompanyName(),
                new TextPaint(paint), 1355, Layout.Alignment.ALIGN_NORMAL, 1,
                1, false);
        spaceRequired += staticLayout.getHeight() + SMALL_GAP;
        spaceRequired += SMALL_TEXT + SMALL_GAP;
        staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint), 1355,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentRightY;

        StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint),
                1355, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();
        spaceRequired += SMALL_TEXT + 30;
        staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint), 1355,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= A4_HEIGHT - MARGIN;
    }

    private void drawNameAndJob() {
        paint.setColor(Color.parseColor("#00051A"));

        paint.setTextSize(LARGE_TEXT);
        paint.setColor(Color.WHITE);
        paint.setTypeface(openSans700);
        canvas.drawText(resume.getFirstName(), 212, 250, paint);
        paint.setTypeface(openSansRegular);
        canvas.drawText(resume.getLastName(), 212 + paint.measureText(resume.getFirstName()) + 80, 250, paint);
        currentLeftY = 300 + LARGE_GAP;  //value of y after drawing text + gap after text

        paint.setTextSize(MEDIUM_TEXT);
        canvas.drawText(resume.getJob(), 212, currentLeftY, paint);
        //add a large gap
        currentLeftY += LARGE_GAP;
        paint.setTextSize(LARGE_TEXT);
        canvas.drawRect(212, currentLeftY, paint.measureText(resume.getFirstName() + " " + resume.getLastName()),
                currentLeftY + 10, paint);
    }

    private void initialize() {
        paint.setColor(Color.parseColor("#00051A"));
        canvas.drawRect(0, 0, A4_WIDTH, 665, paint);

        drawNameAndJob();
    }
}