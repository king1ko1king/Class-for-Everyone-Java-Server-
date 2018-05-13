package com.wanli.swing.frame.listener;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.wanli.swing.entities.OnlineUser;
import com.wanli.utils.StaticVariable;

/**
 * 为tree控件添加鼠标事件
 * @author wanli
 *
 */
public class OnlineTreeListener extends MouseAdapter{
	
	private Tree tree;				// 存储当前tree对象
	private Composite parent;		// 存储当前窗体的所有信息的对象
	private Menu menu = null;		// 右键菜单
	
	public OnlineTreeListener(Tree tree, Composite parent) {
		this.tree = tree;
		this.parent = parent;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent event) {
		
	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		treeMouseDown(event);
	}
	
	protected void treeMouseDown(MouseEvent event) {
		// 清除右键菜单，否则点击空白处也会弹出右键菜单
		if (menu != null) {
			menu.dispose();
		}
		TreeItem selected = tree.getItem(new Point(event.x, event.y));
		// 如果取到节点控件，且是鼠标右键
		if (selected != null && event.button == 3) {
			if (selected.getParentItem() != null) {// 如果不是根节点
				menu = new Menu(tree);// 为节点建POP UP菜单
		        MenuItem newItem = new MenuItem(menu, SWT.PUSH);
		        newItem.setText("发送即时信息");
		        MenuItem newMemberItem = new MenuItem(menu, SWT.PUSH);
		        newMemberItem.setText("查看资料");
		        MenuItem editItem = new MenuItem(menu, SWT.PUSH);
		        editItem.setText("编辑");
		        MenuItem deleteItem = new MenuItem(menu, SWT.PUSH);
		        deleteItem.setText("删除");
		        tree.setMenu(menu);
			} else if(selected.getParentItem() == null) {// 是根节点
				menu = new Menu(tree);// 为节点建POP UP菜单
				
				MenuItem saveStudent = new MenuItem(menu, SWT.PUSH);
				saveStudent.setText("将学生名单存库");
				saveStudent.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
				
				MenuItem callTheRoll = new MenuItem(menu, SWT.PUSH);
				callTheRoll.setText("考勤");
				callTheRoll.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});
				
				MenuItem sort = new MenuItem(menu, SWT.PUSH);
				sort.setText("排序");
				sort.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						TreeItem[] items = selected.getItems();
						for (TreeItem item: items) {
							item.dispose();
						}
						StaticVariable.users = sortMapByKey(StaticVariable.users);
						StaticVariable.onlineUsers.clear();
						for (Map.Entry<String, OnlineUser> user: StaticVariable.users.entrySet()) {
							try {
								TreeItem item = new TreeItem(selected, SWT.NONE);
								item.setText(user.getKey());
								StaticVariable.onlineUsers.put(user.getKey(), item);
				            } catch (Exception e) {  
				                e.printStackTrace();  
				            }
						}
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				MenuItem rename = new MenuItem(menu, SWT.PUSH);
				rename.setText("重命名");
				rename.addSelectionListener(new SelectionAdapter() {
					@Override
		        	public void widgetSelected(SelectionEvent e) {
						// 创建一个可编辑的TreeEditor对象
		        		final TreeEditor editor = new TreeEditor(tree);
		        		editor.horizontalAlignment = SWT.LEFT;
		        		editor.grabHorizontal = true;
		        		editor.minimumWidth = 30;
		        		// 释放之前编辑的控件
		        		Control oldEditor = editor.getEditor();
		        		if (oldEditor != null) {
		        			oldEditor.dispose();
		        		}
		        		// 创建一个文本框，作为编辑节点时输入的文字
		        		Text newEditor = new Text(tree, SWT.NONE);
		        		// 将树节点的值赋值给文本框
		        		newEditor.setText(selected.getText());
		        		// 当文本框的值改变时，也相应的把值赋值给树节点
		        		newEditor.addModifyListener(new ModifyListener() {
							
							@Override
							public void modifyText(ModifyEvent arg0) {
								Text text = (Text) editor.getEditor();
								editor.getItem().setText(text.getText());
							}
						});
		        		newEditor.selectAll();		// 选择所有文本框
		        		newEditor.setFocus();		// 并将焦点设置为该文本框
		        		// 将树节点与文本框节点绑定
		        		editor.setEditor(newEditor, selected);
		        		// 为文本框添加键盘事件
		        		newEditor.addListener(SWT.KeyDown, new Listener() {
							
							@Override
							public void handleEvent(Event event) {
								// 当按下Enter键时，把文本框的值赋值给树节点，并且使文本框失效
								if (event.keyCode == SWT.CR) {
									selected.setText(newEditor.getText());
									newEditor.dispose();
								}
							}
						});
		        	}
				});
		        MenuItem deleteClass = new MenuItem(menu, SWT.PUSH);
		        deleteClass.setText("删除教室");
		        deleteClass.addSelectionListener(new SelectionAdapter() {
		        	
		        	@Override
		        	public void widgetSelected(SelectionEvent e) {
		        		MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO);
		        		messageBox.setText("删除教室");
		        		messageBox.setMessage("确定要删除这个教室吗？");
		        		if (messageBox.open() == SWT.YES) {
		        			StaticVariable.rooms.remove(selected);
		        			System.out.println(StaticVariable.rooms.size());
			        		selected.dispose();
		        		}
		        	}
		        	
		        });
		        tree.setMenu(menu);
			}
		}
		
	}
	
	public static Map<String, OnlineUser> sortMapByKey(Map<String, OnlineUser> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, OnlineUser> sortMap = new TreeMap<String, OnlineUser>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
	
}

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
