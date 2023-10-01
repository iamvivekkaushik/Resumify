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


public class Template10 implements Template {
    private static final String TAG = "Template10";
    // Margin
    private int MARGIN = 90;
    private int LEFT_SIDE = 870;
    // gap in pixels
    private int SMALL_GAP = 35;
    private int MEDIUM_GAP = 50;
    private int LARGE_GAP = 70;
    // text size
    private int SMALL_TEXT = 38;
    private int MEDIUM_TEXT = 40;
    private int HEADING_SIZE = 38;
    private int LARGE_TEXT = 75;
    private int EXTRA_LARGE_TEXT = 100;
    // current value of y
    private int currentPosition = 0;

    //variables
    private Resume resume;
    private Context context;

    // current canvas in use
    private Typeface robotoMedium;
    private Typeface robotoRegular;
    private Canvas canvas = null;
    private Paint paint = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template10(Context context, Resume resume) {
        this.context = context;
        this.resume = resume;
    }

    @Override
    public PdfDocument createPdf() {
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "roboto_regular.ttf");
        robotoMedium = Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
        // Create a new PDF Document
        PdfDocument document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
        //start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        currentPosition = MARGIN;
        paint.setColor(Color.BLACK);

        //Starts Here
        drawUpperSection();

        drawObjective();

        drawSkills();

        if (isDone[SKILLS_SECTION]) drawEducation();

        if (isDone[EDUCATION_SECTION]) drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProject();

        if (isDone[PROJECT_SECTION]) drawReference();


        while (!isDone[SKILLS_SECTION] || !isDone[EDUCATION_SECTION] || !isDone[EXPERIENCE_SECTION]
                || !isDone[REFERENCE_SECTION] || !isDone[PROJECT_SECTION]) {
            document.finishPage(page);
            pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();

            currentPosition = MARGIN;

            if (!isDone[SKILLS_SECTION]) {
                drawSkills();
            }

            if (isDone[SKILLS_SECTION] && !isDone[EDUCATION_SECTION]) {
                drawEducation();
            }

            if (isDone[EDUCATION_SECTION] && !isDone[EXPERIENCE_SECTION]) {
                drawExperience();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && !isDone[PROJECT_SECTION]) {
                drawProject();
            }

            if (isDone[EDUCATION_SECTION] && isDone[EXPERIENCE_SECTION] && isDone[PROJECT_SECTION]
                    && !isDone[REFERENCE_SECTION]) {
                drawReference();
            }
        }
            //Ends Here
        document.finishPage(page);
        return document;
    }

    private void drawUpperSection() {
        paint.setTypeface(Typeface.create(robotoMedium, Typeface.BOLD));
        paint.setTextSize(LARGE_TEXT);

        String name = resume.getFirstName() + " " + resume.getLastName();
        canvas.drawText(name, (A4_WIDTH/2) - (paint.measureText(name)/2), currentPosition + LARGE_TEXT , paint);
        currentPosition += LARGE_TEXT + SMALL_GAP;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(robotoRegular);
        if (!resume.getAddress().isEmpty()) {
            canvas.drawText(resume.getAddress(), (A4_WIDTH/2) - (paint.measureText(resume.getAddress()) / 2),
                    currentPosition + SMALL_TEXT, paint);
            currentPosition += SMALL_TEXT + SMALL_GAP;
        }
        if (!resume.getAddress().isEmpty()) {
            canvas.drawText(resume.getMobile(), MARGIN, currentPosition + SMALL_TEXT, paint);

            if (!resume.getAddress().isEmpty()) {
                canvas.drawText(resume.getEmail(), A4_WIDTH - MARGIN - paint.measureText(resume.getEmail()),
                        currentPosition + SMALL_TEXT, paint);
            }
            currentPosition += SMALL_TEXT + SMALL_GAP;
        }

        paint.setStrokeWidth(2f);
        canvas.drawLine(MARGIN, currentPosition, A4_WIDTH - MARGIN, currentPosition, paint);
        currentPosition += MEDIUM_GAP;
    }

    private void drawObjective() {
        if (resume.getObjective().isEmpty()) {
            return;
        }

        drawHeading("Objective");

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(robotoRegular);
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                2300, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(MARGIN, currentPosition);
        staticLayout.draw(canvas);
        canvas.restore();

        currentPosition += staticLayout.getHeight() + MEDIUM_GAP;
    }

    private void drawSkills() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        drawHeading("Skills");

        boolean drawLeft = true;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(robotoRegular);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            if (!isSpaceAvailable(skill)) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            if (drawLeft) {
                canvas.drawCircle(4 * MARGIN, currentPosition + 23, 7, paint);
                canvas.drawText(skill.getSkill(), 4 * MARGIN + 30, currentPosition + SMALL_TEXT, paint);
            }

            if (!drawLeft) {
                canvas.drawCircle(1240 + 4 * MARGIN, currentPosition + 23, 7, paint);
                canvas.drawText(skill.getSkill(), 1270 + 4 * MARGIN, currentPosition + SMALL_TEXT, paint);

                currentPosition += SMALL_TEXT + SMALL_GAP;
            }

            drawLeft = !drawLeft;
        }
        currentPosition += MEDIUM_GAP;
        isDone[SKILLS_SECTION] = true;
    }

    private void drawEducation() {
        if(resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        drawHeading("Professional Education");

        paint.setTextSize(SMALL_TEXT);
        boolean drawLeft = true;

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            if (drawLeft) {
                int currentPosition = this.currentPosition;
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(edu.getCourse() + ",  ", 2 *  MARGIN, currentPosition + SMALL_TEXT, paint);
                float textWidth = 2 * MARGIN + paint.measureText(edu.getCourse() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(edu.getInstitute(), 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + MEDIUM_GAP;

                if (i == resume.getEduList().size() - 1) {
                    this.currentPosition = currentPosition;
                }
            }

            if (!drawLeft) {
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(edu.getCourse(), 1400, currentPosition + SMALL_TEXT, paint);
                float textWidth = 1400 + paint.measureText(edu.getCourse() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(edu.getStartYear() + " - " + edu.getEndYear(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(edu.getInstitute(), 1400, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + MEDIUM_GAP;
            }

            drawLeft = !drawLeft;
        }
        currentPosition += SMALL_GAP;
        isDone[EDUCATION_SECTION] = true;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        drawHeading("Professional Experience");

        boolean drawLeft = true;
        int leftHeight = 0;
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }



            if (drawLeft) {
                int currentPosition = this.currentPosition;

                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(exp.getJobTitle() + ", ", 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                float textWidth = 2 * MARGIN + paint.measureText(exp.getJobTitle() + ",   ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(exp.getCompanyName(), 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                        880, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
                canvas.save();
                canvas.translate( 2 * MARGIN, currentPosition);
                staticLayout.draw(canvas);
                canvas.restore();
                currentPosition += staticLayout.getHeight() + MEDIUM_GAP;

                leftHeight = currentPosition;

                if (i == resume.getExpList().size() - 1) {
                    this.currentPosition = currentPosition;
                }
            }

            if (!drawLeft) {
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(exp.getJobTitle() + ", ", 1400, currentPosition + SMALL_TEXT, paint);
                float textWidth = 1400 + paint.measureText(exp.getJobTitle() + ",   ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(exp.getStarted() + " - " + exp.getEnded(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(exp.getCompanyName(), 1400, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                        880, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
                canvas.save();
                canvas.translate(1400, currentPosition);
                staticLayout.draw(canvas);
                canvas.restore();

                if (leftHeight > currentPosition + staticLayout.getHeight() + MEDIUM_GAP) currentPosition = leftHeight;
                else currentPosition += staticLayout.getHeight() + MEDIUM_GAP;
            }

            drawLeft = !drawLeft;
        }
        currentPosition += SMALL_GAP;
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawProject() {
        if (resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        drawHeading("Projects");

        boolean drawLeft = true;
        int leftHeight = 0;
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            if (drawLeft) {
                int currentPosition = this.currentPosition;
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(project.getProjectName() + ",  ", 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                float textWidth = 2 * MARGIN + paint.measureText(project.getProjectName() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(project.getProjectRole(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                        880, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
                canvas.save();
                canvas.translate(2 * MARGIN, currentPosition);
                staticLayout.draw(canvas);
                canvas.restore();

                currentPosition += staticLayout.getHeight() + MEDIUM_GAP;

                leftHeight = currentPosition;
                if (i == resume.getProjectList().size() - 1) {
                    this.currentPosition = currentPosition;
                }
            }

            if (!drawLeft) {
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(project.getProjectName() + ",  ", 1400, currentPosition + SMALL_TEXT, paint);
                float textWidth = 1400 + paint.measureText(project.getProjectName() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(project.getProjectRole(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                        880, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
                canvas.save();
                canvas.translate(1400, currentPosition);
                staticLayout.draw(canvas);
                canvas.restore();

                if (leftHeight > currentPosition + staticLayout.getHeight() + MEDIUM_GAP) currentPosition = leftHeight;
                else currentPosition += staticLayout.getHeight() + MEDIUM_GAP;
            }

            drawLeft = !drawLeft;
        }
        isDone[PROJECT_SECTION] = true;
        currentPosition += SMALL_GAP;
    }

    private void drawReference() {
        if (resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        drawHeading("References");

        boolean drawLeft = true;
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            if(!isSpaceAvailable(ref)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            if (drawLeft) {
                int currentPosition = this.currentPosition;
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(ref.getName() + ",  ", 2 *  MARGIN, currentPosition + SMALL_TEXT, paint);
                float textWidth = 2 * MARGIN + paint.measureText(ref.getName() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(ref.getDesignation(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(ref.getPhone(), 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(ref.getEmail(), 2 * MARGIN, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + MEDIUM_GAP;

                if (i == resume.getEduList().size() - 1) {
                    this.currentPosition = currentPosition;
                }
            }

            if (!drawLeft) {
                paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

                canvas.drawText(ref.getName(), 1400, currentPosition + SMALL_TEXT, paint);
                float textWidth = 1400 + paint.measureText(ref.getName() + ",  ");

                paint.setTypeface(robotoRegular);
                canvas.drawText(ref.getDesignation(), textWidth, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(ref.getPhone(), 1400, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + SMALL_GAP;

                canvas.drawText(ref.getEmail(), 1400, currentPosition + SMALL_TEXT, paint);
                currentPosition += SMALL_TEXT + MEDIUM_GAP;
            }

            drawLeft = !drawLeft;
        }
        currentPosition += SMALL_GAP;
        isDone[REFERENCE_SECTION] = true;
    }

    private boolean isSpaceAvailable(Skill skill) {
        int spaceRequired = currentPosition;

        spaceRequired += SMALL_TEXT;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentPosition;

        spaceRequired += SMALL_TEXT + SMALL_GAP;
        spaceRequired += SMALL_TEXT;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentPosition;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                2300, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentPosition;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                880, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRequired = currentPosition;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        spaceRequired += SMALL_TEXT;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private void drawHeading(String heading) {
        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(robotoRegular, Typeface.BOLD));

        paint.setUnderlineText(true);
        canvas.drawText(heading, (A4_WIDTH/2) - (paint.measureText(heading)/2), currentPosition
                + MEDIUM_TEXT, paint);
        paint.setUnderlineText(false);
        currentPosition += MEDIUM_TEXT + SMALL_GAP;
    }
}
