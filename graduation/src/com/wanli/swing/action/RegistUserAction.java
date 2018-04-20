package com.wanli.swing.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;

import com.aliyuncs.exceptions.ClientException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.wanli.swing.entities.UserBean;
import com.wanli.swing.service.RegistService;
import com.wanli.utils.MailUtils;
import com.wanli.utils.Randomutil;
import com.wanli.utils.SmsUtils;

/**
 * ע���˺ź��һ������Action
 * @author wanli
 *
 */
public class RegistUserAction extends ActionSupport implements RequestAware, ModelDriven<UserBean>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//ע��RegisterService
	private RegistService registService;
	//ģ������ʹ�õĶ���
	private UserBean bean;

	public void setRegistService(RegistService registService) {
		this.registService = registService;
	}
	
	public void prepareAddUser() {
		bean = new UserBean();
	}

	@Override
	public UserBean getModel() {
		bean = new UserBean();
		return bean;
	}

	private Map<String, Object> request;
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	@Override
	public void prepare() throws Exception {}
	
	//ע���û�ִ�еķ���
	public String addUser() {
		
		registService.addUser(bean);
		return "adduser";
	}
	//ajax�����첽У���û����Ƿ����ִ�еķ���
	public String findByName() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String username = request.getParameter("username");
		//����registerService���в�ѯ
		UserBean existUser = registService.getByUsername(username);
		//���response������ҳ�����
		HttpServletResponse response = ServletActionContext.getResponse();
		//�����ַ������룬����ҳ���������
		response.setContentType("text/html;charset=UTF-8");
		//�ж�
		if (existUser != null) {
			//��ѯ�����û����û����Ѿ�����
			response.getWriter().println("<font color='red'>���ֻ����Ѿ�ע�ᣬ��ֱ�ӵ�¼</font>");
		} else {
			//û�в�ѯ�����û����û�������ʹ��
			response.getWriter().println("<font color='green'>�ֻ��ſ���ʹ��</font>");
		}
		return NONE;
	}
	
	//ajax�����첽У���ǳ��Ƿ����ִ�еķ���
	public String findByNickname() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String nickname = new String(request.getParameter("nickname").getBytes("iso8859-1"),"UTF-8");
		System.out.println(nickname);
		//����registerService���в�ѯ
		UserBean existUser = registService.getByNickname(nickname);
		//���response������ҳ�����
		HttpServletResponse response = ServletActionContext.getResponse();
		//�����ַ������룬����ҳ���������
		response.setContentType("text/html;charset=UTF-8");
		//�ж�
		if (existUser != null) {
			//��ѯ�����û����û����Ѿ�����
			response.getWriter().println("<font color='red'>�ǳ��Ѿ�����</font>");
		} else {
			//û�в�ѯ�����û����û�������ʹ��
			response.getWriter().println("<font color='green'>�ǳƿ���ʹ��</font>");
		}
		return NONE;
	}
	
	//ajax�����첽У�������Ƿ����ִ�еķ���
	public String findByEmail() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String email = request.getParameter("email");
		//����registerService���в�ѯ
		UserBean existUser = registService.getByEmail(email);
		//���response������ҳ�����
		HttpServletResponse response = ServletActionContext.getResponse();
		//�����ַ������룬����ҳ���������
		response.setContentType("text/html;charset=UTF-8");
		//�ж�
		if (existUser != null) {
			//��ѯ�����û����û����Ѿ�����
			response.getWriter().println("<font color='red'>�����Ѿ�����</font>");
		} else {
			//û�в�ѯ�����û����û�������ʹ��
			response.getWriter().println("<font color='green'>�������ʹ��</font>");
		}
		return NONE;
	}
	
	//�����ʼ���ȡ��֤��
	public String getVeriCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String toAddr = request.getParameter("email");
		String randomNum = Randomutil.getRandom();
//		MailUtils.sendMail(toAddr, randomNum);
		try {
			response.getWriter().println(randomNum);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��������ʧ��");
		}
		return NONE;
	}
	
	//���Ͷ�����֤��
	public String getSmsVeriCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String username = request.getParameter("username");
		String randomNum = Randomutil.getRandom();
//		try {
//			SmsUtils.sendSms(username, randomNum);
//		} catch (ClientException e) {
//			e.printStackTrace();
//			System.out.println("���ŷ���ʧ��");
//		}
		try {
			response.getWriter().println(randomNum);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("��������ʧ��");
		}
		return NONE;
	}
	
	//��������
	public String getBackPassword() {
		return "getBackPassword";
	}
	
	//�������óɹ�
	public String resetPasswordSucc() {
		HttpServletRequest request = ServletActionContext.getRequest();
		//parameter��ʾͨ�����ַ�ʽ�޸����룬�����ֻ��ź��������ַ�ʽ
		String parameter = request.getParameter("parameter");
		String password = request.getParameter("password");
		registService.updatePassword(parameter, password);
		return "resetPasswordSucc";
	}
}
