package vistas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import database.ManejadorBD;
import vistas.shared.SharedInfo;

@SuppressWarnings("unused")
public class Menu extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setAutoRequestFocus(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);	
		
		JLabel labelUsuarioLogeado = new JLabel("SA Logeado >> ["+SharedInfo.getUsername()+"]");
		labelUsuarioLogeado.setBounds(10, 11, 397, 14);
		frame.getContentPane().add(labelUsuarioLogeado);
		
		JButton btnAltaAdministrador = new JButton("Alta Administrador");
		btnAltaAdministrador.setBounds(31, 37, 376, 44);
		btnAltaAdministrador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 AltaAdmin.main(new String[0]);
		    	    frame.dispose();
			}
		});
		frame.getContentPane().add(btnAltaAdministrador);
		
		JButton btnModif = new JButton("Modificacion Admin");
		btnModif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 ModificacionAdmin.main(new String[0]);
		    	    frame.dispose();
			}
		});
		btnModif.setBounds(31, 92, 376, 44);
		frame.getContentPane().add(btnModif);
		
		JButton btnBajaAdmin = new JButton("Baja Admin");
		btnBajaAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 BajaAdmin.main(new String[0]);
		    	    frame.dispose();
			}
		});
		btnBajaAdmin.setBounds(31, 147, 376, 44);
		frame.getContentPane().add(btnBajaAdmin);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.setBounds(172, 228, 89, 23);
		frame.getContentPane().add(btnSalir);
		
		btnSalir.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  int dialogResult = JOptionPane.showConfirmDialog (null, "Seguro desea cerrar sesion?","Warning", 1);
	    	  if(dialogResult == JOptionPane.YES_OPTION){
	    	    frame.dispose();
	    	  }
			  
	      }
	    });
		
	}
}
