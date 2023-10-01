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

public class Template4 implements Template {
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
    private int MEDIUM_TEXT = 52;
    private int HEADING_SIZE = 90;
    private int LARGE_TEXT = 125;
    // current value of y
    private int currentRightY = 0;
    private int currentLeftY = 0;

    // Left side Color
    private String BLUE_COLOR = "#032DC0";

    // context field for later use
    private Context mContext;

    private Resume resume;

    // current canvas in use
    private Typeface mTypefaceNormal;
    private PdfDocument document = null;
    private Canvas canvas = null;
    private Paint paint = null;
    private PdfDocument.Page page = null;
    private int pageNumber = 1;

    // track what to print on the next page
    private boolean isDone[] = {false, false, false, false, false, false, false};
    private int sectionPosition[] = {0, 0, 0, 0, 0};


    public Template4(Context context, Resume resume) {
        mContext = context;
        this.resume = resume;
    }

    public PdfDocument createPdf() {
        // create typeface and initialize variable
        mTypefaceNormal = Typeface.createFromAsset(mContext.getAssets(), "roboto_slab_regular.ttf");
        // Create a new PDF Document
        document = new PdfDocument();
        //Create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, pageNumber++).create();
        //start and initialize the page and variables
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();


        //Start Drawing from here
        paint.setColor(Color.parseColor(BLUE_COLOR));
        canvas.drawRect(0, 0, 990, A4_HEIGHT, paint);

        currentLeftY = MARGIN;
        currentRightY = MARGIN;

        if (!resume.getImgLocation().isEmpty()) {
            Drawable d = Drawable.createFromPath(resume.getImgLocation());
            assert d != null;
            d.setBounds(90, currentLeftY, 900, 900);
            d.draw(canvas);
            currentLeftY += 900 + LARGE_GAP;
        }

        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));
        paint.setTextSize(LARGE_TEXT);
        paint.setLetterSpacing(0.1f);
        canvas.drawText(resume.getFirstName(), (990 / 2) - (paint.measureText(resume.getFirstName()) / 2),
                currentLeftY, paint);
        currentLeftY += LARGE_TEXT + SMALL_GAP;
        canvas.drawText(resume.getLastName(), (990 / 2) - (paint.measureText(resume.getLastName()) / 2),
                currentLeftY, paint);
        currentLeftY += LARGE_TEXT + SMALL_GAP;

        paint.setTypeface(mTypefaceNormal);
        paint.setTextSize(MEDIUM_TEXT + 20);
        canvas.drawText(resume.getJob(), (990 / 2) - (paint.measureText(resume.getJob()) / 2), currentLeftY, paint);
        currentLeftY += HEADING_SIZE;

        createContactAndAbout();

        drawEducation();

        if (isDone[EDUCATION_SECTION]) drawExperience();

        if (isDone[EXPERIENCE_SECTION]) drawProjects();

        if (isDone[PROJECT_SECTION]) drawReference();

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

            paint.setColor(Color.parseColor(BLUE_COLOR));
            canvas.drawRect(0, 0, 990, A4_HEIGHT, paint);

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

    private void drawEducation() {
        if (resume.getEduList().size() == 0) {
            isDone[EDUCATION_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        drawHeading("EDUCATION", R.drawable.grad_cap_black, 1090, currentRightY);
        currentRightY += HEADING_SIZE + MEDIUM_GAP;

        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[EDUCATION_SECTION]; i < resume.getEduList().size(); i++) {
            Education edu = resume.getEduList().get(i);

            if (!isSpaceAvailable(edu)) {
                isDone[EDUCATION_SECTION] = false;
                sectionPosition[EDUCATION_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));

            canvas.drawText(edu.getCourse(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(mTypefaceNormal);

            currentRightY += drawPhotoParagraph(edu.getStartYear() + " - " + edu.getEndYear(),
                    R.drawable.calendar_black, 1290, currentRightY) + SMALL_GAP;

            currentRightY += drawPhotoParagraph(edu.getInstitute(), R.drawable.school_black, 1290, currentRightY) + LARGE_GAP;
        }
        isDone[EDUCATION_SECTION] = true;
    }

    private void drawExperience() {
        if (resume.getExpList().size() == 0) {
            isDone[EXPERIENCE_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        drawHeading("EXPERIENCE", R.drawable.work_desk_black, 1090, currentRightY);
        currentRightY += HEADING_SIZE + MEDIUM_GAP;


        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[EXPERIENCE_SECTION]; i < resume.getExpList().size(); i++) {
            Experience exp = resume.getExpList().get(i);

            if (!isSpaceAvailable(exp)) {
                isDone[EXPERIENCE_SECTION] = false;
                sectionPosition[EXPERIENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));

            paint.setTextSize(SMALL_TEXT);
            canvas.drawText(exp.getJobTitle(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            paint.setTypeface(mTypefaceNormal);

            currentRightY += drawPhotoParagraph(exp.getStarted() + " - " + exp.getEnded(),
                    R.drawable.calendar_black, 1290, currentRightY) + SMALL_GAP;

            currentRightY += drawPhotoParagraph(exp.getCompanyName(), R.drawable.building_black, 1290, currentRightY) + SMALL_GAP;

            StaticLayout staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint),
                    1020, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(1290, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + LARGE_GAP;
        }
        isDone[EXPERIENCE_SECTION] = true;
    }

    private void drawProjects() {
        if (resume.getProjectList().size() == 0) {
            isDone[PROJECT_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        drawHeading("PROJECT", R.drawable.startup, 1090, currentRightY);
        currentRightY += HEADING_SIZE + MEDIUM_GAP;

        paint.setTextSize(SMALL_TEXT);
        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[PROJECT_SECTION]; i < resume.getProjectList().size(); i++) {
            Project project = resume.getProjectList().get(i);

            if (!isSpaceAvailable(project)) {
                isDone[PROJECT_SECTION] = false;
                sectionPosition[PROJECT_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));

            canvas.drawText(project.getProjectName(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT + SMALL_GAP;

            paint.setTypeface(mTypefaceNormal);

            canvas.drawText(project.getProjectRole(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                    1020, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(1290, currentRightY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentRightY += staticLayout.getHeight() + LARGE_GAP;
        }
        isDone[PROJECT_SECTION] = true;
    }

    private void drawReference() {
        if (resume.getReferenceList().size() == 0) {
            isDone[REFERENCE_SECTION] = true;
            return;
        }

        paint.setColor(Color.BLACK);
        drawHeading("REFERENCE", R.drawable.manager, 1090, currentRightY);
        currentRightY += HEADING_SIZE + MEDIUM_GAP;


        paint.setColor(Color.BLACK);

        for (int i = sectionPosition[REFERENCE_SECTION]; i < resume.getReferenceList().size(); i++) {
            Reference ref = resume.getReferenceList().get(i);

            if (!isSpaceAvailable(ref)) {
                isDone[REFERENCE_SECTION] = false;
                sectionPosition[REFERENCE_SECTION] = i;
                return;
            }

            paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));

            paint.setTextSize(SMALL_TEXT);
            canvas.drawText(ref.getName(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT + SMALL_GAP;

            paint.setTypeface(mTypefaceNormal);

            canvas.drawText(ref.getDesignation(), 1290, currentRightY, paint);
            currentRightY += SMALL_TEXT;

            currentRightY += drawPhotoParagraph(ref.getPhone(),
                    R.drawable.phone_black, 1290, currentRightY) + SMALL_GAP;

            currentRightY += drawPhotoParagraph(ref.getEmail(), R.drawable.email_black, 1290, currentRightY) + LARGE_GAP;
        }
        isDone[REFERENCE_SECTION] = true;
    }

    private void drawSkill() {
        if (resume.getSkillList().size() == 0) {
            isDone[SKILLS_SECTION] = true;
            return;
        }

        paint.setTypeface(mTypefaceNormal);
        paint.setColor(Color.WHITE);
        drawHeading("SKILLS", R.drawable.suitcase_white, 75, currentLeftY);
        currentLeftY += HEADING_SIZE + LARGE_GAP + SMALL_GAP;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);

        for (int i = sectionPosition[SKILLS_SECTION]; i < resume.getSkillList().size(); i++) {
            Skill skill = resume.getSkillList().get(i);

            if (currentLeftY + SMALL_TEXT + 40 >= A4_HEIGHT - MARGIN) {
                isDone[SKILLS_SECTION] = false;
                sectionPosition[SKILLS_SECTION] = i;
                return;
            }

            paint.setColor(Color.WHITE);
            canvas.drawText(skill.getSkill(), MARGIN, currentLeftY, paint);
            currentLeftY += 10 + SMALL_TEXT;

            paint.setColor(Color.parseColor("#050707"));
            canvas.drawRect(MARGIN, currentLeftY, MARGIN + 800, currentLeftY + 5, paint);
            paint.setColor(Color.WHITE);
            canvas.drawRect(MARGIN, currentLeftY, MARGIN + (800 * skill.getSkillLevel()) / 100,
                    currentLeftY + 5, paint);
            currentLeftY += 30 + MEDIUM_GAP;
        }
        isDone[SKILLS_SECTION] = true;
    }

    private void drawLanguages() {
        if (resume.getLanguage().isEmpty()) {
            isDone[LANGUAGE_SECTION] = true;
            return;
        }

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);
        paint.setColor(Color.WHITE);

        StaticLayout staticLayout = new StaticLayout(resume.getLanguage(), new TextPaint(paint),
                800, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);


        if ((staticLayout.getHeight() + currentLeftY + HEADING_SIZE + LARGE_GAP) > A4_HEIGHT - MARGIN ) {
            isDone[LANGUAGE_SECTION] = false;
            return;
        }

        drawHeading("LANGUAGES", R.drawable.language_white, 75, currentLeftY);
        currentLeftY += HEADING_SIZE + MEDIUM_GAP;

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

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);
        paint.setColor(Color.WHITE);

        StaticLayout staticLayout = new StaticLayout(resume.getHobby(), new TextPaint(paint),
                800, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);


        if ((staticLayout.getHeight() + currentLeftY + HEADING_SIZE + LARGE_GAP) > A4_HEIGHT - MARGIN ) {
            isDone[HOBBY_SECTION] = false;
            return;
        }

        drawHeading("HOBBIES", R.drawable.hobby_white, 75, currentLeftY);
        currentLeftY += HEADING_SIZE + MEDIUM_GAP;

        canvas.save();
        canvas.translate(MARGIN, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;

        isDone[HOBBY_SECTION] = true;
    }

    private void createContactAndAbout() {
        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);
        paint.setColor(Color.WHITE);

        StaticLayout staticLayout = new StaticLayout(resume.getObjective(), new TextPaint(paint),
                870, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(90, currentLeftY);
        staticLayout.draw(canvas);
        canvas.restore();
        currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;

        Drawable d;

        if (!resume.getMobile().isEmpty()) {
            d = mContext.getResources().getDrawable(R.drawable.telephone_white);
            d.setBounds(90, currentLeftY + 10, 140, currentLeftY + 55);
            d.draw(canvas);

            canvas.drawText("(882) 631 7151", 230, currentLeftY + MEDIUM_TEXT, paint);
            currentLeftY += MEDIUM_TEXT + SMALL_GAP;
        }

        if (!resume.getEmail().isEmpty()) {
            d = mContext.getResources().getDrawable(R.drawable.mail_white);
            d.setBounds(90, currentLeftY + 10, 140, currentLeftY + 55);
            d.draw(canvas);


            staticLayout = new StaticLayout(resume.getEmail(), new TextPaint(paint), 630,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(230, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }

        if (!resume.getWebsite().isEmpty()) {
            d = mContext.getResources().getDrawable(R.drawable.web_white);
            d.setBounds(90, currentLeftY + 10, 140, currentLeftY + 55);
            d.draw(canvas);


            staticLayout = new StaticLayout(resume.getWebsite(), new TextPaint(paint), 630,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(230, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + SMALL_GAP;
        }

        if (!resume.getAddress().isEmpty()) {
            d = mContext.getResources().getDrawable(R.drawable.location_white);
            d.setBounds(90, currentLeftY + 10, 140, currentLeftY + 70);
            d.draw(canvas);

            staticLayout = new StaticLayout(resume.getAddress(), new TextPaint(paint), 630,
                    Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
            canvas.save();
            canvas.translate(230, currentLeftY);
            staticLayout.draw(canvas);
            canvas.restore();
            currentLeftY += staticLayout.getHeight() + MEDIUM_GAP;
        }
    }

    private boolean isSpaceAvailable(Education edu) {
        // Checking for required space
        int spaceRequired = currentRightY + SMALL_TEXT;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);

        StaticLayout staticLayout = new StaticLayout(edu.getStartYear() + " - " + edu.getEndYear(),
                new TextPaint(paint), 900, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + SMALL_GAP;

        staticLayout = new StaticLayout(edu.getInstitute(), new TextPaint(paint), 900,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Experience exp) {
        // Checking for required space
        int spaceRequired = currentRightY + SMALL_TEXT;

        paint.setTextSize(SMALL_TEXT);
        paint.setTypeface(mTypefaceNormal);

        StaticLayout staticLayout = new StaticLayout(exp.getStarted() + " - " + exp.getEnded(),
                new TextPaint(paint), 900, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + SMALL_GAP;

        staticLayout = new StaticLayout(exp.getCompanyName(), new TextPaint(paint), 900,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight() + SMALL_GAP;

        staticLayout = new StaticLayout(exp.getJobDesc(), new TextPaint(paint), 1020,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Project project) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        spaceRequired+= SMALL_TEXT + SMALL_GAP;

        StaticLayout staticLayout = new StaticLayout(project.getProjectDescription(), new TextPaint(paint),
                1020, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        spaceRequired += staticLayout.getHeight();

        return spaceRequired <= (A4_HEIGHT - MARGIN);
    }

    private boolean isSpaceAvailable(Reference ref) {
        int spaceRequired = currentRightY;

        spaceRequired += SMALL_TEXT + SMALL_GAP;
        spaceRequired += SMALL_TEXT + SMALL_GAP;

        spaceRequired += SMALL_TEXT + SMALL_GAP;

        spaceRequired += SMALL_TEXT;

        return spaceRequired <= A4_HEIGHT - MARGIN;
    }

    private int drawPhotoParagraph(String text, int img, int x, int y) {
        Drawable d = mContext.getResources().getDrawable(img);
        d.setBounds(x, y, x + 60, y + MEDIUM_TEXT);
        d.draw(canvas);

        paint.setTextSize(SMALL_TEXT);
        StaticLayout staticLayout = new StaticLayout(text, new TextPaint(paint), 900,
                Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
        canvas.save();
        canvas.translate(x + 120, currentRightY);
        staticLayout.draw(canvas);
        canvas.restore();

        return staticLayout.getHeight();
    }

    private void drawHeading(String text, int img, int x, int y) {
        Drawable d = mContext.getResources().getDrawable(img);
        d.setBounds(x, y, x + 100, y + HEADING_SIZE);
        d.draw(canvas);

        paint.setTextSize(MEDIUM_TEXT);
        paint.setTypeface(Typeface.create(mTypefaceNormal, Typeface.BOLD));
        canvas.drawText(text, x + 200, y + MEDIUM_TEXT + ((HEADING_SIZE - MEDIUM_TEXT) / 2), paint);
    }
}
