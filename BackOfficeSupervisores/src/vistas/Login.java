package vistas;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import database.ManejadorBD;
import vistas.shared.SharedInfo;

@SuppressWarnings("unused")
public class Login extends JFrame {
	public Login() {
	}

	private static JFrame frame;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		frame = new JFrame("Smart Alert Staff");
		JPanel panel = new JPanel();
		frame.setSize(300, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		ubicarComponentes(panel);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);	
		
	}
	
	/**
	 * Ubica los componentes en el frame
	 * @param panel
	 */
	private static void ubicarComponentes(JPanel panel) {

		panel.setLayout(null);

		JLabel userLabel = new JLabel("SA");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		JLabel passwordLabel = new JLabel("Contraseña");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);

		JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);

		JButton loginButton = new JButton("Ingresar");
		loginButton.setBounds(80, 80, 150, 25);
		panel.add(loginButton);
		

		loginButton.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  try {
	    		  ManejadorBD mbd = new ManejadorBD();
	    		  String sa = userText.getText();
	    		  String password = new String(passwordText.getPassword());
	    		  boolean login = mbd.validarUsuario(sa, password);
	    		  if(login) {
	    			  JOptionPane.showMessageDialog(null, "Login Correcto");
	    			 // Menu menu = new Menu();
	    			  SharedInfo.setUsername(sa);
	    			  Menu.main(new String[0]);
	    			  frame.dispose();
	    			  
	    		  }else {
	    			  JOptionPane.showInternalMessageDialog(null, "Login Incorrecto");
	    		  }
	    		  
	    	  } catch (Throwable e1) {
				System.out.println("ERROR ejecutando busqueda de recorrido debido a ["+e1.getMessage()+"]");
	    	  }
	      }
	    });
		
	}
	
	/***********************************************************************
	 ********************** METODOS PRIVADOS *******************************
	 ***********************************************************************/
	
	private boolean validarSA(String username, String password) {
		boolean resultado = false;
		try {
			
		}catch(Throwable t){
			
		}finally {
			
		}
		return resultado;
	}
}
