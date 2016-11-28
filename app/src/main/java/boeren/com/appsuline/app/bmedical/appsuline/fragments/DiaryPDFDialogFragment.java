package boeren.com.appsuline.app.bmedical.appsuline.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.utils.Constants;
import harmony.java.awt.Color;

/**
 * Created by Jamil on 22-1-2015.
 */
public class DiaryPDFDialogFragment  extends DialogFragment implements View.OnClickListener{

    private ImageView tvEmail;
    static private Button btnstartdate, btnenddate,btnshowpdf;
    static int startdate,startmonth,startyear,enddate,endmonth,endyear;

    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private final String dateFormatString="d MMMM yyyy";

    private SimpleDateFormat simpleDateFormat;

    private User activeUser;
    private String gender="";
    private boolean isAllFavourite;
    private String date;
    private int totalBloodEntries =0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        activeUser = BaseController.getInstance().getDbManager(getActivity()).getUserTable().getActiveUser();
        BaseController.getInstance().setActiveUser(activeUser);

        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_pdf_dialog, container);
        getDialog().setTitle(R.string.pdf);

        activeUser =BaseController.getInstance().getActiveUser();
        simpleDateFormat=new SimpleDateFormat(dateFormatString, Locale.getDefault());
        startDateCalendar=Calendar.getInstance();
        startDateCalendar.add(Calendar.MONTH,-1);

        endDateCalendar=Calendar.getInstance();

        view.findViewById(R.id.toggle_alldays).setOnClickListener(this);
        view.findViewById(R.id.toggle_onlyselecteddays).setOnClickListener(this);
        view.findViewById(R.id.btn_pdf_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_pdf_preview).setOnClickListener(this);
        tvEmail = (ImageView) view.findViewById(R.id.btnEmail);
        view.findViewById(R.id.btnEmail).setOnClickListener(this);

        view.findViewById(R.id.btn_pdf_startdate).setOnClickListener(this);
        view.findViewById(R.id.btn_pdf_enddate).setOnClickListener(this);

        setDateTexOnButton((Button) view.findViewById(R.id.btn_pdf_startdate), startDateCalendar);
        setDateTexOnButton((Button) view.findViewById(R.id.btn_pdf_enddate), endDateCalendar);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_pdf_cancel:
                getDialog().dismiss();
                break;
            case R.id.btn_pdf_preview:
                createPDF();
                break;
            case R.id.btnEmail:
                emailPDF();
                break;
            case R.id.btn_pdf_startdate:
                showDatePickerDialog(v,startDateCalendar);
                break;
            case R.id.btn_pdf_enddate:
                showDatePickerDialog(v,endDateCalendar);
                break;
            case R.id.toggle_onlyselecteddays:
                isAllFavourite=true;
                break;
            case R.id.toggle_alldays:
                isAllFavourite=false;
                break;


        }
    }

    public void createPDF()
    {
        Document doc = new Document();


        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appsuline";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, "dagboek.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(), R.drawable.pdf_header);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
            com.lowagie.text.Image myImg = com.lowagie.text.Image.getInstance(stream.toByteArray());
            myImg.setAlignment(com.lowagie.text.Image.ALIGN_LEFT);
            //myImg.setWidthPercentage(50);
            myImg.scalePercent(50);
              //add image to document
            doc.add(myImg);

            PdfPTable table = new PdfPTable(2);
            table.setSpacingBefore(10);

            Font paraFont= new Font(Font.HELVETICA) ;

            table.addCell(createCell("Datum:",paraFont));
            table.addCell(createCell(simpleDateFormat.format(startDateCalendar.getTime())+" t/m "+simpleDateFormat.format(endDateCalendar.getTime()),paraFont));

            table.addCell(createCell(getString(R.string.Title_naam)+":",paraFont));
            table.addCell(createCell(activeUser.getName(),paraFont));

            table.addCell(createCell(getString(R.string.Title_geslacht)+":",paraFont));
            table.addCell(createCell( gender = (activeUser.isMale()) ? "Male" : "Female",paraFont));

            table.addCell(createCell("Geboortedatum:",paraFont));


            table.addCell(createCell(activeUser.getDateOfBirth() ,paraFont));

//            table.addCell(createCell(getString(R.string.Title_leeftijd)+":",paraFont));
//            table.addCell(createCell(activeUser.getAge()+"",paraFont));

            table.addCell(createCell(getString(R.string.Title_naamverpleegkundig)+":",paraFont));
            table.addCell(createCell(activeUser.getNurseName(),paraFont));

            table.addCell(createCell(getString(R.string.pdf_email_nurse)+":",paraFont));
            table.addCell(createCell(activeUser.getNurseEmail(),paraFont));

            table.addCell(createCell(getString(R.string.title_pomp_spuit),paraFont));
            table.addCell(createCell((activeUser.isNeedle()) ? "Spuit":"Pomp",paraFont));

            table.addCell(createCell(getString(R.string.pdf_min_bloodsugarlevel)+":",paraFont));
            table.addCell(createCell(((float)activeUser.getMinBloodLevel())+"",paraFont));

            table.addCell(createCell(getString(R.string.pdf_max_bloodsugarlevel)+":",paraFont));
            table.addCell(createCell(((float)activeUser.getMaxBloodLevel())+"",paraFont));

            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            doc.add(table);



            Font font=new Font(Font.HELVETICA);
            font.setColor(Color.BLUE);
            font.setSize(15);
            font.setStyle(Font.BOLD);

            ArrayList<PDFTableInfo> dataTableInfoList=createDataTables();
            for (PDFTableInfo tableInfo:dataTableInfoList){

                Paragraph paragraph=new Paragraph(tableInfo.getDate(),font);
                paragraph.setSpacingBefore(5);
                paragraph.setSpacingAfter(5);
                doc.add(paragraph);

                doc.add(tableInfo.getDataTable());
            }

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            doc.close();
            //openPdfAppsuline();
            openPdf();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appsuline";
            File file = new File(path, "dagboek.pdf");
            if(file.exists())
                 tvEmail.setVisibility(View.VISIBLE);
            else
                tvEmail.setVisibility(View.INVISIBLE);
        }

    }

    private PdfPCell createCell(String cellText,Font font){
        Phrase phrase=new Phrase(cellText,font);


        PdfPCell cell=new PdfPCell(phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    private void emailPDF(){
        try {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appsuline";
        File file = new File(path, "dagboek.pdf");
        Intent emailIntent  = new Intent(Intent.ACTION_SEND);
        emailIntent .setType("plain/text");
        //String to[] = {activeUser.getNurseEmail()};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, new String[] { activeUser.getNurseEmail() });
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Dagboek van"+" "+activeUser.getName());
        startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (Throwable t) {
            Log.e("email method", "Request failed: " + t.toString());
        }
        finally {
            this.dismiss();
        }
    }

    // this opens the pdf in a separate pdf application
    private void openPdf() {
        try {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/appsuline";
        File file = new File(path, "dagboek.pdf");
        intent.setDataAndType( Uri.fromFile(file), "application/pdf" );
        startActivity(intent);
        } catch (Throwable t) {

            Log.e("openpdf method","Request failed: " + t.toString());

        }
    }


    private void setDateTexOnButton(Button button,Calendar dateCalendar){
        String formattedDate=simpleDateFormat.format(dateCalendar.getTime());
        button.setText(formattedDate);
    }

    private ArrayList<PDFTableInfo> createDataTables(){
        ArrayList<PDFTableInfo> dataTables=new ArrayList<PDFTableInfo>();
        SimpleDateFormat dateFormat=new SimpleDateFormat(Constants.DB_DATE_FORMAT,Locale.getDefault());
        SimpleDateFormat pdfDateFormat=new SimpleDateFormat("cccc dd MMMM",Locale.getDefault());
        long userId=BaseController.getInstance().getActiveUser().getUserId();
        ArrayList<String> favouriteDates=BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getFavouriteDates(userId);
        int numberOfDaysMovedBack=0;
        for(;!startDateCalendar.after(endDateCalendar);){
            date=dateFormat.format(endDateCalendar.getTime());
            if(isAllFavourite && !favouriteDates.contains(date)){
                endDateCalendar.add(Calendar.DAY_OF_YEAR,-1);
                numberOfDaysMovedBack++;
                continue;
            }
            ArrayList<LogBookEntry> entries=getEntriesOfDate(date);
            PDFTableInfo tableInfo=new PDFTableInfo();
            String dateString=pdfDateFormat.format(endDateCalendar.getTime());

            if(isAllFavourite || favouriteDates.contains(date)){
                dateString="! "+dateString+" !";
            }
            tableInfo.setDate(dateString);
            tableInfo.setDataTable(createDateTable(entries));
            dataTables.add(tableInfo);
            endDateCalendar.add(Calendar.DAY_OF_YEAR,-1);
            numberOfDaysMovedBack++;
        }
        endDateCalendar.add(Calendar.DAY_OF_YEAR,numberOfDaysMovedBack);
        return dataTables;
    }
    private PdfPTable createDateTable(ArrayList<LogBookEntry> entries){
        float totalCarbohydrates=0;
        float totalInsulin=0;
        float totalBlood=0;
        int bloodEntriesCount=1;
        PdfPTable table = new PdfPTable(4);
        table.setHorizontalAlignment(Rectangle.ALIGN_LEFT);
        Font paraFont= new Font(Font.HELVETICA);

        for (LogBookEntry entry:entries){
            if(entry.getEntryAmount()>0){
                PdfPCell firstCell=createCell(entry.getEntryTime()+" "+ entry.getEntryName(),paraFont);
                firstCell.setColspan(2);
                table.addCell(firstCell);
                table.addCell(createCell(((float)entry.getEntryAmount())+"",paraFont));
                table.addCell(createCell(entry.getEntryComment(),paraFont));
            }
            if(entry.getEntryType().equals(LogBookEntry.Type.BREAK_FAST)||
                    entry.getEntryType().equals(LogBookEntry.Type.LUNCH)||
                    entry.getEntryType().equals(LogBookEntry.Type.SNACK)||
                    entry.getEntryType().equals(LogBookEntry.Type.DINNER)||
                    entry.getEntryType().equals(LogBookEntry.Type.DRINK)){

                totalCarbohydrates+=entry.getEntryAmount();
            }else if(entry.getEntryType().equals(LogBookEntry.Type.INSULIN)){
                totalInsulin+=entry.getEntryAmount();
            }else if(entry.getEntryType().equals(LogBookEntry.Type.BLOOD)){
                totalBlood+=entry.getEntryAmount();
                bloodEntriesCount++;
            }
        }

        Font blueFont= new Font(Font.HELVETICA);
        blueFont.setColor(Color.BLUE);


        PdfPCell firstCell1=createCell("Totaal aantal koolhydraten",blueFont);
        firstCell1.setColspan(2);

        table.addCell(firstCell1);
        table.addCell(createCell(totalCarbohydrates+"",blueFont));
        table.addCell(createCell("",blueFont));

        PdfPCell firstCell2=createCell("Totaal aantal eenheden insuline",blueFont);
        firstCell2.setColspan(2);
        table.addCell(firstCell2);
        table.addCell(createCell(totalInsulin+"",blueFont));
        table.addCell(createCell("",blueFont));

        PdfPCell firstCell3=createCell("Gemiddelde bloedglucosewaarde",blueFont);
        firstCell3.setColspan(2);



        table.addCell(firstCell3);
       table.addCell(createCell(round2((totalBlood/getTotalBloodEntries(date)))+"",blueFont));

        table.addCell(createCell("",blueFont));

        return table;
    }
    private Double round2(Float val) {
        if(val>0)
        return new BigDecimal(val.toString()).setScale(2  , RoundingMode.HALF_EVEN).doubleValue();
        else
            return 0.0;
    }
    private ArrayList<LogBookEntry> getEntriesOfDate(String date){
        long userId=BaseController.getInstance().getActiveUser().getUserId();
        return BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getAllLogEntries(userId,date);
    }
    private int getTotalBloodEntries(String dateString){
    return  BaseController.getInstance().getDbManager(getActivity()).getLogBookTable().getLogBookBloodCount(activeUser.getUserId(), LogBookEntry.Type.BLOOD, dateString);
    }
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
    public void showDatePickerDialog(final View v,final Calendar dateCalendar) {
        new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateCalendar.set(Calendar.YEAR,year);
                dateCalendar.set(Calendar.MONTH,monthOfYear);
                dateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setDateTexOnButton((Button)v,dateCalendar);
            }
        },
                dateCalendar.get(Calendar.YEAR),
                dateCalendar.get(Calendar.MONTH),
                dateCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private class PDFTableInfo{
        private String date;
        private PdfPTable dataTable;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public PdfPTable getDataTable() {
            return dataTable;
        }

        public void setDataTable(PdfPTable dataTable) {
            this.dataTable = dataTable;
        }
    }
}