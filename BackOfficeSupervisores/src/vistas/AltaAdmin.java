package vistas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTextField;

import database.ManejadorBD;
import modelos.Usuario;
import vistas.shared.SharedInfo;
import javax.swing.JPasswordField;
import java.awt.Color;

public class AltaAdmin {

	private JFrame frmAltaAdmin;
	private JTextField textFieldNombre;
	private JTextField textFieldMail;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldConfirmar;
	
	ManejadorBD mbd = new ManejadorBD();
	private JTextField textFieldCelular;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AltaAdmin window = new AltaAdmin();
					window.frmAltaAdmin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AltaAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAltaAdmin = new JFrame();
		frmAltaAdmin.setTitle("Alta Administrador");
		frmAltaAdmin.setBounds(100, 100, 450, 300);
		frmAltaAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAltaAdmin.getContentPane().setLayout(null);
		frmAltaAdmin.setLocationRelativeTo(null);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNombre.setBounds(10, 41, 69, 14);
		frmAltaAdmin.getContentPane().add(lblNombre);
		
		JLabel lblMail = new JLabel("Mail");
		lblMail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMail.setBounds(10, 66, 46, 14);
		frmAltaAdmin.getContentPane().add(lblMail);
		
		JLabel lblUsername = new JLabel("Username*");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsername.setBounds(10, 110, 94, 14);
		frmAltaAdmin.getContentPane().add(lblUsername);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a");
		lblContrasea.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblContrasea.setBounds(10, 135, 94, 14);
		frmAltaAdmin.getContentPane().add(lblContrasea);
		
		JLabel lblConfirmarContrasea = new JLabel("Confirmar Contrase\u00F1a");
		lblConfirmarContrasea.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblConfirmarContrasea.setBounds(10, 160, 165, 14);
		frmAltaAdmin.getContentPane().add(lblConfirmarContrasea);
		
		JLabel lblDaDeAlta = new JLabel("Da de Alta Nodos?");
		lblDaDeAlta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDaDeAlta.setBounds(10, 205, 154, 14);
		frmAltaAdmin.getContentPane().add(lblDaDeAlta);
		
		JCheckBox chckbxNodosSI = new JCheckBox("Si");
		JCheckBox chckbxNodosNO = new JCheckBox("No");
		chckbxNodosSI.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxNodosSI.setBounds(288, 201, 46, 23);
		frmAltaAdmin.getContentPane().add(chckbxNodosSI);
		chckbxNodosSI.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
		    {
				chckbxNodosNO.setSelected(false);
		     }
	    });
		
		chckbxNodosNO.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxNodosNO.setBounds(357, 201, 54, 23);
		frmAltaAdmin.getContentPane().add(chckbxNodosNO);
		chckbxNodosNO.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
		    {
				chckbxNodosSI.setSelected(false);
		     }
	    });
		
		JLabel lblDaDeAlta_1 = new JLabel("Administrador con Privilegios?");
		lblDaDeAlta_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDaDeAlta_1.setBounds(10, 180, 228, 14);
		frmAltaAdmin.getContentPane().add(lblDaDeAlta_1);
		
		JCheckBox checkBoxPrivilegios = new JCheckBox("Si");
		JCheckBox chckbxPrivilegiosNO = new JCheckBox("No");
		checkBoxPrivilegios.setFont(new Font("Tahoma", Font.PLAIN, 16));
		checkBoxPrivilegios.setBounds(288, 175, 46, 23);
		frmAltaAdmin.getContentPane().add(checkBoxPrivilegios);
		checkBoxPrivilegios.addActionListener(new ActionListener()
	    {
			public void actionPerformed(ActionEvent e)
		    {
		    	chckbxNodosSI.setEnabled(false);
		    	chckbxNodosNO.setEnabled(false);
		    	chckbxNodosSI.setSelected(false);
		    	chckbxNodosNO.setSelected(false);
		    	chckbxPrivilegiosNO.setSelected(false);
		     }
	    });
		
		
		chckbxPrivilegiosNO.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxPrivilegiosNO.setBounds(357, 175, 46, 23);
		frmAltaAdmin.getContentPane().add(chckbxPrivilegiosNO);
		chckbxPrivilegiosNO.addActionListener(new ActionListener()
	    {public void actionPerformed(ActionEvent e)
	      {
	    	chckbxNodosSI.setEnabled(true);
	    	chckbxNodosNO.setEnabled(true);
	    	checkBoxPrivilegios.setSelected(false);
	      }
	    });
		
		JButton btnConfirmarAlta = new JButton("Confirmar ALTA");
		btnConfirmarAlta.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnConfirmarAlta.setBounds(10, 230, 165, 23);
		frmAltaAdmin.getContentPane().add(btnConfirmarAlta);
		btnConfirmarAlta.addActionListener(new ActionListener()
	    {
	   
		public void actionPerformed(ActionEvent e)
	      {
	    	  try {
	    		  String password = new String(passwordField.getPassword());
	    		  String confirmarPass = new String(passwordFieldConfirmar.getPassword());
	    		  String username =  new String(textFieldUsername.getText());
	    		  String mail =  new String(textFieldMail.getText());
	    		  boolean result = false;
	    		  boolean usernameUnico = false;
	    		  comprobarDatos(password, confirmarPass,username, mail );
	    		  usernameUnico = mbd.validarUsernameUnico(username);
	    		  if(password.equals(confirmarPass) && usernameUnico ) {
	    			  Usuario u = new Usuario(); 
	    			  u.setUsername(textFieldUsername.getText());
	    			  u.setMail(textFieldMail.getText());
	    			  u.setNumero_celular(textFieldCelular.getText());
	    			  u.setPassword(password);
	    			  //Admin con privilegios puede crear otros admin
	    			  if(checkBoxPrivilegios.isSelected()) {
	    				  u.setNivel_acceso("AP");
	    				  u.setCrea_nodos(true);
	    			  }else if(chckbxNodosSI.isSelected()){ //Admin solo crea nodos
	    				  u.setNivel_acceso("AN");
	    				  u.setCrea_nodos(true);
	    			  }else { //Admin solo crea usuarios
	    				  u.setNivel_acceso("A");
	    				  u.setCrea_nodos(false);
	    			  }
	    			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    			  String now = sdf.format(new Date());
	    			  u.setFecha_alta(now);
	    			  result = mbd.altaAdmin(u);
	    			  if(result) {
	    				  JOptionPane.showMessageDialog(frmAltaAdmin,"Administrador ["+u.getUsername()+"] dado de Alta Correctamente");
	    				  Menu.main(new String[0]);
	    				  frmAltaAdmin.dispose();
	    			  }else {
	    				  JOptionPane.showInternalMessageDialog(frmAltaAdmin, " No se pudo dar de alta el Administrador ["+u.getUsername()+"]!!");
	    				  return;
	    			  }
	    		  }else if(!password.equals(confirmarPass)) {
	    			  JOptionPane.showMessageDialog(frmAltaAdmin,"Contraseñas no coinciden!!");
	    			  return ;  
	    		  }else {
	    			  JOptionPane.showMessageDialog(frmAltaAdmin,"Username debe ser unico *");
	    			  return ;
	    		  }
	    	  } catch (Throwable e1) {
				System.out.println("ERROR dando de Alta Administrados debido a ["+e1.getMessage()+"]");
	    	  }
	      }
	    });
		
		JLabel lblCompletesLosDatos = new JLabel("Complete los datos ");
		lblCompletesLosDatos.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblCompletesLosDatos.setBounds(110, 11, 208, 23);
		frmAltaAdmin.getContentPane().add(lblCompletesLosDatos);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(183, 35, 228, 20);
		frmAltaAdmin.getContentPane().add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		textFieldMail = new JTextField();
		textFieldMail.setBounds(183, 60, 228, 20);
		frmAltaAdmin.getContentPane().add(textFieldMail);
		textFieldMail.setColumns(10);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(183, 104, 228, 20);
		frmAltaAdmin.getContentPane().add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(183, 129, 228, 20);
		frmAltaAdmin.getContentPane().add(passwordField);
		
		passwordFieldConfirmar = new JPasswordField();
		passwordFieldConfirmar.setBounds(183, 154, 228, 20);
		frmAltaAdmin.getContentPane().add(passwordFieldConfirmar);
		
		JButton btnNewButton = new JButton("Volver");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Menu.main(new String[0]);
				frmAltaAdmin.dispose();
			}
		});
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.setBackground(new Color(184, 134, 11));
		btnNewButton.setBounds(335, 230, 89, 23);
		frmAltaAdmin.getContentPane().add(btnNewButton);
		
		JLabel lblCelular = new JLabel("Celular");
		lblCelular.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCelular.setBounds(10, 88, 69, 14);
		frmAltaAdmin.getContentPane().add(lblCelular);
		
		textFieldCelular = new JTextField();
		textFieldCelular.setBounds(183, 82, 228, 20);
		frmAltaAdmin.getContentPane().add(textFieldCelular);
		textFieldCelular.setColumns(10);
	}
	
	private void comprobarDatos(String pass, String conPass, String username, String mail) {
		if(pass.equals("") ) {
			  JOptionPane.showMessageDialog(frmAltaAdmin,"Debe ingresar Contraseña");
			  return ;
		  }
		  if(conPass.equals("")) {
			  JOptionPane.showMessageDialog(frmAltaAdmin,"Debe Ingresar  Confirmar Contraseña");
			  return ;
		  }
		  if(mail.equals("")) {
			  JOptionPane.showMessageDialog(frmAltaAdmin,"Debe Ingresar Mail");
			  return ; 
		  }
		  if(username.equals("")) {
			  JOptionPane.showMessageDialog(frmAltaAdmin,"Debe Ingresar Username");
			  return ;
		  }
	}
}
