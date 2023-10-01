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

public class Template1 implements Template{
    private static final String TAG = "Template1";
    // Margin
    private int MARGIN = 90;
    private int LEFT_SIDE = 870;
    // gap in pixels
    private int SMALL_GAP = 40;
    private int MEDIUM_GAP = 50;
    private int LARGE_GAP = 100;
    // text size
    private int SMALL_TEXT = 40;
    private int MEDIUM_TEXT = 50;
    private int HEADING_SIZE = 60;
    private int LARGE_TEXT = 125;
    private int EXTRA_LARGE_TEXT = 185;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    //colors
    private String COLOR_WHITE = "#FFFFEF";
    private String COLOR_BLUE = "#00052A";

    //variables
    private Resume resume;
    private Context context;

    // current canvas in use
    private Typeface mSansRegular;
    private Typeface mSans700;
    private Canvas canvas = null;
    private Paint paint = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};

    public Template1(Context context, Resume resume) {
        this.context = context;
        this.resume = resume;
    }

    public PdfDocument createPdf() {
        // Create a new PDF Document
        PdfDocument document = new PdfDocument();

        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();

        //start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();

        mSansRegular = Typeface.createFromAsset(context.getAssets(), "open_sans_regular.ttf");
        mSans700 = Typeface.createFromAsset(context.getAssets(), "open_sans_700.ttf");

        //Start Drawing from here

        currentLeftY = MARGIN;
        initialSetup();

        drawNameAndObjective();

        drawEducation();

        if (isDone[EDUCATION_SECTION]) drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

        if (!resume.getImgLocation().isEmpty()) {
            Drawable d = Drawable.createFromPath(resume.getImgLocation());
            assert d != null;
            d.setBounds(MARGIN, MARGIN, LEFT_SIDE - MARGIN, 780);
            d.draw(canvas);
            currentLeftY += 780 + LARGE_GAP - MARGIN;
        }

        drawContact();

        drawSkill();

        if (isDone[SKILLS_SECTION]) drawLanguages();

        if (isDone[LANGUAGE_SECTION]) drawHobby();

        while (!isDone[SKILLS_SECTION] || !isDone[EDUCATION_SECTION] || !isDone[EXPERIENCE_SECTION]
                || !isDone[REFERENCE_SECTION] || !isDone[PROJECT_SECTION] || !isDone[LANGUAGE_SECTION]
                || !isDone[HOBBY_SECTION]) {
            document.finishPage(page);
            pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
            page = document.startPage(pageInfo);
            canvas = page.getCanvas();

            initialSetup();
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
                drawSkill();
            }

            if (isDone[SKILLS_SECTION] && !isDone[LANGUAGE_SECTION]) {
                drawLanguages();
            }

            if (isDone[SKILLS_SECTION] && isDone[LANGUAGE_SECTION] && !isDone[HOBBY_SECTION]) {
                drawHobby();
            }
        }

        document.finishPage(page);

        return document;
    }

    private void drawReference() {
        if(resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        if (isSpaceAvailable(resume.getReferenceList().get(sectionPosition[REFERENCE_SECTION]))) {
            if (createHeadingRS("REFERENCE")) currentRightY += MEDIUM_GAP;
        }

        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference reference = resume.getReferenceList().get(i);

            //Check if there is enough real state available for this to draw
            if (!isSpaceAvailable(reference)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
            StaticLayout staticLayout = new StaticLayout(reference.getName(), new TextPaint(paint),
                    400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            staticLayout = new StaticLayout(reference.getDesignation(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            paint.setTypeface(mSansRegular);
            staticLayout = new StaticLayout(reference.getEmail(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            paint.setTypeface(mSansRegular);
            staticLayout = new StaticLayout(reference.getPhone(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[REFERENCE_SECTION] = true;
    }

    private void drawProjects() {
        if(resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        if (isSpaceAvailable(resume.getProjectList().get(sectionPosition[PROJECT_SECTION]))) {
            if (createHeadingRS("PROJECT")) currentRightY += MEDIUM_GAP;
        }

        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            //Check if there is enough real state available for this to draw
            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
            StaticLayout staticLayout = new StaticLayout(project.getProjectRole(), new TextPaint(paint),
                    400, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            paint.setTypeface(mSansRegular);
            staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[PROJECT_SECTION] = true;
    }

    private void drawSkill() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        if (createHeadingLS("SKILLS")) currentLeftY += MEDIUM_GAP;

        paint.setTypeface(mSansRegular);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(skill)) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            paint.setColor(Color.parseColor(COLOR_WHITE));

            canvas.drawText(skill.getSkill(), MARGIN, currentLeftY + SMALL_TEXT, paint);

            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(LEFT_SIDE / 2, currentLeftY + 12, LEFT_SIDE - MARGIN,
                    currentLeftY + 30, 30, 30, paint);

            paint.setColor(Color.parseColor(COLOR_WHITE));
            canvas.drawRoundRect(LEFT_SIDE / 2, currentLeftY + 12, LEFT_SIDE / 2 + (345 * skill.getSkillLevel()) / 100,
                    currentLeftY + 30, 30, 30, paint);

            currentLeftY += SMALL_TEXT + MEDIUM_GAP;
        }
        currentLeftY += MEDIUM_GAP;
        isDone[SKILLS_SECTION] = true;
    }

    private void drawHobby() {
        if (resume.getLanguage().isEmpty()) {
            isDone[HOBBY_SECTION] = true;
            return;
        }

        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint),
                690, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((staticLayout.getHeight() + currentLeftY + MEDIUM_GAP) > A4_HEIGHT - MARGIN) {
            isDone[HOBBY_SECTION] = false;
            return;
        }

        if (createHeadingLS("HOBBIES")) currentLeftY += MEDIUM_GAP;

        paint.setColor(Color.parseColor(COLOR_WHITE));
        paint.setTypeface(mSansRegular);
        paint.setTextSize(SMALL_TEXT);

        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentLeftY += staticLayout.getHeight() + LARGE_GAP;
        isDone[HOBBY_SECTION] = true;
    }

    private void drawLanguages() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint),
                690, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        if ((currentLeftY + staticLayout.getHeight() + MEDIUM_GAP) > A4_HEIGHT - MARGIN) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        if (createHeadingLS("LANGUAGES")) currentLeftY += MEDIUM_GAP;

        paint.setColor(Color.parseColor(COLOR_WHITE));
        paint.setTypeface(mSansRegular);
        paint.setTextSize(SMALL_TEXT);

        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + LARGE_GAP;

        isDone[LANGUAGE_SECTION] = true;
    }

    private void drawContact() {
        if (!resume.getEmail().isEmpty() || !resume.getAddress().isEmpty() || !resume.getMobile().isEmpty()
                || !resume.getWebsite().isEmpty()) {
            if (createHeadingLS("CONTACT")) currentLeftY += MEDIUM_GAP;
        }

        if (!resume.getEmail().isEmpty()) {
            drawContactImage(R.drawable.ic_mail_blue, 250 - 125,
                    currentLeftY + 20, 250 - 40, currentLeftY + LARGE_TEXT - 40, resume.getEmail());
            currentLeftY += SMALL_GAP;
        }

        if (!resume.getMobile().isEmpty()) {
            drawContactImage(R.drawable.ic_phone_blue, 250 - 125, currentLeftY + 20,
                    250 - 40, currentLeftY + LARGE_TEXT - 40, resume.getMobile());
            currentLeftY += SMALL_GAP;
        }

        if (!resume.getAddress().isEmpty()) {
            drawContactImage(R.drawable.ic_loc_blue, 250 - 105, currentLeftY + 15,
                    250 - 50, currentLeftY + LARGE_TEXT - 30, resume.getAddress());
            currentLeftY += SMALL_GAP;
        }

        if (!resume.getWebsite().isEmpty()) {
            drawContactImage(R.drawable.ic_web_blue, 250 - 125, currentLeftY + 10,
                    250 - 40, currentLeftY + LARGE_TEXT - 30, resume.getWebsite());
            currentLeftY += SMALL_GAP;
        }
        currentLeftY += MEDIUM_GAP;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        if (isSpaceAvailable(resume.getExpList().get(sectionPosition[EXPERIENCE_SECTION]))) {
            if (createHeadingRS("EXPERIENCE")) currentRightY += MEDIUM_GAP;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }
            paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));

            String time = exp.getStarted() + " - " + exp.getEnded();
            canvas.drawText(time, LEFT_SIDE + MARGIN, currentRightY + SMALL_TEXT + 2, paint);

            StaticLayout staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            paint.setTypeface(mSansRegular);

            staticLayout = new StaticLayout(exp.getCompanyName(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawEducation() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        if (isSpaceAvailable(resume.getEduList().get(sectionPosition[EDUCATION_SECTION]))) {
            if (createHeadingRS("EDUCATION")) currentRightY += MEDIUM_GAP;
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            //Check if page have enough real state available
            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }
            paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));

            String time = edu.getStartYear() + " - " + edu.getEndYear();
            canvas.drawText(time, LEFT_SIDE + MARGIN, currentRightY + SMALL_TEXT + 2, paint);

            StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + 20;

            paint.setTypeface(mSansRegular);

            staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                    1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(LEFT_SIDE + MARGIN + 400 + MARGIN, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();

            currentRightY += staticLayout.getHeight() + MEDIUM_GAP;
        }
        currentRightY += SMALL_GAP;
        isDone[EDUCATION_SECTION] = true;
    }

    private boolean isSpaceAvailable(Reference reference) {
        int spaceRequired = currentRightY;

        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        StaticLayout staticLayout = new StaticLayout(reference.getDesignation(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight() + 20;

        paint.setTypeface(mSansRegular);
        staticLayout = new StaticLayout(reference.getEmail(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight() + 20;

        staticLayout = new StaticLayout(reference.getPhone(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentRightY;

        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        StaticLayout staticLayout = new StaticLayout(project.getProjectName(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight() + 20;

        paint.setTypeface(mSansRegular);
        staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Skill skill) {
        int spaceRequired = currentLeftY;

        spaceRequired += SMALL_TEXT;

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        int spaceRequired = currentRightY;

        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));

        StaticLayout staticLayout = new StaticLayout(exp.getJobTitle(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 20;

        paint.setTypeface(mSansRegular);
        staticLayout = new StaticLayout(exp.getCompanyName(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 20;

        staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Education edu) {
        int spaceRequired = currentRightY;

        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));

        StaticLayout staticLayout = new StaticLayout(edu.getCourse(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + 20;

        paint.setTypeface(mSansRegular);
        staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint),
                1000, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private void drawNameAndObjective() {
        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        paint.setTextSize(LARGE_TEXT);
        paint.setColor(Color.BLACK);
        float defaultLetterSapacing = paint.getLetterSpacing();
        paint.setLetterSpacing(0.3f);
        canvas.drawText(resume.getFirstName().toUpperCase(), LEFT_SIDE + MARGIN, MARGIN + LARGE_TEXT, paint);

        currentRightY = MARGIN + LARGE_TEXT;

        paint.setLetterSpacing(defaultLetterSapacing);
        paint.setTextSize(EXTRA_LARGE_TEXT);
        paint.setTypeface(mSans700);
        canvas.drawText(resume.getLastName().toUpperCase(), LEFT_SIDE + MARGIN, currentRightY + EXTRA_LARGE_TEXT, paint);

        currentRightY += EXTRA_LARGE_TEXT + MEDIUM_GAP;

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        paint.setColor(Color.parseColor(COLOR_BLUE));
        canvas.drawText(resume.getJob(), LEFT_SIDE + MARGIN, currentRightY + MEDIUM_TEXT, paint);

        currentRightY += MEDIUM_TEXT + SMALL_GAP;

        paint.setColor(Color.BLACK);
        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mSansRegular);
        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                1430, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(LEFT_SIDE + MARGIN, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        currentRightY += staticLayout.getHeight() + LARGE_GAP;
    }

    private void initialSetup() {
        paint.setColor(Color.parseColor(COLOR_WHITE));
        canvas.drawRect(0, 0, A4_WIDTH, A4_HEIGHT, paint);
        paint.setColor(Color.parseColor(COLOR_BLUE));
        canvas.drawRect(0, 0, 870, A4_HEIGHT, paint);
    }

    private void drawContactImage(int image, int left, int top, int right, int bottom, String text) {
        paint.setColor(Color.parseColor(COLOR_WHITE));
        canvas.drawRoundRect(-MARGIN, currentLeftY, 250, currentLeftY + LARGE_TEXT - 20, 30, 30, paint);

        Drawable d = context.getResources().getDrawable(image);
        d.setBounds(left, top, right, bottom);
        d.draw(canvas);

        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(text, new TextPaint(paint),
                500, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        if (staticLayout.getHeight() > 55) {
            canvas.translate(250 + MARGIN, currentLeftY);
        } else canvas.translate(250 + MARGIN, currentLeftY + 22);
        staticLayout.draw(canvas);
        canvas.restore();

        if ((currentLeftY + staticLayout.getHeight()) > (currentLeftY + LARGE_TEXT - 20))
            currentLeftY += staticLayout.getHeight();
        else currentLeftY += LARGE_TEXT - 20;
    }

    /**
     * This method create heading for the left section of the resume
     *
     * @param headingText text for the heading
     * @return return true if heading drawn successfully, else return false
     */
    private boolean createHeadingLS(String headingText) {
        if ((currentLeftY + HEADING_SIZE) > (A4_HEIGHT - MARGIN)) return false;

        currentLeftY += MEDIUM_GAP;
        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        paint.setTextSize(HEADING_SIZE + 10);
        paint.setColor(Color.parseColor(COLOR_WHITE));
        canvas.drawText(headingText, LEFT_SIDE / 2 - paint.measureText(headingText) / 2, currentLeftY, paint);

        return true;
    }

    /**
     * This method create heading for the right section of the resume
     *
     * @param headingText text for the heading
     * @return return true if heading drawn successfully, else return false
     */
    private boolean createHeadingRS(String headingText) {
        if ((currentRightY + HEADING_SIZE + 20) > (A4_HEIGHT - MARGIN)) return false;

        paint.setColor(Color.parseColor(COLOR_BLUE));
        paint.setTextSize(HEADING_SIZE);
        paint.setTypeface(Typeface.create(mSansRegular, Typeface.BOLD));
        canvas.drawRoundRect(LEFT_SIDE + MARGIN, currentRightY, LEFT_SIDE + MARGIN + paint.measureText(headingText) + 80,
                currentRightY + HEADING_SIZE + 40, 30, 30, paint);

        paint.setColor(Color.WHITE);
        canvas.drawText(headingText, LEFT_SIDE + MARGIN + 40, currentRightY + HEADING_SIZE + 12, paint);

        currentRightY += HEADING_SIZE + 40;

        return true;
    }
}
