package classes;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Attendance {
	
	public static String displayText = "";
	public static int inputID = -918273645;
	public Shell shlRobostangsAttendance;
	public Text IDInput;
	public static Label lblInfoDyn;
	public static boolean isConfirm = false;
	public static boolean end = false;
	
	/**
	 * Open the window.
	 */
	public void openGUI() {
		inputID = -918273645;
		isConfirm = false;
		Display display = Display.getDefault();
		createContents();
		shlRobostangsAttendance.open();
		shlRobostangsAttendance.layout();
		boolean stop = false;
		while (!stop) {
			//auto generated code
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			
			if (!Interface.getName(inputID).contentEquals("nameNotFound")) {
				if (isConfirm&&Interface.isSignedIn(inputID)) {
					setLabel("You have been signed out");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stop = true;
				} else if (isConfirm&&!Interface.isSignedIn(inputID)) {
					setLabel("You have been signed in");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					stop = true;
				}
			} else if (inputID != -918273645) {
				setLabel("The ID that you have input was not found, please try a different one");
			}
//			!shlRobostangsAttendance.isDisposed()
		}
		shlRobostangsAttendance.close();
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		shlRobostangsAttendance = new Shell(Display.getDefault(), SWT.RESIZE);
		org.eclipse.swt.graphics.Rectangle screenSize = Display.getDefault().getBounds();
		shlRobostangsAttendance.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		int defaultX = 450;
		int defaultY = 300;
		int centerX = screenSize.width / 2;
		int centerY = screenSize.height / 2;
		int offsetX = (defaultX / -2) + centerX;
		int offsetY = (defaultY / -2) + centerY;
		System.out.println(offsetX);
		System.out.println(offsetY);
		shlRobostangsAttendance.setSize(screenSize.width + 100, screenSize.height + 100);
		shlRobostangsAttendance.setLocation(-10, 0);
		shlRobostangsAttendance.setText("Robostangs Attendance");
		
		IDInput = new Text(shlRobostangsAttendance, SWT.BORDER);
		IDInput.setBackground(SWTResourceManager.getColor(255, 255, 255));
		IDInput.setBounds(174 + offsetX, 151 + offsetY, 75, 21);
		
		Label lblUsername = new Label(shlRobostangsAttendance, SWT.NONE);
		lblUsername.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblUsername.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblUsername.setForeground(SWTResourceManager.getColor(255, 140, 0));
		lblUsername.setBounds(29 + offsetX, 150 + offsetY, 85, 26);
		lblUsername.setText("ID Number:");
		
		Label lblRobostangsAttendance = new Label(shlRobostangsAttendance, SWT.NONE);
		lblRobostangsAttendance.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));
		lblRobostangsAttendance.setAlignment(SWT.CENTER);
		lblRobostangsAttendance.setBackground(SWTResourceManager.getColor(0, 0, 0));
		lblRobostangsAttendance.setForeground(SWTResourceManager.getColor(255, 140, 0));
		lblRobostangsAttendance.setBounds(120 + offsetX, 45 + offsetY, 194, 32);
		lblRobostangsAttendance.setText("Robostangs Attendance");
		
		Button btnSignIn = new Button(shlRobostangsAttendance, SWT.NONE);

		btnSignIn.setForeground(SWTResourceManager.getColor(255, 140, 0));
		btnSignIn.setBounds(174, 226, 75, 25);
		btnSignIn.setBounds(174 + offsetX, 226 + offsetY, 75, 25);
		btnSignIn.setText("Sign In / Out");
		
		Label lblInfoStatic = new Label(shlRobostangsAttendance, SWT.NONE);
		lblInfoStatic.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "\\robostangsmall.png"));
		lblInfoStatic.setBounds(10 + offsetX, 10 + offsetY, 104, 104);
		
		Label label = new Label(shlRobostangsAttendance, SWT.HORIZONTAL);
		label.setImage(SWTResourceManager.getImage(System.getProperty("user.dir") + "\\robostangsmall.png"));
		label.setBounds(320 + offsetX, 10 + offsetY, 104, 104);
		
		lblInfoDyn = new Label(shlRobostangsAttendance, SWT.NONE);
		lblInfoDyn.setForeground(SWTResourceManager.getColor(255, 140, 0));
		lblInfoDyn.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblInfoDyn.setBounds(offsetX - 50, 182 + offsetY, 540, 26);
		lblInfoDyn.setText(displayText);
		
		btnSignIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputID = Interface.getIntFromString(IDInput.getText());
				if (IDInput.getText().contentEquals("END")) {
					end = true;
					shlRobostangsAttendance.close();
				}
				System.out.println(IDInput.getText());
				isConfirm = true;
			}
		});
		IDInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println(e.keyCode);
				if(e.keyCode == 13) {
					inputID = Interface.getIntFromString(IDInput.getText());
					if (IDInput.getText().contentEquals("END")) {
						end = true;
						shlRobostangsAttendance.close();
					}
					System.out.println(IDInput.getText());
					isConfirm = true;
				}
				setLabel("Once you have typed in your ID, please press the button below or the button \"Enter\" on the keyboard.");
				
			}
		});
	}
	public static void setLabel(String text) {
		lblInfoDyn.setText(text);
	}
}
