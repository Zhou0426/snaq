package cn.jagl.util;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class ExcelToPDF {
	public ExcelToPDF(){}
	private static final int wdFormatPDF = 17;
	private static final int xlTypePDF = 0;
	private static final int ppSaveAsPDF = 32;
	private static final int msoTrue = -1;
	private static final int msofalse = 0;
	public boolean convert2PDF(String inputFile, String pdfFile) throws Exception{
		String suffix =  getFileSufix(inputFile);
		File file = new File(inputFile);
		if(!file.exists()){
			System.out.println("No Exit.");
			return false;
		}
		if(suffix.equals("pdf")){
			System.out.println("PDF not need to convert!");
			return false;
		}
		if(suffix.equals("xls")||suffix.equals("xlsx")){
			return excel2PDF(inputFile,pdfFile);
		}else{
			System.out.println("Error!");
			return false;
		}
	}
	public static String getFileSufix(String fileName){
		int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
	}

	public boolean excel2PDF(String inputFile,String pdfFile) throws Exception{
		ActiveXComponent app = new ActiveXComponent("Excel.Application");
		app.setProperty("Visible", false);
		Dispatch excels = app.getProperty("Workbooks").toDispatch();
		Dispatch excel = Dispatch.call(excels,
									"Open",
									inputFile,
									false,
									true
									).toDispatch();
		Dispatch.call(excel,
					"ExportAsFixedFormat",
					xlTypePDF,		
					pdfFile
					);
		Dispatch.call(excel, "Close",false);
		app.invoke("Quit");
		return true;		
	}
}
