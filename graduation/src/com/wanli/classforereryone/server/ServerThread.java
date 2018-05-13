package com.wanli.classforereryone.server;

import static org.hamcrest.CoreMatchers.startsWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.aliyuncs.exceptions.ClientException;
import com.wanli.swing.Mmmm;
import com.wanli.swing.entities.ChoiceQuestion;
import com.wanli.swing.entities.OnlineUser;
import com.wanli.swing.frame.MessagePOP_UP;
import com.wanli.swing.service.DBService;
import com.wanli.swing.service.DBServiceUser;
import com.wanli.utils.MailUtils;
import com.wanli.utils.Randomutil;
import com.wanli.utils.SmsUtils;
import com.wanli.utils.StaticVariable;

/**
 * 每个客户的专属线程，可以接收客户端发送的消息，也可以向客户端发送消息
 * @author wanli
 *
 */
public class ServerThread implements Runnable {

	// 定义当前线程所处理的Socket
	private static Socket s = null;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;
	// 接收连接上服务端的客户端IP地址
	private static String ipAddress = "";
	// 存储学生提出的问题
	private String question;
	// 存储所有教室
	private StringBuilder allClass = new StringBuilder();
	// 操作用户数据库
	private DBServiceUser dbServiceUser;
	// 操作成绩表数据库
	private DBService dbService;
	// 生成的6位验证码
	private String randomNum;
	// 登录的用户名
	private static String userName;
	
	public ServerThread(Socket s) throws IOException {
		this.s = s;
		// 初始化该Socket对应的输入流
		br = new BufferedReader(new InputStreamReader(s.getInputStream(), "utf-8"));
		dbServiceUser = new DBServiceUser();
		dbService = new DBService();
		
	}
	
	@Override
	public void run() {
		
		String content = null;
		ipAddress = s.getInetAddress().toString().substring(1);
		// 采用循环不断从Socket中读取客户端发送过来的数据
		while ((content = readFromClient()) != null) {
			
			String[] info = content.split("#\\^");
			System.out.println(Arrays.toString(info));
			switch(info[0]) {
				// 读取注册信息
				// 客户端发送过来的字符串格式：操作编号,手机号,昵称,密码,邮箱,ip地址
				case "1":
					
					if (dbServiceUser.addUser(info)) {
						// 注册成功
						sendToClient("1", info[5]);
					} else {
						// 注册失败
						sendToClient("1-false", info[5]);
					}
					break;
				// 读取登录信息
				// 客户端发送过来的字符串格式：操作编号,手机或邮箱,密码,ip地址
				case "2":
//					System.out.println(Arrays.toString(info));
					userName = dbServiceUser.getUserByNameAndPassword(info[1], info[2]);
					if (userName != null) {
						// 有客户端连接就把连接的客户端使用map存储
						System.out.println("客户端连接成功！！！");
//						try {
//							StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
//						} catch (IOException e) {
//							System.out.println("登录失败...");
//							e.printStackTrace();
//						}
						
						// 登录成功，把昵称发送给客户端
						sendToClient("2#^" + userName, info[3]);
//						System.out.println("登录了。。。。。");
	
					} else {
						// 登录失败
						sendToClient("2-false", info[3]);
					}
					break;
				// 读取回答问题的信息
				// 客户端发送过来的字符串格式：操作编号,昵称,内容,ip地址
				case "3":
//					StaticVariable.correct.clear();
//					StaticVariable.error.clear();
//					StaticVariable.unResponse.clear();
					Display.getDefault().syncExec(new Runnable() {
						
						@Override
						public void run() {
							// StaticVariable.questionSelect.getSelectionIndex() > 0表示已经选择题目，可以进行答题
							if (StaticVariable.questionSelect.getSelectionIndex() > 0) {
								String[] userAnswers = info[2].split(" ");
								// 保存用户回答的答案
								List<String> answerList = new ArrayList<>();
								StaticVariable.answers.put(info[1], "");
								StaticVariable.answers.replace(info[1], info[2]);
								int index = StaticVariable.questionSelect.getSelectionIndex();
//								String[] strs = StaticVariable.questionsMap.get(Integer.toString(index)).split(",");
								String[] strs = StaticVariable.questionsList.get(index - 1).split("#\\^");
								if (strs[0].equals("fill_in_the_blanks")) {
									// answers.length > 1 表示有多个答案
									if ((strs.length - 2) > 1) {
										// 多个答案用空隔分开，过滤掉多余的空隔
										for (int i = 0; i < userAnswers.length; i++) {
											if (!userAnswers[i].equals("")) {
												answerList.add(userAnswers[i]);
											}
										}
//										// 初始化存储正确答案个数，错误答案个数，未回答个数的list
//										for (int i = 2; i < strs.length; i++) {
//											StaticVariable.correct.add(new Integer(0));
//											StaticVariable.error.add(new Integer(0));
//											StaticVariable.unResponse.add(new Integer(0));					
//										}
										// 分别计算回答正确和回答错误的个数
										for (int i = 2; i < strs.length; i++) {
											if (answerList.size() > 1) {
												// 判断正确与否
												if (strs[i].equals(answerList.get(i - 2))) {
													// 回答正确
													int value = StaticVariable.correct.get(i - 2).intValue();
													value++;
													StaticVariable.correct.set(i - 2, new Integer(value));
												} else {
													// 回答错误
													int value = StaticVariable.error.get(i - 2).intValue();
													value++;
													StaticVariable.error.set(i - 2, new Integer(value));
												}
											} else {
												for (int j = 2; j < strs.length; j++) {
													if (j == 2) {
														if (strs[j].equals(answerList.get(j - 2))) {
															// 回答正确
															int value = StaticVariable.correct.get(j - 2).intValue();
															value++;
															StaticVariable.correct.set(j - 2, new Integer(value));
														} else {
															// 回答错误
															int value = StaticVariable.error.get(j - 2).intValue();
															value++;
															StaticVariable.error.set(j - 2, new Integer(value));
														}	
													} else {
														int value = StaticVariable.unResponse.get(j - 2).intValue();
														value++;
														StaticVariable.unResponse.set(j - 2, new Integer(value));
													}
												}
												break;
											}
										}
									} 
								} else {
									// 只有一个答案
									answerList.add(info[2]);
									// 判断正确与否
									if (answerList.get(0).equals(strs[2])) {
										// 回答正确
										int value = StaticVariable.correct.get(0).intValue();
										value++;
										StaticVariable.correct.set(0, new Integer(value));
									} else {
										// 回答错误
										int value = StaticVariable.error.get(0).intValue();
										value++;
										StaticVariable.error.set(0, new Integer(value));
									}
									if (StaticVariable.options.get(info[2]) != null) {
										int option = StaticVariable.options.get(info[2]);
										option++;
										StaticVariable.options.replace(info[2], Integer.valueOf(option));
									}
//									System.out.println(StaticVariable.options);
								}
//								System.out.println("StaticVariable.answers:" + StaticVariable.answers);
//								System.out.println("answerList:" + answerList);
//								System.out.println("StaticVariable.correct:" + StaticVariable.correct);
//								System.out.println("StaticVariable.error:" + StaticVariable.error);
							}
						}
					});
//					System.out.println(info[2]);
					break;
				// 读取提问的信息，并且弹框提示有学生提问
				// 客户端发送过来的字符串格式：操作编号,昵称,内容
				case "4":
					// question为最终显示的提问的内容
					question = info[1] + ":::" + info[2]; 
					new Thread(new ListenningMessage(question)).start();
//					StaticVariable.users.get(ipAddress).setContent(info[2]);
//					System.out.println(info[0] + "," + info[1] + "," + info[2]);				
					break;
				// 忘记密码，重置密码
				// 客户端发送过来的字符串格式：操作编号,手机或邮箱,密码,ip地址
				case "5":
					// 若密码为null，则表示是查询手机号或邮箱是否已经注册过
					if (info[2].equals("null")) {
						if (dbServiceUser.getByUsername(info[1])) {
							// 如果找到该用户，说明已经有注册过，接着发送验证码
							boolean result = info[1].matches("[0-9]+");
							randomNum = Randomutil.getRandom();
							if (result) {
								try {
									SmsUtils.sendSms(info[1], randomNum);
								} catch (ClientException e) {
									System.out.println("短信发送失败");
									e.printStackTrace();
								}
							} else {
								MailUtils.sendMail(info[1], randomNum);
							}
							// 注册过给客户端发送提示信息，并把验证码发送给客户端
							sendToClient("5#^" + randomNum, info[3]);
						} else {
							// 未注册过给客户端发送失败提示
							sendToClient("5-false", info[3]);
						}
					} else {
						// 若密码不是null，则表示是修改密码
						dbServiceUser.updatePassword(info[1], info[2]);
						sendToClient("5", info[3]);
					}
					break;
				// 向客户端发送教室信息
				// 请求格式：6，ip地址
				case "6":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							if (StaticVariable.rooms.size() > 0) {
								allClass.setLength(0);
								allClass.append("6");
//						System.out.println(StaticVariable.rooms.size());
//								for (int i = 0; i < StaticVariable.rooms.size(); i++) {
//									if (i == 0) {
//										allClass.append("6#^" + StaticVariable.rooms.get(i).getText());	
//									} else {
////								System.out.println(StaticVariable.rooms.get(i).getText());
//										allClass.append("#^" + StaticVariable.rooms.get(i).getText());
//									}
//								}
								for (Map.Entry<String, TreeItem> item: StaticVariable.rooms.entrySet()) {
									allClass.append("#^" + item.getValue().getText());
								}
								
//								System.out.println(allClass.toString());
								// 向客户端发送教室信息
								sendToClient(allClass.toString(), info[1]);
							} else {
								sendToClient("6-false", info[1]);
							}
							
						}
					});
					break;
				// 获取题目
				// 请求格式：编号，ip地址
				case "7":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							int index = StaticVariable.questionSelect.getSelectionIndex();
							System.out.println(index);
							if (index > 0) {
//						sendToClient(StaticVariable.questionsMap.get(Integer.toString(index)));
								sendToClient(StaticVariable.questionsList.get(index - 1), info[1]);
							} else {
								sendToClient("7-false", info[1]);
							}							
						}
					});
					break;
				// 校验客户端发送过来的验证码是否正确
				// 请求格式：编号，验证码，ip地址
				case "8":
					if (info[1].equals(randomNum)) {
						sendToClient("8", info[2]);
					} else {
						sendToClient("8-false", info[2]);
					}
					break;
				// 接收客户端发送过来的加入教室的信息
				// 请求格式：编号，教室名称，ip地址
				case "9":
					Display.getDefault().syncExec(new Runnable(){
						public void run() {
							TreeItem treeItem = new TreeItem(StaticVariable.rooms.get(info[2]), SWT.NONE);
							treeItem.setText(info[1]);
							StaticVariable.onlineUsers.put(ServerThread.getIpAddress(), treeItem);
							try {
								StaticVariable.users.put(s.getInetAddress().toString().substring(1), new OnlineUser(s));
								StaticVariable.users.get(ipAddress).setInetAddress(s.getInetAddress().toString().substring(1));
								StaticVariable.users.get(ipAddress).setUsername(info[1]);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					break;
			}
			
		}
		// 跳出循环，表明该Socket对应的客户端已经关闭
		// 删除该Socket
		StaticVariable.quitSocket = s.getInetAddress().toString().substring(1);
		StaticVariable.users.remove(s.getInetAddress().toString().substring(1));
	}

	// 定义读取客户端数据的方法
	private String readFromClient() {
		try {
			return br.readLine();
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("客户端退出了。。。");
		}
		return null;
	}
	
	/**
	 * 向所有客户端发送消息
	 * @param msg
	 */
	public static void sendToAllClient(String msg) {
		// 遍历所有的已连接的客户端，将消息发送给所有的客户端
		for (Map.Entry<String, OnlineUser> user: StaticVariable.users.entrySet()) {
			try {
				System.out.println(msg);
				OnlineUser val = user.getValue();
                PrintWriter pw =val.getPw();  
                pw.println(msg);  
                pw.flush();  
            } catch (Exception e) {  
                e.printStackTrace();
            }
		}
	}
	
//	public static void sendToClient(ChoiceQuestion choice) {
//		String no = "1";
//		String question = "1111";
//		List<String> options = new ArrayList<>();
//		options.add("A.1");
//		options.add("B.2");
//		options.add("C.3");
//		options.add("D.4");
//		String answer = "A";
//		choice = new ChoiceQuestion(no, question, answer, options);
//		PrintWriter pw = null;
//		try {
//			pw = new PrintWriter(s.getOutputStream());
//		} catch (IOException e) {
//			e.printStackTrace();
//		};
//		if (pw != null) {
//			pw.println(choice);  
//			pw.flush();			
//		}
//	}
	
	/**
	 * 向客户端发送消息
	 * @param msg
	 */
	public void sendToClient(String msg, String ipAdress) {
        PrintWriter pw = null;
		try {
			pw = new PrintWriter(new OutputStreamWriter(StaticVariable.usersSocket.get(ipAdress).getOutputStream(),"utf-8"),true);
//			System.out.println(">>>" + userName);
//			System.out.println(">>>" + StaticVariable.usersSocket.size());
//			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(),"utf-8"),true);
//			System.out.println(StaticVariable.usersSocket.get(userName).getInetAddress());
//			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		};
		if (pw != null) {
			pw.println(msg);  
			pw.flush();	
//System.out.println("发送消息");
		}
	}
	
	// 获取连接的客户端的ip地址
	public static String getIpAddress() {
		return ipAddress;
	}
	
	public static String getUserName() {
		return userName;
	}
	
}

/**
 * 当客户端有消息发送过来时，执行线程，显示客户端的提问信息
 * @author wanli
 *
 */
class ListenningMessage implements Runnable {

	private String question;// 客户端提交的问题
	
	public ListenningMessage(String question) {
		this.question = question;
	}
	
	@Override
	public void run() {
		Display.getDefault().syncExec(new Runnable(){
			public void run() {
				// 在表格中添加一项
				TableItem item = new TableItem(StaticVariable.askQuestions, SWT.NONE);
				item.setImage(new Image(StaticVariable.parent.getDisplay(), "image/unanswer.png"));
				item.setText(question);
				item.setFont(new Font(StaticVariable.parent.getDisplay(), "Arial", 20, SWT.NONE));
				StaticVariable.unanswerMap.put(item, true);
				StaticVariable.askQuestion.setText(StaticVariable.unanswerMap.size() + " 提问");
				// 使用弹窗的方式提醒客户端有消息发送过来
				MessagePOP_UP window = new MessagePOP_UP();
				window.open();
			}	
		});
	}
	
}
