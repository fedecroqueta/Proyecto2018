package vistas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import database.ManejadorBD;
import modelos.Usuario;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class ModificacionAdmin {

	private JFrame frmModificaionAdministrador;
	private JTextField textField;
	private JTextField textFieldViejoMail;
	private JTextField textFieldNuevoMail;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModificacionAdmin window = new ModificacionAdmin();
					window.frmModificaionAdministrador.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ModificacionAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmModificaionAdministrador = new JFrame();
		frmModificaionAdministrador.setTitle("Modificacion Administrador");
		frmModificaionAdministrador.setBounds(100, 100, 450, 300);
		frmModificaionAdministrador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmModificaionAdministrador.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Modificacion Administradores");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(101, 12, 218, 14);
		frmModificaionAdministrador.getContentPane().add(lblNewLabel);
		
		JLabel lblAdministradores = new JLabel("Administradores");
		lblAdministradores.setBounds(10, 38, 133, 14);
		frmModificaionAdministrador.getContentPane().add(lblAdministradores);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(182, 36, 242, 20);
		List<Usuario> administradores = ManejadorBD.getAdministradores();
		List<String> usernames = new ArrayList<String>();
		for(int i=0; i< administradores.size(); i++ ) {
			String username = administradores.get(i).getUsername();
			usernames.add(username);
		}
		comboBox.setModel(new DefaultComboBoxModel(usernames.toArray()));
		
		
		JButton btnNewButton = new JButton("Confirmar Modificacion");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(10, 220, 210, 23);
		frmModificaionAdministrador.getContentPane().add(btnNewButton);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBackground(new Color(0, 206, 209));
		btnVolver.setBounds(335, 230, 89, 23);
		frmModificaionAdministrador.getContentPane().add(btnVolver);
		
		JLabel lblNuevoUsername = new JLabel("Nuevo username");
		lblNuevoUsername.setBounds(10, 64, 119, 14);
		frmModificaionAdministrador.getContentPane().add(lblNuevoUsername);
		
		textField = new JTextField();
		textField.setBounds(182, 58, 242, 20);
		frmModificaionAdministrador.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblAntiguoMail = new JLabel("Antiguo Mail");
		lblAntiguoMail.setBounds(10, 90, 89, 14);
		frmModificaionAdministrador.getContentPane().add(lblAntiguoMail);
		
		textFieldViejoMail = new JTextField();
		textFieldViejoMail.setEditable(false);
		textFieldViejoMail.setColumns(10);
		textFieldViejoMail.setBounds(182, 84, 242, 20);
		frmModificaionAdministrador.getContentPane().add(textFieldViejoMail);
		
		JLabel lblNuevo = new JLabel("Nuevo");
		lblNuevo.setBounds(122, 116, 46, 14);
		frmModificaionAdministrador.getContentPane().add(lblNuevo);
		
		textFieldNuevoMail = new JTextField();
		textFieldNuevoMail.setColumns(10);
		textFieldNuevoMail.setBounds(182, 110, 242, 20);
		frmModificaionAdministrador.getContentPane().add(textFieldNuevoMail);
		
		JLabel lblDarquitarPrivilegios = new JLabel("Dar/Quitar Privilegios");
		lblDarquitarPrivilegios.setBounds(10, 168, 119, 14);
		frmModificaionAdministrador.getContentPane().add(lblDarquitarPrivilegios);
		
		JCheckBox chckbxQuitar = new JCheckBox("Quitar");
		chckbxQuitar.setBounds(182, 159, 55, 23);
		frmModificaionAdministrador.getContentPane().add(chckbxQuitar);
		
		JCheckBox chckbxDar = new JCheckBox("Dar");
		chckbxDar.setBounds(266, 159, 97, 23);
		frmModificaionAdministrador.getContentPane().add(chckbxDar);
		
		JLabel lblCreaNodos = new JLabel("Crea Nodos?");
		lblCreaNodos.setBounds(10, 194, 97, 14);
		frmModificaionAdministrador.getContentPane().add(lblCreaNodos);
		
		JCheckBox chckbxSi = new JCheckBox("Si");
		chckbxSi.setBounds(182, 185, 55, 23);
		frmModificaionAdministrador.getContentPane().add(chckbxSi);
		
		JCheckBox chckbxNo = new JCheckBox("No");
		chckbxNo.setBounds(269, 185, 55, 23);
		frmModificaionAdministrador.getContentPane().add(chckbxNo);
		
		JLabel lblCambiarContrasea = new JLabel("Cambiar contrase\u00F1a?");
		lblCambiarContrasea.setBounds(10, 142, 119, 14);
		frmModificaionAdministrador.getContentPane().add(lblCambiarContrasea);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Si");
		chckbxNewCheckBox.setBounds(122, 137, 46, 23);
		frmModificaionAdministrador.getContentPane().add(chckbxNewCheckBox);
		
		JLabel lblVieja = new JLabel("Vieja");
		lblVieja.setBounds(182, 142, 46, 14);
		frmModificaionAdministrador.getContentPane().add(lblVieja);
		
		textField_1 = new JTextField();
		textField_1.setBounds(216, 136, 86, 20);
		frmModificaionAdministrador.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNueva = new JLabel("Nueva");
		lblNueva.setBounds(305, 142, 46, 14);
		frmModificaionAdministrador.getContentPane().add(lblNueva);
		
		textField_2 = new JTextField();
		textField_2.setBounds(338, 136, 86, 20);
		frmModificaionAdministrador.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                Object selected = comboBox.getSelectedItem();
                String username = selected.toString();
                
                Usuario usuario = ManejadorBD.getUsuario(username);
                textFieldViejoMail.setText(usuario.getMail());
                boolean privilegios = (usuario.getNivel_acceso()=="AP") ? true : false;
                if(privilegios) {
                	chckbxDar.setEnabled(false);
                }else {
                	chckbxQuitar.setEnabled(false);
                }
                boolean creaNodos = usuario.isCrea_nodos();
               
            }
        });
		frmModificaionAdministrador.getContentPane().add(comboBox);
	}

	
}
