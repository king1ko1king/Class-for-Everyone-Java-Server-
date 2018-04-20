package com.wanli.swing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.wanli.utils.StaticVariable;

public class Test {

    static{
     System.setProperty("org.eclipse.swt.browser.XULRunnerPath", "e:\\xulrunner"); 
    } 
    public static void main(String[] args) {

        Display display = new Display (); 
        final Shell shell = new Shell (display); 
        FillLayout layout = new FillLayout(); 
        shell.setLayout(layout); 

        Browser browser = new Browser(shell, SWT.MOZILLA);  //1
        browser.addTitleListener(new TitleListener(){  //2
            public void changed(TitleEvent event) { 
                shell.setText(event.title); 
            } 
        }); 
        browser.setUrl("www.baidu.com"); //3
        shell.open (); 
        while (!shell.isDisposed ()) { 
            if (!display.readAndDispatch ()) display.sleep (); 
        } 
        display.dispose ();
    }
    
//    boolean OpenTextFile() {
//		StaticVariable.index = 1;
//		// ����Ի�������Ϊ����
//		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
//		// ���öԻ���򿪵��޶�����
//		dialog.setFilterExtensions(new String[] { " *.txt", "*.doc", "*.docx"});
//		// �򿪶Ի��򣬲����ش��ļ���·��
//		String openFile = dialog.open();
//		if (openFile == null) {
//			return false;
//		}
//		// ��ָ�����ļ�
//		file = new File(openFile);
//		String fileExtension = file.getName().substring(file.getName().indexOf("."));
//		try {
//			//��ȡ��չ��Ϊ.doc��word�ĵ�
//			HWPFDocument doc = null;
//			//��ȡ��չ��Ϊ.docx��word�ĵ�
//			XWPFDocument docx = null;
//			XWPFWordExtractor extractor = null;
//			//��ȡ��չ��Ϊ.txt���ĵ�
//			StringBuffer sb = null;
//			String fileText = null;
//			BufferedReader reader = null;
//			FileInputStream in = null;
//			if (fileExtension.equals(".doc")) {
//				in = new FileInputStream(file);
//				doc = new HWPFDocument(in);
//				fileText = doc.getDocumentText();
//				StaticVariable.questions = fileText.split(new String("#\\^"));
//				StaticVariable.text.setText(StaticVariable.questions[1]);
//			} else if (fileExtension.equals(".docx")) {
//				in = new FileInputStream(file);
//				docx = new XWPFDocument(in);
//				extractor = new XWPFWordExtractor(docx);
//				fileText = extractor.getText();
//				StaticVariable.questions = fileText.split(new String("#\\^"));
//				StaticVariable.text.setText(StaticVariable.questions[1]);
//			}
//			else {
//				// ��ȡ�ļ�
//				FileReader fileReader = new FileReader(file);
//				// ���ַ������ַ����뻺����
//				reader = new BufferedReader(fileReader);
//				sb = new StringBuffer();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					// ͨ�� append() ����ʵ�ֽ��ַ�����ӵ��ַ���������
//					sb.append(line);
//					sb.append("\r\n");
//				}
//				// ����ȡ���ļ����ַ���"#^"�ֿ�����Ϊ'^'��ת���ַ�������ǰ��Ҫ��\\
//				StaticVariable.questions = sb.toString().split(new String("#\\^"));	
//				StaticVariable.text.setText(StaticVariable.questions[1]);
//			}
//			System.out.println(StaticVariable.questions.length);
//			int num = StaticVariable.questions.length - 1;
//			String fileName = file.getName();
//			shell.setText(APPNAME + "-" + file);
//			StaticVariable.tableName = fileName.substring(0, fileName.indexOf("."));
//			dbService.createTable(num, StaticVariable.tableName);
//			
//			//���ļ������ð�ť
////			first.setEnabled(true);
////			previous.setEnabled(true);
////			next.setEnabled(true);
////			last.setEnabled(true);
//			if (reader != null) {
//				reader.close();				
//			}
//			if (in != null) {
//				in.close();
//			}
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
}
